package ggc.pump.device.animas.impl;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.pump.device.animas.impl.handler.AnimasBaseDataHandler;
import ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.pump.util.DataAccessPump;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;


import java.util.List;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     AnimasPumpDeviceReader
 *  Description:  Animas Pump Device Reader
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasPumpDeviceReader extends AnimasDeviceReader
{
    public static final Log LOG = LogFactory.getLog(AnimasPumpDeviceReader.class);


    public AnimasPumpDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(portName, animasDevice, outputWriter);

        AnimasUtils.setDataAccessInstance(DataAccessPump.getInstance());
    }


    public void downloadPumpData() throws PlugInBaseException
    {
        AnimasBaseDataHandler handler = new AnimasBaseDataHandler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadPumpData);
    }


    public void downloadPumpSettings() throws PlugInBaseException
    {
        AnimasBaseDataHandler handler = new AnimasBaseDataHandler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadPumpSettings);

        AnimasPumpDeviceData data = (AnimasPumpDeviceData) handler.getData();

        data.writeSettings(outputWriter);
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
            AnimasPumpDeviceReader adr = new AnimasPumpDeviceReader(port, AnimasDeviceType.Animas_Vibe, new ConsoleOutputWriter());
            adr.downloadPumpData();
        }
        catch (Exception ex)
        {

            System.out.println("Error running AnimasDeviceReader: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


}
