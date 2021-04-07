package com.epam.esm.service.exception;

public class CreationEntityException extends RuntimeException {

    public CreationEntityException() {
    }

    public CreationEntityException(String message) {
        super(message);
    }
}
