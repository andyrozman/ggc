package ggc.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;

import ggc.core.db.hibernate.DayValueH;
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
 *  Filename:     DailyValues
 *  Description:  This file is data file for storing DailyValues, it stores all data 
 *                for a day. 
 * 
 *  @author  tscherno
 *  @author  schultd
 *  @author  Andy {andy@atech-software.com}  
 */

public class DailyValues implements Serializable
{

    private static final long serialVersionUID = -375771912636631298L;
    private boolean debug = false;
    private I18nControlAbstract m_ic = DataAccess.getInstance().getI18nControlInstance();

    private String[] column_names = { m_ic.getMessage("DATE_TIME"), //
                                     m_ic.getMessage("BG"), //
                                     m_ic.getMessage("BOLUS_INSULIN_SHORT"), //
                                     m_ic.getMessage("BASAL_INSULIN_SHORT"), //
                                     m_ic.getMessage("CH"), //
                                     m_ic.getMessage("ACTIVITY"), //
                                     m_ic.getMessage("URINE"), //
                                     m_ic.getMessage("COMMENT") };

    ArrayList<DailyValuesRow> dataRows = new ArrayList<DailyValuesRow>();
    Hashtable<String, String> day_count = new Hashtable<String, String>();

    long date = 0;
    boolean bHasChangedValues = false;
    boolean bOnlyInsert = true;

    float sumBG = 0;
    float sumIns1 = 0;
    float sumIns2 = 0;
    float sumBE = 0;
    float sum_basal = 0.0f;
    float sum_bolus = 0.0f;

    int counterBG = 0;
    int counterBE = 0;
    int counterIns1 = 0;
    int counterIns2 = 0;
    int count_basal = 0;
    int count_bolus = 0;

    float highestBG = 0;
    float lowestBG = Float.MAX_VALUE;
    float stdDev = 0;

    boolean changed = false;

    private ArrayList<DayValueH> deleteList = null;

    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     */
    public DailyValues()
    {
    }


    /**
     * Reset Changed
     */
    public void resetChanged()
    {
        changed = false;
    }


    /**
     * Set Date
     * 
     * @param date
     */
    public void setDate(long date)
    {
        this.date = date;
    }


    /**
     * Set Std Dev
     * @param stdDev
     */
    public void setStdDev(float stdDev)
    {
        this.stdDev = stdDev;
    }


    /**
     * Add Row
     * 
     * @param dVR
     */
    public void addRow(DailyValuesRow dVR)
    {
        dataRows.add(dVR);
        this.sort();

        if (!this.day_count.contains(dVR.getDateAsString()))
        {
            this.day_count.put(dVR.getDateAsString(), "");
            // System.out.println(" Day count: " + this.day_count.size());
        }

        bOnlyInsert = false;
        refreshStatData();
    }


    /**
     * Delete Row
     * @param i
     */
    public void deleteRow(int i)
    {
        try
        {

            DailyValuesRow dVR = getRow(i);

            dataRows.remove(i);
            this.refreshStatData();

            if (dVR.hasHibernateObject())
            {
                // problem on delete
                DataAccess.getInstance().getDb().deleteHibernate(dVR.getHibernateObject());
            }

        }
        catch (Exception ex)
        {
            System.out.println("Error on delete from DailyValues" + ex);
        }

        bOnlyInsert = false;
        changed = true;

    }


    /**
     * Refresh Stat Data
     */
    public void refreshStatData()
    {

        sumBG = 0.0f;
        sumIns1 = 0.0f;
        sumIns2 = 0.0f;
        sumBE = 0.0f;
        sum_bolus = 0.0f;
        sum_basal = 0.0f;

        counterBG = 0;
        counterBE = 0;
        counterIns1 = 0;
        counterIns2 = 0;
        count_bolus = 0;
        count_basal = 0;

        highestBG = 0.0f;
        lowestBG = Float.MAX_VALUE;

        for (int i = 0; i < getRowCount(); i++)
        {
            // System.out.println(i + " / " + getRowCount());

            DailyValuesRow dvr = getRow(i);

            float dVR_BG = dvr.getBGRaw();
            // float dVR_BG = dvr.getBG();

            if (dVR_BG != 0)
            {
                sumBG += dVR_BG;
                if (highestBG < dVR_BG)
                {
                    highestBG = dVR_BG;
                }
                if (lowestBG > dVR_BG)
                {
                    lowestBG = dVR_BG;
                }
                counterBG++;
            }

            sumIns1 += dvr.getIns1();
            if (dvr.getIns1() != 0)
            {
                counterIns1++;
            }

            sumIns2 += dvr.getIns2();
            if (dvr.getIns2() != 0)
            {
                counterIns2++;
            }

            sumBE += dvr.getCH();
            if (dvr.getCH() != 0)
            {
                counterBE++;
            }

            sum_basal += dvr.getBasalInsulin();
            count_basal += dvr.getBasalInsulinCount();

            sum_bolus += dvr.getBolusInsulin();
            count_bolus += dvr.getBolusInsulinCount();

        }

        if (debug)
        {
            System.out.println("date=" + date + "sumBG=" + sumBG + " (" + counterBG + ") " + "sumIns1=" + sumIns1
                    + " (" + counterIns1 + ") " + "sumIns2=" + sumIns2 + " (" + counterIns2 + ") ");
        }

    }


