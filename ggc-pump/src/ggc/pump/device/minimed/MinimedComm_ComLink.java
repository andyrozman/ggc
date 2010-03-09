package ggc.pump.device.minimed;

import ggc.core.db.GGCDb;
import gnu.io.SerialPortEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.HexUtils;


// NOTE: This is only class that will be implemented for first phase of MiniMed testing

public class MinimedComm_ComLink extends MinimedComm_Base
{

    private String[] comm_settings = {};
    private static Log log = LogFactory.getLog(GGCDb.class);

    private static int COMLINK_ENCODING_PROTOCOL[] = {
                                                  21, 49, 50, 35, 52, 37, 38, 22, 26, 25, 
                                                  42, 11, 44, 13, 14, 28
                                              };    
    
    HexUtils hex_util = new HexUtils();
    
    public int[] encode(byte[] input)
    {
        int ai1[] = new int[input.length * 3];
        //t i = 0;
        int cnt = 0;
        
        //MedicalDevice.logInfo(this, "encodeDC: about to encode bytes = <" + MedicalDevice.Util.getHexCompact(ai) + ">");
        for(int i = 0; i < input.length; i++)
        {
            //int l = input[j];
            //Contract.pre(l >= 0 && l <= 255, "value of " + l + " at index " + j + " is out of expected range 0.." + 255);
//            int j1 = input[j] >> 4 & 0xf;
//            int k1 = input[j] & 0xf;
            int p1 = MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[(input[i] >> 4 & 0xf)];
            int p2 = MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[(input[i] & 0xf)];
            ai1[cnt++] = p1 >> 2;
            //int l2 = p1 & 3;
            //int i3 = p2 >> 4 & 3;
            ai1[cnt++] = (p1 & 3) << 2 | (p2 >> 4 & 3);
            ai1[cnt++] = p2 & 0xf;
        }

        //int k = 0;
        cnt = 0;
        //int i1 = (int)Math.ceil(((double)input.length * 6.0d) / 4.0d);
        int ai2[] = new int[(int)Math.ceil(((double)input.length * 6.0d) / 4.0d)];
        for(int i = 0; i < ai1.length; i += 2)
        {
            //int l1;
            if(i < ai1.length - 1)
            	ai2[cnt++] = this.hex_util.getByteFromIntsAsInt(ai1[i], ai1[i + 1]);
            else
            	ai2[cnt++] = this.hex_util.getByteFromIntsAsInt(ai1[i], 5);
            //Contract.post(l1 >= 0 && l1 <= 255, "value of " + l1 + " at index " + j2 + " is out of expected range 0.." + 255);
            //ai2[cnt++] = l1;
        }

        return ai2;
    }
    
    public int[] decode(byte[] input)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int j1 = 0;
        int k1 = (int)Math.floor(((double)input.length * 4D) / 6D);
        int ai1[] = new int[k1];
        for(int l1 = 0; l1 < input.length; l1++)
        {
            for(int i2 = 7; i2 >= 0; i2--)
            {
                int j2 = input[l1] >> i2 & 1;
                k = k << 1 | j2;
                if(++i != 6)
                    continue;
                if(++j == 1)
                {
                    l = decode(k);
                } else
                {
                    int i1 = decode(k);
                    int k2 = this.hex_util.getByteFromIntsAsInt(l, i1);
                    ai1[j1++] = k2;
                    j = 0;
                }
                k = 0;
                i = 0;
            }

        }

        //MedicalDevice.logInfo(this, "decodeDC: decoded bytes = <" + MedicalDevice.Util.getHexCompact(ai1) + ">");
        return ai1;
    
        //return null;
    }

    
    public int decode(int input)
    {
        if ((input < 0) || (input > 63))
        {
            log.error("Exception on decoding of " + input + " value. Out of range. ");
            //throw new PumpExceptinBadDeviceCommException("decodeDC: value of " + i + " is out of expected range 0.." + 63);
            return 0;
        }

        for(int j = 0; j < MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL.length; j++)
        {
            if(MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[j] == input)
                return j;
        }

        log.error("Can't find value of " + this.hex_util.getCorrectHexValue(input) +  " in decode table. ");
        
        return 0;
        
    }
    
    
    
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }

    @Override
    public String[] getCommunicationSettings()
    {   
         
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasEncodingDecoding()
    {
        return true;
    }

    @Override
    public void prepareCommunicationDevice()
    {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
