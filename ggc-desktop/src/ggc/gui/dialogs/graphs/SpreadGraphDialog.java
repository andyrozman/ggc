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
 *  Filename: SpreadGraphFrame.java
 *  Purpose:  Container for the SpreadView, plus some controls.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs.graphs;

import com.atech.utils.ATSwingUtils;
import ggc.core.data.GlucoValues;
import ggc.core.data.graph.DataPlotSelectorPanel;
import ggc.core.util.DataAccess;
import ggc.gui.graphs.SpreadGraphView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.atech.graphics.calendar.DateRangeSelectionPanel;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

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

public class SpreadGraphDialog extends JDialog implements ActionListener, HelpCapable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8611132057833510005L;

    private JButton help_button = null;

    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private SpreadGraphView sGV;
    // private static SpreadGraphFrame singleton = null;

    // private GGCProperties props = GGCProperties.getInstance();

    private JCheckBox chkConnect;
    private DateRangeSelectionPanel dRS;

    /**
     * Constructor
     * 
     * @param da
     */
    public SpreadGraphDialog(DataAccess da)
    {
        super(da.getMainParent(), "", true);
        setTitle(m_ic.getMessage("SPREAD_GRAPH"));

        setSize(800, 520);
        ATSwingUtils.centerJDialog(this, da.getMainParent());

        sGV = new SpreadGraphView();
        getContentPane().add(sGV, BorderLayout.CENTER);

        JPanel controlPanel = initControlPanel();
        getContentPane().add(controlPanel, BorderLayout.SOUTH);

        m_da.enableHelp(this);

        setVisible(true);
    }

    private JPanel initControlPanel()
    {
        JPanel cPanel = new JPanel(new BorderLayout());

        dRS = new DateRangeSelectionPanel(m_da);

        DataPlotSelectorPanel selectionPanel = new DataPlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
        sGV.setData(selectionPanel.getPlotData());

        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("OPTIONS_") + ":"));
        optionsPanel.add(chkConnect = new JCheckBox("  " + m_ic.getMessage("CONNECT_VALUES_FOR_ONE_DAY"), false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension dim = new Dimension(120, 25);

        help_button = ATSwingUtils.createHelpButtonBySize(120, 25, this, m_da);
        buttonPanel.add(help_button);

        JButton drawButton = new JButton("    " + m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setIcon(ATSwingUtils.getImageIcon_22x22("paint.png", this, m_da));
        drawButton.setActionCommand("draw");
        drawButton.addActionListener(this);

        JButton closeButton = new JButton("    " + m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.setActionCommand("close");
        closeButton.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        closeButton.addActionListener(this);
        buttonPanel.add(drawButton);
        buttonPanel.add(closeButton);

        cPanel.add(dRS, BorderLayout.WEST);
        cPanel.add(selectionPanel, BorderLayout.CENTER);
        cPanel.add(optionsPanel, BorderLayout.EAST);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        sGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }

    private void closeDialog()
    {
        sGV = null;
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
            // sGV.repaint(this.getBounds());
            sGV.setConnect(chkConnect.isSelected());
            sGV.repaint();
        }
        else if (action.equals("close"))
        {
            closeDialog();
            // this.dispose();
        }
        else
        {
            System.out.println("SpreadGraphFrame: Unknown command: " + action);
        }
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
        return "GGC_BG_Graph_Spread";
    }

}
