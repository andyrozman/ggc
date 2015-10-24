package ggc.pump.device.dana;

import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import ggc.pump.defs.device.PumpDeviceHandler;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 21.10.15.
 */
public class DanaPumpHandler extends PumpDeviceHandler
{

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.DanaPumpHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        DanaDiabecare_III_R danaReader = new DanaDiabecare_III_R(getCommunicationPort(connectionParameters),
                outputWriter, DataAccessPump.getInstance());
        danaReader.readDeviceDataFull();
        danaReader.close();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        DanaDiabecare_III_R danaReader = new DanaDiabecare_III_R(getCommunicationPort(connectionParameters),
                outputWriter, DataAccessPump.getInstance());
        danaReader.readConfiguration();
        danaReader.close();
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }
}
