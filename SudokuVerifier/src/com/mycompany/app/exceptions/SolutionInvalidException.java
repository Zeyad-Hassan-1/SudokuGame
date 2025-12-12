package com.mycompany.app.exceptions;

/**
 * Exception thrown when a source solution is invalid or incomplete (cannot be used to generate games).
 */
public class SolutionInvalidException extends Exception {
    public SolutionInvalidException(String message) {
        super(message);
    }
}

