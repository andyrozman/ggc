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
 *  Filename: CourseGraphFrame.java
 *  Purpose:  Frame for the CourseGraphView and some controls.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;


import ggc.data.GlucoValues;
import ggc.gui.calendar.DateRangeSelectionPanel;
import ggc.gui.view.CourseGraphView;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.atech.help.HelpCapable;


public class CourseGraphDialog extends JDialog implements ActionListener, HelpCapable //JFrame
{

    private I18nControl m_ic = I18nControl.getInstance();    
    private DataAccess m_da = null;

    private CourseGraphView cGV;

    private JCheckBox chkBG, chkAvgBGDay, chkSumBU, chkMeals, chkSumIns1, chkSumIns2,
	    	      chkSumIns, chkInsPerBU;
    private DateRangeSelectionPanel dRS;
    private JButton help_button = null;


    public CourseGraphDialog(DataAccess da)
    {
        super(da.getMainParent(), "Course Graph", true);
        setTitle(m_ic.getMessage("COURSE_GRAPH"));

        this.m_da = da;

        setSize(700, 520);
        m_da.centerJDialog(this, da.getMainParent());


        cGV = new CourseGraphView(this);
        getContentPane().add(cGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        m_da.enableHelp(this);
        
        setVisible(true);
    }

    private JPanel initControlPanel()
    {
        JPanel cPanel = new JPanel(new BorderLayout());

        dRS = new DateRangeSelectionPanel();

        JPanel selectionPanel = new JPanel(new GridLayout(2, 4));
        selectionPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("TO_BE_DRAWN")+":"));
        selectionPanel.add(chkBG = new JCheckBox("  " + m_ic.getMessage("BG"), true));
        selectionPanel.add(chkSumBU = new JCheckBox("  " + m_ic.getMessage("SUM_BU"), false));
        selectionPanel.add(chkSumIns1 = new JCheckBox("  " + m_ic.getMessage("SUM") + " " + m_da.getSettings().getIns1Abbr(), false));
        selectionPanel.add(chkSumIns = new JCheckBox("  " + m_ic.getMessage("SUM_INSULIN"), false));
        selectionPanel.add(chkAvgBGDay = new JCheckBox("  " + m_ic.getMessage("AVG_BG_PER_DAY"), false));
        selectionPanel.add(chkMeals = new JCheckBox("  " + m_ic.getMessage("MEALS"), false));
        selectionPanel.add(chkSumIns2 = new JCheckBox("  " + m_ic.getMessage("SUM")+" " + m_da.getSettings().getIns2Abbr(), false));
        selectionPanel.add(chkInsPerBU = new JCheckBox("  " + m_ic.getMessage("INS_SLASH_BU"), false));

        Dimension dim = new Dimension(120, 25);
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        
        help_button = m_da.createHelpButtonBySize(120, 25, this);
        buttonPanel.add(help_button);
        
        //Dimension dim = new Dimension(80, 20);
        JButton drawButton = new JButton("    " + m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setIcon(m_da.getImageIcon_22x22("paint.png", this));
        drawButton.setActionCommand("draw");
        drawButton.addActionListener(this);

	JButton closeButton = new JButton("    " + m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.setActionCommand("close");
        closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        closeButton.addActionListener(this);
        buttonPanel.add(drawButton);
        buttonPanel.add(closeButton);

        cPanel.add(dRS, BorderLayout.WEST);
        cPanel.add(selectionPanel, BorderLayout.CENTER);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        cGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }


    public boolean getDrawBG()
    {
        return chkBG.isSelected();
    }

    public boolean getDrawAvgBGDay()
    {
        return chkAvgBGDay.isSelected();
    }


    public boolean getDrawSumBU()
    {
        return chkSumBU.isSelected();
    }


    public boolean getDrawMeals()
    {
        return chkMeals.isSelected();
    }

    public boolean getDrawSumIns1()
    {
        return chkSumIns1.isSelected();
    }


    public boolean getDrawSumIns2()
    {
        return chkSumIns2.isSelected();
    }


    public boolean getDrawSumIns()
    {
        return chkSumIns.isSelected();
    }


    public boolean getDrawInsPerBU()
    {
        return chkInsPerBU.isSelected();
    }


    private void closeDialog()
    {
	cGV = null;
	this.dispose();
    }


    
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("draw")) 
        {
            setNewDateRange();
            //cGV.repaint(this.getBounds());
	    cGV.repaint();
        }
        else if (action.equals("close")) 
        {
	    closeDialog();
        }
        else
            System.out.println("CourseGraphFrame: Unknown command: " + action);
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
	return this.help_button;
    }

    /* 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return "pages.GGC_BG_Graph_Course";
    }
    
    
    
}
