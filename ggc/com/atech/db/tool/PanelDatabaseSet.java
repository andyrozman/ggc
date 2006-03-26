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

//import ggc.util.DataAccess;
import ggc.util.I18nControl;

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


public class PanelDatabaseSet extends JPanel implements ActionListener
{

    I18nControl ic = I18nControl.getInstance();
    DbToolAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    JLabel label_class, label_dialect;

    JComboBox cb_databases = null;

    //NutritionTreeDialog m_dialog = null;
    DatabaseSettings m_database_settings = null;
    


    public PanelDatabaseSet(DbTool dia)
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

	this.setSize(500, 560);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.BOLD, 18);


        label = new JLabel(ic.getMessage("DB_CONFIGURATION"));
        label.setBounds(0, 25, 500, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage("DB_CONFIG_DESC"));
        label.setBounds(40, 100, 410, 130);
        label.setFont(font_normal); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("DATABASE_TYPE"));
        label.setBounds(50, 260, 200, 30);
        label.setFont(font_normal); 
        this.add(label, null);

	cb_databases = new JComboBox(m_da.getAvailableDatabases());
	cb_databases.setBounds(200, 260, 190, 25);
	cb_databases.setFont(font_normal);
	this.add(cb_databases, null);


	label = new JLabel(ic.getMessage("DATABASE_CLASSNAME"));
	label.setBounds(50, 290, 200, 30);
	label.setFont(font_normal); 
	this.add(label, null);

	label_class = new JLabel("");
	label_class.setBounds(200, 290, 200, 30);
	label_class.setFont(font_normal); 
	this.add(label_class, null);


	label = new JLabel(ic.getMessage("HIBERNATE_DIALECT"));
	label.setBounds(50, 320, 200, 30);
	label.setFont(font_normal); 
	this.add(label, null);

	label_dialect = new JLabel("");
	label_dialect.setBounds(200, 320, 200, 30);
	label_dialect.setFont(font_normal); 
	this.add(label_dialect, null);


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
        //this.add(button);

        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
        //this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        label.setFont(font_normal); 
        //this.add(label, null);

//	System.out.println("Pabnel paint");
        return;
    }


    public void setData(DatabaseSettings intr)
    {
	m_database_settings = intr;
	reDraw();
    }

    public void reDraw()
    {
	System.out.println("Redraw Not working fully");

	String dbName = m_database_settings.db_name;
	cb_databases.setEnabled(false);

	Object av[] = m_da.getAvailableDatabases();

	for (int i=0; i<av.length; i++)
	{
	    DatabaseDefObject ddo = (DatabaseDefObject)av[i];
	    if (ddo.name.equals(dbName))
	    {
		cb_databases.setSelectedIndex(i);
	    }
	}

	//System.out.println("m_da: " + m_da);
	//System.out.println("tableOfDb: " + m_da.m_tableOfDatabases);
	//System.out.println("dbName: " + dbName);

	DatabaseDefObject ddo = (DatabaseDefObject)m_da.m_tableOfDatabases.get(dbName);

	label_class.setText(ddo.driver);
	label_dialect.setText(ddo.short_dialect);
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
    
    

