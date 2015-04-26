package ggc.plugin.data.enums;

import java.util.Hashtable;

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
 *  Filename:     GlucoseUnitType
 *  Description:  Glucose Unit Type Enum
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum GlucoseUnitType implements CodeEnumWithTranslation
{

    mg_dL(1, "GLUCOSE_UNIT_MGDL"), //
    mmol_L(2, "GLUCOSE_UNIT_MMOLL"), //
    None(0, "NONE"); //

    int code;
    String i18nKey;
    String translation;

    static Hashtable<Integer, GlucoseUnitType> codeMapping = new Hashtable<Integer, GlucoseUnitType>();

    static
    {
        for (GlucoseUnitType el : values())
        {
            codeMapping.put(el.getCode(), el);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (GlucoseUnitType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }
    }


    private GlucoseUnitType(int code, String i18nKey)
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


    public static GlucoseUnitType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return GlucoseUnitType.None;
        }
    }

}
