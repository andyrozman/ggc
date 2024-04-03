package ggc.meter.data;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.data.GsonUtils;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.ExtendedDailyValueHandler;
import ggc.core.data.ExtendedDailyValueType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.db.hibernate.pen.DayValueH;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.output.OutputWriterType;

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

public class MeterValuesEntry extends DeviceValuesEntry
{

    private static DataAccessMeter dataAccess;
    private static ExtendedDailyValueHandler extendedDailyValueHandler;

    private ATechDate datetime;
    private Integer bgOriginal = null;
    private Float ch = null;
    private Map<String, String> params;
    private Float bgMmolL;
    public DayValueH entry_object = null;
    private Map<ExtendedDailyValueType, String> extendedMap;
    public MeterValuesEntryDataType extendedType = MeterValuesEntryDataType.None;
    public List<GlucoseMeterMarkerDto> glucoseMarkers;


    /**
     * Constructor
     */
    public MeterValuesEntry()
    {
        super();

        checkStaticObjects();

        this.source = dataAccess.getSourceDevice();

        this.loadExtendedEntries(null);
        resetExtendedType();
    }


    private void checkStaticObjects()
    {
        if (dataAccess == null)
            dataAccess = DataAccessMeter.getInstance();

        if (extendedDailyValueHandler == null)
            extendedDailyValueHandler = dataAccess.getExtendedDailyValueHandler();
    }


    /**
     * Constructor
     * @param dv 
     */
    public MeterValuesEntry(DayValueH dv)
    {
        super();

        checkStaticObjects();
        this.datetime = new ATechDate(this.getDateTimeFormat(), dv.getDtInfo());
        this.setBgValue("" + dv.getBg(), GlucoseUnitType.mg_dL, false);
        this.entry_object = dv;
        this.object_status = DeviceValuesEntry.OBJECT_STATUS_OLD;
        this.ch = dv.getCh();

        this.loadExtendedEntries(dv.getExtended());

        // comments ?

        resetExtendedType();
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
        if ((value == null) || (value.equals("_") || value.trim().length() == 0))
            return;

        if (params == null)
        {
            params = new Hashtable<String, String>();
        }

        this.params.put(key, value);
    }


    public void setBgValue(String value, GlucoseUnitType unitType)
    {
        this.setBgValue(value, unitType, true);
    }


    public void setBgValue(float value, GlucoseUnitType unitType)
    {
        this.setBgValue(value, unitType, true);
    }


    /**
     * Set Bg Value (String)
     * 
     * @param value as string
     */
    public void setBgValue(String value, GlucoseUnitType unitType, boolean resetExtendedType)
    {
        if (unitType == GlucoseUnitType.mg_dL)
        {
            this.bgOriginal = dataAccess.getIntValueFromString(value, 0);
            this.bgMmolL = dataAccess.getBGValueFromDefault(GlucoseUnitType.mmol_L, this.bgOriginal);
        }
        else
        {
            this.bgMmolL = dataAccess.getFloatValueFromString(value);

            Float f = dataAccess.getBGValueByType(GlucoseUnitType.mmol_L, GlucoseUnitType.mg_dL, this.bgMmolL);
            this.bgOriginal = f.intValue();
        }

        if (resetExtendedType)
            resetExtendedType();
    }


    public void setBgValue(float value, GlucoseUnitType unitType, boolean resetExtendedType)
    {
        if (unitType == GlucoseUnitType.mg_dL)
        {
            this.bgOriginal = (int) value;
            this.bgMmolL = dataAccess.getBGValueFromDefault(GlucoseUnitType.mmol_L, this.bgOriginal);
        }
        else
        {
            this.bgMmolL = value;

            Float f = dataAccess.getBGValueByType(GlucoseUnitType.mmol_L, GlucoseUnitType.mg_dL, this.bgMmolL);
            this.bgOriginal = f.intValue();
        }

        if (resetExtendedType)
            resetExtendedType();
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

        for (String key : params.keySet())
        {
            sb.append(key + "=" + this.params.get(key) + ";");
        }

        return sb.substring(0, sb.length() - 1);

    }


