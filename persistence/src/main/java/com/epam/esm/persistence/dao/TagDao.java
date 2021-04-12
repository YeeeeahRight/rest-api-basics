package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for Tag
 */
public interface TagDao {
    /**
     * Creates new Tag.
     *
     * @param tag Tag to create
     */
    void create(Tag tag);

    /**
     * Gets all Tags.
     *
     * @return List of founded Tags
     */
    List<Tag> getAll();

    /**
     * Finds Tag by id.
     *
     * @param id Tag id to find
     * @return Optional Tag - Tag if founded or Empty if not
     */
    Optional<Tag> findById(long id);

    /**
     * Finds Tag by name.
     *
     * @param name Tag name to find
     * @return Optional Tag - Tag if founded or Empty if not
     */
    Optional<Tag> findByName(String name);

    /**
     * Deletes Tag by id.
     *
     * @param id Tag id to find
     */
    void deleteById(long id);
}
