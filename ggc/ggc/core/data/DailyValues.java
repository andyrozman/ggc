package ggc.core.data;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
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
 *  Filename: DailyValues
 *  Purpose:  This file is data file for storing DailyValues, it stores all data for
 *      a day. 
 *
 *  Author:   tscherno
 *  Author:   andyrozman  {andy@atech-software.com}
*/


import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DailyValues implements Serializable
{

    private boolean debug = false;

    private I18nControl m_ic = I18nControl.getInstance();

    private String[] column_names = { 
                        m_ic.getMessage("DATE_TIME"),
                        m_ic.getMessage("BG"),
                        m_ic.getMessage("INS_1"),
                        m_ic.getMessage("INS_2"),
                        m_ic.getMessage("BU"),
                        m_ic.getMessage("ACTIVITY"),
                        m_ic.getMessage("URINE"),
                        m_ic.getMessage("COMMENT") };

    ArrayList<DailyValuesRow> dataRows = new ArrayList<DailyValuesRow>();
    //static DataBaseHandler dbH;
    //private static DailyValues singleton = null;
    //java.util.Date date;
    long date = 0;

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

    public ArrayList<DayValueH> deleteList = null;


    public void setStdDev(float stdDev)
    {
        this.stdDev = stdDev;
    }

    public DailyValues()
    {
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

    public void setDateTime(long date)
    {
        //this.date = new Date(date);
        this.date = date;
    }

    /*
    public void setDate(Date date)
    {
        this.date = date;
    }*/

    public void saveDay()
    {
        //dbH.saveDayStats(this);
    }

    public void setNewRow(DailyValuesRow dVR)
    {
        addRow(dVR);
    }


    public void setNewRow(DailyValuesRow dVR, boolean tru)
    {
	
        this.date = dVR.getDate();
        long time = dVR.getDateTime();
        int size = dataRows.size();
        changed = true;

        //addRight:

        //if (time != null && dVR.getDateTime() != null) 
        {
            bHasChangedValues = true;
            //insert in the right place...
            //System.err.println(size + "");
            if (size <= 0)
                dataRows.add(dVR);
            else 
            {
        		int i = 0;
        		boolean added = false;
        
                for ( ; i < size; i++)
        		{
                    if (getDateTimeAt(i) < time) 
        		    {
                        dataRows.add(i, dVR);
                        added = true;
                        break;
                    }
        		}

        		if (!added)
        		{
        		    dataRows.add(i, dVR);
        		}
                //dataRows.add(i, dVR);
            }
        }

	//System.out.println("dataRows=" + dataRows.size());
	
	/*
        float dVR_BG = dVR.getBG();

	if (dVR_BG != 0) 
	{
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

        sumBE += dVR.getCH();
        if (dVR.getCH() != 0)
            counterBE++;
	*/
    
        bOnlyInsert = false;

        refreshStatData();

    }



    public void addRow(DailyValuesRow dVR)
    {
    	this.date = dVR.getDate();
    	long time = dVR.getDateTime();
    	int size = dataRows.size();
    	changed = true;
    
    	if (size <= 0)
    	{
    	    dataRows.add(dVR);
    	}
    	else 
    	{
    
    	    int i = 0;
    	    boolean added = false;
    
    	    for ( ; i < size; i++)
    	    {
        		if (getDateTimeAt(i) > time) 
        		{
        		    dataRows.add(i, dVR);
        		    added = true;
        		    break;
        		}
    	    }
    
    	    if (!added)
    		dataRows.add(i, dVR);
    
    	}
    
    	bOnlyInsert = false;
    	refreshStatData();

    }






    public DailyValuesRow getRowAt(int i)
    {
        return getRow(i);
    }



    public void deleteRow(int i)
    {
        try 
    	{
    
    	    DailyValuesRow dVR = getRow(i);
    
    	    dataRows.remove(i);
    	    this.refreshStatData();
    
    	    if (dVR.hasHibernateObject())
    	    {
                /*
        		if (deleteList==null)
        		    deleteList = new ArrayList<DayValueH>();
        
        		deleteList.add(dVR.getHibernateObject()); 
        		this.changed = true;
                */

                // problem on delete
                DataAccess.getInstance().getDb().deleteHibernate(dVR.getHibernateObject());
                //DailyValueH dvh = dVR.getHibernateObject();
                

    	    }
    
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

    	sumBG = 0.0f;
    	sumIns1 = 0.0f;
    	sumIns2 = 0.0f;
    	sumBE = 0.0f;
    
    	counterBG = 0;
    	counterBE = 0;
    	counterIns1 = 0;
    	counterIns2 = 0;
    
    	highestBG = 0.0f;
    	lowestBG = Float.MAX_VALUE;
    
    
    	for (int i=0; i<getRowCount(); i++)
    	{
    	    //System.out.println(i + " / " + getRowCount());
    
    	    DailyValuesRow dvr = getRow(i);
    
    	    float dVR_BG = dvr.getBG();
    
    	    if (dVR_BG != 0) 
    	    {
    		sumBG += dVR_BG;
    		if (highestBG < dVR_BG)
    		    highestBG = dVR_BG;
    		if (lowestBG > dVR_BG)
    		    lowestBG = dVR_BG;
    		counterBG++;
    	    }
    
    	    sumIns1 += dvr.getIns1();
    	    if (dvr.getIns1() != 0)
    		counterIns1++;
    
    	    sumIns2 += dvr.getIns2();
    	    if (dvr.getIns2() != 0)
    		counterIns2++;
    
    	    sumBE += dvr.getCH();
    	    if (dvr.getCH() != 0)
    		counterBE++;
    	}

    	if (debug)
    	System.out.println("date=" + date +
    			   "sumBG=" + sumBG + " (" + counterBG + ") " +
    			   "sumIns1=" + sumIns1 + " (" + counterIns1 + ") " +
    			   "sumIns2=" + sumIns2 + " (" + counterIns2 + ") ");

    }


    public void setColumnNames(String[] names)
    {
        column_names = names;
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
        return column_names.length;
    }


    public int getRowCount()
    {
        if (dataRows == null)
            return 0;
        else
            return dataRows.size();
    }


    public DailyValuesRow getRow(int index)
    {
        return this.dataRows.get(index);
    }

    public boolean hasDeletedItems()
    {
    	if (deleteList==null)
    	    return false;
    	else 
    	    return (deleteList.size()!=0);
    }

    public ArrayList<DayValueH> getDeletedItems()
    {
        return deleteList;
    }




    public Object getValueAt(int row, int column)
    {
        return getRow(row).getValueAt(column);
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        DailyValuesRow dVR = getRow(row);

	/*
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
        } */

        refreshStatData();
        dVR.setValueAt(aValue, column);

	/*
        highestBG = 0;
        lowestBG = Float.MAX_VALUE;
        for (int j = 0; j < dataRows.size(); j++) {
            dVR = (DailyValuesRow)dataRows.elementAt(j);
            highestBG = Math.max(dVR.getBG(), highestBG);
            lowestBG = Math.min(dVR.getBG(), lowestBG);
        }*/

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

    public long getDate()
    {
        return date;
    }

    public String getDateAsString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public long getDateTimeAt(int row)
    {
        return getRow(row).getDateTime();
    }


    public Date getDateTimeAsDateAt(int row)
    {
        return getRow(row).getDateTimeAsDate();
    }
/*
    public String getDateTimeAsTimeStringAt(int row)
    {
	return getRow(row).getD.getDateTimeAsTime();
    }
    */

    public String getDateTimeAsTimeStringAt(int row)
    {
	return getRow(row).getDateTimeAsTime();
    }


    public String getDateTimeAsStringAt(int row)
    {
        DailyValuesRow dv = getRow(row);
        return dv.getDateAsString() + " " + dv.getTimeAsString();
    }


    public int getDateD(int row)
    {
        return getRow(row).getDateD();
    }


    public int getDateT(int row)
    {
        return getRow(row).getDateT();
    }

    public boolean getChanged(int row)
    {
	return getRow(row).hasChanged();
    }


    public float getBGAt(int row)
    {
        return getRow(row).getBG();
    }

    public float getIns1At(int row)
    {
        return getRow(row).getIns1();
    }

    public float getIns2At(int row)
    {
        return getRow(row).getIns2();
    }

    public float getCHAt(int row)
    {
        return getRow(row).getCH();
    }

    public String getActivityAt(int row)
    {
        return getRow(row).getActivity();
    }

    public String getUrineAt(int row)
    {
        return getRow(row).getUrine();
    }


    public String getCommentAt(int row)
    {
        return getRow(row).getComment();
    }

    public String getColumnName(int column)
    {
        return column_names[column] == null ? m_ic.getMessage("NO_NAME") : column_names[column];
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

    public float getAvgCH()
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

    public float getSumCH()
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

    public int getCHCount()
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
        for (int i = 0; i < getRowCount(); i++) 
        {
            float bg = getBGAt(i);
            if (bg != 0) 
            {
                tmp += (bg - getAvgBG()) * (bg - getAvgBG());
                c++;
            }
        }
        if (--c > 0)
            return (float)Math.sqrt(tmp / c);
        else
            return 0;
    }

/*
    public int getDateD()
    {
	
	String dat = "";

	dat += date.getYear() + 1900;
	dat += getLeadingZero(date.getMonth() + 1, 2);
	dat += getLeadingZero(date.getDate(), 2);

	return Integer.parseInt(dat);

    }
*/
/*
    public int getDateT()
    {
	
	return date.getHours()*100 + date.getMinutes();
	
    }
*/



}
