package com.mycompany.app.controllers.services;

import com.mycompany.app.models.DifficultyEnum;
import com.mycompany.app.models.Game;
import com.mycompany.app.exceptions.NotFoundException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service class responsible for all file I/O operations related to Sudoku games.
 * 
 * Folder Structure:
 * storage/
 *  easy/           (contains generated easy games)
 *  medium/         (contains generated medium games)
 *  hard/           (contains generated hard games)
 *  incomplete/     (contains current game + log file)
 *      current_game
 *      gameLog
 * 
 * File Format for log:
 * (2, 3, 5, 0)
 * (4, 7, 8, 0)
 * (One action per line)
 * 
 * @author nour
 */
public class StorageManager {
    
    private static final String BASE_STORAGE_DIR = "storage";
    private static final String EASY_DIR = BASE_STORAGE_DIR + File.separator + "easy";
    private static final String MEDIUM_DIR = BASE_STORAGE_DIR + File.separator + "medium";
    private static final String HARD_DIR = BASE_STORAGE_DIR + File.separator + "hard";
    private static final String INCOMPLETE_DIR = BASE_STORAGE_DIR + File.separator + "incomplete";
    
    private static final String CURRENT_GAME_FILE = INCOMPLETE_DIR + File.separator + "current_game";
    private static final String GAME_LOG_FILE = INCOMPLETE_DIR + File.separator + "gameLog";
    
    private final Random random;
    
    /**
     * Constructor that initializes the storage manager and creates necessary directories.
     */
    public StorageManager() {
        this.random = new Random();
        initializeDirectories();
    }
    
