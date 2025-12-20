package com.mycompany.app.controllers.services;


import com.mycompany.app.models.DifficultyEnum;
import com.mycompany.app.models.Game;
import com.mycompany.app.models.UserAction;
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
    private static final String BASE_STORAGE_DIR = "storage";
    private static final String EASY_DIR = BASE_STORAGE_DIR + File.separator + "easy";
    private static final String MEDIUM_DIR = BASE_STORAGE_DIR + File.separator + "medium";
    private static final String HARD_DIR = BASE_STORAGE_DIR + File.separator + "hard";
    private static final String INCOMPLETE_DIR = BASE_STORAGE_DIR + File.separator + "incomplete";
    
    private static final String CURRENT_GAME_FILE = INCOMPLETE_DIR + File.separator + "current_game.csv";
    private static final String GAME_LOG_FILE = INCOMPLETE_DIR + File.separator + "gameLog.txt";
    
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
    
    
    
    public int[][] loadGameBoard(DifficultyEnum level) throws NotFoundException, IOException {
        Game game = getRandomGame(level);
        return game.board;
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
    
    public void saveGame(int[][] board, DifficultyEnum difficulty) throws IOException {
        saveGame(new Game(board), difficulty);
    }
    
    public Game getRandomGame(DifficultyEnum difficulty) throws NotFoundException, IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        
        if (files == null || files.length == 0) {
            throw new NotFoundException("No games found for difficulty: " + difficulty);
        }
        
        File selectedFile = files[random.nextInt(files.length)];
        return readGameFromFile(selectedFile.getAbsolutePath());
    }
    
    
    
    public void saveCurrentGame(Game game) throws IOException {
        clearIncompleteFolder();
        writeGameToFile(game, CURRENT_GAME_FILE);
    }
    
    public void saveCurrentGame(int[][] board) throws IOException {
        saveCurrentGame(new Game(board));
    }
    
    public Game getCurrentGame() throws IOException {
        File file = new File(CURRENT_GAME_FILE);
        if (!file.exists()) return null;
        return readGameFromFile(CURRENT_GAME_FILE);
    }
    
    public int[][] getCurrentGameBoard() throws IOException {
        Game game = getCurrentGame();
        return (game != null) ? game.board : null;
    }
    
    public boolean hasCurrentGame() {
        return new File(CURRENT_GAME_FILE).exists();
    }
    
    public void deleteCurrentGame() throws IOException {
        clearIncompleteFolder();
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
    
    public void logUserAction(UserAction action) throws IOException {
        logUserAction(action.toLogEntry());
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
    
    public List<UserAction> readUserActions() throws IOException {
        List<String> logEntries = readGameLog();
        List<UserAction> actions = new ArrayList<>();
        
        for (String entry : logEntries) {
            try {
                actions.add(UserAction.fromLogEntry(entry));
            } catch (Exception e) {
                System.err.println("Failed to parse: " + entry);
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
    
    public UserAction undoLastAction() throws IOException {
        List<UserAction> actions = readUserActions();
        if (actions.isEmpty()) return null;
        
        UserAction lastAction = actions.remove(actions.size() - 1);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_LOG_FILE))) {
            for (UserAction action : actions) {
                writer.write(action.toLogEntry());
                writer.newLine();
            }
        }
        return lastAction;
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
    
    public void deleteGameFromFolder(DifficultyEnum difficulty, String filename) throws IOException {
        String directory = getDirectoryForDifficulty(difficulty);
        String filepath = directory + File.separator + filename;
        File file = new File(filepath);
        if (file.exists()) {
            Files.delete(file.toPath());
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
                } catch (Exception e) {}
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
    
    private boolean hasGamesInDirectory(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.startsWith("game_") && name.endsWith(".csv"));
        return files != null && files.length > 0;
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