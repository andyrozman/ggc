package ggc.pump.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.pump.util.DataAccessPump;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
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
 * Filename:    PumpBaseType
 * Description: Pump Base Types
 *
 * Author: Andy {andy@atech-software.com}
 */

public enum PumpBaseType implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    Basal(1, "BASAL_DOSE"), //
    Bolus(2, "BOLUS_DOSE"), //
    Event(3, "EVENT"), //
    Alarm(4, "ALARM"), //
    Error(5, "ERROR"), //
    Report(6, "REPORT"), //
    PenInjectionBasal(7, "PEN_INJECTION_BASAL"), //
    PenInjectionBolus(8, "PEN_INJECTION_BOLUS"), //
    AdditionalData(9, "ADDITIONAL_DATA"), //

    ;

    static Hashtable<String, PumpBaseType> basetypeTranslationMapping = new Hashtable<String, PumpBaseType>();
    static Hashtable<Integer, PumpBaseType> basetypeCodeMapping = new Hashtable<Integer, PumpBaseType>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpBaseType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            basetypeTranslationMapping.put(pbt.getTranslation(), pbt);
            basetypeCodeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;


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


    private PumpBaseType(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
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
        if (PumpBaseType.basetypeTranslationMapping.containsKey(str))
        {
            return PumpBaseType.basetypeTranslationMapping.get(str).getCode();
        }
        else
        {
            return 0;
        }
    }


    public static PumpBaseType getByCode(int code)
    {
        if (basetypeCodeMapping.containsKey(code))
        {
            return basetypeCodeMapping.get(code);
        }
        else
        {
            return PumpBaseType.None;
        }
    }

}
