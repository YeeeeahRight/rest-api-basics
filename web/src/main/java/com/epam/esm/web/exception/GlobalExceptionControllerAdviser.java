package com.epam.esm.web.exception;

import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.InvalidEntityException;
import com.epam.esm.service.exception.InvalidParametersException;
import com.epam.esm.service.exception.NoSuchEntityException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionControllerAdviser {

    private ResponseEntity<ExceptionResponse> handleException(String message, int code, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(message, code);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEntityException(
            DuplicateEntityException e) {
        return handleException(e.getMessage(), 40901, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEntityExceptionException(
            InvalidEntityException e) {
        return handleException(e.getMessage(), 42201, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(TypeMismatchException e) {
        return handleException(e.getMessage(), 40000, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleNotReadableBodyException() {
        return handleException( "Required request body data is missing.", 40001, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEntityException(MissingServletRequestParameterException e) {
        return handleException(e.getMessage(), 40002, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParametersException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParametersException(InvalidParametersException e) {
        return handleException(e.getMessage(), 40003, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return handleException(e.getMessage(), 40500, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(NoHandlerFoundException e) {
        return handleException(e.getMessage(), 40400, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEntityException(NoSuchEntityException e) {
        return handleException(e.getMessage(), 40401, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(ConversionNotSupportedException e) {
        return handleException(e.getMessage(), 50001, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(HttpMessageNotWritableException e) {
        return handleException(e.getMessage(), 50002, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(Exception e) {
        return handleException(e.getMessage(), 50000, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
