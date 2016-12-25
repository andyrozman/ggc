package ggc.plugin.comm.ports.discovery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public class EmptyDiscoveryAgent extends PortDiscoveryAgentAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(EmptyDiscoveryAgent.class);


    public EmptyDiscoveryAgent(DeviceConnectionProtocol connectionProtocol)
    {
        super(connectionProtocol);
        LOG.debug("Using EmptyDiscoveryAgent for type: {}", connectionProtocol);
    }


    public List<DevicePortDto> getAllPossiblePorts()
    {
        return null;
    }


    public String getSelectProtocolString()
    {
        return "";
    }


    public String getPortDeviceName()
    {
        return "COMMUNICATION_PORT";
    }


    @Override
    public boolean isEmptyDiscoveryAgent()
    {
        return true;
    }

}
