package ggc.plugin.device.impl.minimed;

import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.CRCUtils;

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
 * Filename:     Minimed512
 * Description:  Minimed 512/712 implementation (just settings)
 * 
 * Author: Andy {andy@atech-software.com}
 */


public class MinimedDeviceUtil
{

    /** The s_util. */
    public static MinimedDeviceUtil s_util = null;
    
    /** The protocol_id. */
    public int protocol_id = 0;
    
    /** The port. */
    public String port = "";
    
    /** The serial_number. */
    private String serial_number = null;
    
    /** The serial_number_bcd. */
    public int[] serial_number_bcd = null;
    
    /** The device_type. */
    private int device_type = 0;
    
    /** The hex_utils. */
    private CRCUtils hex_utils = null;
    
    /** The communication_interface. */
    private MinimedComm_Interface communication_interface = null;
    
    /** The firmware_version. */
    public String firmware_version = null;
    
    /** The interface_paradigmlink_delay. */
    public int interface_paradigmlink_delay = 0;
    
    /** The comm_delay_io. */
    public int comm_delay_io;
    
    /** The comm_baudrate. */
    public int comm_baudrate;
    
    /** The comm_rx_buffer_size. */
    public int comm_rx_buffer_size = 16384;
    
    /** The comm_tx_buffer_size. */
    public int comm_tx_buffer_size = 2048;
    
    /** The comm_data_bits. */
    public int comm_data_bits = SerialPort.DATABITS_8;
    
    /** The comm_stop_bits. */
    public int comm_stop_bits = SerialPort.STOPBITS_1;
    
    /** The comm_parity. */
    public int comm_parity = SerialPort.PARITY_NONE;
    
    /** The comm_flowcontrol. */
    public int comm_flowcontrol = SerialPort.FLOWCONTROL_NONE;
    
    /** The commands. */
    Hashtable<Integer, MinimedCommand> commands;
    
    /** The m_da. */
    DataAccessPlugInBase m_da = null;
    
    /** The m_ic. */
    I18nControlAbstract m_ic = null;
    
    
    //setReceiveBufferSize(16384);
    //setTransmitBufferSize(2048);
    //setBaudRate(i);
    //m_serialConfig.setDataBits(3);
    //m_serialConfig.setStopBits(0);
    //m_serialConfig.setParity(0);
    //m_serialConfig.setHandshake(0);
    
    
    
    /**
     * Instantiates a new minimed device util.
     * 
     * @param da the da
     */
    private MinimedDeviceUtil(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.m_ic = m_da.getI18nControlInstance();
        this.hex_utils = new CRCUtils();
        commands = new Hashtable<Integer, MinimedCommand>();
    }
    
    
    /**
     * Adds the command.
     * 
     * @param command_id the command_id
     * @param mdc the mdc
     */
    public void addCommand(int command_id, MinimedCommand mdc)
    {
        commands.put(command_id, mdc);
    }
    
    /**
     * Gets the command.
     * 
     * @param command_id the command_id
     * 
     * @return the command
     */
    public MinimedCommand getCommand(int command_id)
    {
        return commands.get(command_id);
    }

    /**
     * Is Command Available.
     * 
     * @param command_id the command_id
     * 
     * @return true, if checks if is command available
     */
    public boolean isCommandAvailable(int command_id)
    {
        return commands.containsKey(command_id);
    }
    

    
    /**
     * Get Instance.
     * 
     * @param da the da
     * 
     * @return the minimed device util
     */
    public static MinimedDeviceUtil createInstance(DataAccessPlugInBase da)
    {
        if (MinimedDeviceUtil.s_util == null)
            MinimedDeviceUtil.s_util = new MinimedDeviceUtil(da);
        
        return MinimedDeviceUtil.s_util;
    }
    
    
    /**
     * Get Instance.
     * 
     * @return the instance
     */
    public static MinimedDeviceUtil getInstance()
    {
        return MinimedDeviceUtil.s_util;
    }

    
    /**
     * Set Device Type.
     * 
     * @param dev_type the dev_type
     */
    public void setDeviceType(int dev_type)
    {
        this.device_type = dev_type;
    }

    
    /**
     * Gets the device type.
     * 
     * @return the device type
     */
    public int getDeviceType()
    {
        return this.device_type;
    }
    
    
    /**
     * Set Combined Port.
     * 
     * @param comb_port the comb_port
     */
    public void setCombinedPort(String comb_port)
    {
        // fullport is packed: portocol_id;port;serial_id
        String[] ports = comb_port.split(";");
            
        this.protocol_id = m_da.getIntValueFromString(ports[0], 2);
        this.port = ports[1];
        this.serial_number = ports[2];
        this.serial_number_bcd = hex_utils.makePackedBCD(ports[2]);
    }
    
    
    /**
     * Gets the serial number.
     * 
     * @return the serial number
     */
    public String getSerialNumber()
    {
        return this.serial_number;
    }

