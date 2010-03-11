package ggc.pump.device.minimed;

import java.io.IOException;

import ggc.plugin.device.PlugInBaseException;
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

    
    // Here are old methods from minimed stored
    
    
    /*
    private int[] encodeDC(int ai[])
    {
        int ai1[] = new int[ai.length * 3];
        int i = 0;
        log.info( "encodeDC: about to encode bytes = <" + MedicalDevice.Util.getHexCompact(ai) + ">");
        for(int j = 0; j < ai.length; j++)
        {
            int l = ai[j];
            Contract.pre(l >= 0 && l <= 255, "value of " + l + " at index " + j + " is out of expected range 0.." + 255);
            int j1 = l >> 4 & 0xf;
            int k1 = l & 0xf;
            int i2 = ComLink1.DC_ENCODE_TABLE[j1];
            int k2 = ComLink1.DC_ENCODE_TABLE[k1];
            ai1[i++] = i2 >> 2;
            int l2 = i2 & 3;
            int i3 = k2 >> 4 & 3;
            ai1[i++] = l2 << 2 | i3;
            ai1[i++] = k2 & 0xf;
        }

        int k = 0;
        int i1 = (int)Math.ceil(((double)ai.length * 6D) / 4D);
        int ai2[] = new int[i1];
        for(int j2 = 0; j2 < ai1.length; j2 += 2)
        {
            int l1;
            if(j2 < ai1.length - 1)
                l1 = MedicalDevice.Util.makeByte(ai1[j2], ai1[j2 + 1]);
            else
                l1 = MedicalDevice.Util.makeByte(ai1[j2], 5);
            Contract.post(l1 >= 0 && l1 <= 255, "value of " + l1 + " at index " + j2 + " is out of expected range 0.." + 255);
            ai2[k++] = l1;
        }

        return ai2;
    }

    private int[] decodeDC(int ai[])
        throws BadDeviceCommException
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int j1 = 0;
        int k1 = (int)Math.floor(((double)ai.length * 4D) / 6D);
        int ai1[] = new int[k1];
        for(int l1 = 0; l1 < ai.length; l1++)
        {
            for(int i2 = 7; i2 >= 0; i2--)
            {
                int j2 = ai[l1] >> i2 & 1;
                k = k << 1 | j2;
                if(++i != 6)
                    continue;
                if(++j == 1)
                {
                    l = decodeDC(k);
                } else
                {
                    int i1 = decodeDC(k);
                    int k2 = MedicalDevice.Util.makeByte(l, i1);
                    ai1[j1++] = k2;
                    j = 0;
                }
                k = 0;
                i = 0;
            }

        }

        log.info( "decodeDC: decoded bytes = <" + MedicalDevice.Util.getHexCompact(ai1) + ">");
        return ai1;
    }

    private int decodeDC(int i)
        throws BadDeviceCommException
    {
        if(i < 0 || i > 63)
            throw new BadDeviceCommException("decodeDC: value of " + i + " is out of expected range 0.." + 63);
        for(int j = 0; j < ComLink1.DC_ENCODE_TABLE.length; j++)
            if(ComLink1.DC_ENCODE_TABLE[j] == i)
                return j;

        throw new BadDeviceCommException("decodeDC: Can't find value of " + hex_utils.getHex(i) + " in table.");
    }*/

    
    
    
    private void readAckByte() throws PlugInBaseException, IOException
    {
        /*
        int[] rdata = new int[2];
        rdata[0] = getCommunicationPort().read();
        rdata[1] = getCommunicationPort().read();
        
        if (rdata[0] != 102) 
        {
            log.debug("No return reply. We received: <" + hex_utils.getHex(rdata) + ">" );
            return;
        }

        
        if (rdata[1] != 85)
        {
            log.debug("Incorrect ACK package. We expected " + hex_utils.getHex((byte)85) + " (we got " + hex_utils.getHex(rdata[1]) + ")" );
        }
        
        return;
        */
        /*

        int i = 0;
        boolean flag = false;
        boolean flag1 = false;
        
        for(int j = 0; j <= 1 && !flag; j++)
        {
            i = getCommunicationPort().read();
            flag = i == 85;
            if(i == 102)
                flag1 = true;
        }
    
        if(!flag)
        {
            getCommunicationPort().dumpSerialStatus();
            if(flag1)
                throw new IOException("readAckByte: reply " + hex_utils.getHex((byte)102) + " (NAK) does not match expected ACK reply of " + hex_utils.getHex((byte)85));
            else
                throw new IOException("readAckByte: reply " + hex_utils.getHex(i) + " does not match expected ACK reply of " + hex_utils.getHex((byte)85));
        } 
        else
        {
            return;
        }*/
    }
    
    
    
    
}
