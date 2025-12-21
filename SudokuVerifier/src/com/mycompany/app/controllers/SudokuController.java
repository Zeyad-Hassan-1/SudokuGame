package com.mycompany.app.controllers;

import com.mycompany.app.exceptions.*;
import com.mycompany.app.models.*;
import com.mycompany.app.controllers.services.*;
import com.mycompany.app.utility.CSVReader;
import java.io.IOException;
import java.util.List;

/**
 * Main controller for the Sudoku game.
 * Implements Viewable interface and coordinates between model services.
 * Contains the core game logic.
 * 
 * @author Menna
 */

public class SudokuController implements Viewable {
    private final StorageManager storageManager;
    private final GameGenerator gameGenerator;

    private Game currentGame;
    private SudokuVerifier currentVerifier;

    public SudokuController() {
        this.storageManager = new StorageManager();
        this.gameGenerator = new GameGenerator();
        this.currentGame = null;
        this.currentVerifier = null;
    }

    @Override
    public Catalog getCatalog() {
        boolean hasUnfinished = storageManager.hasUnfinishedGame();
        boolean allModesExist = storageManager.hasGameForEachDifficulty();
        return new Catalog(hasUnfinished, allModesExist);
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            Game game = storageManager.loadGame(level);
            currentGame = game;
            currentVerifier = new SudokuVerifier(game.board);
            storageManager.saveCurrentGame(game);
            storageManager.clearGameLog();
            return game;
        } catch (IOException e) {
            throw new NotFoundException("Failed to load game: " + e.getMessage());
        }
    }

    @Override
    public void driveGames(Game source) throws SolutionInvalidException {
        SudokuVerifier verifier = new SudokuVerifier(source.board);
        if (verifier.getState() != SudokuVerifier.State.VALID) {
            throw new SolutionInvalidException("Source solution is not valid");
        }

        try {
            Game[] games = gameGenerator.generateAllLevels(source);
            storageManager.saveGame(games[0], DifficultyEnum.EASY);
            storageManager.saveGame(games[1], DifficultyEnum.MEDIUM);
            storageManager.saveGame(games[2], DifficultyEnum.HARD);
        } catch (IOException e) {
            throw new SolutionInvalidException("Failed to save games: " + e.getMessage());
        } catch (Exception e) {
            throw new SolutionInvalidException("Failed to generate games: " + e.getMessage());
        }
    }

    @Override
    public String verifyGame(Game game) {
        SudokuVerifier verifier = new SudokuVerifier(game.board);
        if (currentGame != null && game.board == currentGame.board) {
            currentVerifier = verifier;
        }
        return verifier.toString();
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        if (game.countEmptyCells(game.board) != 5) {
            throw new InvalidGame("Solver requires exactly 5 empty cells");
        }
        try {
            return SudokuSolver.solve(game.board);
        } catch (Exception e) {
            throw new InvalidGame("Failed to solve: " + e.getMessage());
        }
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        storageManager.logUserAction(userAction);
    }

    public UserAction logAndUpdateCell(int x, int y, int newValue) throws IOException {
        if (currentGame == null)
            throw new IllegalStateException("No game loaded");
        if (!isValidCoordinate(x) || !isValidCoordinate(y)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        if (!isValidValue(newValue)) {
            throw new IllegalArgumentException("Invalid value");
        }

        int previousValue = currentGame.board[x][y];
        UserAction action = new UserAction(x, y, newValue, previousValue);

        currentGame.board[x][y] = newValue;
        storageManager.logUserAction(action.toLogEntry());
        currentVerifier = new SudokuVerifier(currentGame.board);
        storageManager.saveCurrentGame(currentGame);

        return action;
    }

    public int updateCell(int row, int col, int value) throws IOException {
        UserAction action = logAndUpdateCell(row, col, value);
        return action.getPreviousValue();
    }

    public UserAction undoLastAction() throws IOException {
        UserAction undoneAction = storageManager.undoLastAction();
        if (undoneAction != null && currentGame != null) {
            currentGame.board[undoneAction.getX()][undoneAction.getY()] = undoneAction.getPreviousValue();
            currentVerifier = new SudokuVerifier(currentGame.board);
            storageManager.saveCurrentGame(currentGame);
        }
        return undoneAction;
    }

    public List<UserAction> readGameLog() throws IOException {
        return storageManager.readUserActions();
    }

    public Game loadSolutionFromFile(String filePath) throws IOException {
        try {
            int[][] board = CSVReader.readCSV(filePath, false);
            return new Game(board);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid CSV format: " + e.getMessage());
        }
    }

    public int[][] getCurrentBoard() {
        return (currentGame != null) ? currentGame.board : null;
    }

    public SudokuVerifier.State getCurrentGameState() {
        if (currentVerifier == null)
            throw new IllegalStateException("No game loaded");
        return currentVerifier.getState();
    }

    public boolean isCurrentGameCompleteAndValid() {
        return currentVerifier != null &&
                currentVerifier.getState() == SudokuVerifier.State.VALID;
    }

    public boolean handleGameCompletion() {
        if (!isCurrentGameCompleteAndValid())
            return false;

        try {
            storageManager.deleteCurrentGameWithLog();
            currentGame = null;
            currentVerifier = null;
            return true;
        } catch (IOException e) {
            System.err.println("Failed to delete: " + e.getMessage());
            return false;
        }
    }

    public int getEmptyCellCount() {
        return (currentGame != null) ? currentGame.countEmptyCells(currentGame.board) : 0;
    }

    public boolean shouldEnableSolveButton() {
        return getEmptyCellCount() == 5;
    }

    // public int countEmptyCells(int[][] board) {
    // int count = 0;
    // for (int i = 0; i < 9; i++) {
    // for (int j = 0; j < 9; j++) {
    // if (board[i][j] == 0) count++;
    // }
    // }
    // return count;
    // }

    // public int[] findEmptyCells(int[][] board) {
    // int[] emptyCells = new int[countEmptyCells(board)];
    // int index = 0;
    // for (int i = 0; i < 9; i++) {
    // for (int j = 0; j < 9; j++) {
    // if (board[i][j] == 0) emptyCells[index++] = i * 9 + j;
    // }
    // }
    // return emptyCells;
    // }

    private boolean isValidCoordinate(int coord) {
        return coord >= 0 && coord < 9;
    }

    private boolean isValidValue(int value) {
        return value >= 0 && value <= 9;
    }
}