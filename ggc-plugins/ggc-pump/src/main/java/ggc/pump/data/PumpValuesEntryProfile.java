package ggc.pump.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.output.OutputWriterType;
import ggc.plugin.util.DeviceValuesEntryUtil;
import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.util.DataAccessPump;

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

public class PumpValuesEntryProfile extends PumpProfileH implements PumpValuesEntryInterface, DatabaseObjectHibernate
{

    private static final long serialVersionUID = 7772340503037499446L;
    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    ArrayList<ProfileSubEntry> sub_entries = new ArrayList<ProfileSubEntry>();

    private DeviceEntryStatus status = DeviceEntryStatus.Unknown;
    private int object_status = 0;
    private boolean checked = false;
    private int output_type = 0;

    private String value_multiline = "";

    // public boolean checked = false;
    // public int status = 1; //MeterValuesEntry.

    // pump
    // long datetime;
    // .. int base_type;
    // int sub_type;
    // String value;
    // String profile;

    // old
    /*
     * public String bg_str;
     * public int bg_unit;
     * public boolean checked = false;
     * //public
     * public Hashtable<String,String> params;
     * public int status = 1; //MeterValuesEntry.
     * public static I18nControl i18nControlAbstract =
     * I18nControl.getInstance();
     * public String bg_original = null;
     * public OutputUtil util = new OutputUtil();
     */

    // PumpAdditionalDataType pumpAdditionalDataType = null;


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
    public PumpValuesEntryProfile()
    {

        this.source = da.getSourceDevice();

        // pumpAdditionalDataType = new PumpAdditionalDataType();
    }


    /**
     * Constructor
     * 
     * @param pd
     */
    public PumpValuesEntryProfile(PumpProfileH pd)
    {
        this.setId(pd.getId());
        this.setName(pd.getName());
        this.setBasalBase(pd.getBasalBase());
        this.setBasalDiffs(pd.getBasalDiffs());
        this.setActiveFrom(pd.getActiveFrom());
        this.setActiveTill(pd.getActiveTill());
        this.setExtended(pd.getExtended());
        this.setPersonId(pd.getPersonId());
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
     * public void setDateTime(long dt)
     * {
     * this.datetime = dt;
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
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.getActiveFrom());
    }

    /*
     * public void addParameter(String key, String value)
     * {
     * if ((value.equals("_")) || (value.trim().length()==0))
     * return;
     * if (params==null)
     * {
     * params = new Hashtable<String,String>();
     * }
     * this.params.put(key, value);
     * }
     */

    /*
     * public boolean getCheched()
     * {
     * return this.checked;
     * }
     * public int getStatus()
     * {
     * return this.status;
     * }
     */


    /*
     * public String getParametersAsString()
     * {
     * if (this.params==null)
     * return "";
     * StringBuffer sb = new StringBuffer();
     * for(java.util.Enumeration<String> en = this.params.keys();
     * en.hasMoreElements(); )
     * {
     * String key = en.nextElement();
     * sb.append(key + "=" + this.params.get(key) + ";");
     * }
     * return sb.substring(0, sb.length()-1);
     * }
     */

    /**
     * Prepare Entry
     */
    public void prepareEntry()
    {
        /*
         * if (this.object_status == PumpValuesEntry.OBJECT_STATUS_OLD)
         * return;
         * else if (this.object_status == PumpValuesEntry.OBJECT_STATUS_EDIT)
         * {
         * //
         * this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil
         * .BG_MGDL)));
         * this.entry_object.setChanged(System.currentTimeMillis());
         * // this.entry_object.setComment(createComment());
         * }
         * else
         * {
         * this.entry_object = new DayValueH();
         * this.entry_object.setIns1(0);
         * this.entry_object.setIns2(0);
         * this.entry_object.setCh(0.0f);
         * //
         * this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil
         * .BG_MGDL)));
         * // this.entry_object.setDtInfo(this.datetime);
         * this.entry_object.setChanged(System.currentTimeMillis());
         * // this.entry_object.setComment(createComment());
         * }
         */
    }


    /**
     * Add Profile Sub Entry
     * 
     * @param pse
     */
    public void addProfileSubEntry(ProfileSubEntry pse)
    {
        this.sub_entries.add(pse);
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
     * Process Profile Sub Entry
     * 
     * @param type
     */
    public void processProfileSubEntries(int type)
    {
        Collections.sort(this.sub_entries);

        StringBuffer sb_multi = new StringBuffer("<html>");

        if (type == PROCESS_PUMP)
        {
            for (int i = 1; i < this.sub_entries.size(); i++)
            {
                long dt_end = this.sub_entries.get(i).timeStart;

                ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, dt_end);
                atd.add(Calendar.MINUTE, -1);

                this.sub_entries.get(i - 1).timeEnd = (int) atd.getATDateTimeAsLong();
            }
        }

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.sub_entries.size(); i++)
        {
            sb.append(this.sub_entries.get(i).getPacked());
            sb.append(";");

            sb_multi.append(this.sub_entries.get(i).toString());
            sb_multi.append(";");

            if (i > 0 && i % 5 == 0)
            {
                sb_multi.append("<br>");
            }

        }

