package ggc.plugin.report.data.cgms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import ggc.core.db.hibernate.cgms.CGMSDataH;

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
 *  Filename:     CGMSValuesEntry
 *  Description:  Collection of CGMSValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDayData
{

    private static Log log = LogFactory.getLog(CGMSDayData.class);

    long id;

    long datetime;
    long date;
    ATechDate date_obj = null;

    int type;

    boolean empty = true;

    private Hashtable<String, String> params;
    ArrayList<CGMSDayDataEntry> list = null;
    String extended = "";
    int person_id = 0;
    String item_data = null;


    /**
     * Constructor
     */
    public CGMSDayData()
    {
        super();
        list = new ArrayList<CGMSDayDataEntry>();
    }


    /**
     * Constructor
     * 
     * @param pdh 
     */
    public CGMSDayData(CGMSDataH pdh)
    {
        super();
        list = new ArrayList<CGMSDayDataEntry>();
        this.id = pdh.getId();
        this.datetime = pdh.getDt_info();
        this.type = pdh.getBase_type();
        loadExtended(pdh.getExtended());
        this.person_id = pdh.getPerson_id();
    }


    /**
     * Set Date 
     * 
     * @param dt
     */
    public void setDate(long dt)
    {
        this.date = dt;
        this.date_obj = new ATechDate(ATechDate.FORMAT_DATE_ONLY, dt);
        this.datetime = dt;
    }


    /**
     * Get DateTime (long)
     * 
     * @return
     */
    public long getDateTime()
    {
        return this.datetime;
    }


    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDateType.DateOnly, this.datetime);
    }


    /**
     * Get Sub Entry List
     * 
     * @return
     */
    public ArrayList<CGMSDayDataEntry> getSubEntryList()
    {
        return this.list;
    }


    /**
     * Set Empty
     * 
     * @param empty_
     */
    public void setEmpty(boolean empty_)
    {
        this.empty = empty_;
    }


    /**
     * Is Empty
     * 
     * @return
     */
    public boolean isEmpty()
    {
        return this.empty;
    }


    /**
     * Set Type
     *
     * @param type_
     */
    public void setType(int type_)
    {
        this.type = type_;
    }


    /**
     * Get Type
     *
     * @return
     */
    public int getType()
    {
        return this.type;
    }


    /**
     * To String
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "CGMSValuesEntry [date/time=" + this.datetime + ",readings=" + this.list.size() + "type=" + type + "]";
    }


    /**
     * Get DateTime format
     * 
     * @return format of date time (precission)
     */

    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeSec;
    }


    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt.getATDateTimeAsLong();
    }


    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "CD_" + this.datetime + "_" + this.type;
    }


    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "CGMSValuesEntry";
    }


    /**
     * Get Value of object
     * 
     * @return
     */
    public String getValue()
    {
        return this.item_data;
    }


    private void loadExtended(String entry)
    {
        this.extended = entry;

        StringTokenizer strtok = new StringTokenizer(entry, "#$#");

        while (strtok.hasMoreTokens())
        {
            String tok = strtok.nextToken();

            if (tok.startsWith("DATA="))
            {
                tok = tok.substring(5);

                this.item_data = tok;

                StringTokenizer strtok2 = new StringTokenizer(tok, ";");

                while (strtok2.hasMoreTokens())
                {
                    CGMSDayDataEntry cvse = new CGMSDayDataEntry(strtok2.nextToken(), this.type);
                    this.list.add(cvse);
                }

                // System.out.println("tok data: " + tok);
            }
            else if (tok.startsWith("SOURCE="))
            {
                tok = tok.substring(7);
                // System.out.println("tok src: " + tok);
            }
            else
            {
                log.warn("Unknown token with extended data: " + tok);
            }

        }

    }


    public void sortSubs()
    {
        Collections.sort(this.list);
    }


    public boolean equals(Object element)
    {
        if (element instanceof CGMSDayData)
        {
            CGMSDayData other = (CGMSDayData) element;

            return ((this.datetime == other.datetime) && (this.type == other.type));
        }
        else
            return false;
    }
}
