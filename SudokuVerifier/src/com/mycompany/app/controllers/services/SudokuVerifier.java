/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.controllers.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.mycompany.app.models.SudokuData;

/**
 *
 * @author Hazem
 */
public class SudokuVerifier {

    protected SudokuData data;
    protected ArrayList<Duplicate> rowDuplicates;
    protected ArrayList<Duplicate> columnDuplicates;
    protected ArrayList<Duplicate> boxDuplicates;

    public SudokuVerifier(SudokuData data) {
        this.data = data;
        
        this.rowDuplicates = new ArrayList<>();
        this.columnDuplicates = new ArrayList<>();
        this.boxDuplicates = new ArrayList<>();
        
        verify();
    }

    /**
     * Make benefit of the following class when making GUI version
     */
    protected class Duplicate implements Comparable<Duplicate> {

        public static enum Type {
            ROW, COL, BOX
        };
        private Type type;
        private int typeIdx;
        private int value;
        private ArrayList<Integer> duplicatesIdx = new ArrayList<>();

        public Duplicate(Type type, int typeIdx, int value) {
            this.type = type;
            this.typeIdx = typeIdx;
            this.value = value;
        }

        public Duplicate(Type type, int typeIdx, int value, ArrayList<Integer> duplicatesIdx) {
            this(type, typeIdx, value);
            for (Integer val : duplicatesIdx) {
                this.duplicatesIdx.add(val);
            }
            Collections.sort(this.duplicatesIdx);
        }

        public void add(int duplicateIdx) {
            this.duplicatesIdx.add(duplicateIdx);
            Collections.sort(this.duplicatesIdx);
        }

        public Type getType() {
            return type;
        }

        public int getTypeIdx() {
            return typeIdx;
        }

        public int getValue() {
            return value;
        }

        public ArrayList<Integer> getDuplicatesIdx() {
            return new ArrayList<>(duplicatesIdx);
        }

        @Override
        public int compareTo(Duplicate other) {
            int typeComparison = this.type.compareTo(other.getType());
            if (typeComparison != 0) {
                return typeComparison;
            }

            if (this.typeIdx != other.getTypeIdx()) {
                return Integer.compare(this.typeIdx, other.getTypeIdx());
            }

            return Integer.compare(this.value, other.getValue());
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder(type.toString())
                    .append(" ")
                    .append(typeIdx)
                    .append(", #")
                    .append(value);
            str.append(", [");
            if (!duplicatesIdx.isEmpty()) {
                str.append(duplicatesIdx.get(0));
                for (int i = 1; i < duplicatesIdx.size(); i++) {
                    str.append(", ");
                    str.append(duplicatesIdx.get(i));
                }
            }
            str.append("]");
            return str.toString();
        }
    }

    @Override
    public String toString() {
        if (rowDuplicates.isEmpty() && columnDuplicates.isEmpty() && boxDuplicates.isEmpty()) {
            return "\nVALID";
        } else {
            StringBuilder str = new StringBuilder("\nINVALID\n\n");
            Collections.sort(rowDuplicates);
            Collections.sort(columnDuplicates);
            Collections.sort(boxDuplicates);
            for (Duplicate duplicate : rowDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            str.append("------------------------------------------\n");
            for (Duplicate duplicate : columnDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            str.append("------------------------------------------\n");
            for (Duplicate duplicate : boxDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            return str.toString();
        }
    }

    protected void verify() {
        verifyRows();
        verifyColumns();
        verifyBoxes();
    }

    protected void verifyRows() {
        for (int i = 0; i < data.getRows().size(); i++) {
            verifyRow(data.getRows().get(i), i);
        }
    }

    protected void verifyColumns() {
        for (int i = 0; i < data.getColumns().size(); i++) {
            verifyColumn(data.getColumns().get(i), i);
        }
    }

    protected void verifyBoxes() {
        for (int i = 0; i < data.getBoxes().size(); i++) {
            verifyBox(data.getBoxes().get(i), i);
        }
    }

    protected void verifyRow(ArrayList<Integer> row, int rowIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();

        for (int i = 0; i < row.size(); i++) {
            int value = row.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }

        for (int value : valuePositions.keySet()) {
            ArrayList<Integer> positions = valuePositions.get(value);
            if (positions.size() > 1) {
                Duplicate dup = new Duplicate(
                        Duplicate.Type.ROW,
                        rowIndex + 1,
                        value,
                        positions);
                rowDuplicates.add(dup);
            }
        }
    }

    protected void verifyColumn(ArrayList<Integer> column, int colIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();

        for (int i = 0; i < column.size(); i++) {
            int value = column.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }

        for (int value : valuePositions.keySet()) {
            ArrayList<Integer> positions = valuePositions.get(value);
            if (positions.size() > 1) {
                Duplicate dup = new Duplicate(
                        Duplicate.Type.COL,
                        colIndex + 1,
                        value,
                        positions);
                columnDuplicates.add(dup);
            }
        }
    }

    protected void verifyBox(ArrayList<Integer> box, int boxIndex) {
        HashMap<Integer, ArrayList<Integer>> valuePositions = new HashMap<>();

        for (int i = 0; i < box.size(); i++) {
            int value = box.get(i);
            valuePositions.putIfAbsent(value, new ArrayList<>());
            valuePositions.get(value).add(i + 1);
        }

        for (int value : valuePositions.keySet()) {
            ArrayList<Integer> positions = valuePositions.get(value);
            if (positions.size() > 1) {
                Duplicate dup = new Duplicate(
                        Duplicate.Type.BOX,
                        boxIndex + 1,
                        value,
                        positions);
                boxDuplicates.add(dup);
            }
        }
    }
}
