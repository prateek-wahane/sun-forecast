package com.rock8tech.sunforecast.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// MVC exceptions (Spring Web MVC)
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;

// WebFlux equivalents (Spring WebFlux)
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	    private static final org.slf4j.Logger log =
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Missing query/path params ⇒ 400 (covers MVC + WebFlux)
    @ExceptionHandler({ MissingServletRequestParameterException.class, MissingRequestValueException.class })
    public ResponseEntity<Map<String, String>> handleMissingParam(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Validation/binding errors ⇒ 400 (covers MVC + WebFlux)
    @ExceptionHandler({ ConstraintViolationException.class, MethodArgumentNotValidException.class, WebExchangeBindException.class })
    public ResponseEntity<Map<String, String>> handleValidation(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Validation failed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
		log.error("Unhandled error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error"));
    }
}
