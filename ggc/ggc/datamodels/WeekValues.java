/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package ggc.datamodels;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import ggc.db.DataBaseHandler;
import ggc.util.I18nControl;

import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;


public class WeekValues implements Serializable
{
    private I18nControl m_ic = I18nControl.getInstance();
    private Hashtable m_dataTable = null;


    public WeekValues()
    {
        m_dataTable = new Hashtable();
    }

    public void addDay(DailyValues dv)
    {
        m_dataTable.put(dv.getDateAsString(), dv);
    }

    public void addDayValueRow(DailyValuesRow dvr)
    {
        
        if (m_dataTable.contains(dvr.getDateAsString()))
        {
            DailyValues dv_int = (DailyValues)m_dataTable.get(dvr.getDateAsString());
            dv_int.setNewRow(dvr);
        }
        else
        {
            DailyValues dv = new DailyValues();
            dv.setNewRow(dvr);
            dv.setDate(dvr.getDateTime());
            m_dataTable.put(dv.getDateAsString(), dv);
        }
    }

    public Hashtable getAllValues()
    {
        return this.m_dataTable;
    }

}
