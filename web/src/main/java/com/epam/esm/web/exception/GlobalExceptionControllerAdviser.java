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

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEntityException(
            DuplicateEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40901);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidEntityExceptionException(
            InvalidEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 42201);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(TypeMismatchException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40000);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleNotReadableBodyException() {
        ExceptionResponse response = new ExceptionResponse(
                "Required request body data is missing.", 40001);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEntityException(MissingServletRequestParameterException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40002);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParametersException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidParametersException(InvalidParametersException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40003);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40500);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(NoHandlerFoundException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40400);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEntityException(NoSuchEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40401);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(ConversionNotSupportedException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 50001);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(HttpMessageNotWritableException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 50002);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(Exception e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 50000);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
