package ggc.plugin.comm.ports;

import java.util.List;

/**
 * Created by andy on 19.04.15.
 */
public interface CommunicationPortDiscovery
{

    String getKey();


    public List<String> getAllPossiblePorts();

}
