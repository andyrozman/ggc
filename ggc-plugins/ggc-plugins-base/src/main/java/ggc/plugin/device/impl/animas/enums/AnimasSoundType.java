package ggc.plugin.device.impl.animas.enums;

import java.util.HashMap;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.core.plugins.GGCPluginType;

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
 *  Filename:     AnimasSoundType
 *  Description:  Animas Sound Type - which sound type we set
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum AnimasSoundType implements CodeEnumWithTranslation
{
    None(0, "NONE", GGCPluginType.Core), //
    Normal(1, "PCFG_SOUND_NORMAL", GGCPluginType.PumpToolPlugin), //
    AudioBolus(2, "PCFG_SOUND_AUDIOBOLUS", GGCPluginType.PumpToolPlugin), //
    TempBasal(3, "PCFG_SOUND_TEMPBASAL", GGCPluginType.PumpToolPlugin), //
    Alerts(4, "PCFG_SOUND_ALERTS", GGCPluginType.PumpToolPlugin), //
    Reminders(5, "PCFG_SOUND_REMINDERS", GGCPluginType.PumpToolPlugin), //
    Warnings(6, "PCFG_SOUND_WARNINGS", GGCPluginType.PumpToolPlugin), //
    Alarms(7, "PCFG_SOUND_ALARMS", GGCPluginType.PumpToolPlugin), //
    RemoteBolus(8, "PCFG_SOUND_REMOTEBOLUS", GGCPluginType.PumpToolPlugin), //

    CGMS_HighAlert(40, "PCFG_SOUND_HIGH_ALERT", GGCPluginType.CGMSToolPlugin), //
    CGMS_LowAlert(41, "PCFG_SOUND_LOW_ALERT", GGCPluginType.CGMSToolPlugin), //
    CGMS_RiseRate(42, "PCFG_SOUND_RISE_RATE", GGCPluginType.CGMSToolPlugin), //
    CGMS_FallRate(43, "PCFG_SOUND_FALL_RATE", GGCPluginType.CGMSToolPlugin), //
    CGMS_Range(44, "PCFG_SOUND_RANGE", GGCPluginType.CGMSToolPlugin), //
    CGMS_Others(45, "PCFG_SOUND_OTHER", GGCPluginType.CGMSToolPlugin), //
    CGMS_TransmiterOutOfRange(46, "PCFG_SOUND_TRANSMITEROUT_OF_RANGE", GGCPluginType.CGMSToolPlugin) //

    ;

    int code;
    String i18nKey;
    String translation;
    GGCPluginType pluginType;

    static Hashtable<Integer, AnimasSoundType> codeMapping = new Hashtable<Integer, AnimasSoundType>();
    static HashMap<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();

    static
    {
        for (AnimasSoundType pbt : values())
        {
            codeMapping.put(pbt.code, pbt);
        }
    }


    private AnimasSoundType(int code, String i18nKey, GGCPluginType pluginType)
    {
        this.code = code;
        this.i18nKey = i18nKey;
        this.pluginType = pluginType;
    }


    public static void translateKeywords(I18nControlAbstract ic, GGCPluginType pluginType)
    {
        for (AnimasSoundType pbt : values())
        {
            if ((pbt.pluginType == pluginType) || (pbt.pluginType == GGCPluginType.Core))
            {
                pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            }
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


    public String getName()
    {
        return this.name();
    }


    public static AnimasSoundType getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return AnimasSoundType.Normal;
        }
    }
}
