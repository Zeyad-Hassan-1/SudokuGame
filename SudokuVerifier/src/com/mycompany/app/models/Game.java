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
   public int[][] board;
   private DifficultyEnum difficulty;

   public Game(int[][] board) {
      // IMPORTANT: DON'T COPY THE BOARD BY VALUE
      // USE REFERENCES
      this.board = board;
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

   public DifficultyEnum getDifficulty() {
      return difficulty;
   }

   public int countEmptyCells(int[][] board) {
      int count = 0;
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            if (board[i][j] == 0)
               count++;
         }
      }
      return count;
   }

   public int[] findEmptyCells(int[][] board) {
      int[] emptyCells = new int[countEmptyCells(board)];
      int index = 0;
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            if (board[i][j] == 0)
               emptyCells[index++] = i * 9 + j;
         }
      }
      return emptyCells;
   }

}
