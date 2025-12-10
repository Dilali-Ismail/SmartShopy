package org.usermanagement.smartshopy.handler;

import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org. springframework.http.ResponseEntity;
import org.springframework.validation. FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org. springframework.web.bind.annotation. ExceptionHandler;
import org. springframework.web.bind.annotation. RestControllerAdvice;
import org.springframework.web.context. request.WebRequest;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement. smartshopy.exception.UnautorizedException;

import java.io.PrintWriter;
import java.io. StringWriter;
import java.time.LocalDateTime;
import java. util.HashMap;
import java. util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${app.show-stack-trace:true}")
    private boolean showStackTrace;

    /**
     * Construire une réponse d'erreur standard
     */
    private Map<String, Object> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status. value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error. put("path", request.getDescription(false).replace("uri=", ""));
        return error;
    }

    /**
     * Construire une réponse d'erreur avec stack trace
     */
    private Map<String, Object> buildErrorResponseWithStackTrace(
            String message, HttpStatus status, WebRequest request, Exception ex) {

        Map<String, Object> error = buildErrorResponse(message, status, request);

        // ✅ Ajouter la stack trace si activé
        if (showStackTrace) {
            error.put("stackTrace", getStackTraceAsString(ex));
        }

        return error;
    }

    /**
     * Convertir une exception en String (stack trace complète)
     */
    private String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(
            NotFoundException ex, WebRequest request) {

        log.error("NotFoundException: {}", ex.getMessage());

        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus. NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnautorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(
            UnautorizedException ex, WebRequest request) {

        log.error("UnauthorizedException: {}", ex.getMessage());

        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {

        log.error("BadRequestException: {}", ex.getMessage());

        Map<String, Object> error = buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.error("Validation error: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error. getDefaultMessage();
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
     * ✅ MODIFIÉ : Affiche maintenant la stack trace complète
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {

        // ✅ Logger la stack trace complète dans les logs
        log.error("Exception non gérée: ", ex);

        // ✅ Créer une réponse avec la stack trace
        Map<String, Object> error = buildErrorResponseWithStackTrace(
                ex.getMessage() != null ? ex.getMessage() : "Une erreur interne est survenue",
                HttpStatus. INTERNAL_SERVER_ERROR,
                request,
                ex
        );

        return ResponseEntity. status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}