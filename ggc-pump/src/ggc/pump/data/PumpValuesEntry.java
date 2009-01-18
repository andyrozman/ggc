package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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


public class PumpValuesEntry extends DeviceValuesEntry 
//extends PumpDataH implements DatabaseObjectHibernate   // extends PumpValuesEntryAbstract
{

    private static final long serialVersionUID = -2047203215269156938L;

    DataAccessPump m_da = null; //DataAccessPump.getInstance();
	
	// pump 
	ATechDate datetime;
	int base_type;
	int sub_type;
	String value;
	String profile;
	
	// old
	//public String bg_str;
	//public int bg_unit;
	//public boolean checked = false;
	
	
	//private int bg;
	//private float ch;
	//public String meals;
	
	//public
	private Hashtable<String,String> params;
	//public int status = 1; //MeterValuesEntry.
	//private static I18nControl ic = I18nControl.getInstance(); 
	
	//public String bg_original = null;
	//private OutputUtil util = OutputUtil.getInstance();
	
	/*
	public static final int STATUS_UNKNOWN = 0;
    public static final int STATUS_NEW = 1;
    public static final int STATUS_CHANGED = 2;
    public static final int STATUS_OLD = 3;
	
	
    public static final int OBJECT_STATUS_NEW = 1;
    public static final int OBJECT_STATUS_EDIT = 2;
    public static final int OBJECT_STATUS_OLD =3;
    */
    
    //public int object_status = 0;
    
    private PumpDataH entry_object = null;
    private Hashtable<String,PumpValuesEntryExt> additional_data = new Hashtable<String,PumpValuesEntryExt>(); 
    
    /*
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
    };*/
	

    /**
     * Constructor
     * 
     * @param tr
     */
    public PumpValuesEntry(boolean tr)
    {
    }

	
	/**
	 * 
	 */
	public PumpValuesEntry()
	{
	    this.entry_object = new PumpDataH();
        m_da = DataAccessPump.getInstance();
	}


    /**
     * Constructor
     * 
     * @param base_type
     */
    public PumpValuesEntry(int base_type)
    {
        this.entry_object = new PumpDataH();
        m_da = DataAccessPump.getInstance();
    }

	
	/**
     * Constructor
     * 
	 * @param pdh
	 */
	public PumpValuesEntry(PumpDataH pdh)
	{
	    this.entry_object = pdh;
        m_da = DataAccessPump.getInstance();
	}
	
	
	/**
	 * Add Additional Data
	 * 
	 * @param adv
	 */
	public void addAdditionalData(PumpValuesEntryExt adv)
	{
	    this.additional_data.put(new PumpAdditionalDataType().getTypeDescription(adv.getType()), adv);
	}
	
	/**
	 * Get Additional Data
	 * 
	 * @return
	 */
	public Hashtable<String,PumpValuesEntryExt> getAdditionalData()
	{
	    return this.additional_data; 
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
	
	/*
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.datetime);
    }*/
	
	
	/*
	public void setBaseType(int base_type)
	{
	    this.base_type = base_type;
	}*/
	
	
    /**
     * Set Sub Type
     * 
     * @param sub_type
     */
    public void setSubType(int sub_type)
    {
        this.sub_type = sub_type;
    }

    
    /**
     * Set Value
     * 
     * @param val
     */
    public void setValue(String val)
    {
        this.value = val;
    }
    
	
	/**
	 * Set Profile
	 * 
	 * @param profile
	 */
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
    
    
    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        // TODO: 
        return "";
    }
    
    
    
    // added - End
    
    /**
     * Add Parameter
     * 
     * @param key
     * @param valuex
     */
	public void addParameter(String key, String valuex)
	{
		if ((valuex.equals("_")) || (valuex.trim().length()==0))
			return;
		
		if (params==null)
		{
			params = new Hashtable<String,String>();
		}
		
		this.params.put(key, valuex);
		
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
	
	/*
	public boolean getCheched()
	{
	    return this.checked;
	}

	public int getStatus()
	{
	    return this.status;
	}*/
	
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
	
	
	//int base_type;
	
	/**
     * Get Base Type
     *
	 * @return
	 */
	public int getBaseType()
	{
	   return this.base_type;
	}
	
    /**
     * Set Base Type
     * @param type
     */
    public void setBaseType(int type)
    {
       this.base_type = type;
    }
	
	
	/**
	 * Get Column Value
	 */
	public Object getColumnValue(int column)
	{
	    switch(column)
	    {
    	    case 0: // time
    	    {
    	        return this.datetime.getTimeString();
    	    }
    	    case 1: // type
    	    {
    	        return this.m_da.getPumpBaseTypes().basetype_desc[this.base_type];
    	    }
    	    case 2: // subtype
    	    {
    	        return getSubType();
    	    }
    	    case 3: // value
    	    {
    	        return this.entry_object.getValue();
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
	
	
	/**
	 * Get Parameters As String
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
	 * Get Sub Type
	 * 
	 * @return
	 */
	public String getSubType()
	{
	    if (this.entry_object.getSub_type()==0)
	        return "";
	    
	    if (this.entry_object.getBase_type()==PumpBaseType.PUMP_DATA_BASAL)
	    {
	        return m_da.getBasalSubTypes().basal_desc[this.entry_object.getSub_type()];
	    }
	    else if (this.entry_object.getBase_type()==PumpBaseType.PUMP_DATA_BOLUS)
	    {
            return m_da.getBolusSubTypes().bolus_desc[this.entry_object.getSub_type()];
	    }
	    else if (this.entry_object.getBase_type()==PumpBaseType.PUMP_DATA_REPORT)
	    {
            return m_da.getPumpReportTypes().report_desc[this.entry_object.getSub_type()];
	    }
	    else 
	    {
	        return "";
	    }
	}
	
	
	/**
	 * Get Additional Display
	 * @return
	 */
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
	
	/**
     * Prepare Entry
     */
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
	
	
	
    /**
     * Get Db Objects
     * 
     * @return ArrayList of elements extending GGCHibernateObject
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getDbObjects()
    {
        prepareEntry();
        ArrayList<GGCHibernateObject> lst = new ArrayList<GGCHibernateObject>();
        
        if (this.entry_object!=null)
            lst.add(this.entry_object);
        
        return lst;
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
	        return "";
	    }
	    else
	        return p;
	    
	    
	}
	
	
	/**
	 * To String
	 */
	public String toString()
	{
	    //OutputUtil o= null;
	    //return "PumpValuesEntry [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]";
	    return "PumpValuesEntry [date/time=" + this.datetime  + " ?????? ]";
	}

/*
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

*/
    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "" + this.entry_object.getId();
    }

    
    /**
     * Get DateTime (long)
     * 
     * @return
     */
    @Override
    public long getDateTime()
    {
        return this.datetime.getATDateTimeAsLong();    
    }

    
    /**
     * Get DateTime format
     * 
     * @return format of date time (precission)
     */
    @Override
    public int getDateTimeFormat()
    {
        return ATechDate.FORMAT_DATE_AND_TIME_S;
    }


    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt;        
    }

    /**
     * Get Data As String
     */
    @Override
    public String getDataAsString()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    @Override
    public ATechDate getDateTimeObject()
    {
        return this.datetime;
    }
	
	
}	
