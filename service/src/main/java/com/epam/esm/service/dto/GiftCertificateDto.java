package com.epam.esm.service.dto;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashSet;
import java.util.Set;

public class GiftCertificateDto {
    private GiftCertificate giftCertificate;
    private Set<Tag> certificateTags = new HashSet<>();

    @JsonCreator
    public GiftCertificateDto() {
    }

    public GiftCertificateDto(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public Set<Tag> getCertificateTags() {
        return certificateTags;
    }

    public void setCertificateTags(Set<Tag> certificateTags) {
        this.certificateTags = certificateTags;
    }

    public void addTag(Tag tag) {
        this.certificateTags.add(tag);
    }

    @Override
    public String toString() {
        return "GiftCertificateDto{" +
                "giftCertificate=" + giftCertificate +
                ", tags=" + certificateTags +
                '}';
    }
}
