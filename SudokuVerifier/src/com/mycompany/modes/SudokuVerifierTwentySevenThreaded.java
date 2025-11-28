/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modes;

import com.mycompany.core.SudokuVerifier;
import java.util.ArrayList;
import java.util.HashMap;

public class SudokuVerifierTwentySevenThreaded extends SudokuVerifier {
    
    public SudokuVerifierTwentySevenThreaded(ArrayList<ArrayList<Integer>> rows,
            ArrayList<ArrayList<Integer>> columns,
            ArrayList<ArrayList<Integer>> boxes) {
        super(rows, columns, boxes);
        this.rowDuplicates = new ArrayList<>();
        this.columnDuplicates = new ArrayList<>();
        this.boxDuplicates = new ArrayList<>();
        
        propagate();
    }

    @Override
    protected void propagate() {
        ArrayList<Thread> threads = new ArrayList<>();
        
        // Create 9 threads for rows
        for (int i = 0; i < 9; i++) {
            final int rowIndex = i;
            Thread rowThread = new Thread(() -> verifyRow(rows.get(rowIndex), rowIndex));
            threads.add(rowThread);
            rowThread.start();
        }
        
        // Create 9 threads for columns
        for (int i = 0; i < 9; i++) {
            final int colIndex = i;
            Thread colThread = new Thread(() -> verifyColumn(columns.get(colIndex), colIndex));
            threads.add(colThread);
            colThread.start();
        }
        
        // Create 9 threads for boxes
        for (int i = 0; i < 9; i++) {
            final int boxIndex = i;
            Thread boxThread = new Thread(() -> verifyBox(boxes.get(boxIndex), boxIndex));
            threads.add(boxThread);
            boxThread.start();
        }
        
        // Wait for all 27 threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during verification", e);
            }
        }
    }

    @Override
    protected void verify() {
    }

    @Override
    protected void verifyRows() {
    }

    @Override
    protected void verifyColumns() {
    }

    @Override
    protected void verifyBoxes() {
    }

    @Override
    protected void verifyRow(ArrayList<Integer> row, int rowIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();
        
        for (int i = 0; i < row.size(); i++) {
            int value = row.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }
        
        synchronized (rowDuplicates) {
            for (int value : valuePositions.keySet()) {
                ArrayList<Integer> positions = valuePositions.get(value);
                if (positions.size() > 1) {
                    Duplicate dup = new Duplicate(
                        Duplicate.Type.ROW, 
                        rowIndex + 1, 
                        value, 
                        positions
                    );
                    rowDuplicates.add(dup);
                }
            }
        }
    }

    @Override
    protected void verifyColumn(ArrayList<Integer> column, int colIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();
        
        for (int i = 0; i < column.size(); i++) {
            int value = column.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }
        
        synchronized (columnDuplicates) {
            for (int value : valuePositions.keySet()) {
                ArrayList<Integer> positions = valuePositions.get(value);
                if (positions.size() > 1) {
                    Duplicate dup = new Duplicate(
                        Duplicate.Type.COL, 
                        colIndex + 1, 
                        value, 
                        positions
                    );
                    columnDuplicates.add(dup);
                }
            }
        }
    }

    @Override
    protected void verifyBox(ArrayList<Integer> box, int boxIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();
        
        for (int i = 0; i < box.size(); i++) {
            int value = box.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }
        
        synchronized (boxDuplicates) {
            for (int value : valuePositions.keySet()) {
                ArrayList<Integer> positions = valuePositions.get(value);
                if (positions.size() > 1) {
                    Duplicate dup = new Duplicate(
                        Duplicate.Type.BOX, 
                        boxIndex + 1, 
                        value, 
                        positions
                    );
                    boxDuplicates.add(dup);
                }
            }
        }
    }
}