    /**
     * Prepare Entry [Framework v2]
     */
    @Override
    public void prepareEntry_v2()
    {
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
        // FIXME
        // OutputUtil o= null;
        return "MeterValuesEntry [date/time=" + this.datetime.getDateTimeString() + ",bg=" + this.bgOriginal + " mg/dL"
                + "]";
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
                    // Columns: id; dt_info; bg; ins1; ins2; ch; meals_ids;
                    // extended; person_id; comment; changed

                    String v = "0|" + this.getDateTime() + "|" + this.bgOriginal + "|0.0|0.0|";

                    if (this.hasCHEntry())
                    {
                        v += this.dataAccess.getFormatedValueUS(this.ch, 1);
                    }
                    else
                    {
                        v += "0.0";
                    }

                    v += "|null|" + this.createExtendedValueDailyValuesH() + "|1|" + this.getParametersAsString() + "|"
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
        if (!dataAccess.isDataDownloadScreenWide())
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
                return this.bgOriginal;

            case 2:
                return this.bgMmolL;

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
    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeMin;
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
        return null;
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


    private void processEntry(Session sess, String action)
    {
        if (action.equals("Edit")) // DeviceValuesEntry.OBJECT_STATUS_EDIT)
        {
            Transaction tx = sess.beginTransaction();

            prepareObjectForEdit();

            // log.debug("Updated. Status was Edit. Action was: " + act);

            sess.update(this.entry_object);

            tx.commit();
        }
        else
        {
            Transaction tx = sess.beginTransaction();

            prepareObjectForAdd();

            prepareObjectForEdit();

            // log.debug("Added. Status was Add. Action was: " + act);
            Long id = (Long) sess.save(this.entry_object);

            tx.commit();

            this.setId(id.longValue());
        }
    }


    public void prepareObjectForAdd()
    {
        this.entry_object = new DayValueH();
        this.entry_object.setIns1(0);
        this.entry_object.setIns2(0);
        this.entry_object.setCh(0.0f);
        this.entry_object.setPersonId((int) DataAccessMeter.getInstance().getCurrentUserId());
        this.entry_object.setDtInfo(this.getDateTime());
    }


    public void prepareObjectForEdit()
    {
        if (this.bgOriginal != null)
            this.entry_object.setBg(this.bgOriginal); // Integer.parseInt(this.getBGValue(OutputUtil.BG_MGDL)));

        if (this.ch != null)
            this.entry_object.setCh(this.ch);

        this.entry_object.setExtended(this.createExtendedValueDailyValuesH());

        this.entry_object.setChanged(System.currentTimeMillis());
        this.entry_object.setComment(createComment());
        this.entry_object.setPersonId((int) DataAccessMeter.getInstance().getCurrentUserId());
        // log.debug("Updated. Status was Edit. Action was: " + act);
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
        // if (this.value_db!=null)
        // return this.value_db;
        // else
        return this.getValueFull();
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

        if (this.hasBGEntry())
        {
            sb.append("BG=" + this.bgOriginal + ";");
        }

        if (this.hasCHEntry())
        {
            sb.append("CH=" + dataAccess.getFormatedValueUS(this.ch, 1));
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
     * Has BG entry
     * 
     * @return
     */
    public boolean hasBGEntry()
    {
        return checkNotNulOrGreaterThanZero(this.bgOriginal);
    }


    public boolean hasCHEntry()
    {
        return checkNotNulOrGreaterThanZero(this.ch);
    }


    private ExtendedDailyValueHandler getExtendedHandler()
    {
        // FIXME
        return null;
    }


    public boolean hasUrine()
    {
        if (extendedMap == null)
        {
            return false;
        }
        else
            return extendedDailyValueHandler.isExtendedValueSet(ExtendedDailyValueType.Urine, extendedMap)
                    || extendedDailyValueHandler.isExtendedValueSet(ExtendedDailyValueType.Urine_mmolL, extendedMap)
                    || extendedDailyValueHandler.isExtendedValueSet(ExtendedDailyValueType.Urine_mgdL, extendedMap);
    }


    public String getUrineValue()
    {
        return extendedDailyValueHandler.getExtendedValue(ExtendedDailyValueType.Urine, extendedMap);
    }


    private boolean checkNotNulOrGreaterThanZero(Number number)
    {
        if (number == null)
            return false;
        else
            return number.floatValue() > 0;
    }


    private void loadExtendedEntries(String extEntry)
    {
        if (extEntry != null && extEntry.length() > 0)
        {
            this.extendedMap = extendedDailyValueHandler.loadExtended(extEntry);
        }
        else
        {
            this.extendedMap = new HashMap<ExtendedDailyValueType, String>();
        }

        loadGlucoseMarkers();

        extendedDailyValueHandler.setExtendedValue(ExtendedDailyValueType.Source, //
            DataAccessMeter.getInstance().getSourceDevice(), this.extendedMap);
    }


    public void setUrine(ExtendedDailyValueType entryType, String value)
    {
        // String valueFull = null;
        //
        // if (entryType == ExtendedDailyValueType.Urine)
        // {
        // valueFull = value;
        // }
        // else if (entryType == ExtendedDailyValueType.Urine_mgdL)
        // {
        // valueFull = value + " " + "mg/dL";
        // }
        // else
        // // if (entryType == DailyValuesExtendedType.Urine_mgdL)
        // {
        // valueFull = value + " " + "mmol/L";
        // }
        //
        extendedDailyValueHandler.setExtendedValue(entryType, value, extendedMap);

        resetExtendedType();
    }


    public void resetExtendedType()
    {
        int countElements = 0;
        extendedType = MeterValuesEntryDataType.None;

        if (this.hasBGEntry())
        {
            extendedType = MeterValuesEntryDataType.BG;
            countElements++;
        }

        if (this.hasCHEntry())
        {
            if (countElements == 0)
            {
                extendedType = MeterValuesEntryDataType.CH;
            }

            countElements++;
        }

        if (this.hasUrine())
        {
            if (countElements == 0)
            {
                extendedType = MeterValuesEntryDataType.Urine;
            }

            countElements++;
        }

        if (countElements == 0)
        {
            extendedType = MeterValuesEntryDataType.InvalidData;
        }
        else if (countElements > 1)
        {
            extendedType = MeterValuesEntryDataType.Multiple;
        }

    }


    /**
     * Get Extended Type Description (if we use extended interface, this is type description)
     *
     * @return
     */
    public String getExtendedTypeDescription()
    {
        return extendedType.getTranslation(); // extendedTypeDescription;
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
        switch (this.extendedType)
        {
            case BG:
                {
                    if (both_bg)
                        return this.bgOriginal + " mg/dL (" + dataAccess.getFormatedValue(this.bgMmolL, 1) + " mmol/L)";
                    else
                        return getDisplayableBgValueWithUnit();
                }

            case CH:
                return dataAccess.getFormatedValue(this.ch, 1);

            case Urine:
                return getUrineValue();

            case Multiple:
                return this.getExtendedValueMultiple();

            default:
            case InvalidData:
            case None:
                return MeterValuesEntryDataType.InvalidData.getTranslation();
        }

    }


    public String getDisplayableBgValueWithUnit()
    {
        return this.dataAccess.getDisplayedBGFromDefault(this.bgOriginal) + " "
                + dataAccess.getGlucoseUnitType().getTranslation();
    }


    public String getExtendedValueMultiple()
    {
        HashMap<String, String> values = new HashMap<String, String>();

        if (this.hasBGEntry())
        {
            values.put(MeterValuesEntryDataType.BG.getTranslation() + ": ", getDisplayableBgValueWithUnit());
        }

        if (this.hasCHEntry())
        {
            values.put(MeterValuesEntryDataType.CH.getTranslation() + ": ", dataAccess.getFormatedValue(this.ch, 1));
        }

        if (this.hasUrine())
        {
            values.put(MeterValuesEntryDataType.Urine.getTranslation() + ": ", //
                getUrineValue());
        }

        return dataAccess.createKeyValueString(values, ";", true);
    }


    /**
     * Get Entry Type
     * 
     * @return
     */
    public MeterValuesEntryDataType getEntryType()
    {
        return extendedType;
    }


    /**
     * Create data for extended field in database (special entries without CH)
     * @return
     */
    public String createExtendedValueDailyValuesH()
    {
        saveGlucoseMarkers();
        return extendedDailyValueHandler.saveExtended(extendedMap);
    }


    public DeviceEntryStatus getImportStatus(MeterValuesEntry mve)
    {
        for (MeterValuesEntryDataType mpe : MeterValuesEntryDataType.values())
        {
            if (mve.hasImportData(mpe))
            {
                if (!this.hasImportData(mpe))
                {
                    return DeviceEntryStatus.Changed;
                }
                else
                {
                    if (!this.getImportData(mpe).equals(mve.getImportData(mpe)))
                    {
                        return DeviceEntryStatus.Changed;
                    }
                }
            }
        }

        return DeviceEntryStatus.Old;
    }


    public HashMap<MeterValuesEntryDataType, Object> getChangedData(MeterValuesEntry mve)
    {
        HashMap<MeterValuesEntryDataType, Object> changedData = new HashMap<MeterValuesEntryDataType, Object>();

        for (MeterValuesEntryDataType mpe : MeterValuesEntryDataType.getProcessorTypes().keySet())
        {
            if (mve.hasImportData(mpe))
            {
                if (this.hasImportData(mpe))
                {
                    if (!this.getImportData(mpe).equals(mve.getImportData(mpe)))
                    {
                        changedData.put(mpe, mve.getImportData(mpe));
                    }
                }
                else
                {
                    changedData.put(mpe, mve.getImportData(mpe));
                }
            }
        }

        return changedData;
    }


    public HashMap<MeterValuesEntryDataType, Object> getChangedData()
    {
        HashMap<MeterValuesEntryDataType, Object> changedData = new HashMap<MeterValuesEntryDataType, Object>();

        for (MeterValuesEntryDataType mpe : MeterValuesEntryDataType.getProcessorTypes().keySet())
        {

            // System.out.println(String.format(
            // "getChangedData [DT=%s,Type=%s, mveImp=%s, thisImp=%s,
            // mveData=%s,thisData=%s",
            // this.getDateTime(),
            // mpe.name(), mve.hasImportData(mpe), this.hasImportData(mpe),
            // mve.getImportData(mpe),
            // this.getImportData(mpe)));

            if (this.hasImportData(mpe))
            {
                changedData.put(mpe, this.getImportData(mpe));
            }

        }

        return changedData;
    }


    public boolean mergeNewData(MeterValuesEntry mve)
    {
        HashMap<MeterValuesEntryDataType, Object> data = getChangedData(mve);
        boolean changed = false;

        for (Map.Entry<MeterValuesEntryDataType, Object> entry : data.entrySet())
        {
            this.setImportData(entry.getKey(), entry.getValue());
            changed = true;
        }

        return changed;
    }


    public boolean mergeNewData(HashMap<MeterValuesEntryDataType, Object> changedData)
    {
        boolean changed = false;

        for (Map.Entry<MeterValuesEntryDataType, Object> entry : changedData.entrySet())
        {
            this.setImportData(entry.getKey(), entry.getValue());
            changed = true;
        }

        return changed;
    }


    public boolean hasImportData(MeterValuesEntryDataType dataType)
    {
        switch (dataType)
        {
            case BG:
                return this.hasBGEntry();

            case CH:
                return this.hasCHEntry();

            case Urine:
                return this.hasUrine();

            default:
                return false;
        }
    }


    public Object getImportData(MeterValuesEntryDataType dataType)
    {
        switch (dataType)
        {
            case BG:
                return this.bgOriginal;

            case CH:
                return this.ch;

            case Urine:
                return this.getUrineValue();

            default:
                return null;
        }

    }


    public void setImportData(MeterValuesEntryDataType dataType, Object value)
    {
        switch (dataType)
        {
            case BG:
                this.bgOriginal = (Integer) value;
                break;

            case CH:
                this.ch = (Float) value;
                break;

            case Urine:
                extendedDailyValueHandler.setExtendedValue(ExtendedDailyValueType.Urine, (String) value, extendedMap);
                break;

            default:
                break;

        }

        this.resetExtendedType();
    }


    public void setCh(Object ch)
    {
        if (ch instanceof String)
        {
            this.ch = dataAccess.getFloatValueFromString((String) ch);
        }
        else if (ch instanceof Number)
        {
            this.ch = ((Number) ch).floatValue();
        }

        resetExtendedType();
    }


    public Integer getBgOriginal()
    {
        return bgOriginal;
    }


    public Float getBgMmolL()
    {
        return bgMmolL;
    }


    public void addGlucoseMeterMarker(GlucoseMeterMarkerDto marker)
    {
        if (this.glucoseMarkers == null)
        {
            this.glucoseMarkers = new ArrayList<GlucoseMeterMarkerDto>();
        }

        this.glucoseMarkers.add(marker);
    }


    public void loadGlucoseMarkers()
    {
        if (extendedDailyValueHandler.isExtendedValueSet(ExtendedDailyValueType.GlucometerMarkers, extendedMap))
        {
            this.glucoseMarkers = GsonUtils.getListOfType( //
                extendedDailyValueHandler.getExtendedValue(ExtendedDailyValueType.GlucometerMarkers, extendedMap), //
                GlucoseMeterMarkerDto.class);
        }
    }


    public void saveGlucoseMarkers()
    {
        extendedDailyValueHandler.setExtendedValue(ExtendedDailyValueType.GlucometerMarkers, null, extendedMap);
    }

}
