package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.query.SortParameters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {
    void create(GiftCertificate giftCertificate);

    void createCertificateTagReference(long certificateId, long tagId);

    List<GiftCertificate> getAll();

    List<GiftCertificate> getAllWithSorting(SortParameters sortParameters);

    List<GiftCertificate> getAllWithFilter(List<Long> ids, String partInfo);

    List<GiftCertificate> getAllWithSortingFiltering(SortParameters sortParameters,
                                                     List<Long> ids, String partInfo);

    List<Long> getTagIdsByCertificateId(long certificateId);

    List<Long> getCertificateIdsByTagId(long tagId);

    void updateById(long id, Map<String, Object> giftCertificateUpdateInfo);

    Optional<GiftCertificate> findById(long id);

    Optional<GiftCertificate> findByName(String name);

    void deleteById(long id);
}
