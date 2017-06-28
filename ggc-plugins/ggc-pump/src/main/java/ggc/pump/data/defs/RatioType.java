package ggc.pump.data.defs;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.plugin.data.enums.ValueType;

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
 *  Filename:     AnimasSettingSubType
 *  Description:  Animas Settings Sub Type
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum RatioType implements CodeEnumWithTranslation
{
    // simple values
    InsulinCHRatio(1, "PCFG_CH_INS_RATIO", ValueType.Simple), //
    InsulinBGRatio(2, "PCFG_BG_INS_RATIO", ValueType.Simple), //
    BGTarget(3, "PCFG_TARGET_BG", ValueType.Simple), //

    // delta
    InsulinCHRatioDelta(1, "PCFG_CH_INS_RATIO", ValueType.Delta), //
    InsulinBGRatioDelta(2, "PCFG_BG_INS_RATIO", ValueType.Delta), //
    BGTargetDelta(3, "PCFG_TARGET_BG", ValueType.Delta), //

    // range
    InsulinCHRatioRange(1, "PCFG_CH_INS_RATIO", ValueType.Range), //
    InsulinBGRatioRange(2, "PCFG_BG_INS_RATIO", ValueType.Range), //
    BGTargetRange(3, "PCFG_TARGET_BG", ValueType.Range), //
    ;

    int code;
    String i18nKey;
    String translation;
    ValueType valueType;
    static boolean translated = false;


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (RatioType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }

        translated = true;
    }


    RatioType(int code, String i18nKey, ValueType valueType)
    {
        this.code = code;
        this.i18nKey = i18nKey;
        this.valueType = valueType;
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


    public ValueType getValueType()
    {
        return this.valueType;
    }


    public boolean isValueRange()
    {
        return (this.valueType == ValueType.Range);
    }


    public boolean isValueDelta()
    {
        return (this.valueType == ValueType.Delta);
    }

}
