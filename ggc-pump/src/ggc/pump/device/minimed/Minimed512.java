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

public class Minimed512 extends Minimed511
{
    private static Log log = LogFactory.getLog(Minimed512.class);

    // MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();

    /**
     * Constructor 
     */
    /*
     * public Minimed512()
     * {
     * super();
     * }
     */

    /**
     * Constructor
     *  
     * @param da - data access instance
     * @param device_type - device type 
     * @param full_port - full port identification 
     * @param writer - output writer instance
     */
    public Minimed512(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
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
    public Minimed512(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_512, full_port, writer);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public Minimed512(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    /** 
     * initDeviceSpecific - Device specific init
     */
    @Override
    public void initDeviceSpecific()
    {
        super.initDeviceSpecific();

        // util = MinimedDeviceUtil.getInstance();
        util.config.comm_delay_io = 4;
        util.config.comm_baudrate = 56000;

        util.config.strokes_per_basal_unit = 40;
        util.config.strokes_per_bolus_unit = 10;

        log.debug("Initx");

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
    @Override
    public String getName()
    {
        return "Minimed 512/712 515/715";
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    @Override
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
    @Override
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_512;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    @Override
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
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    @Override
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
    public int getMinimedDeviceId()
    {
        return MinimedDevicesIds.PUMP_MINIMED_512;
    }

    /** 
     * createCommands - Create commands for reading
     */
    @Override
    public void createCommands()
    {
        super.createCommands();

        // CONTROL COMMAND
        util.addCommand(MinimedCommand.READ_TEMPORARY_BASAL, new MinimedCommand(152, "Read Temporary Basal"));
        util.addCommand(MinimedCommand.SET_TEMPORARY_BASAL, new MinimedCommand(76,
                "Set Temp Basal Rate (bolus detection only)", 3));
        util.getCommand(MinimedCommand.SET_TEMPORARY_BASAL).command_parameters = util.getParamatersArray(3, 0, 0, 0);
        util.getCommand(MinimedCommand.SET_TEMPORARY_BASAL).allowed_retries = 0;

        // SETTINGS
        util.addCommand(MinimedCommand.READ_PUMP_MODEL, new MinimedCommand(141, "Read Pump Model"));
        util.addCommand(MinimedCommand.READ_BG_TARGETS, new MinimedCommand(140, "Read BG Targets"));
        util.addCommand(MinimedCommand.READ_BG_UNITS, new MinimedCommand(137, "Read BG Units"));
        util.addCommand(MinimedCommand.READ_LANGUAGE, new MinimedCommand(134, "Read Language"));

        // PUMP SETTINGS
        util.addCommand(MinimedCommand.SETTINGS, new MinimedCommand(145, "MM_COMMAND_READ_CURRENT_SETTINGS"));
        util.addCommand(MinimedCommand.READ_BG_ALARM_CLOCKS, new MinimedCommand(142, "Read BG Alarm Clocks"));
        util.addCommand(MinimedCommand.READ_BG_ALARM_ENABLE, new MinimedCommand(151, "Read BG Alarm Enable"));
        util.addCommand(MinimedCommand.READ_BG_REMINDER_ENABLE, new MinimedCommand(144, "Read BG Reminder Enable"));
        util.addCommand(MinimedCommand.READ_INSULIN_SENSITIVITES, new MinimedCommand(139, "Read Insulin Sensitivities"));

        // m_cmdReadBGTargets = new MM511.Command( 140, "Read BG Targets");

        // PUMP DATA COMMANDS
        util.addCommand(MinimedCommand.HISTORY_DATA, new MinimedCommandHistoryData(36));
        util.addCommand(MinimedCommand.READ_PROFILES_STD_DATA, new MinimedCommand(146, "Read Standard Profiles Data",
                192, 1, 8));
        util.addCommand(MinimedCommand.READ_PROFILES_A_DATA, new MinimedCommand(147, "Read Profiles A Data", 192, 1, 9));
        util.addCommand(MinimedCommand.READ_PROFILES_B_DATA,
            new MinimedCommand(148, "Read Profiles B Data", 192, 1, 10));
        util.addCommand(MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS, new MinimedCommand(135,
                "Read Bolus Wizard Setup Status"));
        util.addCommand(MinimedCommand.READ_CARBOHYDRATE_RATIOS, new MinimedCommand(138, "Read Carbohydrate Ratios"));
        util.addCommand(MinimedCommand.READ_CARBOHYDRATE_UNITS, new MinimedCommand(136, "Read Carbohydrate Units"));

        // REMOVED COMMANDS
        // util.addCommand(MinimedCommand.READ_RESERVOIR_WARNING, new
        // MinimedCommand(143, "Read Reservoir Warning"));
        // util.addCommand(MinimedCommand.READ_PARADIGMLINK_IDS, new
        // MinimedCommand( 149, "Read ParadigmLink Ids"));

        // 145

    }

    @Override
    public Object convertDeviceReply(MinimedCommand mc)
    {
        log.debug("convertDeviceReply [code=" + mc.command_code + ",desc=" + mc.command_description + "]");

        switch (mc.command_code)
        {

            case MinimedCommand.READ_BG_ALARM_CLOCKS:
            case MinimedCommand.READ_BG_ALARM_ENABLE:

                // case MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS:
                // case MinimedCommand.READ_CARBOHYDRATE_RATIOS:
                // case MinimedCommand.READ_CARBOHYDRATE_UNITS:
            case MinimedCommand.READ_BG_TARGETS:
                return this.getUnknownSettings(mc);
                /*
                 * case 141:
                 * decodeModelNumber(command.m_rawData);
                 * break;
                 * default:
                 * super.decodeReply(command);
                 * break;
                 */

            case 152:
                return this.convertTempBasal(mc, mc.reply.raw_data);

            case 145:
                return convertCurrentSettings(mc);

            case MinimedCommand.READ_CARBOHYDRATE_RATIOS:
            case MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS:
            case MinimedCommand.READ_INSULIN_SENSITIVITES:
            case MinimedCommand.READ_CARBOHYDRATE_UNITS:
            case MinimedCommand.READ_BG_REMINDER_ENABLE:
            case MinimedCommand.READ_PUMP_MODEL:
            case MinimedCommand.READ_BG_UNITS:
            case MinimedCommand.READ_LANGUAGE:
                return util.decoder.decode(mc);

            default:
                return super.convertDeviceReply(mc);

        }

    }

    @Override
    public boolean convertCurrentSettings(MinimedCommand cmd)
    {
        System.out.println("Settings");
        log.debug("convertCurrentSettings");

        super.convertCurrentSettings(cmd);

        int rd[] = cmd.reply.raw_data;

        util.config.addSetting("MM_SETTINGS_TEMP_BASAL_TYPE", rd[14] != 0 ? "MM_TEMP_BASAL_TYPE_PERCENT"
                : "MM_TEMP_BASAL_TYPE_UNITS");

        if (rd[14] == 1)
        {
            util.config.addSetting("MM_SETTINGS_TEMP_BASAL_PERCENT", "" + rd[15]);
        }

        util.config.addSetting("MM_SETTINGS_PARADIGM_LINK_ENABLE", util.decoder.parseResultEnable(rd[16]));

        convertInsulinActionSetting(rd);

        return true;

    }

    public void convertInsulinActionSetting(int ai[])
    {
        util.config.addSetting("MM_SETTINGS_INSULIN_ACTION_TYPE", ai[17] != 0 ? "MM_INSULIN_ACTION_TYPE_REGULAR"
                : "MM_INSULIN_ACTION_TYPE_FAST");
    }

    @Override
    public int convertTempBasal(MinimedCommand command, int ai[])
    {
        int i = this.util.getHexUtils().makeUnsignedShort(ai[2], ai[3]);
        util.config.addSetting("MM_TEMP_BASAL_RATE", "" + util.decoder.toBasalInsulin(i));
        i = this.util.getHexUtils().makeUnsignedShort(ai[4], ai[5]);
        util.config.addSetting("MM_TEMP_BASAL_DURATION", "" + i);
        log.info("decodeTempBasal: Temp Basal Rate = " + this.util.config.settings.get("MM_TEMP_BASAL_RATE"));
        log.info("decodeTempBasal: Temp Basal Remain Dur = " + this.util.config.settings.get("MM_TEMP_BASAL_DURATION"));
        return i;
    }

}
