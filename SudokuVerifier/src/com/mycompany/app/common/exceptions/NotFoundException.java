package com.mycompany.app.common.exceptions;

/**
 * Exception thrown when a requested game is not found (e.g., no game exists for a difficulty level).
 */
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}

