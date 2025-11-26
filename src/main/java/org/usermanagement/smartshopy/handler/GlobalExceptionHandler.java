package org.usermanagement.smartshopy.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.exception.UnautorizedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("path", request.getDescription(false).replace("uri=", ""));
        return error;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            NotFoundException ex, WebRequest request) {
        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(UnautorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(
            UnautorizedException ex, WebRequest request) {
        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Validation Failed");
        error.put("message", "Erreur de validation des données");
        error.put("errors", validationErrors);
        error.put("path", request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Gestion des exceptions génériques (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        Map<String, Object> error = buildErrorResponse(
                "Une erreur interne est survenue",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
