/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.frontend.utils;
import com.mycompany.main.GamePanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
/**
 *
 * @author Hazem
 */
public class SudokuCell extends JTextField {
    public final int row;
    public final int col;
    public final GamePanel parent;
    public SudokuCell(int row,int col,GamePanel parent)
    {
        super();
        this.parent = parent;
        this.row=row;
        this.col=col;
        int top = (row % 3 == 0) ? 3 : 1;
        int left = (col % 3 == 0) ? 3 : 1;
        int bottom = (row == 8) ? 3 : ((row + 1) % 3 == 0 ? 3 : 1);
        int right = (col == 8) ? 3 : ((col + 1) % 3 == 0 ? 3 : 1);
        setBorder(BorderFactory.createMatteBorder( top, left, bottom, right, Color.BLACK ));
        setHorizontalAlignment(JTextField.CENTER);
        setFont(new Font("Arial", Font.BOLD, 36));
        this.addActionListener(evt -> actionPerformed());
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                lostFocus(); 
            }
        });
    }
    
    private void actionPerformed() {                                            
        valueChanged();
    }
    
    private void lostFocus() {                                      
        valueChanged();
    }
    
    private void valueChanged()
    {
        String field = this.getText();
        int val=0;
        if(field.length()>0)
        {
            try
            {
                val = Integer.parseInt(field);
                if(val<1||val>9)
                {
                    val=0;
                    this.setText("");
                } 
            }catch(Exception e)
            {
                val =0;
                this.setText("");
            }
        }
        parent.notifyCellChange(row, col, val);
    }
    
}
