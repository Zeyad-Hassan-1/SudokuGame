package com.mycompany.app.controllers.services;

/**
 * Service class responsible for all file I/O operations related to Sudoku games.
 * Folder Structure:
 * storage/
 *  easy/           (contains generated easy games)
 *  medium/         (contains generated medium games)
 *  hard/           (contains generated hard games)
 *  incomplete/     (contains current game + log file)
 *      current_game
 *      gameLog
 * File Format for log:
 * (2, 3, 5, 0)
 * (4, 7, 8, 0)
 * (One action per line)
 * @author
 */
public class StorageManager {

}
