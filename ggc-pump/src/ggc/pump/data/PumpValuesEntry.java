package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputWriterType;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.db.PumpData;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.graphics.components.MultiLineTooltip;
import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.statistics.StatisticsItem;
import com.atech.misc.statistics.StatisticsObject;
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
 *  Filename:     PumpValuesEntry
 *  Description:  Pump Values Entry, with all data, also statistics item.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpValuesEntry extends DeviceValuesEntry implements MultiLineTooltip, StatisticsItem 
//extends PumpDataH implements DatabaseObjectHibernate   // extends PumpValuesEntryAbstract
{
    private static Log log = LogFactory.getLog(PumpValuesEntry.class);
    private static final long serialVersionUID = -2047203215269156938L;

    DataAccessPump m_da = null; //DataAccessPump.getInstance();
    I18nControlAbstract m_ic = null;
	
	// pump
    long id;
	ATechDate datetime;
	int base_type;
	int sub_type;
	String value;
	String extended;
	//String profile;
	long person_id;
	String comment;
	long changed;
	String source;


	private Hashtable<String,String> params;
    
    private PumpDataH entry_object = null;
    private Hashtable<String,PumpValuesEntryExt> additional_data = new Hashtable<String,PumpValuesEntryExt>(); 
	

    /**
     * Constructor
     * 
     * @param tr
     */
    public PumpValuesEntry(boolean tr)
    {
        //m_da = DataAccessPump.getInstance();
        //m_ic = m_da.getI18nControlInstance();
    }

	
	/**
	 * Constructor 
	 */
	public PumpValuesEntry()
	{
	    this((String)null);
	}

	
    /**
     * Constructor 
     * 
     * @param src 
     */
    public PumpValuesEntry(String src)
    {
        m_da = DataAccessPump.getInstance();
        m_ic = m_da.getI18nControlInstance();
        
        this.id = 0L;
        this.datetime = new ATechDate(this.getDateTimeFormat(), new GregorianCalendar());
        this.base_type = 0;
        this.sub_type = 0;
        this.value = "";
        this.extended = "";
        this.person_id = m_da.getCurrentUserId();
        this.comment = "";
        this.source = src;
        
    }
	
	

    /**
     * Constructor
     * 
     * @param base_type
     */
    public PumpValuesEntry(int base_type)
    {
        m_da = DataAccessPump.getInstance();
        m_ic = m_da.getI18nControlInstance();

        this.id = 0L;
        this.datetime = new ATechDate(this.getDateTimeFormat(), new GregorianCalendar());
        this.base_type = base_type;
        this.sub_type = 0;
        this.value = "";
        this.extended = "";
        this.person_id = m_da.getCurrentUserId();
        this.comment = "";
    }

	
	/**
     * Constructor
     * 
	 * @param pdh
	 */
	public PumpValuesEntry(PumpDataH pdh)
	{
        m_da = DataAccessPump.getInstance();
        m_ic = m_da.getI18nControlInstance();

        //this.entry_object = pdh;
	    this.id = pdh.getId();
	    this.old_id = pdh.getId();
        this.datetime = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pdh.getDt_info());
        this.base_type = pdh.getBase_type();
        this.sub_type = pdh.getSub_type();
        this.value = pdh.getValue();
        this.extended = pdh.getExtended();
        this.person_id = pdh.getPerson_id();
        this.comment = pdh.getComment();
        
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
     * Set Value
     * 
     * @return 
     */
    public String getValue()
    {
        /*if (this.getSubType()==PumpBaseType.PUMP_DATA_BOLUS)
        {
            return this.value;
        }
        else */
            return this.value;
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
    	        return getBaseTypeString();
    	    }
    	    case 2: // subtype
    	    {
    	        return getSubTypeString();
    	    }
    	    case 3: // value
    	    {
    	        //return this.getValue();
    	        return getValuePrint();
    	    }
    	    case 4: // additional
    	    {
    	        return this.getAdditionalDisplay();
    	    }
    	    case 5: // food
    	    {
    	        return this.isFoodSet();
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
	public int getSubType()
	{
	    return this.sub_type;
	}
	

    /**
     * Get Sub Type
     * 
     * @return
     */
    public String getBaseTypeString()
    {
        return this.m_da.getPumpBaseTypes().basetype_desc[this.base_type];
    }

	
	
	/**
	 * Get Sub Type
	 * 
	 * @return
	 */
	public String getSubTypeString()
	{
	    if (this.sub_type==0)
	        return "";
	    
	    if (this.base_type==PumpBaseType.PUMP_DATA_BASAL)
	    {
	        return m_da.getBasalSubTypes().getDescriptions()[this.sub_type];
	    }
	    else if (this.base_type==PumpBaseType.PUMP_DATA_BOLUS)
	    {
            return m_da.getBolusSubTypes().getDescriptions()[this.sub_type];
	    }
	    else if (this.base_type==PumpBaseType.PUMP_DATA_REPORT)
	    {
	        // TODO remove
	        //System.out.println("m_da: " + m_da);
            //System.out.println("reportTypes: " + m_da.getPumpReportTypes());
            //System.out.println("reportTypes.descriptions: " + m_da.getPumpReportTypes().getDescriptions());
	        
            return m_da.getPumpReportTypes().getDescriptions()[this.sub_type];
	    }
        else if (this.base_type==PumpBaseType.PUMP_DATA_ALARM)
        {
            return m_da.getPumpAlarmTypes().getDescriptions()[this.sub_type];
        }
        else if (this.base_type==PumpBaseType.PUMP_DATA_ERROR)
        {
            return m_da.getPumpErrorTypes().getDescriptions()[this.sub_type];
        }
        else if (this.base_type==PumpBaseType.PUMP_DATA_EVENT)
        {
            return m_da.getPumpEventTypes().getDescriptionForType(this.sub_type);
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
	        //sb.append("<html>");
	        
	        for(Enumeration<String> en=this.additional_data.keys(); en.hasMoreElements(); i++ )
	        {
	            String key = en.nextElement();
	            
	            if (i>0)
	                sb.append("; ");
	            
	            //sb.append(key + "=" + this.additional_data.get(key).toString());
	            sb.append(this.additional_data.get(key).toString());
	            
	            //if (i%3==0)
	            //    sb.append("");
	            
	        }
	        
	        //sb.append("</html>");
	        return sb.toString();
	    }
	}

	
	/**
	 * Is Food Set
	 *  
	 * @return
	 */
	public String isFoodSet()
	{
	    if ((this.additional_data.containsKey(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC])) ||
	        (this.additional_data.containsKey(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB])))
	    {
	        return m_da.getI18nControlInstance().getMessage("YES");
	    }
	    else
	    {
            return m_da.getI18nControlInstance().getMessage("NO");
	    }
	}
	

    /**
     * Get Additional Display
     * @return
     */
    public String getAdditionalDisplayHTML()
    {
        if (this.additional_data.size()==0)
            return "";
        else
        {
            StringBuffer sb = new StringBuffer();
            int i=0;
            sb.append("<html>");
            
            for(Enumeration<String> en=this.additional_data.keys(); en.hasMoreElements(); i++ )
            {
                String key = en.nextElement();
                
                if ((key.equals(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB])) ||
                    (key.equals(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC])))
                    continue;
                
                if (i>0)
                    sb.append("<br>");
                
                //sb.append(key + "=" + this.additional_data.get(key).toString());
                sb.append(this.additional_data.get(key).toString());
                
                //if (i%3==0)
                //    sb.append("");
                
            }
            
            sb.append("</html>");
            return sb.toString();
        }
    }
	
	
    /**
     * Get Value HTML
     * 
     * @return
     */
    public String getValueHTML()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<html>");

        if (this.base_type==PumpBaseType.PUMP_DATA_BASAL)
        {
            if (this.sub_type==PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE)
            {
                String s[] = m_da.getParsedValues(this.value);
                sb.append(String.format("%s: %s<br>%s: %s", 
                              m_ic.getMessage("DURATION"),
                              s[0],
                              m_ic.getMessage("AMOUNT"),
                              s[1]));
            }
            else
                sb.append(this.getValue());
                
        }
        else if (this.base_type==PumpBaseType.PUMP_DATA_BOLUS)
        {
            if (this.sub_type==PumpBolusType.PUMP_BOLUS_SQUARE)
            {
                String s[] = m_da.getParsedValues(this.getValue());
                
                if (s.length==1)
                {
                    // old format
                    sb.append(String.format("%s: %s<br>%s: %s", 
                        m_ic.getMessage("SQUARE_AMOUNT"),
                        s[0],
                        m_ic.getMessage("DURATION"),
                        "??"));
                }
                else
                {
                    sb.append(String.format("%s: %s<br>%s: %s", 
                                  m_ic.getMessage("SQUARE_AMOUNT"),
                                  s[0],
                                  m_ic.getMessage("DURATION"),
                                  s[1]));
                }
            }
            else if (this.sub_type==PumpBolusType.PUMP_BOLUS_MULTIWAVE)
            {
                String s[] = m_da.getParsedValues(this.getValue());
                if (s.length==2)
                {
                    // old format
                    sb.append(String.format("%s: %s<br>%s: %s<br>%s: %s", 
                        m_ic.getMessage("IMMEDIATE_AMOUNT"),
                        s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"),
                        s[1],
                        m_ic.getMessage("DURATION"),
                        "??"));
                }
                else
                {
                    sb.append(String.format("%s: %s<br>%s: %s<br>%s: %s", 
                                  m_ic.getMessage("IMMEDIATE_AMOUNT"),
                                  s[0],
                                  m_ic.getMessage("SQUARE_AMOUNT"),
                                  s[1],
                                  m_ic.getMessage("DURATION"),
                                  s[2]));
                }
            }
            else
                sb.append(this.getValue());
        }
        else
            sb.append(this.getValue());
            
        sb.append("</html>");
        return sb.toString();
    }

    
    /**
     * Get Value Print
     * 
     * @return
     */
    public String getValuePrint()
    {
        StringBuffer sb = new StringBuffer();
        
        if (this.base_type==PumpBaseType.PUMP_DATA_BASAL)
        {
            if (this.sub_type==PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE)
            {
                String s[] = m_da.getParsedValues(this.value);
                sb.append(String.format("%s: %s, %s: %s", 
                              m_ic.getMessage("DURATION"),
                              s[0],
                              m_ic.getMessage("AMOUNT"),
                              s[1]));
            }
            else
                sb.append(this.getValue());
                
        }
        else if (this.base_type==PumpBaseType.PUMP_DATA_BOLUS)
        {
            if (this.sub_type==PumpBolusType.PUMP_BOLUS_SQUARE)
            {
                
                String s[] = m_da.getParsedValues(this.value);
                
                if (s.length==1)
                {
                    // old format
                    sb.append(String.format("%s: %s, %s: %s", 
                                  m_ic.getMessage("SQUARE_AMOUNT"),
                                  s[0],
                                  m_ic.getMessage("DURATION"),
                                  "??"));
                }
                else 
                {
                    sb.append(String.format("%s: %s, %s: %s", 
                                  m_ic.getMessage("SQUARE_AMOUNT"),
                                  s[0],
                                  m_ic.getMessage("DURATION"),
                                  s[1]));
                }
            }
            else if (this.sub_type==PumpBolusType.PUMP_BOLUS_MULTIWAVE)
            {
                String s[] = m_da.getParsedValues(this.value);
                if (s.length==2)
                {
                    // old format
                    sb.append(String.format("%s: %s, %s: %s, %s: %s", 
                        m_ic.getMessage("IMMEDIATE_AMOUNT"),
                        s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"),
                        s[1],
                        m_ic.getMessage("DURATION"),
                        "??"));
                }
                else
                {
                    sb.append(String.format("%s: %s, %s: %s, %s: %s", 
                                  m_ic.getMessage("IMMEDIATE_AMOUNT"),
                                  s[0],
                                  m_ic.getMessage("SQUARE_AMOUNT"),
                                  s[1],
                                  m_ic.getMessage("DURATION"),
                                  s[2]));
                }
            }
            else
                sb.append(this.getValue());
        }
        else
            sb.append(this.getValue());
            
        return sb.toString();
    }
    
    
    /**
     * Print Additional: All Entries (without food data, just info that food is present)
     */
    public static final int PRINT_ADDITIONAL_ALL_ENTRIES = 1;

    
    /**
     * Print Additional: All Entries (with food data)
     */
    public static final int PRINT_ADDITIONAL_ALL_ENTRIES_WITH_FOOD = 2;
    
	
    /**
     * Get Additional Display
     * 
     * @param type 
     * @return
     */
    public String getAdditionalDataPrint(int type)
    {
        if (this.additional_data.size()==0)
            return "";
        else
        {
            StringBuffer sb = new StringBuffer();
            int i=0;
            
            String food_key = null;
            
            for(Enumeration<String> en=this.additional_data.keys(); en.hasMoreElements(); i++ )
            {
                String key = en.nextElement();
                
                if (i>0)
                    sb.append("; ");
                
                //sb.append(key + "=" + this.additional_data.get(key).toString());
                
                if (type==PRINT_ADDITIONAL_ALL_ENTRIES_WITH_FOOD)
                {
                    
                    if ((key.equals(m_da.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC))) ||
                        (key.equals(m_da.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB))))
                    {
                       food_key = key; 
                    }
                    else
                        sb.append(this.additional_data.get(key).toString());
                    
                    
                }
                else
                    sb.append(this.additional_data.get(key).toString());
                
                //if (i%3==0)
                //    sb.append("\n");
                
            }

            
            
            if ((food_key!=null) && (food_key.length()>0))
            {
                
                if (food_key.equals(m_da.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC)))
                {
                    PumpValuesEntryExt pvee = this.additional_data.get(food_key);
                    sb.append("\n" + m_ic.getMessage("FOOD_DESC_PRINT") + ": " + pvee.getValue());
                }
                else if (food_key.equals(m_da.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB)))
                {
                    //PumpValuesEntryExt pvee = this.additional_data.get(food_key);
                    sb.append("\n" + m_ic.getMessage("FOOD_DB_PRINT") + ": " + m_ic.getMessage("FOOD_DESC_PRINT_NOT_YET"));
                }
            }
            
            
            
            return sb.toString();
        }
    }
	
	
	
	/**
     * Prepare Entry [PlugIn Framework v1] 
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
     * Get Db Objects [PlugIn Framework v1]
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


    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();
        
        PumpDataH pdh = new PumpDataH();
        
        pdh.setId(this.id);
        pdh.setDt_info(this.datetime.getATDateTimeAsLong()); 
        pdh.setBase_type(this.base_type); 
        pdh.setSub_type(this.sub_type);
        pdh.setValue(this.value);
        //pdh.setExtended(this.extended);
        pdh.setExtended("SOURCE=" + this.source);
        pdh.setPerson_id((int)this.person_id); 
        pdh.setComment(this.comment); 
        pdh.setChanged(System.currentTimeMillis());
        pdh.setChanged(System.currentTimeMillis());

        Long _id = (Long)sess.save(pdh);
        tx.commit();

        pdh.setId(_id.longValue());
        
        return ""+_id.longValue();
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
        Transaction tx = sess.beginTransaction();

        PumpDataH ch = (PumpDataH)sess.get(PumpDataH.class, new Long(this.id));
        sess.delete(ch);
        tx.commit();

        return true;
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
        Transaction tx = sess.beginTransaction();
        //System.out.println("id: " + old_id);
        PumpDataH pdh = (PumpDataH)sess.get(PumpDataH.class, new Long(this.old_id));
        
        //System.out.println("PumpDataH: " + pdh);
        
        
        pdh.setId(this.old_id);
        pdh.setDt_info(this.datetime.getATDateTimeAsLong()); 
        pdh.setBase_type(this.base_type); 
        pdh.setSub_type(this.sub_type);
        pdh.setValue(this.value);
        pdh.setExtended(this.extended); 
        pdh.setPerson_id((int)this.person_id); 
        pdh.setComment(this.comment); 
        pdh.setChanged(System.currentTimeMillis());

        sess.update(pdh);
        tx.commit();

        return true;
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
        PumpDataH pdh = (PumpDataH)sess.get(PumpDataH.class, new Long(this.id));
        
        this.id = pdh.getId();
        this.datetime = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pdh.getDt_info());
        this.base_type = pdh.getBase_type();
        this.sub_type = pdh.getSub_type();
        this.value = pdh.getValue();
        this.extended = pdh.getExtended();
        this.person_id = pdh.getPerson_id();
        this.comment = pdh.getComment();
        
        return true;
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
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "PumpDataH";
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
    
    public String getDataAsString()
    {
        switch(output_type)
        {
            case OutputWriterType.DUMMY:
                return "";
                
            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                
                return this.getDateTimeObject().getDateTimeString() + ":  Base Type=" + this.getBaseTypeString() + ", Sub Type=" + this.getSubTypeString() + ", Comment=" + this.getComment();
                
            case OutputWriterType.GGC_FILE_EXPORT:
            {
                PumpData pd = new PumpData(this);
                try
                {
                    return pd.dbExport();
                }
                catch(Exception ex)
                {
                    log.error("Problem with PumpValuesEntry export !  Exception: " + ex, ex);
                    return "Value could not be decoded for export!";
                }
            }
                
        
            default:
                return "Value is undefined";
        
        }
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
	

    /**
     * Get Id
     * 
     * @return
     */
