package ggc.plugin.device;

import ggc.plugin.data.enums.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceHandler
{


    /**
     * This is method for reading data from device.
     *
     * @throws PlugInBaseException
     */
    void readDeviceData(DeviceDefinition definition, //
                               Object connectionParameters, //
                               OutputWriter outputWriter) throws PlugInBaseException;

    /**
     * This is method for reading configuration of device.
     *
     * @throws PlugInBaseException
     */
    void readConfiguration(DeviceDefinition definition, //
                                  Object connectionParameters, //
                                  OutputWriter outputWriter) throws PlugInBaseException;


}
