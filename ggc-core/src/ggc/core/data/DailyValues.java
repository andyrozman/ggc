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
import java.util.ArrayList;
import java.util.Collections;

import com.atech.utils.ATechDate;

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
        //this.date = new Date(date);
        this.date = date;
    }

    /*
    public void setDate(Date date)
    {
        this.date = date;
    }*/

    /*
    public void saveDay()
    {
        //dbH.saveDayStats(this);
    }*/

    /*
    public void setNewRow(DailyValuesRow dVR)
    {
        addRow(dVR);
    }*/

    /**
     * Set Std Dev
     * @param stdDev
     */
    public void setStdDev(float stdDev)
    {
        this.stdDev = stdDev;
    }
    

/*    public void setNewRow(DailyValuesRow dVR, boolean _tru)
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
                    if (this.getRow(i).getDateTime() < time) 
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
    
        bOnlyInsert = false;

        refreshStatData();

    }
*/


    /**
     * Add Row
     * 
     * @param dVR
     */
    public void addRow(DailyValuesRow dVR)
    {
        dataRows.add(dVR);
        this.sort();

        /*
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
    
    	    // TODO: sorting
    	    
    	    int i = 0;
    	    boolean added = false;
    
    	    for ( ; i < size; i++)
    	    {
        		if (this.getRow(i).getDateTime() > time) 
        		{
        		    dataRows.add(i, dVR);
        		    added = true;
        		    break;
        		}
    	    }
    
    	    if (!added)
    	        dataRows.add(i, dVR);
    
    	}*/
    
    	bOnlyInsert = false;
    	refreshStatData();

    }



    


/*
    public DailyValuesRow getRowAt(int i)
    {
        return getRow(i);
    }
*/


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


    /**
     * Refresh Stat Data
     */
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
    
    	    float dVR_BG = dvr.getBGRaw();
    	    //float dVR_BG = dvr.getBG();
    	    
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

/*
    public void setColumnNames(String[] names)
    {
        column_names = names;
    }
*/

    /**
     * Has Changed
     * 
     * @return
     */
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
    	if (deleteList==null)
    	    return false;
    	else 
    	    return (deleteList.size()!=0);
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

    /*
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
/*
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
/*
        bHasChangedValues = true;
        bOnlyInsert = false;
    }
*/
    
    /**
     * Set IsNew
     * 
     * @param b
     */
    public void setIsNew(boolean b)
    {
        bOnlyInsert = b;
    }

/*    public boolean onlyInsert()
    {
        return bOnlyInsert;
    }
*/
    
    /**
     * Get Day And Month As String
     *  
     * @return
     */
    public String getDayAndMonthAsString()
    {
//        return getDateAsLocalizedString();
        
        int day, month;
        day = (int) (date % 100);
        month = ((int) (date % 10000))/100;
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
        ATechDate at = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, this.date); //.getGregorianCalendar(), 4);
        System.out.println("Date: (getDate): " + this.getDate() + " , atechdate=" + at.getDateString());
        //return at.toString();
        return m_da.getAsLocalizedDateString((new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, this.date)).getGregorianCalendar(), 4);
    }
    
    
    
    /**
     * Get Date As String
     * 
     * @return
     */
    public String getDateAsString()
    {
        return getDateAsLocalizedString();
        /*
        int day, month, year;
        day = (int) (date % 100);
        month = ((int) (date % 10000))/100;
        year = (int) (date / 10000);
        return String.format("%1$04d-%2$02d-%3$02d", year, month, day); */
    }


    /*
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

    /*
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
*/
    
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

/*
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
*/

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
            if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
                return sumBG / counterBG;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, (sumBG / counterBG));
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
        if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
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
            if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL)
                return lowestBG;
            else
                return m_da.getBGValueDifferent(DataAccess.BG_MGDL, lowestBG);
        }
        else
            return 0;
        
        /*
        if (lowestBG != Float.MAX_VALUE)
            return lowestBG;
        else
            return 0; */
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

    
    /**
     * Sort 
     */
    public void sort()
    {
        Collections.sort(this.dataRows);
    }
    


}
