package com.mycompany.app.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.mycompany.app.models.SudokuData;

public class CSVReader {
    
    public static SudokuData readCSV(String filePath) throws IOException, NumberFormatException {
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
                    if ((value >9)||(value<1)) {
                        throw new IOException("Invalid value at [" + String.valueOf(rowIndex + 1)
                                + "][" + String.valueOf(colIndex + 1)
                                + "] = {"
                                + String.valueOf(value)
                                + "}\n");
                    }
                    //EOV

                    // Add to rows
                    data.getRows().get(rowIndex).add(value);

                    // Add to columns
                    data.getColumns().get(colIndex).add(value);

                    // Add to boxes
                    int boxIndex = (rowIndex / 3) * 3 + (colIndex / 3);
                    data.getBoxes().get(boxIndex).add(value);
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
}
