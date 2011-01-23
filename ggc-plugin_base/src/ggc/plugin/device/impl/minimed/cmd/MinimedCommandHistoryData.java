package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface;

import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

// TODO: Auto-generated Javadoc
/**
 * The Class MinimedCommandHistoryData.
 */
public class MinimedCommandHistoryData extends MinimedCommand
{

    private static final long serialVersionUID = -7673488821661770185L;

    /** The log. */
    private static Log log = LogFactory.getLog(MinimedCommandHistoryData.class);
    
    /** The communication_interface. */
    MinimedComm_Interface communication_interface;
    
    /** The util. */
    MinimedDeviceUtil util = MinimedDeviceUtil.getInstance();

        /**
         * Execute.
         * 
         * @param start_page the start_page
         * @param end_page the end_page
         * 
         * @throws PlugInBaseException the plug in base exception
         */
        void execute(int start_page, int end_page) throws PlugInBaseException
        {
            //Contract.pre(start_page >= 0, "beginPage must be >= 0: " + start_page);
            //Contract.pre(end_page >= 0, "endPage must be >= 0: " + end_page);
            boolean flag = false;
            this.raw_data = new int[0];
            if(start_page <= end_page)
            {
                for(int k = start_page; k <= end_page && !flag; k++)
                {
                    log.debug("execute: (" + this.command_description + ") reading page " + k + " (reading up to page " + end_page + ")");
                    flag = readPage(k);
                }

            } 
            else
            {
                for(int l = start_page; l >= end_page && !flag; l--)
                {
                    log.debug("execute: (" + this.command_description + ") reading page " + l + " (reading down to page " + end_page + ")");
                    flag = readPage(l);
                }

            }
            
            log.debug("execute: " + this.command_description + " returned " + this.raw_data.length + " bytes");
        }

        
        
        /**
         * Read page.
         * 
         * @param i the i
         * 
         * @return true, if successful
         * 
         * @throws PlugInBaseException the plug in base exception
         */
        private boolean readPage(int i) throws PlugInBaseException
        {
            boolean flag = false;
            int j = 0;
            setupCommandParameters(i);
            do
                try
                {
                    this.communication_interface.executeCommand(m_cmdReadHistoryDataPage);
                    flag = true;
                    
//                    MinimedCommand.saveCommandData(m_cmdReadHistoryDataPage, "../data/temp/" + take_dt + "_" + this.serial_id + "_Page_" + i + ".bin");
                    
                }
                catch(PlugInBaseException ex)
                {
                    log.error("Problem executing command: " + ex, ex);
/*                    if(++j <= m_maxRetries)
                    {
                        log.warn("readPage: re-reading history data page " + i + " (attempts = " + (j + 1) + ")");
                        try
                        {
                            getCommunicationsLink().initCommunications();
                        }
                        catch(IOException ioexception) { }
                    } else
                    {
                        MedicalDevice.logError(this, "page " + i + " failed for " + m_description + " after " + j + " attempts" + "; exception = " + baddevicecommexception);
                        throw new BadDeviceCommException("readPage: page " + i + " failed for " + m_description + " after " + j + " attempts", baddevicecommexception.getErrorCode(), baddevicecommexception.getErrorCodeDescription());
                    } */
                }
            while(!flag && j <= this.max_allowed_time && !util.isHaltRequested());
            int ai[] = m_cmdReadHistoryDataPage.raw_data;
            boolean flag1 = ai.length == 0 || util.isHaltRequested();
            if(ai.length > 0)
                this.raw_data = util.getHexUtils().concatIntArrays(this.raw_data, ai);
            
            return flag1;
        }
        

        /**
         * Sets the up command parameters.
         * 
         * @param i the new up command parameters
         */
        void setupCommandParameters(int i)
        {
            m_cmdReadHistoryDataPage.command_parameters[0] = i;
        }

        /* (non-Javadoc)
         * @see ggc.plugin.device.impl.minimed.cmd.MinimedCommand#execute()
         */
        public void execute()
            throws PlugInBaseException
        {
            //execute(0, this.max_records - 1);
            execute(0, 10);
        }

        /** The m_cmd read history data page. */
        MinimedCommand m_cmdReadHistoryDataPage;

        
        
        
        
        
        
        /**
         * Instantiates a new minimed command history data.
         * 
         * @param comm_int the comm_int
         */
        public MinimedCommandHistoryData(MinimedComm_Interface comm_int)
        {
            this(comm_int, 32);
        }

        /**
         * Instantiates a new minimed command history data.
         * 
         * @param comm_int the comm_int
         * @param rec_length the rec_length
         */
        public MinimedCommandHistoryData(MinimedComm_Interface comm_int, int rec_length)
        {
            this(comm_int, 128, "Read History Data", rec_length, 1024);
        }

        /**
         * Instantiates a new minimed command history data.
         * 
         * @param comm_int the comm_int
         * @param command_code the command_code
         * @param description the description
         * @param rec_length the rec_length
         * @param max_record the max_record
         */
        public MinimedCommandHistoryData(MinimedComm_Interface comm_int, int command_code, String description, int rec_length, int max_record)
        {
            super(command_code, description, 0, null);
            this.record_length = rec_length;
//            this.max_records = max_records;
            
            serial_id = util.getSerialNumber();
            this.take_dt = ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDate.FORMAT_DATE_AND_TIME_MIN);
            
            int ai[] = new int[64];
            ai[0] = 0;
            m_cmdReadHistoryDataPage = new MinimedCommand(command_code, description + " (Page)", 1, ai);
            m_cmdReadHistoryDataPage.record_length = rec_length;
            m_cmdReadHistoryDataPage.max_records = 1;
        }
    
    
        
        /** The serial_id. */
        String serial_id;
        
        /** The take_dt. */
        long take_dt;
}
