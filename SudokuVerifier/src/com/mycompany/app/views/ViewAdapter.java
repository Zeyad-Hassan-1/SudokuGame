package com.mycompany.app.views;

/**
 * Adapter class that bridges the GUI (presentation layer) and Controller (application layer).
 * 
 * This class implements the Controllable interface (which the GUI uses) and translates
 * between the simple data types the GUI works with (int[][], char) and the rich domain
 * objects the Controller uses (Game, DifficultyEnum).
 * 
 * Architecture:
 * GUI (works with int[][])
 *   ⬇️ calls
 * ViewAdapter (implements Controllable)
 *   ⬇️ translates and calls
 * Controller (implements Viewable, works with Game objects)
 * 
 * @author Menna
 */
public class ViewAdapter {

}
