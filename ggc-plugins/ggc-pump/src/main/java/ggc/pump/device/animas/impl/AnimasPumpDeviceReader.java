package ggc.pump.device.animas.impl;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;
import ggc.pump.device.animas.impl.handler.AnimasBaseDataV2Handler;
import ggc.pump.util.DataAccessPump;

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


    public AnimasPumpDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(portName, animasDevice, outputWriter);
        AnimasUtils.setDataAccessInstance(DataAccessPump.getInstance());
    }


    public void readData() throws PlugInBaseException
    {
        AnimasBaseDataV2Handler handler = new AnimasBaseDataV2Handler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadPumpData);
    }


    public void readConfiguration() throws PlugInBaseException
    {
        AnimasBaseDataV2Handler handler = new AnimasBaseDataV2Handler(portName, animasDevice, this, outputWriter);
        handler.startAction(AnimasTransferType.DownloadPumpSettings);
    }

}
