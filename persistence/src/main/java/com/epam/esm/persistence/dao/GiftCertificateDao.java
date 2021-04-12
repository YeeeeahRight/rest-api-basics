package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.query.SortParamsContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DAO interface for Certificate
 */
public interface GiftCertificateDao {
    /**
     * Creates new Certificate.
     *
     * @param giftCertificate Certificate to create
     */
    void create(GiftCertificate giftCertificate);

    /**
     * Creates reference of Certificate and Tag.
     *
     * @param certificateId Certificate id
     * @param tagId         Tag id
     */
    void createCertificateTagReference(long certificateId, long tagId);

    /**
     * Gets all Certificates.
     *
     * @return List of founded Certificates
     */
    List<GiftCertificate> getAll();

    /**
     * Gets all Certificates with sorting.
     *
     * @param sortParameters sort parameters
     * @return List of sorted Certificates
     */
    List<GiftCertificate> getAllWithSorting(SortParamsContext sortParameters);

    /**
     * Gets all Certificates with filtering.
     *
     * @param ids      ids for filtering Certificates
     * @param partInfo part info of name/description of Certificate to filter
     * @return List of filtered Certificates
     */
    List<GiftCertificate> getAllWithFiltering(List<Long> ids, String partInfo);

    /**
     * Gets all Certificates with sorting and filtering.
     *
     * @param sortParameters sort parameters
     * @param ids      ids for filtering Certificates
     * @param partInfo part info of name/description of Certificate to filter
     * @return List of filtered and sorted Certificates
     */
    List<GiftCertificate> getAllWithSortingFiltering(SortParamsContext sortParameters,
                                                     List<Long> ids, String partInfo);

    /**
     * Gets Tag ids by Certificate id.
     *
     * @param certificateId Certificate id to search
     * @return List of founded Tag ids
     */
    List<Long> getTagIdsByCertificateId(long certificateId);

    /**
     * Gets Certificate ids by Tag id.
     *
     * @param tagId Tag id to search
     * @return List of founded Certificate ids
     */
    List<Long> getCertificateIdsByTagId(long tagId);

    /**
     * Updates Certificate by id by map of update info
     *
     * @param id                 Certificate id to search
     * @param giftCertificateUpdateInfo Update information with Certificate fields and values
     */
    void updateById(long id, Map<String, Object> giftCertificateUpdateInfo);

    /**
     * Finds Certificate by id.
     *
     * @param id Certificate id to search
     * @return Optional Certificate - Certificate if founded or Empty if not
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * Finds Certificate by name.
     *
     * @param name Certificate name to search
     * @return Optional Certificate - Certificate if founded or Empty if not
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * Deletes Certificate by id.
     *
     * @param id Certificate id to search
     */
    void deleteById(long id);
}
