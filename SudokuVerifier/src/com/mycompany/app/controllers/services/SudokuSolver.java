package com.mycompany.app.controllers.services;

import com.mycompany.app.exceptions.InvalidGame;
import com.mycompany.app.models.Game;

// import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sudoku Solver using Iterator and Flyweight design patterns with parallelism.
 * 
 * Key Design Patterns:
 * - Iterator Pattern: PermutationIterator generates combinations on-demand
 * - Flyweight Pattern: Shares the board across all threads, only copies
 * permutations
 * - Thread Pool Pattern: Reuses worker threads to test permutations in parallel
 * 
 * Algorithm:
 * 1. Find all empty cells in the board (must be exactly 5)
 * 2. Generate all possible permutations of values 1-9 for those 5 positions
 * 3. Use multiple worker threads to test permutations in parallel
 * 4. Stop when first valid solution is found
 * 
 * 👇️👇️👇️👇️👇️👇️👇️👇️
 * reference used :
 * https://codefinity.com/courses/v2/64fdb450-1405-4e74-8cd4-45fc2ebd37e5/58cddf1e-6e70-473c-b05e-7da5b4523a57/2e3a03e3-4099-426c-9af0-1fef1a1159af
 * helped me to understand the executors and threadpools
 * 👆️👆️👆️👆️👆️👆️👆️👆️
 * 
 * @author Zeyad
 */
public class SudokuSolver {
    /**
     * Solves the Sudoku puzzle using parallel permutation testing.
     * 
     * @param board The Sudoku board with some empty cells (value 0)
     * @return Array containing the solution (values for the 5 empty cells)
     * @throws InvalidGame if board doesn't have exactly 5 empty cells
     */
    public static int[] solve(int[][] board) throws InvalidGame {
        Game currentGame = new Game(board);
        int[] emptyPositions = currentGame.findEmptyCells(board);

        if (emptyPositions.length != 5) {
            throw new InvalidGame("Game must have exactly 5 empty cells, found: " + emptyPositions.length);
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        Board verifier = new PermutationVerifier(board, emptyPositions);
        PermutationIterator iterator = new PermutationIterator(5);

        final int[][] solutionWrapper = new int[1][];
        final boolean[] foundFlag = { false };

        while (iterator.hasNext() && !foundFlag[0]) { // Stops submitting when found
            final int[] permCopy = iterator.next().clone();

            executor.submit(() -> {
                if (foundFlag[0])
                    return; // Early exit if already found

                if (verifier.isValidPermutation(board, emptyPositions, permCopy)) {
                    solutionWrapper[0] = permCopy;
                    foundFlag[0] = true;
                }
            });
        }

        executor.shutdownNow(); // Clean up everything

        if (solutionWrapper[0] == null) {
            throw new InvalidGame("No valid solution exists");
        }

        return solutionWrapper[0];

    }

    /**
     * Test the solver.
     */
    /*
     * public static void main(String[] args) {
     * try {
     * int[][] testBoard = {
     * { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
     * { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
     * { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
     * { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
     * { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
     * { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
     * { 9, 6, 1, 5, 3, 7, 2, 8, 4 },
     * { 2, 8, 7, 4, 1, 0, 6, 3, 0 }, // 2 zeros: [7][5]=9, [7][8]=5
     * { 3, 4, 5, 2, 0, 6, 1, 0, 0 } // 3 zeros: [8][4]=8, [8][6]=1, [8][8]=9
     * };
     * // Solution should be: [9, 5, 8, 1, 9]
     * Game currentGame = new Game(testBoard);
     * 
     * int[] emptyPositions = currentGame.findEmptyCells(testBoard);
     * System.out.println("Empty cells found: " + emptyPositions.length);
     * 
     * int[] solution = SudokuSolver.solve(testBoard);
     * System.out.println("Solution found: " + Arrays.toString(solution));
     * 
     * } catch (Exception e) {
     * System.out.println("Error: " + e.getMessage());
     * }
     * }
     */
}