package ggc.pump.device.insulet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import ggc.pump.defs.device.PumpDeviceHandler;

/**
 * Created by andy on 22.05.15.
 */
public class InsuletHandler extends PumpDeviceHandler
{

    Map<DownloadSupportType, List<GGCPlugInFileReaderContext>> fileContexts = new HashMap<DownloadSupportType, List<GGCPlugInFileReaderContext>>();


    public InsuletHandler()
    {
        List<GGCPlugInFileReaderContext> ctxes = new ArrayList<GGCPlugInFileReaderContext>();
        ctxes.add(new FRC_InsuletOmnipod(DownloadSupportType.DownloadConfigFile));
        fileContexts.put(DownloadSupportType.DownloadConfigFile, ctxes);

        ctxes = new ArrayList<GGCPlugInFileReaderContext>();
        ctxes.add(new FRC_InsuletOmnipod(DownloadSupportType.DownloadDataFile));
        fileContexts.put(DownloadSupportType.DownloadDataFile, ctxes);
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.InsuletOmnipodHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {

    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {

    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return this.fileContexts.get(downloadSupportType);
    }
}
