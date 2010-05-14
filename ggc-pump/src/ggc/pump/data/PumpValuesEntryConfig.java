package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.output.OutputWriterType;
import ggc.plugin.util.DeviceValuesEntryUtil;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.i18n.I18nControlAbstract;
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


public class PumpValuesEntryConfig extends PumpProfileH implements PumpValuesEntryInterface , DatabaseObjectHibernate 
{

    private static final long serialVersionUID = -2075399803433172103L;
    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    String key = null;
    String value = null;
    int group = 0;
    
    
    private int status = 0;
    private int object_status = 0;
    private boolean checked = false;
    private int output_type = 0;
    
    
    
    //public boolean checked = false;
    //public int status = 1; //MeterValuesEntry.
    
	
    /**
     * Constructor
     */
    public PumpValuesEntryConfig()
    {
        //m_pump_add = new PumpAdditionalDataType();
    }

    
    /**
     * Constructor
     * 
     * @param _key
     * @param _value
     * @param _group
     */
    public PumpValuesEntryConfig(String _key, String _value, int _group)
    {
        this.key = _key;
        this.value = _value;
        this.group = _group;
    }
    
    
    
    /**
     * Constructor
     * 
     * @param pd
     */
/*    public PumpValuesEntryProfile(PumpProfileH pd)
    {
        this.setId(pd.getId());
        this.setName(pd.getName());
        this.setBasal_base(pd.getBasal_base());
        this.setBasal_diffs(pd.getBasal_diffs());
        this.setActive_from(pd.getActive_from());
        this.setActive_till(pd.getActive_till());
        this.setExtended(pd.getExtended());
        this.setPerson_id(pd.getPerson_id());
        this.setComment(pd.getComment());
        this.setChanged(pd.getChanged());
    }
  */  

    /**
     * Has Changed - This is method which is tied only to changes of value or datetime
     * 
     * @return
     */
    public boolean hasChanged()
    {
        return this.changed;
    }
    
	
/*	
	public void setDateTime(long dt)
	{
	    this.datetime = dt;
	}
	
	
	
	public long getDateTime()
	{
		return this.datetime;
	}
	*/
	
    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return
     */
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.getActive_from());
    }
	
	
    
    
    
	
    
	
	
	
	
