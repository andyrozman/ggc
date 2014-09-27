package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MinimedCommandHistoryData extends MinimedCommand
{

    private static Log log = LogFactory.getLog(MinimedCommandHistoryData.class);
    MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();

    public void executeSubCommands(int start, int end) throws PlugInBaseException
    {

        boolean flag = false;
        this.reply.clearData();
        // m_rawData = new int[0];

        log.debug("executeSubCommand: from " + start + " to " + end);

        // if(i <= j)
        {
            for (int k = start; k <= end && !flag; k++)
            {
                log.info("executeSubCommands: (" + this.command_description + ") reading page " + k
                        + " (reading up to page " + end + ")");
                flag = readPage(k);
            }

        }
        /*
         * else
         * {
         * for(int l = i; l >= j && !flag; l--)
         * {
         * log.info( (new
         * StringBuilder()).append("execute: (").append(this.command_description
         * )
         * .append(") reading page ").append(l).append(" (reading down to page "
         * ).append(j).append(")").toString());
         * flag = readPage(l);
         * }
         * }
         */

        log.info("executeSubCommands: " + this.command_description + " returned " + this.reply.raw_data.length
                + " bytes");
    }

    private boolean readPage(int i) throws PlugInBaseException
    {
        boolean flag = false;
        int j = 0;
        setupCommandParameters(i);

        historyPage.clearDataForce();

        do
        {
            // j=0;

            try
            {
                utils.getCommunicationInterface().sendCommandReadData(historyPage);
                // m_cmdReadHistoryDataPage.executeSubCommands();
                flag = true;
            }
            catch (PlugInBaseException ex)
            {
                if (++j <= this.allowed_retries)
                {
                    log.warn(new StringBuilder().append("readPage: re-reading history data page ").append(i)
                            .append(" (attempts = ").append(j + 1).append(")").toString());
                }
                else
                {
                    String txt = "page " + i + " failed for " + this.command_description + " after " + j + " attempts";

                    log.error(txt + "; exception = " + ex);
                    throw new PlugInBaseException("readPage: " + txt + ". Ex.: " + ex, ex);
                }
            }
        } while (!flag && j <= this.allowed_retries && !utils.device_stopped);

        int ai[] = historyPage.reply.raw_data;

        boolean flag1 = ai.length == 0 || utils.device_stopped;

        if (ai.length > 0)
        {
            this.reply.raw_data = utils.getHexUtils().concat(this.reply.raw_data, ai);
        }

        return flag1;
    }

    void setupCommandParameters(int i)
    {
        historyPage.command_parameters[0] = i;
    }

    @Override
    public void executeSubCommands() throws PlugInBaseException
    {
        executeSubCommands(0, this.max_records - 1);
    }

    protected MinimedCommand historyPage;

    public MinimedCommandHistoryData()
    {
        this(32);

    }

    public MinimedCommandHistoryData(int i)
    {
        this(128, "Read History Data", 1024, i);
    }

    public MinimedCommandHistoryData(int code, String desc, int bytes_rec, int max_rec)
    {
        super(code, desc, bytes_rec, max_rec, 0);

        // log.debug("MinimedCommandHistoryData");

        int ai[] = new int[64];

        ai[0] = 0;
        this.has_sub_commands = true;
        historyPage = new MinimedCommand(code, new StringBuilder().append(desc).append("(Page)").toString(), ai, 1);
        // m_dautils.gai, 1);
        historyPage.record_length = bytes_rec;
        // m_cmdReadHistoryDataPage.command_parameters =
        // utils.getParamatersArray(1, 0);
        // m_cmdReadHistoryDataPage.command_length = 1;
        historyPage.max_records = 1;
    }

    /*
     * MinimedComm_Interface communication_interface;
     * MinimedDeviceUtil util = MinimedDeviceUtil.getInstance();
     * void execute(int start_page, int end_page) throws PlugInBaseException
     * {
     * //Contract.pre(start_page >= 0, "beginPage must be >= 0: " + start_page);
     * //Contract.pre(end_page >= 0, "endPage must be >= 0: " + end_page);
     * boolean flag = false;
     * this.reply.raw_data = new int[0];
     * if(start_page <= end_page)
     * {
     * for(int k = start_page; k <= end_page && !flag; k++)
     * {
     * log.debug("execute: (" + this.command_description + ") reading page " + k
     * + " (reading up to page " + end_page + ")");
     * flag = readPage(k);
     * }
     * }
     * else
     * {
     * for(int l = start_page; l >= end_page && !flag; l--)
     * {
     * log.debug("execute: (" + this.command_description + ") reading page " + l
     * + " (reading down to page " + end_page + ")");
     * flag = readPage(l);
     * }
     * }
     * log.debug("execute: " + this.command_description + " returned " +
     * this.reply.raw_data.length + " bytes");
     * }
     * private boolean readPage(int i) throws PlugInBaseException
     * {
     * boolean flag = false;
     * int j = 0;
     * setupCommandParameters(i);
     * do
     * try
     * {
     * this.communication_interface.executeCommandRetry(m_cmdReadHistoryDataPage)
     * ;
     * flag = true;
     * // MinimedCommand.saveCommandData(m_cmdReadHistoryDataPage,
     * "../data/temp/" + take_dt + "_" + this.serial_id + "_Page_" + i +
     * ".bin");
     * }
     * catch(PlugInBaseException ex)
     * {
     * log.error("Problem executing command: " + ex, ex);
     * /* if(++j <= m_maxRetries)
     * {
     * log.warn("readPage: re-reading history data page " + i + " (attempts = "
     * + (j + 1) + ")");
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
     */
    /*
     * }
     * while(!flag && j <= this.max_allowed_time && !util.isHaltRequested());
     * int ai[] = m_cmdReadHistoryDataPage.reply.raw_data;
     * boolean flag1 = ai.length == 0 || util.isHaltRequested();
     * if(ai.length > 0)
     * this.reply.raw_data =
     * util.getHexUtils().concatIntArrays(this.reply.raw_data, ai);
     * return flag1;
     * }
     * void setupCommandParameters(int i)
     * {
     * m_cmdReadHistoryDataPage.command_parameters[0] = i;
     * }
     * public void execute()
     * throws PlugInBaseException
     * {
     * //execute(0, this.max_records - 1);
     * execute(0, 10);
     * }
     * MinimedCommand m_cmdReadHistoryDataPage;
     * public MinimedCommandHistoryData(MinimedComm_Interface comm_int)
     * {
     * this(comm_int, 32);
     * }
     * public MinimedCommandHistoryData(MinimedComm_Interface comm_int, int
     * rec_length)
     * {
     * this(comm_int, 128, "Read History Data", rec_length, 1024);
     * }
     * public MinimedCommandHistoryData(MinimedComm_Interface comm_int, int
     * command_code, String description, int rec_length, int max_record)
     * {
     * super(command_code, description);
     * //, 0, null);
     * this.record_length = rec_length;
     * this.max_records = max_records;
     * serial_id = util.getSerialNumber();
     * this.take_dt = ATechDate.getATDateTimeFromGC(new GregorianCalendar(),
     * ATechDate.FORMAT_DATE_AND_TIME_MIN);
     * int ai[] = new int[64];
     * ai[0] = 0;
     * m_cmdReadHistoryDataPage = new MinimedCommand(command_code, description +
     * " (Page)", ai, 1);
     * m_cmdReadHistoryDataPage.record_length = rec_length;
     * m_cmdReadHistoryDataPage.max_records = 1;
     * }
     * String serial_id;
     * long take_dt;
     */
}
