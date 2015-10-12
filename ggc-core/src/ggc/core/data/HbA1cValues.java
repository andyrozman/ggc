package ggc.core.data;

import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;

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
 *  Filename:     HbA1cValues  
 *  Description:  Calculates the HbA1c based on data for last 3 months
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */

public class HbA1cValues
{

    DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private float sumBG;
    private int readings;
    private int dayCount;
    private int exp;
    // private int[] ReadingsPerDay;

    private Hashtable<String, DailyValues> m_dataTable = null;


    /**
     * Constructor
     */
    public HbA1cValues()
    {
        sumBG = 0;
        readings = 0;
        dayCount = 0;
        exp = 0;
        // ReadingsPerDay = new int[25];
        m_dataTable = new Hashtable<String, DailyValues>();
    }


    /**
     * Add Day
     * 
     * @param avgBG
     * @param _readings
     */
    public void addDay(float avgBG, int _readings)
    {
        sumBG += avgBG;
        // ReadingsPerDay[_readings]++;
        this.readings += _readings;
        exp += _readings * _readings;
        dayCount++;
    }


    /**
     * Add Day Value Row
     * 
     * @param dvr
     */
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


    /**
     * Process Day Values
     */
    public void processDayValues()
    {

        // System.out.println("!!!!!!!!!!!!!!!!!! HBA!C !");
        for (Enumeration<String> en = m_dataTable.keys(); en.hasMoreElements();)
        {
            DailyValues dv = m_dataTable.get(en.nextElement());
            addDay(dv.getAvgBGRaw(), dv.getBGCount());
        }

        /*
         * int num = 7 - m_dataTable.size();
         * for (int i=0; i<num; i++) { addDay(0.0f, 0); }
         */

        processDayValues2();
    }

    /**
     * Values Class
     */
    public int[] valClass = new int[5];


    /**
     * Process Day Values 2 (new one for new Hba1c Graphs)
     */
    public void processDayValues2()
    {
        // empty days are not in hashtable
        int a = 90 - this.m_dataTable.size();

        if (a > 0)
        {
            valClass[0] += a;
        }

        for (Enumeration<String> en = this.m_dataTable.keys(); en.hasMoreElements();)
        {
            DailyValues dv = this.m_dataTable.get(en.nextElement());

            int cnt = dv.getRowCount();

            // System.out.println(cnt);
            if (cnt <= 1)
            {
                valClass[0]++;
            }
            else if (cnt <= 3)
            {
                valClass[1]++;
            }
            else if (cnt <= 5)
            {
                valClass[2]++;
            }
            else if (cnt <= 7)
            {
                valClass[3]++;
            }
            else
            {
                valClass[4]++;
            }
        }

        // 0, 1 = C1
        // 2, 3 = C2
        // 4, 5 = C3
        // 6, 7 = C4
        // >= 8 = C5

    }


    /**
     * Get Average BG
     * @return
     */
    public float getAvgBG()
    {
        if (dayCount != 0)
        {

            if (m_da.getGlucoseUnitType() == GlucoseUnitType.mg_dL)
                return sumBG / dayCount;
            else
                return m_da.getBGValueDifferent(GlucoseUnitType.mg_dL, sumBG / dayCount);

            // return sumBG / dayCount;

        }
        else
            return 0;
    }


    /**
     * Get Average BG For Method 3
     * 
     * @return
     */
    public float getAvgBGForMethod3()
    {
        if (dayCount != 0)
            return sumBG / dayCount;
        else
            return 0;
    }


    /**
     * Get Day Count
     * 
     * @return
     */
    public int getDayCount()
    {
        return dayCount;
    }


    /**
     * Get Valuation
     * 
     * @return
     */
    public String getValuation()
    {
        float value = 0;

        for (int i = 0; i < 5; i++)
        {
            value += getPercentOfDaysInClass(i) * (i + 1); // max value = 5;
        }

        if (value < 2)
            return m_ic.getMessage("NO_EXPRESSIVENESS");
        if (value < 3)
            return m_ic.getMessage("LITTLE_EXPRESSIVENESS");
        if (value < 4)
            return m_ic.getMessage("STANDARD_EXPRESSIVENESS");
        else
            return m_ic.getMessage("GOOD_EXPRESSIVENESS");
    }


    /**
     * Get Readings
     * 
     * @return
     */
    public int getReadings()
    {
        return readings;
    }


    /**
     * Get Reading Per Day
     * 
     * @return
     */
    public float getReadingsPerDay()
    {
        if (dayCount != 0)
            return readings / (float) dayCount;
        else
            return 0;
    }


    /**
     * Get HbA1c Method 1
     * 
     * @return
     */
    public float getHbA1c_Method1()
    {
        if (readings == 0)
            return 0;

        if (dayCount > 0)
            return (float) ((getAvgBG() + 66.1) / 31.7);
        else
            return 0;
    }


    /**
     * Get HbA1c Method 2
     * 
     * @return
     */
    public float getHbA1c_Method2()
    {
        if (readings == 0)
            return 0;

        if (dayCount > 0)
            return getAvgBG() / 30 + 2;
        else
            return 0;
    }


    /**
     * Get HbA1c Method 3
     * 
     * @return
     */
    public float getHbA1c_Method3()
    {
        // HbA1c =( mg/dl + 86) / 33.3

        if (readings == 0)
            return 0;

        if (dayCount > 0)
            // System.out.println("avg bg: " + getAvgBGForMethod3());
            // System.out.println((getAvgBGForMethod3() + 86.0f) / 33.3f);
            return (getAvgBGForMethod3() + 86.0f) / 33.3f;
        else
            return 0;
    }


    /**
     * Get Exp
     * 
     * @return
     */
    public int getExp()
    {
        return exp;
    }


    /**
     * Get Percent Of Days In Class
     * 
     * @param r
     * @return
     */
    public float getPercentOfDaysInClass(int r)
    {

        float f = valClass[r] / 90.0f;

        // System.out.println("Class: " + r + "= "+ valClass[r] + " = " + f);

        return f;

        /*
         * switch (r)
         * {
         * case 0:
         * return (ReadingsPerDay[0] + ReadingsPerDay[1] - dayCount + 90) /
         * (float) 90;
         * case 1:
         * return (ReadingsPerDay[2] + ReadingsPerDay[3]) / (float) 90;
         * case 2:
         * return (ReadingsPerDay[4] + ReadingsPerDay[5]) / (float) 90;
         * case 3:
         * return (ReadingsPerDay[6] + ReadingsPerDay[7]) / (float) 90;
         * case 4:
         * int tmp = 0;
         * for (int i = 8; i < ReadingsPerDay.length; i++)
         * tmp += ReadingsPerDay[i];
         * return tmp / (float) 90;
         * default:
         * return 0;
         * }
         */
    }
}
