/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
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
 *  Filename: DailyValuesRow.java
 *  Purpose:  One row in the DailyValues Data Model.
 *
 *  Author:   Andy Rozman {andy@atech-software.com}
 */

package ggc.cgm.data;

import ggc.cgm.output.OutputUtil;
import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;
import ggc.core.db.hibernate.DayValueH;

import java.util.Hashtable;

import com.atech.utils.ATechDate;

//import ggc.db.hibernate.DayValueH;


public class CGMValuesEntry 
{
	DataAccessCGM da = DataAccessCGM.getInstance();
	
	// pump 
	long datetime;
	int base_type;
	int sub_type;
	String value;
	String profile;
	
	// old
	public String bg_str;
	public int bg_unit;
	public boolean checked = false;
	//public
	public Hashtable<String,String> params;
	public int status = 1; //MeterValuesEntry.
	public static I18nControl ic = I18nControl.getInstance(); 
	
	public String bg_original = null;
	public OutputUtil util = new OutputUtil();
	
	
	public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_NEW = 1;
    public static final int STATUS_CHANGED = 2;
    public static final int STATUS_OLD = 3;
	
	
    public static final int OBJECT_STATUS_NEW = 1;
    public static final int OBJECT_STATUS_EDIT = 2;
    public static final int OBJECT_STATUS_OLD =3;
    
    
    public int object_status = 0;
    
    public DayValueH entry_object = null;
    
	public static String entry_statuses[] = 
	{
	     CGMValuesEntry.ic.getMessage("UNKNOWN"),
	     CGMValuesEntry.ic.getMessage("NEW"),
	     CGMValuesEntry.ic.getMessage("CHANGED"),
	     CGMValuesEntry.ic.getMessage("OLD")
	};
	
    public static String entry_status_icons[] = 
    {
         "led_gray.gif",
         "led_green.gif",
         "led_yellow.gif",
         "led_red.gif"
    };
	
	
	public CGMValuesEntry()
	{
	}
	
	
	public void setDateTime(long dt)
	{
	    this.datetime = dt;
	}
	
	
	
	public long getDateTime()
	{
		return this.datetime;
	}
	
	
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.datetime);
    }
	
	
	
	public void setBaseType(int base_type)
	{
	    this.base_type = base_type;
	}
	
	
    public void setSubType(int sub_type)
    {
        this.sub_type = sub_type;
    }

    
    public void setValue(String val)
    {
        this.value = val;
    }
    
	
	public void setProfile(String profile)
	{
	    this.profile = profile;
	}
	
	
	
	
	public void addParameter(String key, String value)
	{
		if ((value.equals("_")) || (value.trim().length()==0))
			return;
		
		if (params==null)
		{
			params = new Hashtable<String,String>();
		}
		
		this.params.put(key, value);
		
	}

	
	public void setBgUnit(int bg_type)
	{
		this.bg_unit = bg_type;
	}

	public int getBgUnit()
	{
		return this.bg_unit;
	}
	
	
	public boolean getCheched()
	{
	    return this.checked;
	}

	public int getStatus()
	{
	    return this.status;
	}
	
	
	public void setBgValue(String value)
	{
		this.bg_str = value;
		
		if (this.bg_original==null)
		    this.setDisplayableBGValue(value);
	}
	
	public String getBgValue()
	{
		return this.bg_str;
	}
	
	public void setDisplayableBGValue(String value)
	{
	    bg_original = value; 
	}
	
	public String getBGValue(int st)
	{
	    if (this.bg_unit == OutputUtil.BG_MMOL)
	    {
	        if (st == OutputUtil.BG_MMOL)
	        {
	            return this.bg_original;
	        }
	        else
	        {
	            return "" + (int)(this.util.getBGValueDifferent(OutputUtil.BG_MMOL, Float.parseFloat(this.bg_original)));
	        }
	    }
	    else
	    {
            if (st == OutputUtil.BG_MGDL)
            {
                return this.bg_original;
            }
            else
            {
                return DataAccessCGM.MmolDecimalFormat.format((this.util.getBGValueDifferent(OutputUtil.BG_MGDL, Float.parseFloat(this.bg_original))));
            }
	        
	    }
	    
	}
	
	
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
	
	
	
	public DayValueH getDbObject()
	{
	    return this.entry_object;
	}
	
	
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
	
	
	public String toString()
	{
	    //OutputUtil o= null;
	    return "MeterValuesEntry [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}
	
	
}	
