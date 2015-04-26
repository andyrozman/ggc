package ggc.meter.data;

import ggc.core.data.ExtendedDailyValue;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.util.DataAccess;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriterType;
import ggc.plugin.util.DataAccessPlugInBase;

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

public class MeterValuesEntry extends DeviceValuesEntry // extends
                                                        // OutputWriterData
{
    DataAccessMeter da = DataAccessMeter.getInstance();
    I18nControlAbstract m_ic = da.getI18nControlInstance();
    private ATechDate datetime;
    private String bg_str;
    private int bg_unit = DataAccessMeter.BG_MGDL;

    private static Log log = LogFactory.getLog(MeterValuesEntry.class);

    // public boolean checked = false;
    // public
    private Hashtable<String, String> params;
    // public int status = 1; //MeterValuesEntry.
    // private static I18nControl i18nControlAbstract = I18nControl.getInstance();

    private String bg_original = null;
    private OutputUtil util = OutputUtil.getInstance();
    private String value_db = null;

    private static ExtendedHandler eh_dailyValue;

    private float bg_mmolL;
    private int bg_mgdL;

    // private float ch;

    Hashtable<Integer, MeterValuesEntrySpecial> special_entries = null;
    // new Hashtable<Integer, MeterValuesEntrySpecial>();
    int special_entry_first = -1;

    @SuppressWarnings("unused")
    private boolean entry_changed = false;

    /**
     * Entry object
     */
    public DayValueH entry_object = null;


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
        this.bg_unit = DataAccessPlugInBase.BG_MGDL;
        this.entry_object = dv;
        this.object_status = DeviceValuesEntry.OBJECT_STATUS_OLD;

        if (dv.getCh() > 0.0)
        {
            this.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH, "" + dv.getCh());
        }

        this.loadSpecialEntries(dv.getExtended());
    }

    /**
     * Constructor
     * 
     * @param mves
     */
    public MeterValuesEntry(MeterValuesEntrySpecial mves)
    {
        this.datetime = new ATechDate(this.getDateTimeFormat(), mves.datetime_tag);
        this.source = mves.source;

        // System.out.println("Source:" + this.source);

        this.addSpecialEntry(mves);
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
     * Add Parameter
     * 
     * @param key
     * @param value
     */
    public void addParameter(String key, String value)
    {
        if (value.equals("_") || value.trim().length() == 0)
            return;

        if (params == null)
        {
            params = new Hashtable<String, String>();
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
        if (this.bg_unit == OutputUtil.BG_MGDL)
        {
            this.value_db = value;
            bg_mgdL = Integer.parseInt(this.value_db);
            bg_mmolL = this.util.getBGValueDifferent(OutputUtil.BG_MGDL, bg_mgdL);
        }
        else
        {
            this.value_db = "" + (int) this.util.getBGValueDifferent(OutputUtil.BG_MMOL, Float.parseFloat(value));

            // DataAccess.getInstance().
            bg_mmolL = Float.parseFloat(DataAccess.Decimal1Format.format(Float.parseFloat(value.replace(",", ".")))
                    .replace(",", "."));
            bg_mgdL = (int) this.util.getBGValueDifferent(OutputUtil.BG_MMOL, bg_mmolL);

        }

        if (this.bg_original == null)
        {
            this.setDisplayableBGValue(value);
        }
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

        if (this.bg_original == null)
            return null;
        else
        {
            if (this.bg_unit == OutputUtil.BG_MMOL)
            {
                if (st == OutputUtil.BG_MMOL)
                    return this.bg_original;
                else
                    return ""
                            + (int) this.util.getBGValueDifferent(OutputUtil.BG_MMOL,
                                Float.parseFloat(this.bg_original));
            }
            else
            {
                if (st == OutputUtil.BG_MGDL)
                    return this.bg_original;
                else
                    return DataAccessPlugInBase.Decimal1Format.format(this.util.getBGValueDifferent(OutputUtil.BG_MGDL,
                        Float.parseFloat(this.bg_original)));
            }
        }
    }

    /**
     * Get Parameter as String
     * @return
     */
    public String getParametersAsString()
    {
        if (this.params == null)
            return "";

        StringBuffer sb = new StringBuffer();

        for (java.util.Enumeration<String> en = this.params.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            sb.append(key + "=" + this.params.get(key) + ";");
        }

        return sb.substring(0, sb.length() - 1);

    }

    /**
     * Prepare Entry
     */
    @Override
    public void prepareEntry()
    {
        if (this.object_status == DeviceValuesEntry.OBJECT_STATUS_OLD)
            return;
        else if (this.object_status == DeviceValuesEntry.OBJECT_STATUS_EDIT)
        {
            if (this.bg_original != null)
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            }

            // processSpecialEntries();
            // this.loadSpecialEntries(this.entry_object.getExtended());
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

            if (this.bg_original != null)
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            }

            // this.loadSpecialEntries(this.entry_object.getExtended());

            // processSpecialEntries();
            this.saveSpecialEntries();

            /*
             * if (this.special_entry)
             * this.entry_object.setExtended(this.getSpecialEntryDbEntry()+";" +
             * "SOURCE=" + DataAccessMeter.getInstance().getSourceDevice());
             * else
             * {
             * this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil
             * .BG_MGDL)));
             * this.entry_object.setExtended("SOURCE=" +
             * DataAccessMeter.getInstance().getSourceDevice());
             * }
             */

            // this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            this.entry_object.setDt_info(this.getDateTime());
            this.entry_object.setChanged(System.currentTimeMillis());
            // this.entry_object.setExtended("SOURCE=" +
            // DataAccessMeter.getInstance().getSourceDevice());
            this.entry_object.setComment(createComment());
        }
    }

    /**
     * Entry: None
     */
    public static final int ENTRY_NONE = 0;

    /**
     * Entry: BG
     */
    public static final int ENTRY_BG = 1;

    /**
     * Entry: Special
     */
    public static final int ENTRY_SPECIAL = 2;

    /**
     * Entry: Combined
     */
    public static final int ENTRY_COMBINED = 3;




    /**
     * Prepare Entry [Framework v2]
     */
    @Override
    public void prepareEntry_v2()
    {
        // SPECIAL_METER_FLAGS
        // we use this for old values, to correctly read extended flags, if you
        // used special meters codes
        // (non BG), then you will need to modify this

        if (eh_dailyValue == null)
        {
            eh_dailyValue = DataAccessMeter.getInstance()
                    .getExtendedHandler(DataAccess.EXTENDED_HANDLER_DailyValuesRow);
        }

        if (this.entry_object == null)
            return;

        this.loadSpecialEntries(this.entry_object.getExtended());

    }

    /**
     * This is used just for compliance with old Meter code. This method is deprecated, but since Meter Tool
     * is still not fully switched over to Framework v2, we need this method.
     *  
     * @deprecated
     * @return
     */
    @Deprecated
    public DayValueH getHibernateObject()
    {
        return this.entry_object;
    }

    /*
     * public DayValueH getDbObject()
     * {
     * return this.entry_object;
     * }
     */

    /**
     * Create Comment
     * 
     * @return
     */
    public String createComment()
    {
        String p = this.getParametersAsString();

        if (p == null || p.trim().length() == 0)
            return "";
        else
            return p;
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // OutputUtil o= null;
        return "MeterValuesEntry [date/time=" + this.datetime.getDateTimeString() + ",bg=" + this.bg_str + " "
                + OutputUtil.getBGUnitName(this.bg_unit) + "]";
    }

    /**
     * Get Data As String (for non-db exports)
     */

    public String getDataAsString()
    {
        switch (output_type)
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
                        val = (int) OutputUtil.getInstance().getBGValueDifferent(this.getBgUnit(), fl);

                    }
                    else
                    {
                        try
                        {
                            val = Integer.parseInt(this.getBgValue());
                        }
                        catch (Exception ex)
                        {
                            val = 0;
                        }
                    }

                    String parameters = this.getParametersAsString();

                    /*
                     * if (parameters.equals(""))
                     * System.out.println(mve.getDateTime().getDateTimeString()
                     * + " = " + mve.getBgValue() + " " +
                     * this.out_util.getBGTypeName(mve.getBgUnit()));
                     * else
                     * System.out.println(mve.getDateTime().getDateTimeString()
                     * + " = " + mve.getBgValue() + " " +
                     * this.out_util.getBGTypeName(mve.getBgUnit()) +
                     * " Params: " + parameters );
                     */

                    // Columns: id; dt_info; bg; ins1; ins2; ch; meals_ids;
                    // extended; person_id; comment; changed

                    String v = "0|" + this.getDateTime() + "|" + val + "|0.0|0.0|";

                    if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                    {
                        v += this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value
                                .replace(',', '.');
                    }
                    else
                    {
                        v += "0.0";
                    }

                    v += "|null|" + this.createExtendedValueDailyValuesH() + "|1|" + parameters + "|"
                            + System.currentTimeMillis();

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
                return this.bg_mgdL; // mve.getBGValue(DataAccessMeter.BG_MMOL);

            case 2:
                return this.bg_mmolL; // mve.getBGValue(DataAccessMeter.BG_MGDL);

            case 3:
                return this.getStatusType();

            case 4:
                return this.getChecked();

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
                return this.getStatusType();

            case 4:
                return getChecked();

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

        if (this.entry_object != null)
        {
            lst.add(this.entry_object);
        }

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

    public void setStatusType(DeviceEntryStatus status)
    {

    }

    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        if (this.getEntryType() == MeterValuesEntry.ENTRY_BG)
            return "MVE_" + this.datetime.getATDateTimeAsLong() + "_BG";
        else if (this.getEntryType() == MeterValuesEntry.ENTRY_SPECIAL)
            return "MVE_" + this.datetime.getATDateTimeAsLong() + "_SP_"
                    + this.special_entries.get(this.special_entry_first).special_entry_id;
        else
            return "MVE_" + this.datetime.getATDateTimeAsLong() + "_CMB";
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
        if (this.object_status == DeviceValuesEntry.OBJECT_STATUS_OLD)
        {
            log.debug("Exiting. Status was old. Action was: " + act);
            return;
        }
        else if (this.object_status == DeviceValuesEntry.OBJECT_STATUS_EDIT)
        {
            Transaction tx = sess.beginTransaction();

            if (this.hasSpecialEntries())
            {
                if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                {
                    this.entry_object.setCh(Float.parseFloat(this.special_entries
                            .get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.')));
                }
            }

            this.entry_object.setExtended(this.createExtendedValueDailyValuesH());

            if (this.hasBgEntry())
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            }

            this.entry_object.setChanged(System.currentTimeMillis());
            this.entry_object.setComment(createComment());
            this.entry_object.setPerson_id((int) DataAccessMeter.getInstance().getCurrentUserId());
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
            this.entry_object.setPerson_id((int) DataAccessMeter.getInstance().getCurrentUserId());

            if (this.hasSpecialEntries())
            {
                if (this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
                {
                    this.entry_object.setCh(Float.parseFloat(this.special_entries
                            .get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).special_entry_value.replace(',', '.')));
                }
            }

            this.entry_object.setExtended(this.createExtendedValueDailyValuesH());

            if (this.hasBgEntry())
            {
                this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));
            }

            this.entry_object.setDt_info(this.getDateTime());
            this.entry_object.setChanged(System.currentTimeMillis());
            this.entry_object.setComment(createComment());

            log.debug("Added. Status was Add. Action was: " + act);
            Long id = (Long) sess.save(this.entry_object);

            // System.out.println("Dt: " +
            // this.getDateTimeObject().getDateTimeString() +
            // this.getBgValue());
            // System.out.println("Add: Id=" + id.longValue());

            tx.commit();

            this.setId(id.longValue());

            // return "" + id.longValue();

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
            sb.append("BG=" + this.getBGValue(DataAccessPlugInBase.BG_MGDL) + ";");
        }

        if (this.hasSpecialEntries() && this.special_entries.containsKey(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH))
        {
            sb.append("CH="
                    + this.special_entries.get(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH).getValueValue()
                            .replace(',', '.') + ";");
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

    // ---
    // --- Special entries
    // ---

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
    // public static final int SPECIAL_ENTRY_URINE_MMOLL = 1;

    /**
     * Special Entry: Urine - Ketones (mmol/L)
     */
    // public static final int SPECIAL_ENTRY_URINE_MGDL = 2;

    /**
     * Special Entry: CH
     */
    // public static final int SPECIAL_ENTRY_CH = 3;

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

    /**
     * Has BG entry
     * 
     * @return
     */
    public boolean hasBgEntry()
    {
        return this.bg_original != null;
    }

    /**
     * Has special entries
     * 
     * @return
     */
    public boolean hasSpecialEntries()
    {
        if (this.special_entries == null)
            return false;
        else
            return this.special_entries.size() != 0;
    }

    Hashtable<String, String> ht_ext = null;

    private void loadSpecialEntries(String s)
    {
        // extend this method if you wish to extended this class to use other
        // DailyValuesExtended entries
        this.ht_ext = this.da.getExtendedDailyValueHandler().loadExtended(s);

        // FIXME
        // ExtendedDailyValue

        // System.out.println("HtExt: " + ht_ext);

        for (Enumeration<String> en = ht_ext.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            if (key.equals(da.getExtendedDailyValueHandler().getExtendedTypeDescription(
                ExtendedDailyValue.EXTENDED_URINE)))
            {
                // urine is only one that is not uniquely processed
                String val = ht_ext.get(key);

                this.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_URINE_COMBINED, val);

                /*
                 * if (val.toLowerCase().contains("mmol"))
                 * {
                 * this.special_entries.put(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_MMOLL,
                 * new MeterValuesEntrySpecial(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_MMOLL, val));
                 * }
                 * else if (val.toLowerCase().contains("mg"))
                 * {
                 * this.special_entries.put(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_MGDL,
                 * new MeterValuesEntrySpecial(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_MGDL, val));
                 * }
                 * else
                 * {
                 * this.special_entries.put(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_DESCRIPTIVE,
                 * new MeterValuesEntrySpecial(MeterValuesEntrySpecial.
                 * SPECIAL_ENTRY_URINE_MGDL, val));
                 * }
                 */
            }
            else if (key.equals(da.getExtendedDailyValueHandler().getExtendedTypeDescription(
                ExtendedDailyValue.EXTENDED_SOURCE)))
            {
                this.source = ht_ext.get(key);
                // System.out.println("SSSS: " + this.source);
            }
            /*
             * else
             * {
             * We should do automatic processing here, but for now not
             * neccesary, as we have only urine
             * }
             */
        }
    }

    private void saveSpecialEntries()
    {
        // this.da.getExtendedDailyValueHandler().setExtendedValue(type, val,
        // ht)
        // ExtendedDailyValue

        // extend this method if you wish to extended this class to use other
        // DailyValuesExtended entries

        // FIXME
    }

    /**
     * Get Extended Type Description (if we use extended interface, this is type description)
     * 
     * @return
     */
    public String getExtendedTypeDescription()
    {

        if (this.getEntryType() == MeterValuesEntry.ENTRY_NONE)
            return DataAccessMeter.getInstance().getI18nControlInstance().getMessage("INVALID_DATA");
        else if (this.getEntryType() == MeterValuesEntry.ENTRY_BG)
            return DataAccessMeter.getInstance().getI18nControlInstance().getMessage("BG");
        else if (this.getEntryType() == MeterValuesEntry.ENTRY_SPECIAL)
        {
            MeterValuesEntrySpecial mves = this.special_entries.elements().nextElement();
            return DataAccessMeter.getInstance().getI18nControlInstance().getMessage(mves.getTypeDescription());
        }
        else
            return DataAccessMeter.getInstance().getI18nControlInstance().getMessage("MULTIPLE");

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

        if (this.getEntryType() == MeterValuesEntry.ENTRY_NONE)
            return "No data";
        else if (this.getEntryType() == MeterValuesEntry.ENTRY_BG)
        {
            if (both_bg)
                return this.bg_mgdL + " mg/dL (" + DataAccessPlugInBase.Decimal1Format.format(this.bg_mmolL)
                        + " mmol/L)";
            else
                return this.getBgValue() + " " + OutputUtil.getBGTypeNameStatic(this.getBgUnit());
        }
        else if (this.getEntryType() == MeterValuesEntry.ENTRY_SPECIAL)
        {
            MeterValuesEntrySpecial mves = this.special_entries.elements().nextElement();
            return mves.getPackedValue(); // .getExtendedFreetypeDescription();
        }
        else
        {
            StringBuffer sb = new StringBuffer();
            boolean first = true;

            if (this.hasBgEntry())
            {
                sb.append("BG: " + this.getBgValue() + " " + OutputUtil.getBGTypeNameStatic(this.getBgUnit()));
                first = false;
            }

            for (Enumeration<MeterValuesEntrySpecial> en = this.special_entries.elements(); en.hasMoreElements();)
            {
                MeterValuesEntrySpecial el = en.nextElement();

                if (el.special_entry_id != MeterValuesEntrySpecial.SPECIAL_ENTRY_BG)
                {
                    if (first)
                    {
                        first = false;
                    }
                    else
                    {
                        sb.append(", ");
                    }

                    sb.append(el.getExtendedFreetypeDescription());
                }
            }

            return sb.toString();
        }
    }

    /**
     * Get Allowed Pump Mapped Types
     * 
     * @return
     */
    public Hashtable<String, String> getAllowedPumpMappedTypes()
    {
        return MeterValuesEntrySpecial.getAllowedPumpMappedTypes();
    }

    /**
     * Get Entry Type
     * 
     * @return
     */
    public int getEntryType()
    {
        if (!this.hasBgEntry())
        {
            if (!this.hasSpecialEntries())
                return ENTRY_NONE;
            else if (this.special_entries.size() == 1)
                return ENTRY_SPECIAL;
            else
                return ENTRY_COMBINED;
        }
        else
        {
            if (!this.hasSpecialEntries())
                return ENTRY_BG;
            else
                return ENTRY_COMBINED;
        }
    }

    /**
     * Create data for extended field in database (special entries without CH)
     * @return
     */
    public String createExtendedValueDailyValuesH()
    {
        // we need to ignore ch, all others are transfered

        // FIXME

        String ext = "";

        if (this.special_entries != null)
        {
            for (int i = 1; i <= MeterValuesEntrySpecial.SPECIAL_ENTRY_MAX; i++)
            {
                int key = i;

                if (key != MeterValuesEntrySpecial.SPECIAL_ENTRY_CH)
                {
                    if (this.special_entries.containsKey(key))
                    {
                        ext += this.special_entries.get(key).getSpecialEntryDbEntry() + ";";
                    }

                    /*
                     * try
                     * {
                     * ext +=
                     * this.special_entries.get(key).getSpecialEntryDbEntry() +
                     * ";";
                     * }
                     * catch(Exception ex)
                     * {
                     * //System.out.println
                     * System.exit(1);
                     * return null;
                     * }
                     */
                }
            }
        }

        ext += "SOURCE=" + this.source;

        return ext;

    }

    /**
     * Add Special Entry All (we always append)
     * 
     * @param coll
     */
    public void addSpecialEntryAll(ArrayList<MeterValuesEntrySpecial> coll)
    {
        for (int i = 0; i < coll.size(); i++)
        {
            MeterValuesEntrySpecial mves = coll.get(i);
            this.addSpecialEntry(mves);
        }
    }

    /**
     * Add Special Entry
     * 
     * @param mves
     */
    public void addSpecialEntry(MeterValuesEntrySpecial mves)
    {
        if (mves.special_entry_id == MeterValuesEntrySpecial.SPECIAL_ENTRY_BG)
        {
            this.setBgUnit(DataAccessPlugInBase.BG_MGDL);
            this.setBgValue(mves.special_entry_value);
        }
        else
        {
            if (this.special_entries == null)
            {
                this.special_entries = new Hashtable<Integer, MeterValuesEntrySpecial>();
                this.special_entry_first = mves.special_entry_id;
            }

            if (this.special_entries.containsKey(mves.special_entry_id))
            {
                this.special_entries.get(mves.special_entry_id).special_entry_value = mves.special_entry_value;
            }
            else
            {
                this.special_entries.put(mves.special_entry_id, mves);
            }
        }
    }

    /**
     * Get Data Entries as MVE Special
     * 
     * @return
     */
    public ArrayList<MeterValuesEntrySpecial> getDataEntriesAsMVESpecial()
    {

        ArrayList<MeterValuesEntrySpecial> list = new ArrayList<MeterValuesEntrySpecial>();

        // System.out.println("getDataEntriesAsMVESpecial::Source:" +
        // this.source);

        if (special_entries != null)
        {
            for (Enumeration<Integer> en = this.special_entries.keys(); en.hasMoreElements();)
            {
                int key = en.nextElement();

                MeterValuesEntrySpecial mves = this.special_entries.get(key);
                mves.datetime_tag = this.getDateTime();
                mves.source = this.source;
                list.add(mves);
            }
        }

        if (this.hasBgEntry())
        {
            // System.out.println("BG: " +
            // this.getBGValue(DataAccessMeter.BG_MGDL));
            MeterValuesEntrySpecial mves = new MeterValuesEntrySpecial(MeterValuesEntrySpecial.SPECIAL_ENTRY_BG,
                    this.getBGValue(DataAccessPlugInBase.BG_MGDL));
            mves.datetime_tag = this.getDateTime();
            mves.source = this.source;
            list.add(mves);
        }

        return list;
    }

}
