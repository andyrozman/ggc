package ggc.cgms.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

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
 * Filename: CGMBaseDataType
 * Description: CGMS Base Data types
 *
 * Author: Andy {andy@atech-software.com}
 */

public enum CGMSBaseDataType implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    SensorReading(1, "CGMS_READING"), //
    SensorCalibration(2, "CALIBRATION_READINGS"), //
    Alarm(3, "CGMS_DATA_ALARM"), //
    Event(4, "CGMS_DATA_EVENT"), //
    Error(5, "CGMS_DATA_ERROR"), //
    SensorReadingTrend(6, "CGMS_READING_TREND"), //
    TransmiterEvent(7, "CGMS_TRANSMITER_EVENT")

    ;

    ;

    static Hashtable<String, CGMSBaseDataType> translationMapping = new Hashtable<String, CGMSBaseDataType>();
    static Hashtable<Integer, CGMSBaseDataType> codeMapping = new Hashtable<Integer, CGMSBaseDataType>();
    private static boolean translated;

    static
    {
        for (CGMSBaseDataType pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (CGMSBaseDataType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        translated = true;
    }

    int code;
    String i18nKey;
    String translation;


    CGMSBaseDataType(int code, String i18nKey)
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


    public String getName()
    {
        return this.name();
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
