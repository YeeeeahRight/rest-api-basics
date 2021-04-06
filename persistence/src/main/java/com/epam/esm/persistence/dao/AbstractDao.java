package com.epam.esm.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> {
    private final String getAllQuery;
    private final String findByIdQuery;
    private final String findByColumnQuery;
    private final String deleteByIdQuery;

    private final RowMapper<T> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    public AbstractDao(RowMapper<T> rowMapper, String tableName, JdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;

        getAllQuery = "SELECT * FROM " + tableName;
        findByIdQuery = "SELECT * FROM " + tableName + " WHERE id=?";
        findByColumnQuery = "SELECT * FROM " + tableName + " WHERE %s=?";
        deleteByIdQuery = "DELETE FROM " + tableName + " WHERE id=?";
    }

    public List<T> getAll() {
        return jdbcTemplate.query(getAllQuery, rowMapper);
    }

    public Optional<T> findById(long id) {
        return jdbcTemplate.query(findByIdQuery, rowMapper, id)
                .stream().findAny();
    }

    public void deleteById(long id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    public Optional<T> findByColumn(String columnName, String value) {
        String query = String.format(findByColumnQuery, columnName);
        return jdbcTemplate.query(query, rowMapper, value).stream().findAny();
    }
}
