package ggc.pump.device.animas.impl.handler;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpWriterValues;
import ggc.pump.data.defs.*;
import ggc.pump.data.graph.writer.PumpValuesWriter;


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
 *  Filename:     AnimasPumpDataWriter
 *  Description:  Animas Pump Data Writer
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasPumpDataWriter implements AnimasDataWriter
{
    OutputWriter outputWriter;
    PumpValuesWriter writer;


    public AnimasPumpDataWriter(OutputWriter ow)
    {
        outputWriter = ow;
        this.createDeviceValuesWriter(ow);
        writer = PumpValuesWriter.getInstance(outputWriter);
    }


    public DeviceValuesWriter getDeviceValuesWriter()
    {
        return writer;
    }


    public OutputWriter getOutputWriter()
    {
        return this.outputWriter;
    }

    public void createDeviceValuesWriter(OutputWriter ow)
    {
    }


}
