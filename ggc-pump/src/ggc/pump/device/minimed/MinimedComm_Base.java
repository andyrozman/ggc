package ggc.pump.device.minimed;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.util.DataAccessPump;
import gnu.io.SerialPortEvent;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.CRCUtils;

public abstract class MinimedComm_Base extends SerialProtocol implements MinimedComm_Interface
{

    private static Log log = LogFactory.getLog(MinimedComm_Base.class);
    boolean m_halt_requested = false;
    
    private String serial_number = null;
    private int[] serial_number_bcd = null;
    static CRCUtils hex_utils = new CRCUtils();
    DataAccessPump m_da = DataAccessPump.getInstance();
    
    public MinimedComm_Base()
    {
        super();
    }

    
    public boolean isHaltRequested()
    {
        return m_halt_requested;
    }
    
    
    public void setHaltRequested(boolean req)
    {
        this.m_halt_requested = req;
    }
    
    
    public String getDeviceSerialNumber()
    {
        return serial_number;
    }
    
    public void setDeviceSerialNumber(String serial)
    {
        this.serial_number = serial;
        this.serial_number_bcd = hex_utils.makePackedBCD(getDeviceSerialNumber());
    }
    
    
    
    String m_lastCommandDescription;
    
    
    public abstract void prepareCommunicationDevice();
    
    public abstract boolean hasEncodingDecoding();
    
