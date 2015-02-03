package ggc.plugin.device.impl.animas.data.dto;

import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.data.enums.GlucoseUnitType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;

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
 *  Filename:     PumpInfo
 *  Description:  Pump Info Data
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class PumpInfo
{
    public String serialNumber;
    public String friendlyName;
    public short fontTableIndex;
    public short foodDbSize;
    public String softwareCode;
    public AnimasDeviceType deviceType;
    public short languageIndex1;
    public short languageIndex2;
    public boolean registryUsed;
    public String serialNumberHex;
    public String oSRevision;
    public String oSRevisionReadable;
    public String oSRevisionHex;

    public GlucoseUnitType glucoseUnitType;
    public ClockModeType clockMode;
}
