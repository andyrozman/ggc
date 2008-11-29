package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.output.OutputWriterData;

import java.util.ArrayList;


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
 *  Filename:     CGMValuesEntry.java
 *  Description:  Collection of CGMValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data. Trying to find a way to use super class instead of this.

public abstract class DeviceValuesEntry extends OutputWriterData
{
	protected boolean checked = false;
	protected int status = 1; 
	
	
	
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
	
	
	public abstract long getDateTime();
	

	public abstract int getDateTimeFormat();
	
	
	public abstract Object getColumnValue(int index);
	
	
	public boolean getChecked()
	{
	    return this.checked;
	}

	
    public void setChecked(boolean check)
    {
        this.checked = check;
    }
	
	
	public int getStatus()
	{
	    return this.status;
	}
	
	
    public void setStatus(int status_in)
    {
        this.status = status_in;
    }
    
	
	public abstract void prepareEntry();
	
	public abstract ArrayList<? extends GGCHibernateObject> getDbObjects();
	
	
	//public void prepareEntry()
	//{
	    /*
	    if (this.object_status == PumpValuesEntry.OBJECT_STATUS_OLD)
	        return;
	    else if (this.object_status == PumpValuesEntry.OBJECT_STATUS_EDIT)
	    {
	        this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
	        this.entry_object.setChanged(System.currentTimeMillis());
	        this.entry_object.setComment(createComment());
	    }
	    else
	    {
	        this.entry_object = new DayValueH();
	        this.entry_object.setIns1(0);
            this.entry_object.setIns2(0);
            this.entry_object.setCh(0.0f);
            this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
	        this.entry_object.setDt_info(this.datetime);
            this.entry_object.setChanged(System.currentTimeMillis());
            this.entry_object.setComment(createComment());
	    }*/
	//}
	
	
	
	
	
	
}	
