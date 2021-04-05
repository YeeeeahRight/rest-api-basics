package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements EntityValidator<Tag> {
    private static final int MAX_LENGTH = 60;
    private static final int MIN_LENGTH = 1;

    @Override
    public boolean isValid(Tag entity) {
        int nameLength = entity.getName().length();
        return nameLength >= MIN_LENGTH && nameLength <= MAX_LENGTH;
    }
}
