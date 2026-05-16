package com.moh.yehia.demo.dto;

import java.time.Instant;

/**
 * Response payload for API errors.
 *
 * @param timestamp the error timestamp
 * @param status the HTTP status code
 * @param error the HTTP status reason phrase
 * @param message the error message
 * @param path the request path
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path) {
}

