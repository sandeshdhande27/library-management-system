package com.sandesh.library.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<String> handleUnknownFields(UnrecognizedPropertyException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid field(s) in request: " + ex.getPropertyName());
    }
}