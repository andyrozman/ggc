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

package ggc.gui.dialogs;


import ggc.datamodels.GlucoValues;
import ggc.gui.calendar.DateRangeSelectionPanel;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.gui.view.SpreadGraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class SpreadGraphDialog extends JDialog implements ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();

    private static SpreadGraphView sGV;
    //private static SpreadGraphFrame singleton = null;

    private GGCProperties props = GGCProperties.getInstance();

    private JCheckBox chkBG;
    private JCheckBox chkBU;
    private JCheckBox chkIns1;
    private JCheckBox chkIns2;
    private JCheckBox chkConnect;
    private DateRangeSelectionPanel dRS;

    public SpreadGraphDialog(JFrame parent)
    {
        super();
        setTitle(m_ic.getMessage("SPREAD_GRAPH"));

	Rectangle rec = parent.getBounds();
	int x = rec.x + (rec.width/2);
	int y = rec.y + (rec.height/2);


	setBounds(x-350, y-250, 700, 500);


        addWindowListener(new CloseListener());

        sGV = new SpreadGraphView(this);
        getContentPane().add(sGV, BorderLayout.CENTER);

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
        selectionPanel.add(chkBG = new JCheckBox(m_ic.getMessage("BG"), true));
        selectionPanel.add(chkIns1 = new JCheckBox(props.getIns1Abbr(), false));
        selectionPanel.add(chkBU = new JCheckBox(m_ic.getMessage("BU"), false));
        selectionPanel.add(chkIns2 = new JCheckBox(props.getIns2Abbr(), false));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("OPTIONS_")+":"));
        optionsPanel.add(chkConnect = new JCheckBox(m_ic.getMessage("CONNECT_VALUES_FOR_ONE_DAY"), false));

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
        cPanel.add(optionsPanel, BorderLayout.EAST);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        sGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }

    /*
    public static void showMe()
    {
        if (singleton == null)
            singleton = new SpreadGraphFrame();
        singleton.show();
    }*/

/*
    public static void closeMe()
    {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
            sGV = null;
        }
    }
    */

/*
    public static SpreadGraphFrame getInstance()
    {
        if (singleton == null)
            singleton = new SpreadGraphFrame();
        return singleton;
    }
*/

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

    public boolean getDrawBU()
    {
        return chkBU.isSelected();
    }

    public boolean getDrawIns1()
    {
        return chkIns1.isSelected();
    }

    public boolean getDrawIns2()
    {
        return chkIns2.isSelected();
    }

    public boolean getConnectDays()
    {
        return chkConnect.isSelected();
    }


    private void closeDialog()
    {
	sGV = null;
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
	    sGV.repaint(this.getBounds());
	}
	else if (action.equals("close")) 
	{
	    closeDialog();
	    //this.dispose();
	}
	else
	    System.out.println("SpreadGraphFrame: Unknown command: " + action);
    }


}
