package ggc.plugin.protocol.reader;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressData;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import gnu.io.CommPortIdentifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Enumeration;

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
 *  Filename:     SerialDeviceReader
 *  Description:  This is reader for Serial Devices. It makes additional check that selected
 *        device is present.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class SerialDeviceReader extends AbstractDeviceReader
{
    public static final Log LOG = LogFactory.getLog(SerialDeviceReader.class);

    protected String portName;


    public SerialDeviceReader(String portName, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(outputWriter, false);

        this.portName = portName;


        @SuppressWarnings("rawtypes")
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        StringBuilder sb = new StringBuilder();

        boolean deviceFound = false;

        while (ports.hasMoreElements())
        {
            CommPortIdentifier comp = (CommPortIdentifier) ports.nextElement();

            if (comp.getName().equals(this.portName))
            {
                deviceFound = true;
            }

            sb.append(comp.getName() + " ");
        }

        LOG.debug(String.format("Serial Ports found: %s, configured port (%s) found: %s", sb.toString(), portName,
                deviceFound));

        if (!deviceFound)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort,
                    new Object[] { this.portName });
        }

        customInitAndChecks();
        configureProgressReporter();

    }


}
