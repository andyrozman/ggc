package ggc.pump.device.minimed;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.minimed.MinimedDevicesIds;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommandHistoryCGMS;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.manager.PumpDevicesIds;
import gnu.io.SerialPort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.HexUtils;

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
 *  Filename:     Minimed522  
 *  Description:  Minimed 522/722 implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed523 extends Minimed522
{

    private static Log log = LogFactory.getLog(Minimed522.class);

    
    /**
     * Constructor
     *  
     * @param da - data access instance
     * @param device_type - device type 
     * @param full_port - full port identification 
     * @param writer - output writer instance
     */
    public Minimed523(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, device_type, full_port, writer);
    }
    
    
    
    /**
     * Constructor
     *  
     * @param da - data access instance
     * @param full_port - full port identification 
     * @param writer - output writer instance
     */
    public Minimed523(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_523, full_port, writer);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public Minimed523(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    

    
    public void initDeviceSpecific()
    {
        super.initDeviceSpecific();

//        util.config.comm_delay_io = 4;
//        util.config.comm_baudrate = 56000;
        
        
        util.config.comm_delay_io = 250;
        util.config.comm_baudrate = 57600;
        
        util.config.comm_data_bits = SerialPort.DATABITS_8;
        util.config.comm_flowcontrol = SerialPort.FLOWCONTROL_NONE;
        util.config.comm_parity = SerialPort.PARITY_NONE;
        util.config.comm_stop_bits = SerialPort.STOPBITS_1;
        
        
        
//        m_baudRate = 10;
//        m_ioDelayMS = 4;
/*        
        setReceiveBufferSize(16384);
        setTransmitBufferSize(2048);
        setBaudRate(i);
        m_serialConfig.setDataBits(3);
        m_serialConfig.setStopBits(0);
        m_serialConfig.setParity(0);
        m_serialConfig.setHandshake(0);
        
        Contract.pre(i == 7 || i == 8 || i == 9 || i == 10, "bad baudRate value of " + i + "; must be " + 7 + ", " + 8 + ", " + 9 + " or " + 10);
        m_serialConfig.setBitRate(i);

        
  public static final int HS_NONE = 0;
    public static final int HS_XONXOFF = 1;
    public static final int HS_CTSRTS = 2;
    public static final int HS_CTSDTR = 2;
    public static final int HS_DSRDTR = 3;
    
    public static final int HS_HARD_IN = 16;
    public static final int HS_HARD_OUT = 32;
    public static final int HS_SOFT_IN = 64;
    public static final int HS_SOFT_OUT = 128;
    public static final int HS_SPLIT_MASK = 240;
    private static final String handshakeNames[] = {
        "NONE", "XON-XOFF", "CTS-RTS", "DSR-DTR"
    };
    public static final int BR_110 = 0;
    public static final int BR_150 = 1;
    public static final int BR_300 = 2;
    public static final int BR_600 = 3;
    public static final int BR_1200 = 4;
    public static final int BR_2400 = 5;
    public static final int BR_4800 = 6;
    public static final int BR_9600 = 7;
    public static final int BR_19200 = 8;
    public static final int BR_38400 = 9;
    public static final int BR_57600 = 10;
    public static final int BR_115200 = 11;
    public static final int BR_230400 = 12;
    public static final int BR_460800 = 13;
    private static final int bitRateNumbers[] = {
        110, 150, 300, 600, 1200, 2400, 4800, 9600, 19200, 38400, 
        57600, 0x1c200, 0x38400, 0x70800
    };
    public static final int PY_NONE = 0;
    public static final int PY_ODD = 1;
    public static final int PY_EVEN = 2;
    public static final int PY_MARK = 3;
    public static final int PY_SPACE = 4;
    private static final String parityNames[] = {
        "NONE", "ODD", "EVEN", "MARK", "SPACE"
    };
    public static final int LN_5BITS = 0;
    public static final int LN_6BITS = 1;
    public static final int LN_7BITS = 2;
    public static final int LN_8BITS = 3;
    public static final int ST_1BITS = 0;
    public static final int ST_2BITS = 1;
    public static final int PWR_STANDBY = 0;
    public static final int PWR_ACTIVE = 1;
        
        
        
  */      
    }
    
    
    
    
    
    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Minimed 522/722";
    }


    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "mm_522_722.jpg";
    }
    

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_523;
    }

    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_MINIMED_508";
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.minimed.Minimed522";
    }


    /** 
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }
 
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }
    
    
    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }
   
    
    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        //return "TYPE=Unit;STEP=0.1";
        return null;
    }
    
    
    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    public float getBolusStep()
    {
        return 0.1f;
    }
    
    
    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep()
    {
        return 0.1f;
    }
    
    
    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return false;
    }

    
    public int getMinimedDeviceId()
    {
        return MinimedDevicesIds.PUMP_MINIMED_523;
    }
    
    

    public void createCommands()
    {
        super.createCommands();
        
        // CGMS SETTINGS
        util.addCommand(MinimedCommand.READ_SENSOR_PREDICTIVE_ALERTS, new MinimedCommand(209, "Read Sensor Predictive Alerts"));
        util.addCommand(MinimedCommand.READ_SENSOR_RATE_OF_CHANGE_ALERTS, new MinimedCommand( 212, "Read Sensor Rate Of Change Alerts"));
        util.addCommand(MinimedCommand.READ_SENSOR_DEMO_AND_GRAPH_TIMEOUT, new MinimedCommand( 210, "Read Sensor Demo and Graph Timeout"));
        util.addCommand(MinimedCommand.READ_SENSOR_ALARM_SILENCE, new MinimedCommand( 211, "Read Sensor Alarm Silence"));
        util.addCommand(MinimedCommand.READ_SENSOR_SETTINGS, new MinimedCommand( 207, "Read Sensor Settings"));
        util.addCommand(MinimedCommand.READ_OTHER_DEVICES_ID, new MinimedCommand( 240, "Read Other Devices ID"));

        // CGMS DATA
        util.addCommand(MinimedCommand.READ_VCNTR_HISTORY, new MinimedCommandHistoryCGMS(213, "Read Vcntr History", 1024, 32));
        
    }
    
    
    
    public Object convertDeviceReply(MinimedCommand mc)
    {
        switch(mc.command_code)
        {
            //case MinimedCommand.READ_OTHER_DEVICES_ID: // = 240;
            //    return this.convertDebug(mc);
            
            case 207: // Sensor Settings
            case MinimedCommand.READ_SENSOR_RATE_OF_CHANGE_ALERTS: // = 212;
            case MinimedCommand.READ_SENSOR_PREDICTIVE_ALERTS: // 209;
            case MinimedCommand.READ_SENSOR_ALARM_SILENCE: // = 211;
            case MinimedCommand.READ_SENSOR_DEMO_AND_GRAPH_TIMEOUT:
                return this.util.decoder.decode(mc);
                
            default:
                return super.convertDeviceReply(mc);
        }
        
    }
    
    
    
    public boolean convertCurrentSettings(MinimedCommand cmd) //throws BadDeviceValueException
    {
        super.convertCurrentSettings(cmd);
        
        util.config.addSetting("MM_SETTINGS_BOLUS_SCROLL_STEP_SIZE", "" + cmd.reply.raw_data[21]);
        util.config.addSetting("MM_SETTINGS_CAPTURE_EVENT_ENABLE", util.decoder.parseResultEnable(cmd.reply.raw_data[22]));
        util.config.addSetting("MM_SETTINGS_OTHER_DEVICE_ENABLE", util.decoder.parseResultEnable(cmd.reply.raw_data[23]));
        util.config.addSetting("MM_SETTINGS_OTHER_DEVICE_MARRIES_STATE", util.decoder.parseResultEnable(cmd.reply.raw_data[24]));

        return true;
    }
    
    
    
    
    
    
    
    int getSettingIndexMaxBasal()
    {
        return 7;
    }

    int getSettingIndexTimeDisplayFormat()
    {
        return 9;
    }
    
    public double convertMaxBolus(int ai[])
    {
        return this.util.decoder.toBolusInsulin(this.util.getHexUtils().makeInt(ai[5], ai[6]));
    }

/*    
    void decodeCurrentGlucoseHistoryPageNumber(int ai[])
    {
        super.decodeCurrentGlucoseHistoryPageNumber(ai);
        Contract.invariant(m_cmdReadGlucoseHistoryData.m_extraObject != null);
        int i = ai[5];
        IntRange intrange = (IntRange)m_cmdReadGlucoseHistoryData.m_extraObject;
        m_cmdReadVcntrHistoryData.m_maxRecords = m_cmdReadGlucoseHistoryData.m_maxRecords;
        m_cmdReadVcntrHistoryData.allocateRawData();
        calcTotalBytesToRead();
        m_cmdReadVcntrHistoryData.m_extraObject = new IntRange(intrange.getMinimum(), intrange.getMaximum(), intrange.getDefault(), "Vcntr Page Range");
        log.info( (new StringBuilder()).append("decodeCurrentGlucoseHistoryPageNumber: , available Vcntr pages = ").append(i).append(", Vcntr pages to read = ").append(m_cmdReadVcntrHistoryData.m_maxRecords).toString());
    }
  */  
    
    
    
    protected boolean convertDebug(MinimedCommand cmd)
    {
        this.util.decoder.debugResult(cmd);
        
        //log.debug(cmd.command_code + " [" + cmd.command_description + "]");
        //log.debug("[" + util.getHexUtils().getHexCompact(cmd.reply.raw_data));
        
        int[] rd = cmd.reply.raw_data;
        HexUtils hu = util.getHexUtils();
        
        int i = hu.makeInt(rd[0], rd[1]);
        
        log.debug("Get Value 1: " + i);
        
        i = hu.makeInt(rd[2], rd[3]);
        
        log.debug("Get Value 2: " + i);
        
        
        //log.debug("3: night 30:" + cmd.reply.raw_data[3]);
        //log.debug("4: day:" + cmd.reply.raw_data[4]);
        
        //log.debug(cmd.reply.raw_data[3] - Integer.parseInt("80", 16));
        
        //log.debug("Low predictive alert - Time sensitivyty: 00:" + (cmd.reply.raw_data[4] - Integer.parseInt("80", 16)));
        //log.debug("High predictive alert - Time sensitivyty: 00:" + (cmd.reply.raw_data[3] - Integer.parseInt("80", 16)));
        
        
        return true;
    }
    
    
    
    
    
    
    
}