    public abstract String[] getCommunicationSettings();
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }

    
    MinimedDeviceCommand m_deviceCommand; 
    
    public String toString()
    {
        return m_deviceCommand.toString();
    }

    private void execute() throws PlugInBaseException, Exception
    {
        if(m_deviceCommand.m_commandParameterCount > 0)
        {
            MinimedDeviceCommand devicecommand = makeCommandPacket();
            devicecommand.execute();
        }
        
        m_lastCommandDescription = m_deviceCommand.m_description;
        allocateRawData();
        sendAndRead();
    }
    
    
    
    

    private void sendAndRead() throws PlugInBaseException, IOException, Exception
        
    {
        if(getState() != 7)
            setState(4);
        sendCommand();
        if(m_deviceCommand.m_rawData.length > 0 && !isHaltRequested())
        {
            if(m_deviceCommand.m_rawData.length > 64)
            {
                m_deviceCommand.m_rawData = readDeviceDataPage(m_deviceCommand.m_rawData.length);
            } else
            {
                setState(5);
                m_deviceCommand.m_rawData = readDeviceData();
                //incTotalReadByteCount(m_deviceCommand.m_rawData.length);
                //notifyDeviceUpdateProgress();
            }
        } else
        {
            try
            {
                checkAck();
            }
            catch(IOException ioexception)
            {
                throw new PlugInBaseException("sendAndRead: ERROR - problem checking ACK; exception = " + ioexception);
            }
        }
        if(getState() == 7)
            setState(4);
    }

    private void sendCommand() throws PlugInBaseException, Exception, IOException
    {
//        Contract.pre(this.getCommunicationPort() != null, "serial port is null.");
//        Contract.pre(this.getCommunicationPort().isOpen(), "serial port is not open.");
//        log.info( "sendCommand: SENDING CMD " + m_deviceCommand + "for pump #" + getDeviceSerialNumber());
        log.info("sendCommand: SENDING CMD " + m_deviceCommand + "for pump #" + getDeviceSerialNumber());
        
        try
        {
            int ai[] = buildPacket();
            int ai1[] = new int[2];
            if(m_deviceCommand.isUseMultiXmitMode())
                ai1[0] = 10;
            else
            if(m_deviceCommand.m_commandParameterCount == 0)
                ai1[0] = 5;
            else
                ai1[0] = 4;
            ai1[1] = ai.length;
            ai = hex_utils.concatIntArrays(ai1, ai);
            getCommunicationPort().write(ai);
            log.info( "sendCommand: reading link device ACK & (optional) RDY byte.");
            readAckByte();
            if(m_deviceCommand.m_commandCode == 93 && m_deviceCommand.m_commandParameterCount == 0 && m_deviceCommand.m_commandParameters[0] == 1)
            {
                int i = getCommunicationPort().getReceiveTimeout();
                getCommunicationPort().setReceiveTimeout(17000);
                readReadyByte(ai1[0] == 4);
                getCommunicationPort().setReceiveTimeout(i);
            } else
            {
                readReadyByte(ai1[0] == 4);
            }
            
            
        }
        catch(IOException ioexception)
        {
            throw new PlugInBaseException("sendCommand: ERROR - an IOException  has occurred processing cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + "); exception = " + ioexception);
        }
    }

    private void checkHeaderAndCRC(int ai[]) throws PlugInBaseException
    {
        int i = ai.length - 1;
        int j = ai[i];
        int k = hex_utils.computeCRC8(ai, 0, i) & 0xff;
        if(j != k)
            throw new PlugInBaseException("checkHeaderAndCRC: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad CRC value of " + j + " (expected " + k + ") " + "(byte data = " + "<" + hex_utils.getHex(ai) + ">)");
        if(ai[0] != 167)
            throw new PlugInBaseException("checkHeaderAndCRC: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad Type Code value of " + hex_utils.getHex(ai[0]));
        int ai1[] = packSerialNumber();
        if(ai1[0] != ai[1] || ai1[1] != ai[2] || ai1[2] != ai[3])
            throw new PlugInBaseException("checkHeaderAndCRC: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad serial number value of <" + hex_utils.getHex(ai[1]) + " " + hex_utils.getHex(ai[2]) + " " + hex_utils.getHex(ai[3]) + ">");
        else
            return;
    }

    private void allocateRawData()
    {
        m_deviceCommand.m_rawData = new int[m_deviceCommand.m_bytesPerRecord * m_deviceCommand.m_maxRecords];
    }

    private void checkAck() throws PlugInBaseException, IOException, Exception
    {
//        Contract.pre(this.getCommunicationPort() != null, "serial port is null.");
//        Contract.pre(this.getCommunicationPort().isOpen(), "serial port is not open.");
        log.info( "checkAck: retrieving pump ACK packet...");
        int i = readStatus();
        sendTransferDataCommand();
        int ai[] = new int[i];
        this.getCommunicationPort().read(ai);
        int ai1[] = decode(ai);
        readAckByte();
        checkHeaderAndCRC(ai1);
        if(ai1[4] != 6)
        {
            int j = ai1[5];
            throw new PlugInBaseException("checkAck: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " failed; error code = <" + hex_utils.getHex(j) + ">" + "(" + getNAKDescription(j) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai1) + ">)");
        } else
        {
            log.info( "checkAck: GOOD pump ACK reply received.");
            return;
        }
    }

    private void sendAck() throws PlugInBaseException, IOException
    {
        int i = 0;
//        Contract.pre(this.getCommunicationPort() != null, "serial port is null.");
//        Contract.pre(this.getCommunicationPort().isOpen(), "serial port is not open.");
        log.info( "sendAck: sending cmd " + hex_utils.getHex((byte)6));
        int ai[] = new int[7];
        ai[i++] = 167;
        int ai1[] = packSerialNumber();
        ai[i++] = ai1[0];
        ai[i++] = ai1[1];
        ai[i++] = ai1[2];
        ai[i++] = 6;
        ai[i++] = 0;
        ai[i++] = hex_utils.computeCRC8(ai, 0, i - 1);
        ai = encode(ai);
        int ai2[] = new int[2];
        ai2[0] = 5;
        ai2[1] = ai.length;
        ai = hex_utils.concatIntArrays(ai2, ai);
        this.getCommunicationPort().write(ai);
        readAckByte();
        readReadyByte(ai2[0] == 4);
    }

    
    private void sendTransferDataCommand() throws IOException, Exception
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        log.debug("sendTransferDataCommand: sending cmd.");
        sendCommand((byte)8);
    }
    
    
    private int[] readDeviceDataPage(int i) throws PlugInBaseException, Exception
    {
        int ai[] = new int[70];
        int ai2[] = new int[0];
        int j = 0;
        int k = 1;
        boolean flag = false;
        log.info( "readDeviceDataPage: processing cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")");
        do
        {
            setState(6);
            int ai1[] = readDeviceData();
            if(ai1.length == 0)
            {
                flag = true;
            } else
            {
                ai2 = hex_utils.concatIntArrays(ai2, ai1);
                log.info( "readDeviceDataPage: just read packet " + j + ", bytes = " + ai1.length + ", total bytes = " + ai2.length);
                //incTotalReadByteCount(ai1.length);
                j++;
                //notifyDeviceUpdateProgress();
                boolean flag1 = (m_deviceCommand.m_dataCount & 0x80) == 128;
                int l = m_deviceCommand.m_dataCount & 0x7f;
                if(l != k)
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - sequence number mismatch); expected " + k + ", read " + l);
                if(++k > 127)
                    k = 1;
                flag = ai2.length >= i || isHaltRequested() || flag1;
                try
                {
                    if(!flag && !isHaltRequested())
                    {
                        setState(4);
                        sendAck();
                    }
                }
                catch(IOException ioexception)
                {
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - problem sending ACK; exception = " + ioexception);
                }
            }
        } while(!flag);
        log.debug("readDeviceDataPage: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ") returned " + ai2.length + " bytes.");
        return ai2;
    }

    private int[] readDeviceData() throws PlugInBaseException, Exception
    {
//        Contract.pre(this.getCommunicationPort() != null, "serial port is null.");
//        Contract.pre(this.getCommunicationPort().isOpen(), "serial port is not open.");
        int ai1[] = new int[0];
        int ai[];
        try
        {
            int i = readStatus();
            sendTransferDataCommand();
            ai = new int[i];
            this.getCommunicationPort().read(ai);
            readAckByte();
        }
        catch(IOException ioexception)
        {
            throw new PlugInBaseException("readDeviceData: ERROR - an IOException  has occurred processing cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + "); exception = " + ioexception);
        }
        int ai2[] = decode(ai);
        checkHeaderAndCRC(ai2);
        boolean flag = false;
        if(ai2[4] == 21)
        {
            int k = ai2[5];
            if(k == 13)
                log.info( "readDeviceData: NAK received = no more data.");
            else
                throw new PlugInBaseException("readDeviceData: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " failed; error code = <" + hex_utils.getHex(k) + ">" + "(" + getNAKDescription(k) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2) + ">)");
        } else
        {
            if(ai2[4] != m_deviceCommand.m_commandCode)
                throw new PlugInBaseException("readDeviceData: cmd " + hex_utils.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " has bad command code value of " + hex_utils.getHex(ai2[4]) + " (expected " + hex_utils.getHex(m_deviceCommand.m_commandCode) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2) + ">)");
            
            m_deviceCommand.m_dataCount = ai2[5];
            int j = ai2.length - 6 - 1;
            ai1 = new int[j];
            System.arraycopy(ai2, 6, ai1, 0, j);
            log.info("readDeviceData: decoded packet = <" + hex_utils.getHex(ai1) + ">");
        }
        return ai1;
    }

    private int[] buildPacket()
    {
        int i = 0;
        //Contract.pre(m_deviceCommand.m_cmdLength > 0, "m_cmdLength is < 1.");
        int ai[] = new int[7];
        ai[i++] = 167;
        int ai1[] = packSerialNumber();
        ai[i++] = ai1[0];
        ai[i++] = ai1[1];
        ai[i++] = ai1[2];
        ai[i++] = m_deviceCommand.m_commandCode;
        if(m_deviceCommand.m_commandParameterCount == 0)
        {
            ai[i++] = 0;
        } else
        {
            int ai2[] = new int[i + 1];
            System.arraycopy(ai, 0, ai2, 0, i);
            if(m_sequenceNumber != null)
                ai2[i] = m_sequenceNumber.intValue();
            else
                ai2[i] = m_deviceCommand.m_commandParameterCount;
            ai = hex_utils.concatIntArrays(ai2, m_deviceCommand.m_commandParameters);
            ai = hex_utils.concatIntArrays(ai, new int[1]);
            i = ai.length - 1;
        }
        ai[i++] = hex_utils.computeCRC8(ai, 0, i - 1);
        m_deviceCommand.m_packet = ai;
        
        if (this.hasEncodingDecoding())
            return encode(ai);
        else
            return ai;
        
        //return encodeDC(ai);
    }

    private MinimedDeviceCommand makeCommandPacket()
    {
        MinimedDeviceCommand command = (MinimedDeviceCommand)m_deviceCommand.clone();
        command.m_description = m_deviceCommand.m_description + "-command packet";
        command.m_bytesPerRecord = 0;
        command.m_maxRecords = 0;
        command.m_commandType = 0;
        command.m_commandParameterCount = 0;
        if(m_deviceCommand.m_commandCode == 93 && m_deviceCommand.m_commandParameters[0] == 1)
            command.setUseMultiXmitMode(true);
        return command;
    }

    private int[] packSerialNumber()
    {
//        Contract.pre(getDeviceSerialNumber() != null, "serial number is null");
//        Contract.pre(getDeviceSerialNumber().length() == 6, "serial number must be 6 characters");
        return this.serial_number_bcd;
    }

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

    private static final int TYPE_CODE = 167;
    private static final int CMD_PACKET_LENGTH = 7;
    private static final int DC_ENCODE_WIDTH = 6;
    private static final int DC_DECODE_WIDTH = 4;
    private static final int HEADER_SIZE = 6;
    private static final int MAX_SEQUENCE_NUM = 127;
    private static final int PUMP_EOT = 128;
    private static final int REC_COUNT_NONE = 0;
    private static final int REC_SIZE_MIN = 64;
    private static final int REC_SIZE_NONE = 0;
    private static final int RF_ON_DELAY = 17000;
