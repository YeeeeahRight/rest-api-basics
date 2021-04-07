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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        validateTags(giftCertificateDto.getTags());
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
        createCertificateTags(giftCertificateDto.getTags(), certificateId);
        return certificateId;
    }

    private void createCertificateTags(Set<Tag> tags, long certificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.findByName(tagName);
            if (!tagOptional.isPresent()) {
                tagDao.create(tag);
                tagOptional = tagDao.findByName(tagName);
                if (!tagOptional.isPresent()) {
                    throw new CreationEntityException("" +
                            "Tag with name=" + tagName + " was not created. Something went wrong.");
                }
            }
            long tagId = tagOptional.get().getId();
            giftCertificateDao.createCertificateTagReference(certificateId, tagId);
        }
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
    public List<GiftCertificate> search(String tagName, String partInfo, List<String> sortTypes) {
        return null;
    }

    @Override
    @Transactional
    public GiftCertificate updateById(long id, GiftCertificate giftCertificate) {
        validateCertificate(giftCertificate);
        if (!giftCertificateDao.findById(id).isPresent()) {
            throw new NoSuchEntityException("No gift certificate with id=" + id + ".");
        }
        giftCertificateDao.updateById(id, giftCertificate);
        return giftCertificate;
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
