package ggc.cgms.device.animas.impl.data.dto;

import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.data.enums.GlucoseUnitType;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.output.OutputWriter;
import org.apache.commons.logging.Log;

import java.util.HashMap;
import java.util.List;

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
    private List<SettingEntry> allSettings;
    public GlucoseUnitType glucoseUnitType;

    public HashMap<AnimasSoundType, SoundValueType> soundVolumes = new HashMap<AnimasSoundType, SoundValueType>();

    public int highAlertWarnAbove;
    public int lowAlertWarnBelow;
    public int riseRateWarnAbove;
    public int fallRateWarnAbove;
    public int highAlertSnoozeTime;
    public int lowAlertSnoozeTime;
    public int transmiterOutOfRangeSnoozeTime;

    public boolean highAlertWarningEnabled;
    public boolean lowAlertWarningEnabled;
    public boolean riseRateWarningEnabled;
    public boolean fallRateWarningEnabled;
    public boolean transmiterOutOfRangeWarningEnabled;

    public String transmiterSerialNumber;
    public ClockModeType clockMode;


    public List<SettingEntry> getAllSettings()
    {
        return allSettings;
    }

    public void debugAllSettings(Log log)
    {

    }

    public void writeSettings(OutputWriter outputWritter)
    {
    }

}
