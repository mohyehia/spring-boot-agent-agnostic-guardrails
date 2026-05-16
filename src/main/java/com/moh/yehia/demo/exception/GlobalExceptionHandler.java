package com.moh.yehia.demo.exception;

import com.moh.yehia.demo.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized handler for translating application exceptions into API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles missing category exceptions.
     *
     * @param exception the missing category exception
     * @param request the current HTTP request
     * @return the error response
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCategoryNotFound(
            CategoryNotFoundException exception, HttpServletRequest request) {
        return this.buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    /**
     * Handles validation failures for request bodies.
     *
     * @param exception the validation exception
     * @param request the current HTTP request
     * @return the error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationFailure(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + "=" + error.getDefaultMessage())
                .orElse("Request validation failed");
        return this.buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Handles malformed JSON payloads.
     *
     * @param request the current HTTP request
     * @return the error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMalformedJson(HttpServletRequest request) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, "Request body is invalid", request);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI());
        return ResponseEntity.status(status).body(errorResponse);
    }
}


