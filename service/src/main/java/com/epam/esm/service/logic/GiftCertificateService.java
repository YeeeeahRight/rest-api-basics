package com.epam.esm.service.logic;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {
    long create(GiftCertificateDto giftCertificate);

    List<GiftCertificate> getAll();

    List<GiftCertificateDto> getAllWithTags(String tagName, String partInfo,
                                            List<String> sortColumns, List<String> orderTypes);

    GiftCertificate getById(long id);

    GiftCertificateDto updateById(long id, GiftCertificateDto giftCertificate);

    void deleteById(long id);
}
