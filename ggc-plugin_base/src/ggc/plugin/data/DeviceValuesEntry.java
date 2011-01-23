package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.output.OutputWriterData;
import ggc.plugin.util.DeviceValuesEntryUtil;

import java.util.ArrayList;

import com.atech.misc.statistics.StatisticsItem;
import com.atech.utils.data.ATechDate;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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

public abstract class DeviceValuesEntry implements DeviceValuesEntryInterface, OutputWriterData, StatisticsItem  //extends OutputWriterData implements Comparator<DeviceValuesEntry>, Comparable<DeviceValuesEntry>, StatisticsItem
{
	protected boolean checked = false;
	protected int status = 1; 
    protected int output_type = 0;
    protected boolean is_bg = false;
    private int multiline_tooltip_type = 1;
	
	
	
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
     * Prepare Entry [Framework v1]
     */
    public void prepareEntry()
    {
    }

    
    /**
     * Prepare Entry [Framework v2]
     */
    public void prepareEntry_v2()
    {
    }
    
    
    /**
     * Get Db Objects
     * 
     * @return ArrayList of elements extending GGCHibernateObject
     */
    public abstract ArrayList<? extends GGCHibernateObject> getDbObjects();
	
	
	
	
	
	/**
	 * Get Column Value
	 * 
	 * @param index
	 * @return
	 */
	public abstract Object getColumnValue(int index);
	

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
        return DeviceValuesEntryUtil.compare(d1, d2);
    }

    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(DeviceValuesEntryInterface d2)
    {
        return DeviceValuesEntryUtil.compare(this, d2);
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
    
    

    /**
     * Has MultiLine Tooltip
     * 
     * @return
     */
    public boolean hasMultiLineToolTip()
    {
        return false;
    }
    
    
    /**
     * Set MultiLine Tooltip Type
     * 
     * @param _multiline_tooltip_type
     */
    public void setMultiLineTooltipType(int _multiline_tooltip_type)
    {
        this.multiline_tooltip_type = _multiline_tooltip_type;
    }

    
    /**
     * Get MultiLine Tooltip Type
     * @return 
     */
    public int getMultiLineTooltipType()
    {
        return this.multiline_tooltip_type;
    }
    
    
    /**
     * Checks if is indexed.
     * 
     * @return true, if is indexed
     */
    public boolean isIndexed()
    {
        return false;
    }

    
    /**
     * Gets the multi line tool tip.
     * 
     * @return the multi line tool tip
     */
    public String getMultiLineToolTip()
    {
        return "";
    }

    
    /**
     * Gets the multi line tool tip.
     * 
     * @param index the index
     * 
     * @return the multi line tool tip
     */
    public String getMultiLineToolTip(int index)
    {
        return "";
    }
    
    
    
}	
