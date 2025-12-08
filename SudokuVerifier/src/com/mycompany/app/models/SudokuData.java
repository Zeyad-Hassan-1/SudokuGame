/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.models;

import java.util.ArrayList;

/**
 *
 * @author Hazem
 */
public class SudokuData {
        private ArrayList<ArrayList<Integer>> rows;
        private ArrayList<ArrayList<Integer>> columns;
        private ArrayList<ArrayList<Integer>> boxes;

        public SudokuData() {
            rows = new ArrayList<>();
            columns = new ArrayList<>();
            boxes = new ArrayList<>();
            
            for (int i = 0; i < 9; i++) {
                rows.add(new ArrayList<>());
                columns.add(new ArrayList<>());
                boxes.add(new ArrayList<>());
            }
        }

    /**
     * @return the rows
     */
    public ArrayList<ArrayList<Integer>> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(ArrayList<ArrayList<Integer>> rows) {
        this.rows = rows;
    }

    /**
     * @return the columns
     */
    public ArrayList<ArrayList<Integer>> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(ArrayList<ArrayList<Integer>> columns) {
        this.columns = columns;
    }

    /**
     * @return the boxes
     */
    public ArrayList<ArrayList<Integer>> getBoxes() {
        return boxes;
    }

    /**
     * @param boxes the boxes to set
     */
    public void setBoxes(ArrayList<ArrayList<Integer>> boxes) {
        this.boxes = boxes;
    }
}
