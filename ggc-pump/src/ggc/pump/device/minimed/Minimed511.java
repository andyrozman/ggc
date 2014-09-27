package ggc.pump.device.minimed;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.minimed.MinimedDevicesIds;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommandHistoryData;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.manager.PumpDevicesIds;

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
 *  Filename:     Minimed512  
 *  Description:  Minimed 512/712 implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed511 extends MinimedPumpDevice
{
    private static Log log = LogFactory.getLog(Minimed511.class);

    // MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();

    /**
     * Constructor
     *  
     * @param da - data access instance
     * @param device_type - device type 
     * @param full_port - full port identification 
     * @param writer - output writer instance
     */
    public Minimed511(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
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
    public Minimed511(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_511, full_port, writer);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public Minimed511(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    @Override
    public void initDeviceSpecific()
    {
        util.config.comm_delay_io = 4;
        util.config.comm_baudrate = 56700;

        util.config.comm_rx_buffer_size = 16384;
        util.config.comm_tx_buffer_size = 2048;

        util.config.strokes_per_basal_unit = 10;
        util.config.strokes_per_bolus_unit = 10;

        // m_baudRate = 10;
        // m_ioDelayMS = 4;
        /*
         * setReceiveBufferSize(16384);
         * setTransmitBufferSize(2048);
         * setBaudRate(i);
         * m_serialConfig.setDataBits(3);
         * m_serialConfig.setStopBits(0);
         * m_serialConfig.setParity(0);
         * m_serialConfig.setHandshake(0);
         * Contract.pre(i == 7 || i == 8 || i == 9 || i == 10,
         * "bad baudRate value of " + i + "; must be " + 7 + ", " + 8 + ", " + 9
         * + " or " + 10);
         * m_serialConfig.setBitRate(i);
         * public static final int HS_NONE = 0;
         * public static final int HS_XONXOFF = 1;
         * public static final int HS_CTSRTS = 2;
         * public static final int HS_CTSDTR = 2;
         * public static final int HS_DSRDTR = 3;
         * public static final int HS_HARD_IN = 16;
         * public static final int HS_HARD_OUT = 32;
         * public static final int HS_SOFT_IN = 64;
         * public static final int HS_SOFT_OUT = 128;
         * public static final int HS_SPLIT_MASK = 240;
         * private static final String handshakeNames[] = {
         * "NONE", "XON-XOFF", "CTS-RTS", "DSR-DTR"
         * };
         * public static final int BR_110 = 0;
         * public static final int BR_150 = 1;
         * public static final int BR_300 = 2;
         * public static final int BR_600 = 3;
         * public static final int BR_1200 = 4;
         * public static final int BR_2400 = 5;
         * public static final int BR_4800 = 6;
         * public static final int BR_9600 = 7;
         * public static final int BR_19200 = 8;
         * public static final int BR_38400 = 9;
         * public static final int BR_57600 = 10;
         * public static final int BR_115200 = 11;
         * public static final int BR_230400 = 12;
         * public static final int BR_460800 = 13;
         * private static final int bitRateNumbers[] = {
         * 110, 150, 300, 600, 1200, 2400, 4800, 9600, 19200, 38400,
         * 57600, 0x1c200, 0x38400, 0x70800
         * };
         * public static final int PY_NONE = 0;
         * public static final int PY_ODD = 1;
         * public static final int PY_EVEN = 2;
         * public static final int PY_MARK = 3;
         * public static final int PY_SPACE = 4;
         * private static final String parityNames[] = {
         * "NONE", "ODD", "EVEN", "MARK", "SPACE"
         * };
         * public static final int LN_5BITS = 0;
         * public static final int LN_6BITS = 1;
         * public static final int LN_7BITS = 2;
         * public static final int LN_8BITS = 3;
         * public static final int ST_1BITS = 0;
         * public static final int ST_2BITS = 1;
         * public static final int PWR_STANDBY = 0;
         * public static final int PWR_ACTIVE = 1;
         */
    }

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Minimed 512/712 515/715";
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "mm_515_715.jpg";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_511;
    }

    @Override
    public int getMinimedDeviceId()
    {
        return MinimedDevicesIds.PUMP_MINIMED_511;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_MINIMED_512";
    }

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    @Override
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
        return "ggc.pump.device.minimed.Minimed512";
    }

    /** 
     * Get Max Memory Records
     */
    @Override
    public int getMaxMemoryRecords()
    {
        return 0;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }

    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    @Override
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
    @Override
    public String getTemporaryBasalTypeDefinition()
    {
        // return "TYPE=Unit;STEP=0.1";
        return null;
    }

    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    @Override
    public float getBolusStep()
    {
        return 0.1f;
    }

    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    @Override
    public float getBasalStep()
    {
        return 0.1f;
    }

    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    @Override
    public boolean arePumpSettingsSet()
    {
        return false;
    }

    @Override
    public boolean areConnectionParametersValid()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean areConnectionParametersValid(String param)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getConnectionParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeviceSpecialConfigPanelInterface getSpecialConfigPanel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasNoConnectionParameters()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasSpecialConfig()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void initSpecialConfig()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setConnectionParameters(String param)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void createCommands()
    {
        // CONTROL COMMAND
        util.addCommand(MinimedCommand.COMMAND_ACK, new MinimedCommand(MinimedCommand.COMMAND_ACK, "MM_ACKNOWLEDGE"));
        util.addCommand(MinimedCommand.COMMAND_KEYPAD_PUSH_ACK,
            new MinimedCommand(91, "MM_KEYPAD_PUSH_ACK", util.getParamatersArray(1, 2), 1));
        util.addCommand(MinimedCommand.COMMAND_KEYPAD_PUSH_ESC,
            new MinimedCommand(91, "MM_KEYPAD_PUSH_ESC", util.getParamatersArray(1, 1), 1));
        util.addCommand(MinimedCommand.READ_PUMP_ERROR_STATUS, new MinimedCommand(117, "MM_READ_PUMP_ERROR_STATUS"));
        util.addCommand(MinimedCommand.READ_REMOTE_CONTROL_IDS, new MinimedCommand(118,
                "MM_CMD_READ_REMOTE_CONTROL_IDS"));
        util.addCommand(MinimedCommand.READ_PUMP_STATE, new MinimedCommand(131, "MM_READ_PUMP_STATE"));
        util.addCommand(MinimedCommand.SET_RF_POWER_ON, new MinimedCommand(MinimedCommand.SET_RF_POWER_ON,
                "MM_SET_RF_POWER_ON", 0));
        util.getCommand(MinimedCommand.SET_RF_POWER_ON).command_parameters = util.getParamatersArray(2, 1, 10);
        util.getCommand(MinimedCommand.SET_RF_POWER_ON).command_parameters_count = 2;
        util.getCommand(MinimedCommand.SET_RF_POWER_ON).max_allowed_time = 17000;
        util.addCommand(MinimedCommand.SET_RF_POWER_OFF,
            new MinimedCommand(93, "MM_SET_RF_POWER_OFF", util.getParamatersArray(2, 0, 0), 2));
        util.addCommand(MinimedCommand.SET_SUSPEND, new MinimedCommand(MinimedCommand.CANCEL_SUSPEND,
                "MM_CANCEL_SUSPEND", util.getParamatersArray(1, 1), 1));
        util.addCommand(MinimedCommand.CANCEL_SUSPEND, new MinimedCommand(MinimedCommand.CANCEL_SUSPEND,
                "MM_CANCEL_SUSPEND", util.getParamatersArray(1, 0), 1));
        util.addCommand(MinimedCommand.DETECT_BOLUS,
            new MinimedCommand(75, "MM_DETECT_BOLUS", util.getParamatersArray(3, 0, 0, 0), 3));
        util.addCommand(MinimedCommand.READ_FIRMWARE_VERSION, new MinimedCommand(MinimedCommand.READ_FIRMWARE_VERSION,
                "MM_READ_FIRMWARE_VERSION"));
        util.addCommand(MinimedCommand.READ_PUMP_ID, new MinimedCommand(113, "MM_READ_PUMP_ID"));
        util.addCommand(MinimedCommand.READ_TEMPORARY_BASAL, new MinimedCommand(120, "MM_READ_TEMPORARY_BASAL"));

        // PUMP SETTINGS / STATUS
        util.addCommand(MinimedCommand.READ_REAL_TIME_CLOCK, new MinimedCommand(112, "Read Real Time Clock"));
        util.addCommand(MinimedCommand.SETTINGS, new MinimedCommand(127, "MM_COMMAND_READ_CURRENT_SETTINGS"));
        util.addCommand(MinimedCommand.READ_BATTERY_STATUS, new MinimedCommand(114, "Read Battery Status"));
        util.addCommand(MinimedCommand.READ_REMAINING_INSULIN, new MinimedCommand(115, "Read Remaining Insulin"));

        // PUMP DATA COMMANDS
        util.addCommand(MinimedCommand.HISTORY_DATA, new MinimedCommandHistoryData());
        util.addCommand(MinimedCommand.READ_PROFILES_STD_DATA, new MinimedCommand(122, "Read Standard Profiles Data",
                128, 1, 8));
        util.addCommand(MinimedCommand.READ_PROFILES_A_DATA, new MinimedCommand(123, "Read Profiles A Data", 128, 1, 9));
        util.addCommand(MinimedCommand.READ_PROFILES_B_DATA,
            new MinimedCommand(124, "Read Profiles B Data", 128, 1, 10));

        // REMOVED COMMANDS
        /*
         * util.addCommand(MinimedCommand.ENABLE_DETAIL_TRACE, new
         * MinimedCommand(160, "Enable Detail Trace", util.getParamatersArray(1,
         * 1), 1));
         * util.addCommand(MinimedCommand.DISABLE_DETAIL_TRACE, new
         * MinimedCommand(160, "Disable Detail Trace",
         * util.getParamatersArray(1, 0), 1));
         * util.addCommand(MinimedCommand.READ_PUMP_TRACE, new
         * MinimedCommand(163, "Read Pump Trace", 1024, 49, 0));
         * util.addCommand(MinimedCommand.READ_DETAIL_TRACE, new
         * MinimedCommand(164, "Read Detail Trace", 1024, 11, 0));
         * util.addCommand(MinimedCommand.READ_NEW_ALARM_TRACE, new
         * MinimedCommand(166, "Read New Alarm Trace", 1024, 11, 0));
         * util.addCommand(MinimedCommand.READ_OLD_ALARM_TRACE, new
         * MinimedCommand(167, "Read Old Alarm Trace", 1024, 11, 0));
         * util.addCommand(MinimedCommand.READ_TODAYS_TOTAL_INSULIN, new
         * MinimedCommand(121, "Read Today's Total Insulin"));
         */

    }

    @Override
    public Object convertDeviceReply(MinimedCommand mc)
    {

        switch (mc.command_code)
        {
            case 127: // '\177'
                return convertCurrentSettings(mc);

            case MinimedCommand.SET_TEMPORARY_BASAL:
                return this.convertTempBasal(mc, mc.reply.raw_data);

            case MinimedCommand.READ_FIRMWARE_VERSION:
            case MinimedCommand.READ_PUMP_ID:
            case MinimedCommand.READ_PUMP_STATE:
            default:
                return this.util.decoder.decode(mc);

        }

    }

    public boolean convertCurrentSettings(MinimedCommand cmd)
    {
        // System.out.println("ERROR: Decode Current Settings (Minimed511)");
        log.debug("convertCurrentSettings");

        int rd[] = cmd.reply.raw_data;

        util.config.addSetting("MM_SETTINGS_AUTO_OFF_DURATION_HRS", "" + rd[0]);

        int i = rd[1];

        if (rd[0] == 4)
        {
            util.config.addSetting("MM_SETTINGS_ALARM_MODE", "MM_ALARM_MODE_SILENT");
        }
        else
        {
            util.config.addSetting("MM_SETTINGS_ALARM_MODE", "MM_ALARM_MODE_NORMAL");
            util.config.addSetting("MM_SETTINGS_ALARM_BEEP_VOLUME", "" + rd[0]);
        }

        util.config.addSetting("MM_SETTINGS_EASY_AUDIO_BOLUS_ACTIVE", util.decoder.parseResultEnable(rd[2]));

        if (rd[2] == 1)
        {
            util.config.addSetting("MM_SETTINGS_EASY_AUDIO_BOLUS_STEP_SIZE", "" + util.decoder.toBolusInsulin(rd[3]));
        }

        util.config.addSetting("MM_SETTINGS_VARIABLE_BOLUS_ENABLED", util.decoder.parseResultEnable(rd[4]));
        util.config.addSetting("MM_SETTINGS_MAX_BOLUS", "" + convertMaxBolus(rd));
        util.config.addSetting(
            "MM_SETTINGS_MAX_BASAL_RATE",
            ""
                    + this.util.decoder.toBasalInsulin(this.util.getHexUtils().makeUnsignedShort(
                        rd[getSettingIndexMaxBasal()], rd[getSettingIndexMaxBasal() + 1])));
        util.config.addSetting("MM_SETTINGS_TIME_FORMAT", rd[getSettingIndexTimeDisplayFormat()] != 1 ? "12h" : "24h");
        util.config.addSetting("MM_SETTINGS_INSULIN_CONCENTRATION", "" + (rd[9] != 0 ? 50 : 100));
        util.config.addSetting("MM_SETTINGS_BASAL_PATTERN_ENABLED", util.decoder.parseResultEnable(rd[10]));

        if (rd[10] == 1)
        {
            String patt = "";
            switch (rd[11])
            {
                case 0:
                    patt = "MM_PATTERN_STD";
                    break;

                case 1:
                    patt = "MM_PATTERN_A";
                    break;

                case 2:
                    patt = "MM_PATTERN_B";
                    break;

                default:
                    patt = "MM_PATTERN_UNKNOWN";
                    break;
            }

            util.config.addSetting("MM_SETTINGS_BASAL_PATTERN", patt);

        }

        util.config.addSetting("MM_SETTINGS_RF_ENABLED", util.decoder.parseResultEnable(rd[12]));
        util.config.addSetting("MM_SETTINGS_BLOCK_ENABLED", util.decoder.parseResultEnable(rd[13]));

        return true;

    }

    int getSettingIndexMaxBasal()
    {
        return 6;
    }

    int getSettingIndexTimeDisplayFormat()
    {
        return 8;
    }

    public double convertMaxBolus(int ai[])
    {
        return util.decoder.toBolusInsulin(ai[5]);
    }

    public int convertTempBasal(MinimedCommand command, int ai[])
    {
        int i = this.util.getHexUtils().makeUnsignedShort(ai[0], ai[1]);
        util.config.addSetting("MM_TEMP_BASAL_RATE", "" + util.decoder.toBasalInsulin(i));
        i = this.util.getHexUtils().makeUnsignedShort(ai[2], ai[3]);
        util.config.addSetting("MM_TEMP_BASAL_DURATION", "" + i);
        log.info("decodeTempBasal: Temp Basal Rate = " + this.util.config.settings.get("MM_TEMP_BASAL_RATE"));
        log.info("decodeTempBasal: Temp Basal Remain Dur = " + this.util.config.settings.get("MM_TEMP_BASAL_DURATION"));
        return i;
    }

    /**
     * Get Unknown Settings
     * 
     * @param mc
     * @return
     */
    public Object getUnknownSettings(MinimedCommand mc)
    {
        this.util.decoder.debugResult(mc);
        return null;
    }

}
