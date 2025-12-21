package com.mycompany.app.controllers.services;

/**
 * Concrete Flyweight class that implements the Board interface.
 * Stores intrinsic state (board, emptyPositions) and operates on extrinsic state (permutation).
 * 
 * @author Zeyad
 */
public class PermutationVerifier implements Board {
    // Intrinsic state (shared, immutable)
    private final int[][] board;
    private final int[] emptyPositions;

    /**
     * Creates a verifier for a specific board configuration.
     * This instance can be reused for checking multiple permutations for the same board.
     */
    public PermutationVerifier(int[][] board, int[] emptyPositions) {
        this.board = board;
        this.emptyPositions = emptyPositions;
    }

    @Override
    public boolean isValidPermutation(int[][] board, int[] emptyPositions, int[] permutation) {
        // Check each value in the permutation
        for (int i = 0; i < permutation.length; i++) {
            int position = emptyPositions[i];
            int row = position / 9;
            int col = position % 9;
            int value = permutation[i];

            // Check if this value can be placed at this position
            if (!canPlaceValue(board, row, col, value)) {
                return false; // This permutation is invalid
            }
        }

        // All values can be placed validly
        return true;
    }

    @Override
    public boolean canPlaceValue(int[][] board, int row, int col, int value) {
        // Check if found already in row
        for (int c = 0; c < 9; c++) {
            if (board[row][c] == value) {
                return false;
            }
        }

        // Check if found already in column
        for (int r = 0; r < 9; r++) {
            if (board[r][col] == value) {
                return false;
            }
        }

        // Check if found already in box
        int boxStartRow = (row / 3) * 3;
        int boxStartCol = (col / 3) * 3;

        for (int r = boxStartRow; r < boxStartRow + 3; r++) {
            for (int c = boxStartCol; c < boxStartCol + 3; c++) {
                if (board[r][c] == value) {
                    return false;
                }
            }
        }

        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[] getEmptyPositions() {
        return emptyPositions;
    }
}