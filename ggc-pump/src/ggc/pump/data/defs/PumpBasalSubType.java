package ggc.pump.data.defs;

import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.pump.util.DataAccessPump;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

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

public enum PumpBasalSubType implements CodeEnumWithTranslation
{

    None(0, "NONE"),
    Value(1, "BASAL_VALUE"),
    Profile(2, "BASAL_PROFILE"),
    TemporaryBasalRate(3, "BASAL_TEMPORARY_BASAL_RATE"),
    TemporaryBasalRateProfile(4, "BASAL_TEMPORARY_BASAL_RATE_PROFILE"),
    PumpStatus(5, "BASAL_PUMP_STATUS"),
    TemporaryBasalRateEnded(6, "BASAL_TEMPORARY_BASAL_RATE_ENDED"),
    TemporaryBasalRateCanceled(7, "BASAL_TEMPORARY_BASAL_RATE_CANCELED"),

    ;



    /**
     * Basal Descriptions
     */
    public static String[] basal_desc = null;




    static Hashtable<String, PumpBasalSubType> translationMapping = new Hashtable<String, PumpBasalSubType>();
    static Hashtable<Integer, PumpBasalSubType> codeMapping = new Hashtable<Integer, PumpBasalSubType>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpBasalSubType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] basal_desc_lcl = { ic.getMessage("SELECT_BASAL_TYPE"), ic.getMessage("BASAL_VALUE"),
                ic.getMessage("BASAL_PROFILE"), ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"),
                ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"),
                ic.getMessage("BASAL_PUMP_STATUS"),
                ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_ENDED"),
                ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_CANCELED") };

        basal_desc = basal_desc_lcl;
    }

    int code;
    String i18nKey;
    String translation;

    private PumpBasalSubType(int code, String i18nKey)
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

    public static PumpBasalSubType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return PumpBasalSubType.None;
        }
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.basal_desc;
    }



}
