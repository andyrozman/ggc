package ggc.plugin.report.data.cgms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMValuesEntry.java
 *  Description:  Collection of CGMValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDayDataEntry implements Comparable<CGMSDayDataEntry>
{

    private static final Log log = LogFactory.getLog(CGMSDayDataEntry.class);

    /**
     * DateTime
     */
    public long datetime = 0L;

    /**
     * Date
     */
    public int date = 0;

    /**
     * Time
     */
    public int time = 0;

    /**
     * Value 
     */
    public int value = 0;

    /**
     * Type 
     */
    private int type = 0;

    // NEW
    /**
     * 
     */
    public int sub_type = 0;

    /**
     * Time only 
     */
    public boolean time_only = false;


    /**
     * Constructor
     */
    public CGMSDayDataEntry()
    {
    }


    /**
     * Constructor
     * 
     * @param entry
     * @param type
     */
    public CGMSDayDataEntry(String entry, int type)
    {
        // 115329=147
        this.time = Integer.parseInt(entry.substring(0, entry.indexOf("=")));
        this.value = Integer.parseInt(entry.substring(entry.indexOf("=") + 1));
        this.setType(type);
        this.datetime = time * 10 + type;
    }


    /**
     * Get Sub Entry Value
     * 
     * @return
     */
    public String getSubEntryValue()
    {
        // return getColumnValue(0) + "=" + this.value;
        return this.time + "=" + this.value;
    }


    /**
     * Set Date Time
     * 
     * @param dt
     */
    public void setDateTime(long dt)
    {
        this.datetime = dt;
        date = (int) (dt / 1000000);
        time = (int) (dt - date * 1000000);

        // System.out.println("Date: " + date + ", Time: " + time);
    }


    @Override
    public String toString()
    {
        return datetime + " = " + value;
    }


    public long getDateTime()
    {
        return this.datetime;
    }


    public int getDateTimeFormat()
    {
        return ATechDate.FORMAT_DATE_AND_TIME_S;
    }


    public ATechDate getDateTimeObject()
    {
        return null;
    }


    public void setDateTimeObject(ATechDate dt)
    {
        this.setDateTime(dt.getATDateTimeAsLong());
    }


    public String getValue()
    {
        return "" + this.value;
    }


    public void setId(long idIn)
    {
    }


    public int getType()
    {
        return type;
    }


    public void setType(int type)
    {
        this.type = type;
    }


    public int compare(CGMSDayDataEntry d1, CGMSDayDataEntry d2)
    {
        return d1.time - d2.time;
    }


    public int compareTo(CGMSDayDataEntry d2)
    {
        return this.time - d2.time;
    }

}
