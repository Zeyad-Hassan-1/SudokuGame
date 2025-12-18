package com.mycompany.app.controllers;

import com.mycompany.app.exceptions.InvalidGame;
import com.mycompany.app.exceptions.NotFoundException;
import com.mycompany.app.exceptions.SolutionInvalidException;
import com.mycompany.app.models.UserAction;

import java.io.IOException;

/**
 * Interface of the viewer representing the actions that are needed by the controller.
 */
interface Controllable {
    boolean[] getCatalog();

    int[][] getGame(char level) throws NotFoundException;

    void driveGames(String sourcePath) throws SolutionInvalidException;

    // A boolean array which says if a specifc cell is correct or invalid
    boolean[][] verifyGame(int[][] game);

    // contains the cell x, y and solution for each missing cell
    int[][] solveGame(int[][] game) throws InvalidGame;

    // Logs the user action
    void logUserAction(UserAction userAction) throws IOException;
}