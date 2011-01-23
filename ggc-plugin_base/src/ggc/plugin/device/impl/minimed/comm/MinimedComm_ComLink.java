package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;
import ggc.plugin.device.impl.minimed.MinimedDevicesIds;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommandResponse;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.SerialPortEvent;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CRCUtils;


// NOTE: This is only class that will be implemented for first phase of MiniMed testing

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     MinimedComm_ComLink  
 *  Description:  Communication Protocol: COM Link
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedComm_ComLink extends SerialProtocol implements MinimedComm_Interface //extends MinimedComm_Base
{

    //private String[] comm_settings = {};
    private static Log log = LogFactory.getLog(MinimedComm_ComLink.class);

    private static int COMLINK_ENCODING_PROTOCOL[] = {
                                                  21, 49, 50, 35, 52, 37, 38, 22, 26, 25, 
                                                  42, 11, 44, 13, 14, 28
                                              };    
    MinimedDeviceUtil util = MinimedDeviceUtil.getInstance();
    CRCUtils hex_utils = new CRCUtils();
    //DataAccessPump m_da = DataAccessPump.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    
    MinimedCommandResponse mcr;
    
    
    /**
     * Constructor
     * @param da 
     */
    public MinimedComm_ComLink(DataAccessPlugInBase da)
    {
        super(da);
        
        //super(port, serial_number);
        mcr = new MinimedCommandResponse();
    }
    
    
    
    public int[] encode(int[] input)
    {
        log.debug("Here");
        int ai1[] = new int[input.length * 3];
        //t i = 0;
        int cnt = 0;
        
        //log.info( "encodeDC: about to encode bytes = <" + MedicalDevice.Util.getHexCompact(ai) + ">");
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
            	ai2[cnt++] = this.hex_utils.getByteAsInt(ai1[i], ai1[i + 1]);
            else
            	ai2[cnt++] = this.hex_utils.getByteAsInt(ai1[i], 5);
            //Contract.post(l1 >= 0 && l1 <= 255, "value of " + l1 + " at index " + j2 + " is out of expected range 0.." + 255);
            //ai2[cnt++] = l1;
        }

        return ai2;
    }
    
    public int[] decode(int[] input)
    {
        log.debug("Here");
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
                    int k2 = this.hex_utils.getByteAsInt(l, i1);
                    ai1[j1++] = k2;
                    j = 0;
                }
                k = 0;
                i = 0;
            }

        }

        //log.info( "decodeDC: decoded bytes = <" + MedicalDevice.Util.getHexCompact(ai1) + ">");
        return ai1;
    
        //return null;
    }

    
    private int decode(int input)
    {
        log.debug("Here");
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

        log.error("Can't find value of " + this.hex_utils.getCorrectHexValue(input) +  " in decode table. ");
        
        return 0;
        
    }
    
    
    
    
    
    
    public void serialEvent(SerialPortEvent event)  { }

    
    


   

    

    public int closeCommunicationInterface() throws PlugInBaseException
    {
        super.close();
        // not used for RS232 communication
        return 0;
    }

    
    
    public int closeDevice() throws PlugInBaseException
    {
        log.debug("Here");
        
        if (util.isCommandAvailable(MinimedCommand.COMMAND_CANCEL_SUSPEND))
        {
            
            if (util.firmware_version != null && (util.firmware_version.startsWith("VER 1.6") || util.firmware_version.startsWith("VER 1.7")))
            {
                executeCommand(MinimedCommand.COMMAND_KEYPAD_PUSH_ACK);
                util.sleepMs(500);
                executeCommand(MinimedCommand.COMMAND_KEYPAD_PUSH_ESC);
                util.sleepMs(500);
            }

            executeCommand(MinimedCommand.COMMAND_CANCEL_SUSPEND);
        }
            
            
        try
        {
            executeCommand(MinimedCommand.COMMAND_SET_RF_POWER_OFF);
        }
        catch(PlugInBaseException ex)
        {
            log.debug("shutDownPump: ignoring error: " + ex);
        }

        return 0;
    }

    
    
    public int initializeCommunicationInterface() throws PlugInBaseException
    {
        log.debug("Here");
        
        //setState(2);
        boolean flag = false;
        for(int i = 0; i <= 4 && !flag;)
        {
            try
            {
                initCommunicationsIO();
                flag = true;
                break;
            }
            catch(IOException ex)
            {
                log.warn("Got error on init. Ex.: " + ex);

                if(i == 4)
                {
                    // FIXME
                    throw new PlugInBaseException(ex);
                }
                
                log.warn("initCommunications: retrying...");
                i++;
            }
        }
        //setState(1);
        
        
        
        
        // TODO Auto-generated method stub
        return 0;
    }

    
    private void initCommunicationsIO() throws PlugInBaseException, IOException //, SerialIOHaltedException
    {
        log.debug("Here");
        log.info("initCommunicationsIO: waking up ComLink1.");
        createSerialPort();
        log.debug("initCommunicationsIO: before read until empty");
        readUntilEmpty();
        log.debug("initCommunicationsIO: before send command check reply");
        sendCommandCheckReply((byte)6, (byte)51);
        try
        {
            log.debug("initCommunicationsIO: before status");
            int i = readStatus();
            
            
            System.out.println("Status: " + i);
            if(i > 0)
            {
                log.warn("initCommunicationsIO: dumping " + i + " bytes.");
                this.write((byte)8);
                //sendTransferDataCommand();
                int ai[] = new int[i];
                read(ai);
                
                System.out.println("ai: " + ai);
                
                readAckByte();
            }
        }
        //catch(BadDeviceCommException baddevicecommexception) { }
        catch(IOException ex) 
        {
            // FIXME
            throw new PlugInBaseException(ex);
        }
    }
    
    
    private void createSerialPort() throws PlugInBaseException
    {
//        beginSerialPort(i);
        //int j = 0;
        //boolean flag = false;
        
        log.debug("Create Serial Port");
        
        log.debug("Here");
        
        for(int i=0; i<4; i++)
        {
            try
            {
                this.setPort(util.port); 
                this.setCommunicationSettings(util.comm_baudrate, util.comm_data_bits, 
                    util.comm_stop_bits, util.comm_parity, util.comm_flowcontrol, 0);
                open();
                //setCommPort(new SerialPort(i, MMPump.m_baudRate));
                this.serialPort.enableReceiveTimeout(500);
//                super.portInputStream.
                
                //flag = true;
                break;
            }
            catch(PlugInBaseException ex)
            {
                if (i==3) //++j >= 0)
                    throw ex;
                log.info("createSerialPort: waiting for port to become available...e=" + ex);
                util.sleepMs(5000);
            }
            catch (UnsupportedCommOperationException ex)
            {
                if (i==3)
                    throw new PlugInBaseException(ex);
                log.info("createSerialPort: waiting for port to become available...e=" + ex);
                util.sleepMs(5000);
            }
        }
        
        System.out.println("Cakam");
        
        util.sleepMs(20000);
        
        
        
        //while(!flag);
        
        
        
        System.out.println("Init: "  + readUntilEmpty());
        util.sleepIO();
    }
    
    
    private int readStatus() throws PlugInBaseException, IOException
    {
        log.debug("Here");
    	
        int status = sendCommandGetReply((byte)2); //ASCII_STX);
        int m_numDataBytes = this.portInputStream.available(); 
        	//read();
        readAckByte();
        log.debug("readStatus: CS status follows: NumberReceivedDataBytes=" + m_numDataBytes + ", ReceivedData=" + isStatusReceivedData(status) + ", RS232Mode=" + isStatusRS232Mode(status) + ", FilterRepeat=" + isStatusFilterRepeat(status) + ", AutoSleep=" + isStatusAutoSleep(status) + ", StatusError=" + isStatusError(status) + ", SelfTestError=" + isStatusSelfTestError(status));

        
        if(isStatusError(status))
            throw new PlugInBaseException("readStatus: ComLink1 has STATUS ERROR");
        if(isStatusSelfTestError(status))
            throw new PlugInBaseException("readStatus: ComLink1 has SELFTEST ERROR");
        else
            return isStatusReceivedData(status) ? m_numDataBytes : 0;
    }
    
    private boolean readAckByte() throws PlugInBaseException, IOException
    {
        log.debug("Here");
        
        while(this.portInputStream.available()>0)
        {
        	System.out.println("Wait for data: ");
        }
        
        int[] rdata = new int[2];
        rdata[0] = read();
        rdata[1] = read();
        
        
        /*
        byte[] rdata = new byte[2];
        rdata[0] = this.portInputStream.read();
        rdata[1] = read();
        */
        
        System.out.println("Read Ack Byte: RData " + rdata[0] + ", " + rdata[1]);
        
        if (rdata[0] != 102) 
        {
            log.debug("No return reply. We received: <" + hex_utils.getHex(rdata) + ">" );
            //throw Exception;
            return false;
        }

        
        if (rdata[1] != 85)
        {
            log.debug("Incorrect ACK package. We expected " + hex_utils.getHex((byte)85) + " (we got " + hex_utils.getHex(rdata[1]) + ")" );
            // throw Exception
            return false;
        }
        
        return true;
    }
    
    
    
    private int sendCommandGetReply(byte cmd_byte) throws IOException
    {
        this.write(cmd_byte);
        this.write("\r".getBytes());
        
        
        this.util.sleepIO();
        return read();
    }
    
    
    private void sendCommand(MinimedCommand command) throws PlugInBaseException, IOException, Exception
    {
        // FIXME
        
        log.info("sendCommand: SENDING " + command.getFullCommandDescription() + " for pump #" + util.getSerialNumber());
        
        try
        {
            
            int cmd[] = util.createCommandByte(command);
            write(cmd);
            log.info( "sendCommand: reading link device ACK & (optional) RDY byte.");
            boolean res = readAckByte();
            
            if (!res)
                return;
            
            if ((command.command_code == 93) && (command.command_parameters_count != 0) && (command.command_parameters[0] == 1))
            {
                int i = this.serialPort.getReceiveTimeout();
                this.serialPort.enableReceiveTimeout(17000);
                readReadyByte(command.command_parameters_count > 0);
                this.serialPort.enableReceiveTimeout(i);
            } 
            else
            {
                readReadyByte(command.command_parameters_count > 0);
            }
            
            
        }
        catch(IOException ex)
        {
            throw new PlugInBaseException("sendCommand: ERROR - an IOException  has occurred processing " + command.getFullCommandDescription()  + ". Exception = " + ex);
        }
    }
    
    
    private void readReadyByte(boolean flag) throws IOException, PlugInBaseException
    {
        boolean flag1 = false;
        for(int i = 0; i <= 1 && !flag1;)
        {
            try
            {
                readReadyByteIO(flag);
                flag1 = true;
                continue;
            }
            catch(IOException ioexception)
            {
                // FIXME 
                if (util.protocol_id==MinimedDevice.INTERFACE_PARADIGM_LINK_COM)  // if(m_linkDeviceType == 15)
                {
                    util.interface_paradigmlink_delay = Math.min(util.interface_paradigmlink_delay + 5, 150);
                    log.debug("readReadyByte: increasing paradigm_link delay to " + util.interface_paradigmlink_delay);
                }
                if(i == 1)
                    throw ioexception;
                i++;
            }
        }
    }

    
    private void readReadyByteIO(boolean send_byte) throws PlugInBaseException, IOException
    {
        int i = 0;

        if (send_byte)
        {
            util.sleepParadigmLink();
            this.write((byte)7);
            //sendCommand((byte)7);
            util.sleepParadigmLink();
            readAckByte();
        } 
        else
        {
            util.sleepParadigmLink();
        }
        
        i = read();

        //flag1 = i == 51;
        
        if (i != 51)
            throw new IOException("readReadyByteIO: reply " + hex_utils.getHex(i) + " does not match expected READY reply of " + hex_utils.getHex((byte)51));
        else
            return;
    }
    
    
    
