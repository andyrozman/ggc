package ggc.plugin.device.v2;

import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceHandler
{

    /**
     * Device handler key (defined in DeviceHandlerType)
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
     * Get File Download Contexts for specific downloadSupportType
     * 
     * @param downloadSupportType
     * @return
     */
    List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType);

}
