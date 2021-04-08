package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.dao.AbstractDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    private static final String TABLE_NAME = "tag";
    private static final RowMapper<Tag> ROW_MAPPER = new BeanPropertyRowMapper<>(Tag.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(ROW_MAPPER, TABLE_NAME, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(Query.CREATE_TAG, tag.getName());
    }

    public Optional<Tag> findByName(String name) {
        return findByColumn("name", name);
    }
}
