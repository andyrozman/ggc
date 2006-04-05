package com.atech.db.tool;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ggc.util.I18nControl;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


public class PanelDatabaseRoot extends JPanel implements ActionListener
{

    I18nControl ic = I18nControl.getInstance();
    DbToolAccess m_da = null;

    DbToolApplicationInterface m_application = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    //NutritionTreeDialog m_dialog = null;
    


    public PanelDatabaseRoot(DbTool dia)
    {

        super();

        //m_dialog = dia;
        m_da = DbToolAccess.getInstance();

        font_big = m_da.getFont(DbToolAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DbToolAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DbToolAccess.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

        this.setSize(420, 460);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.BOLD, 18);


        label = new JLabel("Database Root");
	    //ic.getMessage("CONFIGURATION"));
        label.setBounds(0, 35, 420, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage("ADD"));
        label.setBounds(40, 100, 100, 30);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("ADD_DESC"));
        label.setBounds(40, 130, 300, 60);
        label.setFont(font_normal); 
        this.add(label, null);

	JComboBox cb = new JComboBox(m_da.getAvailableDatabases());
	cb.setBounds(110, 190, 170, 25);
	cb.setFont(font_normal);
	this.add(cb, null);


/*
        button = new JButton(ic.getMessage("ADD_"));
        button.setBounds(110, 190, 170, 25);
        button.setFont(font_normal);
        button.addActionListener(this);
        button.setActionCommand("add_");
        this.add(button);
*/
        button = new JButton(ic.getMessage("ADD_"));
        button.setBounds(110, 220, 170, 25);
        button.setFont(font_normal);
        button.setActionCommand("add_p");
        button.addActionListener(this);
        this.add(button);

        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        label.setFont(font_normal); 
        this.add(label, null);

	//System.out.println("Pabnel paint");

        return;
    }


    public void setData(DbToolApplicationInterface intr)
    {
	m_application = intr;
	reDraw();
    }

    public void reDraw()
    {
	System.out.println("Redraw Not working");
    }



    /**
     *  Action Listener
     */
    public void actionPerformed(ActionEvent e) 
    {

        String action = e.getActionCommand();
        System.out.println("PanelDatabaseSet::Unknown command: " + action);
  
    }

}
    
    
