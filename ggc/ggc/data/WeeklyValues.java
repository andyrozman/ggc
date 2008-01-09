/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package ggc.data;


import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
import java.util.Hashtable;
//import java.util.Vector;

//import ggc.db.DataBaseHandler;
import ggc.util.I18nControl;
import ggc.util.DataAccess;

import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;


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

    
    public Hashtable getAllValues()
    {
        return this.m_dataTable;
    }

}
