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

package ggc.gui.dialogs.graphs;

import ggc.core.data.GlucoValues;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.gui.graphs.CourseGraphView;
import ggc.gui.graphs.DataPlotSelectorPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.atech.graphics.calendar.DateRangeSelectionPanel;
import com.atech.help.HelpCapable;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     ###--###  
 *  Description:  ###--###
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class CourseGraphDialog extends JDialog implements ActionListener, HelpCapable // JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = 8111521124871307877L;
    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = null;

    private CourseGraphView cGV;

    private DateRangeSelectionPanel dRS;
    private JButton help_button = null;

    public CourseGraphDialog(DataAccess da)
    {
        super(da.getMainParent(), "Course Graph", true);
        setTitle(m_ic.getMessage("COURSE_GRAPH"));

        this.m_da = da;

        setSize(800, 580);
        m_da.centerJDialog(this, da.getMainParent());

        cGV = new CourseGraphView();
        getContentPane().add(cGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        m_da.enableHelp(this);

        setVisible(true);
    }

    private JPanel initControlPanel()
    {
        JPanel cPanel = new JPanel(new BorderLayout());

        dRS = new DateRangeSelectionPanel(m_da);

        DataPlotSelectorPanel selectionPanel = new DataPlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
        selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK
                | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
                | DataPlotSelectorPanel.INS_TOTAL_MASK);
        cGV.setData(selectionPanel.getPlotData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        buttonPanel.add(help_button);

        // Dimension dim = new Dimension(80, 20);
        Dimension dim = new Dimension(120, 25);
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
            // cGV.repaint(this.getBounds());
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
    // ****** HelpCapable Implementation *****
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
