package com.epam.esm.validator;

public interface EntityValidator<T> {
    boolean isValid(T entity);
}
