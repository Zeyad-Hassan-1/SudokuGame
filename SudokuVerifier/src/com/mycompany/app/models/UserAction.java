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

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }


  public int getValue() {
      return value;
   }
   public int getPreviousValue() {
      return previousValue;
   }






}
