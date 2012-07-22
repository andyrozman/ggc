package ggc.pump.device.minimed.data;

import java.util.Date;

public abstract class MMPCommand implements Cloneable
{

    private String m_description;

    void allocateRawData()
    {
        m_rawData = new int[m_bytesPerRecord * m_maxRecords];
    }

    public Object clone()
    {
        /*
         * FIXME: Problem Checkin
        MMPCommand command = null;
        try
        {
            command = (MMPCommand) super.clone();
            command.m_packet = MedicalDevice.Util.clone(m_packet);
            command.m_commandParameters = MedicalDevice.Util.clone(m_commandParameters);
            command.m_rawData = MedicalDevice.Util.clone(m_rawData);
            if (m_beginDate != null)
                command.m_beginDate = (Date) m_beginDate.clone();
            if (m_endDate != null)
                command.m_endDate = (Date) m_endDate.clone();
        }
        catch (CloneNotSupportedException clonenotsupportedexception)
        {
            Contract.unreachable("Class not cloneable");
        }
        return command;*/
        
        return null;
    }

    public String toString()
    {
        /* FIXME: Problem Checkin
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(MedicalDevice.Util.getHex(m_commandCode));
        stringbuffer.append(" (" + m_description);
        for (int i = 0; i < m_commandParameterCount; i++)
            stringbuffer.append(" p" + i + "=" + m_commandParameters[i]);

        stringbuffer.append(") ");
        return stringbuffer.toString();
        */
        
        return null;
    }

    public abstract void execute() throws Exception;

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

    public boolean isUseMultiXmitMode()
    {
        return m_useMultiXmitMode;
    }

    public int getEffectTime()
    {
        return m_effectTime;
    }

    void setEffectTime(int i)
    {
        m_effectTime = i;
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
    int m_effectTime;

    public MMPCommand(int command_code, String description, int rec_length, int max_records, int command_type)
    {
        this(command_code, description, rec_length, max_records, 0, 0, command_type);
        m_dataOffset = 0;
        m_cmdLength = 2;
        setUseMultiXmitMode(false);
    }

    public MMPCommand(int command_code, String description, int rec_length, int max_records, int address, int address_length,
            int command_type)
    {
        // FIXME: Problem Checkin
        //super(description);
        m_numBytesRead = 0;
        m_extraObject = null;
        m_effectTime = 0;
        m_commandCode = command_code;
        m_bytesPerRecord = rec_length;
        m_maxRecords = max_records;
        allocateRawData();
        m_address = address;
        m_addressLength = address_length;
        m_dataOffset = 2;
        if (address_length == 1)
            m_cmdLength = 2 + address_length;
        else
            m_cmdLength = 2 + address_length + 1;
        m_packet = new int[0];
        m_commandType = command_type;
        m_commandParameterCount = 0;
        m_commandParameters = new int[64];
        setUseMultiXmitMode(false);
        m_maxRetries = 2;
    }
}
