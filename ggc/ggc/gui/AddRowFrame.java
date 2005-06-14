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

package ggc.gui;


import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;


public class AddRowFrame extends JFrame
{
    
    private I18nControl m_ic = I18nControl.getInstance();    
    
    static AddRowFrame singleton = null;
    JTextField DateField, TimeField, BGField, Ins1Field, Ins2Field, BUField, ActField, CommentField;
    JButton AddButton;
    String sDate = null;
    DailyValues dV = null;
    AbstractTableModel mod = null;

    GGCProperties props = GGCProperties.getInstance();

    public AddRowFrame(AbstractTableModel m, DailyValues ndV)
    {
        super("Add New Row");
        super.setTitle(m_ic.getMessage("ADD_NEW_ROW"));
        dV = ndV;
        mod = m;
        init();
    }

    public AddRowFrame(AbstractTableModel m, DailyValues ndV, String nDate)
    {
        super("Add New Row");
        super.setTitle(m_ic.getMessage("ADD_NEW_ROW"));
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
        a.add(new JLabel(m_ic.getMessage("DATE")+":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("BG")+":", SwingConstants.RIGHT));
        a.add(new JLabel(props.getIns1Abbr() + ":", SwingConstants.RIGHT));
        a.add(new JLabel(m_ic.getMessage("ACT")+":", SwingConstants.RIGHT));

        JPanel b = new JPanel(new GridLayout(0, 1));
        DateField = new JTextField(10);
        if (sDate != null) {
            DateField.setText(sDate);
            DateField.setEditable(false);
        }
        b.add(DateField);
        DateField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    BGField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    TimeField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        b.add(BGField = new JTextField());
        BGField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    Ins1Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    DateField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    BUField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        b.add(Ins1Field = new JTextField());
        Ins1Field.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    ActField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    BGField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    Ins2Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        b.add(ActField = new JTextField());
        ActField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    Ins1Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    CommentField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });

        JPanel c = new JPanel(new GridLayout(0, 1));
        c.add(new JLabel(m_ic.getMessage("TIME")+":", SwingConstants.RIGHT));
        c.add(new JLabel(m_ic.getMessage("BU")+":", SwingConstants.RIGHT));
        c.add(new JLabel(props.getIns2Abbr() + ":", SwingConstants.RIGHT));
        c.add(new JLabel(m_ic.getMessage("COMMENT")+":", SwingConstants.RIGHT));

        JPanel d = new JPanel(new GridLayout(0, 1));
        d.add(TimeField = new JTextField(10));
        TimeField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    BUField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    DateField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        d.add(BUField = new JTextField());
        BUField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    Ins2Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    TimeField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    BGField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        d.add(Ins2Field = new JTextField());
        Ins2Field.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    CommentField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    BUField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    Ins1Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });
        d.add(CommentField = new JTextField());
        CommentField.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    Ins2Field.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ActField.requestFocus();
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    AddButton.doClick();
            }
        });

        Box e = Box.createHorizontalBox();
        e.add(a);
        e.add(b);
        e.add(c);
        e.add(d);

        Box g = Box.createHorizontalBox();
        AddButton = new JButton(m_ic.getMessage("ADD_ROW"));
        g.add(Box.createHorizontalGlue());
        getRootPane().setDefaultButton(AddButton);
        AddButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dV.setNewRow(new DailyValuesRow(DateField.getText() + " " + TimeField.getText(), BGField.getText(), Ins1Field.getText(), Ins2Field.getText(), BUField.getText(), ActField.getText(), CommentField.getText()));
                mod.fireTableChanged(null);
                clearFields();
            }
        });

        g.add(AddButton);
        JButton CloseButton = new JButton(m_ic.getMessage("CLOSE"));
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

    private void clearFields()
    {
        TimeField.setText("");
        BGField.setText("");
        Ins1Field.setText("");
        Ins2Field.setText("");
        BUField.setText("");
        ActField.setText("");
        CommentField.setText("");
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }
}
