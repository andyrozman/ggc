package ggc.cgms.device.animas.impl;

import java.util.List;

import ggc.cgms.device.animas.impl.data.AnimasCGMSDeviceData;
import ggc.cgms.device.animas.impl.handler.AnimasDexcomDataHandler;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.output.OutputWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.comm.AnimasCommProtocolAbstract;
import ggc.plugin.device.impl.animas.data.dto.SettingEntry;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;


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
 *  Filename:     AnimasCGMSDeviceReader
 *  Description:  Animas CGMS Device Reader
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasCGMSDeviceReader extends AnimasDeviceReader
{
    public static final Log LOG = LogFactory.getLog(AnimasCGMSDeviceReader.class);


    public AnimasCGMSDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(portName, animasDevice, outputWriter);
    }


    public void readData() throws PlugInBaseException
    {
        AnimasDexcomDataHandler handler = new AnimasDexcomDataHandler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadCGMSData);
    }


    public void readConfiguration() throws PlugInBaseException
    {
        AnimasDexcomDataHandler handler = new AnimasDexcomDataHandler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadCGMSSettings);

        AnimasCGMSDeviceData data = (AnimasCGMSDeviceData) handler.getData();

        //data.writeSettings(outputWriter);
    }


}