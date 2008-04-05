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
 *  Filename: WeeklyValues
 *  Purpose:  This is data class for storing many instances of DailyValues for whole week. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.core.util.DataAccess;

import java.io.Serializable;
import java.util.Hashtable;


public class WeeklyValues implements Serializable
{
    //private I18nControl m_ic = I18nControl.getInstance();
    private Hashtable<String, DailyValues> m_dataTable = null;
    public DataAccess m_da = DataAccess.getInstance();


    public WeeklyValues()
    {
        m_dataTable = new Hashtable<String, DailyValues>();
    }

    public void addDay(DailyValues dv)
    {
        //System.out.println(dv.getDate());
        m_dataTable.put(""+dv.getDate(), dv);
    }

    public void addDayValueRow(DailyValuesRow dvr)
    {
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

    public DailyValues getDayValues(int year, int month, int day)
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

    
    public Hashtable<String, DailyValues> getAllValues()
    {
        return this.m_dataTable;
    }

}
