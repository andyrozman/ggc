package ggc.cgms.device;

import java.util.List;

import ggc.cgms.data.defs.CGMSDeviceDefinition;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandlerAbstract;

/**
 * Created by andy on 15.04.15.
 */
public abstract class CGMSDeviceHandler extends DeviceHandlerAbstract
{

    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    protected String getCommunicationPort(Object connectionParameters)
    {
        return (String) connectionParameters;
    }


    protected CGMSDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (CGMSDeviceDefinition) definition;
    }

}
