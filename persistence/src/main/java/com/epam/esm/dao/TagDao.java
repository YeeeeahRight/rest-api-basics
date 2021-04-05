package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

public interface TagDao {
    void create(Tag tag);

    Set<Tag> getAll();

    Optional<Tag> findById(long id);

    Optional<Tag> findByName(String name);

    void deleteById(long id);
}
