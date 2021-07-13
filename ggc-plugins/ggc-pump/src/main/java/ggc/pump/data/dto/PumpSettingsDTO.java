package ggc.pump.data.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.enums.BigDecimalValueType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.*;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.PumpSettingsType;
import ggc.pump.data.defs.RatioType;

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
// NOTE: This is copied from Animas PumpSettings, but should be addapted to be
// able to use
// globally.

// FIXME: write data only if set
// FIXME: make it global

public class PumpSettingsDTO
{

    private static final Logger LOG = LoggerFactory.getLogger(PumpSettingsDTO.class);

    protected DataAccessPlugInBase dataAccess = null; // DataAccessPump.getInstance();
    protected I18nControlAbstract i18nControl = null; // dataAccess.getI18nControlInstance();
    protected OutputWriter writer;

    public Map<AnimasSoundType, SoundValueType> soundVolumes = new HashMap<AnimasSoundType, SoundValueType>();
    public ClockModeType clockMode;
    public Map<Integer, BasalPatternDTO> basalPatterns = new HashMap<Integer, BasalPatternDTO>();
    public Integer activeBasalPattern;
    public GlucoseUnitType glucoseUnitType;
    private Map<RatioType, List<RatioDTO>> timeValueSettings = new HashMap<RatioType, List<RatioDTO>>();
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
    public String languageString;

    // Animas Specific Settings
    public Short maxLock;
    public Short sickDaysCheckKetones;
    public Short sickDaysCheckBG;
    public BigDecimal sickDaysCheckOverLimit;
    public Map<Integer, String> userInfo;

    public String serialNumber;

    public String friendlyName;
    public String softwareCode;
    public String model;


    public PumpSettingsDTO(DataAccessPlugInBase dataAccess)
    {
        this.dataAccess = dataAccess;
        i18nControl = dataAccess.getI18nControlInstance();
    }


    public void addBasalPatternEntry(int basalProfilerNr, BasalPatternEntryDTO basalProfileEntry)
    {
        if (!basalPatterns.containsKey(basalProfilerNr))
        {
            this.basalPatterns.put(basalProfilerNr, new BasalPatternDTO(basalProfilerNr));
        }

        this.basalPatterns.get(basalProfilerNr).add(basalProfileEntry);
    }


    public void writeSettingsToGGC(PumpSettingsType... pumpSettingsTypes)
    {
        for (PumpSettingsType pumpSettingsType : pumpSettingsTypes)
        {
            writeSettingsToGGC(pumpSettingsType);
        }
    }


    public void writeAllSettingsToGGC(OutputWriter outputWriter)
    {
        this.writer = outputWriter;

        for (PumpSettingsType pumpSettingsType : PumpSettingsType.values())
        {
            writeSettingsToGGC(pumpSettingsType);
        }
    }


