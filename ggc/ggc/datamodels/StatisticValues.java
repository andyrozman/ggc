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
 *  Filename: <filename>
 *  Purpose:  <enter purpose here> 
 *
 *  Author:   <enter your name here>
 */

package ggc.datamodels;


import ggc.db.DataBaseHandler;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;


public class StatisticValues
{
    DataBaseHandler dbH = null;

    float dayCounter = 0;


    float sumBG = 0;
    float sumBU = 0;
    float sumIns1 = 0;
    float sumIns2 = 0;

    int countBG = 0;
    int countBU = 0;
    int countIns1 = 0;
    int countIns2 = 0;

    public StatisticValues()
    {
        dbH = DataBaseHandler.getInstance();
    }

    public StatisticValues(Date sDay, Date eDay)
    {
        this();
        setDayRange(sDay,eDay);
    }

    public void setDayRange(Date sDay, Date eDay)
    {

	dbH.getDayStatsRange(sDay,eDay);
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
        sumBU += dv.getSumBE();
        sumIns1 += dv.getSumIns1();
        sumIns2 += dv.getSumIns2();

        countBG += dv.getBGCount();
        countBU += dv.getBECount();
        countIns1 += dv.getIns1Count();
        countIns2 += dv.getIns2Count();
    }

    public float getCountBG()
    {
        return countBG;
    }

    public float getAvgBG()
    {
        if(dayCounter > 0)
            return sumBG / countBG;
        else
            return 0;
    }

    public float getSumBU()
    {
        return sumBU;
    }

    public float getCountBU()
    {
        return countBU;
    }

    public float getAvgBUPerDay()
    {
        if(dayCounter > 0)
            return sumBU / dayCounter;
        else
            return 0;
    }

    public float getAvgBU()
    {
        if(countBU > 0)
            return sumBU / countBU;
        else
            return 0;
    }

    public float getBUCountPerDay()
    {
        if(dayCounter > 0)
            return countBU / dayCounter;
        else
            return 0;
    }

    public float getSumIns1()
    {
        return sumIns1;
    }

    public float getCountIns1()
    {
        return countIns1;
    }

    public float getAvgIns1PerDay()
    {
        if(dayCounter > 0)
            return sumIns1 / dayCounter;
        else
            return 0;
    }

    public float getAvgIns1()
    {
        if(countIns1 > 0)
            return sumIns1 / countIns1;
        else
            return 0;
    }

    public float getIns1CountPerDay()
    {
        if(dayCounter > 0)
            return countIns1 / dayCounter;
        else
            return 0;
    }

    public float getSumIns2()
    {
        return sumIns2;
    }

    public float getCountIns2()
    {
        return countIns2;
    }

    public float getAvgIns2PerDay()
    {
        if(dayCounter > 0)
            return sumIns2 / dayCounter;
        else
            return 0;
    }

    public float getAvgIns2()
    {
        if(countIns2 > 0)
            return sumIns1 / countIns2;
        else
            return 0;
    }

    public float getIns2CountPerDay()
    {
        if(dayCounter > 0)
            return countIns2 / dayCounter;
        else
            return 0;
    }
}
