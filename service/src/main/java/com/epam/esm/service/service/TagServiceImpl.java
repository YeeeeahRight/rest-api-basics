package com.epam.esm.service.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.NoSuchEntityException;
import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.validator.EntityValidator;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final EntityValidator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    @Transactional
    public long create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidEntityException("Invalid tag data.");
        }
        String tagName = tag.getName();
        boolean isTagExist = tagDao.findByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException
                    ("Tag with name='" + tagName + "' is already exist.");
        }
        tagDao.create(tag);
        Optional<Tag> optionalTag = tagDao.findByName(tagName);
        return optionalTag.map(Tag::getId).orElse(-1L);
    }

    @Override
    public Set<Tag> getAll() {
        return new HashSet<>(tagDao.getAll());
    }

    @Override
    @Transactional
    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("No tag with id=" + id + ".");
        }
        return optionalTag.get();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("No tag with id=" + id + ".");
        }
        tagDao.deleteById(id);
    }
}
