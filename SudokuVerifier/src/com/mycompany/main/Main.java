/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.app.controllers.services.SudokuVerifier;
import com.mycompany.app.utility.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Hazem
 */
public class Main {

    private static void validateArgs(String[] args) throws IllegalArgumentException, Exception, FileNotFoundException
    {
        if(args.length == 1 && args[0].equals("--help"))
        {
            throw new Exception("\nSudoku Verifier 9x9:\n\t"
                    +          "    java -jar SudokuVerifier.jar <csv_file> [allowZeros]\n\n\t"
                    +          "    <csv_file>\t\tPath to CSV file containing 9x9 Sudoku board\n\t"
                    +          "    [allowZeros]\t\tOptional: 'true' to allow zeros (for incomplete games),\n\t"
                    +          "                   \t'false' for strict mode (default: false)\n\n\t"
                    +          "Examples:\n\t"
                    +          "    java -jar SudokuVerifier.jar valid.csv\n\t"
                    +          "    java -jar SudokuVerifier.jar incomplete.csv true");
        }
        if(args.length < 1 || args.length > 2)
        {
            throw new IllegalArgumentException("\nTry\n\t\tjava -jar SudokuVerifier.jar --help\nfor more help");
        }
        
        File file = new File(args[0]);
        if(!file.exists())
        {
            throw new FileNotFoundException("\""+args[0]+"\""+" doesn't exist as a CSV input file");
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            validateArgs(args);
            
            String csvFile = args[0];
            boolean allowZeros = false;
            
            // Parse allowZeros flag if provided
            if(args.length == 2) {
                allowZeros = Boolean.parseBoolean(args[1]);
            }
            
            int[][] board = CSVReader.readCSV(csvFile, allowZeros);
            SudokuVerifier verifier = new SudokuVerifier(board);
            System.out.println(verifier.toHumanReadableString());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
