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
 *  Filename: ReadMeterFrame.java
 *  Purpose:  Frame to read data from a Meter.
 *
 *  Author:   schultd
 */

package gui;


import datamodels.GlucoTableModel;
import datamodels.GlucoValues;
import meter.GlucoReader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ReadMeterFrame extends JFrame
{

    static ReadMeterFrame singleton = null;
    JTextArea logText = null;

    GlucoValues gV = null;
    GlucoTableModel model = null;
    private JTable resTable;

    public ReadMeterFrame()
    {
        super("Read Meter Data");
        gV = new GlucoValues();

        //String[] tmp = {"Date","Time","BG","Ins1","Ins2","BE","Act","Comment"};
        //dv.setColumnNames(tmp);

        model = new GlucoTableModel(gV);
        init();
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new ReadMeterFrame();
        singleton.show();
    }

    public static ReadMeterFrame getInstance()
    {
        return singleton;
    }

    public void close()
    {
        dispose();
        singleton = null;
    }

    public void init()
    {
        setBounds(150, 150, 550, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        resTable = new JTable(model);
        resTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane sp2 = new JScrollPane(resTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GlucoReader gReader = new GlucoReader();
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gV.saveValues();
            }
        });

        JButton addButton = new JButton("Add Row");
        addButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                AddRowFrame aRF = AddRowFrame.getInstance(model, gV);
                aRF.show();
            }
        });

        JButton delButton = new JButton("Delete Row");
        delButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                //System.out.println("getEditingRow(): " + resTable.getEditingRow());
                //System.out.println("getSelectedRow(): " + resTable.getSelectedRow());
                gV.deleteRow(resTable.getSelectedRow());
                model.fireTableChanged(null);
            }
        });

        Box BBox = Box.createHorizontalBox();
        BBox.add(startButton);
        BBox.add(addButton);
        BBox.add(delButton);
        BBox.add(saveButton);

        getContentPane().add(BBox, BorderLayout.SOUTH);

        logText = new JTextArea("log:\n", 8, 35);
        //logText.setPreferredSize(new Dimension(300,200));
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        getContentPane().add(sp, BorderLayout.NORTH);

        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                singleton.repaint();
            }
        });

        getContentPane().add(sp2, BorderLayout.CENTER);
    }

    public void addLogText(String s)
    {
        logText.append(s + "\n");
    }

    public GlucoValues getGlucoValues()
    {
        return gV;
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }

    public GlucoTableModel getResTableModel()
    {
        return model;
    }


}
