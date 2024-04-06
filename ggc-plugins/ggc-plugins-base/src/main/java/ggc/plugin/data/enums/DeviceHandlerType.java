package ggc.plugin.data.enums;

import ggc.core.plugins.GGCPluginType;
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
    NullHandler(null, DownloadSupportType.NotSupportedByGGC, null, false), //
    NoHandler(null, DownloadSupportType.NotSupportedByGGC, null, false), //
    NoSupportInDevice(null, DownloadSupportType.NotSupportedByDevice, null, false), //

    // Meters
    AscensiaUsbHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.DownloadData, //
            "ggc.meter.device.ascensia.AscensiaUsbMeterHandler", true), //
    ArkrayMeterHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.DownloadData, //
            "ggc.meter.device.arkray.ArkrayMeterHandler", true), //
    MenariniMeterHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.DownloadData, //
            "ggc.meter.device.menarini.MenariniMeterHandler", true), //
    AccuChekMeterHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.DownloadData, //
            "ggc.meter.device.accuchek.AccuChekMeterHandler", true), //
    AbbottMeterHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.meter.device.abbott.AbbottNeoMeterHandler", true), //

    OneTouchUsbMeterHandler(GGCPluginType.MeterToolPlugin, DownloadSupportType.DownloadData, //
            "ggc.meter.device.abbott.AbbottNeoMeterHandler", true), //

    // Pumps
    AccuChekPumpHandler(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_Data_DataFile, //
            "ggc.pump.device.accuchek.AccuChekPumpHandler", true), //
    DanaPumpHandler(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.pump.device.accuchek.AccuChekPumpHandler", true), //
    DanaPumpHandlerV2(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.pump.device.dana.DanaPumpHandlerV2", true), //
    AnimasV2PumpHandler(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.pump.device.animas.AnimasIR1200Handler", true), //
    InsuletOmnipodHandler(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_File_Data_Config,
            "ggc.pump.device.insulet.InsuletHandler", true), //
    MinimedPumpHandler(GGCPluginType.PumpToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.pump.device.minimed.MinimedPumpDeviceHandler", false), //

    // CGMSes
    Dexcom7Handler(GGCPluginType.CGMSToolPlugin, DownloadSupportType.DownloadDataFile, //
            "", false), // only for old files
    DexcomHandler(GGCPluginType.CGMSToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.cgms.device.dexcom.DexcomHandler", true), //
    AnimasV2CGMSHandler(GGCPluginType.CGMSToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.cgms.device.animas.AnimasCGMSHandler", true), //
    AbbottLibreHandler(GGCPluginType.CGMSToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.cgms.device.abbott.libre.LibreCGMSHandler", true), //
    MinimedCGMSHandler(GGCPluginType.CGMSToolPlugin, DownloadSupportType.Download_Data_Config, //
            "ggc.cgms.device.minimed.MinimedCGMSDeviceHandler", false), //

    // Connect
    DiaSendHandler(GGCPluginType.ConnectToolPlugin, DownloadSupportType.Download_File_Data_Config, //
            "ggc.connect.software.local.diasend.DiasendHandler", true), //

    ;

    DownloadSupportType downloadSupportType;
    private String className;
    private GGCPluginType pluginType;
    private boolean dynamicallyLoad;

    DeviceHandlerType(GGCPluginType pluginType, DownloadSupportType downloadSupportType, String className, boolean dynamicallyLoad)
    {
        this.pluginType = pluginType;
        this.downloadSupportType = downloadSupportType;
        this.className = className;
        this.dynamicallyLoad = dynamicallyLoad;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return downloadSupportType;
    }

    public GGCPluginType getPluginType() {
        return pluginType;
    }

    public String getClassName() {
        return className;
    }

    public boolean isDynamicallyLoad() {
        return dynamicallyLoad;
    }
}
