package ggc.pump.device.minimed;

import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

public abstract class MinimedComm_Base extends SerialProtocol implements MinimedComm_Interface
{

    
    public MinimedComm_Base()
    {
        super();
    }
    
    
    
    
    
    public abstract void prepareCommunicationDevice();
    
    public abstract boolean hasEncodingDecoding();
    
    public abstract String[] getCommunicationSettings();
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }

    
    
    
    
    
    
}
