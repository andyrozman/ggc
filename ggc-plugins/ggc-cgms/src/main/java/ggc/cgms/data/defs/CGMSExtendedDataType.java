package ggc.cgms.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMSExtendedDataType
 *  Description: CGMS Extended Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum CGMSExtendedDataType implements CodeEnumWithTranslation
{

    None(0, "NONE"), //
    Carbs(1, "CGMS_EXT_CARBS"), //
    InsulinShortActing(2, "CGMS_EXT_INSULIN_SHORT_ACTING"), //
    Health(3, "CGMS_EXT_HEALTH"), //
    Exercise(4, "CGMS_EXT_EXERCISE"), //
    InsulinLongActing(5, "CGMS_EXT_INSULIN_LONG_ACTING"), //
    Ketones(6, "CGMS_EXT_KETONES"), //
    ManualReading(7, "CGMS_EXT_MANUAL_READING"), //

    ;



    static Hashtable<String, CGMSExtendedDataType> translationMapping = new Hashtable<String, CGMSExtendedDataType>();
    static Hashtable<Integer, CGMSExtendedDataType> codeMapping = new Hashtable<Integer, CGMSExtendedDataType>();

    static
    {
        for (CGMSExtendedDataType pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }





    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (CGMSExtendedDataType pbt : values())
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


    CGMSExtendedDataType(int code, String i18nKey)
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


    public static CGMSExtendedDataType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
            return codeMapping.get(code);
        else
            return CGMSExtendedDataType.None;
    }



}
