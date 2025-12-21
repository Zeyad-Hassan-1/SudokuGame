package com.mycompany.app.controllers;

import com.mycompany.app.exceptions.*;
import com.mycompany.app.models.*;
import java.io.IOException;

/**
 * Adapter class that bridges the GUI (presentation layer) and Controller
 * (application layer).
 * 
 * Adapter pattern components :
 * Client : GUI
 * Target : @see Controllable
 * Adaptee : @see Viewable
 * Adapter : @see ViewAdapter
 * 
 * @author Menna
 */

public class ViewAdapter implements Controllable {
    private Viewable controller;

    public ViewAdapter(Viewable controller) {
        this.controller = controller;
    }

    @Override
    public boolean[] getCatalog() {
        Catalog catalog = controller.getCatalog();
        return new boolean[] { catalog.current, catalog.allModesExist };
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum difficulty = charToDifficulty(level);
        Game game = controller.getGame(difficulty);
        return game.board;
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        if (controller instanceof SudokuController) {
            try {
                SudokuController sudokuController = (SudokuController) controller;
                Game sourceGame = sudokuController.loadSolutionFromFile(sourcePath);
                controller.driveGames(sourceGame);
            } catch (IOException e) {
                throw new SolutionInvalidException("Error reading file: " + e.getMessage());
            }
        } else {
            throw new SolutionInvalidException("Controller doesn't support file loading");
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] board) {
        Game game = new Game(board);
        String result = controller.verifyGame(game);// Returns "STATE|row,col|row,col|..."

        boolean[][] verification = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                verification[i][j] = true;
            }
        }

        // Parse the result format: "STATE|row,col|row,col|..."
        String[] parts = result.split("\\|");

        // parts[0] = STATE (VALID/INVALID/INCOMPLETE)
        // parts[1+] = invalid cell coordinates
        if (parts.length > 1) {
            // Loop through all cell coordinates
            for (int i = 1; i < parts.length; i++) {
                if (parts[i].isEmpty())
                    continue;

                String[] coords = parts[i].split(",");
                if (coords.length == 2) {
                    try {
                        int row = Integer.parseInt(coords[0].trim());
                        int col = Integer.parseInt(coords[1].trim());

                        // Mark this cell as invalid (false)
                        if (row >= 0 && row < 9 && col >= 0 && col < 9) {
                            verification[row][col] = false;
                        }
                    } catch (NumberFormatException e) {
                        // Skip malformed coordinates
                        System.err.println("Invalid coordinate format: " + parts[i]);
                    }
                }
            }
        }

        return verification;
    }

    @Override
    public int[][] solveGame(int[][] board) throws InvalidGame {
        Game game = new Game(board);
        int[] solution = controller.solveGame(game);

        int[][] result = new int[5][3];
        int index = 0;
        for (int i = 0; i < 9 && index < 5; i++) {
            for (int j = 0; j < 9 && index < 5; j++) {
                if (board[i][j] == 0) {
                    result[index][0] = i;
                    result[index][1] = j;
                    result[index][2] = solution[index];
                    index++;
                }
            }
        }
        return result;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toLogEntry());
    }

    private DifficultyEnum charToDifficulty(char level) {
        switch (Character.toUpperCase(level)) {
            case 'E':
                return DifficultyEnum.EASY;
            case 'M':
                return DifficultyEnum.MEDIUM;
            case 'H':
                return DifficultyEnum.HARD;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }
}
