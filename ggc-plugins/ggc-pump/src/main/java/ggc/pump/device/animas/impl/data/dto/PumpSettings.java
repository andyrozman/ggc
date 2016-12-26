package ggc.pump.device.animas.impl.data.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.*;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.RatioType;
import ggc.pump.data.dto.RatioDTO;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:     PumpSettings
 *  Description:  Pump Settings
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class PumpSettings
{

    List<SettingEntry> allSettings = null;
    public HashMap<AnimasSoundType, SoundValueType> soundVolumes = new HashMap<AnimasSoundType, SoundValueType>();
    public ClockModeType clockMode;
    public HashMap<Integer, BasalProfile> basalProfiles = new HashMap<Integer, BasalProfile>();
    public Integer activeBasalProfile = 1;
    public GlucoseUnitType glucoseUnitType;
    private HashMap<RatioType, List<RatioDTO>> timeValueSettings = new HashMap<RatioType, List<RatioDTO>>();
    public Boolean audioBolusEnabled;
    public Boolean advancedBolusEnabled = false;
    public Boolean bolusReminderEnabled = false;
    public Short numberOfBasalProfiles;
    public BigDecimal maxBolusProHour;
    public BigDecimal maxBasalAmountProHour;
    public BigDecimal totalDailyDose;
    public BigDecimal maxDoseIn2h;
    public Short lowCartridgeWarning;
    public Boolean autoOffEnabled;
    public Short autoOffTimeoutHr;
    public Boolean iOBEnabled;
    public BolusSpeed bolusSpeed;
    public Short displayTimeout;
    public OcclusionSensitivity occlusionSensitivity;
    public BigDecimal iOBDecay;
    public BolusStepSize audioBolusStepSize;
    public Language language;
    public Short maxLock;
    public Short sickDaysCheckKetones;
    public Short sickDaysCheckBG;
    public BigDecimal sickDaysCheckOverLimit;
    public HashMap<Integer, String> userInfo;

    OutputWriter writer;
    public String serialNumber;

    DataAccessPump dataAccess = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();
    public String friendlyName;
    public String softwareCode;


    public void addBasalProfileEntry(int basalProfilerNr, BasalProfileEntry basalProfileEntry)
    {
        if (!basalProfiles.containsKey(basalProfilerNr))
        {
            this.basalProfiles.put(basalProfilerNr, new BasalProfile(basalProfilerNr));
        }

        this.basalProfiles.get(basalProfilerNr).add(basalProfileEntry);

    }


    public void writeSettingsToGGC(AnimasDataType dataType)
    {
        switch (dataType)
        {
            case ClockMode:
                {
                    writeSetting("CFG_BASE_CLOCK_MODE", clockMode.getTranslation(), clockMode,
                        PumpConfigurationGroup.General);
                    writeSetting("PCFG_ACTIVE_BASAL_PROFILE", "" + this.activeBasalProfile, this.activeBasalProfile,
                        PumpConfigurationGroup.Basal);
                    writeSetting("CFG_BASE_GLUCOSE_UNIT", glucoseUnitType.getTranslation(), glucoseUnitType,
                        PumpConfigurationGroup.General);
                    writeSetting("CFG_BASE_SERIAL_NUMBER", serialNumber, serialNumber, PumpConfigurationGroup.General);
                    writeSetting("CFG_BASE_FIRMWARE_VERSION", softwareCode, softwareCode,
                        PumpConfigurationGroup.General);

                }
                break;

            case SoundSettings:
                {
                    for (Entry<AnimasSoundType, SoundValueType> entry : soundVolumes.entrySet())
                    {
                        writeSetting(
                            i18nControl.getMessage("CFG_SOUND_VOLUME_FOR") + " " + entry.getKey().getTranslation(),
                            entry.getValue().getTranslation(), entry.getValue(), PumpConfigurationGroup.Sound);
                    }
                }
                break;

            case AdvancedSettings:
                {
                    writeSetting("PCFG_AUDIO_BOLUS_ENABLED", getBooleanValue(audioBolusEnabled), audioBolusEnabled,
                        PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_AUDIO_BOLUS_STEP_SIZE", audioBolusStepSize.getValue(), audioBolusStepSize,
                        PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_ADVANCED_BOLUS_ENABLED", getBooleanValue(advancedBolusEnabled),
                        advancedBolusEnabled, PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_BOLUS_REMINDER", getBooleanValue(bolusReminderEnabled), bolusReminderEnabled,
                        PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_NUMBER_OF_BASAL_PROFILES", "" + numberOfBasalProfiles, numberOfBasalProfiles,
                        PumpConfigurationGroup.Basal);
                    writeSetting("PCFG_MAX_BOLUS_IN_HOUR", "" + maxBolusProHour.intValue(), maxBolusProHour,
                        PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_MAX_BASAL_IN_HOUR", "" + maxBasalAmountProHour.intValue(), maxBasalAmountProHour,
                        PumpConfigurationGroup.Basal);
                    writeSetting("PCFG_TOTAL_DAILY_DOSE", "" + totalDailyDose.intValue(), totalDailyDose,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_MAX_DOSE_IN_2HOURS", "" + maxDoseIn2h.intValue(), maxDoseIn2h,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_LOW_CARTRIDGE_WARNING", "" + lowCartridgeWarning, lowCartridgeWarning,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_AUTOOFF_ENABLED", getBooleanValue(autoOffEnabled), autoOffEnabled,
                        PumpConfigurationGroup.Device);
                    writeSetting("PCFG_AUTOOFF_TIMEOUT", "" + autoOffTimeoutHr, autoOffTimeoutHr,
                        PumpConfigurationGroup.Device);
                    writeSetting("PCFG_IOB_ENABLED", getBooleanValue(iOBEnabled), iOBEnabled,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_BOLUS_SPEED", bolusSpeed.getDescription(), bolusSpeed,
                        PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_DISPLAY_TIMEOUT", displayTimeout + " min", displayTimeout,
                        PumpConfigurationGroup.Device);
                    writeSetting("PCFG_OCCLUSION_SENSITIVITY", occlusionSensitivity.name(), occlusionSensitivity,
                        PumpConfigurationGroup.Device);
                    writeSetting("PCFG_IOB_DECAY", "" + formatDecimal(iOBDecay, 2, 1), iOBDecay,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("LANGUAGE", language.getDescription(), language, PumpConfigurationGroup.Device);
                    // addSetting(public short maxLock;

                    if (sickDaysCheckOverLimit != null)
                    {
                        writeSetting("PCFG_SICK_DAYS_CHECK_KETONES_EVERY", "" + sickDaysCheckKetones,
                            sickDaysCheckKetones, PumpConfigurationGroup.Device);
                        writeSetting("PCFG_SICK_DAYS_CHECK_BG_EVERY", "" + sickDaysCheckBG, sickDaysCheckBG,
                            PumpConfigurationGroup.Device);
                        writeSetting("PCFG_SICK_DAYS_CHECK_OVER_LIMIT",
                            formatDecimal(sickDaysCheckOverLimit, 3, glucoseUnitType == GlucoseUnitType.mmol_L ? 1 : 0),
                            sickDaysCheckOverLimit, PumpConfigurationGroup.Device);
                    }

                }
                break;

            case BasalProfile:
                {
                    for (int i = 1; i < 5; i++)
                    {
                        if (this.basalProfiles.containsKey(i))
                        {
                            BasalProfile bp = this.basalProfiles.get(i);
                            writeSetting(
                                i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + i + " "
                                        + i18nControl.getMessage("CFG_BASE_NAME"),
                                bp.name, bp, PumpConfigurationGroup.Basal);

                            int slot = 1;

                            for (BasalProfileEntry en : bp)
                            {
                                String key = i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + i + " "
                                        + i18nControl.getMessage("CFG_BASE_SLOT") + " "
                                        + ((slot < 10) ? "0" + slot : slot);

                                String value = i18nControl.getMessage("CFG_BASE_FROM") + "=" + en.time.getTimeString()
                                        + ", " + i18nControl.getMessage("CFG_BASE_AMOUNT") + "=" + en.amount;

                                writeSetting(key, value, en, PumpConfigurationGroup.Basal);

                                slot++;
                            }
                        }
                    }
                }
                break;

            case FriendlyName:
                {
                    writeSetting("PCFG_FRIENDLY_NAME", this.friendlyName, this.friendlyName,
                        PumpConfigurationGroup.Device);

                }
                break;

            case BGTargetSetting:
                for (Entry<RatioType, List<RatioDTO>> entry : timeValueSettings.entrySet())
                {
                    for (RatioDTO stve : entry.getValue())
                    {
                        writeSetting(entry.getKey().getTranslation() + " #" + stve.getIndex(), stve.getSettingValue(),
                            stve, PumpConfigurationGroup.Bolus);
                    }
                }
                break;

            default:
                System.out.println("writeSettingsToGGC: " + dataType.name());

        }

    }


    public void setBasalName(int basalProfilerNr, String basalName)
    {
        if (!basalProfiles.containsKey(basalProfilerNr))
        {
            this.basalProfiles.put(basalProfilerNr, new BasalProfile(basalProfilerNr));
        }

        this.basalProfiles.get(basalProfilerNr).name = basalName;
    }


    public void addSettingTimeValueEntry(RatioDTO settingTimeValueEntry)
    {
        if (!timeValueSettings.containsKey(settingTimeValueEntry.getType()))
        {
            timeValueSettings.put(settingTimeValueEntry.getType(), new ArrayList<RatioDTO>());
        }

        timeValueSettings.get(settingTimeValueEntry.getType()).add(settingTimeValueEntry);
    }


    public List<SettingEntry> getAllSettings()
    {

        if (allSettings == null)
            createAllSettings();

        return allSettings;

    }


    public void setOutputForSetting(OutputWriter writer)
    {
        this.writer = writer;
    }


    public void createAllSettings()
    {
        // allSettings = new ArrayList<SettingEntry>();
        //
        // // I18n
        //
        // addSetting("CLOCK_MODE", clockMode.getDescription(), clockMode,
        // PumpConfigurationGroup.General);
        // addSetting("ACTIVE_BASAL_PROFILE", "" + this.activeBasalProfile,
        // this.activeBasalProfile, PumpConfigurationGroup.Basal);
        // addSetting("GLUCOSE_UNIT", glucoseUnitType.getDescription(),
        // glucoseUnitType, PumpConfigurationGroup.General);
        //
        // if
        // (AnimasUtils.wasDataTypeProcessed(AnimasDataType.AdvancedSettings))
        // {
        // addSetting("AUDIO_BOLUS_ENABLED", getBooleanValue(audioBolusEnabled),
        // audioBolusEnabled, PumpConfigurationGroup.Bolus);
        // addSetting("AUDIO_BOLUS_STEP_SIZE", audioBolusStepSize.getValue(),
        // audioBolusStepSize, PumpConfigurationGroup.Bolus);
        // addSetting("ADVANCED_BOLUS_ENABLED",
        // getBooleanValue(advancedBolusEnabled), advancedBolusEnabled,
        // PumpConfigurationGroup.Bolus);
        // addSetting("BOLUS_REMINDER", getBooleanValue(bolusReminderEnabled),
        // bolusReminderEnabled, PumpConfigurationGroup.Bolus);
        // addSetting("NUMBER_OF_BASAL_PROFILES", "" + numberOfBasalProfiles,
        // numberOfBasalProfiles, PumpConfigurationGroup.Basal);
        // addSetting("MAX_BOLUS_IN_HOUR", "" + maxBolusProHour.intValue(),
        // maxBolusProHour, PumpConfigurationGroup.Bolus);
        // addSetting("MAX_BASAL_IN_HOUR", "" +
        // maxBasalAmountProHour.intValue(), maxBasalAmountProHour,
        // PumpConfigurationGroup.Basal);
        // addSetting("TOTAL_DAILY_DOSE", "" + totalDailyDose.intValue(),
        // totalDailyDose, PumpConfigurationGroup.Insulin);
        // addSetting("MAX_DOSE_IN_2HOURS", "" + maxDoseIn2h.intValue(),
        // maxDoseIn2h, PumpConfigurationGroup.Insulin);
        // addSetting("LOW_CARTRIDGE_WARNING", "" + lowCartridgeWarning,
        // lowCartridgeWarning, PumpConfigurationGroup.Insulin);
        // addSetting("AUTOOFF_ENABLED", getBooleanValue(autoOffEnabled),
        // autoOffEnabled, PumpConfigurationGroup.Device);
        // addSetting("AUTOOFF_TIMEOUT", "" + autoOffTimeoutHr,
        // autoOffTimeoutHr, PumpConfigurationGroup.Device);
        // addSetting("IOB_ENABLED", getBooleanValue(iOBEnabled), iOBEnabled,
        // PumpConfigurationGroup.Insulin);
        // addSetting("BOLUS_SPEED", bolusSpeed.getDescription(), bolusSpeed,
        // PumpConfigurationGroup.Bolus);
        // addSetting("DISPLAY_TIMEOUT", displayTimeout + " min",
        // displayTimeout, PumpConfigurationGroup.Device);
        // addSetting("OCCLUSION_SENSITIVITY", occlusionSensitivity.name(),
        // occlusionSensitivity, PumpConfigurationGroup.Device);
        // addSetting("IOB_DECAY", "" + formatDecimal(iOBDecay, 2, 1), iOBDecay,
        // PumpConfigurationGroup.Insulin);
        // addSetting("LANGUAGE", language.getDescription(), language,
        // PumpConfigurationGroup.Device);
        // // addSetting(public short maxLock;
        //
        //
        //
        // if (sickDaysCheckOverLimit != null)
        // {
        // addSetting("SICK_DAYS_CHECK_KETONES_EVERY", "" +
        // sickDaysCheckKetones.shortValue(), sickDaysCheckKetones,
        // PumpConfigurationGroup.Device);
        // addSetting("SICK_DAYS_CHECK_BG_EVERY", "" +
        // sickDaysCheckBG.shortValue(), sickDaysCheckBG,
        // PumpConfigurationGroup.Device);
        // addSetting("SICK_DAYS_CHECK_OVER_LIMIT",
        // formatDecimal(sickDaysCheckOverLimit, 3, glucoseUnitType ==
        // GlucoseUnitType.mmol_L ? 1 : 0), sickDaysCheckOverLimit,
        // PumpConfigurationGroup.Device);
        // }
        // }
        //
        // // basal profiles
        // for (int i = 1; i < 5; i++)
        // {
        // if (this.basalProfiles.containsKey(i))
        // {
        // BasalProfile bp = this.basalProfiles.get(i);
        // addSetting("BASAL_PROFILE_" + i + "_NAME", bp.name, bp,
        // PumpConfigurationGroup.Basal);
        //
        // for (BasalProfileEntry en : bp)
        // {
        // addSetting("BASAL_PROFILE_" + i + "_" + en.time.getTimeString(), "" +
        // en.amount, en, PumpConfigurationGroup.Basal);
        // }
        // }
        // }
        //
        // // sound volumes
        // for (Entry<AnimasSettingSubType, List<SettingTimeValueEntry>> entry :
        // timeValueSettings.entrySet())
        // {
        // for (SettingTimeValueEntry stve : entry.getValue())
        // {
        // addSetting(entry.getKey().name() + stve.index,
        // stve.getSettingValue(), stve, PumpConfigurationGroup.Sound);
        // }
        // }
    }


    private String formatDecimal(BigDecimal decimalNr, int nrPlaces, int decPlaces)
    {
        if (decPlaces == 0)
            return String.format("%d", decimalNr.intValue());
        else
        {
            return String.format("%" + nrPlaces + "." + decPlaces + "f", decimalNr.floatValue());
        }

    }


    private String getBooleanValue(Boolean val)
    {
        return val ? i18nControl.getMessage("YES") : i18nControl.getMessage("NO");
    }


    private void addSetting(String key, String value, Object rawValue, PumpConfigurationGroup group)
    {
        if (rawValue != null)
        {
            allSettings.add(new SettingEntry(key, value));
        }
    }


    private void writeSetting(String key, String value, Object rawValue, PumpConfigurationGroup group)
    {
        if (rawValue != null)
        {
            writer.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
        }
    }


    public void debugAllSettings(Logger log)
    {
        List<SettingEntry> settings = getAllSettings();

        for (SettingEntry entry : settings)
        {
            log.debug(entry.key + " = " + entry.value);
        }
    }

}
