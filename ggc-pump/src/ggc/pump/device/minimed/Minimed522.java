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

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: Pump Tool (support for Pump devices)
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
 * Filename: Minimed522
 * Description: Minimed 522/722 implementation (just settings)
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class Minimed522 extends Minimed515
{
    private static Log log = LogFactory.getLog(Minimed522.class);

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
    public Minimed522(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
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
    public Minimed522(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_522, full_port, writer);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public Minimed522(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    @Override
    public void initDeviceSpecific()
    {
        super.initDeviceSpecific();

        // util.config.comm_delay_io = 4;
        // util.config.comm_baudrate = 56000;

        util.config.comm_delay_io = 250;
        util.config.comm_baudrate = 57600;

        util.config.comm_data_bits = SerialPort.DATABITS_8;
        util.config.comm_flowcontrol = SerialPort.FLOWCONTROL_NONE;
        util.config.comm_parity = SerialPort.PARITY_NONE;
        util.config.comm_stop_bits = SerialPort.STOPBITS_1;

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
        return "Minimed 522/722";
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    @Override
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
    @Override
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_522;
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
        return "INSTRUCTIONS_MINIMED_508";
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
        return "ggc.pump.device.minimed.Minimed522";
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
    public int getMinimedDeviceId()
    {
        return MinimedDevicesIds.PUMP_MINIMED_522;
    }

    @Override
    public void createCommands()
    {
        super.createCommands();

        // CGMS SETTINGS COMMANDS
        util.addCommand(MinimedCommand.READ_CALIBRATION_FACTOR, new MinimedCommand(156, "Read Calibration Factor"));
        util.addCommand(MinimedCommand.READ_SENSOR_SETTINGS, new MinimedCommand(153, "Read Sensor Settings"));
        // new MinimedCommand( 205, "Read Current Glucose History Page Number");

        // CGMS DATA COMMANDS
        util.addCommand(MinimedCommand.READ_GLUCOSE_HISTORY, new MinimedCommandHistoryCGMS(154, "Read Glucose History",
                1024, 32));
        util.addCommand(MinimedCommand.READ_ISIG_HISTORY, new MinimedCommandHistoryCGMS(155, "Read Isig History", 2048,
                32));

        // REMOVED COMMANDS
        // new MinimedCommand(40, "Write Glucose History Timestamp", 0);
        // new CommandHistoryDataFilter(168, "Filter Glucose History Data",
        // (MM511.CommandHistoryData)m_cmdReadGlucoseHistoryData);
        // new CommandHistoryDataFilter(169, "Filter Isig History Data",
        // (M511.CommandHistoryData)m_cmdReadIsigHistoryData);

    }

    @Override
    public Object convertDeviceReply(MinimedCommand mc)
    {
        // log.debug("convertDeviceReply [code=" + mc.command_code + ",desc=" +
        // mc.command_description + "]");

        switch (mc.command_code)
        {

            case MinimedCommand.READ_CALIBRATION_FACTOR: // = 156;
                return this.convertCalibrationFactor(mc);

            case MinimedCommand.READ_SENSOR_SETTINGS: // = 153;
                return this.util.decoder.decode(mc);

                /*
                 * case 205:
                 * decodeCurrentGlucoseHistoryPageNumber(command.m_rawData);
                 * break;
                 * case 156:
                 * decodeCalibrationFactor(command.m_rawData);
                 * break;
                 */

            default:
                return super.convertDeviceReply(mc);

        }

    }

    public String convertCalibrationFactor(MinimedCommand cmd)
    {
        util.decoder.debugResult(cmd);

        int i = this.util.getHexUtils().makeInt(cmd.reply.raw_data[0], cmd.reply.raw_data[1]);
        double cal_f = i / 1000.0d;
        // m_calibrationFactor = (double)i / 1000D;
        // log.info( (new
        // StringBuilder()).append("decodeCalibrationFactor: factor=").append(m_calibrationFactor).toString());

        util.config.addSetting("SENSOR_CALIBRATION_FACTOR", "" + cal_f);

        return "" + cal_f;
    }

    /*
     * public boolean convertSensorSettings(MinimedCommand cmd)
     * {
     * HexUtils hu = this.util.getHexUtils();
     * log.debug("Decoded reply: " + hu.getHexCompact(cmd.reply.raw_data));
     * util.config.addSetting("SENSOR_ENABLE",
     * util.decoder.parseResultEnable(cmd.reply.raw_data[0]));
     * int bg_units = cmd.reply.raw_data[22];
     * util.config.addSetting("SENSOR_BG_UNITS",
     * util.decoder.getBGUnitName(bg_units));
     * util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_ENABLE",
     * util.decoder.parseResultEnable(cmd.reply.raw_data[1]));
     * int i = hu.makeInt(cmd.reply.raw_data[2], cmd.reply.raw_data[3]);
     * if (bg_units == 2)
     * util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_VALUE", "" +
     * util.decoder.toWholeUnits(i));
     * else
     * util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_VALUE", "" + i);
     * util.config.addSetting("SENSOR_HIGH_GLUCOSE_SNOOZE_TIME", "" +
     * hu.makeInt(cmd.reply.raw_data[4], cmd.reply.raw_data[5]));
     * util.config.addSetting("SENSOR_LOW_GLUCOSE_LIMIT_ENABLE",
     * util.decoder.parseResultEnable(cmd.reply.raw_data[6]));
     * i = hu.makeInt(cmd.reply.raw_data[7], cmd.reply.raw_data[8]);
     * if (bg_units == 2)
     * util.config.addSetting("SENSOR_LOW_GLUCOSE_LIMIT_VALUE", "" +
     * util.decoder.toWholeUnits(i));
     * else
     * util.config.addSetting("SENSOR_LOW_GLUCOSE_LIMIT_VALUE", "" + i);
     * util.config.addSetting("SENSOR_LOW_GLUCOSE_SNOOZE_TIME", "" +
     * hu.makeInt(cmd.reply.raw_data[9], cmd.reply.raw_data[10]));
     * util.config.addSetting("SENSOR_ALARM_SNOOZE_TIME", "" +
     * hu.makeInt(cmd.reply.raw_data[14], cmd.reply.raw_data[15]));
     * util.config.addSetting("SENSOR_CALIBRATION_REMINDER_ENABLE",
     * util.decoder.parseResultEnable(cmd.reply.raw_data[16]));
     * util.config.addSetting("SENSOR_CALIBRATION_REMINDER_TIME", "" +
     * hu.makeInt(cmd.reply.raw_data[17], cmd.reply.raw_data[18]));
     * util.config.addSetting("SENSOR_TRANSMITER_ID", "" +
     * hu.makeInt(cmd.reply.raw_data[19], cmd.reply.raw_data[20],
     * cmd.reply.raw_data[21]));
     * util.config.addSetting("SENSOR_MISSED_DATA_TIME", "" +
     * hu.makeInt(cmd.reply.raw_data[23], cmd.reply.raw_data[24]));
     * return true;
     * }
     */

}
