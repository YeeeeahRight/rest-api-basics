package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidator tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Transactional
    @Override
    public long create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidEntityException
                    ("Tag name length should be in range [1, 60]");
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
        return tagDao.getAll();
    }

    @Transactional
    @Override
    public Tag getById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("No tag with id=" + id);
        }
        return optionalTag.get();
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Optional<Tag> optionalTag = tagDao.findById(id);
        if (!optionalTag.isPresent()) {
            throw new NoSuchEntityException("No tag with id=" + id);
        }
        tagDao.deleteById(id);
    }
}