/*    
    private void readAckByte() throws IOException, SerialIOHaltedException
    {
        int i = 0;
        boolean flag = false;
        boolean flag1 = false;
        for(int j = 0; j <= 1 && !flag; j++)
        {
            i = read();
            flag = i == 85;
            if(i == 102)
                flag1 = true;
        }
    
        if(!flag)
        {
            getRS232Port().dumpSerialStatus();
            if(flag1)
                throw new IOException("readAckByte: reply " + MedicalDevice.Util.getHex((byte)102) + " (NAK) does not match expected ACK reply of " + MedicalDevice.Util.getHex((byte)85));
            else
                throw new IOException("readAckByte: reply " + MedicalDevice.Util.getHex(i) + " does not match expected ACK reply of " + MedicalDevice.Util.getHex((byte)85));
        } 
        else
        {
            return;
        }
    }
  */  
    
    
    private boolean isStatusError(int status)
    {
        return (status & 0x10) != 0;
    }
    
    private boolean isStatusSelfTestError(int status)
    {
        return (status & 8) != 0;
    }
    
    private boolean isStatusReceivedData(int status)
    {
        return (status & 1) != 0;
    }
    
    
    private boolean isStatusFilterRepeat(int status)
    {
        return (status & 0x40) != 0;
    }
    
    private boolean isStatusAutoSleep(int status)
    {
        return (status & 0x20) != 0;
    }
    
    
    private boolean isStatusRS232Mode(int status)
    {
        return (status & 4) != 0;
    }
    
    
    private void sendCommandCheckReply(byte cmd_byte, byte expected_return) throws IOException, PlugInBaseException
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int i = 0;
        //boolean flag = false;
        for(int j = 0; j <= 1 && (i != expected_return); j++)
        {
            i = sendCommandGetReply(cmd_byte);
            //flag = i == byte1;
        }
    
        if (i != expected_return)
        {
            //dumpInterfaceStatus();
            //throw new IOException("SendCommand: command " + hex_utils.getHex(cmd_byte) + " reply " + hex_utils.getHex(i) + " does not match expected command " + hex_utils.getHex(expected_return));
        } 
        else
        {
            return;
        }
    }
    
    public void dumpInterfaceStatus()
    {
    }
    
    
    
    private int readUntilEmpty()
    {
        int i = 0;
        //Contract.pre(m_serialPort != null, "m_serialPortLocal is null.");
        int j;
        try
        {
            while((j = portInputStream.available() /*m_serialPortLocal.rxReadyCount()*/) > 0) 
            {
            	//System.out.println("")
                byte abyte0[] = new byte[j];
                util.sleepIO();
                i += portInputStream.read(abyte0) /*m_serialPortLocal.getData(abyte0)*/;
            }
        }
        catch(IOException ioexception) { }
        if(i > 0)
            log.info("readUntilEmpty: dumped " + i + (i <= 1 ? " byte." : " bytes."));
        return i;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    public boolean hasEncodingDecodingSupport()
    {
        return true;
    }


    
    // Pump problem with Error Reply:\
    // Invalid return value (%s).
    // Pump is in wrong state for succesful reading.
    

    //  MM_DEVICE_INIT_PROBLEM=%s problem on initialization with %s:\n%s\nException:\n%s
    

    /* (non-Javadoc)
     * @see ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface#initDevice()
     */
    public int initDevice() throws PlugInBaseException
    {
        
        MinimedCommand mdc = null;
        
        log.debug("initDevice - Start");
        
        //setPhase(4);
        //getCommunicationsLink().resetTotalReadByteCount();
        
        
        //---
        //--- Command: Set RF Power On
        //---
        executeCommand(MinimedCommand.COMMAND_SET_RF_POWER_ON);

        
        //---
        //--- Command: Read Pump Error Status
        //---

        //int error_code = -1;
        int param_int = -1;
        
        try
        {
            mdc = util.getCommand(MinimedCommand.COMMAND_READ_PUMP_ERROR_STATUS);
            executeCommand(mdc);
            
            param_int = util.getCommand(MinimedCommand.COMMAND_READ_PUMP_ERROR_STATUS).raw_data[0];
            
            if (param_int > 100)
                param_int -= 100;
        }
        catch(PlugInBaseException ex)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), m_ic.getMessage("MM_PUMP_ERROR_REPLY"), m_ic.getMessage("MM_ERROR_READING_DEVICE"));
            throw new PlugInBaseException(f, ex);
        }
        
        checkCorrectReply(0, 67, 0, param_int, mdc.raw_data, m_ic.getMessage("MM_PUMP_ERROR_REPLY"));
        
        
        //---
        //--- Command: Read Pump State
        //---

        param_int = -1;
        
        try
        {
            mdc = util.getCommand(MinimedCommand.COMMAND_READ_PUMP_STATE);
            executeCommand(mdc);
            
            param_int = mdc.raw_data[0];
        }
        catch(PlugInBaseException ex)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), m_ic.getMessage("MM_PUMP_STATE_REPLY"), m_ic.getMessage("MM_ERROR_READING_DEVICE"));
            throw new PlugInBaseException(f, ex);
        }
        
        
        checkCorrectReply(0, 3,  util.getNormalPumpState(), param_int, mdc.raw_data, m_ic.getMessage("MM_PUMP_STATE_REPLY"));
        
        
        
        
        // Command: Read Pump Error Status

        //---
        //--- Command: Read Pump Temporary Basal
        //---
        try
        {
            mdc = util.getCommand(MinimedCommand.COMMAND_READ_TEMPORARY_BASAL);
            executeCommand(mdc);
            
            param_int = mcr.getUnsignedShort(mdc, MinimedCommandResponse.TBR_DURATION);
        }
        catch(PlugInBaseException ex)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), m_ic.getMessage("MM_PUMP_DELIVERY_TBR"), m_ic.getMessage("MM_ERROR_READING_DEVICE"));
            throw new PlugInBaseException(f, ex);
        }
        
        if(param_int != 0)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), m_ic.getMessage("MM_PUMP_DELIVERY_TBR"), m_ic.getMessage("MM_PUMP_DELIVERING_TBR"));
            log.error(f);
            throw new PlugInBaseException(f);
        } 
        
        
        //---
        //--- Command: Read Pump Temporary Basal
        //---
        
        boolean flag = false;
        try
        {
            flag = detectActiveBolus();
        }
        catch(PlugInBaseException ex)
        {
            throw ex;
            //throw new ConnectToPumpException("Bad Pump Active (bolus) Reply", 3, MedicalDevice.Util.makeString(m_cmdDetectBolus.m_rawData));
        }
        
        if (flag)
        {
            throw new PlugInBaseException("Pump Active (bolus): " + util.getNAKDescription(12));
        } 
        
        
        
        
            
        if (util.getDeviceType()==MinimedDevicesIds.PUMP_MINIMED_511)
        {
            try
            {
                mdc = util.getCommand(MinimedCommand.COMMAND_READ_FIRMWARE_VERSION);
                executeCommand(mdc);
                
                util.firmware_version = mcr.getString(mdc); 
            }
            catch(PlugInBaseException ex)
            {
                log.error("Error getting firmware version. Ex.: " + ex, ex);
            }
        }
        
        log.debug("init Device --- FINISHED");
        return 0;
        
    }
    
    
    private void checkCorrectReply(int min, int max, int required, int returned, int[] raw_data, String error_desc) throws PlugInBaseException
    {
        
        if ((returned<min) || (returned > max))
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), error_desc, m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), String.format(m_ic.getMessage("MM_INVALID_RETURN_VALUE"), returned, min, max));
            log.error(f);
            log.error("Raw Data:\n" + hex_utils.getHex(raw_data));
            throw new PlugInBaseException(f);
        }
        
        if (returned!=required)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), error_desc, m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), String.format(m_ic.getMessage("MM_WRONG_STATE"), returned, required));
            log.error(f);
            throw new PlugInBaseException(f);
        }
        
    }
    
    
    
    private boolean detectActiveBolus() throws PlugInBaseException
    {
        try
        {
            executeCommand(MinimedCommand.COMMAND_DETECT_BOLUS);
        }
        catch(PlugInBaseException ex)
        {
            if (ex.error_code>0)
            {
                if (ex.error_code==12)
                    return true;
                else
                    throw ex;
            }
            else
                throw ex;
            
        }
        
        return false;
    }
    
    
    
    
    
    // sendTransferDataCommand
    //sendCommand((byte)8);

    int m_state = 0;
    
    
    /**
     * Execute Command
     * 
     * @param command_id
     * @throws PlugInBaseException
     */
    public void executeCommand(int command_id) throws PlugInBaseException
    {
        executeCommand(util.getCommand(command_id));
    }
    
    
    /**
     * Execute Command
     * 
     * @param command
     * @throws PlugInBaseException
     */
    public void executeCommand(MinimedCommand command) throws PlugInBaseException
    {
        if (command.has_only_sub_commands)
        {
            command.execute();
        }
        else
        {
            // main command
            if(command.command_parameters_count > 0)
            {
                MinimedCommand devicecommand = command.createCommandPacket();
                executeCommand(devicecommand);
            }
            
            //MedicalDevice.m_lastCommandDescription = m_deviceCommand.m_description;
            command.prepareForReading();
            sendAndRead(command);
            
            if (command.has_sub_commands)
                command.execute();
        }
        
        
    }
    
    
    private void sendAndRead(MinimedCommand command) throws PlugInBaseException
    {
        if (m_state != 7)
            m_state = 4;
        
        try
        {
            sendCommand(command);
        }
        catch(Exception ex)
        {
            log.error("Exception on sending command: " + ex, ex);
            throw new PlugInBaseException(ex);
        }
        
        
        if ((command.raw_data.length > 0) && (!util.isHaltRequested()))
        {
            if (command.raw_data.length > 64)
            {
                command.raw_data = readDeviceDataPage(command); //command.raw_data.length);
            } 
            else
            {
                m_state = 5;
                command.raw_data = readDeviceData(command);
                //incTotalReadByteCount(m_deviceCommand.m_rawData.length);
                //notifyDeviceUpdateProgress();
            }
        } 
        else
        {
            try
            {
                checkAck(command);
            }
            catch(Exception ex)
            {
                throw new PlugInBaseException("sendAndRead: ERROR - problem checking ACK; exception = " + ex);
            }
        }
        
        if (m_state == 7)
            m_state = 4;
    }
    
    
    private int[] readDeviceDataPage(MinimedCommand command) throws PlugInBaseException
    {
        
        int i = command.raw_data.length;
        //int ai[] = new int[70];
        int ai2[] = new int[0];
        int j = 0;
        int k = 1;
        boolean flag = false;
        log.debug("readDeviceDataPage: processing " + command.getFullCommandDescription());
        do
        {
            m_state = 6;
            int ai1[] = readDeviceData(command);
            if(ai1.length == 0)
            {
                flag = true;
            } 
            else
            {
                ai2 = hex_utils.concatIntArrays(ai2, ai1);
                log.debug("readDeviceDataPage: just read packet " + j + ", bytes = " + ai1.length + ", total bytes = " + ai2.length);
                //incTotalReadByteCount(ai1.length);
                j++;
                //notifyDeviceUpdateProgress();
                boolean flag1 = (command.data_count & 0x80) == 128;
                int l = command.data_count & 0x7f;
                if(l != k)
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - sequence number mismatch); expected " + k + ", read " + l);
                if(++k > 127)
                    k = 1;
                flag = ai2.length >= i || util.isHaltRequested() || flag1;
                try
                {
                    if ((!flag) && (!util.isHaltRequested()))
                    {
                        m_state = 4;
                        sendAck();
                    }
                }
                catch(IOException ex)
                {
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - problem sending ACK; exception = " + ex);
                }
            }
        } while(!flag);
        log.debug("readDeviceDataPage: " + command.getFullCommandDescription() + " returned " + ai2.length + " bytes.");
        return ai2;
    }

    
    private void sendAck() throws PlugInBaseException, IOException
    {
        log.debug("sendAck: sending cmd " + hex_utils.getHex((byte)6));
        
        write(util.createCommandByte(MinimedCommand.COMMAND_ACK));
        
        readAckByte();
        readReadyByte(false);
    }
    
    
    
    private int[] readDeviceData(MinimedCommand command) throws PlugInBaseException
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int ai1[] = new int[0];
        int ai[];
        try
        {
            int i = readStatus();
            this.write((byte)8);
            ai = new int[i];
            read(ai);
            readAckByte();
        }
        catch(IOException ex)
        {
            throw new PlugInBaseException("readDeviceData: ERROR - an IOException  has occurred processing " + command.getFullCommandDescription() +  ". Exception = " + ex);
        }
        
        int ai2[] = decode(ai);
        
        checkHeaderAndCRC(command, ai2);
        
        //boolean flag = false;
        if(ai2[4] == 21)
        {
            int k = ai2[5];
            if(k == 13)
                log.debug("readDeviceData: NAK received = no more data.");
            else
                throw new PlugInBaseException("readDeviceData: " + command.getFullCommandDescription() + " failed; error code = <" + hex_utils.getHex(k) + ">" + "(" + util.getNAKDescription(k) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2) + ">)", k);
            
        } 
        else
        {
            if(ai2[4] != command.command_code)
                throw new PlugInBaseException("readDeviceData: " + command.getFullCommandDescription()  + " has bad command code value of " + hex_utils.getHex(ai2[4]) + " (expected " + hex_utils.getHex(command.command_code) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2) + ">)");
            command.data_count = ai2[5];
            int j = ai2.length - 6 - 1;
            ai1 = new int[j];
            System.arraycopy(ai2, 6, ai1, 0, j);
            log.debug("readDeviceData: decoded packet = <" + hex_utils.getHex(ai1) + ">");
        }
        return ai1;
    }
    

    
    /**
     * Check Header and CRC data
     */
    private boolean checkHeaderAndCRC(MinimedCommand command, int arr_in[]) throws PlugInBaseException
    {
        int crc_rec = arr_in[arr_in.length - 1];
        int crc_calc = hex_utils.computeCRC8(arr_in, 0, (arr_in.length - 1)) & 0xff;
        
        if(crc_rec != crc_calc)
        {
            log.debug("checkHeaderAndCRC: " + command.getFullCommandDescription() + " resulted in bad CRC value of " + crc_rec + " (expected " + crc_calc + ") " + "(byte data = " + "<" + hex_utils.getHex(arr_in) + ">)");
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription() + " resulted in bad CRC value of " + crc_rec + " (expected " + crc_calc + ") " + "(byte data = " + "<" + hex_utils.getHex(arr_in) + ">)");
        }
        
        if(arr_in[0] != 167)
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription() + " resulted in bad Type Code value of " + hex_utils.getHex(arr_in[0]));
        
        //int ai1[] = packSerialNumber();
        if(this.util.serial_number_bcd[0] != arr_in[1] || this.util.serial_number_bcd[1] != arr_in[2] || this.util.serial_number_bcd[2] != arr_in[3])
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription() + " resulted in bad serial number value of <" + hex_utils.getHex(arr_in[1]) + " " + hex_utils.getHex(arr_in[2]) + " " + hex_utils.getHex(arr_in[3]) + ">");
        else
            return true;
    }
    
    
    
    private void checkAck(MinimedCommand command) throws PlugInBaseException, IOException
    {
        log.info( "checkAck: retrieving pump ACK packet...");
        int i = readStatus();
        this.write((byte)8);
        int ai[] = new int[i];
        read(ai);
        int ai1[] = decode(ai);
        readAckByte();
        checkHeaderAndCRC(command, ai1);
        if(ai1[4] != 6)
        {
            int j = ai1[5];
            throw new PlugInBaseException("checkAck: " + command.getFullCommandDescription() + " failed; error code = <" + hex_utils.getHex(j) + ">" + "(" + util.getNAKDescription(j) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai1) + ">)");
        } else
        {
            log.info( "checkAck: GOOD pump ACK reply received.");
            return;
        }
    }



    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }



    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public int getDownloadSupportType()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public boolean hasIndeterminateProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public boolean isFileDownloadSupported()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public boolean isReadableDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }



    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    //sendTransferDataCommand();    
    //this.write((byte)8);
    
    
    
}
