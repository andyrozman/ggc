package ggc.plugin.device.impl.animas;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.protocol.reader.SerialDeviceReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     AnimasDeviceReader
 *  Description:  This in main class for reading data from device. Each plugin must extend
 *       this class, to call correct methods.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasDeviceReader extends SerialDeviceReader
{
    public static final Log LOG = LogFactory.getLog(AnimasDeviceReader.class);

    protected AnimasDeviceType animasDevice;


    public AnimasDeviceReader(String portName, AnimasDeviceType animasDevice, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(portName, outputWriter);
        this.animasDevice = animasDevice;

        if (animasDevice.getImplementationType() != AnimasImplementationType.AnimasImplementationV2)
        {
            throw new PlugInBaseException(PlugInExceptionType.UnsupportedDevice, new Object[] { this.getClass().getSimpleName()  });
        }
    }


    /**
     * This are custom checks if everything there is (or everything set is). When not exception must be thrown.
     *
     * @throws PlugInBaseException
     */
    public void customInitAndChecks() throws PlugInBaseException
    {
    }


    /**
     * Here we configure progress reporter. We can either call method configureProgressReporter() or
     * we can directly change progressData instance.
     */
    public void configureProgressReporter()
    {
    }

}
