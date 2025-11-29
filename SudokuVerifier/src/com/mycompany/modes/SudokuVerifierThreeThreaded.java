/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modes;

import com.mycompany.core.SudokuData;
import com.mycompany.core.SudokuVerifier;

public class SudokuVerifierThreeThreaded extends SudokuVerifier {

    public SudokuVerifierThreeThreaded(SudokuData data) {
        super(data);
        verify();//ignore warning
    }

    @Override
    /**
     * @see https://www.geeksforgeeks.org/java/lambda-expressions-java-8/
     */
    protected void verify() {
        // Create three threads: one for rows, one for columns, one for boxes
        Thread rowThread = new Thread(() -> verifyRows());
        Thread columnThread = new Thread(() -> verifyColumns());
        Thread boxThread = new Thread(() -> verifyBoxes());

        // Start all threads

        rowThread.start();
        columnThread.start();
        boxThread.start();

        // Wait for all threads to complete
        try {
            rowThread.join();
            columnThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during verification", e);
        }
    }
}