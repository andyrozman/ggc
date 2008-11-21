package ggc.pump.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.plugin.output.OutputUtil;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.Enumeration;
import java.util.Hashtable;

import org.hibernate.Session;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.utils.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpValuesEntry extends PumpDataH implements DatabaseObjectHibernate   // extends PumpValuesEntryAbstract
{

    private static final long serialVersionUID = -2047203215269156938L;

    DataAccessPump m_da = DataAccessPump.getInstance();
	
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
	
	
	//private int bg;
	//private float ch;
	//public String meals;
	
	//public
	public Hashtable<String,String> params;
	public int status = 1; //MeterValuesEntry.
	public static I18nControl ic = I18nControl.getInstance(); 
	
	//public String bg_original = null;
	public OutputUtil util = OutputUtil.getInstance();
	
	
	public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_NEW = 1;
    public static final int STATUS_CHANGED = 2;
    public static final int STATUS_OLD = 3;
	
	
    public static final int OBJECT_STATUS_NEW = 1;
    public static final int OBJECT_STATUS_EDIT = 2;
    public static final int OBJECT_STATUS_OLD =3;
    
    
    public int object_status = 0;
    
    public DayValueH entry_object = null;
    
    public Hashtable<String,PumpValuesEntryExt> additional_data = new Hashtable<String,PumpValuesEntryExt>(); 
    
    
	public static String entry_statuses[] = 
	{
	     PumpValuesEntry.ic.getMessage("UNKNOWN"),
         PumpValuesEntry.ic.getMessage("NEW"),
         PumpValuesEntry.ic.getMessage("CHANGED"),
         PumpValuesEntry.ic.getMessage("OLD")
	};
	
    public static String entry_status_icons[] = 
    {
         "led_gray.gif",
         "led_green.gif",
         "led_yellow.gif",
         "led_red.gif"
    };
	
	
	public PumpValuesEntry()
	{
	}
	
	
	public void addAdditionalData(PumpValuesEntryExt adv)
	{
	    this.additional_data.put(new PumpAdditionalDataType().getTypeDescription(adv.getType()), adv);
	}
	
	/*
	public void setDateTime(long dt)
	{
	    this.datetime = dt;
	}
	
	
	
	public long getDateTime()
	{
		return this.datetime;
	}*/
	
	
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.datetime);
    }
	
	
	/*
	public void setBaseType(int base_type)
	{
	    this.base_type = base_type;
	}
	
	
    public void setSubType(int sub_type)
    {
        this.sub_type = sub_type;
    }*/

    
    public void setValue(String val)
    {
        this.value = val;
    }
    
	
	public void setProfile(String profile)
	{
	    this.profile = profile;
	}
	
	
	// added
	/*
	public float getBG()
	{
	    return m_da.getBGValueInSelectedFormat(this.bg);
	}
	
	
    public float getBGRaw()
    {
        return this.bg;
    }
	
    public float getCH()
    {
        return this.ch;
    }
    
    public String getActivity()
    {
        // TODO: 
        return "";
    }
    
    public String getUrine()
    {
        // TODO: 
        return "";
    }*/
    
    
    public String getComment()
    {
        // TODO: 
        return "";
    }
    
    public boolean areMealsSet()
    {
        return false;
    }
    
    
    // added - End
    
	
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

	/*
	public void setBgUnit(int bg_type)
	{
		this.bg_unit = bg_type;
	}

	public int getBgUnit()
	{
		return this.bg_unit;
	}*/
	
	
	public boolean getCheched()
	{
	    return this.checked;
	}

	public int getStatus()
	{
	    return this.status;
	}
	
	/*
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
                return DataAccessPump.MmolDecimalFormat.format((this.util.getBGValueDifferent(OutputUtil.BG_MGDL, Float.parseFloat(this.bg_original))));
            }
	        
	    }
	    
	}
	*/
	public Object getColumnValue(int column)
	{
	    switch(column)
	    {
    	    case 0: // time
    	    {
    	        return ATechDate.getTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.getDt_info());
    	    }
    	    case 1: // type
    	    {
    	        return this.m_da.getPumpBaseType().basetype_desc[this.getBase_type()];
    	    }
    	    case 2: // subtype
    	    {
    	        return getSubType();
    	    }
    	    case 3: // value
    	    {
    	        return this.getValue();
    	    }
    	    case 4: // additional
    	    {
    	        return this.getAdditionalDisplay();
    	    }
    	    case 5: // comment
    	    {
    	        return this.getComment();
    	    }
	    }
	    return "";
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
	
	public String getSubType()
	{
	    if (this.getSub_type()==0)
	        return "";
	    
	    if (this.getBase_type()==PumpBaseType.PUMP_DATA_BASAL)
	    {
	        return m_da.getBasalSubType().basal_desc[this.getSub_type()];
	    }
	    else if (this.getBase_type()==PumpBaseType.PUMP_DATA_BOLUS)
	    {
            return m_da.getBolusSubType().bolus_desc[this.getSub_type()];
	    }
	    else if (this.getBase_type()==PumpBaseType.PUMP_DATA_REPORT)
	    {
            return m_da.getPumpReportTypes().report_desc[this.getSub_type()];
	    }
	    else 
	    {
	        return "";
	    }
	}
	
	
	public String getAdditionalDisplay()
	{
	    if (this.additional_data.size()==0)
	        return "";
	    else
	    {
	        StringBuffer sb = new StringBuffer();
	        int i=0;
	        
	        for(Enumeration<String> en=this.additional_data.keys(); en.hasMoreElements(); i++ )
	        {
	            String key = en.nextElement();
	            
	            if (i>0)
	                sb.append(";");
	            
	            //sb.append(key + "=" + this.additional_data.get(key).toString());
	            sb.append(this.additional_data.get(key).toString());
	            
	        }
	        
	        return sb.toString();
	    }
	}
	
	
	public void prepareEntry()
	{
	    System.out.println("prepareEntry not implemented!");
	    
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
	        return "";
	    }
	    else
	        return p;
	    
	    
	}
	
	
	public String toString()
	{
	    //OutputUtil o= null;
	    return "PumpValuesEntry [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}


    public String DbAdd(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean DbDelete(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbEdit(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbHasChildren(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public int getAction()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getObjectName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean isDebugMode()
    {
        // TODO Auto-generated method stub
        return false;
    }


    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "" + this.getId();
    }
	
	
}	
