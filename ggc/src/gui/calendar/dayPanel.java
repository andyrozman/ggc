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
 *  Filename: dayPanel.java
 *  Purpose:  The button Panel to select a day in a month.
 *
 *  Author:   schultd
 */

package gui.calendar;


import datamodels.calendar.CalendarEvent;
import datamodels.calendar.CalendarListener;
import datamodels.calendar.CalendarModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class dayPanel extends JPanel implements CalendarListener
{
    CalendarModel cMod;

    public dayPanel(CalendarModel cMod)
    {
        this.cMod = cMod;
        setLayout(new GridLayout(6, 7));
        doLayoutButtons();
    }

    private void doLayoutButtons()
    {
        //System.out.println("day" + cMod.getDay());
        //System.out.println("month" + cMod.getMonth());
        //System.out.println("year" + cMod.getYear());

        removeAll();
        //setLayout(new GridLayout(6,7));
        this.invalidate();

        add(new JLabel("Su"));
        add(new JLabel("Mo"));
        add(new JLabel("Tu"));
        add(new JLabel("We"));
        add(new JLabel("Th"));
        add(new JLabel("Fr"));
        add(new JLabel("Sa"));

        int firstday = cMod.getFirstDayInMonth() - 1;
        int numdays = firstday + cMod.getNumDaysInMonth();
        int curday = firstday + cMod.getDay();


        for (int i = 0; i < firstday; i++) {
            add(new JLabel(""));
        }

        int lbl = 0;
        for (int i = firstday; i < curday - 1; i++) {
            add(createButton(++lbl));
        }

        JButton button2 = new JButton(++lbl + "");
        button2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        button2.setFont(new Font("Dialog", Font.BOLD, 12));
        add(button2);

        for (int i = curday; i < numdays; i++) {
            add(createButton(++lbl));
        }

        this.validate();
        this.repaint();

    }

    private JButton createButton(final int j)
    {
        JButton button = new JButton(j + "");
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(22, 20));
        button.setFont(new Font("Dialog", Font.PLAIN, 12));
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cMod.setDay(j);
            }
        });
        return button;
    }

    public void dateHasChanged(CalendarEvent e)
    {
        doLayoutButtons();
    }
}
