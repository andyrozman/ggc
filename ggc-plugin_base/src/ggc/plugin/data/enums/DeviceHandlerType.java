package ggc.plugin.data.enums;

import ggc.plugin.device.DownloadSupportType;

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
 *  Filename:     DeviceHandlerType
 *  Description:  Device Handler Type. This is enum of all device handlers (of any plugin)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum DeviceHandlerType
{
    NoHandler(DownloadSupportType.NotSupportedByGGC), //
    NoSupportInDevice(DownloadSupportType.NotSupportedByDevice), //

    // Meters
    AscensiaUsbHandler(DownloadSupportType.DownloadData), //

    // Pumps
    AnimasV2PumpHandler(DownloadSupportType.Download_Data_Config), //
    MinimedPumpHandler(DownloadSupportType.Download_Data_Config), //
    InsuletOmnipodHandler(DownloadSupportType.Download_File_Data_Config), //
    AccuChekPumpHandler(DownloadSupportType.Download_Data_DataFile), //
    DanaPumpHandler(DownloadSupportType.Download_Data_Config), //

    // CGMSes
    AnimasV2CGMSHandler(DownloadSupportType.Download_Data_Config), //
    DexcomHandler(DownloadSupportType.Download_Data_Config), //
    MinimedCGMSHandler(DownloadSupportType.Download_Data_Config), //

    ;

    DownloadSupportType downloadSupportType;


    DeviceHandlerType(DownloadSupportType downloadSupportType)
    {
        this.downloadSupportType = downloadSupportType;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return downloadSupportType;
    }

}
