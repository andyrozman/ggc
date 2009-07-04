package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.util.DeviceValuesEntryUtil;
import ggc.pump.data.defs.PumpAdditionalDataType;
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


public class PumpValuesEntryExt extends PumpDataExtendedH implements DeviceValuesEntryInterface,   /*PumpValuesEntryAbstract,*/ DatabaseObjectHibernate 
{

    private static final long serialVersionUID = 2300422547257308019L;

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    private int status = 0;
    private int object_status = 0;
    private boolean checked = false;
    @SuppressWarnings("unused")
    private int output_type = 0;
    
    
    //public boolean checked = false;
    //public int status = 1; //MeterValuesEntry.
    
	// pump 
//	long datetime;
//..	int base_type;
//	int sub_type;
//	String value;
//	String profile;
	
	// old
/*	public String bg_str;
	public int bg_unit;
	public boolean checked = false;
	//public
	public Hashtable<String,String> params;
	public int status = 1; //MeterValuesEntry.
	public static I18nControl ic = I18nControl.getInstance(); 
	
	public String bg_original = null;
	public OutputUtil util = new OutputUtil();
*/	
	
    
	
    //PumpAdditionalDataType m_pump_add = null;

    
    
	/**
	 * Constructor
	 * 
	 * @param pump_add
	 */
	/*public PumpValuesEntryExt(PumpAdditionalDataType pump_add)
	{
	    this.m_pump_add = pump_add;
	}*/

	
    /**
     * Constructor
     */
    public PumpValuesEntryExt()
    {
        //m_pump_add = new PumpAdditionalDataType();
    }

    
    /**
     * Constructor
     * 
     * @param pd
     */
    public PumpValuesEntryExt(PumpDataExtendedH pd)
    {
        //m_pump_add = new PumpAdditionalDataType();
        
        this.setId(pd.getId());
        this.setDt_info(pd.getDt_info());
        this.setType(pd.getType());
        this.setValue(pd.getValue());
        this.setExtended(pd.getExtended());
        this.setPerson_id(pd.getPerson_id());
        this.setComment(pd.getComment());
        this.setChanged(pd.getChanged());
        
        
    }
    

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
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.getDt_info());
    }
	
	
    
    
    
	
    
	
	
	
	
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
	 * To String
	 */
	public String toString()
	{
	    //OutputUtil o= null;
	    //return "PumpValueEntryExt [unknown]";
	    
	    StringBuffer sb = new StringBuffer();
	    
        if ((this.getType() != PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB) &&
            (this.getType() != PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DESC))   
        {
    	    sb.append(DataAccessPump.getInstance().getAdditionalTypes().getTypeDescription(this.getType()));
    	    sb.append(": ");
	    
    	    if ((this.getType()==PumpAdditionalDataType.PUMP_ADD_DATA_ACTIVITY) ||
    	        (this.getType()==PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT) ||
    	        (this.getType()==PumpAdditionalDataType.PUMP_ADD_DATA_URINE))
    	    {
    	        sb.append(this.getValue());
    	    }
            else if (this.getType() == PumpAdditionalDataType.PUMP_ADD_DATA_BG)
            {
                if (this.da.m_BG_unit==DataAccessPump.BG_MGDL)
                    sb.append(this.getValue() + " mg/dL");
                else
                {
                    //System.out.println("Displayed BG: " + da.getDisplayedBGString(this.getValue()));
                    sb.append(da.getDisplayedBGString(this.getValue()));
                    //sb.append(this.getValue() + " mmol/L");
                    sb.append(" mmol/L");
                    
                    //da.getBGValueDifferent(DataAccessPump.BG_MGDL, Float.parseFloat(arg0)bg_value)
                }
                    
                //po.setValue(this.num_1.getValue().toString());
            }
            else if (this.getType() == PumpAdditionalDataType.PUMP_ADD_DATA_CH)
            {
                sb.append(this.getValue() + " g");
            }
        }
        else
        {
            if (this.getType() == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD_DB)
                sb.append(ic.getMessage("FOOD_SET_DB") + ":  ");
            else
                sb.append(ic.getMessage("FOOD_SET_DESC") + ":  ");
            
            if ((this.getValue()==null) || (this.getValue().length()==0))
                sb.append(ic.getMessage("NO"));
            else
                sb.append(ic.getMessage("YES"));
        } 
        

	    
	    
	    
	    return sb.toString();
	    
	    
	    
	    
//	    return "PumpValueEntryExt [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}

	

    /** 
     * DbAdd
     */
    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataExtendedH ch = new PumpDataExtendedH();

        //ch.setId(id);
        ch.setDt_info(this.getDt_info());
        ch.setType(this.getType());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPerson_id(this.getPerson_id());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());

        Long id = (Long)sess.save(ch);
        tx.commit();

        ch.setId(id.longValue());
        
        return ""+id.longValue();
    }


    /** 
     * DbDelete
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataExtendedH ch = (PumpDataExtendedH)sess.get(PumpDataExtendedH.class, new Long(this.getId()));
        sess.delete(ch);
        tx.commit();

        return true;
    }

    /** 
     * Db Edit
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        if (!this.hasChanged())
            return false;

        Transaction tx = sess.beginTransaction();
        
        PumpDataExtendedH ch = (PumpDataExtendedH)sess.get(PumpDataExtendedH.class, new Long(this.getId()));

        // TODO: changed check
        //ch.setId(id);
        ch.setDt_info(this.getDt_info());
        ch.setType(this.getType());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPerson_id(this.getPerson_id());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());

        sess.update(ch);
        tx.commit();
        
        return true;
    }


    /** 
     * Db Get
     */
    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
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
        return "PumpDataExtendedH";
    }


    /**
     * Is Debug Mode
     */
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
        //System.out.println("getObjectUID: " + this.getId());
        return "" + this.getId();
    }
    
    boolean changed = false;
	
    
    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    public void setDt_info(long dt_info)
    {
        if (dt_info != getDt_info())
        {
            super.setDt_info(dt_info);
            changed = true;
        }
    }
    
    
    /**
     * Set Value
     *  
     * @param value parameter
     */
    public void setValue(String value) 
    {
        if (!value.equals(this.getValue()))
        {
            super.setValue(value);
            changed = true;
        }
    }


    /**
     * Get DateTime (long)
     * 
     * @return
     */
    public long getDateTime()
    {
        return this.getDt_info();
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
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    public void setDateTimeObject(ATechDate dt)
    {
        this.setDt_info(dt.getATDateTimeAsLong());
        
    }




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
        
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * Get Data As String (for export)
     */
    public String getDataAsString()
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "PE_" + this.getDt_info() + "_" + this.getType();
    }

    
    /**
     * Get DeviceValuesEntry Name
     * 
     * @return
     */
    public String getDVEName()
    {
        return "PumpValuesEntryExt";
    }


    /**
     * Get Value of object
     * 
     * @return
     */
/*    public String getValue()
    {
        return this.get
        return null;
    }
*/
    
    
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
        return false; 
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
    
    
}	
