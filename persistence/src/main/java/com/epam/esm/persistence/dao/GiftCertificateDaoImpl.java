package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {
    private static final String CREATE_QUERY = "INSERT INTO certificate" +
            "(name, description, price, duration) VALUES (?, ?, ?, ?)";
    private static final RowMapper<GiftCertificate> ROW_MAPPER =
            new BeanPropertyRowMapper<>(GiftCertificate.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        super(ROW_MAPPER, "certificate", jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcTemplate.update(CREATE_QUERY, giftCertificate.getName(),
                giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration());
    }

    @Override
    public void updateById(long id, GiftCertificate giftCertificate) {
//        jdbcTemplate.update(CREATE_QUERY, giftCertificate.getName(),
//                giftCertificate.getDescription(), giftCertificate.getPrice(),
//                giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate(), id);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return findByColumn("name", name);
    }
}
