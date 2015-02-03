package ggc.cgms.device.animas.impl.data;

import java.util.List;


import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomHistoryEntry;
import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomWarning;
import ggc.cgms.device.animas.impl.data.dto.CGMSSettings;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.output.OutputWriter;
import org.apache.commons.logging.Log;

import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
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

    //List<BasalLogEntry> basalLogEntries = new ArrayList<BasalLogEntry>();

    public AnimasCGMSDeviceData(AnimasDataWriter writer)
    {
        super(writer);

        cgmsSettings = new CGMSSettings();
        //pumpData = new PumpData();

    }

    @Override
    protected void loadBgUnitIntoLocalSettings()
    {
        this.cgmsSettings.glucoseUnitType = this.pumpInfo.glucoseUnitType;
    }

    @Override
    public void debugAllSettings(Log log)
    {
        this.cgmsSettings.debugAllSettings(log);
    }

    @Override
    public void postProcessReceivedData(AnimasDevicePacket adp)
    {
    }

    @Override
    public void writeSettings(OutputWriter outputWritter)
    {

    }


    public void setClockMode(int clockMode)
    {
        // FIXME
        LOG.error("clock mode: " + clockMode);

        ClockModeType cmt = ClockModeType.getEnum(clockMode);
        LOG.warn("setClockMode: " + cmt.name());

        this.cgmsSettings.clockMode = cmt;
    }

    public List<SettingEntry> getAllSettings()
    {
        return this.cgmsSettings.getAllSettings();
    }




    // FIXME not implemneted
    public void addDexcomWarning(AnimasDexcomWarning warning)
    {
        // decode
        LOG.debug(warning.toString());
    }

    public void addDexcomHistory(AnimasDexcomHistoryEntry entry)
    {
        LOG.debug(entry.toString());
    }

    public void setSoundVolume(AnimasSoundType animasSoundType, SoundValueType soundValueType)
    {
        cgmsSettings.soundVolumes.put(animasSoundType, soundValueType);
    }
}
