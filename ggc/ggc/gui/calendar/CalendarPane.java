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
 *  Filename: calendarPane.java
 *  Purpose:  The Pane which contains the whole calendar widget.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import ggc.datamodels.calendar.CalendarEvent;
import ggc.datamodels.calendar.CalendarListener;
import ggc.datamodels.calendar.CalendarModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;


public class calendarPane extends JPanel
{
    private monthPanel mPanel;
    private yearPanel yPanel;
    private dayPanel dPanel;

    CalendarModel cModel;

    Vector listeners = new Vector();

    public calendarPane()
    {
        cModel = new CalendarModel(new Date(), this);


        //
        //this.setLayout(null);

        mPanel = new monthPanel(cModel);
        yPanel = new yearPanel(cModel);

        setLayout(new BorderLayout());

        Box a = Box.createHorizontalBox();
        //a.setBounds(10, 10, 120, 30);

        a.add(mPanel);
        a.add(yPanel);

        add(a, BorderLayout.NORTH);

        dPanel = new dayPanel(cModel);
        add(dPanel, BorderLayout.CENTER);

        this.addCalendarListener(dPanel);
        this.addCalendarListener(mPanel);
        this.addCalendarListener(yPanel);

        //setBounds(10, 10, 200, 200);
    }

    public Date getSelectedDate()
    {
        return cModel.getDate();
    }

    public void addCalendarListener(CalendarListener l)
    {
        listeners.addElement(l);
    }

    public void removeCalendarListener(CalendarListener l)
    {
        listeners.removeElement(l);
    }

    public void notifyListeners(int event)
    {
        //System.out.println("notifyListeners: " + cModel.getYear() + " " + cModel.getMonth()+ " " + cModel.getDay());
        notifyListeners(new CalendarEvent(new GregorianCalendar(cModel.getYear(), cModel.getMonth(), cModel.getDay()), event));
    }

    public void notifyListeners(CalendarEvent e)
    {
        for (int i = 0; i < listeners.size(); i++) 
        {
            CalendarListener l = (CalendarListener)listeners.elementAt(i);
            l.dateHasChanged(e);
        }
    }
}
