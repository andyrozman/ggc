package ggc.plugin.device.impl.minimed.enums;

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
 *  Filename:     MinimedTargetType
 *  Description:  Target type for commands. Used to get list of commands available for
 *       specific transfer type for specific device.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum MinimedTargetType
{
    ActionCommand, //
    InitCommand,

    PumpConfiguration, //
    PumpData, //
    PumpConfiguration_NA, //
    PumpData_NA, //

    PumpDataAndConfiguration, //
    CGMSConfiguration, //
    CGMSData, //

    BaseCommand, //

    Pump(MinimedTransferType.DownloadPumpData, MinimedTransferType.DownloadPumpSettings), //
    CGMS(MinimedTransferType.DownloadCGMSData, MinimedTransferType.DownloadCGMSSettings), //

    CGMSData_NA, //
    CGMSConfiguration_NA;

    MinimedTransferType downloadData;
    MinimedTransferType downloadSettings;


    MinimedTargetType()
    {
    }


    MinimedTargetType(MinimedTransferType downloadData, MinimedTransferType downloadSettings)
    {
        this.downloadData = downloadData;
        this.downloadSettings = downloadSettings;
    }


    public MinimedTransferType getDownloadData()
    {
        return downloadData;
    }


    public MinimedTransferType getDownloadSettings()
    {
        return downloadSettings;
    }
}
