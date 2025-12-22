/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.app.controllers.services.SudokuVerifier;
import com.mycompany.app.utility.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Hazem
 */
import com.mycompany.app.views.*;
public class Main {
    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen("SudokuVerifier/resources/loading.gif");
        splash.setVisible(true);
        
        // Do your initialization
        new Thread(() -> {
            try {
                // Initialize stuff here
                Thread.sleep(2000); // Or actual initialization time
                
                // Close splash
                SwingUtilities.invokeLater(() -> {
                    splash.setVisible(false);
                    splash.dispose();
                });
                
                // Launch main window
                SwingUtilities.invokeLater(() -> {
                    // new YourMainWindow().setVisible(true);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}