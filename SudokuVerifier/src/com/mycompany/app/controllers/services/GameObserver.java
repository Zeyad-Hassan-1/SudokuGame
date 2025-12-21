package com.mycompany.app.controllers.services;

public interface GameObserver {
    
    /**
     * Called when a single cell value changes (user input).
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @param newValue New value (0-9, where 0 means empty)
     */
    void onCellChanged(int row, int col, int newValue);
    
    /**
     * Called when an undo operation is performed.
     * @param row Row index of the cell being restored
     * @param col Column index of the cell being restored
     * @param restoredValue The value being restored
     */
    void onUndo(int row, int col, int restoredValue);
    
    /**
     * Called when the game verification status changes.
     * @param verificationResult String in format "STATE|row,col|row,col|..."
     *                           where STATE is VALID, INVALID, or INCOMPLETE
     *                           and row,col pairs indicate invalid cells (if any)
     */
    void onGameVerified(String verificationResult);
    
    /**
     * Called when a new game is loaded.
     * @param difficulty The difficulty level of the new game
     */
    void onNewGameLoaded(String difficulty);
    
    /**
     * Called when a game is completed successfully (valid and no empty cells).
     */
    void onGameCompleted();
}
