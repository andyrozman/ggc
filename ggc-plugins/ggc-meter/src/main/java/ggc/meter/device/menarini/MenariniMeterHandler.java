package ggc.meter.device.menarini;

import ggc.meter.defs.device.MeterDeviceHandler;
import ggc.plugin.data.enums.DeviceHandlerType;
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
 *  Filename:     AscensiaBreeze2
 *  Description:  Data/device reader for Arkray GlucoCard
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MenariniMeterHandler extends MeterDeviceHandler
{

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.MenariniMeterHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        MeanriniMeterDataReader dataReader = new MeanriniMeterDataReader(definition, (String) connectionParameters,
                outputWriter);
        dataReader.readData();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        MeanriniMeterDataReader dataReader = new MeanriniMeterDataReader(definition, (String) connectionParameters,
                outputWriter);
        dataReader.readConfiguration();
    }


    public void closeDevice() throws PlugInBaseException
    {

    }
}
