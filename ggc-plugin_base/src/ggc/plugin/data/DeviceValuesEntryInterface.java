package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.output.OutputWriterData;

import java.util.ArrayList;
import java.util.Comparator;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.graphics.components.MultiLineTooltip;
import com.atech.utils.data.ATechDate;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: GGC PlugIn Base (base class for all plugins)
 * See AUTHORS for copyright information.
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * Filename: DeviceValuesEntry
 * Description: Collection of DeviceValuesEntry, which contains all daily
 * values.
 * Author: Andy {andy@atech-software.com}
 */
public interface DeviceValuesEntryInterface extends OutputWriterData, Comparator<DeviceValuesEntryInterface>,
        Comparable<DeviceValuesEntryInterface>,
        // StatisticsItem,
        DatabaseObjectHibernate, MultiLineTooltip
{

    /**
     * Status: Unknown
     */
    public static final int STATUS_UNKNOWN = 0;

    /**
     * Status: New
     */
    public static final int STATUS_NEW = 1;

    /**
     * Status: Changed
     */
    public static final int STATUS_CHANGED = 2;

    /**
     * Status: Old
     */
    public static final int STATUS_OLD = 3;

    /**
     * Object Status: New
     */
    public static final int OBJECT_STATUS_NEW = 1;

    /**
     * Object Status: Edit
     */
    public static final int OBJECT_STATUS_EDIT = 2;

    /**
     * Object Status: Old (existing)
     */
    public static final int OBJECT_STATUS_OLD = 3;

    /**
     * Entry Status Icons
     */
    public static String entry_status_icons[] = { "led_gray.gif", "led_green.gif", "led_yellow.gif", "led_red.gif" };

    /**
     * Get DateTime (long)
     *
     * @return
     */
    long getDateTime();

    /**
     * Set DateTime Object (ATechDate)
     *
     * @param dt
     *            ATechDate instance
     */
    void setDateTimeObject(ATechDate dt);

    /**
     * Get DateTime Object (ATechDate)
     *
     * @return ATechDate instance
     */
    ATechDate getDateTimeObject();

    /**
     * Get DateTime format
     *
     * @return format of date time (precission)
     */
    int getDateTimeFormat();

    /**
     * Get Column Value
     *
     * @param index
     * @return
     */
    Object getColumnValue(int index);

    /**
     * Get Table Column Value (in case that we need special display values for
     * download data table, this method
     * can be used, if it's the same as getColumnValue, we can just call that
     * one.
     *
     * @param index
     * @return
     */
    Object getTableColumnValue(int index);

    /**
     * Get Checked
     *
     * @return true if element is checked
     */
    boolean getChecked();

    /**
     * Set Checked
     *
     * @param check
     *            true if element is checked
     */
    void setChecked(boolean check);

    /**
     * Get Status
     *
     * @return status
     */
    int getStatus();

    /**
     * Set Status
     *
     * @param status_in
     */
    void setStatus(int status_in);

    /**
     * Prepare Entry [Framework v1]
     */
    void prepareEntry();

    /**
     * Prepare Entry [Framework v2]
     */
    void prepareEntry_v2();

    /**
     * Get Db Objects
     *
     * @return ArrayList of elements extending GGCHibernateObject
     */
    ArrayList<? extends GGCHibernateObject> getDbObjects();

    /**
     * Get DeviceValuesEntry Name
     *
     * @return
     */
    String getDVEName();

    /**
     * Get Value of object
     *
     * @return
     */
    String getValue();

    /**
     * Set Output Type
     *
     * @see ggc.plugin.output.OutputWriterData#setOutputType(int)
     */
    void setOutputType(int type);

    /**
     * Is Data BG
     *
     * @return
     * @see ggc.plugin.output.OutputWriterData#isDataBG()
     */
    boolean isDataBG();

    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     *
     * @param id_in
     */
    void setId(long id_in);

    /**
     * Get Id (this is used for changing old objects in framework v2)
     *
     * @return id of old object
     */
    long getId();

    /**
     * Comparator method, for sorting objects
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    int compare(DeviceValuesEntryInterface d1, DeviceValuesEntryInterface d2);

    /**
     * Comparator method, for sorting objects
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    int compareTo(DeviceValuesEntryInterface d2);

    /**
     * Set Object status
     *
     * @param status
     */
    void setObjectStatus(int status);

    /**
     * Get Object Status
     *
     * @return
     */
    int getObjectStatus();

    /**
     * Get Special Id
     *
     * @return
     */
    String getSpecialId();

    /**
     * Set Source
     *
     * @param src
     */
    void setSource(String src);

    /**
     * Get Source
     *
     * @return
     */
    String getSource();

    /**
     * Has MultiLine Tooltip
     *
     * @return
     */
    boolean hasMultiLineToolTip();

    /**
     * Set MultiLine Tooltip Type
     *
     * @param _multiline_tooltip_type
     */
    void setMultiLineTooltipType(int _multiline_tooltip_type);

    /**
     * Get MultiLine Tooltip Type
     *
     * @return
     */
    int getMultiLineTooltipType();

}
