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
 * Service class responsible for all file I/O operations related to Sudoku games.
 * 
 * Folder Structure:
 * storage/
 *  easy/           (contains generated easy games)
 *  medium/         (contains generated medium games)
 *  hard/           (contains generated hard games)
 *  incomplete/     (contains current game + log file)
 *      current_game.csv
 *      gameLog.txt
 * 
 * File Format for log:
 * (2, 3, 5, 0)
 * (4, 7, 8, 0)
 * (One action per line)
 * 
 * @author nour
 */


public class StorageManager {
    public static final String BASE_STORAGE_DIR = "storage";
    public static final String EASY_DIR = BASE_STORAGE_DIR + File.separator + "easy";
    public static final String MEDIUM_DIR = BASE_STORAGE_DIR + File.separator + "medium";
    public static final String HARD_DIR = BASE_STORAGE_DIR + File.separator + "hard";
    public static final String INCOMPLETE_DIR = BASE_STORAGE_DIR + File.separator + "incomplete";
    
    public static final String CURRENT_GAME_FILE = INCOMPLETE_DIR + File.separator + "current_game.csv";
    public static final String GAME_LOG_FILE = INCOMPLETE_DIR + File.separator + "gameLog.txt";
    
    private final Random random;
    
    public StorageManager() {
        this.random = new Random();
        initializeDirectories();
    }
    
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
    
    public boolean hasUnfinishedGame() {
        return new File(CURRENT_GAME_FILE).exists();
    }
    
    public boolean hasGameForEachDifficulty() {
        return hasGamesInDirectory(EASY_DIR) && 
               hasGamesInDirectory(MEDIUM_DIR) && 
               hasGamesInDirectory(HARD_DIR);
    }
    
    public Game loadGame(DifficultyEnum level) throws NotFoundException, IOException {
        return getRandomGame(level);
    }
    
    public void saveGame(Game game, DifficultyEnum difficulty) throws IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        String filename = generateUniqueFilename(directory);
        String filepath = directory + File.separator + filename;
        writeGameToFile(game, filepath);
    }
    
    private Game getRandomGame(DifficultyEnum difficulty) throws NotFoundException, IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        File[] files = getGameFiles(directory);
        
        if (files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + difficulty);
        }
        
        File selectedFile = files[random.nextInt(files.length)];
        return readGameFromFile(selectedFile.getAbsolutePath());
    }
    
    public void saveCurrentGame(Game game) throws IOException {
        clearIncompleteFolder();
        writeGameToFile(game, CURRENT_GAME_FILE);
    }
    
    public void deleteCurrentGameWithLog() throws IOException {
        clearIncompleteFolder();
    }
    
    public void logUserAction(String userAction) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_LOG_FILE, true))) {
            writer.write(userAction);
            writer.newLine();
        }
    }
    
    public List<String> readGameLog() throws IOException {
        List<String> actions = new ArrayList<>();
        File logFile = new File(GAME_LOG_FILE);
        if (!logFile.exists()) return actions;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                actions.add(line.trim());
            }
        }
        return actions;
    }
    
    public void clearGameLog() throws IOException {
        File logFile = new File(GAME_LOG_FILE);
        if (logFile.exists()) {
            Files.delete(logFile.toPath());
        }
    }
    
    private String getDirectoryForDifficulty(DifficultyEnum difficulty) {
        switch (difficulty) {
            case EASY: return EASY_DIR;
            case MEDIUM: return MEDIUM_DIR;
            case HARD: return HARD_DIR;
            default: throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }
    }
    
    private String generateUniqueFilename(String directory) {
        File[] files = getGameFiles(directory);
        int maxNumber = 0;
        
        for (File file : files) {
            String name = file.getName();
            try {
                String numberStr = name.substring(5, name.length() - 4);
                int number = Integer.parseInt(numberStr);
                maxNumber = Math.max(maxNumber, number);
            } catch (Exception e) {
                // Skip invalid filenames
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
    
    public Game readGameFromFile(String filepath) throws IOException {
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
    
    private File[] getGameFiles(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        return (files != null) ? files : new File[0];
    }
    
    private boolean hasGamesInDirectory(String directory) {
        return getGameFiles(directory).length > 0;
    }
    
    private void clearIncompleteFolder() throws IOException {
        File incompleteDir = new File(INCOMPLETE_DIR);
        File[] files = incompleteDir.listFiles();
        if (files != null) {
            for (File file : files) {
                Files.delete(file.toPath());
            }
        }
    }
}