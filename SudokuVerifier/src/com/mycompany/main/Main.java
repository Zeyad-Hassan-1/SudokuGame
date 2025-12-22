/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.app.controllers.SudokuController;
import com.mycompany.app.frontend.MainFrame;

/**
 *
 * @author Hazem
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SudokuController controller = new SudokuController();
        MainFrame mainFrame = new MainFrame(controller);
        java.awt.EventQueue.invokeLater(() -> mainFrame.setVisible(true));
    }
    
}
