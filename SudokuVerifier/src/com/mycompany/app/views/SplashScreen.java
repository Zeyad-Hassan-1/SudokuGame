package com.mycompany.app.views;

import javax.swing.*;

public class SplashScreen extends JWindow {
    
    public SplashScreen(String gifPath) {
        // Load GIF
        ImageIcon gif = new ImageIcon(gifPath);
        
        // Create label with GIF
        JLabel label = new JLabel(gif);
        
        // Add to window
        add(label);
        
        // Size to GIF
        pack();
        
        // Center on screen
        setLocationRelativeTo(null);
    }
    
    /**
     * Show splash for specified milliseconds
     */
    public void show(int milliseconds) {
        setVisible(true);
        
        Timer timer = new Timer(milliseconds, e -> {
            setVisible(false);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}