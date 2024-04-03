package ggc.pump.device.dana;

import java.io.File;
import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import ggc.pump.defs.device.PumpDeviceHandler;
import ggc.pump.device.dana.impl.DanaDeviceReader;

/**
 * Created by andy on 21.10.15.
 */

// uses DanaCommProtocolNRSJV1
public class DanaPumpHandlerV2 extends PumpDeviceHandler
{

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.DanaPumpHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        DanaDeviceReader reader = new DanaDeviceReader(getCommunicationPort(connectionParameters), outputWriter);
        reader.readData();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        DanaDeviceReader reader = new DanaDeviceReader(getCommunicationPort(connectionParameters), outputWriter);
        reader.readConfiguration();
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    public void closeDevice() throws PlugInBaseException
    {
        // not used - we have internal handling of closing
    }


    public boolean isEnabled()
    {
        File file = new File("../data/tools/DanaOld.cfg");

        return (!file.exists());
    }
}
