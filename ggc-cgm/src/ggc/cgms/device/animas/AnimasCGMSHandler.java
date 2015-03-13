package ggc.cgms.device.animas;

import ggc.cgms.data.defs.CGMSDeviceDefinition;
import ggc.cgms.device.animas.impl.AnimasCGMSDeviceReader;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandler;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 04.03.15.
 */
public class AnimasCGMSHandler implements DeviceHandler
{

    public AnimasCGMSHandler()
    {
    }

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AnimasV2CGMSHandler;
    }



    public void readDeviceData(DeviceDefinition definition, //
                               Object connectionParameters, //
                               OutputWriter outputWriter) throws PlugInBaseException
    {
        AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader( //
                getCommunicationPort(connectionParameters), //
                this.getAnimasDeviceType(definition), //
                outputWriter);
        reader.readData();
    }

    public void readConfiguration(DeviceDefinition definition, //
                                  Object connectionParameters, //
                                  OutputWriter outputWriter) throws PlugInBaseException
    {
        AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader( //
                getCommunicationPort(connectionParameters), //
                this.getAnimasDeviceType(definition), //
                outputWriter);
        reader.readConfiguration();
    }


    public GGCPlugInFileReaderContext[] getFileDownloadContext(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    private String getCommunicationPort(Object connectionParameters)
    {
        return (String) connectionParameters;
    }


    private CGMSDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (CGMSDeviceDefinition)definition;
    }


    private AnimasDeviceType getAnimasDeviceType(DeviceDefinition definition)
    {
        return (AnimasDeviceType)getDeviceDefinition(definition).getInternalDefintion();
    }


}
