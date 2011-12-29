package ggc.gui.dialogs.stock;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;

/**
 * 
 * @author andy
 *
 */
public class StockSubTypeDialog extends JDialog
{

    private JTextField jTextField0;
    private JPanel panel;
    private JLabel lblId;
    private JLabel lblIdValue;
    private JLabel lblName;
    private JLabel lblDescription;
    private JLabel lblTitle;
    private JTextField txtNamelocdata;
    private JButton btnOk;
    private JButton btnCancel;
    private JButton btnHelp;
    private JLabel lblStockType;
    private JLabel lblStockTypeId;
    private JLabel lblContentamount;
    private JFormattedTextField ftf_content;
    private JButton btnSelstocktype;
    private JLabel label;
    private JLabel label_1;
    private JTextField textField;
    private JLabel label_2;
    private JTextField textField_1;
   

   

   


    public StockSubTypeDialog() 
    {
    	initGUI();
    }

    
    private void initGUI() 
    {
    	setFont(new Font("Dialog", Font.PLAIN, 12));
    	setBackground(Color.white);
    	setForeground(Color.black);
    	
    	getContentPane().setLayout(null);
    	
    	
    	panel = new JPanel();
    	panel.setLayout(null);
    	panel.setBounds(0, 0, 403, 370);
            
        
        this.getContentPane().add(panel, null);
            
            
            jTextField0 = new JTextField();
            this.jTextField0.setFont(new Font("SansSerif", Font.PLAIN, 12));
            jTextField0.setText("jTextField0");
            jTextField0.setBounds(160, 143, 205, 20);            
            
            panel.add(jTextField0);
    	
    	
    	setSize(411, 404);
    	
       
    	
    	
    	
    	this.lblId = new JLabel("ID");
    	this.lblId.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.lblId.setBounds(38, 76, 78, 14);
    	this.panel.add(this.lblId);
    	
    	this.lblIdValue = new JLabel("ID Value");
    	this.lblIdValue.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.lblIdValue.setBounds(160, 76, 46, 14);
    	this.panel.add(this.lblIdValue);
    	
    	this.lblName = new JLabel("NAME");
    	this.lblName.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.lblName.setBounds(38, 146, 101, 14);
    	this.panel.add(this.lblName);
    	
    	this.lblDescription = new JLabel("DESCRIPTION");
    	this.lblDescription.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.lblDescription.setBounds(38, 181, 101, 14);
    	this.panel.add(this.lblDescription);
    	
    	this.lblTitle = new JLabel("Stock Type");
    	this.lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    	this.lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
    	this.lblTitle.setBounds(10, 22, 383, 27);
    	this.panel.add(this.lblTitle);
    	
    	this.txtNamelocdata = new JTextField();
    	this.txtNamelocdata.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.txtNamelocdata.setText("nameLocData");
    	this.txtNamelocdata.setBounds(161, 178, 204, 20);
    	this.panel.add(this.txtNamelocdata);
    	
    	this.btnOk = new JButton("OK");
    	this.btnOk.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.btnOk.setBounds(62, 323, 89, 23);
    	this.panel.add(this.btnOk);
    	
    	this.btnCancel = new JButton("Cancel");
    	this.btnCancel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.btnCancel.setBounds(177, 323, 89, 23);
    	this.panel.add(this.btnCancel);
    	
    	this.btnHelp = new JButton("Help");
    	this.btnHelp.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.btnHelp.setBounds(276, 323, 89, 23);
    	this.panel.add(this.btnHelp);
    	
    	this.lblStockType = new JLabel("Stock Type");
    	this.lblStockType.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.lblStockType.setBounds(38, 111, 101, 14);
    	this.panel.add(this.lblStockType);
    	
    	this.lblStockTypeId = new JLabel("Stock Type ID");
    	this.lblStockTypeId.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.lblStockTypeId.setBounds(160, 111, 158, 14);
    	this.panel.add(this.lblStockTypeId);
    	
    	this.lblContentamount = new JLabel("Content / Pckg");
    	this.lblContentamount.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.lblContentamount.setBounds(38, 216, 101, 14);
    	this.panel.add(this.lblContentamount);
    	
    	this.ftf_content = new JFormattedTextField();
    	this.ftf_content.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	this.ftf_content.setBounds(160, 213, 89, 20);
    	this.panel.add(this.ftf_content);
    	
    	this.btnSelstocktype = new JButton("?");
    	this.btnSelstocktype.setBounds(328, 107, 37, 23);
    	this.panel.add(this.btnSelstocktype);
    	
    	this.label = new JLabel("Usage per day:");
    	this.label.setFont(new Font("SansSerif", Font.BOLD, 12));
    	this.label.setBounds(38, 257, 91, 14);
    	this.panel.add(this.label);
    	
    	this.label_1 = new JLabel("Min:");
    	this.label_1.setBounds(160, 260, 35, 14);
    	this.panel.add(this.label_1);
    	
    	this.textField = new JTextField();
    	this.textField.setColumns(10);
    	this.textField.setBounds(205, 255, 56, 20);
    	this.panel.add(this.textField);
    	
    	this.label_2 = new JLabel("Max:");
    	this.label_2.setBounds(160, 286, 35, 14);
    	this.panel.add(this.label_2);
    	
    	this.textField_1 = new JTextField();
    	this.textField_1.setColumns(10);
    	this.textField_1.setBounds(205, 283, 56, 20);
    	this.panel.add(this.textField_1);
//    	getContentPane().add(this.lblId);
    }
    
    /**
     * Was Action Successful
     * 
     * @return true if action was successful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return false; //m_actionDone;
    }
}
