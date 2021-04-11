package com.epam.esm.persistence.dao.impl;

import com.epam.esm.persistence.dao.AbstractDao;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.contstant.Query;
import com.epam.esm.persistence.query.QueryBuildHelper;
import com.epam.esm.persistence.query.SortParamsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GiftCertificateDaoImpl extends AbstractDao<GiftCertificate> implements GiftCertificateDao {
    private static final String TABLE_NAME = "certificate";
    private static final RowMapper<GiftCertificate> ROW_MAPPER =
            new BeanPropertyRowMapper<>(GiftCertificate.class);

    private final JdbcTemplate jdbcTemplate;
    private final QueryBuildHelper queryBuildHelper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, QueryBuildHelper queryBuildHelper) {
        super(ROW_MAPPER, TABLE_NAME, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.queryBuildHelper = queryBuildHelper;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcTemplate.update(Query.CREATE_CERTIFICATE, giftCertificate.getName(),
                giftCertificate.getDescription(), giftCertificate.getPrice(),
                giftCertificate.getDuration());
    }

    @Override
    public void createCertificateTagReference(long certificateId, long tagId) {
        jdbcTemplate.update(Query.CREATE_CERTIFICATE_TAG_REFERENCE, certificateId, tagId);
    }

    @Override
    public List<Long> getTagIdsByCertificateId(long certificateId) {
        return jdbcTemplate.query(Query.GET_TAG_IDS_BY_CERTIFICATE_ID,
                (resultSet, i) -> resultSet.getLong("tag_id"), certificateId);
    }

    @Override
    public List<Long> getCertificateIdsByTagId(long tagId) {
        return jdbcTemplate.query(Query.GET_CERTIFICATE_IDS_BY_TAG_ID,
                (resultSet, i) -> resultSet.getLong("certificate_id"), tagId);
    }

    @Override
    public List<GiftCertificate> getAllWithSorting(SortParamsContext sortParameters) {
        String query = getAllQuery + " " + queryBuildHelper.buildSortingQuery(sortParameters);
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public List<GiftCertificate> getAllWithFilter(List<Long> ids, String partInfo) {
        List<Object> values = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(getAllQuery).append(" WHERE ");
        fillFilterQueryInfo(ids, partInfo, queryBuilder, values);
        return jdbcTemplate.query(queryBuilder.toString(), ROW_MAPPER, values.toArray());
    }

    private void fillFilterQueryInfo(List<Long> ids, String partInfo,
                                     StringBuilder queryBuilder, List<Object> values) {
        boolean isIdsExist = ids != null && !ids.isEmpty();
        if (isIdsExist) {
            String inFilter = queryBuildHelper.buildInFilteringQuery("id", ids.size());
            queryBuilder.append(inFilter);
            values.addAll(ids);
        }
        if (partInfo != null) {
            if (isIdsExist) {
                queryBuilder.append("AND ");
            }
            queryBuilder.append("(name LIKE ? OR description LIKE ?)");
            String regexPartInfo = queryBuildHelper.buildRegexValue(partInfo);
            values.add(regexPartInfo);
            values.add(regexPartInfo);
        }
    }

    @Override
    public List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                            List<Long> ids, String partInfo) {
        List<Object> values = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(getAllQuery).append(" WHERE ");
        fillFilterQueryInfo(ids, partInfo, queryBuilder, values);
        queryBuilder.append(" ").append(queryBuildHelper.buildSortingQuery(sortParameters));
        System.out.println(queryBuilder.toString());
        return jdbcTemplate.query(queryBuilder.toString(), ROW_MAPPER, values.toArray());
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return findByColumn("name", name);
    }

    @Override
    public void updateById(long id, Map<String, Object> giftCertificateUpdateInfo) {
        if (!giftCertificateUpdateInfo.isEmpty()) {
            StringBuilder updateQueryBuilder = new StringBuilder();
            updateQueryBuilder.append("UPDATE certificate SET last_update_date=NOW(), ");
            String updateColumnsQuery = queryBuildHelper.buildUpdateColumnsQuery(
                    giftCertificateUpdateInfo.keySet());
            updateQueryBuilder.append(updateColumnsQuery);
            updateQueryBuilder.append(" WHERE id=?");
            List<Object> values = new ArrayList<>(giftCertificateUpdateInfo.values());
            values.add(id);
            jdbcTemplate.update(updateQueryBuilder.toString(), values.toArray());
        }
    }
}
