package ggc.pump.device.accuchek;

import java.util.ArrayList;
import java.util.List;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.accuchek.AccuChekSmartPixSpecialConfig;
import ggc.plugin.device.impl.accuchek.FRC_AccuChekSmartPixXml;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.output.OutputWriter;
import ggc.pump.defs.device.PumpDeviceDefinition;
import ggc.pump.defs.device.PumpDeviceHandler;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 20.10.15.
 */
public class AccuChekPumpHandler extends PumpDeviceHandler
{

    private static AccuChekSmartPixSpecialConfig specialConfigPanel;


    public AccuChekPumpHandler()
    {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AccuChekPumpHandler;
    }


    /**
     * {@inheritDoc}
     */
    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        AccuChekPumpReader reader = new AccuChekPumpReader((PumpDeviceDefinition) definition,
                (String) connectionParameters, outputWriter, DataAccessPump.getInstance());
        reader.readDeviceDataFull();
    }


    /**
     * {@inheritDoc}
     */
    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public void closeDevice() throws PlugInBaseException
    {
        // not used for xml reading
    }


    /**
     * {@inheritDoc}
     */
    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        checkIfDataAccessSet();

        List<GGCPlugInFileReaderContext> fileContexts = new ArrayList<GGCPlugInFileReaderContext>();
        fileContexts.add(new FRC_AccuChekSmartPixXml(dataAccessPump, getReader()));

        return fileContexts;
    }


    private AccuChekPumpReader getReader()
    {
        String connectionString = dataAccessPump.getSelectedDeviceConfigEntry().communication_port_raw;

        DeviceDefinition selectedDeviceDefinition = dataAccessPump.getSelectedDeviceDefinition();
        return new AccuChekPumpReader((PumpDeviceDefinition) selectedDeviceDefinition, connectionString,
                dataAccessPump);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSpecialConfig()
    {
        this.specialConfigKey = "AccuChekSmartPix";
    }


    @Override
    public DeviceSpecialConfigPanelAbstract getSpecialConfigPanel(DeviceInstanceWithHandler deviceInstanceWithHandler)
    {
        if (specialConfigPanel == null)
        {
            specialConfigPanel = new AccuChekSmartPixSpecialConfig(DataAccessPump.getInstance(),
                    deviceInstanceWithHandler);
        }

        return specialConfigPanel;
    }

}
