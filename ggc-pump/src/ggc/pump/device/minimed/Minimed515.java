package ggc.pump.device.minimed;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.minimed.MinimedDevicesIds;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.manager.PumpDevicesIds;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
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
 * Filename: Minimed512 Description: Minimed 512/712 implementation (just
 * settings)
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class Minimed515 extends Minimed512
{

    private static Log log = LogFactory.getLog(Minimed515.class);

    // protected MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();

    /**
     * Constructor
     */
    /*
     * public Minimed512() { super(); }
     */

    /**
     * Constructor
     * 
     * @param da
     *            - data access instance
     * @param device_type
     *            - device type
     * @param full_port
     *            - full port identification
     * @param writer
     *            - output writer instance
     */
    public Minimed515(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, device_type, full_port, writer);
    }

    /**
     * Constructor
     * 
     * @param da
     *            - data access instance
     * @param full_port
     *            - full port identification
     * @param writer
     *            - output writer instance
     */
    public Minimed515(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_515, full_port, writer);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public Minimed515(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    @Override
    public void initDeviceSpecific()
    {
        super.initDeviceSpecific();

        util.config.comm_delay_io = 4;
        util.config.comm_baudrate = 56000;

        // m_baudRate = 10;
        // m_ioDelayMS = 4;
        /*
         * setReceiveBufferSize(16384); setTransmitBufferSize(2048);
         * setBaudRate(i); m_serialConfig.setDataBits(3);
         * m_serialConfig.setStopBits(0); m_serialConfig.setParity(0);
         * m_serialConfig.setHandshake(0);
         * Contract.pre(i == 7 || i == 8 || i == 9 || i == 10,
         * "bad baudRate value of " + i + "; must be " + 7 + ", " + 8 + ", " + 9
         * + " or " + 10); m_serialConfig.setBitRate(i);
         * public static final int HS_NONE = 0; public static final int
         * HS_XONXOFF = 1; public static final int HS_CTSRTS = 2; public static
         * final int HS_CTSDTR = 2; public static final int HS_DSRDTR = 3;
         * public static final int HS_HARD_IN = 16; public static final int
         * HS_HARD_OUT = 32; public static final int HS_SOFT_IN = 64; public
         * static final int HS_SOFT_OUT = 128; public static final int
         * HS_SPLIT_MASK = 240; private static final String handshakeNames[] = {
         * "NONE", "XON-XOFF", "CTS-RTS", "DSR-DTR" }; public static final int
         * BR_110 = 0; public static final int BR_150 = 1; public static final
         * int BR_300 = 2; public static final int BR_600 = 3; public static
         * final int BR_1200 = 4; public static final int BR_2400 = 5; public
         * static final int BR_4800 = 6; public static final int BR_9600 = 7;
         * public static final int BR_19200 = 8; public static final int
         * BR_38400 = 9; public static final int BR_57600 = 10; public static
         * final int BR_115200 = 11; public static final int BR_230400 = 12;
         * public static final int BR_460800 = 13; private static final int
         * bitRateNumbers[] = { 110, 150, 300, 600, 1200, 2400, 4800, 9600,
         * 19200, 38400, 57600, 0x1c200, 0x38400, 0x70800 }; public static final
         * int PY_NONE = 0; public static final int PY_ODD = 1; public static
         * final int PY_EVEN = 2; public static final int PY_MARK = 3; public
         * static final int PY_SPACE = 4; private static final String
         * parityNames[] = { "NONE", "ODD", "EVEN", "MARK", "SPACE" }; public
         * static final int LN_5BITS = 0; public static final int LN_6BITS = 1;
         * public static final int LN_7BITS = 2; public static final int
         * LN_8BITS = 3; public static final int ST_1BITS = 0; public static
         * final int ST_2BITS = 1; public static final int PWR_STANDBY = 0;
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
     * getDeviceId - Get Device Id, within MgrCompany class Should be
     * implemented by device class.
     * 
     * @return id of device within company
     */
    @Override
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_515;
    }

    /**
     * getInstructions - get instructions for device Should be implemented by
     * meter class.
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
     * getImplementationStatus - Get Implementation Status
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    @Override
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }

    /**
     * getDeviceClassName - Get Class name of device implementation, used by
     * Reflection at later time
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
     * Get Temporary Basal Type Definition "TYPE=Unit;STEP=0.1"
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
        return MinimedDevicesIds.PUMP_MINIMED_515;
    }

    @Override
    public void createCommands()
    {
        super.createCommands();

        // CONTROL COMMAND
        util.addCommand(MinimedCommand.READ_CURRENT_PUMP_STATUS, new MinimedCommand(206, "Read Current Pump Status")); // detect
                                                                                                                       // bolus
                                                                                                                       // ??
        // m_cmdDetectBolus.m_maxRetries = 0;

        // PUMP SETTINGS
        util.addCommand(MinimedCommand.SETTINGS, new MinimedCommand(192, "MM_COMMAND_READ_CURRENT_SETTINGS"));
        util.addCommand(MinimedCommand.READ_BG_TARGETS, new MinimedCommand(159, "Read BG Target Ranges"));
        // util.addCommand(MinimedCommand.READ_SAVE_SETTINGS_DATE, new
        // MinimedCommand( 193, "Read Saved Settings Date"));
        util.addCommand(MinimedCommand.READ_MISSED_BOLUS_REMINDER_ENABLE, new MinimedCommand(197,
                "Read Bolus Reminder On/Off"));
        util.addCommand(MinimedCommand.READ_MISSED_BOLUS_REMINDERS, new MinimedCommand(198, "Read Bolus Reminders"));
        // util.addCommand(MinimedCommand.READ_FACTORY_PARAMETERS , new
        // MinimedCommand(199, "Read Factory Parameters"));

        // PUMP DATA COMMANDS
        // util.addCommand(MinimedCommand.READ_CURRENT_HISTORY_PAGE_NUMBER, new
        // MinimedCommand( 157, "Read Current History Download Page Number"));

        // REMOVED COMMANDS
        // util.addCommand(MinimedCommand.READ_CONTRAST, new MinimedCommand(
        // 195, "Read Contrast"));

    }

    @Override
    public Object convertDeviceReply(MinimedCommand mc)
    {
        // log.debug("convertDeviceReply [code=" + mc.command_code + ",desc=" +
        // mc.command_description + "]");

        switch (mc.command_code)
        {

        // case 145:

        /*
         * case 157:
         * m_cmdReadHistoryData.m_maxRecords =
         * decodeCurrentHistoryPageNumber(command.m_rawData);
         * m_cmdReadHistoryData.allocateRawData();
         * calcTotalBytesToRead();
         * break;
         * case 206:
         * decodePumpStatus(command.m_rawData);
         * break;
         */

            case MinimedCommand.READ_MISSED_BOLUS_REMINDER_ENABLE:
            case MinimedCommand.READ_MISSED_BOLUS_REMINDERS:

                // return this.getUnknownSettings(mc);

            case 159:
                return this.util.decoder.decode(mc);

            case 192:
                return convertCurrentSettings(mc);

            default:
                return super.convertDeviceReply(mc);

        }

    }

    @Override
    public boolean convertCurrentSettings(MinimedCommand cmd)
    {
        super.convertCurrentSettings(cmd);

        log.debug("convertCurrentSettings");

        util.config.addSetting("MM_SETTINGS_RESERVOIR_WARNING_TYPE",
            cmd.reply.raw_data[18] != 0 ? "MM_RESERVOIR_WARNING_TYPE_TIME" : "MM_RESERVOIR_WARNING_TYPE_UNITS");
        util.config.addSetting("MM_SETTINGS_RESERVOIR_WARNING_POINT", "" + cmd.reply.raw_data[19]);
        util.config.addSetting("MM_SETTINGS_KEYPAD_LOCKED", util.decoder.parseResultEnable(cmd.reply.raw_data[20]));

        return true;
    }

    @Override
    public void convertInsulinActionSetting(int ai[])
    {
        int i = ai[17];
        String s = "";

        if (i == 0 || i == 1)
        {
            s = ai[17] != 0 ? "MM_INSULIN_ACTION_TYPE_REGULAR" : "MM_INSULIN_ACTION_TYPE_FAST";
        }
        else
        {
            if (i == 15)
            {
                s = "MM_INSULIN_ACTION_TYPE_UNSET";
            }
            else
            {
                s = "MM_INSULIN_ACTION_CURVE__" + i + "__";
            }
        }

        util.config.addSetting("MM_SETTINGS_INSULIN_ACTION_TYPE", s);
    }

    /*
     * private void decodePumpStatus(int ai[])
     * throws BadDeviceValueException
     * {
     * Contract.pre(ai != null);
     * Contract.pre(ai.length == 64);
     * m_pumpStatusBolusing = MedicalDevice.Util.parseEnable(ai[1],
     * "bolusing flag");
     * m_pumpStatusSuspended = MedicalDevice.Util.parseEnable(ai[2],
     * "suspended flag");
     * log.info( (new
     * StringBuilder()).append("decodePumpStatus: bolusing=").append
     * (m_pumpStatusBolusing
     * ).append(", suspended=").append(m_pumpStatusSuspended).toString());
     * }
     * private int decodeCurrentHistoryPageNumber(int ai[])
     * throws BadDeviceValueException
     * {
     * Contract.pre(ai != null);
     * Contract.pre(ai.length == 64);
     * m_currentHistoryPageNumber = (int)MedicalDevice.Util.makeLong(ai[0],
     * ai[1], ai[2], ai[3]);
     * int i = (m_currentHistoryPageNumber - (int)m_lastHistoryPageNumber) + 1;
     * if(i <= 0 || i > 36)
     * i = 36;
     * log.info( (new StringBuilder()).append(
     * "decodeCurrentHistoryPageNumber: current page number (pump) = "
     * ).append(m_currentHistoryPageNumber
     * ).append(", last page number (system) = "
     * ).append(m_lastHistoryPageNumber)
     * .append(", pages to read = ").append(i).toString());
     * return i;
     * }
     */

}
