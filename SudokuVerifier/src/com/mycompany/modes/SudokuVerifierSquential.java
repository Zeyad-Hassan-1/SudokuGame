/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.modes;

import com.mycompany.core.SudokuData;

import com.mycompany.core.SudokuVerifier;

/**
 *
 * @author Hazem
 */
public class SudokuVerifierSquential extends SudokuVerifier {

    public SudokuVerifierSquential(SudokuData data) {
        super(data);
        verify(); //ignore warning
    }

    @Override
    protected void verify() {
        verifyRows();
        verifyColumns();
        verifyBoxes();
    }
}
