package ggc.pump.device.minimed.data;

public class Command extends MMPCommand
{

    @Override
    public void execute() throws Exception
    {
        /*
         * boolean flag = false;
         * int i = 0;
         * do
         * try
         * {
         * MedicalDevice.logInfoLow(this, "execute: cmd=" + m_description +
         * ", m_maxRetries=" + m_maxRetries + ", attempts=" + i);
         * getCommunicationsLink().setTotalReadByteCountExpected(m_totalBytesToRead
         * );
         * try
         * {
         * getCommunicationsLink().execute(this);
         * }
         * catch(BadDeviceValueException baddevicevalueexception)
         * {
         * baddevicevalueexception.printStackTrace();
         * }
         * flag = true;
         * }
         * catch(BadDeviceCommException baddevicecommexception)
         * {
         * if(++i <= m_maxRetries)
         * {
         * MedicalDevice.logWarning(this, "execute: cmd failed with exception: "
         * + baddevicecommexception + "; retrying (attempts = " + (i + 1) +
         * ")");
         * setState(7);
         * try
         * {
         * getCommunicationsLink().initCommunications();
         * }
         * catch(IOException ioexception) { }
         * } else
         * {
         * MedicalDevice.logError(this, "cmd " +
         * MedicalDevice.Util.getHex(m_commandCode) + " (" + m_description +
         * ") failed after " + i + " attempts" + "; exception = " +
         * baddevicecommexception);
         * throw new BadDeviceCommException("execute: cmd " +
         * MedicalDevice.Util.getHex(m_commandCode) + " (" + m_description +
         * ") failed after " + i + " attempts",
         * baddevicecommexception.getErrorCode(),
         * baddevicecommexception.getErrorCodeDescription());
         * }
         * }
         * while(!flag && i <= m_maxRetries && !isHaltRequested());
         */
    }

    public Command(int i, String s)
    {
        super(i, s, 64, 1, 0);
    }

    public Command(int command_code, String description, int ai[], int j)
    {
        super(command_code, description, 0, 1, 11);
        m_commandParameters = ai;
        m_commandParameterCount = j;
    }

    // Command(int command_code, String description, int rec_length, int
    // max_records, int command_type)
    // Command(int command_code, String description, int rec_length, int
    // max_records, int address, int address_length, int command_type)

    public Command(int command_code, String description, int rec_length, int max_records, int command_type)
    // Command(int i, String s, int j, int k, int l)
    {
        super(command_code, description, rec_length, max_records, command_type);
    }

    public Command(int i, String s, int j)
    {
        super(i, s, 0, 1, 11);
        m_commandParameterCount = j;
        int k = j / 64 + 1;
        m_commandParameters = new int[64 * k];
    }

    public Command()
    {
        super(0, "(empty command)", 64, 1, 0);
    }
}
