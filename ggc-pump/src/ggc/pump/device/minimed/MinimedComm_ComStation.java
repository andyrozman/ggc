package ggc.pump.device.minimed;

import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

public class MinimedComm_ComStation extends SerialProtocol implements MinimedComm_Interface
{

    
    private static final int DC_ENCODE_TABLE[] = {
                                                  21, 49, 50, 35, 52, 37, 38, 22, 26, 25, 
                                                  42, 11, 44, 13, 14, 28
                                              };    
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {
        // TODO Auto-generated method stub
        
    }

}
