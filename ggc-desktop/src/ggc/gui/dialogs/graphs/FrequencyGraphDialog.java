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

package ggc.gui.dialogs.graphs;

import ggc.core.data.GlucoValues;
import ggc.core.data.graph.DataPlotSelectorPanel;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.gui.graphs.FrequencyGraphView;

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


public class FrequencyGraphDialog extends JDialog implements ActionListener, HelpCapable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7116976481780328914L;
    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = null;

    private FrequencyGraphView fGV;
    JButton help_button = null;

    // ivate GGCProperties props = GGCProperties.getInstance();
    private DateRangeSelectionPanel dRS;

    /**
     * Constructor
     * 
     * @param da
     */
    public FrequencyGraphDialog(DataAccess da)
    {
        super(da.getMainParent(), "CourseGraphFrame", true);
        setTitle(m_ic.getMessage("FREQGRAPHFRAME") + " [" + m_ic.getMessage("NOT_WORKING_100PRO") + "]");

        this.m_da = da;
        /*
         * Rectangle rec = parent.getBounds(); int x = rec.x + (rec.width/2);
         * int y = rec.y + (rec.height/2);
         * 
         * setBounds(x-350, y-250, 700, 500); addWindowListener(new
         * CloseListener());
         */
        setSize(700, 520);

        fGV = new FrequencyGraphView();
        getContentPane().add(fGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        m_da.enableHelp(this);

        setVisible(true);
    }

    private JPanel initControlPanel()
    {
        JPanel cPanel = new JPanel(new BorderLayout());

        dRS = new DateRangeSelectionPanel(m_da);

        DataPlotSelectorPanel selectionPanel = new DataPlotSelectorPanel(DataPlotSelectorPanel.BG_READINGS_MASK);
        fGV.setData(selectionPanel.getPlotData());
        selectionPanel.disableChoice(DataPlotSelectorPanel.BG_AVG_MASK | DataPlotSelectorPanel.BG_MASK
                | DataPlotSelectorPanel.CH_AVG_MASK | DataPlotSelectorPanel.CH_SUM_MASK
                | DataPlotSelectorPanel.INS1_AVG_MASK | DataPlotSelectorPanel.INS1_SUM_MASK
                | DataPlotSelectorPanel.INS2_AVG_MASK | DataPlotSelectorPanel.INS2_SUM_MASK
                | DataPlotSelectorPanel.INS_TOTAL_AVG_MASK | DataPlotSelectorPanel.INS_TOTAL_MASK
                | DataPlotSelectorPanel.INS_TOTAL_SUM_MASK | DataPlotSelectorPanel.INS_PER_CH_MASK
                | DataPlotSelectorPanel.CH_MASK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension dim = new Dimension(120, 25);

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        buttonPanel.add(help_button);

        JButton drawButton = new JButton(m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setActionCommand("draw");
        drawButton.setIcon(m_da.getImageIcon_22x22("paint.png", this));
        drawButton.addActionListener(this);

        JButton closeButton = new JButton(m_ic.getMessage("CLOSE"));
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
        fGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }

    private void closeDialog()
    {
        fGV = null;
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
            fGV.invalidate();
            fGV.validate();
            fGV.repaint();
            // fGV.repaint(this.getBounds());
        }
        else if (action.equals("close"))
        {
            closeDialog();
        }
        else
            System.out.println("FrequencyGraphFrame: Unknown command: " + action);
    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "pages.GGC_BG_Graph_Frequency";
    }

}
