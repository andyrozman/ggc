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

package gui;


import datamodels.GlucoValues;
import util.GGCProperties;
import view.FrequencyGraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class FrequencyGraphFrame extends JFrame
{
    private static FrequencyGraphView fGV;
    private static FrequencyGraphFrame singleton = null;

    private GGCProperties props = GGCProperties.getInstance();

    private JTextField fieldStartDate;
    private JTextField fieldEndDate;

    public FrequencyGraphFrame()
    {
        super("CourseGraphFrame");
        setBounds(200, 400, 700, 300);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Starting Date:"));
        fieldStartDate = new JTextField("01.07.2002", 10);
        datePanel.add(fieldStartDate);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Ending Date:"));
        fieldEndDate = new JTextField("26.07.2002", 10);
        datePanel.add(fieldEndDate);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension dim = new Dimension(80, 20);
        JButton drawButton = new JButton("Draw");
        drawButton.setPreferredSize(dim);
        drawButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setNewDateRange();
                redraw();
            }
        });
        JButton closeButton = new JButton("Close");
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

        cPanel.add(datePanel, BorderLayout.NORTH);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        return cPanel;
    }

    private void setNewDateRange()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            fGV.setGlucoValues(new GlucoValues(sdf.parse(fieldStartDate.getText()), sdf.parse(fieldEndDate.getText())));
        } catch (ParseException e) {
            System.out.println(e);
        }
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new FrequencyGraphFrame();
        singleton.show();
    }

    public static void closeMe()
    {
        if (singleton != null) {
            singleton.dispose();
            singleton = null;
            fGV = null;
        }
    }

    public static FrequencyGraphFrame getInstance()
    {
        if (singleton == null)
            singleton = new FrequencyGraphFrame();
        return singleton;
    }

    public static void redraw()
    {
        if (singleton != null)
            singleton.repaint();
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            closeMe();
        }
    }
}
