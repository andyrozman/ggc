package ggc.meter.device.onetouch;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.defs.device.MeterDeviceHandler;
import ggc.meter.device.ascensia.impl.AscensiaContourUsbReader;
import ggc.meter.device.onetouch.impl.OneTouchUsbReader;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

public class OneTouchUsbMeterHandler extends MeterDeviceHandler
{

    private static final Logger LOG = LoggerFactory.getLogger(OneTouchUsbMeterHandler.class);

    OneTouchUsbReader reader = null;
    MeterDeviceDefinition meterDefinition;


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.OneTouchUsbMeterHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        meterDefinition = (MeterDeviceDefinition) definition;
        try
        {
            reader = new OneTouchUsbReader(this, (String) connectionParameters, outputWriter);
            reader.readFromDevice();
        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            LOG.error("Unexpected exception: " + ex, ex);
            throw new PlugInBaseException(PlugInExceptionType.UnexpectedException, new Object[] { ex.getMessage() },
                    ex);
        }
        finally
        {
            this.closeDevice();
        }
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

        usbDeviceList.add(new USBDevice("LifeScan Verio Flex", 0x2766, 0x0004));

        // Bus 001 Device 025: ID 2766:0004 LifeScan Verio Flex


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
