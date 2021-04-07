package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {
    void create(GiftCertificate giftCertificate);

    List<GiftCertificate> getAll();

    void updateById(long id, GiftCertificate giftCertificate);

    Optional<GiftCertificate> findById(long id);

    Optional<GiftCertificate> findByName(String name);

    void deleteById(long id);
}
