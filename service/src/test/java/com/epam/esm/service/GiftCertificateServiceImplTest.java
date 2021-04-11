package com.epam.esm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.persistence.dao.impl.TagDaoImpl;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.SortParamsContext;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.GiftCertificateServiceImpl;
import com.epam.esm.service.validator.GiftCertificateValidator;
import com.epam.esm.service.validator.SortParamsContextValidator;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

public class GiftCertificateServiceImplTest {
    private static final long ID = 1;
    private static final GiftCertificate GIFT_CERTIFICATE = new GiftCertificate(ID, "name",
            "description", BigDecimal.TEN, ZonedDateTime.now(), ZonedDateTime.now(), 5);
    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO = new GiftCertificateDto(GIFT_CERTIFICATE);

    private GiftCertificateDao certificateDao;
    private Validator<GiftCertificate> certificateValidator;
    private TagDao tagDao;
    private Validator<Tag> tagValidator;
    private Validator<SortParamsContext> sortParamsContextValidator;

    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    public void initMethod() {
        certificateDao = Mockito.mock(GiftCertificateDaoImpl.class);
        certificateValidator = Mockito.mock(GiftCertificateValidator.class);
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagValidator = Mockito.mock(TagValidator.class);
        sortParamsContextValidator = Mockito.mock(SortParamsContextValidator.class);
        giftCertificateService = new GiftCertificateServiceImpl(certificateDao, tagDao,
                certificateValidator, tagValidator, sortParamsContextValidator);
    }

    @Test
    public void testCreateShouldCreateWhenNotExistAndValid() {
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(tagValidator.isValid(any())).thenReturn(true);
        when(certificateDao.findByName(anyString())).thenReturn(Optional.empty());
        giftCertificateService.create(GIFT_CERTIFICATE_DTO);
        verify(certificateDao).create(GIFT_CERTIFICATE);
    }

    @Test
    public void testCreateShouldThrowsInvalidEntityExceptionWhenNotValid() {
        when(certificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityException.class, () -> giftCertificateService.create(GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testCreateShouldThrowsDuplicateEntityExceptionWhenExist() {
        when(certificateValidator.isValid(any())).thenReturn(true);
        when(certificateDao.findByName(anyString())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        assertThrows(DuplicateEntityException.class, () -> giftCertificateService.create(GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testGetAllShouldGetAll() {
        giftCertificateService.getAll();
        verify(certificateDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        giftCertificateService.getById(ID);
        verify(certificateDao).findById(ID);
    }

    @Test
    public void testGetByIdShouldThrowsNotSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.getById(ID));
    }

    @Test
    public void getAllWithTagsShouldGetAllWhenFilteringAndSortingNotExist() {
        giftCertificateService.getAllWithTags(null, null, null, null);
        verify(certificateDao).getAll();
    }

    @Test
    public void getAllWithTagsShouldGetWithFilteringWhenFilteringExist() {
        giftCertificateService.getAllWithTags(null, "z", null, null);
        verify(certificateDao).getAllWithFilter(any(), anyString());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringWhenSoringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTags(null, null,
                Collections.singletonList("name"), null);
        verify(certificateDao).getAllWithSorting(any());
    }

    @Test
    public void getAllWithTagsShouldGetWithSoringAndFilteringWhenSoringAndFilteringExist() {
        when(sortParamsContextValidator.isValid(any())).thenReturn(true);
        giftCertificateService.getAllWithTags(null, "p",
                Collections.singletonList("name"), null);
        verify(certificateDao).getAllWithSortingFiltering(any(), any(), anyString());
    }


    @Test
    public void testUpdateByIdShouldUpdateWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        when(((GiftCertificateValidator)certificateValidator).isNameValid(anyString())).thenReturn(true);
        when(((GiftCertificateValidator)certificateValidator).isDescriptionValid(anyString())).thenReturn(true);
        when(((GiftCertificateValidator)certificateValidator).isDurationValid(anyInt())).thenReturn(true);
        when(((GiftCertificateValidator)certificateValidator).isPriceValid(any())).thenReturn(true);
        giftCertificateService.updateById(ID, GIFT_CERTIFICATE_DTO);
        verify(certificateDao).updateById(anyLong(), any());
    }

    @Test
    public void testUpdateByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.updateById(ID, GIFT_CERTIFICATE_DTO));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.of(GIFT_CERTIFICATE));
        giftCertificateService.deleteById(ID);
        verify(certificateDao).deleteById(ID);
    }

    @Test
    public void testDeleteByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(certificateDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.deleteById(ID));
    }
}
