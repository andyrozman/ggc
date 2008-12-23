package ggc.core.data;

/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: HbA1cValues.java 
 * 
 * Purpose: Calculates the HbA1c based on a given date.
 * 
 * Author: schultd
 * Author: andyrozman {andy@atech-software.com}
 * 
 */

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.util.Enumeration;
import java.util.Hashtable;

public class HbA1cValues
{

    private I18nControl m_ic = I18nControl.getInstance();
    DataAccess m_da = DataAccess.getInstance();

    private float sumBG;
    private int readings;
    private int dayCount;
    private int exp;
    private int[] ReadingsPerDay;

    private Hashtable<String, DailyValues> m_dataTable = null;

    public HbA1cValues()
    {
        sumBG = 0;
        readings = 0;
        dayCount = 0;
        exp = 0;
        ReadingsPerDay = new int[25];
        m_dataTable = new Hashtable<String, DailyValues>();
    }

    public void addDay(float avgBG, int readings)
    {
        sumBG += avgBG;
        ReadingsPerDay[readings]++;
        this.readings += readings;
        exp += readings * readings;
        dayCount++;
    }

    public void addDayValueRow(DailyValuesRow dvr)
    {
        int date = (int) dvr.getDate();

        if (m_dataTable.containsKey("" + date))
        {
            DailyValues dv_int = m_dataTable.get("" + date);
            dv_int.addRow(dvr);
        }
        else
        {
            DailyValues dv = new DailyValues();
            dv.addRow(dvr);
            m_dataTable.put("" + date, dv);
        }

        // addDay(dvr.getBGRaw(), 1);
        // this.readings++;

    }

    public void processDayValues()
    {

        //System.out.println("!!!!!!!!!!!!!!!!!! HBA!C !");
        for (Enumeration<String> en = m_dataTable.keys(); en.hasMoreElements();)
        {
            DailyValues dv = m_dataTable.get(en.nextElement());
            addDay(dv.getAvgBGRaw(), dv.getBGCount());
        }

        /*
         * int num = 7 - m_dataTable.size();
         * 
         * for (int i=0; i<num; i++) { addDay(0.0f, 0); }
         */
        
        processDayValues2();
    }

    
    public int[] valClass = new int[5];
    
    
    public void processDayValues2()
    {
        // empty days are not in hashtable
        int a = 90 - this.m_dataTable.size();
        
        if (a>0)
            valClass[0] += a;
        
        for(Enumeration<String> en = this.m_dataTable.keys(); en.hasMoreElements(); )
        {
            DailyValues dv = this.m_dataTable.get(en.nextElement());
            
            int cnt= dv.getRowCount();
            
            //System.out.println(cnt);
            if (cnt<=1)
                valClass[0]++;
            else if (cnt<=3)
                valClass[1]++;
            else if (cnt<=5)
                valClass[2]++;
            else if (cnt<=7)
                valClass[3]++;
            else
                valClass[4]++;
        }
        
        
        
       // 0, 1 = C1
       // 2, 3 = C2
       // 4, 5 = C3
       // 6, 7 = C4
       // >= 8 = C5 
        
    }
    
    
    public float getAvgBG()
    {
        if (dayCount != 0)
        {
           
            if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
                return sumBG / dayCount;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, (sumBG / dayCount));

           // return sumBG / dayCount;
        
        }   
        else
            return 0;
    }

    public float getAvgBGForMethod3()
    {
        if (dayCount != 0)
            return sumBG / dayCount;
            //return sumBG / readings;
        else
            return 0;
    }

    public int getDayCount()
    {
        return dayCount;
    }

    public String getValuation()
    {
        float value = 0;

        for (int i = 0; i < 5; i++)
            value += getPercentOfDaysInClass(i) * (i + 1); // max value = 5;

        if (value < 2)
            return m_ic.getMessage("NO_EXPRESSIVENESS");
        if (value < 3)
            return m_ic.getMessage("LITTLE_EXPRESSIVENESS");
        if (value < 4)
            return m_ic.getMessage("STANDARD_EXPRESSIVENESS");
        else
            return m_ic.getMessage("GOOD_EXPRESSIVENESS");
    }

    public int getReadings()
    {
        return readings;
    }

    public float getReadingsPerDay()
    {
        if (dayCount != 0)
            return readings / (float) dayCount;
        else
            return 0;
    }

    public float getHbA1c_Method1()
    {
        if (readings == 0)
            return 0;

        if (dayCount > 0)
            return (float) ((getAvgBG() + 66.1) / 31.7);
        else
            return 0;
    }

    public float getHbA1c_Method2()
    {
        if (readings == 0)
            return 0;

        if (dayCount > 0)
            return (getAvgBG() / 30 + 2);
        else
            return 0;
    }

    public float getHbA1c_Method3()
    {
        // HbA1c =( mg/dl + 86) / 33.3

        if (readings == 0)
            return 0;

        if (dayCount > 0)
        {
            //System.out.println("avg bg: " + getAvgBGForMethod3());
            //System.out.println((getAvgBGForMethod3() + 86.0f) / 33.3f);
            return ((getAvgBGForMethod3() + 86.0f) / 33.3f);
        }
        else
            return 0;
    }

    public int getExp()
    {
        return exp;
    }

    public float getPercentOfDaysInClass(int r)
    {

        float f = (valClass[r] /90.0f);
        
        //System.out.println("Class: " + r + "= "+ valClass[r] + " = " + f);
        
        return f;
        
        
        /*
        switch (r)
        {
        case 0:
            return (ReadingsPerDay[0] + ReadingsPerDay[1] - dayCount + 90) / (float) 90;
        case 1:
            return (ReadingsPerDay[2] + ReadingsPerDay[3]) / (float) 90;
        case 2:
            return (ReadingsPerDay[4] + ReadingsPerDay[5]) / (float) 90;
        case 3:
            return (ReadingsPerDay[6] + ReadingsPerDay[7]) / (float) 90;
        case 4:
            int tmp = 0;
            for (int i = 8; i < ReadingsPerDay.length; i++)
                tmp += ReadingsPerDay[i];
            return tmp / (float) 90;
        default:
            return 0;
        }
        
        */
    }
}
