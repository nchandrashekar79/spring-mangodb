package com.example.springmongodb.error;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.springmongodb.product.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ProductNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Request validation failed", fields);
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message, Map<String, String> fields) {
        ApiError error = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, fields);
        return ResponseEntity.status(status).body(error);
    }
}