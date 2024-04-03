package ggc.core.data;

import java.util.*;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.util.DataAccess;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GlucoValues  
 *  Description:  This is collection of DailyValuesRow for different tasks:
 *                graphs, printing and other stuff.
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */

public class GlucoValues // extends DailyValues
{

    // private static final long serialVersionUID = 3904480643937213485L;
    Vector<DailyValues> dayValues = null;
    List<DailyValuesRow> dayValuesRows = null;
    // private DataAccess dataAccess = DataAccess.getInstance();
    GregorianCalendar from_date;
    GregorianCalendar to_date;
    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     */
    public GlucoValues()
    {
        dayValues = new Vector<DailyValues>();
        // dayValues = new ArrayList();
        dayValuesRows = new ArrayList<DailyValuesRow>();
    }


    /**
     * Constructor
     * 
     * @param sDate
     * @param eDate
     */
    public GlucoValues(GregorianCalendar sDate, GregorianCalendar eDate)
    {
        this();

        // dayValues.add(DataAccess.getInstance().getDayStatsRange(sDate,
        // eDate));

        WeeklyValues wv = DataAccess.getInstance().getDayStatsRange(sDate, eDate);
        Hashtable<String, DailyValues> table = wv.getAllValues();

        // System.out.println("GlucoValues: " + table.size());

        // System.out.println("Start: " +
        // dataAccess.getDateTimeStringFromGregorianCalendar(sDate,2));
        // System.out.println("End: " +
        // dataAccess.getDateTimeStringFromGregorianCalendar(eDate,2));

        for (Enumeration<String> en = table.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            addDayValues(table.get(key));
        }

        /*
         * GregorianCalendar gC = new GregorianCalendar(); gC.setTime(sDate);
         * while (gC.getTime().compareTo(eDate) <= 0) { DailyValues dv =
         * dbH.getDayStats(gC.getTime()); //recordCount += dv.getRowCount();
         * dayValues.add(dv); gC.add(Calendar.DATE, 1); }
         */
    }


    /**
     * Constructor
     * 
     * @param sDate
     * @param eDate
     * @param graph
     */
    public GlucoValues(GregorianCalendar sDate, GregorianCalendar eDate, boolean graph)
    {
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE, 0);
        sDate.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
        from_date = sDate;

        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
        to_date = eDate;

        // System.out.println("Range: " + from_date + " - " + to_date);

        dayValuesRows = m_da.getDb().getDayValuesRange(sDate, eDate);