    /**
     * Has Changed
     * 
     * @return
     */
    public boolean hasChanged()
    {
        if (changed)
            return true;
        else
        {
            for (int i = 0; i < this.getRowCount(); i++)
            {
                if (getChanged(i))
                    return true;
            }

            return false;
        }
    }


    /**
     * Get Column Count
     * 
     * @return
     */
    public int getColumnCount()
    {
        return column_names.length;
    }


    /**
     * Get Row Count
     * 
     * @return
     */
    public int getRowCount()
    {
        if (dataRows == null)
            return 0;
        else
            return dataRows.size();
    }


    /**
     * Get Row
     * 
     * @param index
     * @return
     */
    public DailyValuesRow getRow(int index)
    {
        return this.dataRows.get(index);
    }


    /**
     * Has Deleted Items
     * 
     * @return
     */
    public boolean hasDeletedItems()
    {
        if (deleteList == null)
            return false;
        else
            return deleteList.size() != 0;
    }


    /**
     * Get Deleted Items
     * 
     * @return
     */
    public ArrayList<DayValueH> getDeletedItems()
    {
        return deleteList;
    }


    /**
     * Get Value At
     * 
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column)
    {
        return getRow(row).getValueAt(column);
    }


    /**
     * Get Day And Month As String
     *  
     * @return
     */
    public String getDayAndMonthAsString()
    {
        int day, month;
        day = (int) (date % 100);
        month = (int) (date % 10000) / 100;
        return String.format("%1$02d.%2$02d.", day, month);
    }


    /**
     * Get Date
     * 
     * @return A {@code long} with &quot;YYYYMMDD&quot; as contents. E.g. the 3rd January 2012 would be 20120103.
     */
    public long getDate()
    {
        return date;
    }


