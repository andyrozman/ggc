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
 *  Filename: CalendarPane
 *  
 *  Purpose:  The Pane which contains the whole calendar widget.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import ggc.core.data.calendar.CalendarEvent;
import ggc.core.data.calendar.CalendarListener;
import ggc.core.data.calendar.CalendarModel;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JPanel;


public class CalendarPane extends JPanel
{
    private MonthPanel mPanel;
    private YearPanel yPanel;
    private DayPanel dPanel;

    CalendarModel cModel;

    Vector<CalendarListener> listeners = new Vector<CalendarListener>();

    public CalendarPane()
    {
        cModel = new CalendarModel(new GregorianCalendar(), this);


        //
        //this.setLayout(null);

        mPanel = new MonthPanel(cModel);
        yPanel = new YearPanel(cModel);

        setLayout(new BorderLayout());

        Box a = Box.createHorizontalBox();
        //a.setBounds(10, 10, 120, 30);

        a.add(mPanel);
        a.add(yPanel);

        add(a, BorderLayout.NORTH);

        dPanel = new DayPanel(cModel);
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
            CalendarListener l = listeners.elementAt(i);
            l.dateHasChanged(e);
        }
    }
}
