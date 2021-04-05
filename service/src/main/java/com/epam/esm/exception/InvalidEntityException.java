package com.epam.esm.exception;

public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException() {
    }

    public InvalidEntityException(String message) {
        super(message);
    }
}