    /**
     * Get Date As Localized String
     * 
     * @return
     */
    public String getDateAsLocalizedString()
    {
        // ATechDate at = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN,
        // this.date); //.getGregorianCalendar(), 4);
        // System.out.println("Date: (getDate): " + this.getDate() +
        // " , atechdate=" + at.getDateString());
        // return at.toString();
        return m_da.getAsLocalizedDateString(
            new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, this.date).getGregorianCalendar(), 4);
    }


    /**
     * Get Date As String
     * 
     * @return
     */
    public String getDateAsString()
    {
        return getDateAsLocalizedString();
    }


    /**
     * Get Changed
     * 
     * @param row
     * @return
     */
    public boolean getChanged(int row)
    {
        return getRow(row).hasChanged();
    }


    /**
     * Get Column Name
     * 
     * @param column
     * @return
     */
    public String getColumnName(int column)
    {
        return column_names[column] == null ? m_ic.getMessage("NO_NAME") : column_names[column];
    }


    /**
     * Get Average BG Raw (mg/dL)
     * 
     * @return
     */
    public float getAvgBGRaw()
    {
        if (counterBG != 0)
            return sumBG / counterBG;
        else
            return 0;
    }


    /**
     * Get Average BG 
     * 
     * @return
     */
    public float getAvgBG()
    {
        if (counterBG != 0)
        {
            if (m_da.getBGMeasurmentType() == DataAccess.BG_MGDL)
                return sumBG / counterBG;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, sumBG / counterBG);
        }
        else
            return 0;
    }


    /**
     * Get Average Ins 1
     * 
     * @return
     */
    public float getAvgIns1()
    {
        if (counterIns1 != 0)
            return sumIns1 / counterIns1;
        else
            return 0;
    }


    /**
     * Get Average Ins 2
     * 
     * @return
     */
    public float getAvgIns2()
    {
        if (counterIns2 != 0)
            return sumIns2 / counterIns2;
        else
            return 0;
    }


    /**
     * Get Average Ins
     * 
     * @return
     */
    public float getAvgIns()
    {
        if (counterIns1 + counterIns2 != 0)
            return (sumIns1 + sumIns2) / (counterIns1 + counterIns2);
        else
            return 0;
    }


    /**
     * Get Average Bolus
     * 
     * @return
     */
    public float getAvgBolus()
    {
        if (count_bolus != 0)
            return this.sum_bolus / this.count_bolus;
        else
            return 0.0f;
    }


    /**
     * Get Average Basal
     * 
     * @return
     */
    public float getAvgBasal()
    {
        if (count_basal != 0)
            return this.sum_basal / this.count_basal;
        else
            return 0.0f;
    }


    /**
     * Get Average Basal
     * 
     * @return
     */
    public float getAvgBasalBolus()
    {
        if (count_basal + count_bolus != 0)
            return (this.sum_basal + this.sum_bolus) / (count_basal + count_bolus);
        else
            return 0.0f;
    }


    /**
     * Get Average CH
     * 
     * @return
     */
    public float getAvgCH()
    {
        if (counterBE != 0)
            return sumBE / counterBE;
        else
            return 0;
    }


    /**
     * Get Sum BG
     * 
     * @return
     */
    public float getSumBG()
    {
        return sumBG;
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
     * Get Sum Ins 2
     * 
     * @return
     */
    public float getSumIns2()
    {
        return sumIns2;
    }


    /**
     * Get Sum Ins
     * 
     * @return
     */
    public float getSumIns()
    {
        return sumIns1 + sumIns2;
    }


    /**
     * Get Sum Basal
     * 
     * @return
     */
    public float getSumBasal()
    {
        return this.sum_basal;
    }


    /**
     * Get Sum Bolus
     * 
     * @return
     */
    public float getSumBolus()
    {
        return this.sum_bolus;
    }


    /**
     * Get Sum Basal
     * 
     * @return
     */
    public float getSumBasalBolus()
    {
        return this.sum_basal + this.sum_bolus;
    }


    /**
     * Get Sum CH
     * 
     * @return
     */
    public float getSumCH()
    {
        return sumBE;
    }


    /**
     * Get Count of BG
     * 
     * @return
     */
    public int getBGCount()
    {
        return counterBG;
    }


    /**
     * Get Count of Ins 1
     * 
     * @return
     */
    public int getIns1Count()
    {
        return counterIns1;
    }


    /**
     * Get Count of Ins 2
     * 
     * @return
     */
    public int getIns2Count()
    {
        return counterIns2;
    }


    /**
     * Get Count of Ins
     * 
     * @return
     */
    public int getInsCount()
    {
        return counterIns1 + counterIns2;
    }


    /**
     * Get Count of Basal
     * 
     * @return
     */
    public int getBasalCount()
    {
        return this.count_basal;
    }


    /**
     * Get Count of Bolus
     * 
     * @return
     */
    public int getBolusCount()
    {
        return this.count_bolus;
    }


    /**
     * Get Count of Bolus
     * 
     * @return
     */
    public int getBasalBolusCount()
    {
        return this.count_bolus + this.count_basal;
    }


    /**
     * Get Count of CH
     * 
     * @return
     */
    public int getCHCount()
    {
        return counterBE;
    }


    /**
     * Get Highest BG
     * 
     * @return
     */
    public float getHighestBG()
    {
        if (m_da.getBGMeasurmentType() == DataAccess.BG_MGDL)
            return highestBG;
        else
            return m_da.getBGValueDifferent(DataAccess.BG_MGDL, highestBG);
    }


    /**
     * Get Lowest BG
     * 
     * @return
     */
    public float getLowestBG()
    {
        if (lowestBG != Float.MAX_VALUE)
        {
            if (m_da.getBGMeasurmentType() == DataAccess.BG_MGDL)
                return lowestBG;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, lowestBG);
        }
        else
            return 0;
    }


    /**
     * Get Std Dev
     * 
     * @return
     */
    public float getStdDev()
    {
        float tmp = 0;
        int c = 0;
        for (int i = 0; i < getRowCount(); i++)
        {
            float bg = getRow(i).getBG();
            if (bg != 0)
            {
                tmp += (bg - getAvgBG()) * (bg - getAvgBG());
                c++;
            }
        }
        if (--c > 0)
            return (float) Math.sqrt(tmp / c);
        else
            return 0;
    }


    /**
     * Sort 
     */
    public void sort()
    {
        Collections.sort(this.dataRows);
    }

}
