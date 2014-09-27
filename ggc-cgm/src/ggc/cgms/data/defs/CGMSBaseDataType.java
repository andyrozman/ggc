package ggc.cgms.data.defs;

import ggc.cgms.data.defs.extended.CGMSExtendedDataType;
import ggc.cgms.util.DataAccessCGMS;

import java.util.HashMap;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: CGMS Tool (support for CGMS devices)
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
 * Filename: CGMDataType
 * Description: CGMS Data types, as used in database (undefined at this time)
 *
 * Author: Andy {andy@atech-software.com}
 */

// IMPORTANT NOTICE:
// This class is not implemented yet, all existing methods should be rechecked (they were copied from similar
// class, with different type of data.

// TODO this class/enum is in refactoring process

public enum CGMSBaseDataType {

//    CGMS_BG_READING(1),
//
//    CGMS_METER_CALIBRATION(2),
//
//    CGMS_DATA_EVENT(3),
//
//    CGMS_DATA_ALARM(4),
//
//    CGMS_DATA_ERROR(5),
//
//    CGMS_TREND(6),
    
    None(0, "NONE"),
    SensorReading(1, "CGMS_READING"),
    MeterCalibration(2, "CALIBRATION_READINGS"),
    DeviceAlarm(3, "CGMS_DATA_ALARM"),
    DeviceEvent(4, "CGMS_DATA_EVENT"),
    DeviceError(5, "CGMS_DATA_ERROR"),
    SensorReadingTrend(6, "CGMS_READING_TREND"),

    ;

//    DataAccessCGMS.value_type[1] = this.i18n_plugin.getMessage("CGMS_READING");
//    DataAccessCGMS.value_type[2] = this.i18n_plugin.getMessage("CALIBRATION_READINGS");
//    DataAccessCGMS.value_type[4] = this.i18n_plugin.getMessage("CGMS_DATA_EVENT");
//    DataAccessCGMS.value_type[3] = this.i18n_plugin.getMessage("CGMS_DATA_ALARM");
//    DataAccessCGMS.value_type[5] = this.i18n_plugin.getMessage("CGMS_DATA_ERROR");
//    DataAccessCGMS.value_type[6] = this.i18n_plugin.getMessage("CGMS_READING_TREND");

    
    
    private int dataType;

    
    private String description;
    private static HashMap<Integer,CGMSBaseDataType> map = new HashMap<Integer,CGMSBaseDataType>();
    
    static
    {
        for(CGMSBaseDataType el : values())
        {
            map.put(el.getValue(), el);
        }
    }
    

    private CGMSBaseDataType(int type, String description) {
        this.dataType = type;
        this.description = description;
    }


    public int getValue() {
        return dataType;
    }

    public static CGMSBaseDataType getEnum(int value)
    {
        if (map.containsKey(value))
            return map.get(value);
        else
            return CGMSBaseDataType.None;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    } 

}