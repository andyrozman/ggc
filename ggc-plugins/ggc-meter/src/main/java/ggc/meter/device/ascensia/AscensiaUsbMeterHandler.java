package ggc.meter.device.ascensia;

import java.util.ArrayList;
import java.util.List;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.defs.device.MeterDeviceHandler;
import ggc.meter.device.ascensia.impl.AscensiaContourUsbReader;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     AscensiaUsbMeterHandler
 *  Description:  Ascensia Usb Meter Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AscensiaUsbMeterHandler extends MeterDeviceHandler
{

    AscensiaContourUsbReader reader = null;
    MeterDeviceDefinition meterDefinition;


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AscensiaUsbHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        meterDefinition = (MeterDeviceDefinition) definition;
        reader = new AscensiaContourUsbReader(this, (String) connectionParameters, outputWriter);
        reader.readFromDevice();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public void closeDevice() throws PlugInBaseException
    {
        if (reader != null)
            reader.closeDevice();
    }


    public List<USBDevice> getAllowedDevicesList()
    {
        List<USBDevice> usbDeviceList = new ArrayList<USBDevice>();

        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x6002));
        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x7390)); // ?
        usbDeviceList.add(new USBDevice("Contour Next", 0x1a79, 0x7350));
        usbDeviceList.add(new USBDevice("Contour Next USB", 0x1a79, 0x7410));
        usbDeviceList.add(new USBDevice("Contour Next One", 0x1a79, 0x7800));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6300));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6200)); // ?

        return usbDeviceList;
    }


    public MeterDeviceDefinition getMeterDefinition()
    {
        return this.meterDefinition;
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }

}
