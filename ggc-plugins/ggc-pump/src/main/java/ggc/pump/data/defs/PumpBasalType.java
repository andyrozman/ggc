package ggc.pump.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpBasalSubType  
 *  Description:  Pump Basal Sub Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum PumpBasalType implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    Value(1, "BASAL_VALUE"), //
    Profile(2, "BASAL_PROFILE"), //
    TemporaryBasalRate(3, "BASAL_TEMPORARY_BASAL_RATE"), //
    @Deprecated
    TemporaryBasalRateProfile(4, "BASAL_TEMPORARY_BASAL_RATE_PROFILE"), //
    PumpStatus(5, "BASAL_PUMP_STATUS"), //
    TemporaryBasalRateEnded(6, "BASAL_TEMPORARY_BASAL_RATE_ENDED"), //
    TemporaryBasalRateCanceled(7, "BASAL_TEMPORARY_BASAL_RATE_CANCELED"), //
    ValueChange(8, "BASAL_VALUE_CHANGE"), //

    ;

    // tring.format("TBR_VALUE=%s%s;TBR_UNIT=%s;DURATION=%s", sign, val, unit
    // [%,U], "00:00"

    public static boolean isTemporaryBasalType(PumpBasalType type)
    {
        return (type == TemporaryBasalRate || type == TemporaryBasalRateProfile || type == TemporaryBasalRateCanceled
                || type == TemporaryBasalRateEnded);
    }

    /**
     * Basal Descriptions
     */
    public static String[] basal_desc = null;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, PumpBasalType> codeMapping = new HashMap<Integer, PumpBasalType>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpBasalType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] basal_desc_lcl = { ic.getMessage("SELECT_BASAL_TYPE"), ic.getMessage("BASAL_VALUE"), //
                                    ic.getMessage("BASAL_PROFILE"), //
                                    ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"), //
                                    ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"), //
                                    ic.getMessage("BASAL_PUMP_STATUS"), //
                                    ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_ENDED"), //
                                    ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_CANCELED"),
                                    ic.getMessage("BASAL_VALUE_CHANGE"), //
        };

        basal_desc = basal_desc_lcl;
    }

    int code;
    String i18nKey;
    String translation;


    private PumpBasalType(int code, String i18nKey)
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
    public static int getTypeFromDescription(String str)
    {
        return ATDataAccessAbstract.getTypeFromDescription(str, translationMapping);
    }


    public static PumpBasalType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return PumpBasalType.None;
        }
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public static String[] getDescriptions()
    {
        return basal_desc;
    }

}
