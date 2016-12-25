package ggc.plugin.data.enums;

import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     DeviceEntryStatus
 *  Description:  Device Entry Status enum
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum DeviceEntryStatus implements CodeEnumWithTranslation
{

    Unknown(0, "UNKNOWN", "led_gray.gif"), //
    New(1, "NEW", "led_green.gif"), //
    Changed(2, "CHANGED", "led_yellow.gif"), //
    Old(3, "OLD", "led_red.gif");

    int code;
    String i18nKey;
    String translation;
    String statusIcon;
    ImageIcon icon;

    static boolean translated = false;
    static boolean iconsLoaded = false;
    static Hashtable<Integer, DeviceEntryStatus> codeMapping = new Hashtable<Integer, DeviceEntryStatus>();
    static HashMap<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();

    static
    {
        for (DeviceEntryStatus pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    private DeviceEntryStatus(int code, String i18nKey, String statusIcon)
    {
        this.code = code;
        this.i18nKey = i18nKey;
        this.statusIcon = statusIcon;
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (DeviceEntryStatus pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
        }

        translated = true;
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


    public static DeviceEntryStatus getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return DeviceEntryStatus.Unknown;
        }
    }


    public static void loadIcons(JDialog d, DataAccessPlugInBase dataAccess)
    {
        if (iconsLoaded)
            return;

        for (DeviceEntryStatus pbt : values())
        {
            pbt.icon = ATSwingUtils.getImageIcon(pbt.statusIcon, 8, 8, d, dataAccess);
        }

        iconsLoaded = true;
    }


    public ImageIcon getIcon()
    {
        return icon;
    }
}
