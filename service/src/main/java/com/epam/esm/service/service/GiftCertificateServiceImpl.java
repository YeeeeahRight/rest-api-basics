package com.epam.esm.service.service;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final EntityValidator<GiftCertificate> giftCertificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      EntityValidator<GiftCertificate> giftCertificateValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Override
    @Transactional
    public long create(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("Invalid gift certificate data.");
        }
        String certificateName = giftCertificate.getName();
        boolean isCertificateExist = giftCertificateDao.findByName(certificateName).isPresent();
        if (isCertificateExist) {
            throw new DuplicateEntityException("Certificate with name=" +
                    certificateName + " is already exist.");
        }
        giftCertificateDao.create(giftCertificate);
        Optional<GiftCertificate> optionalCertificate = giftCertificateDao.findByName(certificateName);
        return optionalCertificate.map(GiftCertificate::getId).orElse(-1L);
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
        if (!giftCertificateDao.findById(id).isPresent()) {
            throw new NoSuchEntityException("No gift certificate with id=" + id + ".");
        }
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("Invalid gift certificate data.");
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
}
