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
 *  Filename: AddRowFrame.java
 *  Purpose:  Add a new row with Values to ReadMeterFrame or DailyValuesFrame.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.GregorianCalendar;

import javax.swing.*;

import ggc.data.MonthlyValues;
import ggc.data.print.PrintSimpleMonthlyReport;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


// fix this

public class PrintingDialog extends JDialog implements ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();

    private boolean m_actionDone = false;

    private JTextField tfName;
    private JComboBox cb_template = null;
    private String[] schemes_names = null;



    public PrintingDialog(JFrame frame, int type) 
    {
        super(frame, "", true);

        Rectangle rec = frame.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-175, y-150, 350, 300);
        this.setLayout(null);

        init();
        this.setVisible(true);
    }



    private void init() 
    {

    	JPanel panel = new JPanel();
    	panel.setBounds(0, 0, 350, 250);
    	panel.setLayout(null);
    
    	this.getContentPane().add(panel);
    
    	JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
    	label.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
    	label.setHorizontalAlignment(JLabel.CENTER);
    	label.setBounds(0, 20, 350, 35);
    	panel.add(label);
    	
    
    	label = new JLabel(m_ic.getMessage("TYPE_OF_REPORT") + ":" );
    	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	label.setBounds(40, 90, 280, 25);
    	panel.add(label);
        
    	cb_template = new JComboBox();
    	cb_template.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	cb_template.setBounds(40, 130, 230, 25);
    	panel.add(cb_template);
        
    	int year = (new GregorianCalendar()).get(GregorianCalendar.YEAR);
    /*
        JSlider sl = new JSlider();
        BoundedRangeModel model = new BoundedRangeModel();
        model.setMinimum(1970);
        model.setMaximum(year);
        model.setValue(year);
        sl.setModel(model);
        sl.setBounds(120, 210, 30, 25);
    */
    
    	label = new JLabel(m_ic.getMessage("SELECT_YEAR_AND_MONTH") + ":");
    	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL_BOLD));
    	label.setBounds(40, 170, 80, 25);
    	panel.add(label);
    
    	tfName = new JTextField();
    	tfName.setBounds(120, 210, 160, 25);
    	tfName.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	//panel.add(sl);
    	
    
    	JButton button = new JButton(m_ic.getMessage("OK"));
    	button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	button.setActionCommand("ok");
    	button.addActionListener(this);
    	button.setBounds(90, 210, 80, 25);
    	panel.add(button);
    
    	button = new JButton(m_ic.getMessage("CANCEL"));
    	button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	button.setActionCommand("cancel");
    	button.addActionListener(this);
    	button.setBounds(180, 210, 80, 25);
    	panel.add(button);

    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
    	String action = e.getActionCommand();
    
    	if (action.equals("cancel"))
    	{
    	    m_actionDone = false;
    	    this.dispose();
    	}
    	else if (action.equals("ok"))
    	{

	    MonthlyValues mv = m_da.getDb().getMonthlyValues(2006, 4);
	    PrintSimpleMonthlyReport psm = new PrintSimpleMonthlyReport(mv);

	    displayPDF(psm.getName());
         
        
/*
	    if (this.tfName.getText().trim().equals(""))
    	    {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("TYPE_NAME_BEFORE"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
    	    }
    	    m_actionDone = true; */
    	    this.dispose();
    	}
    	else
    	    System.out.println("SchemeDialog: Unknown command: " + action);

    }

    public void displayPDF(String name)
    {
	//File fl = new File();
	//fl.separator

	File fl = new File("../data/temp/");

	System.out.println(fl.getAbsolutePath());
	System.out.println(fl.separator);

	File acr = new File("c:/Program Files/Utils/Acrobat 7/Reader/AcroRd32.exe");
	System.out.println(acr.exists());

	System.out.println(acr.getAbsoluteFile());

	
	try 
	{
//	    String pathToAcrobat = "c:\\Program Files\\Utils\\Acrobat 7.0\\Reader\\AcroRd32.exe ";
	    //Runtime.getRuntime().e
	    Runtime.getRuntime().exec(acr.getAbsoluteFile() + " " +  fl.getAbsolutePath() + fl.separator + name);
	} 
	catch(RuntimeException ex)
	{
	    System.out.println("RE running AcrobatReader: " + ex);
	}
	catch (Exception ex) 
	{
	    System.out.println("Error running AcrobatReader: " + ex);
	    
	}

    }



    public boolean actionSuccesful()
    {
        return m_actionDone;
    }

    public String[] getActionResult()
    {
    	String[] res = new String[3];
    
    	if (m_actionDone)
    	    res[0] = "1";
    	else
    	    res[0] = "0";
    
    	res[1] = this.tfName.getText();
    	res[2] = this.cb_template.getSelectedItem().toString();
    
    	return res;
    }

}
