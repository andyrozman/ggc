package ggc.connect.software.web.nightscout;

import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.enums.ConnectDataType;
import ggc.connect.enums.ConnectHandlerConfiguration;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 08/03/18.
 */
public class NightScoutHandler extends ConnectHandler
{

    @Override
    public FileFilter[] getFileFilters(ConnectHandlerConfiguration config)
    {
        return new FileFilter[0];
    }


    @Override
    public Map<ConnectDataType, List<String>> downloadSummary(ConnectHandlerParameters parameters)
            throws PlugInBaseException
    {
        return null;
    }


    @Override
    public void downloadData(ConnectHandlerParameters parameters, OutputWriter outputWriter) throws PlugInBaseException
    {

    }


    @Override
    public boolean hasSimpleConfiguration()
    {
        return false;
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return null;
    }
}
