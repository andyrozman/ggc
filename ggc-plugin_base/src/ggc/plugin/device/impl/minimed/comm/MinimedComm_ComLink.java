package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CRCUtils;

// NOTE: This is only class that will be implemented for first phase of MiniMed testing

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
 *  Filename:     MinimedComm_ComLink  
 *  Description:  Communication Protocol: COM Link
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedComm_ComLink extends SerialProtocol implements MinimedComm_Interface // extends
                                                                                         // MinimedComm_Base
{

    // private String[] comm_settings = {};
    private static Log log = LogFactory.getLog(MinimedComm_ComLink.class);

    private static int COMLINK_ENCODING_PROTOCOL[] = { 21, 49, 50, 35, 52, 37, 38, 22, 26, 25, 42, 11, 44, 13, 14, 28 };
    MinimedDeviceUtil util = MinimedDeviceUtil.getInstance();
    CRCUtils hex_utils = new CRCUtils();
    // DataAccessPump dataAccess = DataAccessPump.getInstance();
    I18nControlAbstract m_ic = dataAccess.getI18nControlInstance();

    // MinimedCommandResponse mcr;
    MinimedDevice device;

    boolean low_level_debug = false;

    /**
     * Constructor
     * 
     * @param mmd device instance 
     */
    public MinimedComm_ComLink(MinimedDevice mmd)
    {
        super(mmd.getDataAccess());

        // super(port, serial_number);
        // mcr = new MinimedCommandResponse();
        this.device = mmd;
    }

    public int initializeCommunicationInterface() throws PlugInBaseException
    {
        // FIXME
        log.debug("initializeCommunicationInterface (ComLink)");

        for (int i = 0; i <= 4; i++)
        {
            try
            {
                initCommunications();
                break;
            }
            catch (IOException ex)
            {
                if (i == 4)
                {
                    log.warn("Error on initialize. Ex.: " + ex, ex);
                    throw new PlugInBaseException(ex);
                }
                else
                {
                    log.warn("Error on initialize. Retrying. Ex.: " + ex, ex);
                }
            }
        }

        return 0;
    }

    private void initCommunications() throws PlugInBaseException, IOException // ,
                                                                              // SerialIOHaltedException
    {
        log.info("InitCommunications (ComLink)");

        createSerialPort();
        readUntilEmpty();

        sendToDeviceCheckReply((byte) 6, (byte) 51);

        try
        {
            // log.debug("initCommunicationsIO: before status");
            int i = readStatusOfDevice();

            // FIXME we might need to dump before, wasnt needed so far
            // System.out.println("Status: " + i);
            /*
             * if(i > 0)
             * {
             * // FIXME
             * log.warn("initCommunicationsIO: dumping " + i + " bytes.");
             * this.write((byte)8);
             * //sendTransferDataCommand();
             * int ai[] = new int[i];
             * read(ai);
             * System.out.println("ai: " + ai);
             * readFromDeviceAckByte();
             * }
             */
        }

        catch (IOException ex)
        {
            throw new PlugInBaseException("Error on init communication: " + ex, ex);
        }
    }

    private void createSerialPort() throws PlugInBaseException
    {
        log.debug("Create Serial Port");

        for (int i = 0; i < 4; i++)
        {
            try
            {
                this.setPort(util.port);
                this.setCommunicationSettings(util.config.comm_baudrate, util.config.comm_data_bits,
                    util.config.comm_stop_bits, util.config.comm_parity, util.config.comm_flowcontrol, 0);
                open();
                this.setTimeout(2000);
            }
            catch (PlugInBaseException ex)
            {
                if (i == 3) // ++j >= 0)
                    throw ex;
                log.info("createSerialPort: waiting for port to become available...e=" + ex);
                util.sleepMs(5000);
            }
            /*
             * catch (UnsupportedCommOperationException ex)
             * {
             * if (i==3)
             * throw new PlugInBaseException(ex);
             * log.info(
             * "createSerialPort: waiting for port to become available...e=" +
             * ex);
             * util.sleepMs(5000);
             * }
             */
            catch (Exception ex)
            {
                if (i == 3)
                    throw new PlugInBaseException(ex);
                log.info("createSerialPort: waiting for port to become available...e=" + ex);
                util.sleepMs(5000);
            }
        }

        this.readUntilEmpty();
        util.sleepIO();

    }

    public int initDevice() throws PlugInBaseException
    {

        MinimedCommand mdc = null;

        log.debug("initDevice - Start");

        // ---
        // --- Command: Set RF Power On
        // ---
        executeCommandRetry(MinimedCommand.SET_RF_POWER_ON);

        if (this.util.isCommunicationStopped())
            return -1;

        // ---
        // --- Command: Read Pump Error Status
        // ---
        /*
         * //int error_code = -1;
         * int param_int = -1;
         * try
         * {
         * // 117
         * mdc = util.getCommand(MinimedCommand.READ_PUMP_ERROR_STATUS);
         * executeCommandRetry(mdc);
         * param_int =
         * Integer.parseInt(device.convertDeviceReply(mdc).toString());
         * log.debug("Error code: " + param_int);
         * if (param_int > 100)
         * param_int -= 100;
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
         * util.getToolDevice(), m_ic.getMessage("MM_PUMP_ERROR_REPLY"),
         * m_ic.getMessage("MM_ERROR_READING_DEVICE"));
         * throw new PlugInBaseException(f, ex);
         * }
         * // TODO
         * /*
         * if (!checkCorrectReply(0, 67, 0, param_int, mdc.raw_data,
         * m_ic.getMessage("MM_PUMP_ERROR_REPLY")))
         * {
         * log.error(
         * "Device is in error state. For communication to be succcesful device must be cleared of all errors !"
         * );
         * return -1;
         * }
         */

        // ---
        // --- Command: Read Pump State
        // ---
        /*
         * param_int = -1;
         * if (this.util.isCommunicationStopped())
         * return -1;
         * try
         * {
         * mdc = util.getCommand(MinimedCommand.READ_PUMP_STATE);
         * executeCommandRetry(mdc);
         * param_int =
         * Integer.parseInt(device.convertDeviceReply(mdc).toString());
         * log.debug("Pump state: " + param_int);
         * }
         * catch(PlugInBaseException ex)
         * {
         * String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
         * util.getToolDevice(), m_ic.getMessage("MM_PUMP_STATE_REPLY"),
         * m_ic.getMessage("MM_ERROR_READING_DEVICE"));
         * throw new PlugInBaseException(f, ex);
         * }
         * if (!checkDeviceCorrectReply(0, 3, util.getNormalPumpState(),
         * param_int, mdc.reply.raw_data,
         * m_ic.getMessage("MM_PUMP_STATE_REPLY")))
         * {
         * log.error("Pump is not in correct state !");
         * return -1;
         * }
         * if (this.util.isCommunicationStopped())
         * return -1;
         * // Command: Read Pump Error Status
         * //---
         * //--- Command: Read Pump Temporary Basal
         * //---
         * try
         * {
         * mdc = util.getCommand(MinimedCommand.READ_TEMPORARY_BASAL);
         * executeCommandRetry(mdc);
         * param_int =
         * Integer.parseInt(device.convertDeviceReply(mdc).toString());
         * //param_int = this.util.decoder.getUnsignedShort(mdc,
         * MinimedCommandReplyDecoder.TBR_DURATION);
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
         * util.getToolDevice(), m_ic.getMessage("MM_PUMP_DELIVERY_TBR"),
         * m_ic.getMessage("MM_ERROR_READING_DEVICE"));
         * throw new PlugInBaseException(f, ex);
         * }
         * if(param_int != 0)
         * {
         * log.debug("TBR Duration: " + param_int);
         * String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
         * util.getToolDevice(), m_ic.getMessage("MM_PUMP_DELIVERY_TBR"),
         * m_ic.getMessage("MM_PUMP_DELIVERING_TBR"));
         * log.error(f);
         * throw new PlugInBaseException(f);
         * }
         * if (this.util.isCommunicationStopped())
         * return -1;
         * //---
         * //--- Command: Read Pump Active Bolus
         * //---
         * boolean flag = false;
         * try
         * {
         * flag = detectActiveBolus();
         * }
         * catch(PlugInBaseException ex)
         * {
         * throw ex;
         * //throw new ConnectToPumpException("Bad Pump Active (bolus) Reply",
         * 3, MedicalDevice.Util.makeString(m_cmdDetectBolus.m_rawData));
         * }
         * if (flag)
         * {
         * throw new PlugInBaseException("Pump Active (bolus): " +
         * util.getNAKDescription(12));
         * }
         * if (this.util.isCommunicationStopped())
         * return -1;
         * //if (util.getDeviceType()==PumpDevicesIds.PUMP_MINIMED_511)
         * {
         * try
         * {
         * mdc = util.getCommand(MinimedCommand.READ_FIRMWARE_VERSION);
         * executeCommandRetry(mdc);
         * //util.firmware_version = mcr.getString(mdc);
         * log.info("Firmware version: " + device.convertDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Error getting firmware version. Ex.: " + ex, ex);
         * }
         * }
         */

        readMoreData();

        log.debug("init Device --- FINISHED");
        return 0;

    }

    private void readMoreData()
    {
        MinimedCommand mdc = null;

        if (this.util.isCommunicationStopped())
            return;

        // FIXME

        int[] cmds = { MinimedCommand.READ_BG_ALARM_CLOCKS, MinimedCommand.READ_BG_ALARM_ENABLE,
        // MinimedCommand.READ_MISSED_BOLUS_REMINDER_ENABLE,
        // MinimedCommand.READ_MISSED_BOLUS_REMINDERS,
        // MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS,
        // MinimedCommand.READ_CARBOHYDRATE_RATIOS,
        // MinimedCommand.READ_BG_TARGETS
        };

        // MinimedCommand.READ_INSULIN_SENSITIVITES,
        // MinimedCommand.READ_CARBOHYDRATE_UNITS,

        for (int cmd : cmds)
        {

            try
            {
                mdc = util.getCommand(cmd);
                executeCommandRetry(mdc);

                this.device.convertDeviceReply(mdc);
                // this.device.decodeDeviceReply(mdc);
                // util.config.showSettings();

                // log.debug("#####   " + device.decodeDeviceReply(mdc));
            }
            catch (PlugInBaseException ex)
            {
                log.error("Exception: " + ex, ex);
            }
        }

        /*
         * try
         * {
         * mdc = util.getCommand(MinimedCommand.SET_SUSPEND);
         * executeCommandRetry(mdc);
         * this.device.convertDeviceReply(mdc);
         * // util.config.showSettings();
         * //log.debug("#####   " + device.decodeDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * }
         */

        /*
         * this.low_level_debug = true;
         * try
         * {
         * mdc = util.getCommand(MinimedCommand.HISTORY_DATA);
         * executeCommandRetry(mdc);
         * this.device.convertDeviceReply(mdc);
         * //util.config.showSettings();
         * //log.debug("#####   " + device.decodeDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * }
         */

        /*
         * try
         * {
         * //mdc = util.getCommand(MinimedCommand.READ_SENSOR_SETTINGS);
         * mdc = util.getCommand(MinimedCommand.READ_PUMP_MODEL);
         * executeCommandRetry(mdc);
         * this.device.convertDeviceReply(mdc);
         * //util.config.showSettings();
         * //log.debug("#####   " + device.decodeDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * }
         */

        /*
         * try
         * {
         * //mdc = util.getCommand(MinimedCommand.READ_SENSOR_SETTINGS);
         * mdc = util.getCommand(MinimedCommand.READ_LANGUAGE);
         * executeCommandRetry(mdc);
         * this.device.convertDeviceReply(mdc);
         * //log.debug("#####   " + device.decodeDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * }
         */

        /*
         * try
         * {
         * //mdc = util.getCommand(MinimedCommand.READ_SENSOR_SETTINGS);
         * mdc = util.getCommand(MinimedCommand.READ_BG_TARGETS);
         * executeCommandRetry(mdc);
         * this.device.convertDeviceReply(mdc);
         * //util.config.showSettings();
         * //log.debug("#####   " + device.decodeDeviceReply(mdc));
         * }
         * catch(PlugInBaseException ex)
         * {
         * log.error("Exception: " + ex, ex);
         * }
         */

        util.config.showSettings();

    }

    /**
     * Encrypt dtata to send
     */
    public int[] encrypt(int[] input)
    {
        if (low_level_debug)
        {
            log.debug("encrypt()");
        }

        int ai1[] = new int[input.length * 3];
        int cnt = 0;

        // log.info( "encodeDC: about to encode bytes = <" +
        // MedicalDevice.Util.getHexCompact(ai) + ">");
        for (int element : input)
        {
            int p1 = MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[element >> 4 & 0xf];
            int p2 = MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[element & 0xf];
            ai1[cnt++] = p1 >> 2;
            ai1[cnt++] = (p1 & 3) << 2 | p2 >> 4 & 3;
            ai1[cnt++] = p2 & 0xf;
        }

        cnt = 0;
        int ai2[] = new int[(int) Math.ceil(input.length * 6.0d / 4.0d)];
        for (int i = 0; i < ai1.length; i += 2)
        {
            if (i < ai1.length - 1)
            {
                ai2[cnt++] = this.hex_utils.getByteAsInt(ai1[i], ai1[i + 1]);
            }
            else
            {
                ai2[cnt++] = this.hex_utils.getByteAsInt(ai1[i], 5);
            }
        }

        return ai2;
    }

    /**
     * Decrypt data from device 
     */
    public int[] decrypt(int[] input)
    {
        // log.debug("decrypt()");
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int j1 = 0;
        int k1 = (int) Math.floor(input.length * 4D / 6D);
        int ai1[] = new int[k1];

        for (int element : input)
        {
            for (int i2 = 7; i2 >= 0; i2--)
            {
                int j2 = element >> i2 & 1;
                k = k << 1 | j2;
                if (++i != 6)
                {
                    continue;
                }
                if (++j == 1)
                {
                    l = decrypt(k);
                }
                else
                {
                    int i1 = decrypt(k);
                    int k2 = this.hex_utils.getByteAsInt(l, i1);
                    ai1[j1++] = k2;
                    j = 0;
                }
                k = 0;
                i = 0;
            }

        }

        // log.info( "decodeDC: decoded bytes = <" +
        // MedicalDevice.Util.getHexCompact(ai1) + ">");
        return ai1;

        // return null;
    }

    private int decrypt(int input)
    {
        // log.debug("decrypt()");
        if (input < 0 || input > 63)
        {
            log.error("Exception on decoding of " + input + " value. Out of range. ");
            return 0;
        }

        for (int j = 0; j < MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL.length; j++)
        {
            if (MinimedComm_ComLink.COMLINK_ENCODING_PROTOCOL[j] == input)
                return j;
        }

        log.error("Can't find value of " + this.hex_utils.getCorrectHexValue(input) + " in decode table. ");

        return 0;

    }

    public boolean hasEncryptionSupport()
    {
        return true;
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }

    public int closeCommunicationInterface() throws PlugInBaseException
    {
        super.close();
        // not used for RS232 communication
        return 0;
    }

    public int closeDevice() throws PlugInBaseException
    {
        log.debug("closeDevice() [ComLink]");

        if (util.isCommandAvailable(MinimedCommand.CANCEL_SUSPEND))
        {

            if (util.config.firmware_version != null
                    && (util.config.firmware_version.startsWith("VER 1.6") || util.config.firmware_version
                            .startsWith("VER 1.7")))
            {
                executeCommandRetry(MinimedCommand.COMMAND_KEYPAD_PUSH_ACK);
                util.sleepMs(500);
                executeCommandRetry(MinimedCommand.COMMAND_KEYPAD_PUSH_ESC);
                util.sleepMs(500);
            }

            executeCommandRetry(MinimedCommand.CANCEL_SUSPEND);
        }

        try
        {
            executeCommandRetry(MinimedCommand.SET_RF_POWER_OFF);
        }
        catch (PlugInBaseException ex)
        {
            log.debug("shutDownPump: ignoring error: " + ex);
        }

        return 0;
    }

    /**
     * Read status of device
     * 
     * @return
     * @throws PlugInBaseException
     * @throws IOException
     */
    private int readStatusOfDevice() throws PlugInBaseException, IOException
    {
        if (low_level_debug)
        {
            log.debug("readStatusOfDevice()");
        }

        int status = sendToDeviceGetReply((byte) 2);
        // int m_numDataBytes = this.portInputStream.available();
        int m_numDataBytes = -2;

        readFromDeviceAckByte();

        if (low_level_debug)
        {
            log.debug("readStatus: CS status follows: Status=" + status + ", NumberReceivedDataBytes=" + m_numDataBytes
                    + ", ReceivedData=" + isStatusReceivedData(status) + ", RS232Mode=" + isStatusRS232Mode(status)
                    + ", FilterRepeat=" + isStatusFilterRepeat(status) + ", AutoSleep=" + isStatusAutoSleep(status)
                    + ", StatusError=" + isStatusError(status) + ", SelfTestError=" + isStatusSelfTestError(status));
        }

        if (isStatusError(status))
            throw new PlugInBaseException("readStatusOfDevice [ComLink] has STATUS ERROR");
        if (isStatusSelfTestError(status))
            throw new PlugInBaseException("readStatusOfDevice [ComLink] has SELFTEST ERROR");
        else
            return isStatusReceivedData(status) ? m_numDataBytes : 0;
    }

    /**
     * Read Acknowledge from device
     * 
     * @return
     * @throws PlugInBaseException
     */
    @SuppressWarnings("unused")
    private boolean readFromDeviceAckByte() throws PlugInBaseException
    {
        // FIXME

        if (low_level_debug)
        {
            log.debug("readFromDeviceAckByte");
        }

        int first_nr = -1;

        int i = 0;

        boolean found_ack = false;
        boolean found_nak = false;

        for (int j = 0; j <= 1; j++)
        {
            i = readD();

            // log.debug("Read: " + i);

            if (i == 85)
            {
                found_ack = true;
            }

            if (i == 102)
            {
                found_nak = true;
            }

            if (found_ack || found_nak)
            {
                break;
            }
            else
            {
                first_nr = i;
            }
        }

        // System.out.println("First nr: " + first_nr);

        if (!found_ack)
        {
            if (found_nak)
                throw new PlugInBaseException("readAckByte: reply " + this.hex_utils.getHex((byte) 102)
                        + " (NAK) does not match expected ACK reply of " + this.hex_utils.getHex((byte) 85));
            else
                throw new PlugInBaseException("readAckByte: reply " + this.hex_utils.getHex(i)
                        + " does not match expected ACK reply of " + this.hex_utils.getHex((byte) 85));
        }
        else
            return true;

    }

    /**
     * Send something to device and read reply (one char only)
     *  
     * @param cmd_byte
     * @return
     * @throws IOException
     * @throws PlugInBaseException
     */
    private int sendToDeviceGetReply(byte cmd_byte) throws IOException, PlugInBaseException
    {
        this.write(cmd_byte);
        // this.util.sleepIO();
        return this.readD();
    }

    /**
     * ReadD reads from device with timeout (Rxtx problem again. Rxtx has no data blocking)
     * 
     * @return
     * @throws PlugInBaseException
     */
    public int readD() throws PlugInBaseException
    {
        int i = -1;

        util.sleepIO();

        i = this.readIntTimeout();

        if (low_level_debug)
        {
            log.info("readD(): read [" + this.hex_utils.getHexCompact(i) + "]");
        }

        return i;
    }

    /**
     * Rxtx doesn't use available(), which can be little problematic for reading. So we just read until we 
     * get timeout. 
     * 
     * @param ms
     * @return
     * @throws PlugInBaseException
     */
    private int readIntTimeout(long ms) throws PlugInBaseException
    {
        // System.out.println(ms);
        try
        {
            int i = -1;

            i = this.portInputStream.read();

            if (i != -1)
                // log.debug(" <- " + )
                return i;

            long end_time = System.currentTimeMillis() + ms + 1000;

            while (System.currentTimeMillis() < end_time)
            {
                i = this.portInputStream.read();

                if (i != -1)
                    return i;
            }

            return -1;
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(ex);
        }

    }

    public int[] readDataUntilEmpty() throws PlugInBaseException
    {

        ArrayList<Integer> list = new ArrayList<Integer>();

        int read_int = 0;

        while ((read_int = this.readIntTimeout()) != -1)
        {
            list.add(read_int);
        }

        int[] j = new int[list.size()];

        for (int i = 0; i < j.length; i++)
        {
            j[i] = list.get(i);
        }

        return j;
    }

    /*
     * public byte[] readBytesTimeout(long ms) throws IOException
     * {
     * if (this.portInputStream.available() > 0)
     * {
     * byte[] b = new byte[this.portInputStream.available()];
     * this.portInputStream.read(b);
     * return b;
     * }
     * long end_time = System.currentTimeMillis() + ms + 1000;
     * while (System.currentTimeMillis() < end_time)
     * {
     * if (this.portInputStream.available() > 0)
     * {
     * byte[] b = new byte[this.portInputStream.available()];
     * this.portInputStream.read(b);
     * return b;
     * }
     * }
     * byte[] b1 = { -1 };
     * return b1;
     * }
     * public byte readByteTimeout() throws IOException, PlugInBaseException
     * {
     * return this.readByteTimeout(this.timeout_ms);
     * }
     * public byte readByteTimeout(long ms) throws IOException,
     * PlugInBaseException
     * {
     * Integer i = this.readIntTimeout();
     * return i.byteValue();
     * }
     */

    public int readIntTimeout() throws PlugInBaseException
    {
        return this.readIntTimeout(this.timeout_ms);
    }

    /*
     * public void writeD(byte data) throws IOException
     * {
     * if (low_level_debug)
     * log.info("write(byte): writing <" + this.hex_utils.getHex(data) + ">");
     * util.sleepIO();
     * this.write((int)data);
     * }
     */

    private void sendDeviceCommand(MinimedCommand command) throws PlugInBaseException, IOException, Exception
    {
        // FIXME

        log.info("sendCommand: SENDING " + command.getFullCommandDescription() + " for pump #" + util.getSerialNumber());

        try
        {
            // FIXME
            this.setTimeout(command.max_allowed_time);
            int cmd[] = util.createCommandByte(command);
            write(cmd);
            if (low_level_debug)
            {
                log.info("sendCommand: reading link device ACK & (optional) RDY byte.");
            }
            boolean res = readFromDeviceAckByte();

            if (!res)
                return;

            if (command.command_code == 93 && command.command_parameters_count != 0
                    && command.command_parameters[0] == 1)
            {
                int i = this.serialPort.getReceiveTimeout();
                setTimeout(17000);
                readDeviceReadyByte(command.command_parameters_count > 0);
                setTimeout(i);
            }
            else
            {
                readDeviceReadyByte(command.command_parameters_count > 0);
            }

        }
        catch (IOException ex)
        {
            this.util.device_stopped = true;
            this.util.device_stopped_exception = new PlugInBaseException(
                    "sendCommand: ERROR - an IOException  has occurred processing "
                            + command.getFullCommandDescription() + ". Exception = " + ex);
        }
    }

    long timeout_ms = 0L;

    private void setTimeout(long timeout_) // throws Exception
    {
        this.timeout_ms = timeout_;
        // this.serialPort.enableReceiveTimeout(0);
        // this.serialPort.enableReceiveTimeout((int)this.timeout_ms);
    }

    private void readDeviceReadyByte(boolean flag) throws IOException, PlugInBaseException
    {
        // FIXME

        boolean flag1 = false;
        for (int i = 0; i <= 1 && !flag1;)
        {
            try
            {
                readDeviceReadyByteDevice(flag);
                flag1 = true;
                continue;
            }
            catch (IOException ioexception)
            {
                log.error(ioexception);
                // FIXME
                if (util.protocol_id == MinimedDevice.INTERFACE_PARADIGM_LINK_COM) // if(m_linkDeviceType
                                                                                   // ==
                                                                                   // 15)
                {
                    util.interface_paradigmlink_delay = Math.min(util.interface_paradigmlink_delay + 5, 150);
                    log.debug("readReadyByte: increasing paradigm_link delay to " + util.interface_paradigmlink_delay);
                }
                if (i == 1)
                    throw ioexception;
                i++;
            }
        }
    }

    private void readDeviceReadyByteDevice(boolean send_byte) throws PlugInBaseException, IOException
    {
        int i = 0;

        util.sleepParadigmLink();

        if (send_byte)
        {
            this.write((byte) 7);
            // sendCommand((byte)7);
            util.sleepParadigmLink();
            readFromDeviceAckByte();
        }

        // long t = this.getTimeOut();
        // this.setTimeout(10000);

        while ((i = readD()) == -1)
        {

        }

        // i = readD();

        // flag1 = i == 51;

        if (i != 51)
            throw new IOException("readDeviceReadyByteDevice: reply " + hex_utils.getHex(i)
                    + " does not match expected READY reply of " + hex_utils.getHex((byte) 51));
        else
            return;
    }

    private boolean isStatusError(int status)
    {
        return (status & 0x10) != 0;
    }

    private boolean isStatusSelfTestError(int status)
    {
        return (status & 8) != 0;
    }

    private boolean isStatusReceivedData(int status)
    {
        return (status & 1) != 0;
    }

    private boolean isStatusFilterRepeat(int status)
    {
        return (status & 0x40) != 0;
    }

    private boolean isStatusAutoSleep(int status)
    {
        return (status & 0x20) != 0;
    }

    private boolean isStatusRS232Mode(int status)
    {
        return (status & 4) != 0;
    }

    private boolean sendToDeviceCheckReply(byte cmd_byte, byte expected_return) throws IOException, PlugInBaseException
    {
        // Contract.pre(getRS232Port() != null, "serial port is null.");
        // Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int i = 0;
        // boolean flag = false;
        for (int j = 0; j <= 10 && i != expected_return; j++)
        {
            i = sendToDeviceGetReply(cmd_byte);
            // flag = i == byte1;
        }

        if (i != expected_return)
        {

            // dumpInterfaceStatus();
            // throw new IOException("SendCommand: command " +
            // hex_utils.getHex(cmd_byte) + " reply " + hex_utils.getHex(i) +
            // " does not match expected command " +
            // hex_utils.getHex(expected_return));
            log.debug("SendCommand: command " + hex_utils.getHex(cmd_byte) + " reply " + hex_utils.getHex(i)
                    + " does not match expected command " + hex_utils.getHex(expected_return));
            return false;
        }
        else
            return true;
    }

    private int readUntilEmpty()
    {

        try
        {
            int i = 0;
            int j = 0;
            util.sleepIO();

            while ((i = this.portInputStream.read()) != -1)
            {
                log.debug("Remove one character.");
                j++;
                util.sleepIO();
            }

            return j;
        }
        catch (Exception ex)
        {
            log.error("ReadUntilEmpty ex.:" + ex, ex);
            return 0;
        }

        /*
         * if (true)
         * return 0;
         * int i = 0;
         * //Contract.pre(m_serialPort != null, "m_serialPortLocal is null.");
         * int j;
         * try
         * {
         * while((j = portInputStream.available()
         * /*m_serialPortLocal.rxReadyCount()
         *//*
            * ) > 0)
            * {
            * //System.out.println("")
            * byte abyte0[] = new byte[j];
            * util.sleepIO();
            * i += portInputStream.read(abyte0)
            * /*m_serialPortLocal.getData(abyte0)
            *//*
               * ;
               * }
               * }
               * catch(IOException ioexception) { }
               * if(i > 0)
               * log.info("readUntilEmpty: dumped " + i + (i <= 1 ? " byte." :
               * " bytes."));
               * return i;
               */
    }

    // Pump problem with Error Reply:\
    // Invalid return value (%s).
    // Pump is in wrong state for succesful reading.

    // MM_DEVICE_INIT_PROBLEM=%s problem on initialization with
    // %s:\n%s\nException:\n%s

    private boolean checkDeviceCorrectReply(int min, int max, int required, int returned, int[] raw_data,
            String error_desc) throws PlugInBaseException
    {

        if (returned < min || returned > max)
        {
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), error_desc,
                m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
                String.format(m_ic.getMessage("MM_INVALID_RETURN_VALUE"), returned, min, max));
            log.error(f);
            log.error("Raw Data:\n" + hex_utils.getHex(raw_data));
            // throw new PlugInBaseException(f);
            return false;
        }

        if (returned != required)
        {
            log.error("Raw Data:\n" + hex_utils.getHex(raw_data));
            String f = String.format(m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"), util.getToolDevice(), error_desc,
                m_ic.getMessage("MM_DEVICE_INIT_PROBLEM"),
                String.format(m_ic.getMessage("MM_WRONG_STATE"), returned, required));
            log.error(f);
            // throw new PlugInBaseException(f);
            return false;
        }

        return true;

    }

    // FIXME

    private boolean detectActiveBolus() throws PlugInBaseException
    {
        try
        {
            executeCommandRetry(MinimedCommand.DETECT_BOLUS);
        }
        catch (PlugInBaseException ex)
        {
            if (ex.error_code > 0)
            {
                if (ex.error_code == 12)
                    return true;
                else
                    throw ex;
            }
            else
                throw ex;

        }

        return false;
    }

    // sendTransferDataCommand
    // sendCommand((byte)8);

    int m_state = 0;

    /**
     * Execute Command
     * 
     * @param command_id
     * @throws PlugInBaseException
     */
    public boolean executeCommandRetry(int command_id) throws PlugInBaseException
    {
        return executeCommandRetry(util.getCommand(command_id));
    }

    /**
     * Execute Command
     * 
     * @param command
     * @throws PlugInBaseException
     */
    public boolean executeCommandRetry(MinimedCommand command) throws PlugInBaseException
    {
        // FIXME add retry and exception handling

        if (command.has_only_sub_commands)
        {
            /*
             * // main command
             * if(command.command_parameters_count > 0)
             * {
             * MinimedCommand devicecommand = command.createCommandPacket();
             * executeCommand(devicecommand);
             * }
             * //MedicalDevice.m_lastCommandDescription =
             * m_deviceCommand.m_description;
             * command.prepareForReading();
             * sendAndRead(command);
             */
            command.executeSubCommands();
        }
        else
        {
            if (low_level_debug)
            {
                log.info("executeCommand(). Before parameter count !!!");
            }

            // main command
            if (command.command_parameters_count > 0)
            {
                MinimedCommand devicecommand = command.createCommandPacket();
                executeCommandRetry(devicecommand);
            }
            else
            {
                if (low_level_debug)
                {
                    log.info("executeCommand(). Before prepere for reading !!!");
                }

                // MedicalDevice.m_lastCommandDescription =
                // m_deviceCommand.m_description;
                // command.prepareForReading();

                // try with this

                // if (!command.has_sub_commands)
                sendCommandReadData(command);

            }

            // FIXME
            if (command.has_sub_commands)
            {
                /*
                 * try
                 * {
                 * checkDeviceAcknowledge(command);
                 * }
                 * catch(Exception ex)
                 * {
                 * log.error("No acknowledge received. Ex: " + ex, ex);
                 * }
                 */

                command.executeSubCommands();
            }
        }

        // FIXME
        return true;

    }

    public void sendCommandReadData(MinimedCommand command) throws PlugInBaseException
    {

        try
        {
            sendDeviceCommand(command);
        }
        catch (Exception ex)
        {
            log.error("Exception on sending command: " + ex, ex);
            throw new PlugInBaseException(ex);
        }

        log.debug("Length: " + command.reply.raw_data.length + " Return data: " + command.canReturnData());

        // if ((command.reply.raw_data.length > 0) && (!util.isHaltRequested()))
        if (command.canReturnData() && !command.has_sub_commands)
        {
            if (command.reply.raw_data.length > 64)
            {
                command.reply.raw_data = readDeviceDataPage(command); // command.raw_data.length);
            }
            else
            {
                m_state = 5;
                command.reply.raw_data = readDeviceData(command);
                // incTotalReadByteCount(m_deviceCommand.m_rawData.length);
                // notifyDeviceUpdateProgress();
            }
        }
        else
        {
            try
            {
                checkDeviceAcknowledge(command);

            }
            catch (Exception ex)
            {
                log.error("Exception: " + ex, ex);
                throw new PlugInBaseException("sendAndRead: ERROR - problem checking ACK; exception = " + ex);
            }
        }
    }

    /*
     * public void sendCommandReadDataHistory(MinimedCommand command) throws
     * PlugInBaseException
     * {
     * try
     * {
     * sendDeviceCommand(command);
     * }
     * catch(Exception ex)
     * {
     * log.error("Exception on sending command: " + ex, ex);
     * throw new PlugInBaseException(ex);
     * }
     * log.debug("Length: " + command.reply.raw_data.length + " Return data: " +
     * command.canReturnData());
     * //if ((command.reply.raw_data.length > 0) && (!util.isHaltRequested()))
     * if (command.canReturnData())
     * {
     * if (command.reply.raw_data.length > 64)
     * {
     * command.reply.raw_data = readDeviceDataPage(command);
     * //command.raw_data.length);
     * }
     * else
     * {
     * m_state = 5;
     * command.reply.raw_data = readDeviceData(command);
     * //incTotalReadByteCount(m_deviceCommand.m_rawData.length);
     * //notifyDeviceUpdateProgress();
     * }
     * }
     * else
     * {
     * try
     * {
     * checkDeviceAcknowledge(command);
     * }
     * catch(Exception ex)
     * {
     * log.error("Exception: " + ex, ex);
     * throw new
     * PlugInBaseException("sendAndRead: ERROR - problem checking ACK; exception = "
     * + ex);
     * }
     * }
     * }
     */

    // TODO rename

    private int[] readDeviceDataPage(MinimedCommand command) throws PlugInBaseException
    {

        int i = command.reply.raw_data.length;
        // int ai[] = new int[70];
        int ai2[] = new int[0];
        int j = 0;
        int k = 1;
        boolean flag = false;
        log.debug("readDeviceDataPage: processing " + command.getFullCommandDescription());
        do
        {
            m_state = 6;
            int ai1[] = readDeviceData(command);
            if (ai1.length == 0)
            {
                flag = true;
            }
            else
            {
                ai2 = hex_utils.concatIntArrays(ai2, ai1);
                log.debug("readDeviceDataPage: just read packet " + j + ", bytes = " + ai1.length + ", total bytes = "
                        + ai2.length);
                // incTotalReadByteCount(ai1.length);
                j++;
                // notifyDeviceUpdateProgress();
                boolean flag1 = (command.data_count & 0x80) == 128;
                int l = command.data_count & 0x7f;
                if (l != k)
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - sequence number mismatch); expected "
                            + k + ", read " + l);
                if (++k > 127)
                {
                    k = 1;
                }
                flag = ai2.length >= i || util.isHaltRequested() || flag1;
                try
                {
                    if (!flag && !util.isHaltRequested())
                    {
                        sendAcknowledgeToDevice();
                    }
                }
                catch (IOException ex)
                {
                    throw new PlugInBaseException("readDeviceDataPage: ERROR - problem sending ACK; exception = " + ex);
                }
            }
        } while (!flag);
        log.debug("readDeviceDataPage: " + command.getFullCommandDescription() + " returned " + ai2.length + " bytes.");
        return ai2;
    }

    private void sendAcknowledgeToDevice() throws PlugInBaseException, IOException
    {
        log.debug("sendAck: sending cmd " + hex_utils.getHex((byte) 6));

        write(util.createCommandByte(MinimedCommand.COMMAND_ACK));

        readFromDeviceAckByte();
        readDeviceReadyByte(false);
    }

    // TODO rename

    private int[] readDeviceData(MinimedCommand command) throws PlugInBaseException
    {
        int ai1[] = new int[0];
        int ai[];
        try
        {
            readStatusOfDevice();
            this.write((byte) 8);

            ai = this.readDataUntilEmptyAckRemove();
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException("readDeviceData: ERROR - an IOException  has occurred processing "
                    + command.getFullCommandDescription() + ". Exception = " + ex);
        }

        int ai2[] = decrypt(ai);

        // log.debug("Decoded reply: [" + hex_utils.getHexCompact(ai2) + "] ");

        checkHeaderAndCRC(command, ai2);

        // boolean flag = false;
        if (ai2[4] == 21)
        {
            int k = ai2[5];
            if (k == 13)
            {
                log.debug("readDeviceData: NAK received = no more data.");
            }
            else
                throw new PlugInBaseException("readDeviceData: " + command.getFullCommandDescription()
                        + " failed; error code = <" + hex_utils.getHex(k) + ">" + "(" + util.getNAKDescription(k)
                        + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2) + ">)", k);

        }
        else
        {
            // log.debug("Part: " + ai2[4]);
            // log.debug("Command code: " + command.command_code);

            if (ai2[4] != command.command_code)
                throw new PlugInBaseException("readDeviceData: " + command.getFullCommandDescription()
                        + " has bad command code value of " + hex_utils.getHex(ai2[4]) + " (expected "
                        + hex_utils.getHex(command.command_code) + ") " + "(byte data = " + "<" + hex_utils.getHex(ai2)
                        + ">)");

            command.data_count = ai2[5];
            int j = ai2.length - 6 - 1;

            ai1 = new int[j];
            System.arraycopy(ai2, 6, ai1, 0, j);

            log.debug("readDeviceData: decoded packet = [" + hex_utils.getHex(ai1) + "]");
        }
        return ai1;
    }

    private int[] readDataUntilEmptyAckRemove() throws PlugInBaseException
    {
        boolean ack_found = false;
        int[] ai = null;

        ai = this.readDataUntilEmpty();

        // log.debug("Read: " + this.hex_utils.getHex(ai));

        if (ai[ai.length - 1] == 85)
        {
            if (low_level_debug)
            {
                log.debug("Found ACK");
            }

            ack_found = true;
            ai = this.hex_utils.getIntSubArray(ai, 0, ai.length - 1, ai.length);
        }

        log.debug("Read: " + this.hex_utils.getHex(ai));

        if (!ack_found)
        {
            readFromDeviceAckByte();
        }

        return ai;

    }

    // TODO rename

    /**
     * Check Header and CRC data
     */
    private boolean checkHeaderAndCRC(MinimedCommand command, int arr_in[]) throws PlugInBaseException
    {
        int crc_rec = arr_in[arr_in.length - 1];
        int crc_calc = hex_utils.computeCRC8(arr_in, 0, arr_in.length - 1) & 0xff;

        if (crc_rec != crc_calc)
        {
            log.debug("checkHeaderAndCRC: " + command.getFullCommandDescription() + " resulted in bad CRC value of "
                    + crc_rec + " (expected " + crc_calc + ") " + "(byte data = " + "<" + hex_utils.getHex(arr_in)
                    + ">)");
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription()
                    + " resulted in bad CRC value of " + crc_rec + " (expected " + crc_calc + ") " + "(byte data = "
                    + "<" + hex_utils.getHex(arr_in) + ">)");
        }

        if (arr_in[0] != 167)
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription()
                    + " resulted in bad Type Code value of " + hex_utils.getHex(arr_in[0]));

        // int ai1[] = packSerialNumber();
        if (this.util.config.serial_number_bcd[0] != arr_in[1] || this.util.config.serial_number_bcd[1] != arr_in[2]
                || this.util.config.serial_number_bcd[2] != arr_in[3])
            throw new PlugInBaseException("checkHeaderAndCRC: " + command.getFullCommandDescription()
                    + " resulted in bad serial number value of <" + hex_utils.getHex(arr_in[1]) + " "
                    + hex_utils.getHex(arr_in[2]) + " " + hex_utils.getHex(arr_in[3]) + ">");
        else
            return true;
    }

    private boolean checkDeviceAcknowledge(MinimedCommand command) throws Exception
    {
        log.info("checkAck: retrieving pump ACK packet...");

        // log.debug(" available=" + this.portInputStream.available());

        int i = readStatusOfDevice();

        // log.debug("    status=" + i);
        // this.write((byte)8);

        /*
         * int ai[] = new int[i];
         * read(ai);
         */

        this.write((byte) 8);

        boolean ack_found = false;

        int ai[] = this.readDataUntilEmpty();
        if (this.low_level_debug)
        {
            log.debug("Read: " + this.hex_utils.getHex(ai));
        }

        if (ai[ai.length - 1] == 85)
        {
            if (this.low_level_debug)
            {
                log.debug("Found ACK");
            }
            ack_found = true;
            ai = this.hex_utils.getIntSubArray(ai, 0, ai.length - 1, ai.length);
        }

        log.debug("Read: [" + this.hex_utils.getHexCompact(ai) + "] ");

        int ai1[] = decrypt(ai);

        if (!ack_found)
        {
            readFromDeviceAckByte();
        }

        checkHeaderAndCRC(command, ai1);

        if (ai1[4] != 6)
        {
            int j = ai1[5];
            command.getReply().setAcknowledgeReceived(false);
            command.getReply().setException(
                new PlugInBaseException("checkAck: " + command.getFullCommandDescription() + " failed; error code = <"
                        + hex_utils.getHex(j) + ">" + "(" + util.getNAKDescription(j) + ") " + "(byte data = " + "<"
                        + hex_utils.getHex(ai1) + ">)"));

            // FIXME
            return false;
        }
        else
        {
            log.info("checkAck: GOOD pump ACK reply received.");
            command.getReply().setAcknowledgeReceived(true);
            return true;
        }
    }

    @Override
    public void dispose()
    {
    }

    public String getComment()
    {
        return null;
    }

    public String getConnectionPort()
    {
        return null;
    }

    public String getDeviceClassName()
    {
        return null;
    }

    public int getDeviceId()
    {
        return 0;
    }

    @Override
    public String getDeviceSpecialComment()
    {
        return null;
    }

    public int getDownloadSupportType()
    {
        return 0;
    }

    @Override
    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }

    public String getIconName()
    {
        return null;
    }

    public int getImplementationStatus()
    {
        return 0;
    }

    public String getInstructions()
    {
        return null;
    }

    public String getName()
    {
        return null;
    }

    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }

    @Override
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    public boolean isDeviceCommunicating()
    {
        return false;
    }

    @Override
    public boolean isFileDownloadSupported()
    {
        return false;
    }

    @Override
    public boolean isReadableDevice()
    {
        return false;
    }

    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }

    public void readDeviceDataFull() throws PlugInBaseException
    {
    }

    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }

    @Override
    public void readInfo() throws PlugInBaseException
    {
    }

    @Override
    public long getItemId()
    {
        return 0;
    }

}
