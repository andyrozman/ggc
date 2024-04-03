package ggc.core.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     Health
 *  Description:  Health Type
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum Health implements CodeEnumWithTranslation
{
    Undefined(0, "BASE_UNDEFINED"), //
    Illness(1, "HEALTH_ILLNESS"), //
    Stress(2, "HEALTH_STRESS"), //
    HighSymptoms(3, "HEALTH_HIGH_SYMPTOMS"), //
    LowSymptoms(4, "HEALTH_LOW_SYMPTOMS"), //
    Cycle(5, "HEALTH_CYCLE"), //
    Alcohol(6, "HEALTH_ALCOHOL"), //
    Medication(7, "HEALTH_MEDICATION"), //
    ;


    static Hashtable<String, Health> translationMapping = new Hashtable<String, Health>();
    static Hashtable<Integer, Health> codeMapping = new Hashtable<Integer, Health>();

    static
    {
        for (Health pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (Health pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        translated = true;
    }

    int code;
    String i18nKey;
    String translation;
    static boolean translated = false;


    Health(int code, String i18nKey)
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


    public static Health getByCode(int code)
    {
        if (codeMapping.containsKey(code))
            return codeMapping.get(code);
        else
            return Health.Undefined;
    }


    public static Health[] getAllValues()
    {
        return values();
    }

}
