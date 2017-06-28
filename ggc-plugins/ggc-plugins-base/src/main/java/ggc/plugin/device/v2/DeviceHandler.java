package ggc.plugin.device.v2;

import java.util.List;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceHandler
{

    /**
     * Device handler key (defined in DeviceHandlerType)
     *
     * @return DeviceHandlerType enum instance
     */
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


    /**
     * Close device
     * 
     * @throws PlugInBaseException
     */
    void closeDevice() throws PlugInBaseException;


    /**
     * Get File Download Contexts for specific downloadSupportType.
     * 
     * @param downloadSupportType downloadSupportType instance
     *                            
     * @return List of file Reader Contexts for specific downloadSupportType
     */
    List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType);


    /**
     * Register special config. Some devices use special configs. This method puts this special config into
     * registry in DataAccessPlugInBase.
     */
    void registerSpecialConfig();


    /**
     * Get key for special config (if set then its registered)
     *
     * @return
     */
    String getSpecialConfigKey();


    DeviceSpecialConfigPanelAbstract getSpecialConfigPanel(DeviceInstanceWithHandler deviceInstanceWithHandler);


    boolean isEnabled();


    /**
     * getGGCPluginType - get which Plugin uses this handler.
     *
     * @return
     */
    GGCPluginType getGGCPluginType();

}
