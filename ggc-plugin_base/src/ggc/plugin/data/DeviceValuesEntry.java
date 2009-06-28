package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;

import java.util.ArrayList;

import org.hibernate.Session;

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

public abstract class DeviceValuesEntry implements DeviceValuesEntryInterface  //extends OutputWriterData implements Comparator<DeviceValuesEntry>, Comparable<DeviceValuesEntry>, StatisticsItem
{
	protected boolean checked = false;
	protected int status = 1; 
    protected int output_type = 0;
    protected boolean is_bg = false;
	
	
	
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
    public int object_status = 0;
    
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
	 * Constructor
	 */
	public DeviceValuesEntry()
	{
	}
	
	
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
	 * Get Checked 
	 * 
	 * @return true if element is checked
	 */
	public boolean getChecked()
	{
	    return this.checked;
	}

	
    /**
     * Set Checked
     * 
     * @param check true if element is checked
     */
    public void setChecked(boolean check)
    {
        this.checked = check;
    }
	
	
	/**
	 * Get Status
	 * 
	 * @return status
	 */
	public int getStatus()
	{
	    return this.status;
	}
	
	
    /**
     * Set Status
     * 
     * @param status_in
     */
    public void setStatus(int status_in)
    {
        this.status = status_in;
    }
    
	
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
	 * Set Output Type
	 * 
	 * @see ggc.plugin.output.OutputWriterData#setOutputType(int)
	 */
	
    public void setOutputType(int type)
    {
	    this.output_type = type;
    }
	
	
    /**
     * Is Data BG
     * 
     * @see ggc.plugin.output.OutputWriterData#isDataBG()
     */
    
    public boolean isDataBG()
    {
        return this.is_bg; 
    }


    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compare(DeviceValuesEntryInterface d1, DeviceValuesEntryInterface d2)
    {
        return (int)(d1.getDateTime()-d2.getDateTime());
    }

    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(DeviceValuesEntryInterface d2)
    {
        return (int)(this.getDateTime()-d2.getDateTime());
    }
    
	
    /**
     * Set Object status
     * 
     * @param status
     */
    public void setObjectStatus(int status)
    {
        this.object_status = status;
    }
    
    
    /**
     * Get Object Status
     * 
     * @return
     */
    public int getObjectStatus()
    {
        return this.object_status;
    }
    
    

    
    
    
    
    
}	
