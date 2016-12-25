package ggc.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * @author andy
 *
 */
public class StockTypeDialog extends JDialog
{

    private JRadioButton jRadioButton0;
    private JTextField jTextField0;
    private JPanel jPanel0;

   

   

   

    private void initComponents() {
    	setFont(new Font("Dialog", Font.PLAIN, 12));
    	setBackground(Color.white);
    	setForeground(Color.black);
    	
            jPanel0 = new JPanel();
            jPanel0.setLayout(null);
            
            jRadioButton0 = new JRadioButton();
            jRadioButton0.setText("jRadioButton0");
            jRadioButton0.setBounds(59, 48, 104, 24);            
            
            
            jTextField0 = new JTextField();
            jTextField0.setText("jTextField0");
            jTextField0.setBounds(132, 115, 63, 20);            
            
            jPanel0.add(jRadioButton0);
            jPanel0.add(jTextField0);
    	
    	
    	add(jPanel0, BorderLayout.CENTER);
    	setSize(320, 240);
    	
       
    	
    	
    }

    public StockTypeDialog() {
    	initComponents();
    }

    
    
    
    
}
