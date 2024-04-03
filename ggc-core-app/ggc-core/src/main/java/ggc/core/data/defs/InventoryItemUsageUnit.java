package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.core.util.DataAccess;

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
 *  Filename:     InventoryItemUsageUnit
 *  Description:  Stock Usage Unit
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum InventoryItemUsageUnit implements CodeEnumWithTranslation
{
    None(0, "NONE"), //
    Day(1, "TIME_DAY"), //
    Week(2, "TIME_WEEK"), //
    Month(3, "TIME_MONTH"), //

    ;

    static String[] descriptions;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, InventoryItemUsageUnit> codeMapping = new HashMap<Integer, InventoryItemUsageUnit>();

    static
    {
        I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

        for (InventoryItemUsageUnit pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] basal_desc_lcl = { ic.getMessage("SELECT"), //
                                    ic.getMessage("TIME_DAY"), //
                                    ic.getMessage("TIME_WEEK"), //
                                    ic.getMessage("TIME_MONTH"), //
        };

        descriptions = basal_desc_lcl;
    }

    int code;
    String i18nKey;
    String translation;


    private InventoryItemUsageUnit(int code, String i18nKey)
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


    public static InventoryItemUsageUnit getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return InventoryItemUsageUnit.Day;
        }
    }


    public static InventoryItemUsageUnit getByDescription(String description)
    {
        return getByCode(getTypeFromDescription(description));
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
