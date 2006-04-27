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
 *  Filename: yearPanel.java
 *  Purpose:  currently only a textfield to enter a year. will be changed to
 *            something better in the future.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import ggc.data.calendar.CalendarEvent;
import ggc.data.calendar.CalendarListener;
import ggc.data.calendar.CalendarModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class YearPanel extends JPanel implements CalendarListener
{
    private JTextField yearField;

    public YearPanel(final CalendarModel cMod)
    {
        yearField = new JTextField(cMod.getYear() + "", 4);
        add(yearField);

        yearField.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cMod.setYear((new Integer(yearField.getText())).intValue());
            }
        });
    }

    public void dateHasChanged(CalendarEvent e)
    {
        yearField.setText(e.getNewYear() + "");
    }
}
