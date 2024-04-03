package ggc.cgms.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.statistics.StatisticsItem;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.defs.CGMSExtendedDataType;
import ggc.cgms.util.CGMSUtil;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.ExerciseStrength;
import ggc.core.data.defs.Health;
import ggc.core.db.hibernate.cgms.CGMSDataExtendedH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputWriterType;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSValuesExtendedEntry
 *  Description:  Collection of CGMSValuesExtendedEntry, which contains Extended data.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesExtendedEntry extends DeviceValuesEntry implements StatisticsItem
{

    public long id;
    public long datetime;
    private CGMSExtendedDataType type;
    public String value;
    public String extended;
    public int personId;
    public String comment;
    public Integer subType; // = -1; // not stored directly


    static DataAccessCGMS dataAccess;
    static I18nControlAbstract i18nControl;

    public CGMSValuesExtendedEntry()
    {
        super();
        setDataAccess();
        this.source = dataAccess.getSourceDevice();
    }


    private void setDataAccess()
    {
        if (dataAccess==null)
        {
            dataAccess = DataAccessCGMS.getInstance();
            i18nControl = dataAccess.getI18nControlInstance();
        }
    }

    /**
     * Constructor
     * 
     * @param pdh 
     */
    public CGMSValuesExtendedEntry(CGMSDataExtendedH pdh)
    {
        super();
        setDataAccess();
        loadDbData(pdh);
    }


    private void loadDbData(CGMSDataExtendedH pdh)
    {
        this.id = pdh.getId();

        this.datetime = pdh.getDtInfo();
        this.type = CGMSExtendedDataType.getByCode(pdh.getType());
        this.value = pdh.getValue();
        loadExtended(pdh.getExtended());
        this.personId = pdh.getPersonId();
        this.comment = pdh.getComment();
    }


    public Object getTableColumnValue(int index)
    {
        switch (index)
        {
            case 0:
                return this.getDateTimeObject().getDateTimeString();

            case 1:
                return this.type.getTranslation();

            case 2:
                return getDisplayValue();

            case 3:
                return this.getStatusType();

            case 4:
                return getChecked();


            default:
                return "";
        }
    }


    private String getDisplayValue()
    {
        switch (type)
        {
            case Carbs:
                return getValueWithUnit("CFG_BASE_CARBOHYDRATE_UNIT_GRAMS_SHORT");

            case InsulinShortActing:
            case InsulinLongActing:
                return getValueWithUnit("CFG_BASE_UNIT_UNIT_SHORT");

            case Health:
            {
                if (subType==null)
                {
                    return this.getValue();
                }
                else
                {
                    Health health = Health.getByCode(subType);

                    if ((StringUtils.isBlank(this.getValue())) || //
                            ("null".equals(this.getValue())))
                    {
                        return health.getTranslation();
                    }
                    else
                    {
                        return String.format("%s - %s", health.getTranslation(), this.getValue());
                    }
                }
            }

            case Exercise:
            {
                ExerciseStrength exerciseStrength;

                if (subType == null)
                {
                    exerciseStrength = ExerciseStrength.Undefined;
                }
                else
                {
                    exerciseStrength = ExerciseStrength.getByCode(subType);
                }

                return String.format(dataAccess.getI18nControlInstance().getMessage("EXERCISE_DISPLAY"), exerciseStrength.getTranslation(), this.getValue());
            }

            case None:
            default:
                return this.getValue();

        }


    }

    private String getValueWithUnit(String unitKey)
    {
        return String.format("%s %s", this.getValue(), dataAccess.getI18nControlInstance().getMessage(unitKey));
    }




    /**
     * Prepare Entry [Framework v2]
     */
    @Override
    public void prepareEntry_v2()
    {
        this.extended = this.saveExtended();
        this.personId = dataAccess.getCurrentUserId();
    }

    @Override
    public Object getColumnValue(int index)
    {
        return getTableColumnValue(index);
    }


    public String getDVEName()
    {
        return "CGMSValuesExtendedEntry";
    }


    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
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


    public String getSpecialId()
    {
        return "CDE_" + this.datetime + "_" + this.type.getCode();
    }



    public String getDataAsString()
    {
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return this.getDateTimeObject().getDateTimeString() + ":  Type=" + this.type.getTranslation()
                        + ", Value=" + this.getValue();

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


    public String getObjectUniqueId()
    {
        return "" + this.datetime + "" + type.getCode();
    }


    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        CGMSDataExtendedH ch = new CGMSDataExtendedH();

        ch.setDtInfo(this.datetime);
        ch.setType(this.type.getCode());
        ch.setValue(this.getValue());
        ch.setExtended(extended = this.saveExtended());
        ch.setPersonId(this.personId);
        ch.setComment(this.comment);
        ch.setChanged(System.currentTimeMillis());

        Long _id = (Long) sess.save(ch);
        tx.commit();

        ch.setId(_id.longValue());
        //this.id = _id.longValue();

        return "" + _id;
    }


    private void saveDbData(CGMSDataExtendedH ch)
    {
        this.personId = CGMSUtil.getCurrentUserId();

        ch.setDtInfo(this.datetime);
        ch.setType(this.type.getCode());
        ch.setValue(this.getValue());
        ch.setExtended(extended = this.saveExtended());
        ch.setPersonId(this.personId);
        ch.setComment(this.comment);
        ch.setChanged(System.currentTimeMillis());
    }


    public boolean DbEdit(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();
        // System.out.println("id: " + old_id);
        CGMSDataExtendedH ext = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.id);

        // ext.setId(this.id);

        this.saveDbData(ext);

        sess.update(ext);
        tx.commit();

        return true;
    }


    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        CGMSDataExtendedH ch = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.getId());

        sess.delete(ch);
        tx.commit();

        return true;
    }


    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    public boolean DbGet(Session sess) throws Exception
    {
        CGMSDataExtendedH ch = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.getId());

        loadDbData(ch);

        return true;
    }


    public String getObjectName()
    {
        return "CGMSValuesExtendedEntry";
    }


    public boolean isDebugMode()
    {
        return false;
    }


    public int getAction()
    {
        return 0;
    }


    public float getValueForItem(int index)
    {
        return 0;
    }


    public int getStatisticsAction(int index)
    {
        return 0;
    }


    public boolean isSpecialAction(int index)
    {
        return false;
    }


    public int getMaxStatisticsObject()
    {
        return 0;
    }


    public boolean weHaveSpecialActions()
    {
        return false;
    }


    @Override
    public long getDateTime()
    {
        return this.datetime;
    }


    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt.getATDateTimeAsLong();
    }


    @Override
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDateType.DateAndTimeSec, this.datetime);
    }


    @Override
    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeSec;
    }


    private ExtendedCGMSValueHandler getExtendedHandler()
    {
        return (ExtendedCGMSValueHandler) CGMSUtil.getExtendedHandler(this.getDVEName());
    }


    private void loadExtended(String extended2)
    {
        ExtendedCGMSValueHandler handler = getExtendedHandler();
        Map<ExtendedCGMSValueType, String> data = handler.loadExtended(extended2);

        if (handler.isExtendedValueSet(ExtendedCGMSValueType.SubType, data))
        {
            this.subType = Integer.parseInt(handler.getExtendedValue(ExtendedCGMSValueType.SubType, data));
        }

        if (handler.isExtendedValueSet(ExtendedCGMSValueType.Source, data))
        {
            this.source = handler.getExtendedValue(ExtendedCGMSValueType.Source, data);
        }
    }


    private String saveExtended()
    {
        ExtendedCGMSValueHandler handler = getExtendedHandler();
        Map<ExtendedCGMSValueType, String> data = new HashMap<ExtendedCGMSValueType, String>();

        if (this.subType != null && this.subType > 0)
        {
            handler.setExtendedValue(ExtendedCGMSValueType.SubType, "" + this.subType, data);
        }

        if (StringUtils.isNotBlank(this.source))
        {
            handler.setExtendedValue(ExtendedCGMSValueType.Source, this.source, data);
        }

        String ext = handler.saveExtended(data);

        return StringUtils.isNotBlank(ext) ? ext : null;

    }


    public CGMSExtendedDataType getType()
    {
        return type;
    }


    public void setType(CGMSExtendedDataType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "CGMSValuesExtendedEntry [" +
                "id=" + id + ", datetime=" + datetime + ", type=" + type + ", value=" + value +  //
                ", extended=" + extended + ", personId=" + personId +
                ", checked=" + checked + ", source=" + this.source + ", subType=" + this.subType + "]";
    }
}