/*	
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
*/
	
	
	/*
	public boolean getCheched()
	{
	    return this.checked;
	}

	public int getStatus()
	{
	    return this.status;
	}
	*/
	
	
	
	
	/**
	 * Prepare Entry
	 */
	public void prepareEntry()
	{
	    /*
	    if (this.object_status == PumpValuesEntry.OBJECT_STATUS_OLD)
	        return;
	    else if (this.object_status == PumpValuesEntry.OBJECT_STATUS_EDIT)
	    {
//	        this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
	        this.entry_object.setChanged(System.currentTimeMillis());
//	        this.entry_object.setComment(createComment());
	    }
	    else
	    {
	        this.entry_object = new DayValueH();
	        this.entry_object.setIns1(0);
            this.entry_object.setIns2(0);
            this.entry_object.setCh(0.0f);
//            this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
//	        this.entry_object.setDt_info(this.datetime);
            this.entry_object.setChanged(System.currentTimeMillis());
//            this.entry_object.setComment(createComment());
	    }
	    */
	}
	
	
	
	/**
	 * Process: Normal
	 */
	public static final int PROCESS_NORMAL = 1;

    /**
     * Process: Pump
     */
	public static final int PROCESS_PUMP = 2;
	
	
	
	
	
	/** 
	 * To String
	 */
	public String toString()
	{
	    //OutputUtil o= null;
	    return "PumpValueEntryConfig " + this.key + " = " + this.value;
	} 

	
	
	/**
	 * Set Key
	 * 
	 * @param _key
	 */
	public void setKey(String _key)
	{
	    this.key = _key;
	}
	
    /**
     * Set Value
     * 
     * @param _value
     */
    public void setValue(String _value)
    {
        this.value = _value;
    }
	
	
    /**
     * Set Group 
     * 
     * @param grp
     */
    public void setGroup(int grp)
    {
        this.group = grp;
    }
    

    
    /**
     * Get Key
     * 
     * @return
     */
    public String getKey()
    {
        return this.key;
    }
    
    /**
     * Get Value of object
     * 
     * @return
     */
    public String getValue()
    {
        return this.value;
    }
    
    
    /**
     * Get Group
     * 
     * @return
     */
    public int getGroup()
    {
        return this.group;
    }
    
    
    
    /** 
     * DbAdd
     */
    public String DbAdd(Session sess) throws Exception
    {
        // FIXME:  Not used
        Transaction tx = sess.beginTransaction();

        PumpProfileH pd = new PumpProfileH();

        pd.setId(this.getId());
        pd.setName(this.getName());
        pd.setBasal_base(this.getBasal_base());
        pd.setBasal_diffs(this.getBasal_diffs());
        pd.setActive_from(this.getActive_from());
        pd.setActive_till(this.getActive_till());
        pd.setExtended("SOURCE=" + this.source);
        pd.setPerson_id(this.getPerson_id());
        pd.setComment(this.getComment());
        pd.setChanged(System.currentTimeMillis());
        
        Long id = (Long)sess.save(pd);
        tx.commit();

        pd.setId(id.longValue());
        
        return ""+id.longValue();
    }


    /** 
     * DbDelete
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        // FIXME:  Not used

        Transaction tx = sess.beginTransaction();

        PumpProfileH pd = (PumpProfileH)sess.get(PumpProfileH.class, new Long(this.getId()));
        sess.delete(pd);
        tx.commit();

        return true;
    }

    /** 
     * Db Edit
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        // FIXME:  Not used

        if (!this.hasChanged())
            return false;

        PumpProfileH pd = (PumpProfileH)sess.get(PumpProfileH.class, new Long(this.getId()));

//        pd.setId(this.getId());
        pd.setName(this.getName());
        pd.setBasal_base(this.getBasal_base());
        pd.setBasal_diffs(this.getBasal_diffs());
        pd.setActive_from(this.getActive_from());
        pd.setActive_till(this.getActive_till());
        pd.setExtended(this.getExtended());
        pd.setPerson_id(this.getPerson_id());
        pd.setComment(this.getComment());
        pd.setChanged(System.currentTimeMillis());

        
        
        return true;
    }


    /** 
     * Db Get
     */
    public boolean DbGet(Session sess) throws Exception
    {
        // FIXME:  Not used

        PumpProfileH pd = (PumpProfileH)sess.get(PumpProfileH.class, new Long(this.getId()));

        this.setId(pd.getId());
        this.setName(pd.getName());
        this.setBasal_base(pd.getBasal_base());
        this.setBasal_diffs(pd.getBasal_diffs());
        this.setActive_from(pd.getActive_from());
        this.setActive_till(pd.getActive_till());
        this.setExtended(pd.getExtended());
        this.setPerson_id(pd.getPerson_id());
        this.setComment(pd.getComment());
        this.setChanged(pd.getChanged());
        
        return true;
    }


    /**
     * Db Has Children
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    /** 
     * Get Action
     */
    public int getAction()
    {
        return 0;
    }


    /** 
     * Get Object Name
     */
    public String getObjectName()
    {
        // FIXME:  Not used
        return "PumpConfigurationH";
    }


    /**
     * Is Debug Mode
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
        return "" + this.getId();
    }
    
    boolean changed = false;




    /**
     * Get Column Value
     * 
     * @param index
     * @return
     */
    public Object getColumnValue(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Get Db Objects
     * 
     * @return ArrayList of elements extending GGCHibernateObject
     */
    public ArrayList<? extends GGCHibernateObject> getDbObjects()
    {
        // TODO Auto-generated method stub
        return null;
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
        switch (index)
        {
            case 0:
                return this.key;

            case 1:
            default:
                return this.value;
        }
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
                return "Configuration:  " + this.key + " = " + this.value;
                
            case OutputWriterType.GGC_FILE_EXPORT:
            {
                /*
                PumpData pd = new PumpData(this);
                try
                {
                    return pd.dbExport();
                }
                catch(Exception ex)
                {
                    log.error("Problem with PumpValuesEntry export !  Exception: " + ex, ex);
                    return "Value could not be decoded for export!";
                }*/
            }
                
        
            default:
                return "Value is undefined";
        
        }
    }
    


    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "PCFG_" + this.key;
    }

    
    /**
     * Get DeviceValuesEntry Name
     * 
     * @return
     */
    public String getDVEName()
    {
        return "PumpValuesEntryProfile";
    }


 
    
    /**
     * Get DateTime (long)
     * 
     * @return
     */
    public long getDateTime()
    {
        return this.getActive_from();
    }
    
    
    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    public void setDateTimeObject(ATechDate dt)
    {
    }
    
    
    

    /**
     * Get DateTime format
     * 
     * @return format of date time (precission)
     */
    public int getDateTimeFormat()
    {
        return ATechDate.FORMAT_DATE_AND_TIME_S;
    }
    
    
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
     * Get Output Type
     * 
     * @return
     */
    public int getOutputType()
    {
        return this.output_type;
    }
    
    
    /**
     * Is Data BG
     * 
     * @see ggc.plugin.output.OutputWriterData#isDataBG()
     */
    
    public boolean isDataBG()
    {
        return false; 
    }


    /**
     * Comparator method, for sorting objects
     * @param _d1 
     * @param _d2 
     * @return 
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compare(DeviceValuesEntryInterface _d1, DeviceValuesEntryInterface _d2)
    {
        if ((!(_d1 instanceof PumpValuesEntryConfig)) || (!(_d2 instanceof PumpValuesEntryConfig)))
            return 0;

        PumpValuesEntryConfig d1 = (PumpValuesEntryConfig)_d1;
        PumpValuesEntryConfig d2 = (PumpValuesEntryConfig)_d2;
        
        if (d1.getGroup()!=d2.getGroup())
        {
            return d2.getGroup() - d1.getGroup();
        }
        else
        {
            return d1.getKey().compareTo(d2.getKey());
        }
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

    
    long old_id;
    
    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     * 
     * @param id_in
     */
    public void setOldId(long id_in)
    {
        this.old_id = id_in;
    }
    
    
    /**
     * Get Old Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getOldId()
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


    /** 
     * Has MultiLine ToolTip
     */
    public boolean hasMultiLineToolTip()
    {
        return true;
    }


    /** 
     * Get MultiLine ToolTip
     */
    public String getMultiLineToolTip()
    {
        return "";
    }


    /** 
     * Get MultiLine ToolTip
     */
    public String getMultiLineToolTip(int index)
    {
        return "" + this.getTableColumnValue(index);
    }

    
    /** 
     * Is Indexed (multiline tooltip)
     */
    public boolean isIndexed()
    {
        return true;
    }


    int multiline_tooltip_type = 1;
    
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


    public void prepareEntry_v2()
    {
    }

     
}	
