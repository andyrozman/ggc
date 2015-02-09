package ggc.pump.data.defs;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccess;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.pump.util.DataAccessPump;

import java.util.HashMap;
import java.util.Hashtable;

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
 *  Filename:     PumpBolusType  
 *  Description:  Pump Bolus Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum PumpBolusType implements CodeEnumWithTranslation
{
    None(0, "NONE"), //
    Normal(1, "BOLUS_STANDARD"),
    Audio(2, "BOLUS_AUDIO"),
    Extended(3, "BOLUS_SQUARE", "AMOUNT_SQUARE=%s;DURATION=%s"),
    Multiwave(4, "BOLUS_MULTIWAVE", "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s")

    ;

    static String[] bolus_desc;
    static HashMap<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static HashMap<Integer, PumpBolusType> codeMapping = new HashMap<Integer, PumpBolusType>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpBolusType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);

            //System.out.println("Tr: " + ic.getMessage(pbt.i18nKey));
        }


        String[] bolus_desc_lcl = { ic.getMessage("SELECT_BOLUS_TYPE"),
                ic.getMessage("BOLUS_STANDARD"), //
                ic.getMessage("BOLUS_AUDIO"), //
                ic.getMessage("BOLUS_SQUARE"), //
                ic.getMessage("BOLUS_MULTIWAVE"), };

        bolus_desc = bolus_desc_lcl;

        for(String s : bolus_desc_lcl)
        {
            System.out.println("Tr: " + s);
        }

    }

    int code;
    String i18nKey;
    String translation;
    String valueTemplate;

    private PumpBolusType(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }

    private PumpBolusType(int code, String i18nKey, String valueTemplate)
    {
        this.code = code;
        this.i18nKey = i18nKey;
        this.valueTemplate = valueTemplate;
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
    public static int getTypeFromDescription(String str)
    {
        return ATDataAccess.getTypeFromDescription(str, translationMapping);
    }


    public static PumpBolusType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return PumpBolusType.None;
        }
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public static String[] getDescriptions()
    {
        return bolus_desc;
    }
}
