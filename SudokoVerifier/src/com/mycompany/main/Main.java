/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.main;

import com.mycompany.core.SudokoVerifier;
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
                    +          SudokoVerifierFactory.MODE_DESCRIPTION);
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
        
        int mode = Integer.parseInt(args[1]); // to throw exception in case of non-int arg
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            validateArgs(args);
            SudokoVerifier verifier = SudokoVerifierFactory.createVerifier(args[0],Integer.parseInt(args[1]));
            System.out.println(verifier.toString());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return;
    }
    
}
