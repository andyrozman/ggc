package ggc.pump.device.dana.impl;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.AbstractDeviceReader;

/**
 * Created by andy on 11.03.15.
 */
public class DanaDeviceReader extends AbstractDeviceReader
{

    public DanaDeviceReader(OutputWriter ow) throws PlugInBaseException
    {
        super(ow, true);
    }


    @Override
    public void readData() throws PlugInBaseException
    {

    }

    @Override
    public void readConfiguration() throws PlugInBaseException
    {

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
