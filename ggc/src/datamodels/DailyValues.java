/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package datamodels;


import db.DataBaseHandler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


public class DailyValues implements Serializable
{
    String[] columnNames = {"Time", "BG", "Ins1", "Ins2", "BU", "Act", "Comment"};
    Vector dataRows = new Vector();
    static DataBaseHandler dbH;
    private static DailyValues singleton = null;
    java.util.Date date;

    boolean bHasChangedValues = false;
    boolean bOnlyInsert = false;

    float sumBG = 0;
    float sumIns1 = 0;
    float sumIns2 = 0;
    float sumBE = 0;

    int counterBG = 0;
    int counterBE = 0;
    int counterIns1 = 0;
    int counterIns2 = 0;

    float highestBG = 0;
    float lowestBG = Float.MAX_VALUE;
    float stdDev = 0;


    public void setStdDev(float stdDev)
    {
        this.stdDev = stdDev;
    }

    public DailyValues()
    {
        dbH = DataBaseHandler.getInstance();
    }

    public static DailyValues getInstance()
    {
        if (singleton == null)
            singleton = new DailyValues();
        return singleton;
    }

    public void setDate(long date)
    {
        this.date = new Date(date);
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void saveDay()
    {
        dbH.saveDayStats(this);
    }

    public void setNewRow(DailyValuesRow dVR)
    {
        Date time = dVR.getDateTime();
        int size = dataRows.size();

        if (time != null && dVR.getDateTime() != null) {
            bHasChangedValues = true;
            //insert in the right place...
            //System.err.println(size + "");
            if (size > 0) {
                int i = 0;
                for (i = 0; i < size; i++)
                    if (getDateTimeAt(i).after(time)) {
                        dataRows.add(i, dVR);
                        return;
                    }
                dataRows.add(i, dVR);
            } else
                dataRows.add(dVR);
        }
        sumBG += dVR.getBG();
        if (dVR.getBG() != 0) {
            if (highestBG < dVR.getBG())
                highestBG = dVR.getBG();
            if (lowestBG > dVR.getBG())
                lowestBG = dVR.getBG();
            counterBG++;
        }
        sumIns1 += dVR.getIns1();
        if (dVR.getIns1() != 0)
            counterIns1++;
        sumIns2 += dVR.getIns2();
        if (dVR.getIns2() != 0)
            counterIns2++;
        sumBE += dVR.getBE();
        if (dVR.getBE() != 0)
            counterBE++;
    }

    public DailyValuesRow getRowAt(int i)
    {
        return (DailyValuesRow)dataRows.elementAt(i);
    }

    public void deleteRow(int i)
    {
        try {
            if (i != -1)
                dataRows.remove(i);
        } catch (Exception e) {
        }
    }

    public void setColumnNames(String[] Names)
    {
        columnNames = Names;
    }

    public boolean hasChanged()
    {
        return bHasChangedValues;
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        if (dataRows == null)
            return 0;
        else
            return dataRows.size();
    }

    public Object getValueAt(int row, int column)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getValueAt(column);
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        ((DailyValuesRow)(dataRows.elementAt(row))).setValueAt(aValue, column);
        bHasChangedValues = true;
    }

    public void setIsNew(boolean b)
    {
        bOnlyInsert = b;
    }

    public boolean onlyInsert()
    {
        return bOnlyInsert;
    }

    public String getDayAndMonthAsString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        return sdf.format(date);
    }

    public Date getDate()
    {
        return date;
    }

    public String getDateAsString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public Date getDateTimeAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getDateTime();
    }

    public String getDateTimeAsStringAt(int row)
    {
        DailyValuesRow dv = (DailyValuesRow)(dataRows.elementAt(row));
        return dv.getDateAsString() + " " + dv.getTimeAsString();
    }

    public float getBGAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getBG();
    }

    public float getIns1At(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getIns1();
    }

    public float getIns2At(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getIns2();
    }

    public float getBUAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getBE();
    }

    public int getActAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getAct();
    }

    public String getCommentAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getComment();
    }

    public String getColumnName(int column)
    {
        return columnNames[column] == null ? "No Name" : columnNames[column];
    }

    public float getAvgBG()
    {
        if (counterBG != 0)
            return sumBG / counterBG;
        else
            return 0;
    }

    public float getAvgIns1()
    {
        if (counterIns1 != 0)
            return sumIns1 / counterIns1;
        else
            return 0;
    }

    public float getAvgIns2()
    {
        if (counterIns2 != 0)
            return sumIns2 / counterIns2;
        else
            return 0;
    }

    public float getAvgIns()
    {
        if (counterIns1 + counterIns2 != 0)
            return (sumIns1 + sumIns2) / (counterIns1 + counterIns2);
        else
            return 0;
    }

    public float getAvgBE()
    {
        if (counterBE != 0)
            return sumBE / counterBE;
        else
            return 0;
    }

    public float getSumIns1()
    {
        return sumIns1;
    }

    public float getSumIns2()
    {
        return sumIns2;
    }

    public float getSumIns()
    {
        return sumIns1 + sumIns2;
    }

    public float getSumBE()
    {
        return sumBE;
    }

    public int getBGCount()
    {
        return counterBG;
    }

    public int getIns1Count()
    {
        return counterIns1;
    }

    public int getIns2Count()
    {
        return counterIns2;
    }

    public int getInsCount()
    {
        return counterIns1 + counterIns2;
    }

    public int getBECount()
    {
        return counterBE;
    }

    public float getHighestBG()
    {
        return highestBG;
    }

    public float getLowestBG()
    {
        if (lowestBG != Float.MAX_VALUE)
            return lowestBG;
        else
            return 0;
    }

    public float getStdDev()
    {
        float tmp = 0;
        int c = 0;
        for (int i = 0; i < getRowCount(); i++) {
            float bg = getBGAt(i);
            if (bg != 0) {
                tmp += (bg - getAvgBG()) * (bg - getAvgBG());
                c++;
            }
        }
        return (float)Math.sqrt(tmp / --c);
    }
}
