package ggc.cgm.data;

import ggc.cgm.util.DataAccessCGM;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputUtil;

import java.util.ArrayList;
import java.util.Hashtable;

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
 *  Filename:     CGMValuesEntry.java
 *  Description:  Collection of CGMValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data. Trying to find a way to use super class instead of this.

public class CGMValuesEntry extends DeviceValuesEntry
{
	DataAccessCGM da = DataAccessCGM.getInstance();
	
	// pump 
	long datetime;
	int base_type;
	int sub_type;
	String value;
	String profile;
	
	// old
	String bg_str;
	int bg_unit;
	//public
	
	private Hashtable<String,String> params;
	//private static I18nControl ic = I18nControl.getInstance(); 
	
	//private String bg_original = null;
	//private OutputUtil util = OutputUtil.getInstance();
	
	
    
    //private DayValueH entry_object = null;
    
	
	
	
	/**
	 * Constructor
	 */
	public CGMValuesEntry()
	{
	    super();
	}
	
	
	
	
    /**
     * Get DateTime (long)
     * 
     * @return
     */
	public long getDateTime()
	{
		return this.datetime;
	}
	
	
    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.datetime);
    }
	
	
	
	
	
	
	
	
	/**
	 * Add Parameter
	 * 
	 * @param key
	 * @param value_in
	 */
	public void addParameter(String key, String value_in)
	{
		if ((value_in.equals("_")) || (value_in.trim().length()==0))
			return;
		
		if (params==null)
		{
			params = new Hashtable<String,String>();
		}
		
		this.params.put(key, value_in);
		
	}

	
	
	
	
	/**
	 * @return
	 */
	public String getParametersAsString()
	{
		if (this.params==null)
			return "";
		
		StringBuffer sb = new StringBuffer();
		
		for(java.util.Enumeration<String> en = this.params.keys(); en.hasMoreElements(); )
		{
			String key = en.nextElement();
			
			sb.append(key + "=" + this.params.get(key) + ";");
		}
		
		return sb.substring(0, sb.length()-1);
		
	}
	
	
	/**
	 * Prepare Entry
	 * 
	 * @see ggc.plugin.data.DeviceValuesEntry#prepareEntry()
	 */
	public void prepareEntry()
	{
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
	}
	
	
	
	
	
	/**
	 * Create Comment
	 * 
	 * @return
	 */
	public String createComment()
	{
	    String p = this.getParametersAsString();
	    
	    if ((p==null) || (p.trim().length()==0))
	    {
	        return "MTI";
	    }
	    else
	        return "MTI;" + p;
	    
	    
	}
	
	
	/**
	 * To String
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    //OutputUtil o= null;
	    return "CGMValuesEntry [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}

	
	/**
     * Get Column Value
     * 
     * @param index
     * @return
     */
    @Override
    public Object getColumnValue(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    /**
     * Get DateTime format
     * 
     * @return format of date time (precission)
     */
    @Override
    public int getDateTimeFormat()
    {
        return 0;
    }

    
    /**
     * Get Db Objects
     * 
     * @return ArrayList of elements extending GGCHibernateObject
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getDbObjects()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Get Data As String
     * 
     * @see ggc.plugin.output.OutputWriterData#getDataAsString()
     */
    @Override
    public String getDataAsString()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        // TODO Auto-generated method stub
    }
	
	
}	
