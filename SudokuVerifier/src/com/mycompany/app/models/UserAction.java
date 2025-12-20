package com.mycompany.app.models;

/**
 * Represents a user action in the Sudoku game.
 * Used for logging cell value changes to support undo functionality.
 * 
 * Each action records:
 * ▪️The cell position (x, y)
 * ▪️The new value entered by the user
 * ▪️The previous value (for undo)
 * 
 * Log format: (x, y, value, previousValue)
 * Example: (3, 5, 7, 0) means cell (3,5) was changed from 0 to 7
 * 
 * @author Nour
 */
public class UserAction {
    private int x, y, value, previousValue;

    public UserAction(int x, int y, int value, int previousValue) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.previousValue = previousValue;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getValue() { return value; }
    public int getPreviousValue() { return previousValue; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setValue(int value) { this.value = value; }
    public void setPreviousValue(int previousValue) { this.previousValue = previousValue; }

    public String toLogEntry() {
        return String.format("(%d, %d, %d, %d)", x, y, value, previousValue);
    }

    public String toLogString() {
        return toLogEntry();
    }

    public static UserAction fromLogEntry(String logEntry) {
        String cleaned = logEntry.replaceAll("[()]", "").trim();
        String[] parts = cleaned.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid format: " + logEntry);
        }
        return new UserAction(
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim()),
            Integer.parseInt(parts[2].trim()),
            Integer.parseInt(parts[3].trim()));
    }

    public static UserAction fromLogString(String logString) {
        return fromLogEntry(logString);
    }

    public boolean isValid() {
        return x >= 0 && x < 9 && y >= 0 && y < 9 && 
               value >= 0 && value <= 9 && previousValue >= 0 && previousValue <= 9;
    }

    public UserAction reverse() {
        return new UserAction(x, y, previousValue, value);
    }

    public boolean isInsertAction() {
        return previousValue == 0 && value != 0;
    }

    public boolean isClearAction() {
        return previousValue != 0 && value == 0;
    }

    public boolean isModifyAction() {
        return previousValue != 0 && value != 0 && previousValue != value;
    }

    @Override
    public String toString() {
        return String.format("UserAction[x=%d, y=%d, value=%d, prev=%d]", x, y, value, previousValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserAction that = (UserAction) obj;
        return x == that.x && y == that.y && 
               value == that.value && previousValue == that.previousValue;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + value;
        result = 31 * result + previousValue;
        return result;
    }
}