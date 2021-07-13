package ggc.plugin.device.impl.minimed.comm.serial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedCommandPacket;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandParameterType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedResponseStatus;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     MinimedCommunicationComLink
 *  Description:  Communication class for ComLink device (or ParadigmLink). Since this devices are not used anymore,
 *     development of this class was stopped.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCommunicationComLink extends MinimedSerialCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCommunicationComLink.class);

    private static final int COMLINK_ENCODING_PROTOCOL[] = { 21, 49, 50, 35, 52, 37, 38, 22, 26, 25, 42, 11, 44, 13, 14,
                                                             28 };

    public byte COMMAND_2_STX = 2;
    public byte COMMAND_6_ACK = 6;
    public byte COMMAND_7_BEL = 7;
    public byte COMMAND_8_BS = 8;

    public byte RESPONSE_READY = 51;


    public MinimedCommunicationComLink(DataAccessPlugInBase dataAccess, MinimedDataHandler dataHandler)
    {
        super(dataAccess, dataHandler);
    }


    public void preInitInterface()
    {
        serialBCD = MedtronicUtil.getConnectionParameters().serialNumberBCD;
    }


    @Override
    protected void executeHistoryCommandWithRetry(MinimedCommandType minimedCommandType) throws PlugInBaseException
    {

    }


    /**
     * Encrypt data to send to device
     */
    public byte[] encrypt(byte[] input)
    {
        if (lowLevelDebug)
            LOG.debug("encrypt()");

        int midData[] = new int[input.length * 3];
        int counter = 0;

        for (byte b : input)
        {
            int part1 = COMLINK_ENCODING_PROTOCOL[(b >> 4 & 0xf)];
            int part2 = COMLINK_ENCODING_PROTOCOL[(b & 0xf)];
            midData[counter++] = part1 >> 2;
            midData[counter++] = (part1 & 3) << 2 | (part2 >> 4 & 3);
            midData[counter++] = part2 & 0xf;
        }

        counter = 0;
        byte endData[] = new byte[(int) Math.ceil(((double) input.length * 6.0d) / 4.0d)];
        for (int i = 0; i < midData.length; i += 2)
        {
            if (i < midData.length - 1)
                endData[counter++] = (byte) this.bitUtils.getByteAsInt(midData[i], midData[i + 1]);
            else
                endData[counter++] = (byte) this.bitUtils.getByteAsInt(midData[i], 5);
        }

        return endData;
    }


    /**
     * Decrypt data from device
     */
    public byte[] decrypt(byte[] input)
    {
        if (lowLevelDebug)
            LOG.debug("decrypt()");

        byte a1 = 0;
        byte a2 = 0;
        byte a3 = 0;
        byte a4 = 0;
        int endCounter = 0;

        int outputLength = (int) Math.floor(((double) input.length * 4D) / 6D);
        byte endArray[] = new byte[outputLength];

        for (int i = 0; i < input.length; i++)
        {
            for (int j = 7; j >= 0; j--)
            {
                a3 = (byte) (a3 << 1 | (input[i] >> j & 1));

                if (++a1 != 6)
                    continue;

                if (++a2 == 1)
                {
                    a4 = decrypt(a3);
                }
                else
                {
                    endArray[endCounter++] = (byte) this.bitUtils.getByteAsInt(a4, decrypt(a3));
                    a2 = 0;
                }
                a3 = 0;
                a1 = 0;
            }

        }

        return endArray;
    }


    private byte decrypt(byte input)
    {
        if ((input < 0) || (input > 63))
        {
            LOG.error("Problem finding input value " + input + " (out of range).");
            return 0;
        }

        for (int j = 0; j < COMLINK_ENCODING_PROTOCOL.length; j++)
        {
            if (COMLINK_ENCODING_PROTOCOL[j] == input)
                return (byte) j;
        }

        LOG.error("Value " + this.bitUtils.getCorrectHexValue(input) + " could not be found in decryption table. ");

        return 0;
    }


    public SerialSettings getSerialSettings()
    {
        SerialSettings ss = new SerialSettings();
        ss.baudRate = 57600; // 57600;
        ss.dataBits = SerialSettingsType.DataBits8;
        ss.parity = SerialSettingsType.ParityNone; // none
        ss.stopBits = SerialSettingsType.StopBits1;
        ss.dtr = false; // F
        ss.rts = true; // T

        return ss;
    }


    public void postInitPort()
    {
        if (serialCommunicationType == 1)
        {
            this.getNRSHandler().activateBlockingReads();
        }
        else
        {
            this.getJSSCHandler().activate();
        }
    }


    public int closeCommunicationInterface() throws PlugInBaseException
    {
        this.commHandler.disconnectDevice();
        return 0;
    }


    public byte[] createTransmitPacketForCommand(MinimedCommandPacket commandPacket)
    {
        MinimedCommandType commandType = commandPacket.commandType;

        // 0 -> [5] - command header | HEADER (4 = standard, 5 = with
        // parameters, 10 = multixmit mode (RF Power On)
        // 1 -> [x] - length of next packet |
        // 2 -> [167] - comand_package | BODY (ENCODED)
        // 3,4,5 -> - BCD serial number
        // 6 -> commandCode
        // 7 -> parameter count
        // ... parameter array (if param count >0)
        // 8 -> CRC (2-7)

        // Command:
        // 0 -> header code, -> HEADER
        // 1 -> length of command packet,
        // 2 -> commandCode (167), -> BODY (encrypted)
        // 3,4,5 -> serial number as BCD
        // 6 -> command code
        // 7 -> parameters count
        // ... parameters array (0-x)
        // ... CRC

        List<Byte> commandBody = new ArrayList<Byte>();

        commandBody.add((byte) 167);
        commandBody.add((byte) serialBCD[0]);
        commandBody.add((byte) serialBCD[1]);
        commandBody.add((byte) serialBCD[2]);
        commandBody.add((byte) commandType.getCommandCode());

        boolean hasParameters = commandPacket.hasParameters();

        LOG.debug("commandCode: {}, hasParameters: {}", commandType.getCommandCode(), hasParameters);

        if (hasParameters)
        {
            commandBody.add((byte) commandPacket.commandParameterCount);
            addParametersToCommandBody(commandBody, commandPacket);
        }
        else
        {
            commandBody.add((byte) 0);
        }

        byte crc = (byte) bitUtils.computeCRC8(bitUtils.getByteArrayFromList(commandBody), 0, commandBody.size()); // size-1

        commandBody.add(crc);

        byte[] cmdBody = bitUtils.getByteArrayFromList(commandBody);

        LOG.debug("Command to encode: [" + bitUtils.getHexCompact(cmdBody) + "]");

        // if (this.hasEncryptionSupport())
        cmdBody = this.encrypt(cmdBody);

        LOG.debug("Command after encode: [" + bitUtils.getHexCompact(cmdBody) + "]");

        // header [headerCode, length of data]
        byte cmdHeader[] = new byte[2];

        if (commandPacket.multiXmitMode)
            cmdHeader[0] = 10;
        else if (!hasParameters)
            cmdHeader[0] = 5;
        else
            cmdHeader[0] = 4;

        cmdHeader[1] = (byte) cmdBody.length;
        byte[] cmdFull = bitUtils.concat(cmdHeader, cmdBody);

        LOG.debug("Command to send: " + bitUtils.getHex(cmdFull));

        return cmdFull;

    }


    public int getAvailableBytesFromDevice() throws PlugInBaseException
    {
        return this.commHandler.available();
    }


    public int readFromPhysicalPortWithTimeout() throws PlugInBaseException
    {
        return readFromPhysicalPort(deviceTimeout);
    }


    public int readFromPhysicalPort(long timeout) throws PlugInBaseException
    {
        // LOG.debug("readFromPhysicalPort [timeout=" + timeout);

        MedtronicUtil.sleepPhysicalCommunication();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeout; // + 1000;

        do
        {
            // if (this.commHandler.isDataAvailable())
            {
                int data = this.commHandler.read();

                if (data != -1)
                {
                    debugRead(data, startTime);
                    // if (lowLevelDebug)
                    // LOG.debug("readFromPhysicalPort [" + data + "]");

                    return data;
                }
            }

            MedtronicUtil.sleepMs(50);

        } while (System.currentTimeMillis() < endTime);

        return -1;
    }


    /**
     * {@inheritDoc}
     */
    public void initializeCommunicationInterface() throws PlugInBaseException
    {
        if (debug)
            LOG.info("initializeCommunicationInterface (ComLink)");

        for (int i = 0; i <= 4; i++)
        {
            try
            {
                createPhysicalPort();
                postInitPort();

                sendToDeviceCheckReply(COMMAND_6_ACK, RESPONSE_READY);

                readStatusOfDevice();

                break;
            }
            catch (PlugInBaseException ex)
            {
                if (i == 4)
                {
                    LOG.warn("Error on initialize. Ex.: " + ex, ex);
                    throw ex;
                }
                else
                    LOG.warn("Error on initialize. Retrying. Ex.: " + ex, ex);
            }
        }
    }


    public int initDevice() throws PlugInBaseException
    {

        // ---
        // --- Command: Set RF Power On
        // ---
        executeCommandWithRetry(MinimedCommandType.RFPowerOn);

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        // ---
        // --- Command: Read Pump Error Status
        // ---

        int errorCode = executeCommandWithIntegerReply(MinimedCommandType.ReadPumpErrorStatus, "Pump Error Status");

        LOG.debug("Error code: " + errorCode);

        return 0;
    }


    /**
     * {@inheritDoc}
     */
    public int initDeviceFull() throws PlugInBaseException
    {

        /** This needs to be refactored, commands are there, but we need to reafctor everything anyway. **/

        // MinimedCommand mdc = null;

        LOG.debug("init Device -- START");

        // ---
        // --- Command: Set RF Power On
        // ---
        executeCommandWithRetry(MinimedCommandType.RFPowerOn);

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        // ---
        // --- Command: Read Pump Error Status
        // ---

        int errorCode = executeCommandWithIntegerReply(MinimedCommandType.ReadPumpErrorStatus, "Pump Error Status");

        LOG.debug("Error code: " + errorCode);

        if (errorCode > 100)
            errorCode -= 100;

        // {
        // LOG.error("Exception: " + ex, ex);
        // String f =
        // String.format(i18nControl.getMessage("MM_DEVICE_INIT_PROBLEM"),
        // util.getToolDevice(), i18nControl.getMessage("MM_PUMP_ERROR_REPLY"),
        // i18nControl.getMessage("MM_ERROR_READING_DEVICE"));
        // throw new PlugInBaseException("", ex);
        // }

        // TODO

        // if (!checkCorrectReply(0, 67, 0, param_int, mdc.rawData,
        // i18nControl.getMessage("MM_PUMP_ERROR_REPLY")))
        // {
        // LOG.error("Device is in error state. For communication to be
        // succcesful minimedDataHandler must be cleared of all errors !");
        // return -1;
        // }

        // ---
        // --- Command: Read Pump State
        // ---

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        int pumpState = executeCommandWithIntegerReply(MinimedCommandType.ReadPumpErrorStatus, "Pump State");

        // if (!checkDeviceCorrectReply(0, 3, util.getNormalPumpState(),
        // pumpState, mdc.reply.rawData,
        // i18nControl.getMessage("MM_PUMP_STATE_REPLY")))
        // {
        // LOG.error("Pump is not in correct state !");
        // return -1;
        // }

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        // ---
        // --- Command: Read Pump Temporary Basal
        // ---

        int tbr = executeCommandWithIntegerReply(
            MinimedCommandType.getReadTemporaryBasal(this.dataHandler.getDeviceType()), "Temporary Basal");

        // param_int = this.util.decoder.getUnsignedShort(mdc,
        // MinimedCommandReplyDecoder.TBR_DURATION);

        // try
        // {
        // mdc = util.getCommand(MinimedCommand.READ_TEMPORARY_BASAL);
        // executeCommandRetry(mdc);
        // param_int =
        // Integer.parseInt(minimedDataHandler.convertDeviceReply(mdc).toString()
        // );
        // //param_int = this.util.decoder.getUnsignedShort(mdc,
        // MinimedCommandReplyDecoder.TBR_DURATION);
        // }
        // catch(PlugInBaseException ex)
        // {
        // LOG.error("Exception: " + ex, ex);
        // String f =
        // String.format(i18nControl.getMessage("MM_DEVICE_INIT_PROBLEM"),
        // util.getToolDevice(), i18nControl.getMessage("MM_PUMP_DELIVERY_TBR"),
        // i18nControl.getMessage("MM_ERROR_READING_DEVICE"));
        // throw new PlugInBaseException(f, ex);
        // }

        if (tbr != 0)
        {
            LOG.debug("TBR Duration: " + tbr);
            // String f =
            // String.format(i18nControl.getMessage("MM_DEVICE_INIT_PROBLEM"),
            // util.getToolDevice(),
            // i18nControl.getMessage("MM_PUMP_DELIVERY_TBR"),
            // i18nControl.getMessage("MM_PUMP_DELIVERING_TBR"));
            // LOG.error(f);
            // throw new PlugInBaseException(f);
        }

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        // ---
        // --- Command: Read Pump Active Bolus
        // ---
        boolean flag = false;
        try
        {
            flag = detectActiveBolus();
        }
        catch (PlugInBaseException ex)
        {
            throw ex;
            // throw new ConnectToPumpException("Bad Pump Active (bolus) Reply",
            // 3, MedicalDevice.Util.makeString(m_cmdDetectBolus.m_rawData));
        }

        // if (flag)
        // {
        // throw new PlugInBaseException("Pump Active (bolus): " +
        // util.getNAKDescription(12));
        // }

        if (MedtronicUtil.isDownloadCanceled())
            return -1;

        if (dataHandler.getDeviceType() == MinimedDeviceType.Minimed_511)
        {

            String fwVersion = executeCommandWithStringReply(
                MinimedCommandType.getReadTemporaryBasal(this.dataHandler.getDeviceType()), "Temporary Basal");

            MedtronicUtil.setFirmwareVersion(fwVersion);

        }

        LOG.debug("init Device --- FINISHED");
        return 0;

    }


    @Override
    public Logger getLocalLog()
    {
        return LOG;
    }


    public int closeDevice() throws PlugInBaseException
    {
        if (debug)
            LOG.debug("closeDevice() [ComLink]");

        // if (true)
        // return 0;

        if (MedtronicUtil.isFirmwareVersion_16_or_17())
        {
            executeCommandWithRetry(MinimedCommandType.PushAck);
            MedtronicUtil.sleepMs(500);
            executeCommandWithRetry(MinimedCommandType.PushEsc);
            MedtronicUtil.sleepMs(500);
        }

        executeCommandWithRetry(MinimedCommandType.CancelSuspend);

        try
        {
            executeCommandWithRetry(MinimedCommandType.RFPowerOff);
        }
        catch (PlugInBaseException ex)
        {
            LOG.debug("shutDownPump [RFPowerOff]: ignoring error: " + ex);
        }

        return 0;
    }


    /**
     * Read status of Device
     *
     * @return status of device
     * @throws PlugInBaseException
     */

    // TODO
    private int readStatusOfDevice() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readStatusOfDevice -- START");

        int status = sendToDeviceGetReply(COMMAND_2_STX);
        int numDataBytes = getAvailableBytesFromDevice();

        readAcknowledgeFromDevice();

        Map<MinimedResponseStatus, String> statuses = MinimedResponseStatus.getAllRelevantStatuses(status);

        if (lowLevelDebug)
            LOG.debug("readStatus: CS status follows: Status=" + status //
                    + ", NumberReceivedDataBytes=" + numDataBytes //
                    + ", ReceivedData=" + statuses.containsKey(MinimedResponseStatus.ReceivedData) //
                    + ", RS232Mode=" + statuses.containsKey(MinimedResponseStatus.RS232Mode) //
                    + ", FilterRepeat=" + statuses.containsKey(MinimedResponseStatus.FilterRepeat) //
                    + ", AutoSleep=" + statuses.containsKey(MinimedResponseStatus.AutoSleep) //
                    + ", StatusError=" + statuses.containsKey(MinimedResponseStatus.Error) //
                    + ", SelfTestError=" + statuses.containsKey(MinimedResponseStatus.SelfTestError));

        if (statuses.containsKey(MinimedResponseStatus.Error))
            throw new PlugInBaseException("readStatusOfDevice [ComLink] has STATUS ERROR");
        if (statuses.containsKey(MinimedResponseStatus.SelfTestError))
            throw new PlugInBaseException("readStatusOfDevice [ComLink] has SELFTEST ERROR");
        else
        {
            if (lowLevelDebug)
                LOG.debug("readStatusOfDevice -- END");
            return statuses.containsKey(MinimedResponseStatus.ReceivedData) ? numDataBytes : 0;
        }
    }


    /**
     * Read Acknowledge from Device
     *
     * @throws PlugInBaseException
     */
    private void readAcknowledgeFromDevice() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readAcknowledgeFromDevice -- START");

        int readData;
        int readBit = -1;

        for (int j = 0; j <= 1; j++)
        {
            readData = readFromPhysicalPortBlocking();

            if ((MinimedResponseStatus.Acknowledge.getStatusCode() == readData)
                    || (MinimedResponseStatus.NotAcknowledge.getStatusCode() == readData))
            {
                readBit = readData;
                break;
            }

            if (readData != -1)
                readBit = readData;
        }

        if (MinimedResponseStatus.Acknowledge.getStatusCode() != readBit)
        {
            if (MinimedResponseStatus.NotAcknowledge.getStatusCode() == readBit)
            {
                LOG.warn("Device sent NAK (0x66) bit instead of ACK (0x55).");
                throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse,
                        new Object[] { this.bitUtils.getHex((byte) 102), this.bitUtils.getHex((byte) 85) });
            }
            else
            {
                LOG.warn(String.format("Device sent Unknown response (%s) instead of ACK (0x55).",
                    this.bitUtils.getHex((byte) readBit)));
                throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse,
                        new Object[] { this.bitUtils.getHex((byte) readBit), this.bitUtils.getHex((byte) 85) });
            }
        }

        if (lowLevelDebug)
            LOG.debug("readAcknowledgeFromDevice -- END");

    }


    /**
     * Send command byte to Device and wait for response
     *
     * @param commandByte command Byte
     * @return byte returned from device
     * @throws PlugInBaseException
     */
    private int sendToDeviceGetReply(byte commandByte) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("sendToDeviceGetReply: command=" + commandByte + " -- START");

        this.writeToPhysicalPort(commandByte);

        int data = readFromPhysicalPortWithTimeout();

        if (lowLevelDebug)
            LOG.debug("sendToDeviceGetReply: reply=" + data + " -- END");

        return data;
    }


    /**
     * Send Command to Device, read acknowledge and read ready byte
     *
     * @param commandPacket command Packet
     * @throws PlugInBaseException
     */
    private void sendCommandToDevice(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("sendCommandToDevice -- START");

        this.setDeviceTimeout(commandPacket.commandType.maxAllowedTime);

        LOG.info("    sendCommandToDevice: SENDING " + commandPacket.getFullCommandDescription() + " for pump #"
                + MedtronicUtil.getSerialNumber());

        byte cmd[] = createTransmitPacketForCommand(commandPacket);
        this.writeToPhysicalPort(cmd);

        if (lowLevelDebug)
            LOG.info("    sendCommandToDevice: reading link minimedDataHandler ACK & (optional) RDY byte.");

        readAcknowledgeFromDevice();

        if (commandPacket.doWeSendDataForReadyByte())
        {
            if (lowLevelDebug)
                LOG.debug("    commandType=" + commandPacket.commandType.name() + ", commandParams: "
                        + commandPacket.commandParameterCount);
            readDeviceReadyByte(commandPacket.doWeSendDataForReadyByte());
        }

        if (lowLevelDebug)
            LOG.debug("sendCommandToDevice -- END");
    }


    /**
     * Read Device Ready Byte (two retries) and calls readDeviceReadyByteDevice.
     *
     * @param sendData should data be sent to device
     * @throws PlugInBaseException
     */
    private void readDeviceReadyByte(boolean sendData) throws PlugInBaseException
    {
        if (debug)
            LOG.debug("readDeviceReadyByte: sendData=" + sendData + " - START");

        for (int i = 0; i <= 1; i++)
        {
            try
            {
                readDeviceReadyByteDevice(sendData);
                break;
            }
            catch (PlugInBaseException ex)
            {
                break;
                // if (i == 1)
                // {
                // LOG.error(ex.getMessage());
                // throw ex;
                // }
                // else
                // {
                // LOG.warn(ex.getMessage());
                // }
            }
        }

        if (debug)
            LOG.debug("readDeviceReadyByte: sendData=" + sendData + " - END");
    }


    /**
     * Read Device Ready Byte Device (this one talks with device, if we have parameters we need to send command 7
     * before we can wait for READY signal.
     *
     * @param sendData if data should be sent to device
     * @throws PlugInBaseException
     */
    private void readDeviceReadyByteDevice(boolean sendData) throws PlugInBaseException
    {
        if (debug)
            LOG.debug("readDeviceReadyByteDevice: sendData=" + sendData + " - Start");

        MedtronicUtil.sleepInterface();

        if (sendData)
        {
            this.writeToPhysicalPort(COMMAND_7_BEL);
            MedtronicUtil.sleepInterface();
            readAcknowledgeFromDevice();
        }

        if (debug)
            LOG.debug("readDeviceReadyByteDevice: waiting for RDY");

        int data = readFromPhysicalPortBlocking();

        if (data != RESPONSE_READY)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse,
                    new Object[] { "READY [33]", bitUtils.getHex(data) });
        }

        if (debug)
            LOG.debug("readDeviceReadyByteDevice: sendData=" + sendData + " - End");
    }


    /**
     * Send byte to device and expect certain return (we do several retries to get info)
     *
     * @param commandByte command byte
     * @param expectedReturn expected return byte
     * @return true is successful and exception if not
     * @throws PlugInBaseException
     */
    private boolean sendToDeviceCheckReply(byte commandByte, byte expectedReturn) throws PlugInBaseException
    {
        int data = 0;

        LOG.debug("sendToDeviceCheckReply: command=" + commandByte + ", expected: " + expectedReturn);

        for (int j = 0; j <= 10; j++)
        {
            if ((data = sendToDeviceGetReply(commandByte)) == expectedReturn)
            {
                return true;
            }
        }

        PlugInBaseException exception = new PlugInBaseException(PlugInExceptionType.DeviceCommandInvalidResponse,
                new Object[] { bitUtils.getHex(commandByte), bitUtils.getHex(data), bitUtils.getHex(expectedReturn) });

        LOG.error(exception.getMessage());

        throw exception;
    }


    private boolean detectActiveBolus() throws PlugInBaseException
    {
        try
        {
            executeCommandWithRetry(MinimedCommandType.DetectBolus);
            // executeCommandRetry(MinimedCommand.DETECT_BOLUS);
        }
        catch (PlugInBaseException ex)
        {
            if (ex.errorCode > 0)
            {
                if (ex.errorCode == 12)
                    return true;
                else
                    throw ex;
            }
            else
                throw ex;

        }

        return false;
    }


    /**
     * Send Command To Device and Read data
     *
     * @param commandPacket command Packet instance
     * @throws PlugInBaseException
     */
    public void sendCommandToDeviceAndReadData(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("sendCommandToDeviceAndReadData -- START");

        sendCommandToDevice(commandPacket);

        if (lowLevelDebug)
            LOG.debug("Length: " + commandPacket.commandReply.getRawData().length + " Return data: "
                    + commandPacket.canReturnData());

        if (commandPacket.canReturnData())
        {
            if (lowLevelDebug)
                LOG.debug("  canReturnData");

            if (commandPacket.getCommandResponseLength() > 64)
            {
                commandPacket.commandReply.setRawData(readDeviceDataPage(commandPacket));
            }
            else
            {
                commandPacket.commandReply.setRawData(readDeviceDataPacket(commandPacket));
            }
        }
        else
        {
            if (lowLevelDebug)
                LOG.debug("  no return data");

            checkDeviceAcknowledge(commandPacket);
        }

        if (lowLevelDebug)
            LOG.debug("sendCommandToDeviceAndReadData -- END");
    }


    private byte[] readDeviceDataPage(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        LOG.debug("readDeviceDataPage -- START");

        int expectedResponseLength = commandPacket.getCommandResponseLength();
        byte allData[] = new byte[0];
        int packetCount = 0;
        int nextPacket = 1;
        boolean exitLoop;
        LOG.debug("readDeviceDataPage: processing " + commandPacket.getFullCommandDescription());

        do
        {
            byte[] lastPacket = readDeviceDataPacket(commandPacket);

            if (lastPacket.length == 0)
            {
                exitLoop = true;
            }
            else
            {
                allData = bitUtils.concat(allData, lastPacket);
                LOG.debug("readDeviceDataPage: read packet [packetCount=" + packetCount + ", length="
                        + lastPacket.length + ", total_length = " + allData.length);
                packetCount++;
                boolean eod = (commandPacket.getDataCount() & 0x80) == 128;
                int currentPacket = commandPacket.getDataCount() & 0x7f;
                if (currentPacket != nextPacket)
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse,
                            new Object[] { "ERROR - sequence number mismatch: [expected=" + +nextPacket + ", read="
                                    + currentPacket + "]" });
                }
                if (++nextPacket > 127)
                    nextPacket = 1;

                exitLoop = allData.length >= expectedResponseLength || eod;

                if (!exitLoop)
                {
                    sendAcknowledgeToDevice();
                }

            }
        } while (!exitLoop);

        LOG.debug("readDeviceDataPage: " + commandPacket.getFullCommandDescription() + " returned " + allData.length
                + " bytes.");

        LOG.debug("readDeviceDataPage -- END");

        return allData;
    }


    /**
     * Send acknowledge To Device
     *
     * @throws PlugInBaseException
     */
    private void sendAcknowledgeToDevice() throws PlugInBaseException
    {
        if (debug)
            LOG.debug("sendAcknowledgeToDevice: command " + bitUtils.getHex(COMMAND_6_ACK));

        this.writeToPhysicalPort(createTransmitPacketForCommand(createCommandPacket(MinimedCommandType.CommandAck)));

        readAcknowledgeFromDevice();
        readDeviceReadyByte(false);
    }


    private byte[] readDeviceDataPacket(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (debug)
            LOG.debug("readDeviceDataPacket -- START");

        byte data[] = sendBackspaceToDeviceAndGetAndDecryptData(commandPacket);

        if (data[4] == 21)
        {
            int k = data[5];
            if (k == 13)
            {
                LOG.debug("readDeviceDataPacket: NAK received = no more data.");

                if (debug)
                    LOG.debug("readDeviceDataPacket -- END");

                return new byte[0];
            }
            else
            {
                String errorString = String.format(
                    "readDeviceDataPacket FAILED - NAK received [description=%s, errorCode=%s, errorDescription=%s, data=%s]",
                    commandPacket.getFullCommandDescription(), bitUtils.getHex(k), getNAKDescription(k),
                    bitUtils.getHex(data));

                LOG.error(errorString);
                throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRCDesc,
                        new Object[] { errorString });
            }

        }
        else
        {
            if (data[4] != commandPacket.getCommandCode())
            {
                String errorString = String.format(
                    "readDeviceDataPacket FAILED - BAD command value [description=%s, commandValueReceived=%s, commandValueExpected=%s, data=%s]",
                    commandPacket.getFullCommandDescription(), bitUtils.getHex(data[4]),
                    bitUtils.getHex(commandPacket.getCommandCode()), bitUtils.getHex(data));
                LOG.error(errorString);
                throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRCDesc,
                        new Object[] { errorString });
            }

            commandPacket.setDataCount(data[5]);
            int size = data.length - 6 - 1;

            byte endData[] = new byte[size];
            System.arraycopy(data, 6, endData, 0, size);

            LOG.debug("readDeviceDataPacket: packed decoded = [" + bitUtils.getHex(endData) + "]");

            if (debug)
                LOG.debug("readDeviceDataPacket -- END");

            return endData;
        }
    }


    /**
     * Read data until empty and then remove ACK bit
     *
     * @return byte array with result
     * @throws PlugInBaseException
     */
    private byte[] readDataUntilEmptyAndRemoveACK() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readDataUntilEmptyAndRemoveACK -- START");

        boolean ackFound = false;

        MedtronicUtil.sleepPhysicalCommunication();

        byte deviceData[] = this.readDataUntilEmpty();

        if (this.lowLevelDebug)
            LOG.debug("Read from Device: " + this.bitUtils.getHex(deviceData));

        if (MinimedResponseStatus.Acknowledge.getStatusCode() == (deviceData[deviceData.length - 1]))
        {
            if (this.lowLevelDebug)
                LOG.debug("Found ACK");

            ackFound = true;
            deviceData = this.bitUtils.getByteSubArray(deviceData, 0, deviceData.length - 1, deviceData.length);
        }

        LOG.debug("Read: [" + this.bitUtils.getHexCompact(deviceData) + "] ");

        if (!ackFound)
            readAcknowledgeFromDevice();

        if (lowLevelDebug)
            LOG.debug("readDataUntilEmptyAndRemoveACK -- END");

        return deviceData;
    }


    /**
     * Check Device Acknowledge - this is used when there is no data returned.
     *
     * @param commandPacket command packet
     * @return true is acknowledge found
     * @throws PlugInBaseException
     */
    private boolean checkDeviceAcknowledge(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (debug)
            LOG.info("checkDeviceAcknowledge: trying to find ACK packet...");

        byte endData[] = sendBackspaceToDeviceAndGetAndDecryptData(commandPacket);

        if (endData[4] != COMMAND_6_ACK)
        {
            int errorCode = endData[5];

            throw new PlugInBaseException(PlugInExceptionType.DeviceReturnedError,
                    new Object[] { commandPacket.commandType.name(), //
                                   errorCode, //
                                   getNAKDescription(errorCode), //
                                   bitUtils.getHex(endData) //
            });
        }
        else
        {
            if (debug)
                LOG.info("checkDeviceAcknowledge: OK response (ACK and data).");

            return true;
        }
    }


    /**
     * Send BS to Device, get Data, decrypt and Check Data
     *
     * @param commandPacket CommandPacket instance
     * @return array of bytes returned
     * @throws PlugInBaseException
     */
    private byte[] sendBackspaceToDeviceAndGetAndDecryptData(MinimedCommandPacket commandPacket)
            throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("sendBackspaceToDeviceAndGetAndDecryptData");

        this.readStatusOfDevice();

        this.writeToPhysicalPort(COMMAND_8_BS);

        // MedtronicUtil.sleepPhysicalCommunication();

        byte endData[] = this.decrypt(this.readDataUntilEmptyAndRemoveACK());

        checkHeaderAndCRC(commandPacket, endData);

        return endData;
    }


    public boolean checkHeaderAndCRC(MinimedCommandPacket commandPacket, byte[] arrayData) throws PlugInBaseException
    {
        int crcFromRecord = arrayData[arrayData.length - 1];
        int crcCalculated = bitUtils.computeCRC8(arrayData, 0, (arrayData.length - 1)) & 0xff;

        if (crcFromRecord != crcCalculated)
        {
            String error = "Command " + commandPacket.getFullCommandDescription() + " returned bad CRC (expected="
                    + crcFromRecord + ", calculated=" + crcCalculated + ")";
            LOG.error(error);

            throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRCDesc, new Object[] { error });
        }

        if (arrayData[0] != (byte) 167)
        {
            String error = "Command " + commandPacket.getFullCommandDescription() + " returned bad type code (expected="
                    + bitUtils.getHex(167) + ", returned=" + bitUtils.getHex(arrayData[0]) + ")";
            LOG.error(error);
            throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRCDesc, new Object[] { error });
        }

        if (MedtronicUtil.getSerialNumberBCD()[0] != arrayData[1] || //
                MedtronicUtil.getSerialNumberBCD()[1] != arrayData[2] || //
                MedtronicUtil.getSerialNumberBCD()[2] != arrayData[3])
        {
            String error = "Command " + commandPacket.getFullCommandDescription()
                    + " returned bad serial number code (expected="
                    + returnSerialBCD(null, MedtronicUtil.getSerialNumberBCD()) + ", returned="
                    + returnSerialBCD(arrayData, null) + ")";
            LOG.error(error);
            throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRCDesc, new Object[] { error });
        }
        else
            return true;
    }


    private String returnSerialBCD(byte[] dataByte, int[] dataInt)
    {
        if (dataByte != null)
        {
            return bitUtils.getHex(dataByte[1]) + " " + bitUtils.getHex(dataByte[2]) + " "
                    + bitUtils.getHex(dataByte[3]);
        }
        else
        {
            return bitUtils.getHex(dataInt[0]) + " " + bitUtils.getHex(dataInt[1]) + " " + bitUtils.getHex(dataInt[2]);
        }
    }


    /**
     * Execute command with Retry
     *
     * @param commandType comandType (which command)
     * @return MinimedCommandReply command reply
     * @throws PlugInBaseException
     */
    public MinimedCommandReply executeCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {
        int retry = 0;
        int max_retry = 3;

        do
        {
            try
            {
                MinimedCommandPacket cmdPacket = createCommandPacket(commandType);
                return executeCommandWithRetry(cmdPacket);
            }
            catch (PlugInBaseException ex)
            {
                retry++;

                if (retry <= max_retry)
                {
                    LOG.warn("Problem getting data: Retrying: {}/{} Ex.: {}", retry, max_retry, ex.getMessage(), ex);
                }
                else
                {
                    LOG.warn("Problem getting data: Stopping retries. Ex.: {}" + ex.getMessage(), ex);
                    throw ex;
                }

            }
        } while (retry <= max_retry);

        return null;
    }


    public MinimedCommandReply executeCommandWithRetry(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        return executeCommandWithRetry(commandPacket, true);
    }


    /**
     * Execute command with Retry
     *
     * @param commandPacket command packet
     * @return MinimedCommandReply reply instance
     * @throws PlugInBaseException
     */
    public MinimedCommandReply executeCommandWithRetry(MinimedCommandPacket commandPacket, boolean showHeader)
            throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("executeCommandWithRetry(MinimedCommandPacket,boolean) -- START");

        if ((debug) && (showHeader))
        {
            LOG.info("============================================================");
            LOG.info("   executeCommandWithRetry() - Command: " + commandPacket.commandType.name());
            LOG.info("============================================================");
        }

        this.setDeviceTimeout(commandPacket.commandType.maxAllowedTime);

        if (commandPacket.commandType.parameterType == MinimedCommandParameterType.NoParameters)
        {
            executeSingleCommand(commandPacket);
        }
        else if (commandPacket.commandType.parameterType == MinimedCommandParameterType.FixedParameters)
        {
            executeCommandWithParameters(commandPacket);
        }
        else
        {
            executeSingleCommand(commandPacket);
            this.executeSubCommands(commandPacket);
        }

        if (lowLevelDebug)
            LOG.debug("executeCommandWithRetry(MinimedCommandPacket,boolean) -- END");

        return commandPacket.commandReply;

        // this.setDeviceTimeout(commandPacket.commandType.maxAllowedTime);
        // sendCommandToDeviceAndReadData(commandPacket);
        //
        // if (commandPacket.hasSubCommands())
        // {
        // this.executeSubCommands(commandPacket);
        // }
        //
        // return commandPacket.commandReply;

    }


    private MinimedCommandReply executeSingleCommand(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("executeSingleCommand -- START");

        this.sendCommandToDeviceAndReadData(commandPacket);

        if (lowLevelDebug)
            LOG.debug("executeSingleCommand -- START");

        return commandPacket.commandReply;
        // return commandPacket.commandReply;
    }


    public void executeCommandWithParameters(MinimedCommandPacket packet) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("executeCommandWithParameters -- START");

        MinimedCommandPacket commandPacket = packet.createCommandPacket();

        LOG.debug("{}", commandPacket.toString());

        executeSingleCommand(commandPacket);

        LOG.info("============================================================");
        LOG.info("   executeCommandWithRetry() - Command: " + packet.commandType.name() + " - Parameters command");
        LOG.info("============================================================");

        // MinimedCommandReply reply =
        executeSingleCommand(packet);

        if (lowLevelDebug)
            LOG.debug("executeCommandWithParameters -- START");

    }


    public void executeSubCommands(MinimedCommandPacket packet) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("executeSubCommands -- START");

        for (int i = 0; i < packet.commandType.getMaxRecords() - 1; i++)
        {
            packet.commandParameters[0] = (byte) i;
            packet.commandReply.resetData();
            packet.description = packet.commandType.commandDescription + "; Page-" + (i);
            packet.hasSubCommands = false;

            LOG.info("============================================================");
            LOG.info("   executeCommandWithRetry() - Command: " + packet.commandType.name() + "  - Sub page " + i);
            LOG.info("============================================================");

            MinimedCommandReply reply = executeSingleCommand(packet);

            LOG.debug("{} -- Subpage: {} / {} with {} bytes of data.", packet.commandType.name(), i,
                packet.commandType.getMaxRecords(), reply.getRawDataLength());

            MinimedDataPage dataPage = new MinimedDataPage(reply);
            dataPage.setTargetType(dataHandler.getDeviceTargetType());

            MedtronicUtil.getHistoryDecoderProcessor().processPage(dataPage, MedtronicUtil.getOutputWriter());
        }

        if (lowLevelDebug)
            LOG.debug("executeSubCommands -- END");

        // LOG.error("Execute sub commands not implemented.");
    }


    public static void addParametersToCommandBody(List<Byte> commandBody, MinimedCommandPacket commandPacket)
    {
        int counter = 0;
        for (Byte data : commandPacket.commandParameters)
        {
            commandBody.add(data);
            counter++;

        }

        fillParameterWithZeros(commandBody, counter);
    }


    private static void fillParameterWithZeros(List<Byte> commandBody, int startIndex)
    {
        for (int i = startIndex; i < 64; i++)
        {
            commandBody.add((byte) 0);
        }
    }


    public static String getNAKDescription(int i)
    {
        String s;
        if (i <= NAK_DESCRIPTIONS_TABLE.length - 1)
            s = NAK_DESCRIPTIONS_TABLE[i];
        else
            s = "UNKNOWN NAK DESCRIPTION";
        return s;
    }

    private static final String NAK_DESCRIPTIONS_TABLE[] = { "UNKNOWN NAK DESCRIPTION", //
                                                             "REQUEST PAUSE FOR 3 SECONDS", //
                                                             "REQUEST PAUSE UNTIL ACK RECEIVED", //
                                                             "CRC ERROR", //
                                                             "REFUSE PROGRAM UPLOAD", //
                                                             "TIMEOUT ERROR", //
                                                             "COUNTER SEQUENCE ERROR", //
                                                             "PUMP IN ERROR STATE", //
                                                             "INCONSISTENT COMMAND REQUEST", //
                                                             "DATA OUT OF RANGE", //
                                                             "DATA CONSISTENCY", //
                                                             "ATTEMPT TO ACTIVATE UNUSED PROFILES", //
                                                             "PUMP DELIVERING BOLUS", //
                                                             "REQUESTED HISTORY BLOCK HAS NO DATA", //
                                                             "HARDWARE FAILURE" };

}
