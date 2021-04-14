package com.epam.esm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.impl.TagDaoImpl;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.logic.TagServiceImpl;
import com.epam.esm.service.validator.TagValidator;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class TagServiceImplTest {
    private static final long ID = 1;
    private static final Tag TAG = new Tag(ID,"tag");

    private TagDao tagDao;
    private Validator<Tag> tagValidator;

    private TagServiceImpl tagService;

    @BeforeEach
    public void initMethod() {
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagValidator = Mockito.mock(TagValidator.class);
        tagService = new TagServiceImpl(tagDao, tagValidator);
    }

    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
        tagService.create(TAG);
        verify(tagDao).create(TAG);
    }

    @Test
    public void testCreateShouldThrowsInvalidEntityExceptionWhenInvalid() {
        when(tagValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityException.class, () -> tagService.create(TAG));
    }

    @Test
    public void testCreateShouldThrowsDuplicateEntityExceptionWhenExist() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.findByName(anyString())).thenReturn(Optional.of(TAG));
        assertThrows(DuplicateEntityException.class, () -> tagService.create(TAG));
    }

    @Test
    public void testGetAllShouldGetAll() {
        tagService.getAll();
        verify(tagDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(TAG));
        tagService.getById(ID);
        verify(tagDao).findById(ID);
    }

    @Test
    public void testGetByIdShouldThrowsNotSuchEntityExceptionWhenNotFound() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.getById(ID));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.of(TAG));
        tagService.deleteById(ID);
        verify(tagDao).deleteById(ID);
    }

    @Test
    public void testDeleteByIdShouldThrowsNoSuchEntityExceptionWhenNotFound() {
        when(tagDao.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.deleteById(ID));
    }
}
