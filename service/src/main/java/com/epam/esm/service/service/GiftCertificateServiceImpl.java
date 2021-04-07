package com.epam.esm.service.service;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.CreationEntityException;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final EntityValidator<GiftCertificate> giftCertificateValidator;
    private final EntityValidator<Tag> tagEntityValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      EntityValidator<GiftCertificate> giftCertificateValidator,
                                      EntityValidator<Tag> tagEntityValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagEntityValidator = tagEntityValidator;
    }

    @Override
    @Transactional
    public long create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        validateCertificate(giftCertificate);
        validateTags(giftCertificateDto.getCertificateTags());
        String certificateName = giftCertificate.getName();
        boolean isCertificateExist = giftCertificateDao.findByName(certificateName).isPresent();
        if (isCertificateExist) {
            throw new DuplicateEntityException("Certificate with name=" +
                    certificateName + " is already exist.");
        }
        giftCertificateDao.create(giftCertificate);
        Optional<GiftCertificate> optionalCertificate = giftCertificateDao.findByName(certificateName);
        if (!optionalCertificate.isPresent()) {
            throw new CreationEntityException(
                    "Gift certificate was not created. Something went wrong.");
        }
        long certificateId = optionalCertificate.get().getId();
        createCertificateTagsWithReference(giftCertificateDto.getCertificateTags(), certificateId);
        return certificateId;
    }

    private void createCertificateTagsWithReference(Set<Tag> tags, long certificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
            Tag fullTag = tagOptional.orElseGet(() -> createCertificateTag(tag));
            long tagId = fullTag.getId();
            giftCertificateDao.createCertificateTagReference(certificateId, tagId);
        }
    }

    private Tag createCertificateTag(Tag tag) {
        tagDao.create(tag);
        Optional<Tag> tagOptional = tagDao.findByName(tag.getName());
        if (!tagOptional.isPresent()) {
            throw new CreationEntityException("" +
                    "Tag with name=" + tag.getName() + " was not created. Something went wrong.");
        }
        return tagOptional.get();
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateDao.getAll();
    }

    @Override
    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("No gift certificate with id=" + id + ".");
        }
        return certificateOptional.get();
    }

    @Override
    public List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                                   List<String> sortTypes) {
        return null;
    }

    @Override
    @Transactional
    public GiftCertificateDto updateById(long id, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        if (giftCertificate != null) {
            if (!giftCertificateDao.findById(id).isPresent()) {
                throw new NoSuchEntityException("No gift certificate with id=" + id + ".");
            }
            giftCertificateDao.updateById(id, findUpdateInfo(giftCertificate));
        }
        Set<Tag> tags = giftCertificateDto.getCertificateTags();
        if (tags != null) {
            updateCertificateTags(tags, id);
        }
        return buildGiftCertificateDto(id);
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate giftCertificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = giftCertificate.getName();
        if (giftCertificateValidator.isNameValid(name)) {
            updateInfo.put("name", name);
        } else if (name != null) {
            throw new InvalidEntityException("Gift certificate name is not valid.");
        }
        String description = giftCertificate.getDescription();
        if (giftCertificateValidator.isDescriptionValid(description)) {
            updateInfo.put("description", description);
        } else if (description != null) {
            throw new InvalidEntityException("Gift certificate description is not valid.");
        }
        BigDecimal price = giftCertificate.getPrice();
        if (giftCertificateValidator.isPriceValid(price)) {
            updateInfo.put("price", price);
        } else if (price != null) {
            throw new InvalidEntityException("Gift certificate price is not valid.");
        }
        int duration = giftCertificate.getDuration();
        if (giftCertificateValidator.isDurationValid(duration)) {
            updateInfo.put("duration", duration);
        } else if (duration != 0) {
            throw new InvalidEntityException("Gift certificate duration is not valid.");
        }
        return updateInfo;
    }

    private void updateCertificateTags(Set<Tag> tags, long certificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
            Tag fullTag = tagOptional.orElseGet(() -> createCertificateTag(tag));
            long tagId = fullTag.getId();
            if (!giftCertificateDao.getCertificateTagIds(certificateId).contains(tagId)) {
                giftCertificateDao.createCertificateTagReference(certificateId, tagId);
            }
        }
    }

    private GiftCertificateDto buildGiftCertificateDto(long certificateId) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(certificateId);
        if (!giftCertificateOptional.isPresent()) {
            throw new NoSuchEntityException("Certificate with id=" +
                    certificateId + " not found");
        }
        GiftCertificate giftCertificate = giftCertificateOptional.get();
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
        HashSet<Optional<Tag>> optionalTags = new HashSet<>();
        List<Long> tagIds = giftCertificateDao.getCertificateTagIds(giftCertificate.getId());
        tagIds.forEach(id -> optionalTags.add(tagDao.findById(id)));
        optionalTags.stream()
                .filter(Optional::isPresent)
                .forEach(tag -> giftCertificateDto.addTag(tag.get()));
        System.out.println(giftCertificateDto);
        return giftCertificateDto;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("No gift certificate with id=" + id + ".");
        }
        giftCertificateDao.deleteById(id);
    }

    private void validateCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("Invalid gift certificate data.");
        }
    }

    private void validateTags(Set<Tag> tags) {
        boolean isCorrectTags = tags.stream().allMatch(tagEntityValidator::isValid);
        if (!isCorrectTags) {
            throw new InvalidEntityException("Invalid tags data.");
        }
    }
}