    /**
     * Sets the communication interface.
     * 
     * @param comm_int the new communication interface
     */
    public void setCommunicationInterface(MinimedComm_Interface comm_int)
    {
        this.communication_interface = comm_int;
    }
    
    /**
     * Gets the communication interface.
     * 
     * @return the communication interface
     */
    public MinimedComm_Interface getCommunicationInterface()
    {
        return this.communication_interface;
    }
    
    
    /**
     * Gets the hex utils.
     * 
     * @return the hex utils
     */
    public CRCUtils getHexUtils()
    {
        return this.hex_utils;
    }

    
    /**
     * Checks if is pump device.
     * 
     * @return true, if is pump device
     */
    public boolean isPumpDevice()
    {
        return isPumpDevice(this.device_type);
    }    
    

    /**
     * Checks if is cGMS device.
     * 
     * @return true, if is cGMS device
     */
    public boolean isCGMSDevice()
    {
        return isCGMSDevice(this.device_type);
    }
    
    
    /**
     * Checks if is pump device.
     * 
     * @param device_id the device_id
     * 
     * @return true, if is pump device
     */
    public boolean isPumpDevice(int device_id)
    {
        if ((device_id>=10000) && (device_id<20000)) 
            return true;
        else 
            return false;
    }    
    

    /**
     * Checks if is cGMS device.
     * 
     * @param device_id the device_id
     * 
     * @return true, if is cGMS device
     */
    public boolean isCGMSDevice(int device_id)
    {
        if ((device_id>=30000) && (device_id<40000))
            return true;
        else
            return false;
    }
    
    
    
    /**
     * Creates the command byte.
     * 
     * @param command_id the command_id
     * 
     * @return the int[]
     */
    public int[] createCommandByte(int command_id)
    {
        return createCommandByte(getCommand(command_id));
    }

    
    
    /**
     * Creates the command byte.
     * 
     * @param command the command
     * 
     * @return the int[]
     */
    public int[] createCommandByte(MinimedCommand command)
    {
        // 0 -> [5] - command header            | HEADER
        // 1 -> [x] - length of next packet     |
        // 2 -> [167] - comand_package          | BODY (ENCODED)
        // 3,4,5 -> - BCD serial number
        // 6 -> command_code
        // 7 -> parameter
        // 8 -> CRC (2-7)
        
        ArrayList<Integer> list = new ArrayList<Integer>();

        list.add(167);
        list.add(this.serial_number_bcd[0]);
        list.add(this.serial_number_bcd[1]);
        list.add(this.serial_number_bcd[2]);
        list.add(command.command_code);
        
        if (command.command_parameters_count==0)
        {
            list.add(0);
        }
        else
        {
            //int ai2[] = new int[i + 1];
            //System.arraycopy(ai, 0, ai2, 0, i);
            if (command.sequence_number>-1)
                list.add(command.sequence_number);
            else
                list.add(command.command_parameters_count);
            
            hex_utils.addIntArrayToAL(list, command.command_parameters);
        }

        int crc = hex_utils.computeCRC8(hex_utils.getIntArrayFromAL(list), 0, list.size());  // size-1 ??
        
        list.add(crc);
        
        int[] cmd = hex_utils.getIntArrayFromAL(list);
        
        
        
        
        
        
/*        
        int cmd[] = new int[7];
        cmd[0] = 167;
        cmd[1] = this.serial_number_bcd[0];
        cmd[2] = this.serial_number_bcd[1];
        cmd[3] = this.serial_number_bcd[2];
        cmd[4] = command.command_code;
        
        if (command.command_parameters_count==0)
        {
            cmd[5] = 0;
            cmd[6] = hex_utils.computeCRC8(cmd, 0, 7);
        }
        else
        {

            // FIXME
            //throw Exception("Not implemented")
            
            
            i=5;
            
            int ai2[] = new int[i + 1];
            System.arraycopy(ai, 0, ai2, 0, i);
            /*
            if(m_sequenceNumber != null)
                ai2[i] = m_sequenceNumber.intValue();
            else
                ai2[i] = m_deviceCommand.m_commandParameterCount;
            ai = hex_utils.concatIntArrays(ai2, m_deviceCommand.m_commandParameters);
            ai = hex_utils.concatIntArrays(ai, new int[1]);
            i = ai.length - 1;
            
            */
            
            
            
  //      }

        
        
        if (this.getCommunicationInterface().hasEncodingDecodingSupport())
            cmd = this.getCommunicationInterface().encode(cmd); 
        
        int cmd_header[] = new int[2];
        
        if (command.isUseMultiXmitMode())
            cmd_header[0] = 10;
        else if (command.command_parameters_count == 0)
            cmd_header[0] = 5;
        else
            cmd_header[0] = 4;
        cmd_header[1] = cmd.length;
        cmd = hex_utils.concatIntArrays(cmd_header, cmd);
        
        
        
        
        
        
        
        
        /*
            int i = 0;
            //Contract.pre(m_deviceCommand.m_cmdLength > 0, "m_cmdLength is < 1.");
            int ai[] = new int[7];
            ai[i++] = 167;
            int ai1[] = this.getPackedSerialNumber();
            ai[i++] = ai1[0];
            ai[i++] = ai1[1];
            ai[i++] = ai1[2];
            ai[i++] = m_deviceCommand.m_commandCode;
            
            if(m_deviceCommand.m_commandParameterCount == 0)
            {
                ai[i++] = 0;
            } 
            else
            {
                int ai2[] = new int[i + 1];
                System.arraycopy(ai, 0, ai2, 0, i);
                if(m_sequenceNumber != null)
                    ai2[i] = m_sequenceNumber.intValue();
                else
                    ai2[i] = m_deviceCommand.m_commandParameterCount;
                ai = hex_utils.concatIntArrays(ai2, m_deviceCommand.m_commandParameters);
                ai = hex_utils.concatIntArrays(ai, new int[1]);
                i = ai.length - 1;
            }
            ai[i++] = hex_utils.computeCRC8(ai, 0, i - 1);
            m_deviceCommand.m_packet = ai;
            
            if (this.hasEncodingDecoding())
                return encode(ai);
            else
                return ai;
            
            //return encodeDC(ai);
        */
        
        
        return cmd;
    }
    
    
    
