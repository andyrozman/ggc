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
 *  Filename: PrefPrintingPane.java
 *  Purpose:  Preferences Pane to configure the meter.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.gui.panels.prefs;

import ggc.gui.dialogs.PropertiesDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

import com.atech.help.HelpCapable;


public class PrefPrintingPane extends AbstractPrefOptionsPanel implements HelpCapable
{
    //private JComboBox comboMeterType;
    //private JComboBox comboPortId;
//x    private JLabel lab_db = null;
//x    private JLabel lab_file = null;


// insulins
//    private JTextField fieldIns1Name, fieldIns2Name, fieldIns1Abbr, fieldIns2Abbr;

    // bg (mg/dl)
//    private JTextField fieldHighBG1, fieldLowBG1, fieldTargetHighBG1, fieldTargetLowBG1;

    // bg (mmol/l)
//    private JTextField fieldHighBG2, fieldLowBG2, fieldTargetHighBG2, fieldTargetLowBG2;


    private JTextField fieldEmpty, fieldPDFViewer, fieldLunchST, fieldDinnerST, fieldNightST;
    private JButton buttonBrowse;

    
    public PrefPrintingPane(PropertiesDialog dia)
    {
	super(dia);
        init();
        //m_da.enableHelp(this);
    }



    public void init()
    {

	setLayout(new BorderLayout());

        //JPanel ful = new JPanel(new GridLayout(4,0));


        JPanel b = new JPanel(new GridLayout(0, 2));
        b.setBorder(new TitledBorder(m_ic.getMessage("BASIC_SETTINGS")));
        b.add(new JLabel(m_ic.getMessage("EMPTY_VALUE") + ":"));
        b.add(fieldEmpty = new JTextField(m_da.getSettings().getIns1Name(), 20));
        
        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(m_ic.getMessage("PDF_VIEWER_SETTINGS")));
        a.add(new JLabel(m_ic.getMessage("PDF_VIEWER")+":"));
        a.add(fieldPDFViewer = new JTextField(m_da.getSettings().getIns1Name(), 27));
        a.add(new JLabel(""));
        a.add(buttonBrowse = new JButton(m_ic.getMessage("BROWSE")));

	buttonBrowse.addActionListener(this);


        //fieldIns1Name.getDocument().addDocumentListener(this);
        //fieldIns2Name.getDocument().addDocumentListener(this);
        //fieldIns1Abbr.getDocument().addDocumentListener(this);
        //fieldIns2Abbr.getDocument().addDocumentListener(this);

        //JLabel lab_from = new JLabel(m_ic.getMessage("FROM"));
        
        // bg 1 

	JPanel c1 = new JPanel(new GridLayout(0, 1));
	c1.setBorder(new TitledBorder(m_ic.getMessage("SIMPLE_MONTHLY_REPORT")));
	


        JPanel c = new JPanel(new GridLayout(0, 3));
        //c.setBorder(new TitledBorder(m_ic.getMessage("SIMPLE_MONTHLY_REPORT")));

        c.add(new JLabel(m_ic.getMessage("LUNCH_START_TIME")+":"));
	c.add(new JLabel());
        c.add(this.fieldLunchST = new JTextField("", 6));
        
	c.add(new JLabel(m_ic.getMessage("DINNER_START_TIME")+":"));
	c.add(new JLabel());
	c.add(this.fieldDinnerST = new JTextField("", 3));

	c.add(new JLabel(m_ic.getMessage("NIGHT_START_TIME")+":"));
	c.add(new JLabel());
	c.add(this.fieldNightST = new JTextField("", 4));


	c1.add(c);
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
        i.add(c1);
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

	
	// init values

        fieldEmpty.setText(settings.getPrintEmptyValue());
	fieldPDFViewer.setText(settings.getPdfVieverPath());

	fieldLunchST.setText(m_da.getTimeString(settings.getPrintLunchStartTime()));
	fieldDinnerST.setText(m_da.getTimeString(settings.getPrintDinnerStartTime()));
	fieldNightST.setText(m_da.getTimeString(settings.getPrintNightStartTime()));

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

    private int getTimeValue(JTextField field, int default_value)
    {
	try
	{
	    String st = field.getText();
	    st = st.replaceAll(":", "");

	    return Integer.parseInt(st);
	}
	catch(Exception ex)
	{
	    return default_value;
	}
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	JFileChooser jfc = new JFileChooser();

	jfc.setCurrentDirectory(new File("."));
	jfc.setDialogTitle(m_ic.getMessage("SELECT_PDF_VIEWER"));
	jfc.setDialogType(JFileChooser.OPEN_DIALOG);
	jfc.setFileFilter(new FileFilter(){
		/**
		 * Whether the given file is accepted by this filter.
		 */
        @Override
		public boolean accept(File f)
		{
		    if (f.isDirectory())
			return true;

		    int idx = f.getName().lastIndexOf(".");

		    if (idx>-1)
		    {
			String ext = f.getName().toLowerCase().substring(idx);

			if (ext.equals(".exe"))
			    return true;
			else
			    return false;
		    }
		    else
			return false;

		}
		/**
		 * The description of this filter. For example: "JPG and GIF Images"
		 * @see FileView#getName
		 */
		@Override
        public String getDescription()
		{
		    return m_ic.getMessage("EXECUTABLE_FILES");
		}
	});

	jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

	int res = jfc.showOpenDialog(this);

	if (res==JFileChooser.APPROVE_OPTION)
	{
	    File f = jfc.getSelectedFile();
	    this.fieldPDFViewer.setText(f.getPath());
	}

    }


    
    @Override
    public void saveProps()
    {
	settings.setPrintEmptyValue(fieldEmpty.getText()); 
	settings.setPdfVieverPath(fieldPDFViewer.getText());

	settings.setPrintLunchStartTime(getTimeValue(fieldLunchST, 1100));
	settings.setPrintDinnerStartTime(getTimeValue(fieldDinnerST, 1800));
	settings.setPrintNightStartTime(getTimeValue(fieldNightST, 2100));

    }
    
    
    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
    // ****************************************************************
    
    /* 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
	return this.getRootPane();
    }

    /* 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
	return this.parent.getHelpButton();
    }

    /* 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return "pages.GGC_Prefs_Printing";
    }
    
    
    
    
    
}
