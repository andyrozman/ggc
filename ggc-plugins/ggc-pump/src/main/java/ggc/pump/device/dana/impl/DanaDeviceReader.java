package main.java.ggc.pump.device.dana.impl;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.SerialDeviceReader;
import main.java.ggc.pump.device.dana.impl.comm.DanaCommProtocolNRSV1;

/**
 * Created by andy on 11.03.15.
 */
public class DanaDeviceReader extends SerialDeviceReader
{

    public DanaDeviceReader(String portName, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(portName, outputWriter);
    }


    @Override
    public void readData() throws PlugInBaseException
    {
        DanaCommProtocolNRSV1 commProtocol = new DanaCommProtocolNRSV1(this.outputWriter, this, this.portName);
        commProtocol.readDeviceDataFull();
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        DanaCommProtocolNRSV1 commProtocol = new DanaCommProtocolNRSV1(this.outputWriter, this, this.portName);
        commProtocol.readConfiguration();
    }


    @Override
    public void customInitAndChecks() throws PlugInBaseException
    {

    }


    @Override
    public void configureProgressReporter()
    {

    }
}
