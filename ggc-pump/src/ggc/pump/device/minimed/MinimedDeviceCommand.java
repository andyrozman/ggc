package ggc.pump.device.minimed;

import java.util.Date;

import com.atech.utils.ByteUtils;
import com.atech.utils.HexUtils;

public abstract class MinimedDeviceCommand implements Cloneable
{
    static HexUtils hex_utils = new HexUtils();
    

    void allocateRawData()
    {
        m_rawData = new int[m_bytesPerRecord * m_maxRecords];
    }

    public Object clone()
    {
        MinimedDeviceCommand command = null;
        try
        {
            command = (MinimedDeviceCommand)super.clone();
            command.m_packet = ByteUtils.cloneIntArray(m_packet);
            command.m_commandParameters = ByteUtils.cloneIntArray(m_commandParameters);
            command.m_rawData = ByteUtils.cloneIntArray(m_rawData);
            
            if(m_beginDate != null)
                command.m_beginDate = (Date)m_beginDate.clone();
            if(m_endDate != null)
                command.m_endDate = (Date)m_endDate.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception)
        {
            //Contract.unreachable("Class not cloneable");
        }
        return command;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(hex_utils.getHex(m_commandCode));
        stringbuffer.append(" (" + m_description);
        for(int i = 0; i < m_commandParameterCount; i++)
            stringbuffer.append(" p" + i + "=" + m_commandParameters[i]);

        stringbuffer.append(") ");
        return stringbuffer.toString();
    }

    abstract void execute() throws Exception;

    void setBeginDate(Date date)
    {
        m_beginDate = date;
    }

    void setEndDate(Date date)
    {
        m_endDate = date;
    }

    void setUseMultiXmitMode(boolean flag)
    {
        m_useMultiXmitMode = flag;
    }

    boolean isUseMultiXmitMode()
    {
        return m_useMultiXmitMode;
    }

    static final byte SOH = 1;
    static final byte BITS_IN_ACCESS_CODE = 6;
    static final byte REPLY_HELLO = 90;
    static final byte REPLY_TIMEOUT = -1;
    static final int SIO_DELAY_MS = 50;
    int m_commandParameters[];
    int m_commandParameterCount;
    int m_dataCount;
    int m_commandCode;
    int m_rawData[];
    int m_dataOffset;
    int m_bytesPerRecord;
    int m_maxRecords;
    int m_address;
    int m_dataPointer;
    int m_cmdLength;
    int m_addressLength;
    int m_commandType;
    int m_packet[];
    int m_numBytesRead;
    int m_maxRetries;
    boolean m_useMultiXmitMode;
    Date m_beginDate;
    Date m_endDate;
    Object m_extraObject;

    
    MinimedDeviceCommand(Object obj, int i, String s, int j, int k, int l)
    {
        this(i, s, j, k, l);

    }
    
    MinimedDeviceCommand(int command_code, String s, int j, int k, int l)
    {
        this(command_code, s, j, k, 0, 0, l);
        m_dataOffset = 0;
        m_cmdLength = 2;
        setUseMultiXmitMode(false);
    }

    MinimedDeviceCommand(Object obj, int command_code, String s, int j, int k, int l, int i1, int j1)
    {
        this(command_code, s, j, k, l, i1, j1);
    }
    
    
    MinimedDeviceCommand(int command_code, String s, int j, int k, int l, int i1, 
            int j1)
    {
        
        m_numBytesRead = 0;
        m_extraObject = null;
        m_commandCode = command_code;
        m_bytesPerRecord = j;
        m_maxRecords = k;
        allocateRawData();
        m_address = l;
        m_addressLength = i1;
        m_dataOffset = 2;
        if(i1 == 1)
            m_cmdLength = 2 + i1;
        else
            m_cmdLength = 2 + i1 + 1;
        m_packet = new int[0];
        m_commandType = j1;
        m_commandParameterCount = 0;
        m_commandParameters = new int[64];
        setUseMultiXmitMode(false);
        m_maxRetries = 2;
    }
    
    
    MinimedDeviceCommand(String s)
    {
        m_description = s;
    }

    
    
    String m_description;
    
    
    
    
}
