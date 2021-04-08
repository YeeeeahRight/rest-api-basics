package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.SortParameters;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.*;
import com.epam.esm.service.validator.Validator;
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
    private final Validator<GiftCertificate> giftCertificateValidator;
    private final Validator<Tag> tagEntityValidator;
    private final Validator<SortParameters> sortParametersValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<Tag> tagEntityValidator,
                                      Validator<SortParameters> sortParametersValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagEntityValidator = tagEntityValidator;
        this.sortParametersValidator = sortParametersValidator;
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
    @Transactional
    public List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                                   List<String> sortColumns, List<String> orderTypes) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        List<Long> giftCertificateIds;
        boolean isSortExist = sortColumns != null;
        if (isSortExist) {
            giftCertificateIds = findSortedCertificateIds(tagName, partInfo, sortColumns, orderTypes);
        } else if (isFilterExist(tagName, partInfo)) {
            giftCertificateIds = findCertificateIdsWithFiltering(tagName, partInfo);
        } else {
            giftCertificateIds = convertToIds(giftCertificateDao.getAll());
        }
        for (long certificateId : giftCertificateIds) {
            giftCertificateDtoList.add(buildGiftCertificateDto(certificateId));
        }
        return giftCertificateDtoList;
    }

    private boolean isFilterExist(String tagName, String partInfo) {
        return tagName != null || partInfo != null;
    }

    private List<Long> findSortedCertificateIds(String tagName, String partInfo,
                                                List<String> sortColumns, List<String> orderTypes) {
        SortParameters sortParameters = new SortParameters(sortColumns, orderTypes);
        validateSortParams(sortParameters);
        List<Long> giftCertificateIds;
        if (isFilterExist(tagName, partInfo)) {
            List<Long> certificateIdsByTagName = null;
            if (tagName != null) {
                certificateIdsByTagName = findCertificateIdsByTagName(tagName);
            }
            giftCertificateIds = convertToIds(
                    giftCertificateDao.getAllWithSortingFiltering(sortParameters,
                            certificateIdsByTagName, partInfo));
        } else {
            giftCertificateIds = convertToIds(
                    giftCertificateDao.getAllWithSorting(sortParameters));
        }
        return giftCertificateIds;
    }

    private List<Long> findCertificateIdsWithFiltering(String tagName, String partInfo) {
        List<Long> certificateIdsByTagName = null;
        if (tagName != null) {
            certificateIdsByTagName = findCertificateIdsByTagName(tagName);
        }
        return convertToIds(giftCertificateDao.getAllWithFilter(certificateIdsByTagName, partInfo));
    }

    private List<Long> findCertificateIdsByTagName(String tagName) {
        Optional<Tag> tag = tagDao.findByName(tagName);
        if (!tag.isPresent()) {
            throw new NoSuchEntityException("No tag with name=" + tagName);
        }
        long tagId = tag.get().getId();
        return giftCertificateDao.getCertificateIdsByTagId(tagId);
    }

    private List<Long> convertToIds(List<GiftCertificate> giftCertificates) {
        List<Long> giftCertificateIds = new ArrayList<>();
        giftCertificates.forEach(giftCertificate -> giftCertificateIds.add(giftCertificate.getId()));
        return giftCertificateIds;
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
            if (!giftCertificateDao.getTagIdsByCertificateId(certificateId).contains(tagId)) {
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
        List<Long> tagIds = giftCertificateDao.getTagIdsByCertificateId(giftCertificate.getId());
        tagIds.forEach(id -> optionalTags.add(tagDao.findById(id)));
        optionalTags.stream()
                .filter(Optional::isPresent)
                .forEach(tag -> giftCertificateDto.addTag(tag.get()));
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

    private void validateSortParams(SortParameters sortParameters) {
        if (!sortParametersValidator.isValid(sortParameters)) {
            throw new InvalidParametersException("Invalid sort parameters.");
        }
    }
}
