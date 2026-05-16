package com.moh.yehia.demo.exception;

/**
 * Base exception type for application-specific runtime failures.
 */
public class ApplicationException extends RuntimeException {

    /**
     * Creates a new application exception.
     *
     * @param message the exception message
     */
    public ApplicationException(String message) {
        super(message);
    }
}

