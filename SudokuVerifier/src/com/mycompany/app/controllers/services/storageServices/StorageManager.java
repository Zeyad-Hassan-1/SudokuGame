package com.mycompany.app.controllers.services.storageServices;

import com.mycompany.app.models.DifficultyEnum;
import com.mycompany.app.models.Game;
import com.mycompany.app.exceptions.NotFoundException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Storage manager - handles file I/O for games.
 * 
 * Folder Structure:
 * storage/
 *  easy/           (generated easy games)
 *  medium/         (generated medium games)
 *  hard/           (generated hard games)
 *  incomplete/     (current game + log)
 *      current_game.csv
 *      gameLog.txt
 * 
 * Log format: (x, y, value, previousValue)
 * 
 * @author nour
 */
public class StorageManager {
    private final String BASE_DIR;
    private final String EASY_DIR;
    private final String MEDIUM_DIR;
    private final String HARD_DIR;
    private final String INCOMPLETE_DIR;
    private final String CURRENT_GAME_FILE;
    private final String GAME_LOG_FILE;
    
    private final Random random;
    
    public StorageManager() {
        // Determine base directory - check if running from SudokuVerifier or parent
        String workingDir = System.getProperty("user.dir");
        if (workingDir.endsWith("SudokuVerifier")) {
            BASE_DIR = "storage";
        } else if (new File(workingDir + File.separator + "SudokuVerifier" + File.separator + "storage").exists()) {
            BASE_DIR = "SudokuVerifier" + File.separator + "storage";
        } else if (new File(workingDir + File.separator + "storage").exists()) {
            BASE_DIR = "storage";
        } else {
            // Default - create in SudokuVerifier/storage
            BASE_DIR = "SudokuVerifier" + File.separator + "storage";
        }
        
        EASY_DIR = BASE_DIR + File.separator + "easy";
        MEDIUM_DIR = BASE_DIR + File.separator + "medium";
        HARD_DIR = BASE_DIR + File.separator + "hard";
        INCOMPLETE_DIR = BASE_DIR + File.separator + "incomplete";
        CURRENT_GAME_FILE = INCOMPLETE_DIR + File.separator + "current_game.csv";
        GAME_LOG_FILE = INCOMPLETE_DIR + File.separator + "gameLog.txt";
        
        this.random = new Random();
        initializeDirectories();
        
        System.out.println("StorageManager initialized with base: " + new File(BASE_DIR).getAbsolutePath());
    }
    
    // ==================== DIRECTORY SETUP ====================
    
    private void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(EASY_DIR));
            Files.createDirectories(Paths.get(MEDIUM_DIR));
            Files.createDirectories(Paths.get(HARD_DIR));
            Files.createDirectories(Paths.get(INCOMPLETE_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
    
    // ==================== CATALOG QUERIES ====================
    
    public boolean hasUnfinishedGame() {
        return new File(CURRENT_GAME_FILE).exists();
    }
    
    public boolean hasGameForEachDifficulty() {
        return hasGamesInDirectory(EASY_DIR) && 
               hasGamesInDirectory(MEDIUM_DIR) && 
               hasGamesInDirectory(HARD_DIR);
    }
    
    private boolean hasGamesInDirectory(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        return files != null && files.length > 0;
    }
    
    // ==================== GAME STORAGE ====================
    
    /**
     * Load a random game of the specified difficulty.
     */
    public Game loadGame(DifficultyEnum difficulty) throws NotFoundException, IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        
        if (files == null || files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + difficulty);
        }
        
        File selectedFile = files[random.nextInt(files.length)];
        return readGameFromFile(selectedFile.getAbsolutePath());
    }
    
    /**
     * Save a generated game to the appropriate difficulty folder.
     */
    public void saveGame(Game game, DifficultyEnum difficulty) throws IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        String filename = generateUniqueFilename(directory);
        String filepath = directory + File.separator + filename;
        writeGameToFile(game, filepath);
    }
    
    /**
     * Save the current in-progress game.
     */
    public void saveCurrentGame(Game game) throws IOException {
        writeGameToFile(game, CURRENT_GAME_FILE);
    }
    
    /**
     * Load the current/incomplete game from storage.
     */
    public Game loadCurrentGame() throws IOException {
        if (!hasUnfinishedGame()) {
            return null;
        }
        return readGameFromFile(CURRENT_GAME_FILE);
    }

    /**
     * Delete the current game and its log (called when game is completed).
     */
    public void deleteCurrentGameWithLog() throws IOException {
        File currentGame = new File(CURRENT_GAME_FILE);
        File logFile = new File(GAME_LOG_FILE);

        if (currentGame.exists()) {
            Files.delete(currentGame.toPath());
        }
        if (logFile.exists()) {
            Files.delete(logFile.toPath());
        }
    }
    
    // ==================== LOGGING ====================
    
    /**
     * Append a user action to the log file.
     * @param userAction String in format "(x, y, value, previousValue)"
     */
    public void logUserAction(String userAction) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_LOG_FILE, true))) {
            writer.write(userAction);
            writer.newLine();
        }
    }
    
    /**
     * Read all log entries as a list of strings.
     * NEEDED BY CONTROLLER for undo functionality.
     */
    public List<String> readGameLog() throws IOException {
        List<String> entries = new ArrayList<>();
        File logFile = new File(GAME_LOG_FILE);
        
        if (!logFile.exists()) {
            return entries;  // Return empty list if no log file
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                entries.add(line.trim());
            }
        }
        return entries;
    }
    
    /**
     * Clear the game log (called when loading a new game).
     */
    public void clearGameLog() throws IOException {
        File logFile = new File(GAME_LOG_FILE);
        if (logFile.exists()) {
            Files.delete(logFile.toPath());
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    private String getDirectoryForDifficulty(DifficultyEnum difficulty) {
        switch (difficulty) {
            case EASY: return EASY_DIR;
            case MEDIUM: return MEDIUM_DIR;
            case HARD: return HARD_DIR;
            default: throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
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
                    String numberStr = name.substring(5, name.length() - 4);
                    int number = Integer.parseInt(numberStr);
                    maxNumber = Math.max(maxNumber, number);
                } catch (Exception e) {
                    // Skip files with invalid naming
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
                    if (col < 8) line.append(",");
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
                    throw new IOException("Invalid row: " + (row + 1));
                }
                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(values[col].trim());
                }
                row++;
            }
            
            if (row < 9) {
                throw new IOException("Only " + row + " rows found");
            }
        }
        return new Game(board);
    }
}