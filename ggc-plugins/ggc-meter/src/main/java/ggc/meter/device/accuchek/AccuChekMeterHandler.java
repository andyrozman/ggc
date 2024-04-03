package ggc.meter.device.accuchek;

import java.util.ArrayList;
import java.util.List;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.defs.device.MeterDeviceHandler;
import ggc.meter.device.accuchek.impl.AccuChekMeterReader;
import ggc.meter.util.DataAccessMeter;
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

public class AccuChekMeterHandler extends MeterDeviceHandler
{

    private static AccuChekSmartPixSpecialConfig specialConfigPanel;


    public AccuChekMeterHandler()
    {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AccuChekMeterHandler;
    }


    /**
     * {@inheritDoc}
     */
    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        AccuChekMeterReader reader = new AccuChekMeterReader((MeterDeviceDefinition) definition,
                (String) connectionParameters, outputWriter, DataAccessMeter.getInstance());
        reader.readDeviceDataFull();
    }


    /**
     * {@inheritDoc}
     */
    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    /**
     * {@inheritDoc}
     */
    public void closeDevice() throws PlugInBaseException
    {
    }


    /**
     * {@inheritDoc}
     */
    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        checkIfDataAccessSet();

        List<GGCPlugInFileReaderContext> fileContexts = new ArrayList<GGCPlugInFileReaderContext>();
        fileContexts.add(new FRC_AccuChekSmartPixXml(dataAccessMeter, getReader()));

        return fileContexts;
    }


    private AccuChekMeterReader getReader()
    {
        String connectionString = dataAccessMeter.getSelectedDeviceConfigEntry().communication_port_raw;

        DeviceDefinition selectedDeviceDefinition = dataAccessMeter.getSelectedDeviceDefinition();
        return new AccuChekMeterReader((MeterDeviceDefinition) selectedDeviceDefinition, connectionString,
                dataAccessMeter);
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
            specialConfigPanel = new AccuChekSmartPixSpecialConfig(dataAccessMeter.getInstance(),
                    deviceInstanceWithHandler);
        }

        return specialConfigPanel;
    }

}
