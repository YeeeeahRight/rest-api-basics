package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {
    void create(Tag tag);

    List<Tag> getAll();

    Optional<Tag> findById(long id);

    Optional<Tag> findByName(String name);

    void deleteById(long id);
}
