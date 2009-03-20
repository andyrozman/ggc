package ggc.core.data;

import ggc.core.util.DataAccess;

import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

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
 *  Filename:     StatisticValues  
 *  Description:  Data class for storing statistic data
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */


public class StatisticValues
{
    DataAccess m_da = DataAccess.getInstance();

    float dayCounter = 0;


    float sumBG = 0;
    float sumBU = 0;
    float sumIns1 = 0;
    float sumIns2 = 0;

    int countBG = 0;
    int countBU = 0;
    int countIns1 = 0;
    int countIns2 = 0;

    
    /**
     * Constructor
     */
    public StatisticValues()
    {
    }


    /**
     * Constructor
     * 
     * @param sDay
     * @param eDay
     */
    public StatisticValues(GregorianCalendar sDay, GregorianCalendar eDay)
    {
        this();
        setDayRange(sDay,eDay);
    }

    /**
     * Set Day Range
     * 
     * @param sDay
     * @param eDay
     */
    public void setDayRange(GregorianCalendar sDay, GregorianCalendar eDay)
    {
        WeeklyValues wv = DataAccess.getInstance().getDayStatsRange(sDay, eDay);
        Hashtable<String, DailyValues> table = wv.getAllValues();

        for (Enumeration<String> en = table.keys(); en.hasMoreElements(); ) 
        {
            String key = en.nextElement();
            addDayValues((DailyValues)table.get(key));
        }

        //addDayValues(DataAccess.getInstance().getDayStatsRange(sDay, eDay));

	//dbH.getDayStatsRange(sDay,eDay);
        /*GregorianCalendar gC = new GregorianCalendar();
        gC.setTime(sDay);
        while (gC.getTime().compareTo(eDay) <= 0) {
            DailyValues dv = dbH.getDayStats(gC.getTime());
            addDayValues(dv);
            gC.add(Calendar.DATE, 1);
        }*/
    }

    private void addDayValues(DailyValues dv)
    {
        //System.out.println("DailyValues: " + dv);

        if (dv==null)
            return;

	
        dayCounter++;

        sumBG += dv.getSumBG();
        sumBU += dv.getSumCH();
        sumIns1 += dv.getSumIns1();
        sumIns2 += dv.getSumIns2();

        countBG += dv.getBGCount();
        countBU += dv.getCHCount();
        countIns1 += dv.getIns1Count();
        countIns2 += dv.getIns2Count();
/*
	System.out.println("dv=" + dv.getDateAsString() +
			   "sumBG=" + sumBG + 
			   "sumIns1=" + sumIns1 +
			   "sumIns2=" + sumIns2 +
			   "countBG=" + countBG + 
			   "countIns1=" + countIns1 +
			   "countIns2=" + countIns2); */

    }

    /**
     * Get Count BG
     * 
     * @return
     */
    public int getCountBG()
    {
        return countBG;
    }

    /**
     * Get Avg BG
     * 
     * @return
     */
    public float getAvgBG()
    {
        
        if (countBG != 0)
        {
            if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
                return sumBG / countBG;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, (sumBG / countBG));
        }
        else
            return 0;
        
        
        /*
        if(countBG > 0)
            return sumBG / countBG;
        else
            return 0;*/
    }

    /**
     * Get Sum CH/BU
     * 
     * @return
     */
    public float getSumBU()
    {
        return sumBU;
    }

    /**
     * Get Count CH/BU
     * 
     * @return
     */
    public int getCountBU()
    {
        return countBU;
    }

    /**
     * Get Avg CH(BU)/Day
     * 
     * @return
     */
    public float getAvgBUPerDay()
    {
        if(dayCounter > 0)
            return sumBU / dayCounter;
        else
            return 0;
    }

    /**
     * Get Avg CH/BU
     * 
     * @return
     */
    public float getAvgBU()
    {
        if(countBU > 0)
            return sumBU / countBU;
        else
            return 0;
    }

    /**
     * Get  CH/BU Per Day Count
     * 
     * @return
     */
    public float getBUCountPerDay()
    {
        if(dayCounter > 0)
            return countBU / dayCounter;
        else
            return 0;
    }

    /**
     * Get Sum Ins 1
     * 
     * @return
     */
    public float getSumIns1()
    {
        return sumIns1;
    }

    /**
     * Get Count Ins 1
     * 
     * @return
     */
    public int getCountIns1()
    {
        return countIns1;
    }

    /**
     * Get Avg Ins1 / Day
     * 
     * @return
     */
    public float getAvgIns1PerDay()
    {
        if(dayCounter > 0)
            return sumIns1 / dayCounter;
        else
            return 0;
    }

    /**
     * Get Avg Ins1 
     * 
     * @return
     */
    public float getAvgIns1()
    {
        if(countIns1 > 0)
            return sumIns1 / countIns1;
        else
            return 0;
    }

    /**
     * Get Ins 1 Count per Day
     * 
     * @return
     */
    public float getIns1CountPerDay()
    {
        if(dayCounter > 0)
            return countIns1 / dayCounter;
        else
            return 0;
    }

    /**
     * Get Sum Ins 2
     * 
     * @return
     */
    public float getSumIns2()
    {
        return sumIns2;
    }

    /**
     * Get Count Ins 2
     * 
     * @return
     */
    public int getCountIns2()
    {
        return countIns2;
    }

    /**
     * Get Avg Ins2 / Day
     * 
     * @return
     */
    public float getAvgIns2PerDay()
    {
        if(dayCounter > 0)
            return sumIns2 / dayCounter;
        else
            return 0;
    }


    /**
     * Get Avg Ins2 
     * 
     * @return
     */
    public float getAvgIns2()
    {
        if(countIns2 > 0)
            return sumIns2 / countIns2;
        else
            return 0;
    }

    /**
     * Get Ins 2 Count per Day
     * 
     * @return
     */
    public float getIns2CountPerDay()
    {
        if(dayCounter > 0)
            return countIns2 / dayCounter;
        else
            return 0;
    }
}