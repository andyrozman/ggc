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

package ggc.data;


import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.event.EventListenerList;

import ggc.data.event.GlucoValueEvent;
import ggc.data.event.GlucoValueEventListener;
import ggc.util.DataAccess;


// FIX

public class GlucoValues extends DailyValues
{
//    ArrayList dayValues = null;
        Vector<DailyValues> dayValues = null;

    private EventListenerList listenerList = new EventListenerList();
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

                GlucoValueEvent event = new GlucoValueEvent(this, i, i, 0, GlucoValueEvent.INSERT);
                fireGlucoValueChanged(event);
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

            GlucoValueEvent event = new GlucoValueEvent(this, dayValues.size(), dayValues.size(), 0, GlucoValueEvent.INSERT);
            fireGlucoValueChanged(event);
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

                    GlucoValueEvent event = new GlucoValueEvent(this, row, row, 0, GlucoValueEvent.DELETE);
                    fireGlucoValueChanged(event);
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

        GlucoValueEvent event = new GlucoValueEvent(this, row, row, column, GlucoValueEvent.UPDATE);
        fireGlucoValueChanged(event);
    }

    public int getDayCount()
    {
        if (dayValues != null)
            return dayValues.size();
        return 0;
    }

    public String getDateForDayAt(int i)
    {
        return (dayValues.elementAt(i)).getDayAndMonthAsString();
    }


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
    }
}
