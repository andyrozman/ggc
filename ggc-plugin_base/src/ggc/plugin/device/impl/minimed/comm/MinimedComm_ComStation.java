package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.SerialPortEvent;

// TODO: Auto-generated Javadoc
/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: MinimedComm_ComStation Description: Communication Protocol: COM
 * Station (For 508 and 508c only)
 * 
 * Author: Andy {andy@atech-software.com}
 */


public class MinimedComm_ComStation extends SerialProtocol implements MinimedComm_Interface    //extends SerialProtocol implements MinimedComm_Interface
{

    
    /**
     * Constructor.
     * 
     * @param da
     *            the da
     */
    public MinimedComm_ComStation(DataAccessPlugInBase da)
    {
        super(da);
        //super(port, serial_number);
    }
    
    
    
    /* (non-Javadoc)
     * @see ggc.plugin.protocol.SerialProtocol#serialEvent(gnu.io.SerialPortEvent)
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {
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
    }

    
    
    
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
   // }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#closeCommunicationInterface()
     */
    public int closeCommunicationInterface() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#closeDevice()
     */
    public int closeDevice() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#initializeCommunicationInterface()
     */
    public int initializeCommunicationInterface() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#decode(int[])
     */
    public int[] decode(int[] input)
    {
        return input;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#encode(int[])
     */
    public int[] encode(int[] input)
    {
        return input;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#hasEncodingDecodingSupport()
     */
    public boolean hasEncodingDecodingSupport()
    {
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#dumpInterfaceStatus()
     */
    public void dumpInterfaceStatus()
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#initDevice()
     */
    public int initDevice() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#executeCommand(int)
     */
    public void executeCommand(int commandId) throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#executeCommand(ggc.plugin.device.impl.minimed.cmd.MinimedCommand)
     */
    public void executeCommand(MinimedCommand command) throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.protocol.SerialProtocol#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getComment()
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getConnectionPort()
     */
    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getDeviceClassName()
     */
    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getDeviceId()
     */
    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#getDeviceSpecialComment()
     */
    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getDownloadSupportType()
     */
    public int getDownloadSupportType()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#getFileDownloadTypes()
     */
    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getIconName()
     */
    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getImplementationStatus()
     */
    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getInstructions()
     */
    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getName()
     */
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#hasIndeterminateProgressStatus()
     */
    public boolean hasIndeterminateProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#hasSpecialProgressStatus()
     */
    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#isDeviceCommunicating()
     */
    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#isFileDownloadSupported()
     */
    public boolean isFileDownloadSupported()
    {
        // TODO Auto-generated method stub
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#isReadableDevice()
     */
    public boolean isReadableDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readConfiguration()
     */
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#readDeviceDataFull()
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readDeviceDataPartitial()
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readInfo()
     */
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#getItemId()
     */
    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    
    
    
}
