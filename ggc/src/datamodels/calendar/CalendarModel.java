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
 *  Filename: CalendarModel.java
 *  Purpose:  A model for every JComponent which is involved in the Calendar
 *            widgets, to get calendardata.
 *
 *  Author:   schultd
 */

package datamodels.calendar;


import gui.calendar.calendarPane;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CalendarModel
{
    private GregorianCalendar gc;
    calendarPane cp;

    public CalendarModel(Date date, calendarPane cp)
    {
        this.cp = cp;
        gc = new GregorianCalendar();
        gc.setTime(date);
    }

    public void setDate(Date date)
    {
        gc.setTime(date);
        cp.notifyListeners();
    }

    public void setYear(int year)
    {
        gc.set(Calendar.YEAR, year);
        cp.notifyListeners();
    }

    public void setMonth(int month)
    {
        gc.set(Calendar.MONTH, month);
        cp.notifyListeners();
    }

    public void setDay(int day)
    {
        gc.set(Calendar.DAY_OF_MONTH, day);
        cp.notifyListeners();
    }

    public int getYear()
    {
        return gc.get(Calendar.YEAR);
    }

    public int getMonth()
    {
        return gc.get(Calendar.MONTH);
    }

    public int getDay()
    {
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    public int getFirstDayInMonth()
    {
        int old = getDay();
        gc.set(Calendar.DAY_OF_MONTH, 1);
        int r = gc.get(Calendar.DAY_OF_WEEK);
        gc.set(Calendar.DAY_OF_MONTH, old);
        return r;
    }

    public int getNumDaysInMonth()
    {
        return gc.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
