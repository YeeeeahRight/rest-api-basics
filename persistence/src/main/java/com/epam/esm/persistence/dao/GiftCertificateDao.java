package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {
    void create(GiftCertificate giftCertificate);

    void createCertificateTagReference(long certificateId, long tagId);

    List<GiftCertificate> getAll();

    List<Long> getCertificateTagIds(long certificateId);

    void updateById(long id, Map<String, Object> giftCertificateUpdateInfo);

    Optional<GiftCertificate> findById(long id);

    Optional<GiftCertificate> findByName(String name);

    void deleteById(long id);
}
