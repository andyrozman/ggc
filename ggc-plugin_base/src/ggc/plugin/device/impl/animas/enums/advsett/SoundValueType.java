package ggc.plugin.device.impl.animas.enums.advsett;

import java.util.Hashtable;

import com.atech.utils.data.CodeEnum;

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

public enum SoundValueType implements CodeEnum
{
    NotAvailable(0), //
    Vibrate(1), //
    Low(2), //
    Medium(3), //
    High(4), //
    Unknown(-1), //
    ;

    int code;
    static Hashtable<Integer, SoundValueType> soundValueTypeCodeMapping = new Hashtable<Integer, SoundValueType>();

    static
    {
        // I18nControlAbstract i18nControlAbstract =
        // DataAccessPump.getInstance().getI18nControlInstance();

        for (SoundValueType pbt : values())
        {
            // pbt.setTranslation(i18nControlAbstract.getMessage(pbt.i18nKey));
            // basetypeTranslationMapping.put(pbt.getTranslation(), pbt);
            soundValueTypeCodeMapping.put(pbt.code, pbt);
        }
    }

    private SoundValueType(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return this.getCode();
    }

    public static SoundValueType getSoundValueTypeByCode(int code)
    {
        if (soundValueTypeCodeMapping.containsKey(code))
        {
            return soundValueTypeCodeMapping.get(code);
        }
        else
        {
            return SoundValueType.Unknown;
        }
    }

}
