/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.core;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Hazem
 */
public abstract class SudokoVerifier {

    protected ArrayList<ArrayList<Integer>> rows;
    protected ArrayList<ArrayList<Integer>> columns;
    protected ArrayList<ArrayList<Integer>> boxes;
    protected ArrayList<Duplicate> rowDuplicates;
    protected ArrayList<Duplicate> columnDuplicates;
    protected ArrayList<Duplicate> boxDuplicates;

    public SudokoVerifier(ArrayList<ArrayList<Integer>> rows,
            ArrayList<ArrayList<Integer>> columns,
            ArrayList<ArrayList<Integer>> boxes) {
        this.rows = rows;
        this.columns = columns;
        this.boxes = boxes;
    }

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
            return (ArrayList<Integer>) duplicatesIdx.clone();
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
            str.append("[");
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
            return "VALID";
        } else {
            StringBuilder str = new StringBuilder("INVALID\n\n");
            Collections.sort(rowDuplicates);
            Collections.sort(columnDuplicates);
            Collections.sort(boxDuplicates);
            for (Duplicate duplicate : rowDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            str.append("------------------------------------------");
            for (Duplicate duplicate : columnDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            str.append("------------------------------------------");
            for (Duplicate duplicate : boxDuplicates) {
                str.append(duplicate.toString());
                str.append("\n");
            }
            return str.toString();
        }
    }

    protected abstract void propagate();

    protected abstract void verify();

    protected abstract void verifyRows();

    protected abstract void verifyColumns();

    protected abstract void verifyBoxes();

    protected abstract void verifyRow(ArrayList<Integer> row);

    protected abstract void verifyColumn(ArrayList<Integer> column);

    protected abstract void verifyBox(ArrayList<Integer> box);
}
