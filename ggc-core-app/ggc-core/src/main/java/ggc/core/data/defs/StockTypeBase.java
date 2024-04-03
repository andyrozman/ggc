package ggc.core.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
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
 *  Filename:     StockTypeBase
 *  Description:  Stock Type Base
 *
 *  Author:    Andy {andy@atech-software.com}
 */
@Deprecated
public enum StockTypeBase implements CodeEnumWithTranslation
{
    None(0, "NONE"), //
    Insulin(1, "STOCKTYPE_INSULIN"), //
    BG(2, "STOCKTYPE_BG"), //
    Pen(3, "STOCKTYPE_PEN"), //
    Pump(4, "STOCKTYPE_PUMP"), //
    CGMS(5, "STOCKTYPE_CGMS");

    static String[] descriptions;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, StockTypeBase> codeMapping = new HashMap<Integer, StockTypeBase>();

    static
    {
        for (StockTypeBase pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }

    }

    int code;
    String i18nKey;
    String translation;


    StockTypeBase(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (StockTypeBase pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
        }

        String[] stockTypeDescriptions = { ic.getMessage("STOCKTYPE_ANY"), //
                                           ic.getMessage("STOCKTYPE_INSULIN"), //
                                           ic.getMessage("STOCKTYPE_BG"), //
                                           ic.getMessage("STOCKTYPE_PEN"), //
                                           ic.getMessage("STOCKTYPE_PUMP"), //
                                           ic.getMessage("STOCKTYPE_CGMS"), //
        };

        descriptions = stockTypeDescriptions;
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


    public static StockTypeBase getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return StockTypeBase.None;
        }
    }


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public static StockTypeBase getByDescription(String str)
    {
        return getByCode(ATDataAccessAbstract.getTypeFromDescription(str, translationMapping));
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