        String s = sb.toString();
        s = s.substring(0, s.length() - 1);

        String s_multi = sb_multi.substring(0, sb_multi.length() - 1);
        s_multi += "</html>";

        this.value_multiline = s_multi;

        this.setBasalDiffs(s);

    }


    /**
     * End Entry (create basal diffs entry)
     */
    public void endEntry()
    {
        this.processProfileSubEntries(1);
    }


    /** 
     * To String
     */
    @Override
    public String toString()
    {
        // OutputUtil o= null;
        return "PumpValueEntryProfile [id=" + this.getId() + "] " + this.getBasalDiffs();
    }


    /** 
     * DbAdd
     */
    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpProfileH pd = new PumpProfileH();

        pd.setId(this.getId());
        pd.setName(this.getName());
        pd.setBasalBase(this.getBasalBase());
        pd.setBasalDiffs(this.getBasalDiffs());
        pd.setActiveFrom(this.getActiveFrom());
        pd.setActiveTill(this.getActiveTill());
        pd.setExtended("SOURCE=" + this.source);
        pd.setPersonId(this.getPersonId());
        pd.setComment(this.getComment());
        pd.setChanged(System.currentTimeMillis());

        Long id = (Long) sess.save(pd);
        tx.commit();

        pd.setId(id.longValue());

        return "" + id.longValue();
    }


    /** 
     * DbDelete
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpProfileH pd = (PumpProfileH) sess.get(PumpProfileH.class, new Long(this.getId()));
        sess.delete(pd);
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

        PumpProfileH pd = (PumpProfileH) sess.get(PumpProfileH.class, new Long(this.getId()));

        // pd.setId(this.getId());
        pd.setName(this.getName());
        pd.setBasalBase(this.getBasalBase());
        pd.setBasalDiffs(this.getBasalDiffs());
        pd.setActiveFrom(this.getActiveFrom());
        pd.setActiveTill(this.getActiveTill());
        pd.setExtended(this.getExtended());
        pd.setPersonId(this.getPersonId());
        pd.setComment(this.getComment());
        pd.setChanged(System.currentTimeMillis());

        return true;
    }


    /** 
     * Db Get
     */
    public boolean DbGet(Session sess) throws Exception
    {

        PumpProfileH pd = (PumpProfileH) sess.get(PumpProfileH.class, new Long(this.getId()));

        this.setId(pd.getId());
        this.setName(pd.getName());
        this.setBasalBase(pd.getBasalBase());
        this.setBasalDiffs(pd.getBasalDiffs());
        this.setActiveFrom(pd.getActiveFrom());
        this.setActiveTill(pd.getActiveTill());
        this.setExtended(pd.getExtended());
        this.setPersonId(pd.getPersonId());
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
        return "PumpProfileH";
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
                return ic.getMessage("PROFILE_TYPE_SH");

            case 2:
                return this.getName(); // da.getAdditionalTypes().getTypeDescription(this.getType());

            case 3:
                return ""; // ATechDate.getDateTimeString(this.getDateTimeFormat(),
                           // this.getActiveTill());

            case 4:
                return this.getValue();

            case 5:
                return this.getStatusType();

            case 6:
                return new Boolean(getChecked());

            default:
                return "";
        }
    }


    /** 
     * Get Data As String (for export)
     */
    public String getDataAsString()
    {
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return "Profile=" + this.getName() + ", From="
                        + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.getActiveFrom())
                        + ", Till="
                        + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.getActiveTill());

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
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "PP_" + this.getActiveFrom() + "_" + this.getName();
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
     * Get Value of object
     * 
     * @return
     */
    public String getValue()
    {
        // this.processProfileSubEntries(1);
        return this.getBasalDiffs();
    }


    /**
     * Get DateTime (long)
     * 
     * @return
     */
    public long getDateTime()
    {
        return this.getActiveFrom();
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
    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeSec;
    }


    /**
     *
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
     * {@inheritDoc}
     */
    public DeviceEntryStatus getStatusType()
    {
        return this.status;
    }


    /**
     * {@inheritDoc}
     */
    public void setStatusType(DeviceEntryStatus stat)
    {
        this.status = stat;
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
        return null;
    }


    /** 
     * Get MultiLine ToolTip
     */
    public String getMultiLineToolTip(int index)
    {
        // System.out.println("getMultiLIneToolTip");

        if (index == 4)
            return this.value_multiline;
        else
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
