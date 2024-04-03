package ggc.cgms.device.abbott.libre;

import java.util.List;

import ggc.cgms.defs.device.CGMSDeviceHandler;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.handler.UsbHidDeviceHandler;
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
 *  Filename:     LibreCGMSHandler
 *  Description:  Arkray Meter Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class LibreCGMSHandler extends CGMSDeviceHandler implements UsbHidDeviceHandler
{

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AbbottLibreHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        LibreCGMSDataReader dataReader = new LibreCGMSDataReader(this, definition, (String) connectionParameters,
                outputWriter);
        dataReader.readData();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        LibreCGMSDataReader dataReader = new LibreCGMSDataReader(this, definition, (String) connectionParameters,
                outputWriter);
        dataReader.readConfiguration();
    }


    public void closeDevice() throws PlugInBaseException
    {

    }


    public List<USBDevice> getAllowedDevicesList()
    {
        return null;
    }
}
