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


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import com.sun.org.apache.bcel.internal.classfile.Unknown;

import ggc.datamodels.GlucoValues;
import ggc.gui.calendar.DateRangeSelectionPanel;
import ggc.gui.view.CourseGraphView;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


public class CourseGraphDialog extends JDialog implements ActionListener //JFrame
{

    private I18nControl m_ic = I18nControl.getInstance();    
    private DataAccess m_da = DataAccess.getInstance();

    private CourseGraphView cGV;

    private JCheckBox chkBG, chkAvgBGDay, chkSumBU, chkMeals, chkSumIns1, chkSumIns2,
	    	      chkSumIns, chkInsPerBU;
    private DateRangeSelectionPanel dRS;


    public CourseGraphDialog(JFrame parent)
    {
        super(parent, "Course Graph", true);
        setTitle(m_ic.getMessage("COURSE_GRAPH"));

	//System.out.println(parent.getBounds());

	Rectangle rec = parent.getBounds();
	int x = rec.x + (rec.width/2);
	int y = rec.y + (rec.height/2);


        setBounds(x-350, y-250, 700, 500);
        addWindowListener(new CloseListener());

        cGV = new CourseGraphView(this);
        getContentPane().add(cGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension dim = new Dimension(80, 20);
        JButton drawButton = new JButton(m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setActionCommand("draw");
        drawButton.addActionListener(this);

	JButton closeButton = new JButton(m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.setActionCommand("close");
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

    /*
    public static void showMe()
    {
        if (singleton == null)
            singleton = new CourseGraphFrame();
        singleton.show();
    }
    */
/*
    public static void closeMe()
    {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
            cGV = null;
        }
        this.dispose();
    } */

    /*
    public static CourseGraphFrame getInstance()
    {
        if (singleton == null)
            singleton = new CourseGraphFrame();
        return singleton;
    }*/

    /*
    public static void redraw()
    {
        if (singleton != null)
            singleton.repaint();
    }*/

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


    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
	    closeDialog();
        }
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

}
