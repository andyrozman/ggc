package ggc.pump.data.defs;

import java.util.HashMap;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccess;
import com.atech.utils.data.CodeEnumWithTranslation;

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
 *  Filename:     PumpReport  
 *  Description:  Pump Report Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum PumpReport implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    Misc(1, "REPORT_MISC"), //
    BolusTotalDay(2, "REPORT_BOLUS_TOTAL_DAY"), //
    BasalTotalDay(3, "REPORT_BASAL_TOTAL_DAY"), //
    InsulinTotalDay(4, "REPORT_INSULIN_TOTAL_DAY"), //

    ;

    /**
     * Report Descriptions
     */
    public static String[] descriptions = null;

    static HashMap<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static HashMap<Integer, PumpReport> codeMapping = new HashMap<Integer, PumpReport>();

    static
    {
        for (PumpReport pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (PumpReport pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        String[] reportDescriptions = { ic.getMessage("SELECT_SUBTYPE"), //
                                       ic.getMessage("REPORT_MISC"), //
                                       ic.getMessage("REPORT_BOLUS_TOTAL_DAY"), //
                                       ic.getMessage("REPORT_BASAL_TOTAL_DAY"), //
                                       ic.getMessage("REPORT_INSULIN_TOTAL_DAY"), };

        descriptions = reportDescriptions;

        translated = true;
    }

    int code;
    String i18nKey;
    String translation;
    static boolean translated = false;


    PumpReport(int code, String i18nKey)
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
        return ATDataAccess.getTypeFromDescription(str, translationMapping);
    }


    public static PumpReport getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return PumpReport.None;
        }
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public static String[] getDescriptions()
    {
        return descriptions;
    }

}
