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

// TODO: Auto-generated Javadoc
/**
 * Application:   GGC - GNU Gluco Control
 * Plug-in:       Pump Tool (support for Pump devices)
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename:     MinimedCommand
 * Description:  Minimed Command
 * 
 * Author: Andy {andy@atech-software.com}
 */


public class MinimedCommand implements Serializable
{
    
    private static final long serialVersionUID = 5687165074481235169L;


    /** The log. */
    private static Log log = LogFactory.getLog(MinimedCommand.class);
    
    
    /** Command: Set RF Power On. */
    public static final int COMMAND_SET_RF_POWER_ON = 93;

    /** Command: Set RF Power Off. */
    public static final int COMMAND_SET_RF_POWER_OFF = 400;

    /** Command: Read Pump Error Status. */
    public static final int COMMAND_READ_PUMP_ERROR_STATUS = 117;

    /** Command: Read Pump State. */
    public static final int COMMAND_READ_PUMP_STATE = 131;

    /** Command: Read Temporary Basal. */
    public static final int COMMAND_READ_TEMPORARY_BASAL = 120;

    /** Command: Detect Bolus. */
    public static final int COMMAND_DETECT_BOLUS = 75;
    
    /** Command: Cancel Suspend. */
    public static final int COMMAND_CANCEL_SUSPEND = 77;
    
    /** Command: Read Firmware Version. */
    public static final int COMMAND_READ_FIRMWARE_VERSION = 116;

    /** Command: Ack. */
    public static final int COMMAND_ACK = 6;
    
    /** Command: Keypad Push (ACK). */
    public static final int COMMAND_KEYPAD_PUSH_ACK = 401;
    
    /** Command: Keypad Push (ESC). */
    public static final int COMMAND_KEYPAD_PUSH_ESC = 402;
    
    
    /** The Constant COMMAND_HISTORY_DATA. */
    public static final int COMMAND_HISTORY_DATA = 403;
    
    
    /** Command: Code/Id. */
    public int command_code = 0; 

    /** Command: Description. */
    public String command_description = ""; 

    /** Command: Parameters Array. */
    public int[] command_parameters = null;
    
    /** Command: Parameters Count. */
    public int command_parameters_count = 0;
    
    /** Command: Multi Xmit Mode. */
    public boolean use_multi_xmit_mode = false;

    /** Command: Sequence Number. */
    public int sequence_number = -1;
    
    /** Command: Raw Data. */
    public int[] raw_data = null;
    
    /** Command: Sub description. */
    public String sub_description = null;
    
    /** Command: Record Length. */
    public int record_length = 64;

    /** Command: Max Records. */
    public int max_records = 1;
    
    /** Command: Data Count (for paging). */
    public int data_count = 0;
    
    /** Command: Command Type. */
    public int command_type = 0;
    
    /** Command: Allowed Retries. */
    public int allowed_retries = 0;
    
    /** Command: Max Timeout (Allowed Time). */
    public int max_allowed_time = 2000; 
    
    
    /** The has_sub_commands. */
    public boolean has_sub_commands = false;
    
    /** The has_only_sub_commands. */
    public boolean has_only_sub_commands = false;
    
    /**
     * Constructor.
     * 
     * @param command_code_ the command_code_
     * @param desc the desc
     */
    public MinimedCommand(int command_code_, String desc)
    {
        this(command_code_, desc, 0, null, 1, 2000);
    }
    
