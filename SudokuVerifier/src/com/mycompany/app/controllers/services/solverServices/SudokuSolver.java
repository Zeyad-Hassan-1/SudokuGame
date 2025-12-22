package com.mycompany.app.controllers.services.solverServices;

import java.util.Arrays;

import com.mycompany.app.exceptions.InvalidGame;
import com.mycompany.app.models.Game;

/**
 * Sudoku Solver using Iterator and Flyweight patterns with worker threads.
 *
 * - Iterator: PermutationIterator generates 9^5 combinations on-demand -
 * Flyweight: Board shared across threads, only permutation is copied - No
 * synchronized/volatile/thread-safe structures used
 *
 * @author Zeyad
 */
public class SudokuSolver {

    public static int[] solve(int[][] board) throws InvalidGame {
        Game currentGame = new Game(board);
        int[] emptyPositions = currentGame.findEmptyCells(board);

        if (emptyPositions.length != 5) {
            throw new InvalidGame("Game must have exactly 5 empty cells, found: " + emptyPositions.length);
        }

        int numWorkers = Runtime.getRuntime().availableProcessors();
        Board verifier = new PermutationVerifier(board, emptyPositions); // Flyweight
        PermutationIterator iterator = new PermutationIterator(5); // Iterator

        int[][] taskSlots = new int[numWorkers][]; // Task for each worker
        boolean[] hasTask = new boolean[numWorkers]; // Does worker have a task?
        boolean[] running = {true}; // Workers should keep running
        int[][] solution = {null}; // Found solution

        // Create and start workers
        Thread[] workers = new Thread[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            final int id = i;
            workers[i] = new Thread(() -> {
                while (running[0] && solution[0] == null) {
                    if (hasTask[id]) {
                        int[] perm = taskSlots[id];
                        hasTask[id] = false;

                        if (verifier.isValidPermutation(board, emptyPositions, perm)) {
                            solution[0] = perm;
                        }
                    }
                    Thread.yield();
                }
            });
            workers[i].start();
        }

        // Dispatch permutations to workers
        int w = 0;
        while (iterator.hasNext() && solution[0] == null) {
            if (!hasTask[w]) {
                taskSlots[w] = iterator.next();
                hasTask[w] = true;
                w = (w + 1) % numWorkers;
            } else {
                w = (w + 1) % numWorkers;
            }
        }

        // Shutdown and wait
        running[0] = false;
        for (Thread t : workers) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }

        if (solution[0] == null) {
            throw new InvalidGame("No valid solution exists");
        }
        return solution[0];
    }

    /**
     * Test the solver.
     */
     public static void main(String[] args) {
         try {
             int[][] testBoard = {
                     { 5, 3, 4, 6, 7, 8, 0, 1, 2 },
                     { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
                     { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
                     { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
                     { 4, 2, 6, 8, 5, 3, 7, 0, 1 },
                     { 7, 1, 3, 9, 0, 4, 8, 5, 6 },
                     { 9, 6, 1, 5, 0, 7, 2, 0, 4 },
                     { 2, 8, 7, 4, 1, 9, 6, 3, 5 }, 
                     { 3, 4, 5, 2, 8, 6, 1, 7, 9 } 
             };
             Game currentGame = new Game(testBoard);
             int[] emptyPositions = currentGame.findEmptyCells(testBoard);
             System.out.println("Empty cells found: " + emptyPositions.length);
             int[] solution = SudokuSolver.solve(testBoard);
             System.out.println("Solution found: " + Arrays.toString(solution));
         } catch (Exception e) {
             System.out.println("Error: " + e.getMessage());
         }
     }
}
