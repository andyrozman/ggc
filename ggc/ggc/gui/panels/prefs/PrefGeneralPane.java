/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: prefGeneralPane.java
 *  Purpose:  General options. Now containg also database selection and look and feel changes.
 *
 *  Author:   schultd, andyrozman
 */

package ggc.gui.panels.prefs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;

import ggc.db.tool.DbToolApplicationGGC;
import ggc.util.I18nControl;

public class PrefGeneralPane extends AbstractPrefOptionsPanel 
{
    private JTextField fieldUserName;

    //private I18nControl m_ic = I18nControl.getInstance();

    private JComboBox langBox, cb_database, cb_lf_type, cb_lf_type_class;
    private JTextField tf_lf;
    private JLabel l_db_desc, l_lf_desc;
    private JButton b_browse;

    private Object[] databases = null;
    private Object[] lf_classes_name = null;
    private Object[] lf_classes_class = null;    

    private PrefGeneralPane parent = null;


    DbToolApplicationGGC m_dbc = m_da.getDbConfig();


    public PrefGeneralPane() 
    {
        init();
	parent = this;
    }

    private void init() 
    {
	this.setLayout(null);

	// general settings
	JPanel a = new JPanel(); //new GridLayout(2,3));
	a.setBounds(10,10,490,90);
	a.setLayout(null);
	a.setBorder(new TitledBorder(m_ic.getMessage("GENERAL_SETTINGS")));

	langBox = new JComboBox(m_da.getAvailableLanguages());
	langBox.setSelectedIndex(m_da.getSelectedLanguageIndex());
	langBox.setBounds(120, 50, 150, 25);

	JLabel label = new JLabel();
	label.setBounds(20, 20, 100, 25);
	label.setText(m_ic.getMessage("YOUR_NAME") + ":");
	a.add(label);

	fieldUserName = new JTextField(m_da.getSettings().getUserName(), 10);
	fieldUserName.setBounds(120, 20, 150, 25);
	a.add(fieldUserName);


	label = new JLabel();
	label.setBounds(20, 50, 100, 25);
	label.setText(m_ic.getMessage("YOUR_LANGUAGE") + ":");
	a.add(label);
	a.add(langBox);
	
	fieldUserName.getDocument().addDocumentListener(this);
	langBox.addItemListener(this);

	this.add(a);


	// database settings

	JPanel p2 = new JPanel(); 
	p2.setBounds(10,110,490,160);
	p2.setLayout(null);
	p2.setBorder(new TitledBorder(m_ic.getMessage("DATABASE_SETTINGS")));

	label = new JLabel(m_ic.getMessage("DATABASE_SETTINGS_DESC"));
	label.setBounds(20, 25, 440, 150);
	label.setVerticalAlignment(JLabel.TOP);
	p2.add(label); //new JLabel(m_ic.getMessage("DATABASE_SETTINGS_DESC")));

	label = new JLabel(m_ic.getMessage("SELECTED_DATABASE") + ":");
	label.setBounds(20, 120, 180, 25);
	p2.add(label);

	this.cb_database = new JComboBox(this.m_da.getDbConfig().getAllDatabasesNamesPlusAsArray());
	this.cb_database.setBounds(155, 120, 280, 25);
	this.cb_database.setSelectedIndex(this.m_da.getDbConfig().getSelectedDatabaseIndex());
	p2.add(this.cb_database);

	this.add(p2);



	JPanel p3 = new JPanel(/*new GridLayout(2, 1)*/);
	p3.setBorder(new TitledBorder(m_ic.getMessage("LAF_SETTINGS")));
	p3.setBounds(10,280,490,135);
	p3.setLayout(null);

	/*
	label = new JLabel(m_ic.getMessage("LAF_SETTINGS_DESC"));
	label.setBounds(20, 10, 400, 30);
	p3.add(label);
	*/

	addLabel(p3, m_ic.getMessage("LAF_SETTINGS_DESC"), 20, 10, 400, 30);
	
	addLabel(p3, m_ic.getMessage("SELECTED_LAF_TYPE_NAME") +":", 20, 40, 150, 25);
	addLabel(p3, m_ic.getMessage("SELECTED_LAF_TYPE_CLASS")+":", 20, 70, 150, 25);
	addLabel(p3, m_ic.getMessage("SELECTED_SKINLF_DEF")+":", 20, 100, 150, 25);


	this.cb_lf_type = new JComboBox(m_dbc.getAvailableLFs());
	this.cb_lf_type.setBounds(135, 40, 330, 23);
	this.cb_lf_type.addItemListener(this);
	p3.add(this.cb_lf_type);

	this.cb_lf_type_class = new JComboBox(m_dbc.getAvailableLFsClass());
	this.cb_lf_type_class.setBounds(135, 70, 330, 23);
	this.cb_lf_type_class.addItemListener(this);
	p3.add(this.cb_lf_type_class);

	//this.cb_lf_type_class.setSelectedIndex(idx);


	this.tf_lf = new JTextField();
	this.tf_lf.setBounds(170, 100, 150, 23);
	this.tf_lf.setText(this.m_dbc.getSelectedLFSkin());
	this.tf_lf.setEditable(false);
	p3.add(this.tf_lf);

	this.b_browse = new JButton(m_ic.getMessage("BROWSE")+"...");
	this.b_browse.setBounds(330, 100, 120, 23);
	this.b_browse.addActionListener(new ActionListener(){
		/**
		 * Invoked when an action occurs.
		 */
		public void actionPerformed(ActionEvent e)
		{
		    JFileChooser jfc = new JFileChooser();
		    jfc.setCurrentDirectory(new File("../data/skinlf_themes"));
		    jfc.setDialogTitle(m_ic.getMessage("SELECT_SKINLF_SKIN"));
		    jfc.setDialogType(JFileChooser.CUSTOM_DIALOG);
		    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		    int res = jfc.showDialog(parent, m_ic.getMessage("SELECT"));


		    if (res==JFileChooser.APPROVE_OPTION)
		    {
			tf_lf.setText(jfc.getSelectedFile().getName());
		    }

		}
		});

	p3.add(this.b_browse);

	int idx = this.m_dbc.getSelectedLFIndex();
	this.cb_lf_type.setSelectedIndex(idx);
	

	//p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_SKINLF_DEF")));
	//p_p_3.add(this.tf_lf = new JTextField());
	//p_p_3.add(this.b_browse = new JButton(m_ic.getMessage("BROWSE")+"..."));


	//cb_lf_type, cb_lf_type_class


/*
	//JPanel p_p_3 = new JPanel(new GridLayout(3, 3));
	//p_p_3.setBounds(20, 50, 300, 75);
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_LAF_TYPE_NAME")));
	p_p_3.add(this.cb_lf_type = new JComboBox());
	p_p_3.add(new JLabel());
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_LAF_TYPE_CLASS")));
	p_p_3.add(this.tf_lf = new JTextField());
	p_p_3.add(new JLabel());
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_SKINLF_DEF")));
	p_p_3.add(this.tf_lf = new JTextField());
	p_p_3.add(this.b_browse = new JButton(m_ic.getMessage("BROWSE")+"..."));

	p3.add(p_p_3);
*/
	//this.cb_lf_type.addItemListener(this);


	this.add(p3);



	//this.setLayout(new BorderLayout());


/*
        JPanel a = new JPanel(new GridLayout(2,32));
	a.setBorder(new TitledBorder(m_ic.getMessage("GENERAL_SETTINGS")));
        langBox = new JComboBox(m_da.getAvailableLanguages());
        langBox.setSelectedIndex(m_da.getSelectedLanguageIndex());

        a.add(new JLabel(m_ic.getMessage("YOUR_NAME") + ":"));
        a.add(fieldUserName = new JTextField(m_da.getSettings().getUserName(), 10));
	a.add(new JLabel());

        a.add(new JLabel(m_ic.getMessage("YOUR_LANGUAGE") + ":"));
        a.add(langBox);
	a.add(new JLabel());

        fieldUserName.getDocument().addDocumentListener(this);
        langBox.addItemListener(this);

	// database settings
	JPanel par_p_2 = new JPanel(new GridLayout(1,1));

	JPanel p_2 = new JPanel(); //new GridLayout(2, 1));
	p_2.setLayout(null);
	p_2.setSize(300, 130);
	//p_2.setBounds(0, 0, 300, 200);
	//p_2.setBackground(Color.red);
	p_2.setBorder(new TitledBorder(m_ic.getMessage("DATABASE_SETTINGS")));

	JLabel label = new JLabel(m_ic.getMessage("DATABASE_SETTINGS_DESC"));
	label.setBounds(10, 15, 450, 90);
	p_2.add(label); //new JLabel(m_ic.getMessage("DATABASE_SETTINGS_DESC")));

	label = new JLabel(m_ic.getMessage("SELECTED_DATABASE") + ":");
	label.setBounds(10, 120, 120, 25);
	p_2.add(label);

	this.cb_database = new JComboBox();
	this.cb_database.setBounds(140, 120, 280, 25);
	p_2.add(this.cb_database);
	//JPanel p_p_2 = new JPanel(new GridLayout(1, 2));
	//p_p_2.add(new JLabel(m_ic.getMessage("SELECTED_DATABASE")));
	//p_p_2.add(this.cb_database = new JComboBox());
	//p_2.add(p_p_2);

	par_p_2.add(p_2);


	JPanel p_3 = new JPanel(new GridLayout(2, 1));
	p_3.setBorder(new TitledBorder(m_ic.getMessage("LAF_SETTINGS")));
	p_3.add(new JLabel(m_ic.getMessage("LAF_SETTINGS_DESC")));

	
	JPanel p_p_3 = new JPanel(new GridLayout(3, 3));
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_LAF_TYPE_NAME")));
	p_p_3.add(this.cb_lf_type = new JComboBox());
	p_p_3.add(new JLabel());
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_LAF_TYPE_CLASS")));
	p_p_3.add(this.tf_lf = new JTextField());
	p_p_3.add(new JLabel());
	p_p_3.add(new JLabel(m_ic.getMessage("SELECTED_SKINLF_DEF")));
	p_p_3.add(this.tf_lf = new JTextField());
	p_p_3.add(this.b_browse = new JButton(m_ic.getMessage("BROWSE")+"..."));

	p_3.add(p_p_3);

	this.cb_lf_type.addItemListener(this);

	
/*
        Box c = Box.createVerticalBox();
	//c.add
        c.add(a);
	c.add(par_p_2);
	c.add(p_3);
*/
/*
        add(a, BorderLayout.NORTH);
	add(p_2, BorderLayout.CENTER);
	add(p_3, BorderLayout.SOUTH);
*/

    }


    public void addLabel(JPanel panel, String text, int x, int y, int width, int height)
    {
	JLabel label = new JLabel(text);
	label.setBounds(x, y, width, height);
	panel.add(label);
    }


    
    boolean in_change = false;

    public void itemStateChanged(ItemEvent e)
    {

	if (in_change)
	    return;
	else
	    in_change = true;

	//System.out.println(e);

	JComboBox cb = (JComboBox)e.getSource();

	int index = cb.getSelectedIndex();

	if (this.cb_lf_type.equals(cb))
	    this.cb_lf_type_class.setSelectedIndex(index);
	else
	    this.cb_lf_type.setSelectedIndex(index);


	boolean skin = m_dbc.isSkinLFSelected(index);

	//this.tf_lf.setEnabled(skin);
	this.b_browse.setEnabled(skin); 

	in_change = false;
    }
    


    @Override
    public void saveProps() 
    {
	settings.setUserName(fieldUserName.getText());
	settings.setLanguage(langBox.getSelectedItem().toString());

	this.m_dbc.setSelectedDatabaseIndex(this.cb_database.getSelectedIndex());
	this.m_dbc.setSelectedLF(this.cb_lf_type.getSelectedIndex(), this.tf_lf.getText());
    }
}
