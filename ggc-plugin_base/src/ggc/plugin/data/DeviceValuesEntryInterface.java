package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.output.OutputWriterData;

import java.util.ArrayList;
import java.util.Comparator;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.utils.ATechDate;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     DeviceValuesEntry
 *  Description:  Collection of DeviceValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data. Trying to find a way to use super class instead of this.

public interface DeviceValuesEntryInterface extends OutputWriterData,    /*(OutputWriter,*/ 
                                                    Comparator<DeviceValuesEntryInterface>, 
                                                    Comparable<DeviceValuesEntryInterface>, 
                                                    //StatisticsItem, 
                                                    DatabaseObjectHibernate
{
	//public boolean checked = false;
	//public int status = 1; 
    //public int output_type = 0;
    //public boolean is_bg = false;
	
	
	
	/**
     * Status: Unknown
	 */
	public static final int STATUS_UNKNOWN = 0;
	
    /**
     * Status: New
     */
    public static final int STATUS_NEW = 1;
    
    /**
     * Status: Changed
     */
    public static final int STATUS_CHANGED = 2;
    
    /**
     * Status: Old
     */
    public static final int STATUS_OLD = 3;
	
	
    /**
     *  Object Status: New
     */
    public static final int OBJECT_STATUS_NEW = 1;
    
    /**
     *  Object Status: Edit
     */
    public static final int OBJECT_STATUS_EDIT = 2;
    
    /**
     *  Object Status: Old (existing)
     */
    public static final int OBJECT_STATUS_OLD =3;
    
    
    /**
     * Object status
     */
    //public int object_status = 0;
    
    /*
	private static String entry_statuses[] = 
	{
	     "UNKNOWN",
	     "NEW",
	     "CHANGED",
	     "OLD"
	};*/
	
    /**
     * Entry Status Icons 
     */
    public static String entry_status_icons[] = 
    {
         "led_gray.gif",
         "led_green.gif",
         "led_yellow.gif",
         "led_red.gif"
    };
	
	
	
	
	
	/**
	 * Get DateTime (long)
	 * 
	 * @return
	 */
	public abstract long getDateTime();
	
	
    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    public abstract void setDateTimeObject(ATechDate dt);
    
    
    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    public abstract ATechDate getDateTimeObject();
    

	/**
	 * Get DateTime format
	 * 
	 * @return format of date time (precission)
	 */
	public abstract int getDateTimeFormat();
	
	
	/**
	 * Get Column Value
	 * 
	 * @param index
	 * @return
	 */
	public abstract Object getColumnValue(int index);
	

    /**
     * Get Table Column Value (in case that we need special display values for download data table, this method 
     * can be used, if it's the same as getColumnValue, we can just call that one. 
     * 
     * @param index
     * @return
     */
    public abstract Object getTableColumnValue(int index);
	
	
	/**
	 * Get Checked 
	 * 
	 * @return true if element is checked
	 */
	public boolean getChecked();

	
    /**
     * Set Checked
     * 
     * @param check true if element is checked
     */
    public void setChecked(boolean check);
	
	
	/**
	 * Get Status
	 * 
	 * @return status
	 */
	public int getStatus();
	
	
    /**
     * Set Status
     * 
     * @param status_in
     */
    public void setStatus(int status_in);
    
	
	/**
	 * Prepare Entry
	 */
	public abstract void prepareEntry();
	
	
	/**
	 * Get Db Objects
	 * 
	 * @return ArrayList of elements extending GGCHibernateObject
	 */
	public abstract ArrayList<? extends GGCHibernateObject> getDbObjects();
	
	
	/**
	 * Get DeviceValuesEntry Name
	 * 
	 * @return
	 */
	public abstract String getDVEName();
	
	
	/**
	 * Get Value of object
	 * 
	 * @return
	 */
	public abstract String getValue();
	
	
	/**
	 * Set Output Type
	 * 
	 * @see ggc.plugin.output.OutputWriterData#setOutputType(int)
	 */
	
    public void setOutputType(int type);
	
	
    /**
     * Is Data BG
     * @return 
     * 
     * @see ggc.plugin.output.OutputWriterData#isDataBG()
     */
    public boolean isDataBG();


    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compare(DeviceValuesEntryInterface d1, DeviceValuesEntryInterface d2);

    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(DeviceValuesEntryInterface d2);
    
	
    /**
     * Set Object status
     * 
     * @param status
     */
    public void setObjectStatus(int status);
    
    
    /**
     * Get Object Status
     * 
     * @return
     */
    public int getObjectStatus();
    
    
    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId();
    
}	
