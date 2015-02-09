package ggc.pump.device.animas;

import ggc.plugin.data.enums.DeviceDefinition;
import ggc.plugin.device.DeviceHandler;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.PumpDeviceDefinition;
import ggc.pump.device.animas.impl.AnimasPumpDeviceReader;



public class AnimasIR1200Handler implements DeviceHandler
{

    public AnimasIR1200Handler()
    {
    }


    public void readDeviceData(DeviceDefinition definition, //
                               Object connectionParameters, //
                               OutputWriter outputWriter) throws PlugInBaseException
    {
        //try
        {
            AnimasPumpDeviceReader reader = new AnimasPumpDeviceReader( //
                    getCommunicationPort(connectionParameters), //
                    this.getAnimasDeviceType(definition), //
                    outputWriter);
            reader.downloadPumpData();
        }

    }

    public void readConfiguration(DeviceDefinition definition, //
                                  Object connectionParameters, //
                                  OutputWriter outputWriter) throws PlugInBaseException
    {
        //try
        {
            AnimasPumpDeviceReader reader = new AnimasPumpDeviceReader( //
                    getCommunicationPort(connectionParameters), //
                    this.getAnimasDeviceType(definition), //
                    outputWriter);
            reader.downloadPumpSettings();
        }
    }

    private String getCommunicationPort(Object connectionParameters)
    {
        return (String) connectionParameters;
    }


    private PumpDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (PumpDeviceDefinition)definition;
    }

    private AnimasDeviceType getAnimasDeviceType(DeviceDefinition definition)
    {
        return (AnimasDeviceType)getDeviceDefinition(definition).getInternalDefintion();
    }
}
