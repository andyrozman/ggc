package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     ClockModeType
 *  Description:  Clock Mode Type
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum ClockModeType implements CodeEnumWithTranslation
{
    ClockMode12Hour(1, "CLOCK_MODE_12H"), //
    ClockMode24Hour(0, "CLOCK_MODE_24H"), //
    ;

    int code;
    String i18nKey;
    String translation;

    private static Map<Integer, ClockModeType> codeMapping = new HashMap<Integer, ClockModeType>();
    private static Map<String, ClockModeType> mapByDescription = new HashMap<String, ClockModeType>();

    static
    {
        for (ClockModeType el : values())
        {
            codeMapping.put(el.getCode(), el);
        }
    }


    ClockModeType(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (ClockModeType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }

        for (ClockModeType el : values())
        {
            mapByDescription.put(el.getTranslation(), el);
        }
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


    public static ClockModeType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return ClockModeType.ClockMode12Hour;
        }
    }


    public static ClockModeType getByDescription(String description)
    {
        if (mapByDescription.containsKey(description))
        {
            return mapByDescription.get(description);
        }
        else
        {
            return ClockModeType.ClockMode12Hour;
        }
    }


    public static ClockModeType getByCode(int code, boolean reverse)
    {
        if (reverse)
        {
            return getByCode(code == 1 ? 0 : 1);
        }
        else
            return getByCode(code);

    }

}