    public void writeSettingsToGGC(PumpSettingsType pumpSettingsType)
    {
        switch (pumpSettingsType)
        {
            case ClockMode:
                {
                    if (clockMode != null)
                    {
                        writeSetting("CFG_BASE_CLOCK_MODE", clockMode.getTranslation(), PumpConfigurationGroup.General);
                    }
                }
                break;

            case ActiveBasalPattern:
                {
                    if (this.activeBasalPattern != null)
                    {
                        writeSetting("PCFG_ACTIVE_BASAL_PROFILE", this.activeBasalPattern,
                            PumpConfigurationGroup.Basal);
                    }
                }
                break;

            case GlucoseUnit:
                {
                    if (glucoseUnitType != null)
                    {
                        writeSetting("CFG_BASE_GLUCOSE_UNIT", glucoseUnitType.getTranslation(),
                            PumpConfigurationGroup.General);
                    }
                }
                break;

            case DeviceSerialAndVersion:
                {
                    writeSetting("CFG_BASE_SERIAL_NUMBER", serialNumber, PumpConfigurationGroup.General);
                    writeSetting("CFG_BASE_FIRMWARE_VERSION", softwareCode, PumpConfigurationGroup.General);
                    writeSetting("CFG_BASE_PRODUCT_NAME", model, PumpConfigurationGroup.General);
                }
                break;

            case SoundSettings:
                {
                    for (Entry<AnimasSoundType, SoundValueType> entry : soundVolumes.entrySet())
                    {
                        writeSetting(
                            i18nControl.getMessage("CFG_SOUND_VOLUME_FOR") + " " + entry.getKey().getTranslation(),
                            entry.getValue().getTranslation(), PumpConfigurationGroup.Sound);
                    }
                }
                break;

            case AdvancedSettings:
                {
                    writeSetting("PCFG_AUDIO_BOLUS_ENABLED", audioBolusEnabled, PumpConfigurationGroup.Bolus);

                    if (audioBolusStepSize != null)
                    {
                        writeSetting("PCFG_AUDIO_BOLUS_STEP_SIZE",
                            audioBolusStepSize.getValue() + " " + i18nControl.getMessage("CFG_BASE_UNIT_UNIT_SHORT"),
                            PumpConfigurationGroup.Bolus);
                    }

                    writeSetting("PCFG_ADVANCED_BOLUS_ENABLED", advancedBolusEnabled, PumpConfigurationGroup.Bolus);
                    writeSetting("PCFG_BOLUS_REMINDER", bolusReminderEnabled, PumpConfigurationGroup.Bolus);

                    writeSetting("PCFG_NUMBER_OF_BASAL_PROFILES", numberOfBasalProfiles, PumpConfigurationGroup.Basal);

                    if (maxBolusProHour != null)
                    {
                        writeSetting("PCFG_MAX_BOLUS_IN_HOUR",
                            maxBolusProHour.intValue() + " " + i18nControl.getMessage("CFG_BASE_UNIT_UNIT_SHORT"),
                            PumpConfigurationGroup.Bolus);
                    }

                    writeSetting("PCFG_MAX_BASAL_IN_HOUR", maxBasalAmountProHour, BigDecimalValueType.Integer,
                        PumpConfigurationGroup.Basal);
                    writeSetting("PCFG_TOTAL_DAILY_DOSE", totalDailyDose, BigDecimalValueType.Integer,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_MAX_DOSE_IN_2HOURS", maxDoseIn2h, BigDecimalValueType.Integer,
                        PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_LOW_CARTRIDGE_WARNING", lowCartridgeWarning, PumpConfigurationGroup.Insulin);
                    writeSetting("PCFG_AUTOOFF_ENABLED", autoOffEnabled, PumpConfigurationGroup.Device);
                    writeSetting("PCFG_AUTOOFF_TIMEOUT", autoOffTimeoutHr, PumpConfigurationGroup.Device);
                    writeSetting("PCFG_IOB_ENABLED", iOBEnabled, PumpConfigurationGroup.Insulin);
                    if (bolusSpeed != null)
                    {
                        writeSetting("PCFG_BOLUS_SPEED", bolusSpeed.getDescription(), PumpConfigurationGroup.Bolus);
                    }

                    if (displayTimeout != null)
                    {
                        writeSetting("PCFG_DISPLAY_TIMEOUT", displayTimeout + " min", PumpConfigurationGroup.Device);
                    }

                    if (occlusionSensitivity != null)
                    {
                        writeSetting("PCFG_OCCLUSION_SENSITIVITY", occlusionSensitivity.name(),
                            PumpConfigurationGroup.Device);
                    }

                    if (iOBDecay != null)
                    {
                        writeSetting("PCFG_IOB_DECAY", "" + formatDecimal(iOBDecay, 2, 1),
                            PumpConfigurationGroup.Insulin);
                    }

                    if (language != null)
                    {
                        writeSetting("LANGUAGE", language.getDescription(), PumpConfigurationGroup.Device);
                    }

                    if (languageString != null)
                    {
                        writeSetting("LANGUAGE", languageString, PumpConfigurationGroup.Device);
                    }

                    // addSetting(public short maxLock;

                    if (sickDaysCheckOverLimit != null)
                    {
                        writeSetting("PCFG_SICK_DAYS_CHECK_KETONES_EVERY", sickDaysCheckKetones,
                            PumpConfigurationGroup.Device);
                        writeSetting("PCFG_SICK_DAYS_CHECK_BG_EVERY", sickDaysCheckBG, PumpConfigurationGroup.Device);

                        writeSetting("PCFG_SICK_DAYS_CHECK_OVER_LIMIT",
                            formatDecimal(sickDaysCheckOverLimit, 3, glucoseUnitType == GlucoseUnitType.mmol_L ? 1 : 0),
                            PumpConfigurationGroup.Device);
                    }

                }
                break;

            case BasalPatterns:
                {
                    // FIXME
                    for (int i = 0; i <= 48; i++) // Dana has max patterns,
                                                  // which is 48
                    {
                        if (this.basalPatterns.containsKey(i))
                        {
                            BasalPatternDTO bp = this.basalPatterns.get(i);
                            writeSetting(
                                i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + i + " "
                                        + i18nControl.getMessage("CFG_BASE_NAME"),
                                bp.getName(), PumpConfigurationGroup.Basal);

                            int slot = 1;

                            for (BasalPatternEntryDTO en : bp)
                            {
                                String key = i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + i + " "
                                        + i18nControl.getMessage("CFG_BASE_SLOT") + " "
                                        + ((slot < 10) ? "0" + slot : slot);

                                String value = i18nControl.getMessage("CFG_BASE_FROM") + "="
                                        + en.getStarTime().getTimeString() + ", "
                                        + i18nControl.getMessage("CFG_BASE_AMOUNT") + "=" + en.getRate();

                                writeSetting(key, value, PumpConfigurationGroup.Basal);

                                slot++;
                            }
                        }
                    }
                }
                break;

            case FriendlyName:
                {
                    writeSetting("PCFG_FRIENDLY_NAME", this.friendlyName, PumpConfigurationGroup.Device);
                }
                break;

            case InsulinCarbRatio:
            case InsulinBGRatio:
            case BGTargets:
                writeRatioSettings(pumpSettingsType);
                break;

            case UnknownDataType:
                break;

            default:
                LOG.warn("writeSettingsToGGC: Unknown type: " + pumpSettingsType.name());

        }

    }


    private void writeRatioSettings(PumpSettingsType settingsType)
    {
        List<RatioType> ratioTypes = RatioType.getRatioTypes(settingsType);

        for (RatioType ratioType : ratioTypes)
        {
            if (timeValueSettings.containsKey(ratioType))
            {
                for (RatioDTO stve : timeValueSettings.get(ratioType))
                {
                    writeSetting(ratioType.getTranslation() + " #" + stve.getIndex(), stve.getSettingValue(),
                        PumpConfigurationGroup.Bolus);
                }
            }
        }

    }


    public void writeSettingsToGGC(AnimasDataType dataType)
    {
        switch (dataType)
        {
            case ClockMode:
                {
                    writeSettingsToGGC( //
                        PumpSettingsType.ClockMode, //
                        PumpSettingsType.ActiveBasalPattern, //
                        PumpSettingsType.GlucoseUnit, //
                        PumpSettingsType.DeviceSerialAndVersion);
                }
                break;

            case SoundSettings:
                {
                    for (Entry<AnimasSoundType, SoundValueType> entry : soundVolumes.entrySet())
                    {
                        writeSetting(
                            i18nControl.getMessage("CFG_SOUND_VOLUME_FOR") + " " + entry.getKey().getTranslation(),
                            entry.getValue().getTranslation(), PumpConfigurationGroup.Sound);
                    }
                }
                break;

            case AdvancedSettings:
                {
                    writeSettingsToGGC(PumpSettingsType.AdvancedSettings);
                }
                break;

            case BasalProfile:
                {
                    writeSettingsToGGC(PumpSettingsType.BasalPatterns);
                }
                break;

            case FriendlyName:
                {
                    writeSettingsToGGC(PumpSettingsType.FriendlyName);
                }
                break;

            case BGTargetSetting:
                {
                    writeSettingsToGGC( //
                        PumpSettingsType.InsulinCarbRatio, //
                        PumpSettingsType.InsulinBGRatio, //
                        PumpSettingsType.BGTargets);
                }
                break;

            default:
                System.out.println("writeSettingsToGGC: " + dataType.name());

        }

    }


    public void setBasalName(int basalProfilerNr, String basalName)
    {
        if (!basalPatterns.containsKey(basalProfilerNr))
        {
            this.basalPatterns.put(basalProfilerNr, new BasalPatternDTO(basalProfilerNr));
        }

        this.basalPatterns.get(basalProfilerNr).setName(basalName);
    }


    public void addSettingTimeValueEntry(RatioDTO settingTimeValueEntry)
    {
        if (!timeValueSettings.containsKey(settingTimeValueEntry.getType()))
        {
            timeValueSettings.put(settingTimeValueEntry.getType(), new ArrayList<RatioDTO>());
        }

        timeValueSettings.get(settingTimeValueEntry.getType()).add(settingTimeValueEntry);
    }


    public void setOutputForSetting(OutputWriter writer)
    {
        this.writer = writer;
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


    protected void writeSetting(String key, String value, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            writer.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
        }
    }


    private void writeSetting(String key, Integer value, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            writeSetting(key, "" + value, group);
        }
    }


    private void writeSetting(String key, Short value, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            writeSetting(key, "" + value, group);
        }
    }


    private void writeSetting(String key, Boolean value, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            writeSetting(key, getBooleanValue(value), group);
        }
    }


    private void writeSetting(String key, BigDecimal value, BigDecimalValueType valueType, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            String valueString = "";

            switch (valueType)
            {
                case Integer:
                    valueString = "" + value.intValue();
                    break;
                case Byte:
                    valueString = "" + value.byteValue();
                    break;
                case Short:
                    valueString = "" + value.shortValue();
                    break;
            }

            writeSetting(key, valueString, group);
        }
    }

    // public void debugAllSettings(Logger log)
    // {
    // List<SettingEntry> settings = getAllSettings();
    //
    // for (SettingEntry entry : settings)
    // {
    // log.debug(entry.key + " = " + entry.value);
    // }
    // }

}
