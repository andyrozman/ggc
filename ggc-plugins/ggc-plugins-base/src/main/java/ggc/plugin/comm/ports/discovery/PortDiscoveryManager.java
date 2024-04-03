package ggc.plugin.comm.ports.discovery;

import java.util.HashMap;
import java.util.Map;

import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public class PortDiscoveryManager
{

    Map<DeviceConnectionProtocol, PortDiscoveryAgentInterface> discoveryAgents;
    private static PortDiscoveryManager portDiscoveryManagerInstance;


    public static PortDiscoveryManager getInstance()
    {
        if (portDiscoveryManagerInstance == null)
        {
            portDiscoveryManagerInstance = new PortDiscoveryManager();
        }

        return portDiscoveryManagerInstance;
    }


    private PortDiscoveryManager()
    {
        discoveryAgents = new HashMap<DeviceConnectionProtocol, PortDiscoveryAgentInterface>();
        registerDiscoveryAgent(new MassStorageDeviceDiscoveryAgent());
        registerDiscoveryAgent(new UsbHidDiscoveryAgent());
        registerDiscoveryAgent(new SerialDiscoveryAgent());
        registerDiscoveryAgent(new BluetoothSerialDiscoveryAgent());
    }


    public void registerDiscoveryAgent(PortDiscoveryAgentInterface discoveryAgent)
    {
        discoveryAgents.put(discoveryAgent.getType(), discoveryAgent);
    }


    public PortDiscoveryAgentInterface getDiscoveryAgent(DeviceConnectionProtocol connectionProtocol)
    {
        if (discoveryAgents.containsKey(connectionProtocol))
        {
            return discoveryAgents.get(connectionProtocol);
        }
        else
        {
            // // TODO remove
            // // this is just temporary so that old code still works
            // if ((connectionProtocol ==
            // DeviceConnectionProtocol.BlueTooth_Serial)
            // || (connectionProtocol ==
            // DeviceConnectionProtocol.Serial_USBBridge))
            // {
            // return null;
            // }

            // if agent is not registered for type we register
            // EmptyDiscoveryAgent
            // for this type
            PortDiscoveryAgentInterface discoveryAgent = new EmptyDiscoveryAgent(connectionProtocol);
            registerDiscoveryAgent(discoveryAgent);

            return discoveryAgent;
        }
    }

}
