package ggc.core.data;

import ggc.core.util.DataAccess;

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import com.atech.utils.data.ATechDate;

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
 *  Filename:      DayValuesData  
 *  Descriptione:  This is data class for storing all data from DayValueH type. This will 
 *            be new class, which will with time replace all (or most) other data classes, 
 *            or at least be super class of this classes. 
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */


public class DayValuesData implements Iterable<DailyValues>, Iterator<DailyValues>
{

    private Hashtable<String, DailyValues> m_dataTable = null;
    private DataAccess m_da = DataAccess.getInstance();
    
    private long range_from = 0L;
    private GregorianCalendar range_from_gc = null;
    private long range_to = 0L;
    private GregorianCalendar range_to_gc = null;
    
    /**
     * Range: None
     */
    public static final int RANGE_NONE = 0;
    
    /**
     * Range: Day
     */
    public static final int RANGE_DAY = 1;
    
    /**
     * Range: Week
     */
    public static final int RANGE_WEEK = 2;
    
    /**
     * Range: Month
     */
    public static final int RANGE_MONTH = 3;
    
    /**
     * Range: 3 Months
     */
    public static final int RANGE_3_MONTHS = 4;

    /**
     * Range: Custom
     */
    public static final int RANGE_CUSTOM = 5;
    
    
    private int type = DayValuesData.RANGE_NONE;


    /**
     * Constructor
     * 
     * @param from
     * @param till
     */
    public DayValuesData(long from, long till)
    {
        m_dataTable = new Hashtable<String, DailyValues>();
        this.type = DayValuesData.RANGE_CUSTOM;
        
        this.setRangeFrom(from);
        this.setRangeTo(till);
    }

    // TODO: more constructors
    
    /**
     * Add Day
     * 
     * @param dv
     */
    public void addDay(DailyValues dv)
    {
        // TODO: check range
        m_dataTable.put(""+dv.getDate(), dv);
    }

    /**
     * Add Day Value Row
     * 
     * @param dvr
     */
    public void addDayValueRow(DailyValuesRow dvr)
    {
        // TODO: check range
        int date = (int)dvr.getDate();
        //System.out.println(date);

        if (m_dataTable.containsKey(""+date))
        {
            DailyValues dv_int = m_dataTable.get(""+date);
            dv_int.addRow(dvr);
        }
        else
        {
            DailyValues dv = new DailyValues();
            dv.setDate(dvr.getDateTime());
            dv.addRow(dvr);
            m_dataTable.put(""+date, dv);
        }
    }

    /**
     * Get Daily Values Object
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public DailyValues getDailyValuesObject(int year, int month, int day)
    {
        String days = ""+year + DataAccess.getLeadingZero(month, 2) + DataAccess.getLeadingZero(day, 2);
        if (!m_dataTable.containsKey(days))
        {
            return null;
        }
        else
        {
            DailyValues dv = m_dataTable.get(days);
            return dv;
        }
    }

    
    /**
     * Set Range From
     * 
     * @param from
     */
    public void setRangeFrom(long from)
    {
        // TODO extend to several input formats
        this.range_from = from;
        range_from_gc = new ATechDate(ATechDate.FORMAT_DATE_ONLY, from).getGregorianCalendar();
    }
    

    /**
     * Set Range To
     * 
     * @param to
     */
    public void setRangeTo(long to)
    {
        // TODO extend to several input formats
        this.range_to = to;
        range_to_gc = new ATechDate(ATechDate.FORMAT_DATE_ONLY, to).getGregorianCalendar();
    }
    
    
    /**
     * Get From As Localized String
     * 
     * @return
     */
    public String getFromAsLocalizedDate()
    {
        return m_da.getAsLocalizedDateString(this.range_from_gc, 2);
    }

    /**
     * Get To As Localized String
     * 
     * @return
     */
    public String getToAsLocalizedDate()
    {
//        return "xx";
        return m_da.getAsLocalizedDateString(this.range_to_gc, 2);
    }
    
    
    //public static final int DATE_MONTH = 0;
    //public static final int DATE_YEAR = 0;
    
    
    
    /**
     * Get All Values
     * 
     * @return
     */
    public Hashtable<String, DailyValues> getAllValues()
    {
        return this.m_dataTable;
    }

    /**
     * Get Range Type
     * @return
     */
    public int getRangeType()
    {
        return this.type;
    }

    /**
     * Get Range Begin (long)
     * 
     * @return
     */
    public long getRangeBegin()
    {
        return this.range_from;
    }
    

    /**
     * Get Range End (long)
     * 
     * @return
     */
    public long getRangeEnd()
    {
        return this.range_to;
    }

    
    /**
     * Get Range Begin (ATechDate)
     * 
     * @return
     */
    public ATechDate getRangeBeginObject()
    {
        return new ATechDate(this.range_from);
    }
    

    /**
     * Get Range End (ATechDate)
     * 
     * @return
     */
    public ATechDate getRangeEndObject()
    {
        return new ATechDate(this.range_to);
    }

    /**
     * Iterator
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<DailyValues> iterator()
    {
        //System.out.println("dasta: " + this.m_dataTable);
        return this;
    }

    private long current_element = 0L;
    
    /**
     * Has Next
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        
        if (this.m_dataTable.size()==0)
            return false;

        //System.out.println("current: " + this.current_element);
        
        if (this.current_element==this.range_to)  // we came to end
            return false;
        else
        {
            return (findNext(false)!=null);
        }
    }

    
    private DailyValues findNext(boolean iterate)
    {
        //long srch = 0L;
        
        ATechDate atd = null;
        
        if (this.current_element==0)
        {
            atd = new ATechDate(ATechDate.FORMAT_DATE_ONLY, this.range_from);
        }
        else
        {
            atd = new ATechDate(ATechDate.FORMAT_DATE_ONLY, this.current_element);
            atd.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }

        
        //System.out.println("range: " + range_from);
        //System.out.println("el: " +atd.getATDateTimeAsLong());
        
        boolean found = false;
        while (!(found = this.m_dataTable.containsKey("" + atd.getATDateTimeAsLong())))
        {
            if (atd.getATDateTimeAsLong()==this.range_to)
                break;
            else
                atd.add(GregorianCalendar.DAY_OF_MONTH, 1);
        }
                

        if (!found)
            return null;
        else
        {
            if (iterate)
            {
                this.current_element = atd.getATDateTimeAsLong();
            }
            return this.m_dataTable.get("" + atd.getATDateTimeAsLong());
        }
        
    }
    
    /**
     * Next Value
     * @see java.util.Iterator#next()
     */
    public DailyValues next()
    {
        return findNext(true);
    }

    /**
     * Remove (N/A)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
    }
    
    
    
    /**
     * Size of collection
     * 
     * @return
     */
    public int size()
    {
        return this.m_dataTable.size();
    }
    
    
    
}
