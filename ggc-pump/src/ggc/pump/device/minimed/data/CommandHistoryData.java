package ggc.pump.device.minimed.data;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class CommandHistoryData extends Command
{

    /*
     * FIXME: Problem Checkin
     * public void execute(int i, int j)
     * throws BadDeviceCommException
     * {
     * Contract.pre(i >= 0, "beginPage must be >= 0: " + i);
     * Contract.pre(j >= 0, "endPage must be >= 0: " + j);
     * boolean flag = false;
     * m_rawData = new int[0];
     * if(i <= j)
     * {
     * for(int k = i; k <= j && !flag; k++)
     * {
     * MedicalDevice.logInfoLow(this, "execute: (" + m_description +
     * ") reading page " + k + " (reading up to page " + j + ")");
     * flag = readPage(k);
     * }
     * } else
     * {
     * for(int l = i; l >= j && !flag; l--)
     * {
     * MedicalDevice.logInfoLow(this, "execute: (" + m_description +
     * ") reading page " + l + " (reading down to page " + j + ")");
     * flag = readPage(l);
     * }
     * }
     * MedicalDevice.logInfoLow(this, "execute: " + m_description + " returned "
     * + m_rawData.length + " bytes");
     * }
     * private boolean readPage(int i)
     * throws SerialIOHaltedException, BadDeviceCommException
     * {
     * /*
     * boolean flag = false;
     * int j = 0;
     * setupCommandParameters(i);
     * do
     * try
     * {
     * m_cmdReadHistoryDataPage.execute();
     * writeToDisk(i);
     * flag = true;
     * }
     * catch(BadDeviceCommException baddevicecommexception)
     * {
     * if(++j <= m_maxRetries)
     * {
     * MedicalDevice.logWarning(this, "readPage: re-reading history data page "
     * + i + " (attempts = " + (j + 1) + ")");
     * try
     * {
     * getCommunicationsLink().initCommunications();
     * }
     * catch(IOException ioexception) { }
     * } else
     * {
     * MedicalDevice.logError(this, "page " + i + " failed for " + m_description
     * + " after " + j + " attempts" + "; exception = " +
     * baddevicecommexception);
     * throw new BadDeviceCommException("readPage: page " + i + " failed for " +
     * m_description + " after " + j + " attempts",
     * baddevicecommexception.getErrorCode(),
     * baddevicecommexception.getErrorCodeDescription());
     * }
     * }
     * while(!flag && j <= m_maxRetries && !isHaltRequested());
     * int ai[] = m_cmdReadHistoryDataPage.m_rawData;
     * boolean flag1 = ai.length == 0 || isHaltRequested();
     * if(ai.length > 0)
     * m_rawData = MedicalDevice.Util.concat(m_rawData, ai);
     * return flag1;
     */
    /*
     * return false;
     * }
     */

    void setupCommandParameters(int i)
    {
        m_cmdReadHistoryDataPage.m_commandParameters[0] = i;
    }

    /*
     * FIXME: Problem Checkin
     * public void execute()
     * throws BadDeviceCommException
     * {
     * execute(0, m_maxRecords - 1);
     * }
     */

    private void writeToDisk(int page)
    {
        try
        {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream("mm_history_page_" + page + ".data");

            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            // Write object out to disk
            obj_out.writeObject(this);
        }
        catch (Exception ex)
        {
            System.out.println("Error writing data. Ex.:" + ex);
        }

    }

    Command m_cmdReadHistoryDataPage;

    CommandHistoryData()
    {
        this(32);
    }

    CommandHistoryData(int i)
    {
        this(128, "Read History Data", 1024, i);
    }

    CommandHistoryData(int i, String s, int j, int k)
    {
        super(i, s, j, k, 0);
        int ai[] = new int[64];
        ai[0] = 0;
        m_cmdReadHistoryDataPage = new Command(i, s + "(Page)", ai, 1);
        m_cmdReadHistoryDataPage.m_bytesPerRecord = j;
        m_cmdReadHistoryDataPage.m_maxRecords = 1;
    }
}