    /**
     * Constructor.
     * 
     * @param command_code_ the command_code_
     * @param desc the desc
     * @param cmd_parameter_count the cmd_parameter_count
     * @param params the params
     */
    public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params)
    {
        this(command_code_, desc, cmd_parameter_count, params, 1, 2000);
    }

    /**
     * Constructor.
     * 
     * @param command_code_ the command_code_
     * @param desc the desc
     * @param cmd_parameter_count the cmd_parameter_count
     * @param params the params
     * @param max_retries_ the max_retries_
     */
    public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params, int max_retries_)
    {
        this(command_code_, desc, cmd_parameter_count, params, max_retries_, 2000);
    }

    
    
    
    
    
    /**
     * Constructor.
     * 
     * @param command_code_ the command_code_
     * @param desc the desc
     * @param cmd_parameter_count the cmd_parameter_count
     * @param params the params
     * @param allowed_retries_ the allowed_retries_
     * @param max_allowed_time_ the max_allowed_time_
     */
    public MinimedCommand(int command_code_, String desc, int cmd_parameter_count, int[] params, int allowed_retries_, int max_allowed_time_)
    {
        this.command_code = command_code_;
        this.command_description = desc;
        this.command_parameters_count = cmd_parameter_count;
        this.command_parameters = params;
        this.allowed_retries = allowed_retries_;
        this.max_allowed_time = max_allowed_time_;
        
        // fixme
        
        
        if ((command_code == 93) && (params[0] == 1))
            setUseMultiXmitMode(true);
        
        
    }
    
    
    /**
     * Get Full Command Description.
     * 
     * @return the full command description
     */
    public String getFullCommandDescription()
    {
        MinimedDeviceUtil mdu = MinimedDeviceUtil.getInstance();
        
        if (this.sub_description==null)
            return " CMD [id=" + this.command_code + ",hex=" + mdu.getHexUtils().getHex(command_code) + ",description=" + mdu.getI18nControl().getMessage(this.command_description) + "] ";
        else
            return " CMD [id=" + this.command_code + ",hex=" + mdu.getHexUtils().getHex(command_code) + ",description=" + mdu.getI18nControl().getMessage(this.command_description) + "-" + this.sub_description + "] ";
    }
    
    
    /**
     * Is Multi Xmit Mode.
     * 
     * @return true, if checks if is use multi xmit mode
     */
    public boolean isUseMultiXmitMode()
    {
        return use_multi_xmit_mode;
    }
    
    /**
     * Set Multi Xmit Mode.
     * 
     * @param mxmit the mxmit
     */
    public void setUseMultiXmitMode(boolean mxmit)
    {
        use_multi_xmit_mode = mxmit;
    }
    
    
    /**
     * Prepare for Reading.
     */
    public void prepareForReading()
    {
        this.raw_data = new int[this.record_length * this.max_records];
    }
    

    /**
     * Create Command Packet.
     * 
     * @return the minimed command
     */
    public MinimedCommand createCommandPacket()
    {
        MinimedCommand cmd = new MinimedCommand(this.command_code, this.command_description);
        cmd.sub_description = "command packet";

        cmd.command_parameters = null;
        cmd.record_length = 0;
        cmd.max_records = 0;
        cmd.command_type = 0;
        cmd.command_parameters_count =0;
        cmd.raw_data = this.raw_data;
        
        if ((this.command_code == 93) && (this.command_parameters[0] == 1))
            cmd.setUseMultiXmitMode(true);
        
        return cmd;
    }
    
    
    
    /**
     * Execute.
     * 
     * @throws PlugInBaseException the plug in base exception
     */
    public void execute() throws PlugInBaseException
    {
    }
    
    
    /**
     * Save command data.
     * 
     * @param cmd the cmd
     * @param filename the filename
     */
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
            log.error("�rror writing object to file. Ex: " + ex, ex);
        }
    }
    
    /**
     * Load object data.
     * 
     * @param filename the filename
     * 
     * @return the minimed command
     */
    public static MinimedCommand loadObjectData(String filename)
    {
        try
        {
     // Read from disk using FileInputStream
            FileInputStream f_in = new FileInputStream(filename);

        // Read object using ObjectInputStream
        ObjectInputStream obj_in = 
            new ObjectInputStream (f_in);

        // Read an object
        Object obj = obj_in.readObject();
        
        return (MinimedCommand)obj;
        }
        catch(Exception ex)
        {
            log.error("�rror reading object from file [" + filename + "]. Ex: " + ex, ex);
            return null;
        }
        
    }
    
    
}
