package ggc.plugin.data;

import java.util.ArrayList;
import java.util.Comparator;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.graphics.components.MultiLineTooltip;
import com.atech.utils.data.ATechDate;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.output.OutputWriterConfigData;
import ggc.plugin.output.OutputWriterData;

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
public interface DeviceValueConfigEntryInterface extends OutputWriterConfigData, Comparator<DeviceValueConfigEntryInterface>,
        Comparable<DeviceValueConfigEntryInterface>, MultiLineTooltip
{

    /**
     * Get Column Value
     *
     * @param index
     * @return
     */
    Object getColumnValue(int index);


    /**
     * Set Output Type
     *
     * @see ggc.plugin.output.OutputWriterData#setOutputType(int)
     */
    void setOutputType(int type);


    /**
     * Comparator method, for sorting objects
     *
     * @see Comparable#compareTo(Object)
     */
    int compare(DeviceValueConfigEntryInterface d1, DeviceValueConfigEntryInterface d2);

    /**
     * Comparator method, for sorting objects
     *
     * @see Comparable#compareTo(Object)
     */
    int compareTo(DeviceValueConfigEntryInterface d2);


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