    /**
     * Creates all required directories if they don't exist.
     */
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            System.err.println("Error creating storage directories: " + e.getMessage());
        }
    }
    
    /**
     * Saves a game to the appropriate difficulty directory.
     * 
     * @param game The game to save
     * @param difficulty The difficulty level
     * @throws IOException if save operation fails
     */
    public void saveGame(Game game, DifficultyEnum difficulty) throws IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        String filename = generateUniqueFilename(directory);
        String filepath = directory + File.separator + filename;
        
        writeGameToFile(game, filepath);
    }
    
    /**
     * Retrieves a random game from the specified difficulty directory.
     * 
     * @param difficulty The difficulty level
     * @return Game instance
     * @throws NotFoundException if no games exist for the specified difficulty
     * @throws IOException if read operation fails
     */
    public Game getRandomGame(DifficultyEnum difficulty) throws NotFoundException, IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        File dir = new File(directory);
        
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        
        if (files == null || files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + difficulty);
        }
        
        // Select a random game file
        File selectedFile = files[random.nextInt(files.length)];
        return readGameFromFile(selectedFile.getAbsolutePath());
    }
    
    /**
     * Saves the current game being played.
     * 
     * @param game The current game
     * @throws IOException if save operation fails
     */
    public void saveCurrentGame(Game game) throws IOException {
        writeGameToFile(game, CURRENT_GAME_FILE);
    }
    
    /**
     * Retrieves the current game in progress.
     * 
     * @return Game instance, or null if no current game exists
     * @throws IOException if read operation fails
     */
    public Game getCurrentGame() throws IOException {
        File file = new File(CURRENT_GAME_FILE);
        if (!file.exists()) {
            return null;
        }
        return readGameFromFile(CURRENT_GAME_FILE);
    }
    
    /**
     * Checks if a current game exists.
     * 
     * @return true if a game is in progress, false otherwise
     */
    public boolean hasCurrentGame() {
        return new File(CURRENT_GAME_FILE).exists();
    }
    
    /**
     * Deletes the current game file.
     * 
     * @throws IOException if deletion fails
     */
    public void deleteCurrentGame() throws IOException {
        File file = new File(CURRENT_GAME_FILE);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
    }
    
    /**
     * Logs a user action to the game log file.
     * Format: (x, y, value, previousValue)
     * 
     * @param userAction String representation of the action
     * @throws IOException if write operation fails
     */
    public void logUserAction(String userAction) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_LOG_FILE, true))) {
            writer.write(userAction);
            writer.newLine();
        }
    }
    
    /**
     * Reads all user actions from the log file.
     * 
     * @return List of action strings
     * @throws IOException if read operation fails
     */
    public List<String> readGameLog() throws IOException {
        List<String> actions = new ArrayList<>();
        File logFile = new File(GAME_LOG_FILE);
        
        if (!logFile.exists()) {
            return actions;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actions.add(line);
            }
        }
        
        return actions;
    }
    
    /**
     * Clears the game log file.
     * 
     * @throws IOException if clear operation fails
     */

    
    /**
     * Checks if at least one game exists for each difficulty level.
     * 
     * @return true if all difficulty levels have games, false otherwise
     */

    
    /**
     * Counts the number of games available for a given difficulty.
     * 
     * @param difficulty The difficulty level
     * @return Number of games available
     */

    
    /**
     * Deletes all games for a specific difficulty level.
     * 
     * @param difficulty The difficulty level
     * @throws IOException if deletion fails
     */

    /**
     * Deletes all games from all difficulty directories.
     * 
     * @throws IOException if deletion fails
     */

    
    // ==================== Private Helper Methods ====================
    
    /**
     * Gets the directory path for a given difficulty level.
     */

    
    /**
     * Generates a unique filename for a game in the specified directory.
     */

    
    /**
     * Writes a game board to a CSV file.
     */
    
    
    /**
     * Reads a game board from a CSV file.
     */

    
    /**
     * Checks if a directory contains any game files.
     */
    private boolean hasGamesInDirectory(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        return files != null && files.length > 0;
    }



        public void clearGameLog() throws IOException {
        File logFile = new File(GAME_LOG_FILE);
        if (logFile.exists()) {
            Files.delete(logFile.toPath());
        }
    }

        public boolean allDifficultiesHaveGames() {
        return hasGamesInDirectory(EASY_DIR) && 
               hasGamesInDirectory(MEDIUM_DIR) && 
               hasGamesInDirectory(HARD_DIR);
    }


        public int getGameCount(DifficultyEnum difficulty) {
        String directory = getDirectoryForDifficulty(difficulty);
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        return (files != null) ? files.length : 0;
    }


    
    public void clearGamesForDifficulty(DifficultyEnum difficulty) throws IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        
        if (files != null) {
            for (File file : files) {
                Files.delete(file.toPath());
            }
        }
    }

        public void clearAllGames() throws IOException {
        clearGamesForDifficulty(DifficultyEnum.EASY);
        clearGamesForDifficulty(DifficultyEnum.MEDIUM);
        clearGamesForDifficulty(DifficultyEnum.HARD);
    }


    
    private String getDirectoryForDifficulty(DifficultyEnum difficulty) {
        switch (difficulty) {
            case EASY:
                return EASY_DIR;
            case MEDIUM:
                return MEDIUM_DIR;
            case HARD:
                return HARD_DIR;
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }
    }
    private String generateUniqueFilename(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        
        int maxNumber = 0;
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                try {
                    // Extract number from "game_X.csv"
                    String numberStr = name.substring(5, name.length() - 4);
                    int number = Integer.parseInt(numberStr);
                    maxNumber = Math.max(maxNumber, number);
                } catch (Exception e) {
                    // Ignore files that don't match the expected format
                }
            }
        }
        
        return "game_" + (maxNumber + 1) + ".csv";
    }
    private void writeGameToFile(Game game, String filepath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (int row = 0; row < 9; row++) {
                StringBuilder line = new StringBuilder();
                for (int col = 0; col < 9; col++) {
                    line.append(game.board[row][col]);
                    if (col < 8) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }
        private Game readGameFromFile(String filepath) throws IOException {
        int[][] board = new int[9][9];
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            int row = 0;
            
            while ((line = reader.readLine()) != null && row < 9) {
                String[] values = line.split(",");
                
                if (values.length != 9) {
                    throw new IOException("Invalid game file format: row " + (row + 1) + 
                                        " has " + values.length + " values instead of 9");
                }
                
                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(values[col].trim());
                }
                
                row++;
            }
            
            if (row < 9) {
                throw new IOException("Invalid game file format: only " + row + " rows found");
            }
        }
        
        return new Game(board);
    }





}





 















