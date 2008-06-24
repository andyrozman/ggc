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
 *  Filename: GlucoValues.java
 *  Purpose:  The data read from the meter and translated into a more general
 *            form.
 *
 *  Author:   schultd
 */

package ggc.core.data;


import ggc.core.util.DataAccess;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;



public class GlucoValues extends DailyValues
{
//    ArrayList dayValues = null;
        Vector<DailyValues> dayValues = null;

    //private EventListenerList listenerList = new EventListenerList();
    private DataAccess m_da = DataAccess.getInstance();

    //private int recordCount = 0;

    public GlucoValues()
    {
        dayValues = new Vector<DailyValues>();
        //dayValues = new ArrayList();
    }

    public GlucoValues(GregorianCalendar sDate, GregorianCalendar eDate)
    {
        this();

        //dayValues.add(DataAccess.getInstance().getDayStatsRange(sDate, eDate));


        WeeklyValues wv = DataAccess.getInstance().getDayStatsRange(sDate, eDate);
        Hashtable<String,DailyValues> table = wv.getAllValues();

    	//System.out.println("GlucoValues: " + table.size());
    
    
    	//System.out.println("Start: " + m_da.getDateTimeStringFromGregorianCalendar(sDate,2));
    	//System.out.println("End: " + m_da.getDateTimeStringFromGregorianCalendar(eDate,2));

        for (Enumeration<String> en = table.keys(); en.hasMoreElements(); ) 
        {
            String key = (String)en.nextElement();
            addDayValues((DailyValues)table.get(key));
        }


/*
        GregorianCalendar gC = new GregorianCalendar();
        gC.setTime(sDate);
        while (gC.getTime().compareTo(eDate) <= 0) 
         {
            DailyValues dv = dbH.getDayStats(gC.getTime());
            //recordCount += dv.getRowCount();
            dayValues.add(dv);
            gC.add(Calendar.DATE, 1);
        } */
    }


    private void addDayValues(DailyValues dv)
    {
        //System.out.println("DailyValues: " + dv);

        for (int i=0; i<dv.getRowCount(); i++) 
        {
            addRow(dv.getRowAt(i));
        }

    }


    @Override
    public void addRow(DailyValuesRow dRow)
    {
        String s1 = dRow.getDateAsString();

        for (int i = 0; i < dayValues.size(); i++) 
        {
            DailyValues dV = dayValues.elementAt(i);
            //System.out.println("date1:" + s1 + "");
            //System.out.println("date2:" + dV.getDateAsString() + "");
            if (s1.equals(dV.getDateAsString())) 
            {
                dV.setNewRow(dRow);

//X                GlucoValueEvent event = new GlucoValueEvent(this, i, i, 0, GlucoValueEvent.INSERT);
//X                fireGlucoValueChanged(event);
                return;
            }
        }

        if (dRow.getDateTime() != 0) 
        {
            DailyValues dV = new DailyValues();

            dV.addRow(dRow);
            //dV.setD.setDate(dRow.getDateTime());
            dV.setIsNew(true);
            dayValues.add(dV);

//X            GlucoValueEvent event = new GlucoValueEvent(this, dayValues.size(), dayValues.size(), 0, GlucoValueEvent.INSERT);
//X            fireGlucoValueChanged(event);
        }
    }

    @Override
    public void setNewRow(DailyValuesRow dRow)
    {
        //DataBaseHandler dbH = DataBaseHandler.getInstance();
        if (!m_da.getDb().dateTimeExists(dRow.getDateTime())) 
        {
            addRow(dRow);
            //rMF.getResTableModel().fireTableChanged(null);
        }
    }

    public void saveValues()
    {
        for (int i = 0; i < dayValues.size(); i++)
            dayValues.elementAt(i).saveDay();
    }

    @Override
    public int getRowCount()
    {
        int c = 0;
        for (int i = 0; i < dayValues.size(); i++)
            c += (dayValues.elementAt(i)).getRowCount();
        return c;
    }

