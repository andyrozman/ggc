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
 *  Filename: CalendarEvent.java
 *  Purpose:  Event for CalendarListeners.
 *
 *  Author:   schultd
 */

package datamodels.calendar;


import java.util.Calendar;
import java.util.GregorianCalendar;


public class CalendarEvent
{

    GregorianCalendar date;

    public CalendarEvent(GregorianCalendar date)
    {
        this.date = date;
        //System.out.println("gregcal in calevent: " + date.toString());
    }

    public int getNewMonth()
    {
        return date.get(Calendar.MONTH);
    }

    public int getNewYear()
    {
        return date.get(Calendar.YEAR);
    }

    public int getNewDay()
    {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    public long getNewDate()
    {
        return date.getTime().getTime();
    }
}
