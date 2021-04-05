package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.Set;

public interface TagService {
    long create(Tag tag);

    Set<Tag> getAll();

    Tag getById(long id);

    void deleteById(long id);
}
