package com.mycompany.app.controllers;

import com.mycompany.app.exceptions.InvalidGame;
import com.mycompany.app.exceptions.NotFoundException;
import com.mycompany.app.exceptions.SolutionInvalidException;
import com.mycompany.app.models.Catalog;
import com.mycompany.app.models.DifficultyEnum;

import java.io.IOException;

/**
 * Interface of the controller representing the actions that are needed by the viewer.
 */
public interface Viewable {
    
    /**
     * Returns catalog information about game availability.
     * @return Catalog with current (unfinished game exists) and allModesExist flags
     */
    Catalog getCatalog();
    
    /**
     * Returns a random game with the specified difficulty.
     * Note: the Game class is the representation of the sudoku game in the controller.
     * @param level The difficulty level (EASY, MEDIUM, HARD)
     * @return Game instance
     * @throws NotFoundException if no game found for the specified difficulty
     */
    Game getGame(DifficultyEnum level) throws NotFoundException;
    
    /**
     * Gets a sourceSolution and generates three levels of difficulty (easy, medium, hard).
     * @param source The solved Sudoku game to use as source
     * @throws SolutionInvalidException if the source solution is invalid or incomplete
     */
    void driveGames(Game source) throws SolutionInvalidException;
    
    /**
     * Given a game, verifies its state.
     * If invalid, returns invalid and locates the invalid duplicates.
     * If valid and complete, returns a completion value.
     * If valid and incomplete, returns another value.
     * The exact representation as a string is done as you best see fit.
     * @param game The game to verify
     * @return String representation of verification result
     */
    String verifyGame(Game game);
    
    /**
     * Returns the correct combination for the missing numbers.
     * Hint: So, there are many ways you can approach this, one way is
     * to have a way to map an index in the combination array to its location in the board.
     * One other way is to try to encode the location and the answer all in just one int.
     * @param game The game to solve
     * @return Array of solutions (encoded as ints - location and value)
     * @throws InvalidGame if the game cannot be solved (e.g., not exactly 5 empty cells)
     */
    int[] solveGame(Game game) throws InvalidGame;
    
    /**
     * Logs the user action.
     * @param userAction String representation of user action
     * @throws IOException if logging fails
     */
    void logUserAction(String userAction) throws IOException;
}

