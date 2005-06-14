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

package ggc.gui;


import ggc.datamodels.GlucoValues;
import ggc.gui.calendar.DateRangeSelectionPanel;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;
import ggc.view.CourseGraphView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CourseGraphFrame extends JFrame
{

    private I18nControl m_ic = I18nControl.getInstance();    

    private static CourseGraphView cGV;
    private static CourseGraphFrame singleton = null;

    private GGCProperties props = GGCProperties.getInstance();

    private JCheckBox chkBG;
    private JCheckBox chkAvgBGDay;
    private JCheckBox chkSumBU;
    private JCheckBox chkMeals;
    private JCheckBox chkSumIns1;
    private JCheckBox chkSumIns2;
    private JCheckBox chkSumIns;
    private JCheckBox chkInsPerBU;
    private DateRangeSelectionPanel dRS;


    public CourseGraphFrame()
    {
        super("Course Graph");
        setTitle(m_ic.getMessage("COURSE_GRAPH"));
        setBounds(200, 400, 700, 300);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        cGV = new CourseGraphView();
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
        selectionPanel.add(chkBG = new JCheckBox(m_ic.getMessage("BG"), true));
        selectionPanel.add(chkSumBU = new JCheckBox(m_ic.getMessage("SUM_BU"), false));
        selectionPanel.add(chkSumIns1 = new JCheckBox(m_ic.getMessage("SUM") + " " + props.getIns1Abbr(), false));
        selectionPanel.add(chkSumIns = new JCheckBox(m_ic.getMessage("SUM_INSULIN"), false));
        selectionPanel.add(chkAvgBGDay = new JCheckBox(m_ic.getMessage("AVG_BG_PER_DAY"), false));
        selectionPanel.add(chkMeals = new JCheckBox(m_ic.getMessage("MEALS"), false));
        selectionPanel.add(chkSumIns2 = new JCheckBox(m_ic.getMessage("SUM")+" " + props.getIns2Abbr(), false));
        selectionPanel.add(chkInsPerBU = new JCheckBox(m_ic.getMessage("INS_SLASH_BU"), false));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension dim = new Dimension(80, 20);
        JButton drawButton = new JButton(m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setNewDateRange();
                redraw();
            }
        });
        JButton closeButton = new JButton(m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                closeMe();
            }
        });
        buttonPanel.add(drawButton);
        buttonPanel.add(closeButton);

        cPanel.add(dRS, BorderLayout.WEST);
        cPanel.add(selectionPanel, BorderLayout.CENTER);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        cGV.setGlucoValues(new GlucoValues(dRS.getStartDate(), dRS.getEndDate()));
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new CourseGraphFrame();
        singleton.show();
    }

    public static void closeMe()
    {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
            cGV = null;
        }
    }

    public static CourseGraphFrame getInstance()
    {
        if (singleton == null)
            singleton = new CourseGraphFrame();
        return singleton;
    }

    public static void redraw()
    {
        if (singleton != null)
            singleton.repaint();
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

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            closeMe();
        }
    }
}
