package ggc.plugin.device.v2;

import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;

/**
 * Created by andy on 22.10.15.
 */
public abstract class DeviceHandlerAbstract implements DeviceHandler
{

    protected String specialConfigKey;


    /**
     * {@inheritDoc}
     */
    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    protected String getCommunicationPort(Object connectionParameters)
    {
        return (String) connectionParameters;
    }


    /**
     * {@inheritDoc}
     */
    // FIXME remove
    public void registerSpecialConfig()
    {
    }


    public String getSpecialConfigKey()
    {
        return specialConfigKey;
    }


    public DeviceSpecialConfigPanelAbstract getSpecialConfigPanel(DeviceInstanceWithHandler deviceInstanceWithHandler)
    {
        return null;
    }

}
