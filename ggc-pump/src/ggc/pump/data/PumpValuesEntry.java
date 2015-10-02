package ggc.pump.data;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.statistics.StatisticsItem;
import com.atech.misc.statistics.StatisticsObject;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.CodeEnum;

import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.graph.data.GraphValue;
import ggc.plugin.graph.data.GraphValuesCapable;
import ggc.plugin.output.OutputWriterType;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.*;
import ggc.pump.util.DataAccessPump;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: PumpValuesEntry
 * Description: Pump Values Entry, with all data, also
 * statistics item.
 *
 * Author: Andy {andy@atech-software.com}
 */

public class PumpValuesEntry extends DeviceValuesEntry implements StatisticsItem, PumpValuesEntryInterface,
        GraphValuesCapable
{

    private static Log log = LogFactory.getLog(PumpValuesEntry.class);
    private static final long serialVersionUID = -2047203215269156938L;

    DataAccessPump dataAccess; // = DataAccessPump.getInstance();
    I18nControlAbstract m_ic; // = dataAccess.getI18nControlInstance();

    // pump
    long id;
    ATechDate datetime;
    PumpBaseType baseType;
    int sub_type;
    String value;
    String extended;
    // String profile;
    long person_id;
    String comment;
    long changed;
    String source;
    int multiline_tooltip_type = 1;

    private Hashtable<String, String> params;

    private PumpDataH entry_object = null;
    private Map<PumpAdditionalDataType, PumpValuesEntryExt> additional_data = new HashMap<PumpAdditionalDataType, PumpValuesEntryExt>();


    /**
     * Constructor
     *
     * @param tr
     */
    public PumpValuesEntry(boolean tr)
    {
        // dataAccess = DataAccessPump.getInstance();
        // m_ic = dataAccess.getI18nControlInstance();
    }


    /**
     * Constructor
     */
    public PumpValuesEntry()
    {
        this((String) null);
    }


    /**
     * Constructor
     *
     * @param src
     */
    public PumpValuesEntry(String src)
    {
        dataAccess = DataAccessPump.getInstance();
        m_ic = dataAccess.getI18nControlInstance();

        this.id = 0L;
        this.datetime = new ATechDate(this.getDateTimeFormat(), new GregorianCalendar());
        this.baseType = PumpBaseType.None;
        this.sub_type = 0;
        this.value = "";
        this.extended = null;
        this.comment = null;

        this.source = (src == null) ? dataAccess.getSourceDevice() : src;
        this.person_id = dataAccess.getCurrentUserIdAsInt();
    }


    /**
     * Constructor
     *
     * @param baseType
     */
    public PumpValuesEntry(int baseType)
    {
        dataAccess = DataAccessPump.getInstance();
        m_ic = dataAccess.getI18nControlInstance();

        this.id = 0L;
        this.datetime = new ATechDate(this.getDateTimeFormat(), new GregorianCalendar());
        this.baseType = PumpBaseType.getByCode(baseType);
        this.sub_type = 0;
        this.value = "";
        this.extended = null;
        this.comment = null;

        this.source = dataAccess.getSourceDevice();
        this.person_id = dataAccess.getCurrentUserIdAsInt();
    }


    /**
     * Constructor
     *
     * @param pdh
     */
    public PumpValuesEntry(PumpDataH pdh)
    {
        dataAccess = DataAccessPump.getInstance();
        m_ic = dataAccess.getI18nControlInstance();

        // this.entry_object = pdh;
        this.id = pdh.getId();
        this.old_id = pdh.getId();
        this.datetime = new ATechDate(ATechDateType.DateAndTimeSec, pdh.getDt_info());
        this.baseType = PumpBaseType.getByCode(pdh.getBase_type());
        this.sub_type = pdh.getSub_type();
        this.value = pdh.getValue();
        this.extended = pdh.getExtended();
        // loadExtended(pdh.getExtended());
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
        this.additional_data.put(adv.getTypeEnum(), adv);
    }


    /**
     * Get Additional Data
     *
     * @return
     */
    public Map<PumpAdditionalDataType, PumpValuesEntryExt> getAdditionalData()
    {
        return this.additional_data;
    }


    /**
     * Set Sub Type
     *
     * @param sub_type
     * @deprecated use setSubType(CodeEnum) instead.
     */
    @Deprecated
    public void setSubType(int sub_type)
    {
        this.sub_type = sub_type;
    }


    public void setSubType(CodeEnum codeEnum)
    {
        this.sub_type = codeEnum.getCode();
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
        /*
         * if (this.getSubType()==PumpBaseType.PUMP_DATA_BOLUS) { return
         * this.value; } else
         */
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
        if (valuex.equals("_") || (valuex.trim().length() == 0))
        {
            return;
        }

        if (params == null)
        {
            params = new Hashtable<String, String>();
        }

        this.params.put(key, valuex);

    }


    // int base_type;

    /**
     * Get Base Type
     *
     * @return
     */
    public PumpBaseType getBaseType()
    {
        return this.baseType;
    }


    /**
     * Set Base Type
     *
     * @param type
     */
    public void setBaseType(int type)
    {
        this.baseType = PumpBaseType.getByCode(type);
    }


    /**
     * Set Base Type
     *
     * @param type
     */
    public void setBaseType(PumpBaseType type)
    {
        this.baseType = type;
    }


    /**
     * Get Column Value
     */
    @Override
    public Object getColumnValue(int column)
    {
        switch (column)
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
                    // return this.getValue();
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
     *
     * @return
     */
    public String getParametersAsString()
    {
        if (this.params == null)
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (java.util.Enumeration<String> en = this.params.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            sb.append(key + "=" + this.params.get(key) + ";");
        }

        return sb.substring(0, sb.length() - 1);

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
        return baseType.getTranslation();
    }


    /**
     * Get Sub Type
     *
     * @return
     */
    public String getSubTypeString()
    {
        // System.out.println("getSubTypeString [" + this.sub_type + "]");

        if (this.sub_type == 0)
        {
            return "";
        }

        if (this.baseType == PumpBaseType.Basal)
        {
            return PumpBasalType.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.Bolus)
        {
            return PumpBolusType.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.Report)
        {
            return PumpReport.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.Alarm)
        {
            return PumpAlarms.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.Error)
        {
            return PumpErrors.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.Event)
        {
            return PumpEvents.getByCode(this.sub_type).getTranslation();
        }
        else if (this.baseType == PumpBaseType.AdditionalData)
        {
            return PumpAdditionalDataType.getByCode(this.sub_type).getTranslation();
        }
        else
        {
            return "";
        }
    }


    /**
     * Get Additional Display
     *
     * @return
     */
    public String getAdditionalDisplay()
    {
        if (this.additional_data.size() == 0)
        {
            return "";
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            // sb.append("<html>");

            for (PumpValuesEntryExt entry : this.additional_data.values())
            {
                DataAccessPlugInBase.appendToStringBuilder(sb, entry.toString(), "; ");
            }

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
        if (this.additional_data.containsKey(PumpAdditionalDataType.FoodDescription)
                || this.additional_data.containsKey(PumpAdditionalDataType.FoodDb))
        {
            return dataAccess.getI18nControlInstance().getMessage("YES");
        }
        else
        {
            return dataAccess.getI18nControlInstance().getMessage("NO");
        }
    }


    /**
     * Get Additional Display
     *
     * @return
     */
    public String getAdditionalDisplayHTML()
    {
        if (this.additional_data.size() == 0)
        {
            return "";
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            // sb.append("<html>");

            for (PumpValuesEntryExt entry : this.additional_data.values())
            {

                if (entry.getTypeEnum() == PumpAdditionalDataType.FoodDb
                        || entry.getTypeEnum() == PumpAdditionalDataType.FoodDescription)
                {
                    continue;
                }

                DataAccessPump.appendToStringBuilder(sb, entry.toString(), "<br>");
            }

            sb.append("</html>");
            return "<html>" + sb.toString();
        }
    }


    /**
     * Get Value HTML
     *
     * @return Value in HTML format
     */
    public String getValueHTML()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");

        if (this.baseType == PumpBaseType.Basal)
        {
            if (this.sub_type == PumpBasalType.TemporaryBasalRate.getCode())
            {
                String s[] = dataAccess.getParsedValues(this.value);
                sb.append(String.format("%s: %s<br>%s: %s", m_ic.getMessage("DURATION"), s[0],
                    m_ic.getMessage("AMOUNT"), s[1]));
            }
            else
            {
                sb.append(this.getValue());
            }

        }
        else if (this.baseType == PumpBaseType.Bolus)
        {
            if (this.sub_type == PumpBolusType.Extended.getCode())
            {
                String s[] = dataAccess.getParsedValues(this.getValue());

                if (s.length == 1)
                {
                    // old format
                    sb.append(String.format("%s: %s<br>%s: %s", m_ic.getMessage("SQUARE_AMOUNT"), s[0],
                        m_ic.getMessage("DURATION"), "??"));
                }
                else
                {
                    sb.append(String.format("%s: %s<br>%s: %s", m_ic.getMessage("SQUARE_AMOUNT"), s[0],
                        m_ic.getMessage("DURATION"), s[1]));
                }
            }
            else if (this.sub_type == PumpBolusType.Multiwave.getCode())
            {
                String s[] = dataAccess.getParsedValues(this.getValue());
                if (s.length == 2)
                {
                    // old format
                    sb.append(String.format("%s: %s<br>%s: %s<br>%s: %s", m_ic.getMessage("IMMEDIATE_AMOUNT"), s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"), s[1], m_ic.getMessage("DURATION"), "??"));
                }
                else
                {
                    sb.append(String.format("%s: %s<br>%s: %s<br>%s: %s", m_ic.getMessage("IMMEDIATE_AMOUNT"), s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"), s[1], m_ic.getMessage("DURATION"), s[2]));
                }
            }
            else
            {
                sb.append(this.getValue());
            }
        }
        else
        {
            sb.append(this.getValue());
        }

        sb.append("</html>");
        return sb.toString();
    }


    /**
     * Get Value Print
     *
     * @return Printable (Displayable) value
     */
    public String getValuePrint()
    {
        StringBuilder sb = new StringBuilder();

        if (this.baseType == PumpBaseType.Basal)
        {
            if (this.sub_type == PumpBasalType.TemporaryBasalRate.getCode())
            {
                String s[] = dataAccess.getParsedValues(this.value);
                sb.append(String.format("%s: %s, %s: %s", m_ic.getMessage("DURATION"), s[0], m_ic.getMessage("AMOUNT"),
                    s[1]));
            }
            else
            {
                sb.append(this.getValue());
            }

        }
        else if (this.baseType == PumpBaseType.Bolus)
        {
            if (this.sub_type == PumpBolusType.Extended.getCode())
            {

                String s[] = dataAccess.getParsedValues(this.value);

                if (s.length == 1)
                {
                    // old format
                    sb.append(String.format("%s: %s, %s: %s", m_ic.getMessage("SQUARE_AMOUNT"), s[0],
                        m_ic.getMessage("DURATION"), "??"));
                }
                else if (s.length == 2)
                {
                    sb.append(String.format("%s: %s, %s: %s", m_ic.getMessage("SQUARE_AMOUNT"), s[0],
                        m_ic.getMessage("DURATION"), s[1]));
                }
                else
                {
                    sb.append(this.getValue());
                }

            }
            else if (this.sub_type == PumpBolusType.Multiwave.getCode())
            {
                String s[] = dataAccess.getParsedValues(this.value);
                if (s.length == 2)
                {
                    // old format
                    sb.append(String.format("%s: %s, %s: %s, %s: %s", m_ic.getMessage("IMMEDIATE_AMOUNT"), s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"), s[1], m_ic.getMessage("DURATION"), "??"));
                }
                else if (s.length == 3)
                {
                    sb.append(String.format("%s: %s, %s: %s, %s: %s", m_ic.getMessage("IMMEDIATE_AMOUNT"), s[0],
                        m_ic.getMessage("SQUARE_AMOUNT"), s[1], m_ic.getMessage("DURATION"), s[2]));
                }
                else
                {
                    sb.append(this.getValue());
                }
            }
            else
            {
                sb.append(this.getValue());
            }
        }
        else
        {
            sb.append(this.getValue());
        }

        return sb.toString();
    }

    /**
     * Print Additional: All Entries (without food data, just info that food is
     * present)
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
        if (this.additional_data.size() == 0)
        {
            return "";
        }
        else
        {
            StringBuilder sb = new StringBuilder();

            PumpValuesEntryExt foodEntry = null;

            for (PumpValuesEntryExt entry : this.additional_data.values())
            {

                if (type == PRINT_ADDITIONAL_ALL_ENTRIES_WITH_FOOD)
                {
                    if (entry.getTypeEnum() == PumpAdditionalDataType.FoodDescription || //
                            entry.getTypeEnum() == PumpAdditionalDataType.FoodDb)
                    {
                        foodEntry = entry;
                    }
                    else
                    {
                        DataAccessPump.appendToStringBuilder(sb, entry.toString(), "; ");
                    }

                }
                else
                {
                    DataAccessPump.appendToStringBuilder(sb, entry.toString(), "; ");
                }
            }

            if (foodEntry != null)
            {
                if (foodEntry.getTypeEnum() == PumpAdditionalDataType.FoodDescription)
                {
                    DataAccessPump.appendToStringBuilder(sb,
                        m_ic.getMessage("FOOD_DESC_PRINT") + ": " + foodEntry.getValue(), "\n");
                }
                else
                // if
                // (food_key.equals(PumpAdditionalDataType.FoodDb.getTranslation()))
                {
                    // FIXME
                    DataAccessPump.appendToStringBuilder(sb,
                        m_ic.getMessage("FOOD_DB_PRINT") + ": " + m_ic.getMessage("FOOD_DESC_PRINT_NOT_YET"), "\n");
                }
            }

            return sb.toString();
        }
    }


    // /**
    // * Prepare Entry [PlugIn Framework v1]
    // */
    // @Override
    // public void prepareEntry()
    // {
    // System.out.println("prepareEntry not implemented!");
    //
    // /*
    // * if (this.object_status == PumpValuesEntry.OBJECT_STATUS_OLD) return;
    // * else if (this.object_status == PumpValuesEntry.OBJECT_STATUS_EDIT) {
    // * this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.
    // * BG_MGDL))); this.entry_object.setChanged(System.currentTimeMillis());
    // * this.entry_object.setComment(createComment()); } else {
    // * this.entry_object = new DayValueH(); this.entry_object.setIns1(0);
    // * this.entry_object.setIns2(0); this.entry_object.setCh(0.0f);
    // * this.entry_object.setBg(Integer.parseInt(this.getBGValue(OutputUtil.
    // * BG_MGDL))); this.entry_object.setDt_info(this.datetime);
    // * this.entry_object.setChanged(System.currentTimeMillis());
    // * this.entry_object.setComment(createComment()); }
    // */
    // }

    /**
     * Create Comment
     *
     * @return
     */
    public String createComment()
    {
        String p = this.getParametersAsString();

        if ((p == null) || (p.trim().length() == 0))
        {
            return "";
        }
        else
        {
            return p;
        }

    }


    /**
     * To String
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PumpValuesEntry [date/time=" + this.datetime + ", base_type=" + this.getBaseTypeString());

        if (this.getSubType() != 0)
        {
            sb.append(", sub_type=" + this.getSubTypeString());
        }

        if (StringUtils.isNotBlank(this.getValue()))
        {
            sb.append(", value=" + this.getValue());
        }

        if (this.additional_data.size() > 0)
        {
            sb.append(", add_data=" + this.additional_data);
        }

        sb.append("]");

        return sb.toString();
    }


    /**
     * DbAdd - Add this object to database
     *
     * @param sess
     *            Hibernate Session object
     * @throws Exception
     *             (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataH pdh = new PumpDataH();

        pdh.setId(this.id);
        pdh.setDt_info(this.datetime.getATDateTimeAsLong());
        pdh.setBase_type(this.baseType.getCode());
        pdh.setSub_type(this.sub_type);
        pdh.setValue(this.value);
        pdh.setExtended("SOURCE=" + this.source);
        pdh.setPerson_id((int) this.person_id);
        pdh.setComment(this.comment);
        pdh.setChanged(System.currentTimeMillis());
        pdh.setChanged(System.currentTimeMillis());

        Long _id = (Long) sess.save(pdh);
        tx.commit();

        pdh.setId(_id.longValue());

        return "" + _id.longValue();
    }


    /**
     * DbDelete - Delete this object in database
     *
     * @param sess
     *            Hibernate Session object
     * @throws Exception
     *             (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        PumpDataH ch = (PumpDataH) sess.get(PumpDataH.class, this.id);
        sess.delete(ch);
        tx.commit();

        return true;
    }


    /**
     * DbEdit - Edit this object in database
     *
     * @param sess
     *            Hibernate Session object
     * @throws Exception
     *             (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();
        // System.out.println("id: " + old_id);
        PumpDataH pdh = (PumpDataH) sess.get(PumpDataH.class, this.old_id);

        // System.out.println("PumpDataH: " + pdh);

        pdh.setId(this.old_id);
        pdh.setDt_info(this.datetime.getATDateTimeAsLong());
        pdh.setBase_type(this.baseType.getCode());
        pdh.setSub_type(this.sub_type);
        pdh.setValue(this.value);
        pdh.setExtended(this.extended);
        pdh.setPerson_id((int) this.person_id);
        pdh.setComment(this.comment);
        pdh.setChanged(System.currentTimeMillis());

        sess.update(pdh);
        tx.commit();

        return true;
    }


    /**
     * DbGet - Loads this object. Id must be set.
     *
     * @param sess
     *            Hibernate Session object
     * @throws Exception
     *             (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {
        PumpDataH pdh = (PumpDataH) sess.get(PumpDataH.class, this.id);

        this.id = pdh.getId();
        this.datetime = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pdh.getDt_info());
        this.baseType = PumpBaseType.getByCode(pdh.getBase_type());
        this.sub_type = pdh.getSub_type();
        this.value = pdh.getValue();
        this.extended = pdh.getExtended();
        this.person_id = pdh.getPerson_id();
        this.comment = pdh.getComment();

        return true;
    }


    /**
     * DbHasChildren - Shows if this entry has any children object, this is
     * needed for delete
     *
     * @param sess
     *            Hibernate Session object
     * @throws Exception
     *             (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    /**
     * getAction - returns action that should be done on object 0 = no action 1
     * = add action 2 = edit action 3 = delete action This is used mainly for
     * objects, contained in lists and dialogs, used for processing by higher
     * classes (classes calling selectors, wizards, etc...
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
     *
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
     * @param dt
     *            ATechDate instance
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
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.getDateTimeObject().getDateTimeString() + ":  Base Type=" + this.getBaseTypeString());

                    if (StringUtils.isNotBlank(this.getSubTypeString()))
                    {
                        sb.append(", Sub Type=" + this.getSubTypeString());
                    }

                    if (StringUtils.isNotBlank(this.getValue()))
                    {
                        sb.append(", Value=" + this.getValue());
                    }

                    if (StringUtils.isNotBlank(this.getComment()))
                    {
                        sb.append(", Comment=" + this.getComment());
                    }

                    return sb.toString();
                }

            case OutputWriterType.GGC_FILE_EXPORT:
                {
                    /*
                     * PumpData pd = new PumpData(this); try { return
                     * pd.dbExport();
                     * } catch(Exception ex) { log.error(
                     * "Problem with PumpValuesEntry export !  Exception: " +
                     * ex,
                     * ex); return "Value could not be decoded for export!"; }
                     */
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
    /*
     * public long getId() { return this.id; }
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

    // ---
    // --- Statistics
    // ---

    /**
     * Statistics Value: Bolus Insulin Sum
     */
    public static final int INS_SUM_BOLUS = 1;

    /**
     * Statistics Value: Basal Insulin Sum
     */
    public static final int INS_SUM_BASAL = 2;

    /**
     * Statistics Value: Both Insulin Sum
     */
    public static final int INS_SUM_TOGETHER = 3;

    /**
     * Statistics Value: Bolus Insulin Average
     */
    public static final int INS_AVG_BOLUS = 4;

    /**
     * Statistics Value: Basal Insulin Average
     */
    public static final int INS_AVG_BASAL = 5;

    /**
     * Statistics Value: Both Insulin Average
     */
    public static final int INS_AVG_TOGETHER = 6;

    /**
     * Statistics Value: Bolus Insulin Doses
     */
    public static final int INS_DOSES_BOLUS = 7;

    /**
     * Statistics Value: Basal Insulin Doses
     */
    public static final int INS_DOSES_BASAL = 8;

    /**
     * Statistics Value: Both Insulin Doses
     */
    public static final int INS_DOSES_TOGETHER = 9;

    /**
     * Statistics Value: Carbohydrates Sum
     */
    public static final int CH_SUM = 10;

    /**
     * Statistics Value: Carbohydrates Average
     */
    public static final int CH_AVG = 11;

    /**
     * Statistics Value: Meals
     */
    public static final int MEALS = 12;

    /**
     * Statistics Value: Blood Glucose Average
     */
    public static final int BG_AVG = 13;

    /**
     * Statistics Value: Blood Glucose Maximal
     */
    public static final int BG_MAX = 14;

    /**
     * Statistics Value: Blood Glucose Minimal
     */
    public static final int BG_MIN = 15;

    /**
     * Statistics Value: Blood Glucose Count
     */
    public static final int BG_COUNT = 16;

    /**
     * Statistics Value: Blood Glucose Standard Deviation
     */
    public static final int BG_STD_DEV = 17;


    /**
     * Get Max Statistics Object - we can have several Statistic types defined
     * here
     *
     * @return
     */
    public int getMaxStatisticsObject()
    {
        return 18;
    }


    /**
     * Get Statistics Action - we define how statistic is done (we have several
     * predefined types of statistics
     *
     * @param index
     *            index for statistics item
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
     * @param index
     *            index for statistics item
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
                    if (this.baseType == PumpBaseType.Bolus)
                    {
                        if (this.getValue().contains(";"))
                        {
                            String vals[] = this.getValue().split(";");

                            float sum = 0.0f;

                            for (String val : vals)
                            {
                                if (val.startsWith("AMOUNT"))
                                {
                                    String ps[] = val.split("=");

                                    sum += dataAccess.getFloatValueFromString(ps[1], 0.0f);
                                }
                            }

                            return sum;
                        }
                        else
                        {
                            return dataAccess.getFloatValueFromString(this.getValue(), 0.0f);
                        }
                    }
                    else
                    {
                        return 0.0f;
                    }
                }

            case PumpValuesEntry.CH_SUM:
            case PumpValuesEntry.CH_AVG:
            case PumpValuesEntry.MEALS:
                {
                    if (this.additional_data.containsKey(PumpAdditionalDataType.Carbohydrates))
                    {
                        return dataAccess.getFloatValueFromString(
                            this.additional_data.get(PumpAdditionalDataType.Carbohydrates).getValue(), 0.0f);
                    }
                    else
                    {
                        return 0.0f;
                    }
                }

            case PumpValuesEntry.BG_AVG:
            case PumpValuesEntry.BG_MAX:
            case PumpValuesEntry.BG_MIN:
            case PumpValuesEntry.BG_COUNT:
                {
                    if (this.additional_data.containsKey(PumpAdditionalDataType.BloodGlucose))
                    {
                        return dataAccess.getFloatValueFromString(
                            this.additional_data.get(PumpAdditionalDataType.BloodGlucose).getValue(), 0.0f);
                    }
                    else
                    {
                        return 0.0f;
                    }

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
    @Override
    public String getMultiLineToolTip()
    {
        return null;
    }


    /**
     * Get MultiLine ToolTip
     */
    @Override
    public String getMultiLineToolTip(int index)
    {
        switch (index)
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
     * public String getValue() { return null; }
     */

    /**
     * Get Food Tip
     *
     * @return
     */
    public String getFoodTip()
    {
        if (this.additional_data.containsKey(PumpAdditionalDataType.FoodDescription))
        {
            String t = this.additional_data.get(PumpAdditionalDataType.FoodDescription).getValue();
            t = t.replace("]", "]<br>");
            t = t.replace(",", "");

            return "<html>" + t + "</html>";

        }
        else if (this.additional_data.containsKey(PumpAdditionalDataType.FoodDb))
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
    @Override
    public boolean isIndexed()
    {
        return true;
    }


    /**
     * Get Table Column Value (in case that we need special display values for
     * download data table, this method can be used, if it's the same as
     * getColumnValue, we can just call that one.
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
                return this.getStatusType();

            case 6:
                return getChecked();

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
        return "PD_" + this.datetime.getATDateTimeAsLong() + "_" + this.baseType + "_" + this.sub_type;
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
    /*
     * public String getValue() { return null; }
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


    public ArrayList<GraphValue> getGraphValues()
    {
        ArrayList<GraphValue> list = new ArrayList<GraphValue>();

        // FIXME
        // for (Enumeration<PumpValuesEntryExt> en =
        // this.additional_data.elements(); en.hasMoreElements();)
        // {
        // // PumpValuesEntryExt ex = en.nextElement();
        // GraphValue gv = en.nextElement().getGraphValue();
        //
        // if (gv != null)
        // {
        // list.add(gv);
        // }
        // }

        // FIXME - Add also current object if necessary

        // list.add(this.additional_data.elements());

        // TODO Auto-generated method stub
        return list;
    }


    public GraphValue getGraphValue()
    {
        return null;
    }

}
