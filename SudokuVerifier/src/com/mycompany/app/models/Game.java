package com.mycompany.app.models;

/**
 * Represents a Sudoku game board with associated metadata.
 * This is the main data structure used by the controller layer.
 * 
 * The board is represented as a 9x9 integer array where:
 * Values 1-9 represent filled cells
 * Value 0 represents an empty cell
 * 
 * @author Nour
 */
public class Game {
   int[][] board;
   private DifficultyEnum difficulty;

   public Game(int[][] board, DifficultyEnum difficulty) {
      // IMPORTANT: DON'T COPY THE BOARD BY VALUE
      // USE REFERENCES
      this.board = board;
      this.difficulty = difficulty;
   }
   // Add methods and attributes if needed

   public int[][] getBoard() {
      return board;
   }

   public void setBoard(int[][] board) {
      this.board = board;
   }

   public void setDifficulty(DifficultyEnum difficulty) {
      this.difficulty = difficulty;
   }

 

}
}
