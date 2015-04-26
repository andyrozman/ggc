package ggc.pump.device.animas.impl.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;
import ggc.pump.device.animas.impl.data.dto.*;

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
 *  Filename:     AnimasPumpDeviceData
 *  Description:  Animas Pump Device Data
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasPumpDeviceData extends AnimasDeviceData
{

    public PumpSettings pumpSettings;
    // public PumpData pumpData;
    List<BasalLogEntry> basalLogEntries = new ArrayList<BasalLogEntry>();
    public int basalProgramNum;
    HashMap<Short, BolusEntry> bolusEntriesBySyncRecordId = new HashMap<Short, BolusEntry>();
    HashMap<Integer, BolusEntry> bolusEntriesByIndex = new HashMap<Integer, BolusEntry>();


    public AnimasPumpDeviceData(AnimasDataWriter writer)
    {
        super(writer);

        pumpSettings = new PumpSettings();

    }


    @Override
    protected void loadBgUnitIntoLocalSettings()
    {
        this.pumpSettings.glucoseUnitType = this.pumpInfo.glucoseUnitType;
    }


    @Override
    protected void loadSerialNumberIntoLocalSettings()
    {
        this.pumpSettings.serialNumber = this.pumpInfo.serialNumber;
    }


    @Override
    protected void loadClockModeIntoLocalSettings()
    {
        this.pumpSettings.clockMode = this.pumpInfo.clockMode;
    }


    @Override
    public void debugAllSettings(Log log)
    {
        this.pumpSettings.debugAllSettings(log);
    }


    @Override
    public void postProcessReceivedData(AnimasDevicePacket adp)
    {
    }


    public void writeSettings(AnimasDataType dataType)
    {
        this.pumpSettings.setOutputForSetting(this.getDataWriter().getOutputWriter());
        this.pumpSettings.writeSettingsToGGC(dataType);
    }


    @Override
    protected void loadSoftwareCodeIntoLocalSettings()
    {
        this.pumpSettings.softwareCode = this.pumpInfo.softwareCode;
    }


    public void setBasalName(int basalNr, String basalName)
    {
        debug("BasalName: basalProfileNr=" + basalNr + ", name=" + basalName);
        this.pumpSettings.setBasalName(basalNr, basalName);
    }


    public void addBasalProfileEntry(int basalProfilerNr, BasalProfileEntry basalProfileEntry)
    {
        this.pumpSettings.addBasalProfileEntry(basalProfilerNr, basalProfileEntry);
        debug("Basal: " + basalProfileEntry.time.getTimeString() + ", value=" + basalProfileEntry.amount);
    }


    public void setActiveBasalProfile(int activeBasalProfile)
    {
        this.pumpSettings.activeBasalProfile = activeBasalProfile;
        debug("ActiveBasalProfile: " + activeBasalProfile);
    }


    public List<SettingEntry> getAllSettings()
    {
        return this.pumpSettings.getAllSettings();
    }


    public void addBasalLog(ATechDate dateTime, float rate, int flag)
    {
        this.basalLogEntries.add(new BasalLogEntry(dateTime, rate, flag));
        debug(String.format("BasalLog: %s, Rate: %4.3f, Flags: %s", dateTime.getDateTimeString(), rate, flag));
    }


    public void addSettingTimeValueEntry(SettingTimeValueEntry settingTimeValueEntry)
    {
        this.pumpSettings.addSettingTimeValueEntry(settingTimeValueEntry);

        if (this.debugDataSaving)
        {
            LOG.info(settingTimeValueEntry.toString());
        }
    }


    public void setSoundVolume(AnimasSoundType soundType, SoundValueType valueType)
    {
        this.pumpSettings.soundVolumes.put(soundType, valueType);
        debug(soundType.name() + ": " + valueType.name());
    }


    public BolusEntry getBolusLogBySyncRecordId(short syncRecord)
    {
        return bolusEntriesBySyncRecordId.get(syncRecord);
    }


    public BolusEntry getBolusLogByIndex(int index)
    {
        return bolusEntriesByIndex.get(index);
    }

    int bolusIndex = 1;


    public void addBolusLogEntry(BolusEntry bolusEntry)
    {
        // NEW
        bolusEntriesByIndex.put(bolusIndex, bolusEntry);
        bolusIndex++;

        // OLD, should be removed if new is functioning correctly
        // bolusEntriesBySyncRecordId.put(bolusEntry.syncCounter, bolusEntry);
    }

}
