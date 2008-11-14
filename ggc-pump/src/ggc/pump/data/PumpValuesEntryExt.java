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

package ggc.pump.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.util.DataAccessPump;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATechDate;

//import ggc.db.hibernate.DayValueH;


public class PumpValuesEntryExt extends PumpDataExtendedH implements PumpValuesEntryAbstract, DatabaseObjectHibernate 
{

    private static final long serialVersionUID = 2300422547257308019L;

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    
    public boolean checked = false;
    public int status = 1; //MeterValuesEntry.
    
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
	
	
    PumpAdditionalDataType m_pump_add = null;

    
    
	public PumpValuesEntryExt(PumpAdditionalDataType pump_add)
	{
	    this.m_pump_add = pump_add;
	}

	
    public PumpValuesEntryExt()
    {
        m_pump_add = new PumpAdditionalDataType();
    }

    
    public PumpValuesEntryExt(PumpDataExtendedH pd)
    {
        m_pump_add = new PumpAdditionalDataType();
        
        this.setId(pd.getId());
        this.setDt_info(pd.getDt_info());
        this.setType(pd.getType());
        this.setValue(pd.getValue());
        this.setExtended(pd.getExtended());
        this.setPerson_id(pd.getPerson_id());
        this.setComment(pd.getComment());
        this.setChanged(pd.getChanged());
        
        
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
	
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.getDt_info());
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
	
	
	
	public boolean getCheched()
	{
	    return this.checked;
	}

	public int getStatus()
	{
	    return this.status;
	}
	
	
	
	/*
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
		
	}*/
	
	
	public void prepareEntry()
	{
	    
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
	}
	
	
	public DayValueH getDbObject()
	{
	    return this.entry_object;
	}
	
	/*
	public String createComment()
	{
	    String p = this.getParametersAsString();
	    
	    if ((p==null) || (p.trim().length()==0))
	    {
	        return "PTI";
	    }
	    else
	        return "PTI;" + p;
	    
	    
	}*/
	
	
	public String toString()
	{
	    //OutputUtil o= null;
	    //return "PumpValueEntryExt [unknown]";
	    
	    StringBuffer sb = new StringBuffer();
	    
        if (this.getType() != PumpAdditionalDataType.PUMP_ADD_DATA_FOOD)
        {
    	    sb.append(this.m_pump_add.getTypeDescription(this.getType()));
    	    sb.append(":  ");
        }
	    
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
                System.out.println("Displayed BG: " + da.getDisplayedBGString(this.getValue()));
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
        else if (this.getType() == PumpAdditionalDataType.PUMP_ADD_DATA_FOOD)
        {
            sb.append(ic.getMessage("FOOD_SET") + ":");
            
            if ((this.getValue()==null) || (this.getValue().length()==0))
                sb.append(ic.getMessage("NO"));
            else
                sb.append(ic.getMessage("YES"));
        } 
        

	    
	    
	    
	    return sb.toString();
	    
	    
	    
	    
//	    return "PumpValueEntryExt [date/time=" + this.datetime  + ",bg=" + this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]"; 
	}


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


    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataExtendedH ch = (PumpDataExtendedH)sess.get(PumpDataExtendedH.class, new Long(this.getId()));
        sess.delete(ch);
        tx.commit();

        return true;
    }


    public boolean DbEdit(Session sess) throws Exception
    {
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

        return true;
    }


    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    public int getAction()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getObjectName()
    {
        return "PumpDataExtendedH";
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
