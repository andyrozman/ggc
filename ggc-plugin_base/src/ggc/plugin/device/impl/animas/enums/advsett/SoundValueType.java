package ggc.plugin.device.impl.animas.enums.advsett;

import java.util.HashMap;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnum;
import com.atech.utils.data.CodeEnumWithTranslation;

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
 *  Filename:     SoundValueType
 *  Description:  Sound Value Type
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum SoundValueType implements CodeEnumWithTranslation
{
    NotAvailable(0, "CFG_SOUND_VOLUME_NA"), //
    Vibrate(1, "CFG_SOUND_VOLUME_VIBRATE"), //
    Low(2, "CFG_SOUND_VOLUME_LOW"), //
    Medium(3, "CFG_SOUND_VOLUME_MEDIUM"), //
    High(4, "CFG_SOUND_VOLUME_HIGH"), //
    Unknown(-1, "CFG_SOUND_VOLUME_UNKNOWN"), //

    Enabled(1, "CFG_SOUND_VOLUME_ENABLED"), //
    Disabled(0, "CFG_SOUND_VOLUME_DISABLED") //
    ;


    int code;
    String i18nKey;
    String translation;

    static Hashtable<Integer, SoundValueType> codeMapping = new Hashtable<Integer, SoundValueType>();
    static HashMap<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();



    static
    {
        for (SoundValueType pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }

    private SoundValueType(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        for (SoundValueType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }
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


    public static SoundValueType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return SoundValueType.Unknown;
        }
    }

}
