package ggc.pump.device.minimed.cmd;

import java.util.Date;

import com.atech.utils.ByteUtils;
import com.atech.utils.HexUtils;

public abstract class MinimedCommand implements Cloneable
{
    static HexUtils hex_utils = new HexUtils();
    

    void allocateRawData()
    {
        m_rawData = new int[m_bytesPerRecord * m_maxRecords];
    }

    public Object clone()
    {
        MinimedCommand command = null;
        try
        {
            command = (MinimedCommand)super.clone();
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

    abstract public void execute() throws Exception;

    void setBeginDate(Date date)
    {
        m_beginDate = date;
    }

    void setEndDate(Date date)
    {
        m_endDate = date;
    }

    public void setUseMultiXmitMode(boolean flag)
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
    public int m_commandParameters[];
    public int m_commandParameterCount;
    public int m_dataCount;
    public int m_commandCode;
    public int m_rawData[];
    public int m_dataOffset;
    public int m_bytesPerRecord;
    public int m_maxRecords;
    public int m_address;
    public int m_dataPointer;
    public int m_cmdLength;
    public int m_addressLength;
    public int m_commandType;
    public int m_packet[];
    public int m_numBytesRead;
    public int m_maxRetries;
    public boolean m_useMultiXmitMode;
    Date m_beginDate;
    Date m_endDate;
    Object m_extraObject;

    
    MinimedCommand(Object obj, int command_code, String description, int record_length, int k, int l)
    {
        this(command_code, description, record_length, k, l);

    }
    
    MinimedCommand(int command_code, String description, int record_length, int k, int l)
    {
        this(command_code, description, record_length, k, 0, 0, l);
        m_dataOffset = 0;
        m_cmdLength = 2;
        setUseMultiXmitMode(false);
    }

    MinimedCommand(Object obj, int command_code, String description, int record_length, int k, int l, int i1, int j1)
    {
        this(command_code, description, record_length, k, l, i1, j1);
    }
    
    
    MinimedCommand(int command_code, String description, int record_length, int k, int l, int i1, int j1)
    {
        this.m_description = description;
        m_numBytesRead = 0;
        m_extraObject = null;
        m_commandCode = command_code;
        m_bytesPerRecord = record_length;
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
    
    
    MinimedCommand(String s)
    {
        m_description = s;
    }

    
    
    public String m_description;
    
    
    
    
}
