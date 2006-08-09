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
 *  Filename: prefMeterConfPane.java
 *  Purpose:  Preferences Pane to configure the meter.
 *
 *  Author:   schultd
 */

package ggc.gui.panels.prefs;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.JSpinner.DateEditor;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import ggc.gui.MainFrame;


public class PrefPrintingPane extends AbstractPrefOptionsPanel
{
    //private JComboBox comboMeterType;
    //private JComboBox comboPortId;
    private JLabel lab_db = null;
    private JLabel lab_file = null;


// insulins
    private JTextField fieldIns1Name, fieldIns2Name, fieldIns1Abbr, fieldIns2Abbr;

    // bg (mg/dl)
    private JTextField fieldHighBG1, fieldLowBG1, fieldTargetHighBG1, fieldTargetLowBG1;

    // bg (mmol/l)
    private JTextField fieldHighBG2, fieldLowBG2, fieldTargetHighBG2, fieldTargetLowBG2;


    private JTextField fieldEmpty, fieldPDFViewer;
    private JButton buttonBrowse;

    
    public PrefPrintingPane()
    {
        init();
    }

    public void getDbData()
    {
        /*
    	if (MainFrame.dbH.isConnected())
    	{
    	    lab_db.setText("PROBLEM");
    	}
    	else
    	{
    	    lab_db.setText(m_ic.getMessage("DB_IS_NOT_CONNECTED"));
    	}
        */

    }


    public void init()
    {


        //JPanel ful = new JPanel(new GridLayout(4,0));


        JPanel b = new JPanel(new GridLayout(0, 3));
        b.setBorder(new TitledBorder(m_ic.getMessage("BASIC_SETTINGS")));
        b.add(new JLabel(m_ic.getMessage("EMPTY_VALUE") + ":"));
        b.add(fieldEmpty = new JTextField(m_da.getSettings().getIns1Name(), 20));
        
        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(m_ic.getMessage("PDF_VIEWER_SETTINGS")));
        a.add(new JLabel(m_ic.getMessage("PDF_VIEWER")+":"));
        a.add(fieldPDFViewer = new JTextField(m_da.getSettings().getIns1Name(), 27));
        a.add(new JLabel(""));
        a.add(buttonBrowse = new JButton(m_ic.getMessage("BROWSE")));


        //fieldIns1Name.getDocument().addDocumentListener(this);
        //fieldIns2Name.getDocument().addDocumentListener(this);
        //fieldIns1Abbr.getDocument().addDocumentListener(this);
        //fieldIns2Abbr.getDocument().addDocumentListener(this);

        JLabel lab_from = new JLabel(m_ic.getMessage("FROM"));
        
        // bg 1 
        JPanel c = new JPanel(new GridLayout(0, 3));
        c.setBorder(new TitledBorder(m_ic.getMessage("SIMPLE_MONTHLY_REPORT")));

        c.add(new JLabel("BR"));
        //c.add(new JLabel("BR"));
        
        
/*        
        c.add(new JLabel(m_ic.getMessage("BREAKFAST")+":"));
        createLittlePanel(c, new JLabel(m_ic.getMessage("FROM")), new JLabel("0:00"));

        //c.add(new JLabel(m_ic.getMessage("FROM")));
        //c.add(new JLabel("0:00"));
        //c.add(new JLabel(m_ic.getMessage("TILL")));

        JSpinner spin = new JSpinner();

        

        

        //SpinnerDateModel sdmm = JSpinner.DateEditor(
        
        SpinnerDateModel sdm = new SpinnerDateModel();
        spin.setModel(sdm);
        spin.setEditor(new JSpinner.DateEditor(spin, "HH:mm"));
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        //spin.setFor

        createLittlePanel(c, new JLabel(m_ic.getMessage("TILL")), spin);
        //createLittlePanel(c, spin);

        //c.add(spin);
        c.add(new JLabel(m_ic.getMessage("LUNCH")+":"));
        //c.add(new JLabel(m_ic.getMessage("FROM")));
/*
        JPanel pp = new JPanel();
        pp.setLayout(new GridLayout(0,2));
        pp.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low()));
        pp.add(new JLabel());
*/
        /*
        createLittlePanel(c, new JLabel(m_ic.getMessage("FROM")), fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low()));
        //c.add(pp);
        //c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));
        c.add(new JLabel(m_ic.getMessage("TILL")));
//        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));

        c.add(new JLabel(m_ic.getMessage("DINNER")+":"));
        c.add(new JLabel(m_ic.getMessage("FROM")));
//        c.add(fieldTargetHighBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetHigh(), 10));
        c.add(new JLabel(m_ic.getMessage("TILL")));
//        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));

        c.add(new JLabel(m_ic.getMessage("NIGHT")+":"));
        c.add(new JLabel(m_ic.getMessage("FROM")));
//        c.add(fieldTargetLowBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetLow(), 10));
        c.add(new JLabel(m_ic.getMessage("TILL")));
//        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));
*/

