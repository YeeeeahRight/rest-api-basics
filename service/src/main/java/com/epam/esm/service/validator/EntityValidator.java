package com.epam.esm.service.validator;

public interface EntityValidator<T> {
    boolean isValid(T entity);
}