/*    public long getId()
    {
        return this.id;
    }
  */  
    
    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }
    
    
    /**
     * Set Comment
     * 
     * @param value
     */
    public void setComment(String value)
    {
        this.comment = value;
    }

    
    
    //---
    //---  Statistics
    //---
    
    
    /**
     * Statistics Value: Bolus Insulin Sum
     */
    public static final int INS_SUM_BOLUS =1;
    
    /**
     * Statistics Value: Basal Insulin Sum
     */
    public static final int INS_SUM_BASAL =2;

    /**
     * Statistics Value: Both Insulin Sum
     */
    public static final int INS_SUM_TOGETHER =3;
    
    /**
     * Statistics Value: Bolus Insulin Average
     */
    public static final int INS_AVG_BOLUS =4;

    /**
     * Statistics Value: Basal Insulin Average
     */
    public static final int INS_AVG_BASAL =5;

    /**
     * Statistics Value: Both Insulin Average
     */
    public static final int INS_AVG_TOGETHER =6;

    /**
     * Statistics Value: Bolus Insulin Doses
     */
    public static final int INS_DOSES_BOLUS =7;

    /**
     * Statistics Value: Basal Insulin Doses
     */
    public static final int INS_DOSES_BASAL =8;

    /**
     * Statistics Value: Both Insulin Doses
     */
    public static final int INS_DOSES_TOGETHER =9;
    
    /**
     * Statistics Value: Carbohydrates Sum
     */
    public static final int CH_SUM =10;

    /**
     * Statistics Value: Carbohydrates Average
     */
    public static final int CH_AVG =11;

    /**
     * Statistics Value: Meals
     */
    public static final int MEALS =12;

    /**
     * Statistics Value: Blood Glucose Average
     */
    public static final int BG_AVG =13;

    /**
     * Statistics Value: Blood Glucose Maximal
     */
    public static final int BG_MAX =14;

    /**
     * Statistics Value: Blood Glucose Minimal
     */
    public static final int BG_MIN =15;
    
    /**
     * Statistics Value: Blood Glucose Count
     */
    public static final int BG_COUNT =16;
    
    /**
     * Statistics Value: Blood Glucose Standard Deviation
     */
    public static final int BG_STD_DEV =17;

    
    /**
     * Get Max Statistics Object - we can have several Statistic types defined here
     * 
     * @return
     */
    public int getMaxStatisticsObject()
    {
        return 18;
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
        switch (index)
        {

            case PumpValuesEntry.CH_AVG:
            case PumpValuesEntry.BG_AVG:
            case PumpValuesEntry.INS_AVG_BOLUS:
                return StatisticsObject.OPERATION_AVERAGE;
        
        
            case PumpValuesEntry.INS_SUM_BOLUS:
            case PumpValuesEntry.CH_SUM:
                return StatisticsObject.OPERATION_SUM;


            case PumpValuesEntry.MEALS:
            case PumpValuesEntry.INS_DOSES_BOLUS:
            case PumpValuesEntry.BG_COUNT:
                return StatisticsObject.OPERATION_COUNT;
                

            case PumpValuesEntry.BG_MAX:
                return StatisticsObject.OPERATION_MAX;
                
            case PumpValuesEntry.BG_MIN:
                return StatisticsObject.OPERATION_MIN;
                
                
                
            case PumpValuesEntry.INS_SUM_BASAL:
            case PumpValuesEntry.INS_SUM_TOGETHER:
            
            case PumpValuesEntry.INS_AVG_BASAL:
            case PumpValuesEntry.INS_AVG_TOGETHER:
            case PumpValuesEntry.INS_DOSES_BASAL:
    
            case PumpValuesEntry.INS_DOSES_TOGETHER:
            case PumpValuesEntry.BG_STD_DEV:
                return StatisticsObject.OPERATION_SPECIAL;
    
                
        
            default:
                return 0;
        }
    }


    /**
     * Get Value For Item
     * 
     * @param index index for statistics item 
     * @return 
     */
    public float getValueForItem(int index)
    {
        switch (index)
        {
            case PumpValuesEntry.INS_AVG_BOLUS:
            case PumpValuesEntry.INS_SUM_BOLUS:
            case PumpValuesEntry.INS_DOSES_BOLUS:
            {
                if (this.base_type==PumpBaseType.PUMP_DATA_BOLUS)
                {
                    if (this.getValue().contains(";"))
                    {
                        String vals[] = this.getValue().split(";");
                        
                        float sum = 0.0f;
                        
                        for(int i=0; i<vals.length; i++)
                        {
                            if (vals[i].startsWith("AMOUNT"))
                            {
                                String ps[] = vals[i].split("=");
                                
                                sum += m_da.getFloatValueFromString(ps[1], 0.0f);
                            }
                        }
                        
                        return sum;
                    }
                    else
                    {
                        return m_da.getFloatValueFromString(this.getValue(), 0.0f);
                    }
                }
                    
                else
                    return 0.0f;
            }
                
                
            case PumpValuesEntry.CH_SUM:
            case PumpValuesEntry.CH_AVG:
            case PumpValuesEntry.MEALS:
            {
                if (this.additional_data.containsKey(m_da.getAdditionalTypes().getDescriptions()[PumpAdditionalDataType.PUMP_ADD_DATA_CH]))
                {
                    return m_da.getFloatValueFromString(this.additional_data.get(m_da.getAdditionalTypes().getDescriptions()[PumpAdditionalDataType.PUMP_ADD_DATA_CH]).getValue(), 0.0f);
                }
                else
                    return 0.0f;
            }
                
            
            case PumpValuesEntry.BG_AVG:
            case PumpValuesEntry.BG_MAX:
            case PumpValuesEntry.BG_MIN:
            case PumpValuesEntry.BG_COUNT:
            {
                if (this.additional_data.containsKey(m_da.getAdditionalTypes().getDescriptions()[PumpAdditionalDataType.PUMP_ADD_DATA_BG]))
                {
                    return m_da.getFloatValueFromString(this.additional_data.get(m_da.getAdditionalTypes().getDescriptions()[PumpAdditionalDataType.PUMP_ADD_DATA_BG]).getValue(), 0.0f);
                }
                else
                    return 0.0f;
                
            }
                
            case PumpValuesEntry.INS_DOSES_BASAL:
            case PumpValuesEntry.INS_DOSES_TOGETHER:
                
            case PumpValuesEntry.BG_STD_DEV:
            case PumpValuesEntry.INS_SUM_BASAL:
            case PumpValuesEntry.INS_SUM_TOGETHER:
            case PumpValuesEntry.INS_AVG_BASAL:
            case PumpValuesEntry.INS_AVG_TOGETHER:
                
            default:
                return 0.0f;
        }
    }


    /**
     * Is Special Action - tells if selected statistics item has special actions
     * 
     * @param index
     * @return
     */
    public boolean isSpecialAction(int index)
    {
        switch (index)
        {
            case PumpValuesEntry.INS_SUM_BASAL:
            case PumpValuesEntry.INS_SUM_TOGETHER:
            case PumpValuesEntry.INS_AVG_BASAL:
            case PumpValuesEntry.INS_AVG_TOGETHER:
            case PumpValuesEntry.INS_DOSES_BASAL:
            case PumpValuesEntry.INS_DOSES_TOGETHER:
            case PumpValuesEntry.BG_STD_DEV:
                return true;
                
            case PumpValuesEntry.INS_AVG_BOLUS:
            case PumpValuesEntry.INS_SUM_BOLUS:
            case PumpValuesEntry.INS_DOSES_BOLUS:
            case PumpValuesEntry.CH_SUM:
            case PumpValuesEntry.CH_AVG:
            case PumpValuesEntry.MEALS:
            case PumpValuesEntry.BG_AVG:
            case PumpValuesEntry.BG_MAX:
            case PumpValuesEntry.BG_MIN:
            case PumpValuesEntry.BG_COUNT:
            default:
                return false;
        }
    }


    /**
     * If we have any special actions for any of objects
     * 
     * @return
     */
    public boolean weHaveSpecialActions()
    {
        return true;
    }


    /** 
     * Get MultiLine ToolTip
     */
    public String getMultiLineToolTip()
    {
        return null;
    }


    /** 
     * Get MultiLine ToolTip
     */
    public String getMultiLineToolTip(int index)
    {
        switch(index)
        {
            case 0: // time
            {
                return this.datetime.getTimeString();
            }
            case 1: // type
            {
                return getBaseTypeString();
            }
            case 2: // subtype
            {
                return getSubTypeString();
            }
            case 3: // value
            {
                return this.getValueHTML();
            }
            case 4: // additional
            {
                return this.getAdditionalDisplayHTML();
            }
            case 5: // comment
            {
                return this.getFoodTip();
            }
            
            default:
                return null;
                
        }
        
    }

    
    /*
    public String getValue()
    {
        
        return null;
    }*/
    
    
    
    /**
     * Get Food Tip
     * 
     * @return
     */
    public String getFoodTip()
    {
        if (this.additional_data.containsKey(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC]))
        {
            String t = this.additional_data.get(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC]).getValue();
            t = t.replace("]", "]<br>");
            t = t.replace(",", "");
            
            return "<html>" + t + "</html>";
            
        }
        else if (this.additional_data.containsKey(m_da.getAdditionalTypes().addata_desc[PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB]))
        {
            // TODO
            return "Not implemented yet !";
        }
        else
        {
            return null;
        }
        
    }
    

    /** 
     * Is Indexed (multiline tooltip)
     */
    public boolean isIndexed()
    {
        return true;
    }

    /**
     * Get Table Column Value (in case that we need special display values for download data table, this method 
     * can be used, if it's the same as getColumnValue, we can just call that one. 
     * 
     * @param column
     * @return
     */
    public Object getTableColumnValue(int column)
    {
        switch (column)
        {
            case 0:
                return getDateTimeObject().getDateTimeString();

            case 1:
                return m_ic.getMessage("BASE_TYPE_SH");

            case 2:
                return this.getBaseTypeString();

            case 3:
                return this.getSubTypeString();

            case 4:
                return this.getValuePrint();
                
            case 5:
                return this.getStatus();
            
            case 6:
                return new Boolean(getChecked());

            default:
                return "";
        }
        
    }
 	
    
    
    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "PD_" + this.datetime.getATDateTimeAsLong() + "_" + this.base_type + "_" + this.sub_type;
    }

    
    /**
     * Get DeviceValuesEntry Name
     * 
     * @return
     */
    public String getDVEName()
    {
        return "PumpValuesEntry";
    }


    /**
     * Get Value of object
     * 
     * 
     */
/*    public String getValue()
    {
        return null;
    }
  */ 
    
 
    
    long old_id;
    
    /**
     * Set Id (this is used for changing old objects in framework v2)
     * 
     * @param id_in
     */
    public void setId(long id_in)
    {
        this.old_id = id_in;
    }
    
    
    /**
     * Get Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getId()
    {
        return this.old_id;
    }
    
    
    
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
    
    
    
    /**
     * Get Additional Data Count
     * 
     * @return
     */
    public int getAdditionalDataCount()
    {
        return this.additional_data.size();
    }
    
    
}