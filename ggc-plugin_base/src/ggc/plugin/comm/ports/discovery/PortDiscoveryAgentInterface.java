package ggc.plugin.comm.ports.discovery;

import java.util.List;

import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 19.04.15.
 */
public interface PortDiscoveryAgentInterface
{

    DeviceConnectionProtocol getType();


    List<DevicePortDto> getAllPossiblePorts() throws PlugInBaseException;


    String getSelectProtocolString();


    String getPortDeviceName();


    /**
     * Is Empty Discovery Agent
     * @return
     */
    boolean isEmptyDiscoveryAgent();

}
