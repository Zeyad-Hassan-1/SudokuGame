/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modes;

import com.mycompany.core.SudokuData;
import com.mycompany.core.SudokuVerifier;
import java.util.ArrayList;

public class SudokuVerifierTwentySevenThreaded extends SudokuVerifier {

    public SudokuVerifierTwentySevenThreaded(SudokuData data) {
        super(data);
        verify(); //ignore warning
    }

    @Override
    /**
     * @see https://www.geeksforgeeks.org/java/lambda-expressions-java-8/
     */
    protected void verify() {
        ArrayList<Thread> threads = new ArrayList<>();
        
        for (int i = 0; i < 9; i++) {
            final int rowIndex = i;
            Thread rowThread = new Thread(() -> verifyRow(data.getRows().get(rowIndex), rowIndex));
            threads.add(rowThread);
            rowThread.start();
        }

        for (int i = 0; i < 9; i++) {
            final int colIndex = i;
            Thread colThread = new Thread(() -> verifyColumn(data.getColumns().get(colIndex), colIndex));
            threads.add(colThread);
            colThread.start();
        }

        for (int i = 0; i < 9; i++) {
            final int boxIndex = i;
            Thread boxThread = new Thread(() -> verifyBox(data.getBoxes().get(boxIndex), boxIndex));
            threads.add(boxThread);
            boxThread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during verification", e);
            }
        }
    }
}