        fillDailyValues();
    }


    public GlucoValues(GregorianCalendar sDate, GregorianCalendar eDate, Collection<DailyValuesRow> collection)
    {
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE, 0);
        sDate.set(Calendar.SECOND, 0);
        sDate.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
        from_date = sDate;

        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 0);
        eDate.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
        to_date = eDate;

        for (DailyValuesRow dailyValuesRow : collection)
        {
            addRow(dailyValuesRow);
        }
    }


    /**
     * Get Range From
     * 
     * @return
     */
    public GregorianCalendar getRangeFrom()
    {
        return from_date;
    }


    /**
     * Get Range To
     * 
     * @return
     */
    public GregorianCalendar getRangeTo()
    {
        return to_date;
    }


    private void addDayValues(DailyValues dv)
    {
        // System.out.println("DailyValues: " + dv);

        for (int i = 0; i < dv.getRowCount(); i++)
        {
            addRow(dv.getRow(i));
        }

    }


    private void fillDailyValues()
    {
        dayValues = new Vector<DailyValues>();

        // dayValuesRows
        for (int i = 0; i < this.dayValuesRows.size(); i++)
        {
            addRow(this.dayValuesRows.get(i), false);
        }

    }


    /**
     * Add Row
     * 
     * @param dRow 
     */
    public void addRow(DailyValuesRow dRow)
    {
        addRow(dRow, true);
    }


    /**
     * Add Row
     * 
     * @param dRow 
     * @param external 
     */
    public void addRow(DailyValuesRow dRow, boolean external)
    {
        //
        if (external)
        {
            dayValuesRows.add(dRow);
        }

        String s1 = dRow.getDateAsString();

        for (int i = 0; i < dayValues.size(); i++)
        {
            DailyValues dV = dayValues.elementAt(i);
            // System.out.println("date1:" + s1 + "");
            // System.out.println("date2:" + dV.getDateAsString() + "");
            if (s1.equals(dV.getDateAsString()))
            {
                dV.addRow(dRow);

                // X GlucoValueEvent event = new GlucoValueEvent(this, i, i, 0,
                // GlucoValueEvent.INSERT);
                // X fireGlucoValueChanged(event);
                return;
            }
        }

        if (dRow.getDateTime() != 0)
        {
            DailyValues dV = new DailyValues();

            dV.addRow(dRow);
            dV.setDate(dRow.getDateTime());
            // dV.setIsNew(true);
            dayValues.add(dV);

            // X GlucoValueEvent event = new GlucoValueEvent(this,
            // dayValues.size(), dayValues.size(), 0, GlucoValueEvent.INSERT);
            // X fireGlucoValueChanged(event);
        }
    }

    /**
     * Set New Row
     * 
     * @see ggc.core.data.DailyValues#setNewRow(ggc.core.data.DailyValuesRow)
     */
    /*
     * public void setNewRow(DailyValuesRow dRow)
     * {
     * if (!dataAccess.getDb().dateTimeExists(dRow.getDateTime()))
     * {
     * addRow(dRow);
     * }
     * }
     */


    /**
     * Save Values
     */
    /*
     * public void saveValues()
     * {
     * for (int i = 0; i < dayValues.size(); i++)
     * dayValues.elementAt(i).saveDay();
     * }
     */

    /**
     * Get Daily Values Row Count
     * 
     * @return
     */
    public int getDailyValuesRowsCount()
    {
        return this.dayValuesRows.size();
    }


    /**
     * Get Daily Values Row 
     * 
     * @param index 
     * @return
     */
    public DailyValuesRow getDailyValueRow(int index)
    {
        return this.dayValuesRows.get(index);
    }


    /**
     * Get Row Count
     * @return 
     * 
     * @see ggc.core.data.DailyValues#getRowCount()
     */
    public int getRowCount()
    {
        int c = 0;
        for (int i = 0; i < dayValues.size(); i++)
        {
            c += dayValues.elementAt(i).getRowCount();
        }
        return c;
    }


    /**
     * Delete Row
     * @param row 
     * 
     * @see ggc.core.data.DailyValues#deleteRow(int)
     */
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

                    // X GlucoValueEvent event = new GlucoValueEvent(this, row,
                    // row, 0, GlucoValueEvent.DELETE);
                    // X fireGlucoValueChanged(event);
                    return;
                }
            }
        }
    }


    /**
     * Get Value At
     * @param row 
     * @param column 
     * @return 
     * 
     * @see ggc.core.data.DailyValues#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        int c = 0;
        Object o = null;
        for (int i = 0; i < dayValues.size(); i++)
        {
            int old = c;
            DailyValues dV = dayValues.elementAt(i);
            c += dV.getRowCount();
            if (old <= row && row < c)
            {
                o = dV.getValueAt(row - old, column);
            }
        }
        return o;
    }


    /**
     * Get Daily Values For Day
     * 
     * @param day
     * @return
     * 
     * @deprecated Use getDailyValuesItem() instead
     */
    @Deprecated
    public DailyValues getDailyValuesForDay(int day)
    {
        return dayValues.elementAt(day);
    }


    /**
     * Set Value At
     * 
     * @see ggc.core.data.DailyValues#setValueAt(java.lang.Object, int, int)
     */
    /*
     * public void setValueAt(Object aValue, int row, int column)
     * {
     * int c = 0;
     * for (int i = 0; i < dayValues.size(); i++)
     * {
     * int old = c;
     * DailyValues dV = dayValues.elementAt(i);
     * c += dV.getRowCount();
     * if ((old <= row) && (row < c))
     * dV.setValueAt(aValue, row - old, column);
     * }
     * // X GlucoValueEvent event = new GlucoValueEvent(this, row, row, column,
     * // GlucoValueEvent.UPDATE);
     * // X fireGlucoValueChanged(event);
     * }
     */

    /**
     * Get Daily Values Item (one Day)
     * 
     * @param index
     * @return
     */
    public DailyValues getDailyValuesItem(int index)
    {
        return dayValues.elementAt(index);
    }


    /**
     * Get Daily Values Items Count
     * 
     * @return
     */
    public int getDailyValuesItemsCount()
    {
        return dayValues.size();
    }


    /**
     * @return The amount of days physically present in this
     *         <code>{@link GlucoValues}</code>.
     *         
     * @deprecated use getDailyValuesItemCount() instead
     */
    @Deprecated
    public int getDayCount()
    {
        return dayValues.size();
    }


    /**
     * @return The amount of days between the first and the last entry contained
     *         in this <code>{@link GlucoValues}</code>.
     */
    public long getDaySpan()
    {
        long firstDay = Long.MAX_VALUE, lastDay = Long.MIN_VALUE, currentDay, dayCount;

        for (DailyValues dayValue : dayValues)
        {
            currentDay = dayValue.getDate();
            if (currentDay > lastDay)
            {
                lastDay = currentDay;
            }
            if (currentDay < firstDay)
            {
                firstDay = currentDay;
            }
        }

        if (firstDay == Long.MAX_VALUE)
            return 0L;
        if (firstDay == lastDay)
            return 1L;

        // firstDay = dataAccess.getDateTimeAsDateObject(firstDay *
        // 10000).getTime();
        // lastDay = dataAccess.getDateTimeAsDateObject(lastDay *
        // 10000).getTime();

        // FIXME This is experimental, untested
        firstDay = ATechDate.getGregorianCalendar(ATechDate.FORMAT_DATE_ONLY, firstDay).getTimeInMillis(); // getTime()).getTime();
        lastDay = ATechDate.getGregorianCalendar(ATechDate.FORMAT_DATE_ONLY, lastDay).getTimeInMillis();

        long timeDiff = lastDay - firstDay;
        dayCount = timeDiff / (1000 * 60 * 60 * 24);

        // round up if neccessary
        if (dayCount * (1000 * 60 * 60 * 24) != timeDiff)
        {
            dayCount++;
        }

        // add 1 as the first day needs to be counted, too
        dayCount++;

        return dayCount;
    }


    /**
     * Get Date For Day At
     * 
     * @param i
     * @return
     */
    public String getDateForDayAt(int i)
    {
        // if the caller requests a key we can't supply, return null (not an
        // exception)
        if (dayValues.size() <= i)
            return null;
        else
            return dayValues.elementAt(i).getDayAndMonthAsString();
    }

    /*
     * public void addGlucoValueEventListener(GlucoValueEventListener listener)
     * { listenerList.add(GlucoValueEventListener.class, listener); }
     * public void removeGlucoValueEventListener(GlucoValueEventListener
     * listener) { listenerList.remove(GlucoValueEventListener.class, listener);
     * }
     * protected void fireGlucoValueChanged(GlucoValueEvent event) { //
     * Guaranteed to return a non-null array Object[] listeners =
     * listenerList.getListenerList();
     * // Process the listeners last to first, notifying // those that are
     * interested in this event for (int i = listeners.length - 2; i >= 0; i -=
     * 2) { if (listeners[i] instanceof GlucoValueEventListener) { // Lazily
     * create the event: if (event != null)
     * ((GlucoValueEventListener)listeners[i + 1]).glucoValuesChanged(event); }
     * } }
     */
}
