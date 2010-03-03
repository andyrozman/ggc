package ggc.core.data;

import ggc.core.util.DataAccess;

import java.util.ArrayList;
import java.util.GregorianCalendar;

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
 *  Filename:     CollectionValues
 *  Description:  This file is data file for storing DailyValues, it stores all data 
 *                for a certain time period. 
 * 
 *  @author  tscherno
 *  @author  schultd
 *  @author  Andy {andy@atech-software.com}  
 */


public class CollectionValues extends DailyValues
{

    //private boolean debug = false;
    //private I18nControlAbstract m_ic = DataAccess.getInstance().getI18nControlInstance();

    private static final long serialVersionUID = 4415864395080066275L;

    ArrayList<DailyValuesRow> dataRows = new ArrayList<DailyValuesRow>();

    long date = 0;

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
    int count_days = 0;

    float highestBG = 0;
    float lowestBG = Float.MAX_VALUE;
    float stdDev = 0;

    boolean changed = false;

    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     * 
     * @param sDay 
     * @param eDay 
     */
    public CollectionValues(GregorianCalendar sDay, GregorianCalendar eDay)
    {
        super();
        setDayRange(sDay, eDay);
    }

    
    
    /**
     * Set Day Range
     * 
     * @param sDay
     * @param eDay
     */
    public void setDayRange(GregorianCalendar sDay, GregorianCalendar eDay)
    {
        //long diff = eDay.getTimeInMillis() - sDay.getTimeInMillis();
        
        
        
        ArrayList<DailyValuesRow> lst = DataAccess.getInstance().getDb().getDayValuesRange(sDay, eDay);

        for (int i=0; i<lst.size(); i++ ) 
        {
            this.addRow(lst.get(i));
        }

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
     * Get Sum CH Per Day
     * 
     * @return
     */
    public float getSumCHPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;
        
        return this.getSumCH()/this.day_count.size();
    }
    
    
    /**
     * Get CH Count Per Day
     * 
     * @return
     */
    public float getCHCountPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;

        return this.getCHCount()/this.day_count.size();
    }

    
    /**
     * Get Sum Bolus Per Day
     * 
     * @return
     */
    public float getSumBolusPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;

        return this.getSumBolus()/this.day_count.size();
    }
    

    /**
     * Get Bolus Count Per Day
     * 
     * @return
     */
    public float getBolusCountPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;

        return this.getBolusCount()/this.day_count.size();
    }
    
    
    /**
     * Get Sum Basal Per Day
     * 
     * @return
     */
    public float getSumBasalPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;

        return this.getSumBasal()/this.day_count.size();
    }

    
    /**
     * Get Basal Count Per Day
     * 
     * @return
     */
    public float getBasalCountPerDay()
    {
        if (this.day_count.size()==0)
            return 0.0f;

        return this.getBasalCount()/this.day_count.size();
    }
    
    
}
