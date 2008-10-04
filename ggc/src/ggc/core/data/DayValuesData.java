package ggc.core.data;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
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
 *  Filename: DayValuesData
 *  Purpose:  This is data class for storing all data from DayValueH type. This will be new class, which
 *            will with time replace all (or most) other data classes, or at least be super class of this
 *            classes. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.core.util.DataAccess;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import com.atech.utils.ATechDate;


public class DayValuesData implements Iterable<DailyValues>, Iterator<DailyValues>
{

    private Hashtable<String, DailyValues> m_dataTable = null;
    public DataAccess m_da = DataAccess.getInstance();
    
    private long range_from = 0L;
    private GregorianCalendar range_from_gc = null;
    private long range_to = 0L;
    private GregorianCalendar range_to_gc = null;
    
    public static final int RANGE_NONE = 0;
    public static final int RANGE_DAY = 1;
    public static final int RANGE_WEEK = 2;
    public static final int RANGE_MONTH = 3;
    public static final int RANGE_3_MONTHS = 4;

    public static final int RANGE_CUSTOM = 5;
    
    
    private int type = DayValuesData.RANGE_NONE;


    public DayValuesData(long from, long till)
    {
        m_dataTable = new Hashtable<String, DailyValues>();
        this.type = DayValuesData.RANGE_CUSTOM;
        
        
        
        this.setRangeFrom(from);
        this.setRangeTo(till);
    }

    // TODO: more constructors
    
    public void addDay(DailyValues dv)
    {
        // TODO: check range
        m_dataTable.put(""+dv.getDate(), dv);
    }

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
            dv.addRow(dvr);
            m_dataTable.put(""+date, dv);
        }
    }

    public DailyValues getDailyValuesObject(int year, int month, int day)
    {
        String days = ""+year + m_da.getLeadingZero(month, 2) + m_da.getLeadingZero(day, 2);
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

    
    public void setRangeFrom(long from)
    {
        // TODO extend to several input formats
        this.range_from = from;
        range_from_gc = new ATechDate(ATechDate.FORMAT_DATE_ONLY, from).getGregorianCalendar();
    }
    

    public void setRangeTo(long to)
    {
        // TODO extend to several input formats
        this.range_to = to;
        range_to_gc = new ATechDate(ATechDate.FORMAT_DATE_ONLY, to).getGregorianCalendar();
    }
    
    
    public String getFromAsLocalizedDate()
    {
//        return "xx";
        return getAsLocalizedString(this.range_from_gc);
    }

    public String getToAsLocalizedDate()
    {
//        return "xx";
        return getAsLocalizedString(this.range_to_gc);
    }
    
    
    public static final int DATE_MONTH = 0;
    public static final int DATE_YEAR = 0;
    
    
    private String getAsLocalizedString(GregorianCalendar gc_value)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, this.m_da.getI18nControlInstance().getSelectedLanguageLocale());
        
        return df.format(gc_value.getTime());
    }
    
    
    
    public Hashtable<String, DailyValues> getAllValues()
    {
        return this.m_dataTable;
    }

    public int getType()
    {
        return this.type;
    }

    public long getRangeBegin()
    {
        return this.range_from;
    }
    

    public long getRangeEnd()
    {
        return this.range_to;
    }

    
    public ATechDate getRangeBeginObject()
    {
        return new ATechDate(this.range_from);
    }
    

    public ATechDate getRangeEndObject()
    {
        return new ATechDate(this.range_to);
    }

    public Iterator<DailyValues> iterator()
    {
        System.out.println("dasta: " + this.m_dataTable);
        return this;
    }

    private long current_element = 0L;
    
    public boolean hasNext()
    {
        
        //System.out.println("dt siye: " + this.m_dataTable.size());
        
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
    
    public DailyValues next()
    {
        return findNext(true);
    }

    public void remove()
    {
    }
    
    
    
    public int size()
    {
        return this.m_dataTable.size();
    }
    
    
    
}
