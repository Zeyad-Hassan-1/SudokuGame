package com.mycompany.app.models;

/**
 * Catalog class representing game availability status.
 * Contains two boolean flags:
 * - current: True if there is a game in progress, False otherwise.
 * - allModesExist: True if there is at least one game available for each difficulty, False otherwise.
 */
public class Catalog {
    public boolean current; // True if there is a game in progress, False otherwise.
    public boolean allModesExist; // True if there is at least one game available for each difficulty, False otherwise.
    
    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }
}