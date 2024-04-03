package ggc.cgms.data;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.data.defs.CGMSExtendedDataType;
import ggc.cgms.data.defs.CGMSViewerFilter;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.ExerciseStrength;
import ggc.core.data.defs.Health;
import ggc.core.db.hibernate.cgms.CGMSDataExtendedH;
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
 *  Filename:     CGMValuesEntry.java
 *  Description:  Collection of CGMValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesSubEntryExtended extends CGMSValuesSubEntry
{

    private static DataAccessCGMS dataAccessCGMS;
    private static ExtendedCGMSValueHandler extendedHandler;

    private CGMSDataExtendedH dataExtendedH;


    /**
     * Constructor
     *
     * @param dataExtendedH
     */
    public CGMSValuesSubEntryExtended(CGMSDataExtendedH dataExtendedH)
    {
        this.datetime = dataExtendedH.getDtInfo();
        this.time = (int)ATechDate.convertATDate(dataExtendedH.getDtInfo(), ATechDateType.DateAndTimeSec, ATechDateType.TimeOnlySec);
        this.dataExtendedH = dataExtendedH;

        if (dataAccessCGMS==null)
        {
            dataAccessCGMS = DataAccessCGMS.getInstance();
            extendedHandler = (ExtendedCGMSValueHandler)dataAccessCGMS.getExtendedHandler(DataAccessCGMS.EXTENDED_HANDLER_CGMSValuesExtendedEntry);
        }

        this.source = dataAccessCGMS.getSourceDevice();
    }


    /**
     * Get Sub Entry Value
     *
     * @return
     */
    public String getSubEntryValue()
    {
        return this.time + "=" + this.dataExtendedH.getValue();
    }




    @Override
    public String toString()
    {
        return datetime + " = " + value;
    }


    // FIXME This is problem, when we will have other types of data

    @Override
    public Object getColumnValue(int index)
    {
        switch (index)
        {
            case 0:
                return ATechDate.getTimeString(ATechDateType.TimeOnlySec, time);

            case 1:
                return getTypeExtendedObject().getTranslation();

            case 2:
                return getDisplayValue();

            case 3:
                return "";

            default:
                return "N/A";
        }
    }


    private String getDisplayValue()
    {
        switch (getTypeExtendedObject())
        {
            case Carbs:
                return getValueWithUnit("CFG_BASE_CARBOHYDRATE_UNIT_GRAMS_SHORT");

            case InsulinShortActing:
            case InsulinLongActing:
                return getValueWithUnit("CFG_BASE_UNIT_UNIT_SHORT");

            case Health:
            {
                Integer subType = getSubType();

                if (subType==null)
                {
                    return this.dataExtendedH.getValue();
                }
                else
                {
                    Health health = Health.getByCode(subType);

                    if ((StringUtils.isBlank(this.dataExtendedH.getValue())) || //
                            ("null".equals(this.dataExtendedH.getValue())))
                    {
                        return health.getTranslation();
                    }
                    else
                    {
                        return String.format("%s - %s", health.getTranslation(), this.dataExtendedH.getValue());
                    }
                }
            }

            case Exercise:
                Integer subType = getSubType();
                ExerciseStrength exerciseStrength;

                if (subType==null)
                {
                    exerciseStrength = ExerciseStrength.Undefined;
                }
                else
                {
                    exerciseStrength = ExerciseStrength.getByCode(subType);
                }

                return String.format(dataAccessCGMS.getI18nControlInstance().getMessage("EXERCISE_DISPLAY"), exerciseStrength.getTranslation(), this.dataExtendedH.getValue());

            case None:
            default:
                return this.dataExtendedH.getValue();

        }


    }


    Map<ExtendedCGMSValueType, String> extendedValues;

    private Integer getSubType()
    {
        extendedValues = extendedHandler.loadExtended(dataExtendedH.getExtended());

        String value = extendedHandler.getExtendedValue(ExtendedCGMSValueType.SubType, extendedValues);

        if (StringUtils.isNotBlank(value))
            return Integer.parseInt(value);
        else
            return null;

    }


    private String getValueWithUnit(String unitKey)
    {
        return String.format("%s %s", this.dataExtendedH.getValue(), DataAccessCGMS.getInstance().getI18nControlInstance().getMessage(unitKey));
    }



//    switch (record.getEventType())
//    {
//
//
//        case Health:
//        {
//            cvex.setType(CGMSExtendedDataType.Health);
//            cvex.subType = record.eventSubType;
//        }
//        break;
//        case Exercise:
//        {
//            cvex.setType(CGMSExtendedDataType.Exercise);
//            cvex.subType = record.eventSubType;
//            cvex.value = "" + record.eventValue;
//        }
//        default:
//            break;
//    }


    @Override
    public long getDateTime()
    {
        return this.datetime;
    }


    @Override
    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeSec;
    }


    @Override
    public ATechDate getDateTimeObject()
    {
        return null;
    }


    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        this.setDateTime(dt.getATDateTimeAsLong());
    }


    public String getDVEName()
    {
        return "CGMSValuesSubEntry";
    }


