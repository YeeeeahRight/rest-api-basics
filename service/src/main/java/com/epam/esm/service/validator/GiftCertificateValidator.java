package com.epam.esm.service.validator;

import com.epam.esm.persistence.entity.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator implements EntityValidator<GiftCertificate> {
    private static final int NAME_MAX_LENGTH = 80;
    private static final int NAME_MIN_LENGTH = 1;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final int DESCRIPTION_MIN_LENGTH = 1;
    private static final int DURATION_MIN_VALUE = 1;

    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        if (name == null || description == null
                || giftCertificate.getPrice() == null) {
            return false;
        }
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH
            || description.length() < DESCRIPTION_MIN_LENGTH
                || description.length() > DESCRIPTION_MAX_LENGTH) {
            return false;
        }
        int duration = giftCertificate.getDuration();
        return duration >= DURATION_MIN_VALUE;
    }
}
