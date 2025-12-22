package com.mycompany.app.controllers;

import com.mycompany.app.exceptions.*;
import com.mycompany.app.models.*;
import com.mycompany.app.controllers.services.*;
import com.mycompany.app.controllers.services.solverServices.SudokuSolver;
import com.mycompany.app.controllers.services.storageServices.GameGenerator;
import com.mycompany.app.controllers.services.storageServices.StorageManager;
import com.mycompany.app.utility.CSVReader;
import java.io.IOException;
import java.util.List;

/**
 * Main controller for the Sudoku game.
 * Implements Viewable interface and coordinates between model services.
 * Contains the core game logic.
 * 
 * OBSERVER PATTERN:
 * - This class is the Subject
 * - GUI components are Observers (implement GameObserver interface)
 * - Notifies observers when game state changes
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
    public Game getUnfinishedGame() throws NotFoundException 
    {
        try{
            Game game = storageManager.readGameFromFile(StorageManager.CURRENT_GAME_FILE);
            currentGame = game;
            currentVerifier = new SudokuVerifier(game.board);
            
            return game;
        } catch (IOException e) {
            throw new NotFoundException("Failed to load game: " + e.getMessage());
        }
    }
    
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            Game game = storageManager.loadGame(level);
            currentGame = game;
            currentVerifier = new SudokuVerifier(game.board);
            storageManager.deleteCurrentGameWithLog();
            storageManager.saveCurrentGame(game);

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
        String result = verifier.toString();

       return result;
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

    /**
     * Updates a cell value (without UserAction - for internal use).
     * Returns the previous value.
     * @param row
     * @param col
     * @param newValue
     * @return 
     * @throws java.io.IOException
     */
    public int updateCellValue(int row, int col, int newValue) throws IOException {
        if (currentGame == null)
            throw new IllegalStateException("No game loaded");
        if (!isValidCoordinate(row) || !isValidCoordinate(col)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
        if (!isValidValue(newValue)) {
            throw new IllegalArgumentException("Invalid value");
        }

        int previousValue = currentGame.board[row][col];
        currentGame.board[row][col] = newValue;
        currentVerifier = new SudokuVerifier(currentGame.board);
        storageManager.saveCurrentGame(currentGame);
        return previousValue;
    }

    /**
     * Restores a cell to a previous value (without UserAction - for internal use).
     */
    public void restoreCellValue(int row, int col, int restoredValue) throws IOException {
        if (currentGame == null)
            throw new IllegalStateException("No game loaded");
        
        currentGame.board[row][col] = restoredValue;
        currentVerifier = new SudokuVerifier(currentGame.board);
        storageManager.saveCurrentGame(currentGame);
    }

    /**
     * Removes the last action from log and returns its log entry string.
     */
    public String removeLastActionFromLog() throws IOException {
        List<String> logEntries = storageManager.readGameLog();
        if (logEntries.isEmpty()) {
            return null;
        }
        
        String lastEntry = logEntries.remove(logEntries.size() - 1);
        
        // Rewrite log without last entry
        storageManager.clearGameLog();
        for (String entry : logEntries) {
            storageManager.logUserAction(entry);
        }
        
        return lastEntry;
    }

    /**
     * Gets all log entries as strings.
     */
    public List<String> getAllLogEntries() throws IOException {
        return storageManager.readGameLog();
    }

    /**
     * Gets the current game board.
     */
    public int[][] getCurrentBoard() {
        return (currentGame != null) ? currentGame.board : null;
    }

    public Game loadSolutionFromFile(String filePath) throws IOException {
        try {
            int[][] board = CSVReader.readCSV(filePath, false);
            return new Game(board);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid CSV format: " + e.getMessage());
        }
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

    private boolean isValidCoordinate(int coord) {
        return coord >= 0 && coord < 9;
    }

    private boolean isValidValue(int value) {
        return value >= 0 && value <= 9;
    }
}