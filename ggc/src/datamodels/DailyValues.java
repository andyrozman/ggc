/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package datamodels;


import db.DataBaseHandler;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;


public class DailyValues
{
    String[] columnNames = new String[0];
    Vector dataRows = new Vector();
    ResultSet results;
    static DataBaseHandler dbH;
    private static DailyValues singleton = null;
    Date date;

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

    public void setDateAndUpdate(long date)
    {
        setDate(date);
        getDayStats(this.date.toString());
    }

    private void getDayStats(String date)
    {
        sumBG = 0;
        sumIns1 = 0;
        sumIns2 = 0;
        sumBE = 0;

        counterBG = 0;
        counterBE = 0;
        counterIns1 = 0;
        counterIns2 = 0;

        highestBG = 0;
        lowestBG = Float.MAX_VALUE;
        stdDev = 0;


        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.date = new Date(sf.parse(date).getTime());
        } catch (ParseException e) {
        }

        results = dbH.getDayStats(date);
        try {
            ResultSetMetaData metadata = results.getMetaData();
            int columns = metadata.getColumnCount();

            columnNames = new String[columns];
            for (int i = 0; i < columns; i++)
                columnNames[i] = metadata.getColumnLabel(i + 1);

            dataRows = new Vector();
            Vector BGdata = new Vector();

            while (results.next()) {
                Date rDate = results.getDate(1);
                Time rTime = results.getTime(2);
                //bg
                float rBG = results.getFloat(3);
                if (rBG != 0) {
                    sumBG += rBG;
                    BGdata.addElement(new Float(rBG));
                    counterBG++;
                    if (highestBG < rBG)
                        highestBG = rBG;
                    if (lowestBG > rBG)
                        lowestBG = rBG;
                }
                //ins1
                float rIns1 = results.getFloat(4);
                if (rIns1 != 0) {
                    sumIns1 += rIns1;
                    counterIns1++;
                }

                //ins2
                float rIns2 = results.getFloat(5);
                if (rIns2 != 0) {
                    sumIns2 += rIns2;
                    counterIns2++;
                }

                //be
                float rBE = results.getFloat(6);
                if (rBE != 0) {
                    sumBE += rBE;
                    counterBE++;
                }

                int rAct = results.getInt(7);
                String rComment = results.getString(8);

                DailyValuesRow dVR = new DailyValuesRow(rDate, rTime, rBG, rIns1, rIns2, rBE, rAct, rComment);
                dataRows.addElement(dVR);
            }
            float avgBG = 0;
            if (counterBG != 0)
                avgBG = sumBG / counterBG;

            float tmp = 0;
            for (int i = 0; i < BGdata.size(); i++)
                tmp += Math.pow(((Float)(BGdata.get(i))).floatValue() - avgBG, 2.0);

            if (BGdata.size() != 0)
                stdDev = (float)Math.sqrt(tmp / (BGdata.size() - 1));
            else
                stdDev = 0;
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

    public void saveDay()
    {
        dbH.saveDayStats(this);
    }

    public void setNewRow(DailyValuesRow dVR)
    {
        Time time = dVR.getTime();
        int size = dataRows.size();

        if (time != null && dVR.getDate() != null) {
            bHasChangedValues = true;
            //insert in the right place...
            //System.err.println(size + "");
            if (size > 0) {
                int i = 0;
                for (i = 0; i < size; i++)
                    if (getTimeAt(i).after(time)) {
                        dataRows.add(i, dVR);
                        return;
                    }
                dataRows.add(i, dVR);
            } else
                dataRows.add(dVR);
        }
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

    public Date getDate()
    {
        return date;
    }

    public Time getTimeAt(int row)
    {
        return ((DailyValuesRow)(dataRows.elementAt(row))).getTime();
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
        return stdDev;
    }
}
