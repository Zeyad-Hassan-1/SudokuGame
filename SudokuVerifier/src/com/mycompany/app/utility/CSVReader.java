package com.mycompany.app.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.app.models.SudokuData;

public class CSVReader {

    /**
     * Reads a Sudoku board from a CSV file.
     * 
     * @param filePath Path to the CSV file
     * @param allowZeros If true, allows values 0-9 (0 represents empty cells for incomplete games).
     *                   If false, only allows values 1-9 (strict mode for solved boards).
     * @return SudokuData containing the board data
     * @throws IOException if file cannot be read or contains invalid data
     * @throws NumberFormatException if values cannot be parsed as integers
     */
    public static SudokuData readCSV(String filePath, boolean allowZeros) throws IOException, NumberFormatException {
        SudokuData data = new SudokuData();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int rowIndex = 0;

            while ((line = br.readLine()) != null) {

                //@Validation ~ hazem
                if (rowIndex >= 9) {
                    throw new IOException("Input file contains more than 9 rows.");
                }
                //EOV

                String[] values = line.split(",");

                //@Validation ~ hazem
                if (values.length != 9) {
                    throw new IOException("Row " + (rowIndex + 1) + " must contain exactly 9 values, found " + values.length + ".");
                }
                //EOV

                for (int colIndex = 0; colIndex < 9 && colIndex < values.length; colIndex++) {
                    int value = Integer.parseInt(values[colIndex].trim());

                    //@Validation ~ hazem
                    if (allowZeros) {
                        // Allow 0-9 for incomplete games (0 = empty cell)
                        if ((value > 9) || (value < 0)) {
                            throw new IOException("Invalid value at [" + String.valueOf(rowIndex + 1)
                                    + "][" + String.valueOf(colIndex + 1)
                                    + "] = {"
                                    + String.valueOf(value)
                                    + "}\n");
                        }
                    } else {
                        // Strict mode: only 1-9 allowed (for solved boards)
                        if ((value > 9) || (value < 1)) {
                            throw new IOException("Invalid value at [" + String.valueOf(rowIndex + 1)
                                    + "][" + String.valueOf(colIndex + 1)
                                    + "] = {"
                                    + String.valueOf(value)
                                    + "}\n");
                        }
                    }
                    //EOV

                    // Add to rows
                    data.getRows()[rowIndex][colIndex] = value;

                    // Add to columns
                    data.getColumns()[colIndex][rowIndex] = value;

                    // Add to boxes
                    int boxIndex = (rowIndex / 3) * 3 + (colIndex / 3);
                    int positionInBox = (rowIndex % 3) * 3 + (colIndex % 3);
                    data.getBoxes()[boxIndex][positionInBox] = value;
                }

                rowIndex++;
            }

            //@Validation ~ hazem
            if (rowIndex < 9) {
                throw new IOException("Input file contains less than 9 rows, found " + rowIndex + ".");
            }
            //EOV
        }

        return data;
    }

    /**
     * Reads a Sudoku board from a CSV file with strict validation (no zeros allowed).
     * This is the default mode for reading solved Sudoku boards.
     * 
     * @param filePath Path to the CSV file
     * @return SudokuData containing the board data
     * @throws IOException if file cannot be read or contains invalid data (including zeros)
     * @throws NumberFormatException if values cannot be parsed as integers
     */
    public static SudokuData readCSV(String filePath) throws IOException, NumberFormatException {
        return readCSV(filePath, false);
    }
}
