package com.epam.esm.service.service;

import com.epam.esm.persistence.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {
    long create(GiftCertificate giftCertificate);

    List<GiftCertificate> getAll();

    List<GiftCertificate> search(String tagName, String partInfo, List<String> sortTypes);

    GiftCertificate getById(long id);

    GiftCertificate updateById(long id, GiftCertificate giftCertificate);

    void deleteById(long id);
}
