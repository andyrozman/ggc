/******************************************************
 * File: DailyValues.java
 * created Mar 29, 2002 1:09:43 PM by tscherno
 */

package ggc.datamodels;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import ggc.db.DataBaseHandler;
import ggc.util.I18nControl;


public class DailyValues implements Serializable
{

    private I18nControl m_ic = I18nControl.getInstance();

    String[] columnNames = { 
                        m_ic.getMessage("TIME"),
                        m_ic.getMessage("BG"),
                        m_ic.getMessage("INS_1"),
                        m_ic.getMessage("INS_2"),
                        m_ic.getMessage("BU"),
                        m_ic.getMessage("ACT"),
                        m_ic.getMessage("COMMENT") };

    Vector dataRows = new Vector();
    static DataBaseHandler dbH;
    //private static DailyValues singleton = null;
    java.util.Date date;

    boolean bHasChangedValues = false;
    boolean bOnlyInsert = true;

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

    boolean changed = false;

    public ArrayList deleteList = null;


    public void setStdDev(float stdDev)
    {
        this.stdDev = stdDev;
    }

    public DailyValues()
    {
        dbH = DataBaseHandler.getInstance();
    }

    /*
    public static DailyValues getInstance()
    {
        if (singleton == null)
            singleton = new DailyValues();
        return singleton;
    }
    */

    public void resetChanged()
    {
	changed = false;
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
	changed = true;

        addRight:
        if (time != null && dVR.getDateTime() != null) 
	{
            bHasChangedValues = true;
            //insert in the right place...
            //System.err.println(size + "");
            if (size <= 0)
                dataRows.add(dVR);
            else 
	    {
                int i = 0;
                for (; i < size; i++)
                    if (getDateTimeAt(i).after(time)) {
                        dataRows.add(i, dVR);
                        break addRight;
                    }
                dataRows.add(i, dVR);
            }
        }

        float dVR_BG = dVR.getBG();
        if (dVR_BG != 0) {
            sumBG += dVR_BG;
            if (highestBG < dVR_BG)
                highestBG = dVR_BG;
            if (lowestBG > dVR_BG)
                lowestBG = dVR_BG;
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

        bOnlyInsert = false;
    }






    public DailyValuesRow getRowAt(int i)
    {
        return (DailyValuesRow)dataRows.elementAt(i);
    }



    public void deleteRow(int i)
    {
	changed = true;

        DailyValuesRow del_row = null;

        try {
            if (i != -1) 
	    {
                

                DailyValuesRow dVR = (DailyValuesRow)dataRows.elementAt(i);

                //del_row = dVR;

                if (dVR.getBG() != 0) {
                    sumBG -= dVR.getBG();
                    counterBG--;
                }
                if (dVR.getIns1() != 0) {
                    sumIns1 -= dVR.getIns1();
                    counterIns1--;
                }
                if (dVR.getIns2() != 0) {
                    sumIns2 -= dVR.getIns2();
                    counterIns2--;
                }
                if (dVR.getBE() != 0) {
                    sumBE -= dVR.getBE();
                    counterBE--;
                }

                dataRows.remove(i);
                highestBG = 0;
                lowestBG = Float.MAX_VALUE;
                for (int j = 0; j < dataRows.size(); j++) {
                    DailyValuesRow dVRa = (DailyValuesRow)dataRows.elementAt(j);
                    highestBG = Math.max(dVRa.getBG(), highestBG);
                    lowestBG = Math.min(dVRa.getBG(), lowestBG);
                }

		if (dVR.hasHibernateObject())
		{
		    if (deleteList==null)
			deleteList = new ArrayList();

		    deleteList.add(dVR.getHibernateObject());
                    this.changed = true;
		}

            }

	    //DailyValuesRow dVR = (DailyValuesRow)dataRows.elementAt(i);



        } 
	catch (Exception ex) 
	{
            System.out.println("Error on delete from DailyValues" + ex);
        }

        bOnlyInsert = false;
	changed = true;

    }


    public void refreshStatData()
    {
        // implement
    }


    public void setColumnNames(String[] Names)
    {
        columnNames = Names;
    }

    public boolean hasChanged()
    {
        //return bHasChangedValues;
	if (changed)
	    return true;
	else
	{
	    for(int i=0; i<this.getRowCount(); i++)
	    {
		if (getChanged(i))
		    return true;
	    }

	    return false;
	}
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

    public boolean hasDeletedItems()
    {
	if (deleteList==null)
	    return false;
	else 
	    return (deleteList.size()!=0);
    }

    public ArrayList getDeletedItems()
    {
        return deleteList;
    }




    public Object getValueAt(int row, int column)
    {
	return ((DailyValuesRow)(dataRows.elementAt(row))).getValueAt(column);
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        DailyValuesRow dVR = (DailyValuesRow)(dataRows.elementAt(row));
        if (column > 0 && column < 5) {
            float oldVal = ((Float)dVR.getValueAt(column)).floatValue();
            float newVal = ((Float)aValue).floatValue();
            switch (column) {
                case 1:
                    sumBG -= oldVal - newVal;
                    if (oldVal != 0)
                        counterBG--;
                    if (newVal != 0)
                        counterBG++;
                    break;
                case 2:
                    sumIns1 -= oldVal - newVal;
                    if (oldVal != 0)
                        counterIns1--;
                    if (newVal != 0)
                        counterIns1++;
                    break;
                case 3:
                    sumIns2 -= oldVal - newVal;
                    if (oldVal != 0)
                        counterIns2--;
                    if (newVal != 0)
                        counterIns2++;
                    break;
                case 4:
                    sumBE -= oldVal - newVal;
                    if (oldVal != 0)
                        counterBE--;
                    if (newVal != 0)
                        counterBE++;
            }
        }
        dVR.setValueAt(aValue, column);

        highestBG = 0;
        lowestBG = Float.MAX_VALUE;
        for (int j = 0; j < dataRows.size(); j++) {
            dVR = (DailyValuesRow)dataRows.elementAt(j);
            highestBG = Math.max(dVR.getBG(), highestBG);
            lowestBG = Math.min(dVR.getBG(), lowestBG);
        }

        bHasChangedValues = true;
        bOnlyInsert = false;
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


    public int getDateD(int row)
    {
        DailyValuesRow dv = (DailyValuesRow)(dataRows.elementAt(row));
        return dv.getDateD();
    }


    public int getDateT(int row)
    {
        DailyValuesRow dv = (DailyValuesRow)(dataRows.elementAt(row));
        return dv.getDateT();
    }

    public boolean getChanged(int row)
    {
	return ((DailyValuesRow)(dataRows.elementAt(row))).hasChanged();
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
        return columnNames[column] == null ? m_ic.getMessage("NO_NAME") : columnNames[column];
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

    public float getSumBG()
    {
        return sumBG;
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
        if (--c > 0)
            return (float)Math.sqrt(tmp / c);
        else
            return 0;
    }


    public int getDateD()
    {
	
	String dat = "";

	dat += date.getYear() + 1900;
	dat += getLeadingZero(date.getMonth() + 1, 2);
	dat += getLeadingZero(date.getDate(), 2);

	return Integer.parseInt(dat);

    }


    public int getDateT()
    {
	
	return date.getHours()*100 + date.getMinutes();
	
    }



    public String getLeadingZero(int value, int num_places)
    {

	String tmp = "" + value;

	while (tmp.length()<num_places)
	{
	    tmp = "0" + tmp; 
	}

	return tmp;

    }


}
