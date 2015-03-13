package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.graph.data.GraphValue;
import ggc.plugin.graph.data.GraphValuesCapable;
import ggc.plugin.output.OutputWriterType;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.plugin.util.DeviceValuesEntryUtil;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.graph.PumpGraphContext;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;

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

public class PumpValuesEntryExt extends PumpDataExtendedH implements PumpValuesEntryInterface,
        DatabaseObjectHibernate, GraphValuesCapable
{
    private static final long serialVersionUID = 2300422547257308019L;

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    private int status = 0;
    private int object_status = 0;
    private boolean checked = false;
    private int output_type = 0;



    PumpAdditionalDataType pumpAdditionalDataType;

    /**
     * Constructor
     * 
     * @param pump_add
     */
    /*
     * public PumpValuesEntryExt(PumpAdditionalDataType pump_add)
     * {
     * this.pumpAdditionalDataType = pump_add;
     * }
     */

    /**
     * Constructor
     */
    public PumpValuesEntryExt()
    {
        this((String) null);
        // pumpAdditionalDataType = new PumpAdditionalDataType();
    }

    /**
     * Constructor
     * 
     * @param src 
     */
    public PumpValuesEntryExt(String src)
    {
        this.source = (src==null) ? da.getSourceDevice() : src;
        this.setPerson_id(da.getCurrentUserIdAsInt());
    }

    /**
     * Constructor
     * 
     * @param pd
     */
    public PumpValuesEntryExt(PumpDataExtendedH pd)
    {
        // pumpAdditionalDataType = new PumpAdditionalDataType();

        this.setId(pd.getId());
        this.setDt_info(pd.getDt_info());
        this.setType(pd.getType());

        this.setValue(pd.getValue());
        this.setExtended(pd.getExtended());
        this.setPerson_id(pd.getPerson_id());
        this.setComment(pd.getComment());
        this.setChanged(pd.getChanged());

    }

    public void setType(int type)
    {
        super.setType(type);
        this.pumpAdditionalDataType = PumpAdditionalDataType.getByCode(type);
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
     * public void setDateTime(long dt)
     * {
     * this.datetime = dt;99999
     * }
     * public long getDateTime()
     * {
     * return this.datetime;
     * }
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
    }


    public PumpAdditionalDataType getTypeEnum()
    {
        if (pumpAdditionalDataType==null)
        {
            pumpAdditionalDataType = PumpAdditionalDataType.getByCode(this.getType());
        }

        return this.pumpAdditionalDataType;
    }



    /** 
     * To String
     */
    @Override
    public String toString()
    {
        // OutputUtil o= null;
        // return "PumpValueEntryExt [unknown]";

        StringBuffer sb = new StringBuffer();

        if (this.getTypeEnum() != PumpAdditionalDataType.FoodDb
                && this.getTypeEnum() != PumpAdditionalDataType.FoodDescription)
        {
            sb.append(this.getTypeEnum().getTranslation());
            sb.append(": ");

            if (this.getTypeEnum() == PumpAdditionalDataType.Activity
                    || this.getTypeEnum() == PumpAdditionalDataType.Comment
                    || this.getTypeEnum() == PumpAdditionalDataType.Urine)
            {
                sb.append(this.getValue());
            }
            else if (this.getTypeEnum() == PumpAdditionalDataType.BloodGlucose)
            {
                if (this.da.m_BG_unit == DataAccessPlugInBase.BG_MGDL)
                {
                    sb.append(this.getValue() + " mg/dL");
                }
                else
                {
                    // System.out.println("Displayed BG: " +
                    // da.getDisplayedBGString(this.getValue()));
                    sb.append(da.getDisplayedBGString(this.getValue()));
                    // sb.append(this.getValue() + " mmol/L");
                    sb.append(" mmol/L");

                    // da.getBGValueDifferent(DataAccessPump.BG_MGDL,
                    // Float.parseFloat(arg0)bg_value)
                }

                // po.setValue(this.num_1.getValue().toString());
            }
            else if (this.getTypeEnum() == PumpAdditionalDataType.Carbohydrates)
            {
                sb.append(this.getValue() + " g");
            }
        }
        else
        {
            if (this.getTypeEnum() == PumpAdditionalDataType.FoodDescription)
            {
                sb.append(ic.getMessage("FOOD_SET_DB") + ":  ");
            }
            else
            {
                sb.append(ic.getMessage("FOOD_SET_DESC") + ":  ");
            }

            if (this.getValue() == null || this.getValue().length() == 0)
            {
                sb.append(ic.getMessage("NO"));
            }
            else
            {
                sb.append(ic.getMessage("YES"));
            }
        }

        return sb.toString();

        // return "PumpValueEntryExt [date/time=" + this.datetime + ",bg=" +
        // this.bg_str + " " + OutputUtil.getBGUnitName(this.bg_unit) + "]";
    }

    /** 
     * DbAdd
     */
    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataExtendedH ch = new PumpDataExtendedH();

        // ch.setId(id);
        ch.setDt_info(this.getDt_info());
        ch.setType(this.getType());
        ch.setValue(this.getValue());
        ch.setExtended("SOURCE=" + this.source);
        ch.setPerson_id(this.getPerson_id());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());

        Long id = (Long) sess.save(ch);
        tx.commit();

        ch.setId(id.longValue());

        return "" + id.longValue();
    }

    /** 
     * DbDelete
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataExtendedH ch = (PumpDataExtendedH) sess.get(PumpDataExtendedH.class, this.getId());
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

        PumpDataExtendedH ch = (PumpDataExtendedH) sess.get(PumpDataExtendedH.class, this.getId());

        // TODO: changed check
        // ch.setId(id);
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
        // System.out.println("getObjectUID: " + this.getId());
        return "" + this.getId();
    }

    boolean changed = false;

    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    @Override
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
    @Override
    public void setValue(String value)
    {
        if (this.getValue() == null || !value.equals(this.getValue()))
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
        switch (index)
        {
            case 0:
                return getDateTimeObject().getDateTimeString();

            case 1:
                return ic.getMessage("EXT_TYPE_SH");

            case 2:
                return PumpAdditionalDataType.getByCode(this.getType());

            case 3:
                return "";

            case 4:
                return this.getValue();

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
    /*
     * public String getValue()
     * {
     * return this.get
     * return null;
     * }
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
     * Get Type Description
     * 
     * @return
     */
    public String getTypeDesc()
    {
        return PumpAdditionalDataType.getByCode(this.getType()).getTranslation();
    }

    /**
     * Get Data As String
     */
    public String getDataAsString()
    {

        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return this.getDateTimeObject().getDateTimeString() + ":  Extended Type=" + this.getTypeDesc()
                        + ", Value=" + this.getValue() + ", Comment=" + this.getComment();

            case OutputWriterType.GGC_FILE_EXPORT:
                {
                    /*
                     * PumpData pd = new PumpData(this);
                     * try
                     * {
                     * return pd.dbExport();
                     * }
                     * catch(Exception ex)
                     * {
                     * log.error(
                     * "Problem with PumpValuesEntry export !  Exception: " +
                     * ex, ex);
                     * return "Value could not be decoded for export!";
                     * }
                     */
                }

            default:
                return "Value is undefined";

        }
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

    public ArrayList<GraphValue> getGraphValues()
    {
        return null;
    }

    public GraphValue getGraphValue()
    {
        int map = this.da.getGraphContext().getObjectMapping(PumpGraphContext.OBJECT_PUMP_EXT_VALUES, this.getType());

        if (map != -1)
            return new GraphValue(this.getDt_info(), map, this.getValue());
        else
            return null;
    }

}