//    public long getId()
//    {
//        return 100000000000000L * type + this.datetime;
//    }


    public String getSource()
    {
        return this.source;
    }


//    public String getSpecialId()
//    {
//        return "CDS_" + this.datetime + "_" + this.type;
//    }


    public Object getTableColumnValue(int index)
    {
        return null;
    }


    public String getValue()
    {
        return "" + this.dataExtendedH.getValue();
    }


    public void setId(long idIn)
    {
    }


    public void setSource(String src)
    {
        this.source = src;
    }


    public String getDataAsString()
    {
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return this.getSubEntryValue();
//                return this.getDateTimeObject().getDateTimeString() + ":  Type=" + this.type + ", Value="
//                        + this.getValue();

            case OutputWriterType.GGC_FILE_EXPORT:
                {
                    return this.getSubEntryValue();
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




    public String getObjectName()
    {
        return this.getDVEName();
    }


    public String getObjectUniqueId()
    {
        return this.getSpecialId();
    }


    public boolean isDebugMode()
    {
        return false;
    }



    public int getType()
    {
        return 0;
    }


    public void setType(int type)
    {

    }


    public void setType(CGMSBaseDataType type)
    {

    }


    public CGMSBaseDataType getTypeObject()
    {
        return null;
    }

    public CGMSExtendedDataType getTypeExtendedObject()
    {
        return CGMSExtendedDataType.getByCode(this.dataExtendedH.getType());
    }


//    public int compare(CGMSValuesSubEntry d1, CGMSValuesSubEntry d2)
//    {
//        System.out.println("Compare: " + d1.time + ", " + d2.time);
//        return d1.time - d2.time;
//    }
//
//
//    public int compareTo(CGMSValuesSubEntry d2)
//    {
//        return this.time - d2.time;
//    }

    /**
     * Comparator method, for sorting objects
     *
     * @see Comparable#compareTo(Object)
     */
    /*
     * public int compare(DeviceValuesEntryInterface d1,
     * DeviceValuesEntryInterface d2)
     * {
     * if (!this.time_only)
     * return DeviceValuesEntryUtil.compare(d1, d2);
     * System.out.println("cmp: time only");
     * if ((d1 instanceof CGMSValuesSubEntry) && (d2 instanceof
     * CGMSValuesSubEntry))
     * return 0;
     * System.out.println("cmp: time only");
     * CGMSValuesSubEntry o1 = (CGMSValuesSubEntry)d1;
     * CGMSValuesSubEntry o2 = (CGMSValuesSubEntry)d2;
     * int diff_dt = o1.time - o2.time;
     * if (diff_dt==0)
     * {
     * return o1.type - o2.type;
     * }
     * else
     * return diff_dt;
     * }
     */
    /**
     * Comparator method, for sorting objects
     *
     * @see Comparable#compareTo(Object)
     */
    /*
     * public int compareTo(DeviceValuesEntryInterface d2)
     * {
     * return compare(this, d2);
     * }
     */


    public boolean isItemFiltered(CGMSViewerFilter filter)
    {
        return (filter == CGMSViewerFilter.All || filter == CGMSViewerFilter.AdditionalData);
    }

}
