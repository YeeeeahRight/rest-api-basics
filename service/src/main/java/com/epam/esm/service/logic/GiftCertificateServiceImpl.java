package com.epam.esm.service.logic;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.SortParamsContext;
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
    private final Validator<SortParamsContext> sortParametersValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      Validator<GiftCertificate> giftCertificateValidator,
                                      Validator<Tag> tagEntityValidator,
                                      Validator<SortParamsContext> sortParametersValidator) {
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
            throw new DuplicateEntityException("certificate.already.exist");
        }
        giftCertificateDao.create(giftCertificate);
        long certificateId = giftCertificateDao.findByName(certificateName)
                .map(GiftCertificate::getId).orElse(-1L);
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
        return tagDao.findByName(tag.getName()).get();
    }

    @Override
    public List<GiftCertificate> getAll() {
        return giftCertificateDao.getAll();
    }

    @Override
    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(id);
        if (!certificateOptional.isPresent()) {
            throw new NoSuchEntityException("certificate.not.found");
        }
        return certificateOptional.get();
    }

    @Override
    @Transactional
    public List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                                   List<String> sortColumns, List<String> orderTypes) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificates;
        boolean isSortExist = sortColumns != null;
        if (isSortExist) {
            SortParamsContext sortParameters = new SortParamsContext(sortColumns, orderTypes);
            validateSortParams(sortParameters);
            if (isFilterExist(tagName, partInfo)) {
                giftCertificates = getCertificatesWithSortingAndFiltering(tagName, partInfo, sortParameters);
            } else {
                giftCertificates = giftCertificateDao.getAllWithSorting(sortParameters);
            }
        } else if (isFilterExist(tagName, partInfo)) {
            giftCertificates = getCertificatesWithFiltering(tagName, partInfo);
        } else {
            giftCertificates = giftCertificateDao.getAll();
        }
        for (GiftCertificate giftCertificate : giftCertificates) {
            giftCertificateDtoList.add(buildGiftCertificateDto(giftCertificate));
        }
        return giftCertificateDtoList;
    }

    private boolean isFilterExist(String tagName, String partInfo) {
        return tagName != null || partInfo != null;
    }

    private List<GiftCertificate> getCertificatesWithSortingAndFiltering
            (String tagName, String partInfo, SortParamsContext sortParameters) {
        List<Long> certificateIdsByTagName = null;
        if (tagName != null) {
            certificateIdsByTagName = findCertificateIdsByTagName(tagName);
        }
        return giftCertificateDao.getAllWithSortingFiltering(sortParameters,
                certificateIdsByTagName, partInfo);
    }

    private List<GiftCertificate> getCertificatesWithFiltering(String tagName, String partInfo) {
        List<Long> certificateIdsByTagName = null;
        if (tagName != null) {
            certificateIdsByTagName = findCertificateIdsByTagName(tagName);
        }
        return giftCertificateDao.getAllWithFilter(certificateIdsByTagName, partInfo);
    }

    private List<Long> findCertificateIdsByTagName(String tagName) {
        Optional<Tag> tag = tagDao.findByName(tagName);
        if (!tag.isPresent()) {
            throw new NoSuchEntityException("tag.not.found");
        }
        long tagId = tag.get().getId();
        return giftCertificateDao.getCertificateIdsByTagId(tagId);
    }

    @Override
    @Transactional
    public GiftCertificateDto updateById(long id, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        if (giftCertificate != null) {
            if (!giftCertificateDao.findById(id).isPresent()) {
                throw new NoSuchEntityException("certificate.not.found");
            }
            giftCertificateDao.updateById(id, findUpdateInfo(giftCertificate));
        }
        Set<Tag> tags = giftCertificateDto.getCertificateTags();
        if (tags != null) {
            updateCertificateTags(tags, id);
        }
        return buildGiftCertificateDto(giftCertificateDao.findById(id).get());
    }

    private Map<String, Object> findUpdateInfo(GiftCertificate giftCertificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        GiftCertificateValidator giftCertificateValidator =
                (GiftCertificateValidator) this.giftCertificateValidator;
        String name = giftCertificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityException("certificate.name.invalid");
            }
            updateInfo.put("name", name);
        }
        String description = giftCertificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityException("certificate.description.invalid");
            }
            updateInfo.put("description", description);
        }
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityException("certificate.price.invalid");
            }
            updateInfo.put("price", price);
        }
        int duration = giftCertificate.getDuration();
        if (duration != 0) {
            if (!giftCertificateValidator.isDurationValid(duration)) {
                throw new InvalidEntityException("certificate.duration.invalid");
            }
            updateInfo.put("duration", duration);
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

    private GiftCertificateDto buildGiftCertificateDto(GiftCertificate giftCertificate) {
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
            throw new NoSuchEntityException("certificate.not.found");
        }
        giftCertificateDao.deleteById(id);
    }

    private void validateCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("certificate.invalid");
        }
    }

    private void validateTags(Set<Tag> tags) {
        boolean isCorrectTags = tags.stream().allMatch(tagEntityValidator::isValid);
        if (!isCorrectTags) {
            throw new InvalidEntityException("tag.invalid");
        }
    }

    private void validateSortParams(SortParamsContext sortParameters) {
        if (!sortParametersValidator.isValid(sortParameters)) {
            throw new InvalidParametersException("sort.params.invalid");
        }
    }
}
