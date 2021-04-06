package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    private static final String CREATE_QUERY = "INSERT INTO tag(name) VALUES (?)";
    private static final RowMapper<Tag> ROW_MAPPER = new BeanPropertyRowMapper<>(Tag.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(ROW_MAPPER, "tag", jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(CREATE_QUERY, tag.getName());
    }

    public Optional<Tag> findByName(String name) {
        return findByColumn("name", name);
    }
}
