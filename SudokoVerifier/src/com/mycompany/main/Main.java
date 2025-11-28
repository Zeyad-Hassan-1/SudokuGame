/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.core.SudokoVerifierFactory;
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
            throw new Exception("\nSudoko Verifier 9x9:\n\t"
                    +          "    java -jar SudokoVerifier.jar <.csv filePath> <mode>\n\n\t"
                    +          "    <.csv filePath>\tFull-Path/Relative-Path for csv file containting\n\t"
                    +          "                   \ta 9x9 sudoko solution to be validated\n\n\t"
                    +          "    <mode>         \t* 0: means one thread (main thread) in other wo-\n\t"
                    +          "                   \t     rds a regular sequential program.\n\n\t" 
                    +          "                   \t* 3: means four threads, three added to the main\n\t"
                    +          "                   \t     one per each type: one for rows,one for\n\t"
                    +          "                   \t     columns, and one for boxes.\n\n\t"
                    +          "                   \t* 27: means twenty eight threads, twenty seven\n\t"
                    +          "                   \t      added to the main, one per each\n\t"
                    +          "                   \t      (row or column or box), we got 9 rows,\n\t"
                    +          "                   \t      9 cols, and 9 boxes which sum up \n\t"
                    +          "                   \t      to 27 threads.");
        }
        if(args.length!=2)
        {
            throw new IllegalArgumentException("\nTry\n\t\tjava -jar SudokoVerifier.jar --help\nfor more help");
        }
        
        File file = new File(args[0]);
        if(!file.exists())
        {
            throw new FileNotFoundException("\""+args[0]+"\""+"doesn't exist as a CSV input file");
        }
        
        for(int mode : SudokoVerifierFactory.modes)
        {
            if(Integer.parseInt(args[1])==mode)
            {
                return;
            }
        }
        
        throw new IllegalArgumentException("Error: Unkown mode --> "+args[1]+"?!");
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            validateArgs(args);
            SudokoVerifier verifier = SudokoVerifierFactor.createVerifier(args[0],Integer.parseInt(args[1]));
            verifier.printResult();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return;
    }
    
}
