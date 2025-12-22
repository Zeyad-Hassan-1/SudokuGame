/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.inputverifiers;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Hazem
 */
public class isNumber extends InputVerifier {
    @Override
    public boolean verify(JComponent input)
    {
        JTextField field = (JTextField) input;
        try
        {
            int val = Integer.parseInt(field.getText());
            return ((val>=1)&&(val<=9));
        }catch(Exception e)
        {
            return false;
        }
    }
    
}
