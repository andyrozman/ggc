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

package ggc.core.data;


import ggc.core.util.DataAccess;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;



public class RatioValues 
{

    //private EventListenerList listenerList = new EventListenerList();
    private DataAccess m_da = DataAccess.getInstance();

    //private int recordCount = 0;
    Vector<DailyValues> dayValues;
    
    Hashtable<String,DailyValues> data_table = null;
    float average_insulin_usage = 0.0f;
    
    public RatioValues()
    {
	this.data_table = new Hashtable<String,DailyValues>();
    }

    
    public RatioValues(GregorianCalendar sDate, GregorianCalendar eDate)
    {
        this();

        
        //dayValues.add(DataAccess.getInstance().getDayStatsRange(sDate, eDate));


        WeeklyValues wv = DataAccess.getInstance().getDayStatsRange(sDate, eDate);
        Hashtable<String,DailyValues> table = wv.getAllValues();

        for (Enumeration<String> en = table.keys(); en.hasMoreElements(); ) 
        {
            String key = (String)en.nextElement();
            addDayValues((DailyValues)table.get(key));
        }

    }

    
    private void addDayValues(DailyValues dv)
    {
	if (!this.data_table.containsKey(dv.getDateAsString()))
	{
	    this.data_table.put(dv.getDateAsString(), dv);
	}
    }
    

    private void addDayValuesRow(DailyValuesRow dvr)
    {
	if (this.data_table.containsKey(dvr.getDateAsString()))
	{
	    this.data_table.get(dvr.getDateAsString()).addRow(dvr);
	}
	else
	{
	    DailyValues dv = new DailyValues();
	    dv.addRow(dvr);
	    
	    this.data_table.put(dvr.getDateAsString(), dv);
	}
    }


    public float getAverageInsulinUsage()
    {
	if (average_insulin_usage == 0.0f)
	    this.calculateAvergeInsulinUsage();
	
	return this.average_insulin_usage;
    }
    
    
    private void calculateAvergeInsulinUsage()
    {
	float sum = 0.0f;
	
	for(Enumeration<String> en = this.data_table.keys(); en.hasMoreElements(); )
	{
	    sum += this.data_table.get(en.nextElement()).getSumIns();
	}

	this.average_insulin_usage = sum / this.data_table.size();
    }
    

    public float getInsulinCarb_Ratio()
    {
	return this.getInsulinCarb_Ratio(RatioValues.INS_CARB_RATIO_RULE_500);
    }
    
    public static final int INS_CARB_RATIO_RULE_500 = 500;
    public static final int INS_CARB_RATIO_RULE_450 = 450;
    public static final int INS_CARB_RATIO_RULE_300 = 300;
    
    public static final int SENSITIVITY_FACTOR_RULE_1800 = 1800;
    public static final int SENSITIVITY_FACTOR_RULE_1500 = 1500;
    
    public static final int SENSITIVITY_FACTOR_RULE_100 = 100;
    public static final int SENSITIVITY_FACTOR_RULE_83 = 83;
    

    
    public float getInsulinCarb_Ratio(int rule)
    {
	return (rule * (1.0f)) / this.getAverageInsulinUsage();
    }

    
    public float getSensitivityFactor(int rule)
    {
	if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
	{
	    
	}
	return 0.0f;
    }
    
    
    public float getSensitivityFactorMgDL(int rule)
    {
	int rule_all = rule;
	
	if (rule==100)
	{
	    rule = 1800;
	}
	else if (rule==83)
	{
	    rule = 1500;
	}
	
	return rule * (1.0f) / this.getAverageInsulinUsage();
    }
    
    
    public float getSensitivityFactorMmolL(int rule)
    {
	float rule_all = rule;
	
	if (rule==1800)
	{
	    rule_all = 100f;
	}
	else if (rule==1500)
	{
	    rule_all = 83.33f;
	}
	
	return rule_all / this.getAverageInsulinUsage();
    }
    
    

}
