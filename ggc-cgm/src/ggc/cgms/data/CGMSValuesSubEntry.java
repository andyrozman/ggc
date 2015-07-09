package ggc.cgms.data;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.atech.misc.statistics.StatisticsItem;
import com.atech.misc.statistics.StatisticsObject;
import com.atech.utils.data.ATechDate;

import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.data.defs.CGMSTrendArrow;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.GGCHibernateObject;
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
 *  Filename:     CGMValuesEntry.java
 *  Description:  Collection of CGMValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesSubEntry extends DeviceValuesEntry implements StatisticsItem
{

    private static final Log log = LogFactory.getLog(CGMSValuesSubEntry.class);

    /**
     * DateTime
     */
    public long datetime = 0L;

    /**
     * Date
     */
    public int date = 0;

    /**
     * Time
     */
    public int time = 0;

    /**
     * Value 
     */
    public int value = 0;

    /**
     * Type 
     */
    private int type = 0;

    // NEW
    /**
     * 
     */
    public int sub_type = 0;

    /**
     * Source
     */
    public String source;

    /**
     * Time only 
     */
    public boolean time_only = false;

    private CGMSBaseDataType typeObject;


    /**
     * Constructor
     */
    public CGMSValuesSubEntry()
    {
        this.source = DataAccessCGMS.getInstance().getSourceDevice();
    }


    /**
     * Constructor
     * 
     * @param entry
     * @param type
     */
    public CGMSValuesSubEntry(String entry, int type)
    {
        // 115329=147
        this.time = Integer.parseInt(entry.substring(0, entry.indexOf("=")));
        this.value = Integer.parseInt(entry.substring(entry.indexOf("=") + 1));
        this.setType(type);
        this.source = DataAccessCGMS.getInstance().getSourceDevice();

        this.datetime = time * 10 + type;
        // time_only = true;
    }


    /**
     * Get Sub Entry Value
     * 
     * @return
     */
    public String getSubEntryValue()
    {
        // return getColumnValue(0) + "=" + this.value;
        return this.time + "=" + this.value;
    }


    /**
     * Set Date Time
     * 
     * @param dt
     */
    public void setDateTime(long dt)
    {
        this.datetime = dt;
        date = (int) (dt / 1000000);
        time = (int) (dt - date * 1000000);

        // System.out.println("Date:  " + date + ", Time: " + time);
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
                return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_S, time);

            case 1:
                return CGMSBaseDataType.getByCode(this.type).getTranslation(); // DataAccessCGMS.value_type[type];

            case 2:
                return getDisplayValue();

            case 3:
                return "";

            default:
                return "N/A";
        }
    }


    private Object getDisplayValue()
    {
        // FIXME
        switch (this.typeObject)
        {
            case Alarm:
            case Event:
                return "?? " + this.value;

            case SensorCalibration:
            case SensorReading:
                return DataAccessCGMS.getInstance().getDisplayedBGString("" + this.value);

            case SensorReadingTrend:
                // ??
                return CGMSTrendArrow.getEnum(this.value).name();

            default:
                return "??";

        }

    }


    @Override
    public long getDateTime()
    {
        return this.datetime;
    }


    @Override
    public int getDateTimeFormat()
    {
        return ATechDate.FORMAT_DATE_AND_TIME_S;
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


    public long getId()
    {
        return 100000000000000L * type + this.datetime;
    }


    public String getSource()
    {
        return this.source;
    }


    public String getSpecialId()
    {
        return "CDS_" + this.datetime + "_" + this.type;
    }


    public Object getTableColumnValue(int index)
    {
        return null;
    }


    public String getValue()
    {
        return "" + this.value;
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
                return this.getDateTimeObject().getDateTimeString() + ":  Type=" + this.type + ", Value="
                        + this.getValue();

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


    public String DbAdd(Session sess) throws Exception
    {
        return null;
    }


    public boolean DbDelete(Session sess) throws Exception
    {
        return false;
    }


    public boolean DbEdit(Session sess) throws Exception
    {
        return false;
    }


    public boolean DbGet(Session sess) throws Exception
    {
        return false;
    }


    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    public int getAction()
    {
        return 0;
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

    /**
     * Statistics Value: Blood Glucose Average
     */
    public static final int STAT_AVG_BG1 = 1;

    /**
     * Statistics Value: Blood Glucose Maximal
     */
    public static final int STAT_MAX_BG1 = 2;

    /**
     * Statistics Value: Blood Glucose Minimal
     */
    public static final int STAT_MIN_BG1 = 3;

    /**
     * Statistics Value: Blood Glucose Count
     */
    public static final int STAT_COUNT_BG1 = 4;

    /**
     * Statistics Value: Blood Glucose Standard Deviation
     */
    public static final int STAT_STD_DEV_BG1 = 5;

    /**
     * Statistics Value: Blood Glucose Average
     */
    public static final int STAT_AVG_BG2 = 6;

    /**
     * Statistics Value: Blood Glucose Maximal
     */
    public static final int STAT_MAX_BG2 = 7;

    /**
     * Statistics Value: Blood Glucose Minimal
     */
    public static final int STAT_MIN_BG2 = 8;

    /**
     * Statistics Value: Blood Glucose Count
     */
    public static final int STAT_COUNT_BG2 = 9;

    /**
     * Statistics Value: Blood Glucose Standard Deviation
     */
    public static final int STAT_STD_DEV_BG2 = 10;


    public int getMaxStatisticsObject()
    {
        return 11;
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
        switch (index)
        {

            case CGMSValuesSubEntry.STAT_AVG_BG1:
            case CGMSValuesSubEntry.STAT_AVG_BG2:
                return StatisticsObject.OPERATION_AVERAGE;

            case CGMSValuesSubEntry.STAT_COUNT_BG1:
            case CGMSValuesSubEntry.STAT_COUNT_BG2:
                return StatisticsObject.OPERATION_COUNT;

            case CGMSValuesSubEntry.STAT_MAX_BG1:
            case CGMSValuesSubEntry.STAT_MAX_BG2:
                return StatisticsObject.OPERATION_MAX;

            case CGMSValuesSubEntry.STAT_MIN_BG1:
            case CGMSValuesSubEntry.STAT_MIN_BG2:
                return StatisticsObject.OPERATION_MIN;

            case CGMSValuesSubEntry.STAT_STD_DEV_BG1:
            case CGMSValuesSubEntry.STAT_STD_DEV_BG2:
                return StatisticsObject.OPERATION_SPECIAL;

            default:
                return 0;
        }
    }


    public float getValueForItem(int index)
    {

        switch (index)
        {

            case CGMSValuesSubEntry.STAT_AVG_BG1:
            case CGMSValuesSubEntry.STAT_MAX_BG1:
            case CGMSValuesSubEntry.STAT_MIN_BG1:
            case CGMSValuesSubEntry.STAT_COUNT_BG1:
                {
                    if (type == 1)
                        return this.value;
                    else
                        return 0.0f;

                }

            case CGMSValuesSubEntry.STAT_AVG_BG2:
            case CGMSValuesSubEntry.STAT_MAX_BG2:
            case CGMSValuesSubEntry.STAT_MIN_BG2:
            case CGMSValuesSubEntry.STAT_COUNT_BG2:
                {
                    if (type == 2)
                        return this.value;
                    else
                        return 0.0f;

                }

            case CGMSValuesSubEntry.STAT_STD_DEV_BG1:
            case CGMSValuesSubEntry.STAT_STD_DEV_BG2:

            default:
                return 0.0f;
        }

    }


    public boolean isSpecialAction(int index)
    {
        switch (index)
        {

            case CGMSValuesSubEntry.STAT_STD_DEV_BG1:
            case CGMSValuesSubEntry.STAT_STD_DEV_BG2:
                return true;

            default:
                return false;
        }

    }


    public boolean weHaveSpecialActions()
    {
        return true;
    }


    public int getType()
    {
        return type;
    }


    public void setType(int type)
    {
        this.type = type;
        this.typeObject = CGMSBaseDataType.getByCode(type);
    }


    public void setType(CGMSBaseDataType type)
    {
        this.type = type.getCode();
        this.typeObject = type;
    }


    public CGMSBaseDataType getTypeObject()
    {
        return typeObject;
    }


    public int compare(CGMSValuesSubEntry d1, CGMSValuesSubEntry d2)
    {
        return d1.time - d2.time;
    }


    public int compareTo(CGMSValuesSubEntry d2)
    {
        return this.time - d2.time;
    }

    /**
     * Comparator method, for sorting objects
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
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
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    /*
     * public int compareTo(DeviceValuesEntryInterface d2)
     * {
     * return compare(this, d2);
     * }
     */

}
