package com.epam.esm.exception;

public class ExceptionResponse {
    private String errorMessage;
    private int errorCode;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
