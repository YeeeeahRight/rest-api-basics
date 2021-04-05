package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ExceptionResponse> handleException(DuplicateEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40901);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 42201);
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ExceptionResponse> handleException(NoSuchEntityException e) {
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 40401);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e) {
        System.out.println(e.getLocalizedMessage());
        ExceptionResponse response = new ExceptionResponse(e.getMessage(), 50001);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
