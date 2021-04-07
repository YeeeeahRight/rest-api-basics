package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {
    private static final String CREATE_QUERY = "INSERT INTO certificate" +
            "(name, description, price, duration) VALUES (?, ?, ?, ?)";
    private static final String CREATE_CERTIFICATE_TAG_REFERENCE = "INSERT INTO " +
            "certificate_tag(certificate_id, tag_id) VALUES (?, ?)";
    private static final String GET_CERTIFICATE_TAG_IDS = "SELECT * FROM " +
            "certificate_tag WHERE certificate_id=?";
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
    public void createCertificateTagReference(long certificateId, long tagId) {
       jdbcTemplate.update(CREATE_CERTIFICATE_TAG_REFERENCE, certificateId, tagId);
    }

    @Override
    public List<Long> getCertificateTagIds(long certificateId) {
        return jdbcTemplate.query(GET_CERTIFICATE_TAG_IDS,
                (resultSet, i) -> resultSet.getLong("tag_id"), certificateId);
    }

    @Override
    public void updateById(long id, Map<String, Object> giftCertificateUpdateInfo) {
        if (!giftCertificateUpdateInfo.isEmpty()) {
            jdbcTemplate.update(buildUpdateQuery(giftCertificateUpdateInfo, id));
        }
    }

    private String buildUpdateQuery(Map<String, Object> giftCertificateUpdateInfo, long id) {
        StringBuilder updateQueryBuilder = new StringBuilder();
        updateQueryBuilder.append("UPDATE certificate SET last_update_date=NOW(), ");
        boolean isFirstElement = true;
        for (Map.Entry<String, Object> infoElement : giftCertificateUpdateInfo.entrySet()) {
            if (!isFirstElement) {
                updateQueryBuilder.append(", ");
            } else {
                isFirstElement = false;
            }
            updateQueryBuilder.append(infoElement.getKey());
            updateQueryBuilder.append("='").append(infoElement.getValue()).append('\'');
        }
        updateQueryBuilder.append(" WHERE id=").append(id);
        return updateQueryBuilder.toString();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return findByColumn("name", name);
    }
}
