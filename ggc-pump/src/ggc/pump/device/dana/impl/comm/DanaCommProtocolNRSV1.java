package ggc.pump.device.dana.impl.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.AbstractDeviceReader;

/**
 * Created by andy on 11.03.15.
 */

/**
 * Communication Protsol as used by Dana_III_R with NRJavaSerial
 */

public class DanaCommProtocolNRSV1 extends DanaCommProtocolV1
{

    private static final Logger LOG = LoggerFactory.getLogger(DanaCommProtocolNRSV1.class);


    public DanaCommProtocolNRSV1(OutputWriter outputWriter, AbstractDeviceReader reader, String portName)
    {
        super(outputWriter, reader, portName);
    }


    @Override
    protected void createCommunicationHandler() throws Exception
    {
        this.commHandler = new NRSerialCommunicationHandler(this.portName, getSerialSettings());
    }

}
