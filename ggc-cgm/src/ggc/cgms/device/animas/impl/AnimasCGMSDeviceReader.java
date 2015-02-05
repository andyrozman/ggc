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
import ggc.plugin.device.impl.animas.util.AnimasException;

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


    public AnimasCGMSDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws AnimasException
    {
        super(portName, animasDevice, outputWriter);
    }


    public void downloadCGMSData() throws PlugInBaseException
    {
        try
        {
            AnimasDexcomDataHandler handler = new AnimasDexcomDataHandler(portName, animasDevice, this, outputWriter);
            handler.startAction(AnimasTransferType.DownloadCGMSData);
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException("Exception running downloadCGMSData: " + ex, ex);
        }
    }


    public void downloadCGMSSettings() throws PlugInBaseException
    {
        try
        {
            AnimasDexcomDataHandler handler = new AnimasDexcomDataHandler(portName, animasDevice, this, outputWriter);
            handler.startAction(AnimasTransferType.DownloadCGMSSettings);

            AnimasCGMSDeviceData data = (AnimasCGMSDeviceData) handler.getData();

            data.writeSettings(outputWriter);
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException("Exception running downloadCGMSSettings: " + ex, ex);
        }

    }


    public static final void main(String[] args)
    {
        // -- PORT Set here
        String windowsPort = "COM9";
        String linuxPort = "/dev/ttyUSB0";
        // --

        String port = windowsPort;

        if (System.getProperty("os.name").toLowerCase().contains("linux"))
        {
            port = linuxPort;
        }

        try
        {
            AnimasCGMSDeviceReader adr = new AnimasCGMSDeviceReader(port, AnimasDeviceType.Animas_Vibe, new ConsoleOutputWriter());
            adr.downloadCGMSData();
        }
        catch (Exception ex)
        {

            System.out.println("Error running AnimasDeviceReader: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


}