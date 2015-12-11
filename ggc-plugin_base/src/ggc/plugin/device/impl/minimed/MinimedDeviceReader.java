package ggc.plugin.device.impl.minimed;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.AbstractDeviceReader;
import gnu.io.CommPortIdentifier;

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
 *  Filename:     MinimedDeviceReader
 *  Description:  Minimed Device Reader abstract class.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedDeviceReader extends AbstractDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedDeviceReader.class);

    protected MinimedDeviceType minimedDevice;
    protected MinimedConnectionParametersDTO connectionParametersDTO;

    protected MinimedTargetType deviceTargetType = null;
    protected MinimedDeviceHandlerInterface handler;


    public MinimedDeviceReader(MinimedDeviceHandlerInterface handler, String communicationParameters,
            MinimedDeviceType minimedDevice, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(outputWriter, false);

        this.minimedDevice = minimedDevice;
        this.handler = handler;
        this.deviceTargetType = handler.getDeviceTargetType();

        MinimedUtil.setDeviceType(minimedDevice);

        connectionParametersDTO = new MinimedConnectionParametersDTO(communicationParameters);

        MinimedUtil.setConnectionParameters(connectionParametersDTO);

        System.out.println("connectionParametersDTO.interfaceType: " + connectionParametersDTO.interfaceType);

        // FIXME extend
        if (connectionParametersDTO.interfaceType == MinimedCommInterfaceType.CareLinkUSB)
        {
            @SuppressWarnings("rawtypes")
            Enumeration ports = CommPortIdentifier.getPortIdentifiers();
            // Set<String> portList = NRSerialPort.getAvailableSerialPorts();
            // ???
            StringBuilder sb = new StringBuilder();

            boolean deviceFound = false;

            while (ports.hasMoreElements())
            {
                CommPortIdentifier comp = (CommPortIdentifier) ports.nextElement();

                if (comp.getName().equals(connectionParametersDTO.portName))
                {
                    deviceFound = true;
                }

                sb.append(comp.getName() + " ");
            }

            LOG.debug(String.format("Serial Ports found: %s, configured port (%s) found: %s", sb.toString(),
                connectionParametersDTO.portName, deviceFound));

            if (!deviceFound)
            {
                throw new PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort,
                        new Object[] { connectionParametersDTO.portName });
            }
        }
        else
        {
            throw new PlugInBaseException(PlugInExceptionType.UnsupportedDevice);
        }

        MinimedUtil.setMinimedDeviceReader(this);
    }


    @Override
    public void readData() throws PlugInBaseException
    {
        this.refreshConverters();

        MinimedDataHandler dataHandler = new MinimedDataHandler(this.handler.getDataAccessInstance(),
                connectionParametersDTO, minimedDevice, this, outputWriter);
        dataHandler.startAction(this.handler.getDeviceTargetType().getDownloadData());
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        this.refreshConverters();

        MinimedDataHandler dataHandler = new MinimedDataHandler(this.handler.getDataAccessInstance(),
                connectionParametersDTO, minimedDevice, this, outputWriter);
        dataHandler.startAction(this.handler.getDeviceTargetType().getDownloadSettings());
    }


    public void customInitAndChecks() throws PlugInBaseException
    {
    }


    public void configureProgressReporter()
    {
        // this.configureProgressReporter();
    }


    public void refreshConverters()
    {
        MinimedUtil.setOutputWriter(this.outputWriter);
        MinimedUtil.setDeviceType(this.minimedDevice);

        MinimedUtil.refreshConverters(getDeviceTargetType());
    }


    /**
     * Device Target Type (either Pump or CGMS)
     * 
     * @return MinimedTargetType instance
     */
    public MinimedTargetType getDeviceTargetType()
    {
        return this.deviceTargetType;
    }

}
