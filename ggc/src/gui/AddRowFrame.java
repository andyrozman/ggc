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
 *  Filename: AddRowFrame.java
 *  Purpose:  Add a new row with Values to ReadMeterFrame or DailyValuesFrame.
 *
 *  Author:   schultd
 */

package gui;


import datamodels.DailyValues;
import datamodels.DailyValuesRow;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class AddRowFrame extends JFrame
{
    static AddRowFrame singleton = null;
    JTextField DateField, TimeField, BGField, Ins1Field, Ins2Field, BUField, ActField, CommentField;
    String sDate = null;
    DailyValues dV = null;
    AbstractTableModel mod = null;

    public AddRowFrame(AbstractTableModel m, DailyValues ndV)
    {
        super("Add New Row");
        dV = ndV;
        mod = m;
        init();
    }

    public AddRowFrame(AbstractTableModel m, DailyValues ndV, String nDate)
    {
        super("Add New Row");
        sDate = nDate;
        dV = ndV;
        mod = m;
        init();
    }

    public static AddRowFrame getInstance(AbstractTableModel m, DailyValues ndV)
    {
        if (singleton == null)
            singleton = new AddRowFrame(m, ndV);
        return singleton;
    }

    public static AddRowFrame getInstance(AbstractTableModel m, DailyValues ndV, String nDate)
    {
        if (singleton == null)
            singleton = new AddRowFrame(m, ndV, nDate);
        return singleton;
    }

    private void init()
    {
        setBounds(150, 150, 300, 150);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());

        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel("Date:", SwingConstants.RIGHT));
        a.add(new JLabel("BG:", SwingConstants.RIGHT));
        a.add(new JLabel("Ins1:", SwingConstants.RIGHT));
        a.add(new JLabel("Act:", SwingConstants.RIGHT));

        JPanel b = new JPanel(new GridLayout(0, 1));
        DateField = new JTextField(10);
        if (sDate != null) {
            DateField.setText(sDate);
            DateField.setEditable(false);
        }
        b.add(DateField);
        b.add(BGField = new JTextField());
        b.add(Ins1Field = new JTextField());
        b.add(ActField = new JTextField());

        JPanel c = new JPanel(new GridLayout(0, 1));
        c.add(new JLabel("Time:", SwingConstants.RIGHT));
        c.add(new JLabel("BU:", SwingConstants.RIGHT));
        c.add(new JLabel("Ins2:", SwingConstants.RIGHT));
        c.add(new JLabel("Comment:", SwingConstants.RIGHT));

        JPanel d = new JPanel(new GridLayout(0, 1));
        d.add(TimeField = new JTextField(10));
        d.add(BUField = new JTextField());
        d.add(Ins2Field = new JTextField());
        d.add(CommentField = new JTextField());

        Box e = Box.createHorizontalBox();
        e.add(a);
        e.add(b);
        e.add(c);
        e.add(d);

        Box g = Box.createHorizontalBox();
        JButton AddButton = new JButton("Add Row");
        g.add(Box.createHorizontalGlue());
        getRootPane().setDefaultButton(AddButton);
        AddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dV.setNewRow(new DailyValuesRow(DateField.getText(), TimeField.getText(), BGField.getText(), Ins1Field.getText(), Ins2Field.getText(), BUField.getText(), ActField.getText(), CommentField.getText()));
                mod.fireTableChanged(null);
            }
        });

        g.add(AddButton);
        JButton CloseButton = new JButton("Close");
        g.add(Box.createHorizontalStrut(10));
        CloseButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        g.add(CloseButton);
        g.add(Box.createHorizontalGlue());
        getContentPane().add(g, BorderLayout.SOUTH);


        getContentPane().add(e, BorderLayout.NORTH);
        getContentPane().add(g, BorderLayout.SOUTH);
    }

    private void close()
    {
        dispose();
        singleton = null;
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }
}
