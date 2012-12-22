package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     MinimedCommand  
 *  Description:  Minimed Command
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedCommand implements Serializable
{

    private static final long serialVersionUID = -5765155190832642817L;


    private static Log log = LogFactory.getLog(MinimedCommand.class);
    
    
    /**
     * Command: Set RF Power On
     */
    public static final int SET_RF_POWER_ON = 93;

    /**
     * Command: Set RF Power Off
     */
    public static final int SET_RF_POWER_OFF = 400;

    /**
     * Command: Read Pump Error Status
     */
    public static final int READ_PUMP_ERROR_STATUS = 117;

    /**
     * Command: Read Pump State
     */
    public static final int READ_PUMP_STATE = 131;

    /**
     * Command: Read Temporary Basal
     */
    public static final int READ_TEMPORARY_BASAL = 120;

    /**
     * Command: Detect Bolus
     */
    public static final int DETECT_BOLUS = 75;
    
    
    
    /**
     * Command: Cancel Suspend
     */
    public static final int CANCEL_SUSPEND = 77;
    
    /**
     * Command: Read Firmware Version
     */
    public static final int READ_FIRMWARE_VERSION = 116;

    /**
     * Command: Ack
     */
    public static final int COMMAND_ACK = 6;
    
    /**
     * Command: Keypad Push (ACK)
     */
    public static final int COMMAND_KEYPAD_PUSH_ACK = 4401;
    
    /**
     * Command: Keypad Push (ESC)
     */
    public static final int COMMAND_KEYPAD_PUSH_ESC = 4402;
    
    
    public static final int HISTORY_DATA = 4403;
    
    
    /**
     * 
     */
    public static final int SETTINGS = 4404;
    
    
    public static final int SET_SUSPEND = 4405;
    
    
    public static final int GET_SERIAL_NUMBER = 113;
    
    
    public static final int READ_REAL_TIME_CLOCK = 112;
    public static final int READ_PUMP_ID = 113;
    public static final int READ_TODAYS_TOTAL_INSULIN = 121;
    public static final int READ_PROFILES_STD_DATA = 122;
    public static final int READ_PROFILES_A_DATA = 123;
    public static final int READ_PROFILES_B_DATA = 124;
    public static final int READ_BATTERY_STATUS = 114;
    /**
     * 
     */
    public static final int READ_REMAINING_INSULIN = 115;
    
    public static final int READ_REMOTE_CONTROL_IDS = 118;
    public static final int READ_TEMP_BASAL_RATE_BOLUS_DETECTION = 75;
    
    public static final int ENABLE_DETAIL_TRACE = 4409;
    public static final int DISABLE_DETAIL_TRACE = 4410;
    
    public static final int READ_PUMP_TRACE = 4411;
    public static final int READ_DETAIL_TRACE = 4412;
    public static final int READ_NEW_ALARM_TRACE = 4413;
    public static final int READ_OLD_ALARM_TRACE = 4414;
    
    
    
    // 512
    /**
     * 
     */
    public static final int READ_PUMP_MODEL = 141;
    public static final int READ_BG_ALARM_CLOCKS = 142;
    public static final int READ_BG_ALARM_ENABLE = 151;
    public static final int READ_BG_REMINDER_ENABLE = 144;

    public static final int READ_BG_TARGETS = 140; 
    public static final int READ_BG_UNITS = 137;
    public static final int READ_BOLUS_WIZARD_SETUP_STATUS= 135;
    public static final int READ_CARBOHYDRATE_RATIOS = 138;
    public static final int READ_CARBOHYDRATE_UNITS = 136;
    
    public static final int READ_PARADIGMLINK_IDS = 149; 
    public static final int READ_INSULIN_SENSITIVITES = 139;
    public static final int READ_RESERVOIR_WARNING = 143;
    public static final int READ_LANGUAGE = 134;
    public static final int SET_TEMPORARY_BASAL = 76;
    
    
    // 515
    //public static final int READ_BG_TARGETS = 159; 
    public static final int READ_CURRENT_HISTORY_PAGE_NUMBER = 157;
    public static final int READ_SAVE_SETTINGS_DATE = 193;
    public static final int READ_CONTRAST = 195; 
    public static final int READ_MISSED_BOLUS_REMINDER_ENABLE = 197;
    public static final int READ_MISSED_BOLUS_REMINDERS = 198;
    public static final int READ_FACTORY_PARAMETERS = 199;
    public static final int READ_CURRENT_PUMP_STATUS = 206;
    
    
    // 522
    
    public static final int READ_CALIBRATION_FACTOR = 156;
    public static final int READ_SENSOR_SETTINGS = 153;
    public static final int READ_GLUCOSE_HISTORY = 154;
    public static final int READ_ISIG_HISTORY = 155;
    
    // 523
    public static final int READ_SENSOR_PREDICTIVE_ALERTS = 209;
    public static final int READ_SENSOR_RATE_OF_CHANGE_ALERTS = 212;
    public static final int READ_SENSOR_DEMO_AND_GRAPH_TIMEOUT = 210;
    public static final int READ_SENSOR_ALARM_SILENCE = 211;
    public static final int READ_OTHER_DEVICES_ID = 240;
    public static final int READ_VCNTR_HISTORY = 213;
    
    
    
    
    /**
     * Command: Code/Id
     */
    public int command_code = 0; 

    /**
     * Command: Description
     */
    public String command_description = ""; 

    /**
     * Command: Parameters Array
     */
    public int[] command_parameters = null;
    
    /**
     * Command: Parameters Count
     */
    public int command_parameters_count = 0;
    
    /**
     * Command: Multi Xmit Mode
     */
    public boolean use_multi_xmit_mode = false;

    /**
     * Command: Sequence Number
     */
    public int sequence_number = -1;
    
    /**
     * Command: Raw Data
     */
    //public int[] raw_data = null;
    
    /**
     * Command: Sub description
     */
    public String sub_description = null;
    
    /**
     * Command: Record Length
     */
    public int record_length = 64;

    /**
     * Command: Max Records
     */
    public int max_records = 1;
    
    /**
     * Command: Data Count (for paging)
     */
    public int data_count = 0;
    
    /**
     * Command: Command Type
     */
    public int command_type = 0;
    
    /**
     * Command: Allowed Retries
     */
    public int allowed_retries = 0;
    
    /**
     * Command: Max Timeout (Allowed Time)
     */
    public int max_allowed_time = 2000; 
    

    
    public int type_of_reply = 0;
    
    
    public boolean has_sub_commands = false;
    
    public boolean has_only_sub_commands = false;
    
    int address = 0;
    int address_length = 0;
    int data_offset = 0;
    int command_length = 0;
    int[] packet = null;
    //int max_retries = 2;
    
    
    //public boolean has_sub_commands = false;
    
    
    
    /**
     * Constructor
     * 
     * @param command_code_
     * @param desc
     */
/*    public MinimedCommand(int command_code_, String desc)
    {
        this(command_code_, desc, 0, null, 1, 2000);
    }*/
    
    /**
     * Constructor
     * 
     * @param command_code_
     * @param desc
     * @param cmd_parameter_count 
     * @param params 
     */
/*    public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params)
    {
        this(command_code_, desc, cmd_parameter_count, params, 1, 2000);
    }

    
    public MinimedCommand(int command_code_, String desc, int[] params)
    {
        this(command_code_, desc, params.length, params, 1, 2000);
    }
  */  
    
    /**
     * Constructor
     * 
     * @param command_code_
     * @param desc
     * @param cmd_parameter_count 
     * @param params 
     * @param max_retries_ 
     */
    /*public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params, int max_retries_)
    {
        this(command_code_, desc, cmd_parameter_count, params, max_retries_, 2000);
    }

    
    
    public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params, int allowed_retries_, int max_allowed_time_)
    {
        this(command_code_, desc, params, allowed_retries_, max_allowed_time_, 0);    
    }
    
    */
    
    
    
    /**
     * Constructor
     * 
     * @param command_code_
     * @param desc
     * @param cmd_parameter_count 
     * @param params 
     * @param allowed_retries_ 
     * @param max_allowed_time_ 
     */
 /*   public MinimedCommand(int command_code_, String desc, int[] params, int allowed_retries_, int max_allowed_time_, int type_of_reply_)
    {
        this.command_code = command_code_;
        this.command_description = desc;
        if (params == null)
        {
            this.command_parameters_count = 0;
        }
        else
            this.command_parameters_count = params.length;
        
        
        //this.command_parameters_count = cmd_parameter_count;
        this.command_parameters = params;
        this.allowed_retries = allowed_retries_;
        this.max_allowed_time = max_allowed_time_;
        
        this.type_of_reply = type_of_reply_;
        
        // FIXME
        
        if ((command_code == 93) && (params!=null) && (params[0] == 1))
            setUseMultiXmitMode(true);
        
        this.clearData();
    }
   */ 
    
    
    
    
    public MinimedCommand(int code, String desc)
    {
        // Command(int code, String desc, int bytes_per_rec, int max_recs, int cmd_type)
        this(code, desc, 64, 1, 0);
    }

    public MinimedCommand(int code, String desc, int cmd_params[], int param_count)
    {
        // Command(int code, String desc, int bytes_per_rec, int max_recs, int cmd_type)
        this(code, desc, 0, 1, 11);
        this.command_parameters = cmd_params;
        this.command_parameters_count = param_count;
    }

    /*
    public MinimedCommand(int i, String s, int j, int k, int l)
    {
        // Command(int code, String desc, int bytes_per_rec, int max_recs, int cmd_type)
        this(i, s, j, k, l);
    }*/

    public MinimedCommand(int code, String desc, int param_count)
    {
        // Command(int code, String desc, int bytes_per_rec, int max_recs, int cmd_type)
        this(code, desc, 0, 1, 11);
        this.command_parameters_count = param_count;
        int k = param_count / 64 + 1;
        this.command_parameters = new int[64 * k];
    }
    
    
    
    public MinimedCommand(int code, String desc, int bytes_per_rec, int max_recs, int cmd_type)
    {
        this(code, desc, bytes_per_rec, max_recs, 0, 0, cmd_type);
        
//        m_dataOffset = 0;
//        m_cmdLength = 2;
        setUseMultiXmitMode(false);
    }

    public MinimedCommand(int code, String desc, int bytes_per_rec, int max_recs, int addy, int addy_len, 
            int cmd_type)
    {
        this.command_description = desc;
        this.command_code = code;
        
        this.record_length = bytes_per_rec;
        this.max_records = max_recs;
        
        prepareForReading();
        
        this.address = 0;
        this.address_length = addy_len;
        this.data_offset = 2;
    
        if (addy_len==1)
            this.command_length = 2 + addy_len;
        else
            this.command_length = 2 + addy_len + 1;

        this.packet = new int[0];
        this.command_type = cmd_type;
        this.command_parameters_count = 0;
        this.command_parameters = new int[64];
        setUseMultiXmitMode(false);
        this.allowed_retries = 2;
        
        
        
        
        /*
        //m_numBytesRead = 0;
        //m_extraObject = null;
        //m_effectTime = 0;
        allocateRawData();
        m_address = addy;
        m_addressLength = addy_len;
        m_dataOffset = 2;
        if(addy_len == 1)
            m_cmdLength = 2 + addy_len;
        else
            m_cmdLength = 2 + addy_len + 1;
        m_packet = new int[0];
        m_commandType = cmd_type;
        m_commandParameterCount = 0;
        m_commandParameters = new int[64];
        setUseMultiXmitMode(false);
        m_maxRetries = 2;
        */
    }
    
    
    
    
    
    
    
    
    
    /**
     * Get Full Command Description
     * 
     * @return
     */
    public String getFullCommandDescription()
    {
        if (this.sub_description==null)
            return " CMD [id=" + this.command_code + ",hex=" + MinimedDeviceUtil.getInstance().getHexUtils().getHex(command_code) + ",description=" + MinimedDeviceUtil.getInstance().getI18nControl().getMessage(this.command_description) + "] ";
        else
            return " CMD [id=" + this.command_code + ",hex=" + MinimedDeviceUtil.getInstance().getHexUtils().getHex(command_code) + ",description=" + MinimedDeviceUtil.getInstance().getI18nControl().getMessage(this.command_description) + "-" + this.sub_description + "] ";
    }
    
    
    /**
     * Is Multi Xmit Mode
     * 
     * @return
     */
    public boolean isUseMultiXmitMode()
    {
        if (this.use_multi_xmit_mode)
            return true;
        
        return ((command_code == 93) && (this.command_parameters!=null) && (this.command_parameters[0] == 1));
//            setUseMultiXmitMode(true);        
//        return use_multi_xmit_mode;
    }
    
    /**
     * Set Multi Xmit Mode
     * 
     * @param mxmit 
     */
    public void setUseMultiXmitMode(boolean mxmit)
    {
        use_multi_xmit_mode = mxmit;
    }
    
    
    /**
     * Prepare for Reading
     */
    public void prepareForReading()
    {
        if (this.reply==null)
        {
            this.reply = new MinimedCommandReply(MinimedCommandReply.COMMAND_REPLY_DATA, record_length, max_records);
        }
        
        this.reply.clearData();
    }
    
    public MinimedCommandReply reply = null;
    
    
    public void clearData()
    {
        prepareForReading();
    }

    
    public void clearDataForce()
    {
        log.debug("Rec length: " + record_length + ", max_records= " + max_records);
        this.reply = new MinimedCommandReply(MinimedCommandReply.COMMAND_REPLY_DATA, record_length, max_records);
        this.reply.clearData();
    }
    
    
    public MinimedCommandReply getReply()
    {
        return this.reply;
    }
    
    

    /**
     * Create Command Packet
     * 
     * @return
     */
    public MinimedCommand createCommandPacket()
    {
        log.debug("createCommandPacket()");
        MinimedCommand cmd = new MinimedCommand(this.command_code, this.command_description);
        cmd.sub_description = "command packet";

        cmd.command_parameters = null;
        cmd.record_length = 0;
        cmd.max_records = 0;
        cmd.command_type = 0;
        cmd.command_parameters_count =0;
        cmd.reply = this.reply;
        //cmd.raw_data = this.raw_data;
        
        //cmd.prepareForReading();
        
        //cmd.command_parameters = this.command_parameters;
        
        if ((this.command_code == 93) && (this.command_parameters[0] == 1))
            cmd.setUseMultiXmitMode(true);
        
        return cmd;
    }
    
    
    
    
    public boolean canReturnData()
    {
        return (this.max_records * this.record_length) > 0;
    }
    
    
    
    
    public boolean hasSubCommands()
    {
        return this.has_sub_commands;
    }
    
    
    
    
    
    public void executeSubCommands() throws PlugInBaseException
    {
    }
    
    
    public static void saveCommandData(MinimedCommand cmd, String filename)
    {
        try
        {
            // Write to disk with FileOutputStream
            FileOutputStream f_out = new FileOutputStream(filename);

            //Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

            // Write object out to disk
            obj_out.writeObject ( cmd );
        }
        catch(IOException ex)
        {
            log.error("Error writing object to file. Ex: " + ex, ex);
        }
    }
    
    public static MinimedCommand loadObjectData(String filename)
    {
        try
        {
            // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream(filename);

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
    
            // Read an object
            Object obj = obj_in.readObject();
            
            return (MinimedCommand)obj;
        }
        catch(Exception ex)
        {
            log.error("Error reading object from file [" + filename + "]. Ex: " + ex, ex);
            return null;
        }
        
    }
    
    
}
