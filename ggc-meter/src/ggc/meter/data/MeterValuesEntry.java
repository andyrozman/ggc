package ggc.meter.data;

import ggc.core.data.ExtendedDailyValue;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.util.DataAccess;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriterType;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.ext.ExtendedHandler;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;


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
	I18nControlAbstract m_ic = da.getI18nControlInstance();
	private ATechDate datetime;
	private String bg_str;
	private int bg_unit;
	
    private static Log log = LogFactory.getLog(MeterValuesEntry.class);
	
	//public boolean checked = false;
	//public
	private Hashtable<String,String> params;
	//public int status = 1; //MeterValuesEntry.
	//private static I18nControl ic = I18nControl.getInstance(); 
	
	private String bg_original = null;
	private OutputUtil util = OutputUtil.getInstance();
	private String value_db = null;
	
	private static ExtendedHandler eh_dailyValue;
	
	private float bg_mmolL;
	private int bg_mgdL;
	
//	private float ch;
	
	Hashtable<Integer, MeterValuesEntrySpecial> special_entries = null; 
	//new Hashtable<Integer, MeterValuesEntrySpecial>();
	int special_entry_first = -1;
	
	private boolean entry_changed = false;
	
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
//    public boolean special_entry = false;

    
    /**
     * Special Entry Id
     */
//    public int special_entry_id = -1;

    /**
     * Special Entry Value
     */
//    public String special_entry_value = null;
    
    
    
	/**
	 * Constructor
	 */
	public MeterValuesEntry()
	{
	    super();
	    this.source = DataAccessMeter.getInstance().getSourceDevice();
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
        
        if (dv.getCh()>0.0)
        {
            this.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH, "" + dv.getCh());
        }
        
        this.loadSpecialEntries(dv.getExtended());
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
	        bg_mgdL = Integer.parseInt(this.value_db);
	        bg_mmolL = (float)this.util.getBGValueDifferent(OutputUtil.BG_MGDL, (float)bg_mgdL);
		}
		else
		{
		    this.value_db = "" + ((int)this.util.getBGValueDifferent(OutputUtil.BG_MMOL, Float.parseFloat(value)));
		
		    //DataAccess.getInstance().
		    bg_mmolL = Float.parseFloat((DataAccess.Decimal1Format.format( (Float.parseFloat(value.replace(",", "."))) )).replace(",", "."));
            bg_mgdL = (int)this.util.getBGValueDifferent(OutputUtil.BG_MMOL, bg_mmolL);
		
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
//                System.out.println("util: " + this.util);
//                System.out.println("bg_org: " + this.bg_original);
                
                if (this.bg_original==null)
                {
                    // FIXME
                    //System.out.println("special: " + this.getSpecialEntryDbEntry());
                }
                
                
                return DataAccessMeter.Decimal1Format.format((this.util.getBGValueDifferent(OutputUtil.BG_MGDL, Float.parseFloat(this.bg_original))));
            }
	        
	    }
	    
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
	        if (this.bg_original!=null)    
	            this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));

            //processSpecialEntries();
            //this.loadSpecialEntries(this.entry_object.getExtended());
	        this.saveSpecialEntries();
	        
	        this.entry_object.setChanged(System.currentTimeMillis());
	        this.entry_object.setComment(createComment());
	    }
	    else
	    {
	        this.entry_object = new DayValueH();
	        this.entry_object.setIns1(0);
            this.entry_object.setIns2(0);
            this.entry_object.setCh(0.0f);

            
            if (this.bg_original!=null)    
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));

//            this.loadSpecialEntries(this.entry_object.getExtended());
            
