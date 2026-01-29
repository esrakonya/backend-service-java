package org.esrakonya.backend.common.exception;

import org.esrakonya.backend.auth.exception.InvalidCredentialsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. VALIDATION ERRORS (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage())
        );

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", errorMap);
    }

    // 2. RESOURCE NOT FOUND (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // 3. CONFLICT / ALREADY EXISTS (409)
    @ExceptionHandler({ResourceAlreadyExistsException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiErrorResponse> handleConflicts(Exception ex) {
        String message = (ex instanceof DataIntegrityViolationException)
                ? "Database constraint violation: Duplicate entry or integrity error."
                : ex.getMessage();
        return buildResponse(HttpStatus.CONFLICT, message, null);
    }

    // 4. UNAUTHORIZED (401)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    // 5. FORBIDDEN (403) - CRITICAL FOR INTEGRATION TESTS
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied: You do not have the required permissions.", null);
    }

    // 6. FINAL FALLBACK (500) - MUST BE AT THE BOTTOM
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralExceptions(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage(), null);
    }

    // Senior Helper Method to keep code DRY
    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, Map<String, String> errors) {
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, status);
    }
}