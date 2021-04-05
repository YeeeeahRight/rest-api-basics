package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String CREATE_QUERY = "INSERT INTO tag(name) VALUES (?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM tag";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM tag WHERE id=?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM tag WHERE name=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM tag WHERE id=?";
    private static final RowMapper<Tag> ROW_MAPPER = new BeanPropertyRowMapper<>(Tag.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(CREATE_QUERY, tag.getName());
    }

    @Override
    public Set<Tag> getAll() {
        return new LinkedHashSet<>(jdbcTemplate.query(GET_ALL_QUERY, ROW_MAPPER));
    }

    @Override
    public Optional<Tag> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, ROW_MAPPER, id)
                .stream().findAny();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return jdbcTemplate.query(FIND_BY_NAME_QUERY, ROW_MAPPER, name)
                .stream().findAny();
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }
}