    @Override
    public void deleteRow(int row)
    {
        if (row != -1) 
        {
            int c = 0;
            for (int i = 0; i < dayValues.size(); i++) 
            {
                int old = c;
                DailyValues dV = dayValues.elementAt(i);
                c += dV.getRowCount();
                if (old <= row && row < c) 
                {
                    dV.deleteRow(row - old);

//X                    GlucoValueEvent event = new GlucoValueEvent(this, row, row, 0, GlucoValueEvent.DELETE);
//X                    fireGlucoValueChanged(event);
                    return;
                }
            }
        }
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        int c = 0;
        Object o = null;
        for (int i = 0; i < dayValues.size(); i++) 
        {
            int old = c;
            DailyValues dV = dayValues.elementAt(i);
            c += dV.getRowCount();
            if ((old <= row) && (row < c))
                o = dV.getValueAt(row - old, column);
        }
        return o;
    }

    public DailyValues getDailyValuesForDay(int day)
    {
        return dayValues.elementAt(day);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        int c = 0;
        for (int i = 0; i < dayValues.size(); i++) 
        {
            int old = c;
            DailyValues dV = dayValues.elementAt(i);
            c += dV.getRowCount();
            if ((old <= row) && (row < c)) 
                dV.setValueAt(aValue, row - old, column);
        }

//X        GlucoValueEvent event = new GlucoValueEvent(this, row, row, column, GlucoValueEvent.UPDATE);
//X        fireGlucoValueChanged(event);
    }

    // FIXME wontwork
    public long getDayCount()
    {
        long firstDay = Long.MAX_VALUE, lastDay = Long.MIN_VALUE, currentDay, dayCount;
        
        for (DailyValues dayValue : dayValues)
        {
            currentDay = dayValue.getDate();
            if (currentDay > lastDay)
                lastDay = currentDay;
            if (currentDay < firstDay)
                firstDay = currentDay;
        }
        
        if (firstDay == Long.MAX_VALUE)
            return 0L;
        if (firstDay == lastDay)
        {
            return 1L;
        }
        
        int day, month, year;
        GregorianCalendar firstCal, lastCal;

        day = (int) (firstDay % 100);
        month = ((int) (firstDay % 10000))/100;
        year = (int) (firstDay / 10000);
        firstCal = new GregorianCalendar(year, month - 1, day);
        firstDay = firstCal.getTimeInMillis();

        day = (int) (lastDay % 100);
        month = ((int) (lastDay % 10000))/100;
        year = (int) (lastDay / 10000);
        lastCal = new GregorianCalendar(year, month - 1, day);
        lastDay = lastCal.getTimeInMillis();

        long timeDiff = lastDay - firstDay;
        dayCount = (timeDiff / (1000 * 60 * 60 * 24));
        
        // round up if neccessary
        if ((dayCount * (1000 * 60 * 60 * 24)) != timeDiff)
            dayCount++;
        
        // add 1 as the first day needs to be counted, too
        dayCount++;
        
        return dayCount;
    }

    public String getDateForDayAt(int i)
    {
        // if the caller requests a key we can't supply, return null (not an exception)
        if (dayValues.size() <= i)
        {
            return null;
        }
        else
        {
            return (dayValues.elementAt(i)).getDayAndMonthAsString();
        }
    }

/*
    public void addGlucoValueEventListener(GlucoValueEventListener listener)
    {
        listenerList.add(GlucoValueEventListener.class, listener);
    }

    public void removeGlucoValueEventListener(GlucoValueEventListener listener)
    {
        listenerList.remove(GlucoValueEventListener.class, listener);
    }

    protected void fireGlucoValueChanged(GlucoValueEvent event)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) 
        {
            if (listeners[i] instanceof GlucoValueEventListener) 
            {
                // Lazily create the event:
                if (event != null)
                    ((GlucoValueEventListener)listeners[i + 1]).glucoValuesChanged(event);
            }
        }
    } */
}
