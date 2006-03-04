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
 *  Filename: HbA1cFrame.java
 *  Purpose:  gives a "guess" about the current HbA1c
 *
 *  Author:   schultd
 */

package ggc.gui;


import ggc.datamodels.HbA1cValues;
import ggc.db.DataBaseHandler;
import ggc.util.I18nControl;
import ggc.view.HbA1cView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;


public class HbA1cFrame extends JFrame
{
    private I18nControl m_ic = I18nControl.getInstance();        

    private static HbA1cFrame singleton = null;
    private static HbA1cView hbView;
    private HbA1cValues hbValues;

    private JLabel lblHbA1c;
    private JLabel lblExp;
    private JLabel lblBGAvg;
    private JLabel lblReadings;
    private JLabel lblReadingsPerDay;


    public HbA1cFrame()
    {
        super("HbA1c");
        init();

        hbValues = DataBaseHandler.getInstance().getHbA1c(new Date(System.currentTimeMillis()));
        updateLabels();

        hbView.setHbA1cValues(hbValues);
    }

    public void updateLabels()
    {
        lblExp.setText(hbValues.getValuation());
        lblHbA1c.setText(hbValues.getHbA1c_Method1() + "");
        lblBGAvg.setText(hbValues.getAvgBG() + "");
        lblReadings.setText(hbValues.getReadings() + "");
        lblReadingsPerDay.setText(hbValues.getReadingsPerDay() + "");
    }

    private void init()
    {
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());
        setBounds(100, 100, 500, 400);

        JPanel infoPanel = new JPanel(new BorderLayout());

        Box a = Box.createHorizontalBox();
        a.add(new JLabel(m_ic.getMessage("VALUATION")+":"));
        a.add(lblExp = new JLabel());

        Box b = Box.createHorizontalBox();
        b.add(new JLabel(m_ic.getMessage("BG")+" "+ m_ic.getMessage("AVG")+":"));
        b.add(lblBGAvg = new JLabel());

        Box c = Box.createHorizontalBox();
        c.add(new JLabel(m_ic.getMessage("READINGS")+":"));
        c.add(lblReadings = new JLabel());

        Box d = Box.createHorizontalBox();
        d.add(new JLabel(m_ic.getMessage("READINGS_SLASH_DAY")+":"));
        d.add(lblReadingsPerDay = new JLabel());

        Box e = Box.createVerticalBox();
        e.add(new JLabel(m_ic.getMessage("YOUR_CURRENT")+" HbA1c:"));
        lblHbA1c = new JLabel();
        lblHbA1c.setFont(new Font("Dialog", Font.BOLD, 16));
        e.add(lblHbA1c);
        e.add(a);
        e.add(Box.createVerticalStrut(20));
        e.add(b);
        e.add(c);
        e.add(d);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton(m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(new Dimension(80, 20));
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                closeMe();
            }
        });
        buttonPanel.add(closeButton);

        infoPanel.add(buttonPanel, BorderLayout.SOUTH);
        infoPanel.add(e, BorderLayout.NORTH);

        hbView = new HbA1cView();

        getContentPane().add(hbView, BorderLayout.CENTER);
        getContentPane().add(infoPanel, BorderLayout.EAST);
    }

    public void setHbA1cText(String s)
    {
        lblHbA1c.setText(s);
    }

    public void setBGAvgText(String s)
    {
        lblBGAvg.setText(s);
    }

    public void setReadingsText(String s)
    {
        lblReadings.setText(s);
    }

    public void setReadingsPerDayText(String s)
    {
        lblReadingsPerDay.setText(s);
    }

    public void setExpressivnessText(String s)
    {
        lblExp.setText(s);
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new HbA1cFrame();
        singleton.show();
    }

    public static void closeMe()
    {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
        }
    }

    public static HbA1cFrame getInstance()
    {
        if (singleton == null)
            singleton = new HbA1cFrame();
        return singleton;
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            closeMe();
        }
    }
}