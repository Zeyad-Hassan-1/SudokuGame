/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.core;

import java.io.IOException;

import com.mycompany.modes.SudokuVerifierSquential;
import com.mycompany.modes.SudokuVerifierThreeThreaded;
import com.mycompany.modes.SudokuVerifierTwentySevenThreaded;
import com.mycompany.util.CSVReader;

/**
 *
 * @author Hazem
 */
public class SudokuVerifierFactory {

    public static final int MODE_SEQUENTIAL = 0;
    public static final int MODE_THREE_THREADED = 3;
    public static final int MODE_TWENTY_SEVEN_THREADED = 27;
    public static final String MODE_DESCRIPTION = "    <mode>         \t* 0: means one thread (main thread) in other wo-\n\t"
            + "                   \t     rds a regular sequential program.\n\n\t"
            + "                   \t* 3: means four threads, three added to the main\n\t"
            + "                   \t     one per each type: one for rows,one for\n\t"
            + "                   \t     columns, and one for boxes.\n\n\t"
            + "                   \t* 27: means twenty eight threads, twenty seven\n\t"
            + "                   \t      added to the main, one per each\n\t"
            + "                   \t      (row or column or box), we got 9 rows,\n\t"
            + "                   \t      9 cols, and 9 boxes which sum up \n\t"
            + "                   \t      to 27 threads.";

    public static SudokuVerifier createVerifier(String fileName, int mode) throws IOException {
        SudokuData data = CSVReader.readCSV(fileName);

        switch (mode) {
            case MODE_SEQUENTIAL:
                return new SudokuVerifierSquential(data);
            case MODE_THREE_THREADED:
                return new SudokuVerifierThreeThreaded(data);
            case MODE_TWENTY_SEVEN_THREADED:
                return new SudokuVerifierTwentySevenThreaded(data);
            default:
                throw new IllegalArgumentException("Error: Unkown mode --> " + mode + "?!");
        }
    }
}
