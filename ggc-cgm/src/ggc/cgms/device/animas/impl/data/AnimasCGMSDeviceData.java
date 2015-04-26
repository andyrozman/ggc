package ggc.cgms.device.animas.impl.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.atech.utils.data.ATechDate;

import ggc.cgms.data.defs.CGMSTransmiterEvents;
import ggc.cgms.data.writer.CGMSValuesWriter;
import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomHistoryEntry;
import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomWarning;
import ggc.cgms.device.animas.impl.data.dto.CGMSSettings;
import ggc.cgms.device.animas.impl.handler.AnimasCGMSDataWriter;
import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;

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
 *  Filename:     AnimasCGMSDeviceData
 *  Description:  Animas CGMS Device Data
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasCGMSDeviceData extends AnimasDeviceData
{

    public CGMSSettings cgmsSettings;
    // AnimasCGMSDataWriter animasCGMSDataWriter;
    CGMSValuesWriter cgmsDataWriter;

    // List<BasalLogEntry> basalLogEntries = new ArrayList<BasalLogEntry>();
    List<AnimasDexcomHistoryEntry> transmiterEvents = new ArrayList<AnimasDexcomHistoryEntry>();


    public AnimasCGMSDeviceData(AnimasDataWriter writer)
    {
        super(writer);

        cgmsSettings = new CGMSSettings();

        cgmsDataWriter = CGMSValuesWriter.getInstance(writer.getOutputWriter());
        // animasCGMSDataWriter = (AnimasCGMSDataWriter) writer;
        // pumpData = new PumpData();

    }


    public AnimasCGMSDeviceData(AnimasCGMSDataWriter animasCGMSDataWriter)
    {
        super(animasCGMSDataWriter);
        cgmsSettings = new CGMSSettings();

        // System.out.println("CGSMSet " + cgmsSettings);

        cgmsDataWriter = CGMSValuesWriter.getInstance(animasCGMSDataWriter.getOutputWriter());
    }


    @Override
    protected void loadBgUnitIntoLocalSettings()
    {
        this.cgmsSettings.glucoseUnitType = this.pumpInfo.glucoseUnitType;
    }


    @Override
    protected void loadSerialNumberIntoLocalSettings()
    {
        this.cgmsSettings.serialNumber = this.pumpInfo.serialNumber;
    }


    @Override
    protected void loadClockModeIntoLocalSettings()
    {
        this.cgmsSettings.clockMode = this.pumpInfo.clockMode;
    }


    @Override
    public void debugAllSettings(Log log)
    {
        this.cgmsSettings.debugAllSettings(log);
    }


    @Override
    public void postProcessReceivedData(AnimasDevicePacket adp)
    {
        switch (adp.dataTypeObject)
        {
            case DexcomBgHistory:
                processEventsFromBgHistory();
                break;

            default:
                LOG.warn("postProcessReceivedData [objectType=" + adp.dataTypeObject.name()
                        + "] should have postprocessing implemented, but it's not !!!");
        }

    }

    Map<Integer, Integer> mapOfUnknownSpecialValues = new HashMap<Integer, Integer>();


    private void processEventsFromBgHistory()
    {
        for (AnimasDexcomHistoryEntry adhe : transmiterEvents)
        {

            CGMSTransmiterEvents sgv = adhe.getSpecialValue();

            if (sgv == null)
            {
                if (mapOfUnknownSpecialValues.containsKey(adhe.getGlucoseValueWithoutFlags()))
                {
                    LOG.warn("Unknown Special (Transmiter) Value: " + adhe.getGlucoseValueWithoutFlags());
                }
            }
            else
            {
                writeData("TransmiterEvent_" + sgv.name(), adhe.dateTime);
            }
        }

        this.transmiterEvents.clear();
    }


    @Override
    public void writeSettings(AnimasDataType dataType)
    {
        this.cgmsSettings.setOutputForSetting(this.getDataWriter().getOutputWriter());
        this.cgmsSettings.writeSettingsToGGC(dataType);
    }


    @Override
    protected void loadSoftwareCodeIntoLocalSettings()
    {
        this.cgmsSettings.softwareCode = this.pumpInfo.softwareCode;

    }


    public void setClockMode(int clockMode)
    {
        ClockModeType cmt = ClockModeType.getByCode(clockMode);
        this.cgmsSettings.clockMode = cmt;
    }


    public List<SettingEntry> getAllSettings()
    {
        return this.cgmsSettings.getAllSettings();
    }


    public void addDexcomWarning(AnimasDexcomWarning warning)
    {
        String cgmsWriterKey = warning.warningType.getCgmsWriterKey();

        if (StringUtils.isNotBlank(cgmsWriterKey))
        {
            writeData(cgmsWriterKey, warning.dateTime);
        }
    }


    public void addDexcomHistory(AnimasDexcomHistoryEntry entry)
    {
        writeData("SensorReading", entry.dateTime, entry.getGlucoseValue());
    }


    public void setSoundVolume(AnimasSoundType animasSoundType, SoundValueType soundValueType)
    {
        cgmsSettings.soundVolumes.put(animasSoundType, soundValueType);
    }


    public void addDexcomTransmiterEvent(AnimasDexcomHistoryEntry entry)
    {
        transmiterEvents.add(entry);
    }


    public void writeData(String key, ATechDate dateTime, Number value)
    {
        cgmsDataWriter.writeObject(key, dateTime, value);
    }


    public void writeData(String key, ATechDate dateTime, String value)
    {
        cgmsDataWriter.writeObject(key, dateTime, value);
    }


    public void writeData(String key, ATechDate dateTime)
    {
        cgmsDataWriter.writeObject(key, dateTime);
    }

}
