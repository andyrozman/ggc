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
 *  Filename: monthPanel.java
 *  Purpose:  ComboBox to select a month.
 *
 *  Author:   schultd
 */

package gui.calendar;


import datamodels.calendar.CalendarEvent;
import datamodels.calendar.CalendarListener;
import datamodels.calendar.CalendarModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class monthPanel extends JPanel implements CalendarListener
{
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private JComboBox monthCombo;

    public monthPanel(final CalendarModel cMod)
    {
        monthCombo = new JComboBox(months);
        monthCombo.setSelectedIndex(cMod.getMonth());
        add(monthCombo);

        monthCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cMod.setMonth(monthCombo.getSelectedIndex());
            }
        });
    }

    public void dateHasChanged(CalendarEvent e)
    {
        if (e.getEvent() > CalendarEvent.MONTH_CHANGED)
            monthCombo.setSelectedIndex(e.getNewMonth());
    }
}
