package ggc.cgms.data.defs;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnum;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.cgms.util.DataAccessCGMS;

import java.util.HashMap;
import java.util.Hashtable;

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
// This class is not implemented yet, all existing methods should be rechecked
// (they were copied from similar
// class, with different type of data.

// TODO this class/enum is in refactoring process

public enum CGMSBaseDataType implements CodeEnumWithTranslation
{

    // CGMS_BG_READING(1),
    //
    // CGMS_METER_CALIBRATION(2),
    //
    // CGMS_DATA_EVENT(3),
    //
    // CGMS_DATA_ALARM(4),
    //
    // CGMS_DATA_ERROR(5),
    //
    // CGMS_TREND(6),

    None(0, "NONE"), //
    SensorReading(1, "CGMS_READING"), //
    MeterCalibration(2, "CALIBRATION_READINGS"), //
    DeviceAlarm(3, "CGMS_DATA_ALARM"), //
    DeviceEvent(4, "CGMS_DATA_EVENT"), //
    DeviceError(5, "CGMS_DATA_ERROR"), //
    SensorReadingTrend(6, "CGMS_READING_TREND"), //

    ;


    static Hashtable<String, CGMSBaseDataType> translationMapping = new Hashtable<String, CGMSBaseDataType>();
    static Hashtable<Integer, CGMSBaseDataType> codeMapping = new Hashtable<Integer, CGMSBaseDataType>();

    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSBaseDataType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;

    private CGMSBaseDataType(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
    }

    public int getCode()
    {
        return code;
    }

    public String getI18nKey()
    {
        return i18nKey;
    }


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public int getTypeFromDescription(String str)
    {
        if (translationMapping.containsKey(str))
        {
            return translationMapping.get(str).getCode();
        }
        else
        {
            return 0;
        }
    }

    public static CGMSBaseDataType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return CGMSBaseDataType.None;
        }
    }


}
