package com.mycompany.app.controllers.services;

import java.util.List;
import com.mycompany.app.models.Game;
import com.mycompany.app.utility.RandomPairs;

/**
 * Service class for generating Sudoku games of different difficulty levels.
 * 
 *Generation process:
 * 
 *   Start with a solved (valid) Sudoku board
 *   Randomly remove a specified number of cells
 *   Replace removed cells with 0 (empty)
 * 
 *Uses @see RandomPairs utility to select cells to remove
 * 
 * @author Menna
 */
public class GameGenerator {
    private RandomPairs randomPairs;
    
    public GameGenerator() {
        this.randomPairs = new RandomPairs();
    }
    
    
    public Game generateGame(Game solvedGame, String difficulty) {
        int[][] board = deepCopyBoard(solvedGame.board);
        int cellsToRemove = getCellsToRemove(difficulty);
        
        
        List<int[]> positions = randomPairs.generateDistinctPairs(cellsToRemove);
        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            board[row][col] = 0; 
        }
        return new Game(board);
    }
    
    
    public Game[] generateAllLevels(Game solvedGame) {
        Game[] games = new Game[3];
        games[0] = generateGame(solvedGame, "EASY");
        games[1] = generateGame(solvedGame, "MEDIUM");
        games[2] = generateGame(solvedGame, "HARD");
        return games;
    }
    
    private int getCellsToRemove(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY": return 10;
            case "MEDIUM": return 20;
            case "HARD": return 25;
            default: throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }
    private int[][] deepCopyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

}