//            processSpecialEntries();
            this.saveSpecialEntries();

            /*
            if (this.special_entry)
                this.entry_object.setExtended(this.getSpecialEntryDbEntry()+";" + "SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            else
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
                this.entry_object.setExtended("SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            }
            */
            
            
            //this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
	        this.entry_object.setDt_info(this.getDateTime());
            this.entry_object.setChanged(System.currentTimeMillis());
            //this.entry_object.setExtended("SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
            this.entry_object.setComment(createComment());
	    }
	}


    public static final int ENTRY_NONE = 0;
	public static final int ENTRY_BG = 1;
    public static final int ENTRY_SPECIAL = 2;
    public static final int ENTRY_COMBINED = 3;

	//public int entry_type = 0;
	
	
	/*
	public int getEntryType()
	{
	    return entry_type;
	}*/
	
	
	/*
	public void processSpecialEntries()
	{
	    
        //if (this.special_entry)
        //    this.entry_object.setExtended(this.getSpecialEntryDbEntry());
	    
        if (this.special_entry)
            this.entry_object.setExtended(this.getSpecialEntryDbEntry()+";" + "SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());

        
        this.entry_object.setExtended("SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
        
	}*/
	
	
	
    /**
     * Prepare Entry [Framework v2]
     */
    public void prepareEntry_v2()
    {
        // SPECIAL_METER_FLAGS
        // we use this for old values, to correctly read extended flags, if you used special meters codes 
        // (non BG), then you will need to modify this
        
        if (eh_dailyValue==null)
        {
            eh_dailyValue = DataAccessMeter.getInstance().getExtendedHandler(DataAccess.EXTENDED_HANDLER_DailyValuesRow);
        }
        
        if (this.entry_object==null)
            return;
        
        this.loadSpecialEntries(this.entry_object.getExtended());
        
        /*
        Hashtable<String,String> dt = eh_dailyValue.loadExtended(this.entry_object.getExtended());

        // URINE
        if (eh_dailyValue.isExtendedValueSet(ExtendedDailyValue.EXTENDED_URINE, dt))
        {
            this.special_entry=true;
            
            String val = eh_dailyValue.getExtendedValue(ExtendedDailyValue.EXTENDED_URINE, dt);
            val = val.toUpperCase();
            //String val = dt.get(ExtendedDailyValue.EXTENDED_URINE).toUpperCase();
            
            if (val.contains("MG"))
            {
                this.special_entry_id = MeterValuesEntry.SPECIAL_ENTRY_URINE_MGDL;
                this.special_entry_value = val.substring(0, val.indexOf("MG")).trim();
            }
            else
            {
                this.special_entry_id = MeterValuesEntry.SPECIAL_ENTRY_URINE_MMOLL;
                
                if (val.contains("MMOL"))
                {
                    this.special_entry_value = val.substring(0, val.indexOf("MMOL")).trim();
                }
                else
                {
                    this.special_entry_value = val.trim();
                }
            }
        }  // URINE
        */
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
                return this.getDateTimeObject().getDateTimeString() + "  " + getExtendedTypeValue(false);
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
                
                //Columns: id; dt_info; bg; ins1; ins2; ch; meals_ids; extended; person_id; comment; changed
                
                String v = "0|" + this.getDateTime() + "|" + val + "|0.0|0.0|";
                
                if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                {
                    v += this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.');
                }
                else
                    v += "0.0";
                
                v += "|null|" + this.createExtendedValueDailyValuesH() + "|1|" + parameters + "|" + System.currentTimeMillis();
                
                return v;
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
     * @param column
     * @return
     */
    @Override
    public Object getColumnValue(int column)
    {
        if (!da.isDataDownloadSceenWide())
            return this.getColumnValueBase(column); 
        else
            return this.getColumnValueExtended(column);
    }


        
    private Object getColumnValueBase(int column)
    {
        switch (column)
        {
            case 0:
                return this.getDateTimeObject().getDateTimeString();
    
            case 1:
                return this.bg_mgdL; //mve.getBGValue(DataAccessMeter.BG_MMOL);
    
            case 2:
                return this.bg_mmolL; //mve.getBGValue(DataAccessMeter.BG_MGDL);
    
            case 3:
                return new Integer(this.getStatus());
    
            case 4:
                return new Boolean(this.getChecked());
    
            default:
                return "";
        }
    }
        
    
    private Object getColumnValueExtended(int column)
    {
        switch (column)
        {
            case 0:
                return getDateTimeObject().getDateTimeString();

            case 1:
                return this.getExtendedTypeDescription();

            case 2:
                // value
                return this.getExtendedTypeValue(true);

            case 3:
                return this.getStatus();
            
            case 4:
                return new Boolean(getChecked());

            default:
                return "";
        }
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
        processEntry(sess, "Add");
        
        return "" + this.getId();
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
        processEntry(sess, "Edit");
        return true;
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


    private void processEntry(Session sess, String act)
    {
        if (this.object_status == MeterValuesEntry.OBJECT_STATUS_OLD)
        {
            log.debug("Exiting. Status was old. Action was: " + act);
            return;
        }
        else if (this.object_status == MeterValuesEntry.OBJECT_STATUS_EDIT)
        {
            Transaction tx = sess.beginTransaction();
            
            if (this.hasSpecialEntries())
            {
                if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                {
                    this.entry_object.setCh(Float.parseFloat(this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.')));
                }
            }

            this.entry_object.setExtended(this.createExtendedValueDailyValuesH());
            

            if (this.hasBgEntry())
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            
            this.entry_object.setChanged(System.currentTimeMillis());
            this.entry_object.setComment(createComment());
            this.entry_object.setPerson_id((int)DataAccessMeter.getInstance().getCurrentUserId());
            log.debug("Updated. Status was Edit. Action was: " + act);
            
            sess.update(this.entry_object);
        
            tx.commit();
        }
        else
        {
            Transaction tx = sess.beginTransaction();
            
            this.entry_object = new DayValueH();
            this.entry_object.setIns1(0);
            this.entry_object.setIns2(0);
            this.entry_object.setCh(0.0f);
            this.entry_object.setPerson_id((int)DataAccessMeter.getInstance().getCurrentUserId());

            
            if (this.hasSpecialEntries())
            {
                if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                {
                    this.entry_object.setCh(Float.parseFloat(this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.')));
                }
            }

            this.entry_object.setExtended(this.createExtendedValueDailyValuesH());

            if (this.hasBgEntry())
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            
            
            this.entry_object.setDt_info(this.getDateTime());
            this.entry_object.setChanged(System.currentTimeMillis());
            this.entry_object.setComment(createComment());
            
            log.debug("Added. Status was Add. Action was: " + act);
            Long id = (Long) sess.save(this.entry_object);

//            System.out.println("Dt: " + this.getDateTimeObject().getDateTimeString() + this.getBgValue());
//            System.out.println("Add: Id=" + id.longValue());
            
            tx.commit();

            this.setId(id.longValue());
            
            //return "" + id.longValue();
            
        }
        
        
        
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
        return getDVEName();
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
    
    
    /**
     * Get Value of object (for comparing two objects are the same)
     * 
     * @return
     */
    public String getValueFull()
    {
        // for comparing if object is the same
        
        StringBuffer sb = new StringBuffer();
        
        if (this.hasBgEntry())
        {
            sb.append("BG=" + bg_original + ";");
        }
        
        if ((this.hasSpecialEntries()) && (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH)))
        {
            sb.append("CH=" + this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.') + ";");
        }
        
        sb.append(this.createExtendedValueDailyValuesH());
        return sb.toString();
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
     

    //---
    //--- Special entries
    //---
    
    
    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
//    public static final int SPECIAL_ENTRY_URINE_MMOLL = 1;

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
//    public static final int SPECIAL_ENTRY_URINE_MGDL = 2;
    
    /**
     * Special Entry: CH
     */
//    public static final int SPECIAL_ENTRY_CH = 3;
    
    
    /**
     * Set Special Entry
     * 
     * @param type
     * @param value
     */
    public void addSpecialEntry(int type, String value)
    {
        if (this.special_entries == null)
        {
            this.special_entries = new Hashtable<Integer, MeterValuesEntrySpecial>();
            this.special_entry_first = type;
        }
        
        MeterValuesEntrySpecial sp = new MeterValuesEntrySpecial(type, value); 
        this.special_entries.put(type, sp);
    }
    
    
    public boolean hasBgEntry()
    {
        return (this.bg_original!=null);
    }
    
    
    public boolean hasSpecialEntries()
    {
        if (this.special_entries == null)
            return false;
        else
            return (this.special_entries.size()!=0);
    }
    
    
    public void loadSpecialEntries(String s)
    {
        // FIXME
    }
    

    
    public void saveSpecialEntries()
    {
        // FIXME
    }
    
    
    

/*    
    String special_entry_tags[] = { "", "URINE", "URINE", "CH" };
    String special_entry_units[] = { "", " mmol/L", " mg/dL", " g" };  // this are not required, if your special 
    boolean special_entry_transfer_unit[] = { false, true, true, false };                                                             // entry has no unit, leave this empty (have at least one space as unit), so that code will work
    
    int special_entry_pump_map[] = { -1, 4, 4, 5 };
  */  
    
    
    
    
    
    /**
     * Get Extended Type Description (if we use extended interface, this is type description)
     * 
     * @return
     */
    public String getExtendedTypeDescription()
    {
        // FIXME
        return null;
        /*
        if (this.special_entry)
            return m_ic.getMessage(this.special_entry_tags[this.special_entry_id]);
        else
            return m_ic.getMessage("BG");
           */
    }
    
    /**
     * Get Extended Type Value (if we use extended interface, this is value)
     * 
     * @param both_bg 
     * 
     * @return
     */
    public String getExtendedTypeValue(boolean both_bg)
    {
        
        if (this.getEntryType()==MeterValuesEntry.ENTRY_NONE)
            return "No data";
        else if (this.getEntryType()==MeterValuesEntry.ENTRY_BG)
        {
            if (both_bg)
                return this.bg_mgdL + " mg/dL (" + DataAccessMeter.Decimal1Format.format(this.bg_mmolL) + " mmol/L";            
            else
                return "BG: " + this.getBgValue() + " " + OutputUtil.getBGTypeNameStatic(this.getBgUnit());
        }
        else if (this.getEntryType()==MeterValuesEntry.ENTRY_SPECIAL)
        {
            // FIXME
            return null;
        }
        else 
        {
            // FIXME
            // COMBINED
            return this.getDateTimeObject().getDateTimeString() + "  BG: " + this.getBgValue() + " " + OutputUtil.getBGTypeNameStatic(this.getBgUnit());
        }
            
      
        
        
        
        
//            return this.special_entry_value + this.special_entry_units[this.special_entry_id];
    }
    

    /**
     * Is Special Entry
     * 
     * @return
     */
/*    public boolean isSpecialEntry()
    {
        return this.entry_type == this.ENTRY_SPECIAL;
    }
  */  
    
    
    /**
     * Get Allowed Pump Mapped Types
     * 
     * @return
     */
    public Hashtable<String,String> getAllowedPumpMappedTypes()
    {
        return MeterValuesEntrySpecial.getAllowedPumpMappedTypes();
        
        /*
        Hashtable<String,String> ht = new Hashtable<String,String>();
        
        for(int i=0; i<this.special_entry_pump_map.length; i++)
        {
            if (this.special_entry_pump_map[i]!=-1)
            {
                if (!ht.containsKey(""+this.special_entry_pump_map[i]))
                {
                    ht.put(""+this.special_entry_pump_map[i], "");
                }
            }
            
        }
        
        return ht; */
    }
    

    /*
    public int getPumpMappedType()
    {
        if (this.entry_type==ENTRY_BG)
            return 3;
        else if (this.entry_type==ENTRY_SPECIAL)
            return this.special_entries.get(this.special_entry_first).getPumpMappedType();
        else
            return -1;
    }*/
    
    
    public int getEntryType()
    {
        if (!this.hasBgEntry())
        {
            if (!this.hasSpecialEntries())
                return ENTRY_NONE;
            else //if (this.special_entries.size()==1)
                return ENTRY_SPECIAL;
            //else
            //    return ENTRY_COMBINED;
        }
        else
        {
            if (!this.hasSpecialEntries())
                return ENTRY_BG;
            else
                return ENTRY_COMBINED;
        }
    }
    
/*
    public String getSpecialEntriesDesc()
    {
        // FIXME
    }
  */  
    
    
    /**
     * Create data for extended field in database (special entries without CH)
     * @return
     */
    public String createExtendedValueDailyValuesH()
    {
        // we need to ignore ch, all others are transfered
        
        String ext = "";
        
        if (this.special_entries!=null)
        {
            for(int i=1; i<=MeterValuesEntrySpecial.SPECIAL_ENTRY_MAX; i++)
            {
                int key = i;
                
                if (key!=MeterValuesEntrySpecial.SPECIAL_ENTRY_CH)
                {
                    ext += this.special_entries.get(key).getSpecialEntryDbEntry() + ";";
                }
            }
        }
        
        ext += "SOURCE=" + this.source;
        
        return ext;
        
    }
    
    // we always append 
    public void addSpecialEntryAll(ArrayList<MeterValuesEntrySpecial> coll)
    {
        // FIXME
    }
    
    
    
    public ArrayList<MeterValuesEntrySpecial> getDataEntriesAsMVESpecial()
    {
        // FIXME
        return null;
    }
    
    
    
}	
