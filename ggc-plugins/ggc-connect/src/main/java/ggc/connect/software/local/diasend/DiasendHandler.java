package ggc.connect.software.local.diasend;

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

public class DiasendHandler extends ConnectHandler
{

    DiasendReader reader;


    public DiasendHandler()
    {
    }


    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.DiaSendHandler;
    }


    public FileFilter[] getFileFilters(ConnectHandlerConfiguration config)
    {
        FileFilter[] filters = new FileFilter[1];

        filters[0] = createFileFilter("xls", "DIASEND_XSL_FILE_DESCRIPTION");

        return filters;

    }


    @Override
    public Map<ConnectDataType, List<String>> downloadSummary(ConnectHandlerParameters parameters)
            throws PlugInBaseException
    {
        if (reader == null)
            reader = new DiasendReader();

        return reader.createSummaryInformation(parameters);
    }


    @Override
    public void downloadData(ConnectHandlerParameters parameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        if (reader == null)
            reader = new DiasendReader();

        reader.readData(parameters, outputWriter);
    }


    @Override
    public boolean hasSimpleConfiguration()
    {
        return false;
    }

}
