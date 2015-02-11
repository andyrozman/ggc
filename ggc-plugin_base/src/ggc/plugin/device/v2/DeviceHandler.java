package ggc.plugin.device.v2;

import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceHandler
{

    DeviceHandlerType getDeviceHandlerKey();

    /**
     * This is method for reading data from device.
     *
     * @throws ggc.plugin.device.PlugInBaseException
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
