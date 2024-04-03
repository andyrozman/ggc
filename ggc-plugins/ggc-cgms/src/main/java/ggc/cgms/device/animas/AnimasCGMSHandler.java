package ggc.cgms.device.animas;

import ggc.cgms.defs.device.CGMSDeviceHandler;
import ggc.cgms.device.animas.impl.AnimasCGMSDeviceReader;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasCGMSHandler extends CGMSDeviceHandler
{

    public AnimasCGMSHandler()
    {
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AnimasV2CGMSHandler;
    }


    public void readDeviceData(DeviceDefinition definition, //
            Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException
    {
        AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader( //
                getCommunicationPort(connectionParameters), //
                this.getAnimasDeviceType(definition), //
                outputWriter);
        reader.readData();
    }


    public void readConfiguration(DeviceDefinition definition, //
            Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException
    {
        AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader( //
                getCommunicationPort(connectionParameters), //
                this.getAnimasDeviceType(definition), //
                outputWriter);
        reader.readConfiguration();
    }


    public void closeDevice() throws PlugInBaseException
    {
        // not used - handled internally
    }


    private AnimasDeviceType getAnimasDeviceType(DeviceDefinition definition)
    {
        return (AnimasDeviceType) getDeviceDefinition(definition).getInternalDefintion();
    }

}
