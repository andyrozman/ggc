package ggc.plugin.comm.ports.discovery;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.IBMCommunicationHandler;
import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public class BluetoothSerialDiscoveryAgent extends SerialDiscoveryAgent
{

    private static final Logger LOG = LoggerFactory.getLogger(BluetoothSerialDiscoveryAgent.class);


    public BluetoothSerialDiscoveryAgent()
    {
        super(DeviceConnectionProtocol.BlueTooth_Serial);
    }


    public List<DevicePortDto> getAllPossiblePorts() throws PlugInBaseException
    {
        try
        {
            Set<String> availablePorts = IBMCommunicationHandler.getAllAvailablePortsString();

            logFoundPorts(LOG, availablePorts);

            return getListOfDevicePorts(availablePorts);
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorCommunicationWithProtocolHandler,
                    new Object[] { "IBMCommunicationHandler", ex.getMessage() }, ex);
        }
    }

}
