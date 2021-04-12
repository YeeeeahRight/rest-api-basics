package com.epam.esm.persistence.contstant;

public final class Query {
    public static final String CREATE_CERTIFICATE = "INSERT INTO certificates" +
            "(name, description, price, duration) VALUES (?, ?, ?, ?)";
    public static final String CREATE_CERTIFICATE_TAG_REFERENCE = "INSERT INTO " +
            "certificates_tags(certificate_id, tag_id) VALUES (?, ?)";
    public static final String GET_TAG_IDS_BY_CERTIFICATE_ID = "SELECT * FROM " +
            "certificates_tags WHERE certificate_id=?";
    public static final String GET_CERTIFICATE_IDS_BY_TAG_ID = "SELECT * FROM " +
            "certificates_tags WHERE tag_id=?";

    public static final String CREATE_TAG= "INSERT INTO tags(name) VALUES (?)";
}
