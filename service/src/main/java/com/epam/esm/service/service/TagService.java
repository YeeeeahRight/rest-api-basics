package com.epam.esm.service.service;

import com.epam.esm.persistence.entity.Tag;

import java.util.Set;

public interface TagService {
    long create(Tag tag);

    Set<Tag> getAll();

    Tag getById(long id);

    void deleteById(long id);
}
