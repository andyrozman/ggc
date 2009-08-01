package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriterType;

import java.util.ArrayList;
import java.util.Hashtable;

import org.hibernate.Session;

import com.atech.utils.ATechDate;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:      MeterValuesEntry  
 *  Description:   One entry in table of values
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MeterValuesEntry extends DeviceValuesEntry //extends OutputWriterData
{
	DataAccessMeter da = DataAccessMeter.getInstance();
	private ATechDate datetime;
	private String bg_str;
	private int bg_unit;
	//public boolean checked = false;
	//public
	private Hashtable<String,String> params;
	//public int status = 1; //MeterValuesEntry.
	//private static I18nControl ic = I18nControl.getInstance(); 
	
	private String bg_original = null;
	private OutputUtil util = OutputUtil.getInstance();
	private String value_db = null;
	
	
    /**
     * Entry object
     */
    public DayValueH entry_object = null;
    
	/*
    public static String entry_status_icons[] = 
    {
         "led_gray.gif",
         "led_green.gif",
         "led_yellow.gif",
         "led_red.gif"
    };*/
	
	
    
    /**
     * Special Entry (if it is)
     */
    public boolean special_entry = false;

    /**
     * Special Entry Id
     */
    public int special_entry_id = -1;

    /**
     * Special Entry Value
     */
    public String special_entry_value = null;
    
    
    
	/**
	 * Constructor
	 */
	public MeterValuesEntry()
	{
	    super();
	}

	
    /**
     * Constructor
     * @param dv 
     */
    public MeterValuesEntry(DayValueH dv)
    {
        super();
        this.datetime = new ATechDate(this.getDateTimeFormat(), dv.getDt_info());
        this.value_db = "" + dv.getBg();
        this.bg_original = "" + dv.getBg();
        this.bg_unit = DataAccessMeter.BG_MGDL;
        this.entry_object = dv;
        this.object_status = MeterValuesEntry.OBJECT_STATUS_OLD;
    }
	
	
    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt;
    }
    
    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    public ATechDate getDateTimeObject()
    {
        return this.datetime;
    }
    

    /**
     * Get DateTime (long)
     * 
     * @return
     */
    public long getDateTime()
    {
        return this.datetime.getATDateTimeAsLong();
    }
    
	
	
	
	/**
	 * Add Parameter
	 * 
	 * @param key
	 * @param value
	 */
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

	
	/**
	 * Set Bg Unit
	 * 
	 * @param bg_type
	 */
	public void setBgUnit(int bg_type)
	{
		this.bg_unit = bg_type;
	}

	/**
	 * Get Bg Unit
	 * 
	 * @return
	 */
	public int getBgUnit()
	{
		return this.bg_unit;
	}
	
	
	
	/**
	 * Set Bg Value (String)
	 * 
	 * @param value as string
	 */
	public void setBgValue(String value)
	{
		this.bg_str = value;
		
		// set value_db for comparing values, v2
		if (this.bg_unit==OutputUtil.BG_MGDL)
		{
		    this.value_db = value;
		}
		else
		{
		    this.value_db = "" + ((int)this.util.getBGValueDifferent(OutputUtil.BG_MMOL, Float.parseFloat(value)));
		}
		
		
		if (this.bg_original==null)
		    this.setDisplayableBGValue(value);
	}
	
	/**
	 * Get Bg Value (String)
	 * @return
	 */
	public String getBgValue()
	{
		return this.bg_str;
	}
	
	/**
	 * Set Displayable Bg Value
	 * @param value
	 */
	public void setDisplayableBGValue(String value)
	{
	    bg_original = value; 
	}
	
	/**
	 * Get Bg Value by Type
	 * @param st
	 * @return
	 */
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
                return DataAccessMeter.Decimal1Format.format((this.util.getBGValueDifferent(OutputUtil.BG_MGDL, Float.parseFloat(this.bg_original))));
            }
	        
	    }
	    
	}

	
	/**
	 * Special Entry: Urine - Ketones (mmol/L)
	 */
	public static final int SPECIAL_ENTRY_URINE_MMOLL = 1;

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
	public static final int SPECIAL_ENTRY_URINE_MGDL = 2;
	
	
	/**
	 * Set Special Entry
	 * 
	 * @param type
	 * @param value
	 */
	public void setSpecialEntry(int type, String value)
	{
	    this.special_entry = true;
	    this.special_entry_id = type;
	    this.special_entry_value = value;
	}
	
	
	String special_entry_tags[] = { "", "URINE", "URINE" };
    String special_entry_units[] = { "", " mmol/L", " mg/dL" };  // this are not required, if your special 
                                                                 // entry has no unit, leave this empty
	
	/**
	 * Get Special Entry DbEntry
	 * @return
	 */
	public String getSpecialEntryDbEntry()
	{
	    StringBuffer sb = new StringBuffer();
	    
	    
	    sb.append(this.special_entry_tags[this.special_entry_id]);
	    sb.append("=");
	    sb.append(this.special_entry_value);
        sb.append(this.special_entry_units[this.special_entry_id]);
	    
	    return sb.toString();
	}
	
	
	
	
	
	
	/**
	 * Get Parameter as String
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
     */
	public void prepareEntry()
	{
	    if (this.object_status == MeterValuesEntry.OBJECT_STATUS_OLD)
	        return;
	    else if (this.object_status == MeterValuesEntry.OBJECT_STATUS_EDIT)
	    {
	        if (this.special_entry)
	            this.entry_object.setExtended(this.getSpecialEntryDbEntry());
	        else
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
            
            if (this.special_entry)
                this.entry_object.setExtended(this.getSpecialEntryDbEntry()+";" + "SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            else
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
                this.entry_object.setExtended("SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            }
            
            
            
            //this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
	        this.entry_object.setDt_info(this.getDateTime());
            this.entry_object.setChanged(System.currentTimeMillis());
            //this.entry_object.setExtended("SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            this.entry_object.setComment(createComment());
	    }
	}

	
	
	/**
	 * This is used just for compliance with old Meter code. This method is deprecated, but since Meter Tool
	 * is still not fully switched over to Framework v2, we need this method.
	 *  
	 * @deprecated
	 * @return
	 */
	public DayValueH getHibernateObject()
	{
	    return this.entry_object;
	}
	
	
	/*
	public DayValueH getDbObject()
	{
	    return this.entry_object;
	}*/
	
	
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
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
	    //OutputUtil o= null;
	    return "MeterValuesEntry [date/time=" + this.datetime.getDateTimeString() + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}


	/**
	 * Get Data As String (for non-db exports)
	 */
    
    public String getDataAsString()
    {
        switch(output_type)
        {
            case OutputWriterType.DUMMY:
                return "";
                
            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
            {
                if (this.special_entry)
                    return this.getDateTimeObject().getDateTimeString() + "  Special: " + this.getSpecialEntryDbEntry();
                else
                    return this.getDateTimeObject().getDateTimeString() + "  BG: " + this.getBgValue() + " " + OutputUtil.getBGTypeNameStatic(this.getBgUnit());
            }
                
            case OutputWriterType.GGC_FILE_EXPORT:
            {
                int val = 0;
                
                if (this.getBgUnit() == OutputUtil.BG_MMOL)
                {
                    float fl = Float.parseFloat(this.getBgValue());
                    val = (int)OutputUtil.getInstance().getBGValueDifferent(this.getBgUnit(), fl);
                    
                }
                else
                {
                    try
                    {
                        val = Integer.parseInt(this.getBgValue());
                    }
                    catch(Exception ex)
                    {
                        val = 0;
                    }
                }

                String parameters = this.getParametersAsString();
                
                /*
                if (parameters.equals(""))
                    System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
                else
                    System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()) + " Params: " + parameters );
                */
                return "0|" + this.getDateTime() + "|" + val + 
                            "|0.0|0.0|0.0|null|" + this.getSpecialEntryDbEntry() + "|1|" + parameters + "|" + System.currentTimeMillis();
                
            }
                
        
            default:
                return "Value is undefined";
        
        }
    }


    /**
     * Is Data BG
     * 
     * @see ggc.plugin.output.OutputWriterData#isDataBG()
     */
    @Override
    public boolean isDataBG()
    {
        return true;
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
        return ATechDate.FORMAT_DATE_AND_TIME_MIN;
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
        ArrayList<DayValueH> lst = new ArrayList<DayValueH>();
        
        if (this.entry_object!=null)
            lst.add(this.entry_object);
        
        return lst;
    }

    /**
     * Get Value For Item
     * 
     * @param index index for statistics item 
     * @return 
     */
    public float getValueForItem(int index)
    {
        return 0;
    }

    
    /**
     * Get Statistics Action - we define how statistic is done (we have several predefined 
     *    types of statistics
     * 
     * @param index index for statistics item 
     * @return
     */
    public int getStatisticsAction(int index)
    {
        return 0;
    }
    

    /**
     * Is Special Action - tells if selected statistics item has special actions
     * 
     * @param index
     * @return
     */
    public boolean isSpecialAction(int index)
    {
        return false;
    }
    
    
    /**
     * Get Max Statistics Object - we can have several Statistic types defined here
     * 
     * @return
     */
    public int getMaxStatisticsObject()
    {
        return 0;
    }


    /**
     * If we have any special actions for any of objects
     * 
     * @return
     */
    public boolean weHaveSpecialActions()
    {
        return false;
    }


    /**
     * Get Table Column Value (in case that we need special display values for download data table, this method 
     * can be used, if it's the same as getColumnValue, we can just call that one. 
     * 
     * @param index
     * @return
     */
    public Object getTableColumnValue(int index)
    {
        return this.getColumnValue(index);
    }


    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "MVE_" + this.datetime.getATDateTimeAsLong();
    }

    
    
    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "";
    }
    
    
    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {
        return "";
    }


    /**
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        return false;
    }


    /**
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        return false;
    }


    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {
        return false;
    }
    

    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "";
    }


    /**
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public boolean isDebugMode()
    {
        return false;
    }



    /**
     * getAction - returns action that should be done on object
     *    0 = no action
     *    1 = add action
     *    2 = edit action
     *    3 = delete action
     *    This is used mainly for objects, contained in lists and dialogs, used for 
     *    processing by higher classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }

    

    /**
     * Get DeviceValuesEntry Name
     * 
     * @return
     */
    public String getDVEName()
    {
        return "MeterValuesEntry";
    }


    /**
     * Get Value of object
     * 
     * @return
     */
    public String getValue()
    {
        return this.value_db;
    }
    
    
    long old_id;
    
    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     * 
     * @param id_in
     */
    public void setId(long id_in)
    {
        this.old_id = id_in;
    }
    
    
    /**
     * Get Old Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getId()
    {
        return this.old_id;
    }
    
    
    String source;
    
    /**
     * Set Source
     * 
     * @param src
     */
    public void setSource(String src)
    {
        this.source = src;
        
    }
    
    /**
     * Get Source 
     * 
     * @return
     */
    public String getSource()
    {
        return this.source;
    }
     
    
}	
