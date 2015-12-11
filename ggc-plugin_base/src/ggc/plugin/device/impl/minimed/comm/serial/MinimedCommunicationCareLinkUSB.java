package ggc.plugin.device.impl.minimed.comm.serial;

import java.nio.ByteBuffer;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedCommandPacket;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.data.dto.CareLinkUSBResponseDto;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedResponseStatus;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.plugin.util.LogEntryType;

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
 *  Filename:     MinimedCommunicationCareLinkUSB
 *  Description:  Communication class for CareLinkUSB (ComLink2) device. 
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCommunicationCareLinkUSB extends MinimedSerialCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCommunicationCareLinkUSB.class);

    private static final int IO_DELAY_MS = 100;
    private static final int IO_DELAY_PAGE_READ_MS = 10;
    private static final int INIT_DELAY_MS = 1000;
    private static final int MAX_INIT_RETRY = 5;

    private boolean writeHistoryDataToFile = true;


    /**
     * Constructor
     *
     * @param minimedDataHandler data Handler
     */
    public MinimedCommunicationCareLinkUSB(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
    {
        super(dataAccess, minimedDataHandler);
    }


    public void preInitInterface()
    {
        serialBCD = MinimedUtil.getConnectionParameters().serialNumberBCD;
        this.lowLevelDebug = false;
        MinimedUtil.setLowLevelDebug(this.lowLevelDebug);
        MinimedUtil.setIoDelay(IO_DELAY_MS);
    }


    @Override
    public SerialSettings getSerialSettings()
    {
        SerialSettings ss = new SerialSettings();
        ss.baudRate = 9600;
        ss.dataBits = SerialSettingsType.DataBits8;
        ss.parity = SerialSettingsType.ParityNone; // none
        ss.stopBits = SerialSettingsType.StopBits1;
        ss.dtr = false; // F
        ss.rts = false; // T

        return ss;
    }


    public void initializeCommunicationInterface() throws PlugInBaseException
    {
        if (debug)
            LOG.debug("InitCommunications (CareLinkUSB) Com Interface");

        for (int i = 1; i <= MAX_INIT_RETRY; i++)
        {
            try
            {
                createPhysicalPort();

                readUntilDrained();

                break;
            }
            catch (PlugInBaseException ex)
            {
                if (i == MAX_INIT_RETRY)
                {
                    LOG.warn("Error on initialize. Ex.: " + ex, ex);
                    throw ex;
                }
                else
                {
                    LOG.warn("Error on initialize. Retrying. Ex.: " + ex, ex);
                    MinimedUtil.sleepMs(INIT_DELAY_MS);
                }
            }
        }

        if (debug)
            LOG.debug("InitCommunications (CareLinkUSB) Device");

        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        initializeCareLinkUSBInterface();

    }


    public MinimedCommandReply executeCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {
        MinimedCommandPacket commandPacket = new MinimedCommandPacket(commandType);
        executeCommandWithRetry(commandPacket);

        return commandPacket.commandReply;
    }


    @Override
    protected void executeHistoryCommandWithRetry(MinimedCommandType minimedCommandType) throws PlugInBaseException
    {
        MinimedCommandPacket commandPacket = new MinimedCommandPacket(minimedCommandType);

        int rec = 0;

        for (; rec < minimedCommandType.getMaxRecords(); rec++)
        {
            commandPacket.clearReply();

            commandPacket.commandParameters = new byte[] { (byte) rec };

            MinimedUtil.getOutputWriter().writeLog(LogEntryType.DEBUG,
                "Downloading Data (" + minimedCommandType.name() + ") - Page " + rec);

            executeCommandWithRetry(commandPacket);

            MinimedDataPage dataPage = new MinimedDataPage(commandPacket.commandReply);
            dataPage.setTargetType(dataHandler.getDeviceTargetType());

            if (writeHistoryDataToFile)
                writeToFile(commandPacket, dataPage);

            MinimedUtil.getHistoryDecoderProcessor().processPage(dataPage, MinimedUtil.getOutputWriter());

            LOG.debug("Read {} page(s) out of {}.", rec, commandPacket.commandType.maxRecords);

            dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);
        }

        LOG.debug("Total pages read:  {} /  {}.", rec, commandPacket.commandType.maxRecords);

    }


    public byte[] createTransmitPacketForCommand(MinimedCommandPacket commandPacket)
    {
        int parameterCount = commandPacket.commandParameterCount;

        byte packet[] = new byte[parameterCount + 16];

        packet[0] = 1;
        packet[1] = 0;
        packet[2] = (byte) 167;
        packet[3] = 1;

        packet[4] = (byte) serialBCD[0];
        packet[5] = (byte) serialBCD[1];
        packet[6] = (byte) serialBCD[2];
        packet[7] = (byte) (0x80 | bitUtils.getHighByte(parameterCount));
        packet[8] = (byte) bitUtils.getLowByte(parameterCount);
        packet[9] = (byte) (commandPacket.commandType.commandCode != 93 ? 0 : 85);
        packet[10] = (byte) commandPacket.commandType.allowedRetries;

        int recordsRequired = calcRecordsRequired(commandPacket.getResponseSize());
        packet[11] = (byte) (recordsRequired <= 1 ? recordsRequired : 2);
        packet[12] = 0;
        packet[13] = (byte) commandPacket.commandType.commandCode;
        packet[14] = (byte) bitUtils.computeCRC8(packet, 0, 14);

        int j = 15;

        if (parameterCount > 0)
        {
            System.arraycopy(commandPacket.commandParameters, 0, packet, j, parameterCount);
            j += parameterCount;
            packet[j] = (byte) bitUtils.computeCRC8(commandPacket.commandParameters, 0, parameterCount);
        }

        return packet;
    }


    private int calcRecordsRequired(int length)
    {
        int j = length / 64;
        int k = length % 64;
        return (j + (k <= 0 ? 0 : 1));
    }


    private void initializeCareLinkUSBInterface() throws PlugInBaseException
    {
        sleepMs(2000);

        executeComLinkUSBCommand(CareLinkUSBCommand.ProductInfo, null);
        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        executeComLinkUSBCommand(CareLinkUSBCommand.UsbInterfaceStats, null);
        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        executeComLinkUSBCommand(CareLinkUSBCommand.RadioInterfaceStats, null);
        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        executeComLinkUSBCommand(CareLinkUSBCommand.SignalStrength, null);
        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);
    }


    public int closeCommunicationInterface() throws PlugInBaseException
    {
        LOG.debug("closeCommunicationInterface()");
        return 0;
    }


    public int closeDevice() throws PlugInBaseException
    {
        LOG.debug("closeDevice()");

        if ((commHandler != null) && (commHandler.isDeviceConnected()))
        {
            commHandler.disconnectDevice();
            commHandler = null;
        }

        return 0;
    }


    @Override
    public Logger getLocalLog()
    {
        return LOG;
    }


    private CareLinkUSBResponseDto checkAcknowledgeFromCareLink(byte[] dataIn, CareLinkUSBCommand command)
            throws PlugInBaseException
    {
        return checkAcknowledgeFromCareLink(dataIn, false, command);
    }


    private CareLinkUSBResponseDto checkAcknowledgeFromCareLink(byte[] dataIn, boolean throwException,
            CareLinkUSBCommand command) throws PlugInBaseException
    {
        CareLinkUSBResponseDto dto = new CareLinkUSBResponseDto();

        byte[] header = bitUtils.getByteSubArray(dataIn, 0, 3);
        byte[] data = bitUtils.getByteSubArray(dataIn, 3, dataIn.length - 3);

        dto.header = header;
        dto.data = data;

        // System.out.println("Header: " + bitUtils.getHex(dto.header));
        // System.out.println("Body: " + bitUtils.getHex(dto.data));

        dto.rawData = dataIn;

        if ((command != null) && (command == CareLinkUSBCommand.ReadData))
        {
            if (!MinimedResponseStatus.hasStatus(MinimedResponseStatus.ReceivingData, header[0]))
            {
                throw new PlugInBaseException(PlugInExceptionType.WrongResponseStatus,
                        new Object[] { MinimedResponseStatus.ReceivingData,
                                       MinimedResponseStatus.getAllStatusesAsString(header[0]) });
            }
        }
        else
        {
            if (!MinimedResponseStatus.hasStatus(MinimedResponseStatus.ReceivedData, header[0]))
            {
                throw new PlugInBaseException(PlugInExceptionType.WrongResponseStatus,
                        new Object[] { MinimedResponseStatus.ReceivedData,
                                       MinimedResponseStatus.getAllStatusesAsString(header[0]) });
            }

            if (MinimedResponseStatus.Acknowledge.getStatusCode() == header[1])
            {
                dto.errorCode = 0;
            }
            else if (MinimedResponseStatus.NotAcknowledge.getStatusCode() == header[1])
            {
                dto.errorCode = header[2];

                CareLinkUSBNak nak = CareLinkUSBNak.getByCode(header[2]);

                dto.errorDescription = nak.getDescription();
                LOG.warn("Received NAK ({}). {}", dto.errorDescription, throwException ? "" : "Ignoring for now.");

                if (throwException)
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceNAK, new Object[] { nak.name() });
                }
            }
            else
            {
                dto.errorCode = -1;
                dto.errorDescription = "Bad ACK/NAK response received " + header[1] + ".";

                LOG.warn("Bad ACK/NAK received ({}). {}", header[1], throwException ? "" : "Ignoring for now.");

                if (throwException)
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseCommand,
                            new Object[] { header[1], "Acknowledge/Not Acknowledge" });
                }
            }
        }

        return dto;
    }


    /**
     * For executing CareLinkUSBCommand commands (called internally)
     *
     * @param command CareLinkUSBCommand instance
     * @return CareLinkResponseDto instance
     * @throws PlugInBaseException
     */
    public CareLinkUSBResponseDto executeComLinkUSBCommand(CareLinkUSBCommand command,
            MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        return executeComLinkUSBCommand(command, 64, commandPacket);
    }


    /**
     * For executing CareLinkUSBCommand commands (called internally)
     *
     * @param command CareLinkUSBCommand instance
     * @param size expected size of returned value (default is 64, can change only in case of ReadData command.
     * @return CareLinkResponseDto instance
     * @throws PlugInBaseException
     */
    public CareLinkUSBResponseDto executeComLinkUSBCommand(CareLinkUSBCommand command, int size,
            MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        dataHandler.checkIfDownloadCanceled();

        if (debug)
        {
            LOG.info("============================================================");

            if (commandPacket == null)
                LOG.info("   execute ComLinkUSBCommand: {} (size={})", command.name(), size);
            else
                LOG.info("   execute ComLinkUSBCommand: {} (size={}) [{}]", command.name(), size,
                    commandPacket.commandType.name());

            LOG.info("============================================================");
        }

        byte[] packet;

        if (size == 64)
        {
            packet = command.preparePacket();
        }
        else
        {
            packet = command.preparePacket(size);
        }

        CareLinkUSBResponseDto responseDto = executeCommandBase(packet, size, command);

        return convertComLinkUSBCommandResponse(command, responseDto);
    }


    private CareLinkUSBResponseDto executeCommandBase(byte[] packet, int size, CareLinkUSBCommand command)
            throws PlugInBaseException
    {
        PlugInBaseException exception = null;

        for (int i = 0; i < 4; i++)
        {
            try
            {
                if (i > 0)
                {
                    if (lowLevelDebug)
                        LOG.debug("Retrying command: {} of 3", i);
                }

                MinimedUtil.sleepPhysicalCommunication();

                if (lowLevelDebug)
                    LOG.info("Write packet: {}", bitUtils.getHex(packet));

                commHandler.write(packet);

                if (lowLevelDebug)
                    LOG.info("Max response size set to: {}", size);

                MinimedUtil.sleepPhysicalCommunication();

                byte[] rawResponse = readWithSize(size);

                if (lowLevelDebug)
                    LOG.info("Raw Response: {}", bitUtils.getHex(rawResponse));

                return checkAcknowledgeFromCareLink(rawResponse, command);
            }
            catch (PlugInBaseException ex)
            {
                sleepMs(2000);
                exception = ex;
                LOG.warn("ACK problem. Ex.: ", ex.getMessage());
            }
        }

        throw exception;
    }


    /**
     * Convert ComLinkUSBCommand response (most of this data is descriptive in nature, but some of it used for retrieving data)
     *
     * @param command CareLinkUSBCommand instance
     * @param responseDto CareLinkResponseDto response dto instance
     * @return modified (if required) CareLinkResponseDto instance
     */
    private CareLinkUSBResponseDto convertComLinkUSBCommandResponse(CareLinkUSBCommand command,
            CareLinkUSBResponseDto responseDto)
    {
        switch (command)
        {
            case LinkStatus:
                decodeLinkStatus(responseDto);
                break;

            case ProductInfo:
                decodeProductInfo(responseDto.data);
                break;

            case RadioInterfaceStats:
                decodeInterfaceStats(responseDto.data, "Radio");
                break;

            case UsbInterfaceStats:
                decodeInterfaceStats(responseDto.data, "USB");
                break;

            case SignalStrength:
                LOG.debug("Signal Strength= {} dBm",
                    responseDto.data[0] < 0 ? (responseDto.data[0] + 256) : responseDto.data[0]);
                break;

            case ReadData:
                decodeReadData(responseDto);
                break;
        }

        return responseDto;
    }


    private void decodeReadData(CareLinkUSBResponseDto responseDto)
    {
        if (lowLevelDebug)
            decodeReadDataDebug(responseDto);
        else
            decodeReadDataNoDebug(responseDto);
    }


    private void decodeReadDataNoDebug(CareLinkUSBResponseDto responseDto)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("ReadData [size=%s]", responseDto.data.length));

        String dt = dataAccess.getGregorianCalendarAsString(new GregorianCalendar());

        sb.append(String.format("\nTime when decodeReadData: %s", dt));

        byte[] rawDataBefore = responseDto.rawData;

        responseDto.eod = (responseDto.rawData[5] & 0x80) > 0;

        sb.append(String.format("\nEOD: %s", responseDto.eod));
        sb.append(String.format("\nBefore modification: %s",
            bitUtils.getHex(new int[] { responseDto.rawData[5], responseDto.rawData[6] })));

        responseDto.rawData[5] = (byte) (responseDto.rawData[5] & 0x7F);

        sb.append(String.format("\nAfter modification: %s",
            bitUtils.getHex(new int[] { responseDto.rawData[5], responseDto.rawData[6] })));

        int resultLength = bitUtils.makeInt(responseDto.rawData[5], responseDto.rawData[6]); // .getShort(5);

        sb.append(String.format("\nResult length: %s", resultLength));
        sb.append(String.format("\nResponse length: %s", responseDto.rawData.length));

        int crc = responseDto.rawData[responseDto.rawData.length - 1];

        responseDto.data = bitUtils.getByteSubArray(responseDto.rawData, 13, resultLength);

        byte expectedCrc = (byte) MinimedUtil.getBitUtils().computeCRC8(responseDto.data);

        if (crc != expectedCrc)
        {
            LOG.error("CRC Expected: {}, Was: {}. Ignored for now.", crc, expectedCrc);

            LOG.debug("ReadData came to CRC error. Dumping debug data. Files will be also created.\n{}", sb.toString());

            writeToFile("decodeReadData_" + dt + "_complete.bin", rawDataBefore);
            writeToFile("decodeReadData_" + dt + "_changed.bin", responseDto.rawData);
        }
    }


    private void decodeReadDataDebug(CareLinkUSBResponseDto responseDto)
    {
        LOG.debug("ReadData [size={}]", responseDto.data.length);

        String dt = dataAccess.getGregorianCalendarAsString(new GregorianCalendar());

        LOG.debug("Time when decodeReadData: " + dt);

        byte[] rawDataBefore = responseDto.rawData;

        responseDto.eod = (responseDto.rawData[5] & 0x80) > 0;

        LOG.info("EOD: {}", responseDto.eod);
        LOG.info("Before modification: {}",
            bitUtils.getHex(new int[] { responseDto.rawData[5], responseDto.rawData[6] }));

        responseDto.rawData[5] = (byte) (responseDto.rawData[5] & 0x7F);

        LOG.info("After modification: {}",
            bitUtils.getHex(new int[] { responseDto.rawData[5], responseDto.rawData[6] }));

        int resultLength = bitUtils.makeInt(responseDto.rawData[5], responseDto.rawData[6]); // .getShort(5);

        LOG.info("Result length: {}", resultLength);
        LOG.info("Response length: {}", responseDto.rawData.length);

        int crc = responseDto.rawData[responseDto.rawData.length - 1];

        responseDto.data = bitUtils.getByteSubArray(responseDto.rawData, 13, resultLength);

        byte expectedCrc = (byte) MinimedUtil.getBitUtils().computeCRC8(responseDto.data);

        LOG.debug("CRC: expected={}, calculated={}", expectedCrc, crc);

        if (crc != expectedCrc)
        {
            LOG.error("CRC Expected: {}, Was: {}. Ignored for now.", crc, expectedCrc);

            writeToFile("decodeReadData_" + dt + "_complete.bin", rawDataBefore);
            writeToFile("decodeReadData_" + dt + "_changed.bin", responseDto.rawData);
        }
        else
        {
            LOG.info("CRC checks out!");
        }

    }


    private void decodeLinkStatus(CareLinkUSBResponseDto responseDto)
    {
        responseDto.parsedValue = bitUtils.makeInt(responseDto.data[3], responseDto.data[4]);
        LOG.debug("LinkStatus: size={}", responseDto.parsedValue);
    }


    private void decodeInterfaceStats(byte[] dataResponse, String ifName)
    {
        String interfaceStats = "USB CRC Errors: {}, " + //
                "USB Sequence Errors: {}, " + //
                "USB NAK Errors: {}, " + //
                "USB Timeouts: {}, " + //
                "USB Transmitted: {}, " + //
                "USB Received: {}.";

        int received = bitUtils.makeInt(dataResponse[4], dataResponse[5], dataResponse[6], dataResponse[7]);
        int transmitted = bitUtils.makeInt(dataResponse[8], dataResponse[9], dataResponse[10], dataResponse[11]);

        if (ifName.equals("Radio"))
        {
            interfaceStats = interfaceStats.replaceAll("USB", ifName);
        }

        LOG.info(interfaceStats, dataResponse[0], dataResponse[1], dataResponse[2], dataResponse[3], transmitted,
            received);
    }


    private void decodeProductInfo(byte[] dataResponse)
    {
        int interfaceCount = dataResponse[18];

        StringBuilder sb = new StringBuilder();
        for (int j = 19; j < 19 + interfaceCount * 2; j += 2)
        {
            sb.append("Interface");
            sb.append(dataResponse[j]);
            sb.append("=");

            int interfaceType = dataResponse[j + 1];

            if (interfaceType == 1)
            {
                sb.append("USB");
            }
            else if (interfaceType == 3)
            {
                sb.append("Paradigm RF");
            }
            else
            {
                sb.append("Undefined interface");
            }

            sb.append("; ");
        }

        String rfCode;

        if (dataResponse[5] == 0)
            rfCode = "868.35Mhz";
        else if ((dataResponse[5] == 1) || (dataResponse[5] == (byte) 255))
            rfCode = "916.5Mhz";
        else
            rfCode = "Unknown";

        LOG.info("readProductInfo: Serial #=" + dataResponse[0] + dataResponse[1] + dataResponse[2] + //
                ", Product Version=" + dataResponse[3] + "." + dataResponse[4] + //
                ", RF Freq=" + rfCode + //
                ", Product Description=" + bitUtils.makeString(dataResponse, 6, 15).trim() + //
                ", Software Version=" + dataResponse[16] + "." + dataResponse[17] + //
                ", #Interfaces=" + interfaceCount + ": " + sb.toString());
    }


    public MinimedCommandReply executeCommandWithRetry(MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        dataHandler.checkIfDownloadCanceled();

        if (debug)
        {
            LOG.info("============================================================");
            LOG.info("   execute - Command: {} {}", commandPacket.commandType.name(),
                (commandPacket.commandParameters != null
                        ? "(parameters: " + bitUtils.getByteArrayShow(commandPacket.commandParameters) + ")" : ""));
            LOG.info("============================================================");
        }

        byte[] packetMine = createTransmitPacketForCommand(commandPacket);

        if (lowLevelDebug)
        {
            LOG.debug("Packet: " + bitUtils.getHex(packetMine));
        }

        executeCommandBase(packetMine, 64, null);
        sleepMs(commandPacket.commandType.maxAllowedTime);

        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        boolean finished = false;

        // boolean finished = (!commandPacket.commandType.canReturnData());
        //
        // if (finished)
        // {
        // dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 2);
        // return null;
        // }

        byte[] allData = new byte[0];

        int counter = 0;
        while (!finished || counter == 30)
        {
            int responseSize = getRemoteResponseSize(commandPacket.commandType.minimalBufferSizeToStartReading,
                commandPacket);

            if (lowLevelDebug)
                LOG.info("LinkStatus reports size: {}", responseSize);

            // for (int i = 0; i < retries; i++) {
            CareLinkUSBResponseDto responseDto = executeComLinkUSBCommand(CareLinkUSBCommand.ReadData, responseSize,
                commandPacket);

            allData = bitUtils.concat(allData, responseDto.data);

            finished = responseDto.isEOD();

            if (lowLevelDebug)
                LOG.info("My data: {}", bitUtils.getHex(allData));

            if (responseDto.data.length == 0)
            {
                sleepMs(250);
                LOG.warn("Length was 0. We will retry soon.");
            }

            if (lowLevelDebug)
            {
                LOG.debug("ReadRadio Response: {}", bitUtils.getHex(responseDto.data.length));
                LOG.debug("ReadRadio Response size: {}", responseDto.rawData.length);
                LOG.debug("LinkStatus reports size: {}", responseSize);
            }

            if (responseDto.isEOD())
            {
                break;
            }

            counter += 1;
            sleepMs(commandPacket.commandType.maxAllowedTime);
        }

        LOG.debug("Final Response Length for {} is {}", commandPacket.commandType.name(), allData.length);

        commandPacket.commandReply.setRawData(allData);
        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        return commandPacket.commandReply;
    }


    private int getRemoteResponseSize(int minSize, MinimedCommandPacket commandPacket) throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("getRemoteResponseSize() minSize={} - START", minSize);

        int responseSize = 0;
        for (int i = 0; i < 30; i++)
        {
            CareLinkUSBResponseDto responseDto = executeComLinkUSBCommand(CareLinkUSBCommand.LinkStatus, commandPacket);

            if (lowLevelDebug)
                LOG.info("Response size: {} - Retry {}", responseDto.parsedValue, i);

            if (responseDto.parsedValue > minSize)
            {
                if (lowLevelDebug)
                {
                    LOG.info("About to break");
                    LOG.info("Response: {}", bitUtils.getHex(responseDto.data));
                }
                responseSize = responseDto.parsedValue;
                break;
            }

            sleepMs(2000);
        }

        if (lowLevelDebug)
            LOG.info("getRemoteResponseSize() Returning: {} - END", responseSize);

        dataHandler.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

        return responseSize;
    }


    // private String getComLink2NAKDescription(int i)
    // {
    // String s;
    // if (i <= COMLINK2_NAK_DESCRIPTIONS_TABLE.length - 1)
    // s = COMLINK2_NAK_DESCRIPTIONS_TABLE[i];
    // else
    // s = "UNKNOWN NAK DESCRIPTION";
    // return s;
    // }
    //
    // private static final String COMLINK2_NAK_DESCRIPTIONS_TABLE[] = { "NO
    // ERROR", "CRC MISMATCH", "COMMAND DATA ERROR",
    // "COMM BUSY AND/OR COMMAND CANNOT BE EXECUTED",
    // "COMMAND NOT SUPPORTED" };

    public int initDevice() throws PlugInBaseException
    {
        LOG.debug("initDevice (CareLink USB) - Start");

        executeCommandWithRetry(MinimedCommandType.RFPowerOn);

        LOG.debug("initDevice (CareLink USB) - End");

        return 0;
    }

    enum CareLinkUSBCommand
    {
        // TransmitPacket(1), //
        // Initialize(2), //
        LinkStatus(3, 0), //
        ProductInfo(4, 0), //
        RadioInterfaceStats(5, 0), //
        UsbInterfaceStats(5, 1), //
        SignalStrength(6, 0), //
        ReadData(12, 0), //

        ;

        byte[] packet = null;


        CareLinkUSBCommand(int... code)
        {
            packet = new byte[code.length];

            for (int i = 0; i < code.length; i++)
            {
                packet[i] = (byte) code[i];
            }
        }


        public byte[] preparePacket()
        {
            return packet;
        }


        public byte[] preparePacket(int size)
        {
            byte[] packetData = MinimedUtil.getBitUtils().concat(packet,
                ByteBuffer.allocate(2).putShort((short) size).array());

            byte[] crc = { (byte) MinimedUtil.getBitUtils().computeCRC8(packetData) };

            LOG.debug("CRC: {}", (byte) MinimedUtil.getBitUtils().computeCRC8(packetData));

            return MinimedUtil.getBitUtils().concat(packetData, crc);
        }

    }

    enum CareLinkUSBNak
    {
        UnknownNAKError(-1, "UNKNOWN NAK DESCRIPTION"), //
        NoError(0, "NO ERROR"), //
        CrcMismatch(1, "CRC MISMATCH"), //
        CommandDataError(2, "COMMAND DATA ERROR"), //
        CommBusyOrCommandCantBeExecuted(3, "COMM BUSY AND/OR COMMAND CANNOT BE EXECUTED"), //
        CommandNotSupported(4, "COMMAND NOT SUPPORTED");

        static Map<Integer, CareLinkUSBNak> mapByCode;


        static
        {
            mapByCode = new HashMap<Integer, CareLinkUSBNak>();

            for (CareLinkUSBNak nak : values())
            {
                mapByCode.put(nak.getCode(), nak);
            }
        }

        int code;
        String description;


        CareLinkUSBNak(int code, String description)
        {
            this.code = code;
            this.description = description;
        }


        public static CareLinkUSBNak getByCode(int code)
        {
            if (mapByCode.containsKey(code))
                return mapByCode.get(code);
            else
                return CareLinkUSBNak.UnknownNAKError;
        }


        public int getCode()
        {
            return code;
        }


        public String getDescription()
        {
            return description;
        }
    }

}
