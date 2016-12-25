package ggc.plugin.comm.ports.discovery;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public class SerialDiscoveryAgent extends PortDiscoveryAgentAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(SerialDiscoveryAgent.class);


    public SerialDiscoveryAgent()
    {
        super(DeviceConnectionProtocol.Serial_USBBridge);
    }


    public SerialDiscoveryAgent(DeviceConnectionProtocol protocol)
    {
        super(protocol);
    }


    public List<DevicePortDto> getAllPossiblePorts() throws PlugInBaseException
    {
        try
        {
            Set<String> availablePorts = NRSerialCommunicationHandler.getAvailablePorts();

            logFoundPorts(LOG, availablePorts);

            return getListOfDevicePorts(availablePorts);
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorCommunicationWithProtocolHandler,
                    new Object[] { "NRSerialCommunicationHandler", ex.getMessage() }, ex);
        }

    }


    public String getSelectProtocolString()
    {
        return "SELECT_SERIAL_PORT";
    }


    public String getPortDeviceName()
    {
        return "SERIAL_PORT";
    }

}
