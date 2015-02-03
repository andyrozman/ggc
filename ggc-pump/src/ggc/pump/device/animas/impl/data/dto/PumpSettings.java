package ggc.pump.device.animas.impl.data.dto;

import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.data.enums.GlucoseUnitType;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSettingSubType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.BolusSpeed;
import ggc.plugin.device.impl.animas.enums.advsett.BolusStepSize;
import ggc.plugin.device.impl.animas.enums.advsett.Language;
import ggc.plugin.device.impl.animas.enums.advsett.OcclusionSensitivity;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

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
    public int activeBasalProfile = 1;
    public GlucoseUnitType glucoseUnitType;
    private HashMap<AnimasSettingSubType, List<SettingTimeValueEntry>> timeValueSettings = new HashMap<AnimasSettingSubType, List<SettingTimeValueEntry>>();
    public boolean audioBolusEnabled = true;
    public boolean advancedBolusEnabled = false;
    public boolean bolusReminderEnabled = false;
    public short numberOfBasalProfiles = 1;
    public BigDecimal maxBolusProHour;
    public BigDecimal maxBasalAmountProHour;
    public BigDecimal totalDailyDose;
    public BigDecimal maxDoseIn2h;
    public short lowCartridgeWarning;
    public boolean autoOffEnabled = true;
    public short autoOffTimeoutHr;
    public boolean iOBEnabled;
    public BolusSpeed bolusSpeed;
    public short displayTimeout;
    public OcclusionSensitivity occlusionSensitivity;
    public BigDecimal iOBDecay;
    public BolusStepSize audioBolusStepSize;
    public Language language;
    public short maxLock;
    public Short sickDaysCheckKetones;
    public Short sickDaysCheckBG;
    public BigDecimal sickDaysCheckOverLimit;
    public HashMap<Integer, String> userInfo;


    public void addBasalProfileEntry(int basalProfilerNr, BasalProfileEntry basalProfileEntry)
    {
        if (!basalProfiles.containsKey(basalProfilerNr))
        {
            this.basalProfiles.put(basalProfilerNr, new BasalProfile(basalProfilerNr));
        }

        this.basalProfiles.get(basalProfilerNr).add(basalProfileEntry);

    }

    public void addCommandsProcessed()
    {

    }

    public void setBasalName(int basalProfilerNr, String basalName)
    {
        if (!basalProfiles.containsKey(basalProfilerNr))
        {
            this.basalProfiles.put(basalProfilerNr, new BasalProfile(basalProfilerNr));
        }

        this.basalProfiles.get(basalProfilerNr).name = basalName;
    }

    public void addSettingTimeValueEntry(SettingTimeValueEntry settingTimeValueEntry)
    {
        if (!timeValueSettings.containsKey(settingTimeValueEntry.type))
        {
            timeValueSettings.put(settingTimeValueEntry.type, new ArrayList<SettingTimeValueEntry>());
        }

        timeValueSettings.get(settingTimeValueEntry.type).add(settingTimeValueEntry);
    }

    public List<SettingEntry> getAllSettings()
    {

        if (allSettings == null)
            createAllSettings();

        return allSettings;

    }

    private void createAllSettings()
    {
        allSettings = new ArrayList<SettingEntry>();

        // I18n

        addSetting("CLOCK_MODE", clockMode.getDescription());
        addSetting("ACTIVE_BASAL_PROFILE", "" + this.activeBasalProfile);
        addSetting("GLUCOSE_UNIT", glucoseUnitType.getDescription());

        if (AnimasUtils.wasDataTypeProcessed(AnimasDataType.AdvancedSettings))
        {
            addSetting("AUDIO_BOLUS_ENABLED", getBooleanValue(audioBolusEnabled));
            addSetting("AUDIO_BOLUS_STEP_SIZE", audioBolusStepSize.getValue());
            addSetting("ADVANCED_BOLUS_ENABLED", getBooleanValue(advancedBolusEnabled));
            addSetting("BOLUS_REMINDER", getBooleanValue(bolusReminderEnabled));
            addSetting("NUMBER_OF_BASAL_PROFILES", "" + numberOfBasalProfiles);
            addSetting("MAX_BOLUS_IN_HOUR", "" + maxBolusProHour.intValue());
            addSetting("MAX_BASAL_IN_HOUR", "" + maxBasalAmountProHour.intValue());
            addSetting("TOTAL_DAILY_DOSE", "" + totalDailyDose.intValue());
            addSetting("MAX_DOSE_IN_2HOURS", "" + maxDoseIn2h.intValue());
            addSetting("LOW_CARTRIDGE_WARNING", "" + lowCartridgeWarning);
            addSetting("AUTOOFF_ENABLED", getBooleanValue(autoOffEnabled));
            addSetting("AUTOOFF_TIMEOUT", "" + autoOffTimeoutHr);
            addSetting("IOB_ENABLED", getBooleanValue(iOBEnabled));
            addSetting("BOLUS_SPEED", bolusSpeed.getDescription());
            addSetting("DISPLAY_TIMEOUT", displayTimeout + " min");
            addSetting("OCCLUSION_SENSITIVITY", occlusionSensitivity.name());
            addSetting("IOB_DECAY", "" + formatDecimal(iOBDecay, 2, 1));
            addSetting("LANGUAGE", language.getDescription());
            // addSetting(public short maxLock;

            if (sickDaysCheckOverLimit != null)
            {
                addSetting("SICK_DAYS_CHECK_KETONES_EVERY", "" + sickDaysCheckKetones.shortValue());
                addSetting("SICK_DAYS_CHECK_BG_EVERY", "" + sickDaysCheckBG.shortValue());
                addSetting("SICK_DAYS_CHECK_OVER_LIMIT",
                    formatDecimal(sickDaysCheckOverLimit, 3, glucoseUnitType == GlucoseUnitType.mmol_L ? 1 : 0));
            }
        }

        // basal profiles
        for (int i = 1; i < 5; i++)
        {
            if (this.basalProfiles.containsKey(i))
            {
                BasalProfile bp = this.basalProfiles.get(i);
                addSetting("BASAL_PROFILE_" + i + "_NAME", bp.name);

                for (BasalProfileEntry en : bp)
                {
                    addSetting("BASAL_PROFILE_" + i + "_" + en.time.getTimeString(), "" + en.amount);
                }
            }
        }

        // sound volumes
        for (Entry<AnimasSettingSubType, List<SettingTimeValueEntry>> entry : timeValueSettings.entrySet())
        {
            for (SettingTimeValueEntry stve : entry.getValue())
            {
                addSetting(entry.getKey().name() + stve.index, stve.getSettingValue());
            }
        }
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

    private String getBooleanValue(boolean val)
    {
        return val ? "YES" : "NO";
    }

    private void addSetting(String key, String value)
    {
        allSettings.add(new SettingEntry(key, value));
    }

    public void debugAllSettings(Log log)
    {
        List<SettingEntry> settings = getAllSettings();

        for (SettingEntry entry : settings)
        {
            log.debug(entry.key + " = " + entry.value);
        }
    }

}
