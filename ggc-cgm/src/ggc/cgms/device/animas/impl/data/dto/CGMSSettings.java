package ggc.cgms.device.animas.impl.data.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.atech.i18n.I18nControlAbstract;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.output.OutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSSettings
 *  Description:  CGMS Settings
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSSettings
{

    public OutputWriter outputWriter;
    public I18nControlAbstract i18nControl = DataAccessCGMS.getInstance().getI18nControlInstance();

    private List<SettingEntry> allSettings;
    public GlucoseUnitType glucoseUnitType;

    public Map<AnimasSoundType, SoundValueType> soundVolumes = new HashMap<AnimasSoundType, SoundValueType>();

    public Integer highAlertWarnAbove;
    public Integer lowAlertWarnBelow;
    public Short riseRateWarnAbove;
    public Short fallRateWarnAbove;
    public Integer highAlertSnoozeTime;
    public Integer lowAlertSnoozeTime;
    public Short transmiterOutOfRangeSnoozeTime;

    public Boolean highAlertWarningEnabled;
    public Boolean lowAlertWarningEnabled;
    public Boolean riseRateWarningEnabled;
    public Boolean fallRateWarningEnabled;
    public Boolean transmiterOutOfRangeWarningEnabled;

    public String transmiterSerialNumber;
    public ClockModeType clockMode;

    public String softwareCode;
    public String serialNumber;


    public List<SettingEntry> getAllSettings()
    {
        return allSettings;
    }


    public void debugAllSettings(Log log)
    {

    }


    public void writeSettingsToGGC(AnimasDataType dataType)
    {
        switch (dataType)
        {

            case DexcomSettings:
                {
                    // writeSetting("CFG_BASE_CLOCK_MODE",
                    // clockMode.getTranslation(), clockMode,
                    // CGMSConfigurationGroup.General);
                    writeSetting("CFG_BASE_GLUCOSE_UNIT", glucoseUnitType.getTranslation(), glucoseUnitType,
                        CGMSConfigurationGroup.General);
                    writeSetting("CFG_BASE_SERIAL_NUMBER", serialNumber, serialNumber, CGMSConfigurationGroup.General);
                    writeSetting("CFG_BASE_FIRMWARE_VERSION", softwareCode, softwareCode,
                        CGMSConfigurationGroup.General);

                    writeSetting(getCombinedKeyword("CCFG_X_WARNING_ABOVE", AnimasSoundType.CGMS_HighAlert), ""
                            + highAlertWarnAbove, highAlertWarnAbove, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_WARNING_BELOW", AnimasSoundType.CGMS_LowAlert), ""
                            + lowAlertWarnBelow, lowAlertWarnBelow, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_WARNING_ABOVE", AnimasSoundType.CGMS_RiseRate), ""
                            + riseRateWarnAbove, riseRateWarnAbove, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_WARNING_ABOVE", AnimasSoundType.CGMS_FallRate), ""
                            + fallRateWarnAbove, fallRateWarnAbove, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_SNOOZE_TIME", AnimasSoundType.CGMS_HighAlert), ""
                            + highAlertSnoozeTime, highAlertSnoozeTime, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_SNOOZE_TIME", AnimasSoundType.CGMS_LowAlert), ""
                            + lowAlertSnoozeTime, lowAlertSnoozeTime, CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_SNOOZE_TIME", AnimasSoundType.CGMS_TransmiterOutOfRange),
                        "" + transmiterOutOfRangeSnoozeTime, transmiterOutOfRangeSnoozeTime,
                        CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_ENABLED", AnimasSoundType.CGMS_HighAlert),
                        getBooleanValue(highAlertWarningEnabled), highAlertWarningEnabled,
                        CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_ENABLED", AnimasSoundType.CGMS_LowAlert),
                        getBooleanValue(lowAlertWarningEnabled), lowAlertWarningEnabled,
                        CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_ENABLED", AnimasSoundType.CGMS_RiseRate),
                        getBooleanValue(riseRateWarningEnabled), riseRateWarningEnabled,
                        CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_ENABLED", AnimasSoundType.CGMS_FallRate),
                        getBooleanValue(fallRateWarningEnabled), fallRateWarningEnabled,
                        CGMSConfigurationGroup.Warnings);
                    writeSetting(getCombinedKeyword("CCFG_X_ENABLED", AnimasSoundType.CGMS_TransmiterOutOfRange),
                        getBooleanValue(transmiterOutOfRangeWarningEnabled), transmiterOutOfRangeWarningEnabled,
                        CGMSConfigurationGroup.Warnings);

                    writeSetting("CFG_BASE_SERIAL_NUMBER", transmiterSerialNumber, transmiterSerialNumber,
                        CGMSConfigurationGroup.Transmiter);
                }
                break;

            default:
                System.out.println("writeSettingsToGGC::Type not supported: " + dataType.name());

        }

    }


    private String getCombinedKeyword(String keyword, AnimasSoundType soundtype)
    {
        return String.format(i18nControl.getMessage(keyword), soundtype.getTranslation());
    }


    private String getBooleanValue(Boolean val)
    {
        return val ? i18nControl.getMessage("YES") : i18nControl.getMessage("NO");
    }


    private void writeSetting(String key, String value, Object rawValue, CGMSConfigurationGroup group)
    {
        if (rawValue != null)
        {
            outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
        }
    }


    public void setOutputForSetting(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }
}
