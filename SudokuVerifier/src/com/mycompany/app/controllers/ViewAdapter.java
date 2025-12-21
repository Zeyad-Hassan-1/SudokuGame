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
        String result = controller.verifyGame(game);

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
        // Use the controller's current board state (which has user updates)
        // instead of the passed board which may be out of sync
        int[][] actualBoard = controller.getCurrentBoard();
        if (actualBoard == null) {
            actualBoard = board;
        }
        
        Game game = new Game(actualBoard);
        int[] solution = controller.solveGame(game);
        int[] emptyPositions = game.findEmptyCells(actualBoard);
        int[][] result = new int[5][3];
        
        for (int i = 0; i < 5; i++) {
            int encodedPosition = emptyPositions[i];
            int row = encodedPosition / 9;
            int col = encodedPosition % 9;
            result[i][0] = row;
            result[i][1] = col;
            result[i][2] = solution[i];
        }
        
        return result;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toLogEntry());
    }

    @Override
    public int[][] getCurrentBoard() {
        return controller.getCurrentBoard();
    }

    @Override
    public int[][] loadCurrentGame() throws Exception {
        Game game = controller.loadCurrentGame();
        return (game != null) ? game.board : null;
    }

    /**
     * Set the current game in the controller (called when displaying a board).
     */
    public void setCurrentGame(int[][] board) throws IOException {
        if (controller instanceof SudokuController) {
            ((SudokuController) controller).setCurrentGame(board);
        }
    }

    @Override
    public java.util.List<String> getAllLogEntries() throws IOException {
        return ((SudokuController) controller).getAllLogEntries();
    }

    public UserAction logAndUpdateCell(int row, int col, int newValue) throws IOException {
        if (!(controller instanceof SudokuController)) {
            throw new IllegalStateException("Controller doesn't support cell updates");
        }
        SudokuController sudokuController = (SudokuController) controller;
        
        int previousValue = sudokuController.updateCellValue(row, col, newValue);
        UserAction action = new UserAction(row, col, newValue, previousValue);
        controller.logUserAction(action.toLogEntry());
        
        return action;
    }

    public UserAction undoLastAction() throws IOException {
        if (!(controller instanceof SudokuController)) {
            throw new IllegalStateException("Controller doesn't support undo");
        }
        SudokuController sudokuController = (SudokuController) controller;
        
        String lastEntry = sudokuController.removeLastActionFromLog();
        if (lastEntry == null) {
            return null;
        }
        
        UserAction undoneAction = UserAction.fromLogEntry(lastEntry);
        
        // Restore the cell value
        sudokuController.restoreCellValue(
            undoneAction.getX(), 
            undoneAction.getY(), 
            undoneAction.getPreviousValue()
        );
        
        return undoneAction;
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
