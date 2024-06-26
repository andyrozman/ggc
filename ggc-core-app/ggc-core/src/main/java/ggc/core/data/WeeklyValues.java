package ggc.core.data;

import java.io.Serializable;
import java.util.Hashtable;

import com.atech.utils.ATDataAccessAbstract;

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
 *  Filename:     WeeklyValues  
 *  Description:  Weekly Values, used mainly for printing
 * 
 *  Author: Andy {andy@atech-software.com}  
 */

public class WeeklyValues implements Serializable
{

    private static final long serialVersionUID = 7748966026356288165L;
    private Hashtable<String, DailyValues> m_dataTable = null;


    // private DataAccess dataAccess = DataAccess.getInstance();

    /**
     * Constructor
     */
    public WeeklyValues()
    {
        m_dataTable = new Hashtable<String, DailyValues>();
    }


    /**
     * Add Day
     * 
     * @param dv
     */
    public void addDay(DailyValues dv)
    {
        // System.out.println(dv.getDate());
        m_dataTable.put("" + dv.getDate(), dv);
    }


    /**
     * Add Day Value Row
     * 
     * @param dvr
     */
    public void addDayValueRow(DailyValuesRow dvr)
    {
        int date = (int) dvr.getDate();
        // System.out.println(date);

        if (m_dataTable.containsKey("" + date))
        {
            DailyValues dv_int = m_dataTable.get("" + date);
            dv_int.addRow(dvr);
        }
        else
        {
            DailyValues dv = new DailyValues();
            dv.setDate(dvr.getDateTime());
            dv.addRow(dvr);
            m_dataTable.put("" + date, dv);
        }
    }


    /**
     * Get Day Values
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public DailyValues getDayValues(int year, int month, int day)
    {
        String days = "" + year + ATDataAccessAbstract.getLeadingZero(month, 2)
                + ATDataAccessAbstract.getLeadingZero(day, 2);
        if (!m_dataTable.containsKey(days))
            return null;
        else
        {
            DailyValues dv = m_dataTable.get(days);
            return dv;
        }
    }


    /**
     * Get All Values (Hashtable)
     * 
     * @return
     */
    public Hashtable<String, DailyValues> getAllValues()
    {
        return this.m_dataTable;
    }

}
