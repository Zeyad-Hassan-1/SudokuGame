package com.mycompany.app.controllers.services;

/**
 * FLyweight interface
 * 
 * @author Zeyad
 */
public interface Board {
    /**
     * Verifies if an entire permutation is valid for the given empty positions.
     * only READS from the board, never writes to it.
     * @param board          The shared Sudoku board (not modified)
     * @param emptyPositions Array of positions where cells are empty
     * @param permutation    Array of values to place at those positions
     * @return true if all values in permutation can be validly placed
     */
    boolean isValidPermutation(int[][] board, int[] emptyPositions, int[] permutation);

    /**
     * Checks if a value can be placed at the given position on the board.
     * only READS from the board, never writes to it.
     * @param board The shared Sudoku board (9x9)
     * @param row   Row index (0-8)
     * @param col   Column index (0-8)
     * @param value Value to check (1-9)
     * @return true if value can be placed at (row, col), false otherwise
     */
    boolean canPlaceValue(int[][] board, int row, int col, int value);
}
