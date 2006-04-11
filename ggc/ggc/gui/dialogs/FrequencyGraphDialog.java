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
 *  Filename: FrequencyGraphFrame.java
 *  Purpose:  Container for the View and some controls.
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

import ggc.datamodels.GlucoValues;
import ggc.gui.calendar.DateRangeSelectionPanel;
import ggc.gui.view.FrequencyGraphView;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


public class FrequencyGraphDialog extends JDialog implements ActionListener
{
    private I18nControl m_ic = I18nControl.getInstance();    
    //private DataAccess m_da = DataAccess.getInstance();

    private static FrequencyGraphView fGV;

    //ivate GGCProperties props = GGCProperties.getInstance();
    private DateRangeSelectionPanel dRS;


    public FrequencyGraphDialog(JFrame parent)
    {
        super(parent, "CourseGraphFrame", true);
        setTitle(m_ic.getMessage("FREQGRAPHFRAME"));

	Rectangle rec = parent.getBounds();
	int x = rec.x + (rec.width/2);
	int y = rec.y + (rec.height/2);

	setBounds(x-350, y-250, 700, 500);
        addWindowListener(new CloseListener());

        fGV = new FrequencyGraphView();
        getContentPane().add(fGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel initControlPanel()
    {
        JPanel cPanel = new JPanel(new BorderLayout());

        dRS = new DateRangeSelectionPanel();

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
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        fGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }


    private void closeDialog()
    {
	fGV = null;
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
	    fGV.repaint(this.getBounds());
	}
	else if (action.equals("close")) 
	{
	    closeDialog();
	}
	else
	    System.out.println("FrequencyGraphFrame: Unknown command: " + action);
    }

}
