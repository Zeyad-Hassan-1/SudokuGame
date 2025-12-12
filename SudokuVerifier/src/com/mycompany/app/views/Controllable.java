package com.mycompany.app.views;

import com.mycompany.app.exceptions.InvalidGame;
import com.mycompany.app.exceptions.NotFoundException;
import com.mycompany.app.exceptions.SolutionInvalidException;
import com.mycompany.app.models.Catalog;
import com.mycompany.app.models.UserAction;

import java.io.IOException;

/**
 * Interface of the viewer representing the actions that are needed by the controller.
 */
public interface Controllable {
    
    /**
     * Returns catalog information about game availability.
     * @return Catalog with current (unfinished game exists) and allModesExist flags
     */
    Catalog getCatalog();
    
    /**
     * Returns a game board with the specified difficulty level.
     * @param level Character representing difficulty level ('e' for easy, 'm' for medium, 'h' for hard)
     * @return int[][] representation of the game board (9x9)
     * @throws NotFoundException if no game found for the specified difficulty
     */
    int[][] getGame(char level) throws NotFoundException;
    
    /**
     * Gets a sourceSolution and generates three levels of difficulty (easy, medium, hard).
     * @param source int[][] representation of the solved Sudoku game to use as source
     * @throws SolutionInvalidException if the source solution is invalid or incomplete
     */
    void driveGames(int[][] source) throws SolutionInvalidException;
    
    /**
     * Verifies a game board and returns a boolean array indicating which cells are correct or invalid.
     * A boolean array which says if a specific cell is correct or invalid.
     * @param game int[][] representation of the game board (9x9)
     * @return bool[][] array where true indicates correct cell, false indicates invalid cell
     */
    boolean[][] verifyGame(int[][] game);
    
    /**
     * Solves the game and returns the solution for missing cells.
     * Contains the cell x, y and solution for each missing cell.
     * @param game int[][] representation of the game board (9x9)
     * @return int[][] array containing [x, y, solution] for each missing cell
     * @throws InvalidGame if the game cannot be solved (e.g., not exactly 5 empty cells)
     */
    int[][] solveGame(int[][] game) throws InvalidGame;
    
    /**
     * Logs the user action.
     * @param userAction UserAction object containing action details
     * @throws IOException if logging fails
     */
    void logUserAction(UserAction userAction) throws IOException;
}