        Box i = Box.createVerticalBox();
        i.add(b);
        i.add(a);
        //i.add(d);
        i.add(c);
        //i.add(b);

        add(i, BorderLayout.NORTH);
        
        
        //setLayout(null);
//        setBounds(0,0,300,300);
        //setLayout(


        // empty
        // pdf application 
        // daily times
  /*      
        JPanel a = new JPanel();
        a.setBounds(0,0,440,430);
        a.setLayout(null);
        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("PRINTING")));


        JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
        a.setBounds(40,40,100,40);
        a.add(label);

        
        JPanel p1 = new JPanel();
        p1.setLayout(null);
        p1.setBounds(10, 20, 420, 160);
        p1.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("SIMPLE_MONTHLY_REPORT")));

        JLabel l1 = new JLabel(m_ic.getMessage("NUT_DATA_DESC"));
        l1.setBounds(20, 10, 390, 150);
        p1.add(l1);


        a.add(p1);


        JPanel p2 = new JPanel();
        p2.setBounds(10,180, 420, 100);
        p2.setLayout(null); 
        p2.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("NUTRITION_DATA_INFO")));


        JLabel lab = new JLabel();
        lab.setText(m_ic.getMessage("NUTRITION_DATA_DB"));
        lab.setBounds(40, 30, 150, 25);
        lab.setHorizontalAlignment(JLabel.LEFT);
        p2.add(lab);

        lab_db = new JLabel("x");
        lab_db.setBounds(220, 30, 150, 25);
        p2.add(lab_db);



	lab = new JLabel();
	lab.setText(m_ic.getMessage("NUTRITION_DATA_FILE"));
	lab.setBounds(40, 55, 150, 25);
	lab.setHorizontalAlignment(JLabel.LEFT);
	p2.add(lab);

	lab_file = new JLabel("x");
	lab_file.setBounds(220, 55, 150, 25);
	p2.add(lab_file);



	a.add(p2);
/*
	JPanel p3 = new JPanel();
	p3.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("NUTRITION_ACTION")));
	p3.setLayout(new GridLayout(3,2));

	JButton b889 = new JButton("Deer");
	p3.add(b889);

	JLabel ll = new JLabel("djhfjs dfhjdshf shfkjhsd fsdjk");
	p3.add(ll);

	JButton b33889 = new JButton("Deeds ffr");
	p3.add(b33889);

	JLabel ll2 = new JLabel("djhfjs dfhjdshf shfkjhsd fsdjk");
	p3.add(ll2);


	a.add(p3);
*/


//        add(a, BorderLayout.NORTH);
    }


    public void createLittlePanel(JPanel parent, JComponent comp)
    {
        JPanel pp = new JPanel();
        pp.setLayout(new GridLayout(0,2));
        pp.add(comp);
        pp.add(new JLabel());
        
        parent.add(pp);
    }


    public void createLittlePanel(JPanel parent, JComponent comp1, JComponent comp2)
    {
        JPanel pp = new JPanel();
        pp.setLayout(new GridLayout(0,3));
        pp.add(comp1);
        pp.add(comp2);
        //pp.add(new JLabel());

        parent.add(pp);
    }

    
    public void saveProps()
    {
    }
}
