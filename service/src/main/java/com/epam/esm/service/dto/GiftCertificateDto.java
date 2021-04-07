package com.epam.esm.service.dto;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;

import java.util.Set;

public class GiftCertificateDto {
    private GiftCertificate giftCertificate;
    private Set<Tag> tags;

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "GiftCertificateDto{" +
                "giftCertificate=" + giftCertificate +
                ", tags=" + tags +
                '}';
    }
}
