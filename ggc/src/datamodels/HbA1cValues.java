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
 *  Filename: HbA1cValues.java
 *  Purpose:  Calculates the HbA1c based on a given date.
 *
 *  Author:   schultd
 */

package datamodels;


public class HbA1cValues
{
    private float sumBG;
    private int readings;
    private int dayCount;
    private int exp;
    private int[] ReadingsPerDay;

    public HbA1cValues()
    {
        sumBG = 0;
        readings = 0;
        dayCount = 0;
        exp = 0;
        ReadingsPerDay = new int[25];
    }

    public void addDay(float avgBG, int readings)
    {
        sumBG += avgBG;
        ReadingsPerDay[readings]++;
        this.readings += readings;
        exp += readings * readings;
        dayCount++;

    }

    public float getAvgBG()
    {
        if (dayCount != 0)
            return sumBG / dayCount;
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
            value += getPercentOfDaysInClass(i) * (i + 1); //max value = 5;

        if (value < 2)
            return "No Expressiveness";
        if (value < 3)
            return "Little Expressiveness";
        if (value < 4)
            return "Standard Expressiveness";
        else
            return "Good Expressiveness";
    }

    public int getReadings()
    {
        return readings;
    }

    public float getReadingsPerDay()
    {
        if (dayCount != 0)
            return readings / dayCount;
        else
            return 0;
    }

    public float getHbA1c_Method1()
    {
        if (dayCount > 0)
            return (float)((getAvgBG() + 66.1) / 31.7);
        else
            return 0;
    }

    public float getHbA1c_Method2()
    {
        if (dayCount > 0)
            return (float)(getAvgBG() / 30 + 2);
        else
            return 0;
    }

    public int getExp()
    {
        return exp;
    }

    public float getPercentOfDaysInClass(int r)
    {
        switch (r) {
            case 0:
                return (ReadingsPerDay[0] + ReadingsPerDay[1] - dayCount + 90) / (float)90;
            case 1:
                return (ReadingsPerDay[2] + ReadingsPerDay[3]) / (float)90;
            case 2:
                return (ReadingsPerDay[4] + ReadingsPerDay[5]) / (float)90;
            case 3:
                return (ReadingsPerDay[6] + ReadingsPerDay[7]) / (float)90;
            case 4:
                int tmp = 0;
                for (int i = 8; i < ReadingsPerDay.length; i++)
                    tmp += ReadingsPerDay[i];
                return tmp / (float)90;
            default:
                return 0;
        }
    }
}
