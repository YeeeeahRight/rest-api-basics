package com.epam.esm.persistence.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.persistence.config.TestJdbcConfig;
import com.epam.esm.persistence.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.SortParamsContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@Component
@ContextConfiguration(classes = {TestJdbcConfig.class})
@Transactional
public class GiftCertificateDaoImplTest {
    private static final GiftCertificate CERTIFICATE_TO_CREATE = new GiftCertificate(
            4L, "certificate new", "description new", new BigDecimal("1.10"),
            LocalDateTime.parse("2020-01-01T01:11:11").atZone(ZoneId.of("GMT+3")),
            LocalDateTime.parse("2021-01-01T01:22:11").atZone(ZoneId.of("GMT+3")), 1);
    private static final Tag FIRST_TAG = new Tag(1L, "tag 1");
    private static final Tag SECOND_TAG = new Tag(2L, "tag 2");
    private static final Tag THIRD_TAG = new Tag(3L, "tag 3");
    private static final List<Tag> FIRST_CERTIFICATE_TAGS = Arrays.asList(FIRST_TAG, THIRD_TAG);
    private static final GiftCertificate FIRST_CERTIFICATE = new GiftCertificate(
            1L, "certificate 1", "description 1", new BigDecimal("1.10"),
            LocalDateTime.parse("2020-01-01T01:11:11").atZone(ZoneId.of("GMT+3")),
            LocalDateTime.parse("2021-01-01T01:22:11").atZone(ZoneId.of("GMT+3")), 1);
    private static final GiftCertificate SECOND_CERTIFICATE = new GiftCertificate(
            2L, "certificate 2", "description 2", new BigDecimal("2.20"),
            LocalDateTime.parse("2020-02-02T02:22:22").atZone(ZoneId.of("GMT+3")),
            LocalDateTime.parse("2021-02-02T02:33:22").atZone(ZoneId.of("GMT+3")), 2);
    private static final GiftCertificate THIRD_CERTIFICATE = new GiftCertificate(
            3L, "certificate 3", "description 3", new BigDecimal("3.30"),
            LocalDateTime.parse("2020-03-03T03:33:33").atZone(ZoneId.of("GMT+3")),
            LocalDateTime.parse("2021-03-03T03:44:33").atZone(ZoneId.of("GMT+3")), 3);

    @Autowired
    private final GiftCertificateDaoImpl certificateDao;

    @Autowired
    public GiftCertificateDaoImplTest(GiftCertificateDaoImpl giftCertificateDao) {
        this.certificateDao = giftCertificateDao;
    }

    @Test
    public void testCreateCertificateShouldCreate() {
        //given
        //when
        certificateDao.create(CERTIFICATE_TO_CREATE);
        Optional<GiftCertificate> giftCertificate = certificateDao
                .findByName(CERTIFICATE_TO_CREATE.getName());
        //then
        assertTrue(giftCertificate.isPresent());
    }

    @Test
    public void testCreateCertificateTagReferenceShouldCreate() {
        //given
        //when
        certificateDao.createCertificateTagReference(SECOND_CERTIFICATE.getId(), THIRD_TAG.getId());
        List<Long> tagIds = certificateDao.getTagIdsByCertificateId(SECOND_CERTIFICATE.getId());
        //then
        assertTrue(tagIds.contains(THIRD_TAG.getId()));
    }

    @Test
    public void testGetAllShouldGet() {
        //given
        //when
        List<GiftCertificate> giftCertificates = certificateDao.getAll();
        //then
        assertEquals(Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE, THIRD_CERTIFICATE),
                giftCertificates);
    }

    @Test
    public void testGetTagIdsByCertificateIdShouldGet() {
        //given
        //when
        List<Long> tagIds = certificateDao.getTagIdsByCertificateId(FIRST_TAG.getId());
        //then
        assertEquals(FIRST_CERTIFICATE_TAGS.stream()
                .map(Tag::getId)
                .collect(Collectors.toList()), tagIds);
    }

    @Test
    public void testGetCertificateIdsByTagIdShouldGet() {
        //given
        //when
        List<Long> tagIds = certificateDao.getTagIdsByCertificateId(SECOND_TAG.getId());
        //then
        assertEquals(SECOND_TAG.getId(), tagIds.get(0));
    }

    @Test
    public void testGetAllWithSortingShouldGet() {
        //given
        SortParamsContext sortParamsContext = new SortParamsContext(
                Collections.singletonList("id"), Collections.singletonList("DESC"));
        //when
        List<GiftCertificate> giftCertificates = certificateDao.getAllWithSorting(sortParamsContext);
        //then
        assertEquals(Arrays.asList(THIRD_CERTIFICATE, SECOND_CERTIFICATE, FIRST_CERTIFICATE),
                giftCertificates);
    }

    @Test
    public void testGetAllWithFilteringShouldGet() {
        //given
        //when
        List<GiftCertificate> giftCertificates = certificateDao.getAllWithFiltering(
                Arrays.asList(1L, 2L), "certif");
        //then
        assertEquals(Arrays.asList(FIRST_CERTIFICATE, SECOND_CERTIFICATE), giftCertificates);
    }

    @Test
    public void testGetAllWithSortingFilteringShouldGet() {
        //given
        SortParamsContext sortParamsContext = new SortParamsContext(
                Collections.singletonList("id"), Collections.singletonList("DESC"));
        //when
        List<GiftCertificate> giftCertificates = certificateDao.getAllWithSortingFiltering(
                sortParamsContext, Arrays.asList(1L, 2L), "certif");
        //then
        assertEquals(Arrays.asList(SECOND_CERTIFICATE, FIRST_CERTIFICATE), giftCertificates);
    }

    @Test
    public void testFindByNameShouldFind() {
        //given
        //when
        Optional<GiftCertificate> giftCertificate = certificateDao.findByName(
                FIRST_CERTIFICATE.getName());
        //then
        assertEquals(FIRST_CERTIFICATE, giftCertificate.get());
    }

    @Test
    public void testFindByIdShouldFind() {
        //given
        //when
        Optional<GiftCertificate> giftCertificate = certificateDao.findById(
                FIRST_CERTIFICATE.getId());
        //then
        assertEquals(FIRST_CERTIFICATE, giftCertificate.get());
    }

    @Test
    public void testUpdateByIdShouldUpdate() {
        //given
        Map<String, Object> updateInfo = new HashMap<>();
        updateInfo.put("name", "certificate new name");
        //when
        certificateDao.updateById(FIRST_CERTIFICATE.getId(),updateInfo);
        Optional<GiftCertificate> giftCertificate = certificateDao.findById(
                FIRST_CERTIFICATE.getId());
        //then
        assertEquals(giftCertificate.get().getName(), "certificate new name");
    }

    @Test
    public void testDeleteByIdShouldDelete() {
        //given
        //when
        certificateDao.deleteById(THIRD_CERTIFICATE.getId());
        //then
        boolean isExist = certificateDao.findById(THIRD_CERTIFICATE.getId()).isPresent();
        assertFalse(isExist);
    }
}
