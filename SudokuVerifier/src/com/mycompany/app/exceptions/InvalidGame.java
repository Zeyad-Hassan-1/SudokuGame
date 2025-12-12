package com.mycompany.app.exceptions;

/**
 * Exception thrown when a game cannot be solved (e.g., not exactly 5 empty cells, or invalid state).
 */
public class InvalidGame extends Exception {
    public InvalidGame(String message) {
        super(message);
    }
}

