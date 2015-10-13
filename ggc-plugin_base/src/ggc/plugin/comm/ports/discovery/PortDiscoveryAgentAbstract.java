package ggc.plugin.comm.ports.discovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import ggc.core.util.DataAccess;
import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public abstract class PortDiscoveryAgentAbstract implements PortDiscoveryAgentInterface
{

    DeviceConnectionProtocol connectionProtocol;


    public PortDiscoveryAgentAbstract(DeviceConnectionProtocol connectionProtocol)
    {
        this.connectionProtocol = connectionProtocol;
    }


    public DeviceConnectionProtocol getType()
    {
        return connectionProtocol;
    }


    public boolean isEmptyDiscoveryAgent()
    {
        return false;
    }


    public List<DevicePortDto> getListOfDevicePorts(Collection<String> listOfString)
    {
        List<DevicePortDto> listPorts = new ArrayList<DevicePortDto>();

        for (String port : listOfString)
        {
            listPorts.add(new DevicePortDto(port));
        }

        return listPorts;
    }


    public void logFoundPorts(Logger log, Collection<?> availablePorts)
    {
        String ports = DataAccess.getInstance().createStringRepresentationOfCollection(availablePorts, ", ");
        log.debug("   Found: " + ports);
    }

}