//    MM511.Command m_deviceCommand;
    private Integer m_sequenceNumber;

/*
    private RS232Command(DeviceCommand devicecommand)
    {
        m_deviceCommand = (MM511.Command)devicecommand;
    }
*/

    
    private void readAckByte() throws PlugInBaseException, IOException
    {
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
        } else
        {
            return;
        }
    }

    private int readStatus() throws PlugInBaseException, IOException
    {
        m_status = sendCommandGetReply((byte)2);
        m_numDataBytes = getCommunicationPort().read();
        readAckByte();
        log.debug("readStatus: CS status follows: NumberReceivedDataBytes=" + m_numDataBytes + ", ReceivedData=" + isStatusReceivedData() + ", RS232Mode=" + isStatusRS232Mode() + ", FilterRepeat=" + isStatusFilterRepeat() + ", AutoSleep=" + isStatusAutoSleep() + ", StatusError=" + isStatusError() + ", SelfTestError=" + isStatusSelfTestError());
        if(isStatusError())
            throw new PlugInBaseException("readStatus: ComLink1 has STATUS ERROR");
        if(isStatusSelfTestError())
            throw new PlugInBaseException("readStatus: ComLink1 has SELFTEST ERROR");
        else
            return isStatusReceivedData() ? m_numDataBytes : 0;
    }
    
    
    
    private void readReadyByteIO(boolean flag) throws PlugInBaseException, IOException
    {
        int i = 0;
        boolean flag1 = false;
        if(flag)
        {
            
            m_da.sleepMs(m_paradigmLinkDelay);
            sendCommand((byte)7);
            m_da.sleepMs(m_paradigmLinkDelay);
            readAckByte();
        } else
        {
            m_da.sleepMs(m_paradigmLinkDelay);
        }
        i = getCommunicationPort().read();
        flag1 = i == 51;
        if(!flag1)
            throw new IOException("readReadyByteIO: reply " + hex_utils.getHex(i) + " does not match expected READY reply of " + hex_utils.getHex((byte)51));
        else
            return;
    }

    private int sendCommandGetReply(byte byte0) throws PlugInBaseException, IOException
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        sendCommand(byte0);
        return getCommunicationPort().read();
    }
    
    private void sendCommandCheckReply(byte byte0, byte byte1)
        throws IOException, PlugInBaseException
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int i = 0;
        boolean flag = false;
        for(int j = 0; j <= 1 && !flag; j++)
        {
            i = sendCommandGetReply(byte0);
            flag = i == byte1;
        }
    
        if(!flag)
        {
            getCommunicationPort().dumpSerialStatus();
            throw new IOException("SendCommand: command " + hex_utils.getHex(byte0) + " reply " + hex_utils.getHex(i) + " does not match expected command " + hex_utils.getHex(byte1));
        } 
        else
        {
            return;
        }
    }
    
    private void sendCommand(byte byte0) throws PlugInBaseException, IOException
    {
        //Contract.pre(getRS232Port() != null, "serial port is null.");
        //Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        getCommunicationPort().write(byte0);
    }

    
    private void readReadyByte(boolean flag)
    throws IOException, PlugInBaseException
{
    boolean flag1 = false;
    for(int i = 0; i <= 1 && !flag1;)
        try
        {
            readReadyByteIO(flag);
            flag1 = true;
            continue;
        }
        catch(IOException ioexception)
        {
            // FIXME 
            if(m_linkDeviceType == 15)
            {
                m_paradigmLinkDelay = Math.min(m_paradigmLinkDelay + 5, 150);
                log.debug("readReadyByte: increasing m_paradigmLinkDelay to " + m_paradigmLinkDelay);
            }
            if(i == 1)
                throw ioexception;
            i++;
        }

}
    
    
    
    
    private boolean isStatusFilterRepeat()
    {
        return (m_status & 0x40) > 0;
    }
    
    private boolean isStatusAutoSleep()
    {
        return (m_status & 0x20) > 0;
    }
    
    private boolean isStatusError()
    {
        return (m_status & 0x10) > 0;
    }
    
    private boolean isStatusSelfTestError()
    {
        return (m_status & 8) > 0;
    }
    
    private boolean isStatusRS232Mode()
    {
        return (m_status & 4) > 0;
    }
    
    private boolean isStatusReceivedData()
    {
        return (m_status & 1) > 0;
    }
    

    public String getNAKDescription(int i)
    {
        String s;
        if(i <= NAK_DESCRIPTIONS_TABLE.length - 1)
            s = NAK_DESCRIPTIONS_TABLE[i];
        else
            s = "UNKNOWN NAK DESCRIPTION";
        return s;
    }
    
    
    private static final String NAK_DESCRIPTIONS_TABLE[] = {
            "UNKNOWN NAK DESCRIPTION", 
            "REQUEST PAUSE FOR 3 SECONDS", 
            "REQUEST PAUSE UNTIL ACK RECEIVED", 
            "CRC ERROR", 
            "REFUSE PROGRAM UPLOAD", 
            "TIMEOUT ERROR", 
            "COUNTER SEQUENCE ERROR", 
            "PUMP IN ERROR STATE", 
            "INCONSISTENT COMMAND REQUEST", 
            "DATA OUT OF RANGE", 
            "DATA CONSISTENCY", 
            "ATTEMPT TO ACTIVATE UNUSED PROFILES", 
            "PUMP DELIVERING BOLUS", 
            "REQUESTED HISTORY BLOCK HAS NO DATA", 
            "HARDWARE FAILURE"
         };

    
    
    int m_status = 0;
    int m_numDataBytes = 0;
    int m_paradigmLinkDelay = 0;
    int m_linkDeviceType = 0;
    int current_state = 0;
    
    public void setState(int state)
    {
        this.current_state = state;
    }
    
    
    public int getState()
    {
        return this.current_state;
    }
    
    
    public abstract int[] decode(int[] input);

    
    public abstract int[] encode(int[] input);
    
    
    public abstract SerialProtocol getCommunicationPort();
    
    
    
    
    
}
