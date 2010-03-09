package ggc.pump.device.minimed;

import java.io.IOException;

import minimed.ddms.deviceportreader.BadDeviceCommException;
import minimed.ddms.deviceportreader.BadDeviceValueException;
import minimed.ddms.deviceportreader.ComLink1;
import minimed.ddms.deviceportreader.DeviceCommand;
import minimed.ddms.deviceportreader.MM511;
import minimed.ddms.deviceportreader.MedicalDevice;
import minimed.ddms.deviceportreader.SerialIOHaltedException;
import minimed.util.Contract;
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

    
    
    
    public String toString()
    {
        return m_deviceCommand.toString();
    }

    private void execute()
        throws BadDeviceCommException, BadDeviceValueException, SerialIOHaltedException
    {
        if(m_deviceCommand.m_commandParameterCount > 0)
        {
            DeviceCommand devicecommand = makeCommandPacket();
            devicecommand.execute();
        }
        MedicalDevice.m_lastCommandDescription = m_deviceCommand.m_description;
        allocateRawData();
        sendAndRead();
    }

    private void sendAndRead()
        throws BadDeviceCommException
    {
        if(getState() != 7)
            setState(4);
        sendCommand();
        if(m_deviceCommand.m_rawData.length > 0 && !getDeviceListener().isHaltRequested())
        {
            if(m_deviceCommand.m_rawData.length > 64)
            {
                m_deviceCommand.m_rawData = readDeviceDataPage(m_deviceCommand.m_rawData.length);
            } else
            {
                setState(5);
                m_deviceCommand.m_rawData = readDeviceData();
                incTotalReadByteCount(m_deviceCommand.m_rawData.length);
                notifyDeviceUpdateProgress();
            }
        } else
        {
            try
            {
                checkAck();
            }
            catch(IOException ioexception)
            {
                throw new BadDeviceCommException("sendAndRead: ERROR - problem checking ACK; exception = " + ioexception);
            }
        }
        if(getState() == 7)
            setState(4);
    }

    private void sendCommand()
        throws BadDeviceCommException, SerialIOHaltedException
    {
        Contract.pre(getRS232Port() != null, "serial port is null.");
        Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        MedicalDevice.logInfo(this, "sendCommand: SENDING CMD " + m_deviceCommand + "for pump #" + getDeviceSerialNumber());
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
            ai = MedicalDevice.Util.concat(ai1, ai);
            getRS232Port().write(ai);
            MedicalDevice.logInfo(this, "sendCommand: reading link device ACK & (optional) RDY byte.");
            readAckByte();
            if(m_deviceCommand.m_commandCode == 93 && m_deviceCommand.m_commandParameterCount == 0 && m_deviceCommand.m_commandParameters[0] == 1)
            {
                int i = getRS232Port().getReadTimeOut();
                getRS232Port().setReadTimeOut(17000);
                readReadyByte(ai1[0] == 4);
                getRS232Port().setReadTimeOut(i);
            } else
            {
                readReadyByte(ai1[0] == 4);
            }
        }
        catch(IOException ioexception)
        {
            throw new BadDeviceCommException("sendCommand: ERROR - an IOException  has occurred processing cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + "); exception = " + ioexception);
        }
    }

    private void checkHeaderAndCRC(int ai[])
        throws BadDeviceCommException
    {
        int i = ai.length - 1;
        int j = ai[i];
        int k = MedicalDevice.Util.computeCRC8(ai, 0, i) & 0xff;
        if(j != k)
            throw new BadDeviceCommException("checkHeaderAndCRC: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad CRC value of " + j + " (expected " + k + ") " + "(byte data = " + "<" + MedicalDevice.Util.getHex(ai) + ">)");
        if(ai[0] != 167)
            throw new BadDeviceCommException("checkHeaderAndCRC: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad Type Code value of " + MedicalDevice.Util.getHex(ai[0]));
        int ai1[] = packSerialNumber();
        if(ai1[0] != ai[1] || ai1[1] != ai[2] || ai1[2] != ai[3])
            throw new BadDeviceCommException("checkHeaderAndCRC: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " resulted in bad serial number value of <" + MedicalDevice.Util.getHex(ai[1]) + " " + MedicalDevice.Util.getHex(ai[2]) + " " + MedicalDevice.Util.getHex(ai[3]) + ">");
        else
            return;
    }

    private void allocateRawData()
    {
        m_deviceCommand.m_rawData = new int[m_deviceCommand.m_bytesPerRecord * m_deviceCommand.m_maxRecords];
    }

    private void checkAck()
        throws BadDeviceCommException, IOException, SerialIOHaltedException
    {
        Contract.pre(getRS232Port() != null, "serial port is null.");
        Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        MedicalDevice.logInfo(this, "checkAck: retrieving pump ACK packet...");
        int i = readStatus();
        sendTransferDataCommand();
        int ai[] = new int[i];
        getRS232Port().read(ai);
        int ai1[] = decodeDC(ai);
        readAckByte();
        checkHeaderAndCRC(ai1);
        if(ai1[4] != 6)
        {
            int j = ai1[5];
            throw new BadDeviceCommException("checkAck: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " failed; error code = <" + MedicalDevice.Util.getHex(j) + ">" + "(" + MM511.getNAKDescription(j) + ") " + "(byte data = " + "<" + MedicalDevice.Util.getHex(ai1) + ">)", new Integer(j), MM511.getNAKDescription(j));
        } else
        {
            MedicalDevice.logInfo(this, "checkAck: GOOD pump ACK reply received.");
            return;
        }
    }

    private void sendAck()
        throws IOException, SerialIOHaltedException
    {
        int i = 0;
        Contract.pre(getRS232Port() != null, "serial port is null.");
        Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        MedicalDevice.logInfo(this, "sendAck: sending cmd " + MedicalDevice.Util.getHex((byte)6));
        int ai[] = new int[7];
        ai[i++] = 167;
        int ai1[] = packSerialNumber();
        ai[i++] = ai1[0];
        ai[i++] = ai1[1];
        ai[i++] = ai1[2];
        ai[i++] = 6;
        ai[i++] = 0;
        ai[i++] = MedicalDevice.Util.computeCRC8(ai, 0, i - 1);
        ai = encodeDC(ai);
        int ai2[] = new int[2];
        ai2[0] = 5;
        ai2[1] = ai.length;
        ai = MedicalDevice.Util.concat(ai2, ai);
        getRS232Port().write(ai);
        readAckByte();
        readReadyByte(ai2[0] == 4);
    }

    private int[] readDeviceDataPage(int i)
        throws BadDeviceCommException, SerialIOHaltedException
    {
        int ai[] = new int[70];
        int ai2[] = new int[0];
        int j = 0;
        int k = 1;
        boolean flag = false;
        MedicalDevice.logInfo(this, "readDeviceDataPage: processing cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")");
        do
        {
            setState(6);
            int ai1[] = readDeviceData();
            if(ai1.length == 0)
            {
                flag = true;
            } else
            {
                ai2 = MedicalDevice.Util.concat(ai2, ai1);
                MedicalDevice.logInfo(this, "readDeviceDataPage: just read packet " + j + ", bytes = " + ai1.length + ", total bytes = " + ai2.length);
                incTotalReadByteCount(ai1.length);
                j++;
                notifyDeviceUpdateProgress();
                boolean flag1 = (m_deviceCommand.m_dataCount & 0x80) == 128;
                int l = m_deviceCommand.m_dataCount & 0x7f;
                if(l != k)
                    throw new BadDeviceCommException("readDeviceDataPage: ERROR - sequence number mismatch); expected " + k + ", read " + l);
                if(++k > 127)
                    k = 1;
                flag = ai2.length >= i || getDeviceListener().isHaltRequested() || flag1;
                try
                {
                    if(!flag && !getDeviceListener().isHaltRequested())
                    {
                        setState(4);
                        sendAck();
                    }
                }
                catch(IOException ioexception)
                {
                    throw new BadDeviceCommException("readDeviceDataPage: ERROR - problem sending ACK; exception = " + ioexception);
                }
            }
        } while(!flag);
        MedicalDevice.logInfoLow(this, "readDeviceDataPage: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ") returned " + ai2.length + " bytes.");
        return ai2;
    }

    private int[] readDeviceData()
        throws BadDeviceCommException, SerialIOHaltedException
    {
        Contract.pre(getRS232Port() != null, "serial port is null.");
        Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int ai1[] = new int[0];
        int ai[];
        try
        {
            int i = readStatus();
            sendTransferDataCommand();
            ai = new int[i];
            getRS232Port().read(ai);
            readAckByte();
        }
        catch(IOException ioexception)
        {
            throw new BadDeviceCommException("readDeviceData: ERROR - an IOException  has occurred processing cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + "); exception = " + ioexception);
        }
        int ai2[] = decodeDC(ai);
        checkHeaderAndCRC(ai2);
        boolean flag = false;
        if(ai2[4] == 21)
        {
            int k = ai2[5];
            if(k == 13)
                MedicalDevice.logInfo(this, "readDeviceData: NAK received = no more data.");
            else
                throw new BadDeviceCommException("readDeviceData: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " failed; error code = <" + MedicalDevice.Util.getHex(k) + ">" + "(" + MM511.getNAKDescription(k) + ") " + "(byte data = " + "<" + MedicalDevice.Util.getHex(ai2) + ">)", new Integer(k), MM511.getNAKDescription(k));
        } else
        {
            if(ai2[4] != m_deviceCommand.m_commandCode)
                throw new BadDeviceCommException("readDeviceData: cmd " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + " (" + m_deviceCommand.m_description + ")" + " has bad command code value of " + MedicalDevice.Util.getHex(ai2[4]) + " (expected " + MedicalDevice.Util.getHex(m_deviceCommand.m_commandCode) + ") " + "(byte data = " + "<" + MedicalDevice.Util.getHex(ai2) + ">)");
            m_deviceCommand.m_dataCount = ai2[5];
            int j = ai2.length - 6 - 1;
            ai1 = new int[j];
            System.arraycopy(ai2, 6, ai1, 0, j);
            MedicalDevice.logInfoHigh(this, "readDeviceData: decoded packet = <" + MedicalDevice.Util.getHex(ai1) + ">");
        }
        return ai1;
    }

    private int[] buildPacket()
    {
        int i = 0;
        Contract.pre(m_deviceCommand.m_cmdLength > 0, "m_cmdLength is < 1.");
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
            ai = MedicalDevice.Util.concat(ai2, m_deviceCommand.m_commandParameters);
            ai = MedicalDevice.Util.concat(ai, new int[1]);
            i = ai.length - 1;
        }
        ai[i++] = MedicalDevice.Util.computeCRC8(ai, 0, i - 1);
        m_deviceCommand.m_packet = ai;
        return encodeDC(ai);
    }

    private DeviceCommand makeCommandPacket()
    {
        MM511.Command command = (MM511.Command)m_deviceCommand.clone();
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
        return MedicalDevice.Util.makePackedBCD(getDeviceSerialNumber());
    }

    /*
    private int[] encodeDC(int ai[])
    {
        int ai1[] = new int[ai.length * 3];
        int i = 0;
        MedicalDevice.logInfo(this, "encodeDC: about to encode bytes = <" + MedicalDevice.Util.getHexCompact(ai) + ">");
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

        MedicalDevice.logInfo(this, "decodeDC: decoded bytes = <" + MedicalDevice.Util.getHexCompact(ai1) + ">");
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

        throw new BadDeviceCommException("decodeDC: Can't find value of " + MedicalDevice.Util.getHex(i) + " in table.");
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

    
    
    
    
    
}