    /**
     * Sleep io.
     */
    public void sleepIO()
    {
        sleepMs(this.comm_delay_io);
    }
    

    /**
     * Sleep paradigm link.
     */
    public void sleepParadigmLink()
    {
        sleepMs(this.interface_paradigmlink_delay);
    }
    
    
    
    /**
     * Sleep ms.
     * 
     * @param ms the ms
     */
    public void sleepMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(Exception ex)
        {}
    }
    
    /** The halt_requested. */
    boolean halt_requested = false;
    
    
    /**
     * Checks if is halt requested.
     * 
     * @return true, if is halt requested
     */
    public boolean isHaltRequested()
    {
        return halt_requested;
    }
    
    
    /**
     * Gets the normal pump state.
     * 
     * @return the normal pump state
     */
    public int getNormalPumpState()
    {
        if (this.device_type==MinimedDevicesIds.PUMP_MINIMED_511)
            return 0;
        else
            return 3;
        
    }
    
    /**
     * Get Tool Device.
     * 
     * @return the tool device
     */
    public String getToolDevice()
    {
        if (this.isPumpDevice())
            return m_ic.getMessage("MM_DEVICE_PUMP");
        
        if (this.isCGMSDevice())
            return m_ic.getMessage("MM_DEVICE_CGMS");
        
        return "Unknown";
    }
    
    
    
    /**
     * Gets the nAK description.
     * 
     * @param i the i
     * 
     * @return the nAK description
     */
    public String getNAKDescription(int i)
    {
        String s;
        if(i <= NAK_DESCRIPTIONS_TABLE.length - 1)
            s = NAK_DESCRIPTIONS_TABLE[i];
        else
            s = "UNKNOWN NAK DESCRIPTION";
        return s;
    }
    
    
    /** The Constant NAK_DESCRIPTIONS_TABLE. */
    private static final String NAK_DESCRIPTIONS_TABLE[] = {
            "UNKNOWN NAK DESCRIPTION", 
            "REQUEST PAUSE FOR 3 SECONDS", 
            "REQUEST PAUSE UNTIL ACK RECEIVED", 
            "CRC ERROR", 
            "REFUSE PROGRAM UPLOAD", 
            "TIMEOUT ERROR", 
            "COUNTER SEQUENCE ERROR", 
            "PUMP IN ERROR STATE", 
            "INCONSISTENT COMMAND REQUEST", 
            "DATA OUT OF RANGE", 
            "DATA CONSISTENCY", 
            "ATTEMPT TO ACTIVATE UNUSED PROFILES", 
            "PUMP DELIVERING BOLUS", 
            "REQUESTED HISTORY BLOCK HAS NO DATA", 
            "HARDWARE FAILURE"
         };
    
    
    /**
     * Gets the paramaters array.
     * 
     * @param count the count
     * @param params the params
     * 
     * @return the paramaters array
     */
    public int[] getParamatersArray(int count, int...params)
    {
        int arr[] = new int[count];
        
        for (int i=0; i<count; i++)
        {
            arr[i] = params[i];
        }
        
        return arr;
    }
    
    
    
    /**
     * Gets the data access.
     * 
     * @return the data access
     */
    public DataAccessPlugInBase getDataAccess()
    {
        return this.m_da;
    }
    
    /**
     * Gets the i18n control.
     * 
     * @return the i18n control
     */
    public I18nControlAbstract getI18nControl()
    {
        return this.m_ic;
    }
    
    
    
}
