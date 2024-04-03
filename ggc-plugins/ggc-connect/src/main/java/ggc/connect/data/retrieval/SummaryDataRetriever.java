package ggc.connect.data.retrieval;

import java.util.List;
import java.util.Map;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.enums.ConnectDataType;
import ggc.connect.gui.ConnectShowSummaryDialog;
import ggc.plugin.device.PlugInBaseException;

public class SummaryDataRetriever implements Runnable
{

    ConnectShowSummaryDialog dialog;
    // DeviceHandlerType deviceHandlerType;
    // ConnectHandlerConfiguration configuration;
    ConnectHandlerParameters parameters;
    ConnectHandler connectHandler;


    // public SummaryDataRetriever(ConnectShowSummaryDialog dialog, DeviceHandlerType
    // deviceHandlerType,
    // ConnectHandlerConfiguration configuration, ConnectHandlerParameters parameters,
    // ConnectHandler connectHandler)
    // {
    // this.dialog = dialog;
    // this.deviceHandlerType = deviceHandlerType;
    // this.configuration = configuration;
    // this.parameters = parameters;
    // this.connectHandler = connectHandler;
    // }

    public SummaryDataRetriever(ConnectShowSummaryDialog dialog, ConnectHandlerParameters parameters,
            ConnectHandler connectHandler)
    {
        this.dialog = dialog;
        this.parameters = parameters;
        this.connectHandler = connectHandler;
    }


    public void run()
    {
        // try
        // {
        // Thread.sleep(6000);
        // }
        // catch (InterruptedException e)
        // {}

        // retrieve summary data from handler

        try
        {
            Map<ConnectDataType, List<String>> connectDataTypeListMap = connectHandler.downloadSummary(this.parameters);

            this.dialog.setDataRetrieved(connectDataTypeListMap);
        }
        catch (PlugInBaseException e)
        {
            this.dialog.setDataRetrieved(null);
        }

        // List configurationItemsList = Arrays.asList("Config Item 1", "Config Item 2", "Config
        // Item 3", "Config Item 4",
        // "Config Item 5", "Item 6", "Config Item 7", "Config Item 8");
        //
        // Map<ConnectDataType, List<String>> mapItems = new HashMap<ConnectDataType,
        // List<String>>();
        // mapItems.put(ConnectDataType.ConfigurationItems, configurationItemsList);
        // mapItems.put(ConnectDataType.DataItems,
        // Arrays.asList("Import Item 1", "Import Item 2", "Import Item 3", "Import Item 4", "Import
        // Item 5"));
        //
        // mapItems.put(ConnectDataType.UnsupportedItems,
        // Arrays.asList("Import Item 1", "Import Item 2", "Import Item 3"));

        // this.dialog.setDataRetrieved(mapItems);

    }
}
