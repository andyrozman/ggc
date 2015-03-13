package ggc.pump.device.animas;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandler;
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


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AnimasV2PumpHandler;
    }



    public void readDeviceData(DeviceDefinition definition, //
                               Object connectionParameters, //
                               OutputWriter outputWriter) throws PlugInBaseException
    {
        AnimasPumpDeviceReader reader = new AnimasPumpDeviceReader( //
                getCommunicationPort(connectionParameters), //
                this.getAnimasDeviceType(definition), //
                outputWriter);
        reader.readData();

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
            reader.readConfiguration();
        }
    }

    public GGCPlugInFileReaderContext[] getFileDownloadContext(DownloadSupportType downloadSupportType)
    {
        return null;
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
