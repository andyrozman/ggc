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
 *  Filename: YearPanel
 *  
 *  Purpose:  currently only a textfield to enter a year. will be changed to
 *            something better in the future.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import ggc.core.data.calendar.CalendarEvent;
import ggc.core.data.calendar.CalendarListener;
import ggc.core.data.calendar.CalendarModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;


public class YearPanel extends JPanel implements CalendarListener
{
    /**
     * 
     */
    private static final long serialVersionUID = -2088319783321709765L;
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
