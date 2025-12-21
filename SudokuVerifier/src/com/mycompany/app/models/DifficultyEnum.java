package com.mycompany.app.models;

/**
 * 
 * @author Nour
 */
public enum DifficultyEnum {
    EASY(10),
    MEDIUM(20),
    HARD(25);

    private final int cellsToRemove;

    DifficultyEnum(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }

    public static DifficultyEnum fromChar(char c) {
        switch (c) {
            case 'e':
                return EASY;
            case 'm':
                return MEDIUM;
            case 'h':
                return HARD;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + c);
        }
    }

    public char toChar() {
        switch (this) {
            case EASY:
                return 'E';
            case MEDIUM:
                return 'M';
            case HARD:
                return 'H';
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + this);
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
