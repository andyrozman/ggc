package ggc.plugin.device.impl.animas.comm;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.enums.*;
import ggc.plugin.device.impl.animas.handler.AnimasDataConverter;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;
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
 *  Filename:     AnimasCommProtocolV2
 *  Description:  Communication class for all devices 1200 and after (till Vibe)
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */
public class AnimasCommProtocolV2 extends AnimasCommProtocolAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(AnimasCommProtocolV2.class);

    private static final int RETRIES_SERIAL_PORT_ON_INIT = 10;
    private static final int MAX_INIT_ATTEMPTS = 8;
    private static final int ENTRY_READ_TIMEOUT = 2000;
    private static final int ENTRY_READ_MAX_TIMEOUT_COUNT = 3;

    // command
    protected AnimasDevicePacket animasDevicePacket;
    public HashMap<AnimasDeviceCommand, Object> commandToSend = new HashMap<AnimasDeviceCommand, Object>();
    protected AnimasDataConverter dataConverter;

    // retry
    private boolean retryData;
    private boolean reconnectToDevice;
    protected boolean noReconnectMode = false;

    // connection transfer flags
    public boolean controlMode;
    public int nr;
    public int ns;
    public int acknowledgeRequired;
    public boolean xmitRights;
    public boolean connectionState;
    public boolean disconnectSignalSent;
    private boolean foundSerial = false;


    public AnimasCommProtocolV2(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader,
            OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
        this.setDebugMode(false, false);
    }


    protected int determineMaxProgressForOperation(AnimasTransferType baseTransferType)
    {
        int maxCount = 0;
        AnimasDataType[] dataTypes = AnimasDataType.values();

        for (AnimasDataType adt : dataTypes)
        {
            if ((adt.getBaseTransferType() == baseTransferType) && //
                    (this.baseData.isCommandAllowedForDeviceType(adt)))
            {
                maxCount += adt.getEntriesCount();
            }
        }

        return maxCount;
    }


    protected void sendRequestAndWait(AnimasDataType dataType, int startRecord, int numberOfRecords,
            int commandWaitTime) throws PlugInBaseException
    {
        if ((dataType.getBaseTransferType() != AnimasTransferType.All)
                && (!this.baseData.isCommandAllowedForDeviceType(dataType)))
        {
            return;
        }

        retryData = false;
        reconnectToDevice = false;
        int retryCount = 0;
        String retryString = "";
        AnimasDevicePacket adp = null;

        do
        {
            if (retryCount > 0)
            {
                retryString = " [retry " + retryCount + "]";
            }

            String logMsg;

            if (reconnectToDevice)
            {
                logMsg = "Device reading failed for " + this.animasDevicePacket.dataTypeObject.name()
                        + ", trying to reconnect and retry.";
                LOG.warn(logMsg);
                outputWriter.writeLog(LogEntryType.WARNING, logMsg);
                adp = this.animasDevicePacket;
                this.reconnectToDevice();

                if (!shouldWeRetryDownloadingData(adp))
                {
                    doPostProcessing();
                    return;
                }
                // retryData = true;
            }
            else
            {
                retryData = false;
            }

            if (startRecord == 0)
            {
                logMsg = "Downloading - " + dataType.getDebugDescription() + retryString;
                LOG.debug(logMsg);
                outputWriter.writeLog(LogEntryType.INFO, logMsg);
            }
            else
            {
                logMsg = "Downloading - " + dataType.getDebugDescription() + " #" + startRecord + retryString;
                LOG.debug(logMsg);
                outputWriter.writeLog(LogEntryType.INFO, logMsg);
            }

            // AnimasDevicePacket adp = this.animasDevicePacket;

            sendRequestToPump(dataType, startRecord, numberOfRecords);

            if (adp != null)
            {
                this.animasDevicePacket = adp;
                this.animasDevicePacket.downloadedQuantity = 0;
            }

            try
            {
                waitForDownloadedQuantity(Math.min(commandWaitTime, numberOfRecords));
            }
            catch (PlugInBaseException ex)
            {
                if (ex.getExceptionType() == PlugInExceptionType.DeviceUnreachableRetry)
                {
                    logMsg = "Device was unreachable, trying to retry [count=" + (retryCount + 1) + "]";
                    LOG.debug(logMsg);
                    outputWriter.writeLog(LogEntryType.WARNING, logMsg);

                    retryData = true;
                    reconnectToDevice = true;
                }
                else
                {
                    throw ex;
                }
            }

            AnimasUtils.sleepInMs(10L);

            retryCount++;

            if (retryCount > 3)
            {
                LOG.warn(
                    "Communication with device was stopped by device (device not " + "reachable anymore). Exiting.");
                throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByDevice);
            }

        } while (retryData);
    }


    protected void sendRequestAndWait(AnimasDataType dataType, int startRecord, int numberOfRecords,
            int commandWaitTime, int systemWaitTimeBefore) throws PlugInBaseException
    {
        AnimasUtils.sleepInMs(systemWaitTimeBefore);
        sendRequestAndWait(dataType, startRecord, numberOfRecords, commandWaitTime);
    }


    public boolean shouldWeRetryDownloadingData(AnimasDevicePacket devicePacket)
    {
        return true;
    }


    public void closeConnection() throws PlugInBaseException
    {
        if (this.baseData.doWeHaveConcurrentOperation("closeConnection"))
        {
            return;
        }

        if (!disconnectSignalSent)
        {
            sendDisconnectSignal();
            AnimasUtils.sleepInMs(1000L);
        }

        LOG.debug("Pump is disconnected.");

        this.disconnectDevice();

        throw new PlugInBaseException(PlugInExceptionType.CommunicationPortClosed);
    }


    public void sendInstructionsSignal(char[] txdata) throws PlugInBaseException
    {
        debugCommunication("sendInstructionsSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.Instruction, //
            AnimasDeviceCommand.ResponseReceivedRequired);

        int nst = AnimasUtils.getUnsignedShort(this.ns);
        this.xmitRights = false;

        this.ns = AnimasUtils.getUnsignedShort(((this.ns + 1) & 0x7));

        this.acknowledgeRequired = 1;

        while (this.acknowledgeRequired > 0)
        {
            char[] data = new char[2 + txdata.length];

            data[0] = ((char) (this.baseData.pumpConnectorInfo.connectionAddress + 1));
            data[1] = ((char) (16 + (this.nr * 32) + (nst * 2)));

            System.arraycopy(txdata, 0, data, 2, txdata.length);

            sendMessageToDevice(data);

            long timeOut = System.currentTimeMillis() + 20000;

            while ((this.acknowledgeRequired > 0))
            {
                if (this.isDataAvailable())
                {
                    this.readDataFromDevice();
                    timeOut = System.currentTimeMillis() + 20000;
                }

                if (System.currentTimeMillis() > timeOut)
                {
                    throw new PlugInBaseException(PlugInExceptionType.NoResponseFromDeviceForIssuedCommand,
                            new Object[] { this.animasDevicePacket.dataTypeObject.name() });
                }

                AnimasUtils.sleepInMs(100L);
            }
        }
    }


    public void sendResponseReceivedSignal() throws PlugInBaseException
    {
        debugCommunication("sendResponseReceivedSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.ResponseReceived, //
            AnimasDeviceCommand.ResponseReceivedRequired);

        this.xmitRights = false;

        sendMessageToDevice(new char[] { (char) (this.baseData.pumpConnectorInfo.connectionAddress + 1),
                                         (char) (17 + (this.nr * 32)) });
    }


    public void sendConnectSignal() throws PlugInBaseException
    {
        debugCommunication("sendConnectSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.Connect);

        if (!this.connectionState)
        {
            this.xmitRights = false;
            this.baseData.pumpConnectorInfo.connectionAddress += 2;

            sendMessageToDevice(new char[] { '\377', '\223', '\001', '\0', '\0', '\0', //
                                             (char) this.baseData.pumpConnectorInfo.deviceAdapter[0], //
                                             (char) this.baseData.pumpConnectorInfo.deviceAdapter[1], //
                                             (char) this.baseData.pumpConnectorInfo.deviceAdapter[2], //
                                             (char) this.baseData.pumpConnectorInfo.deviceAdapter[3], //
                                             (char) this.baseData.pumpConnectorInfo.connectionAddress });
            this.ns = 0;
            this.nr = 0;
        }
    }


    public void sendForcedDisconnectSignal() throws PlugInBaseException
    {
        debugCommunication("sendForcedDisconnectSignal");

        for (int i = 0; i < 256; i++)
        {
            char[] data = { (char) i, 'S' };
            sendMessageToDevice(data);
        }
    }


    public void sendDisconnectSignal() throws PlugInBaseException
    {
        debugCommunication("sendDisconnectSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.Disconnect);

        disconnectSignalSent = true;

        if (this.connectionState)
        {
            this.xmitRights = false;
            this.baseData.pumpConnectorInfo.connectionAddress += 2;

            sendMessageToDevice(new char[] { (char) (this.baseData.pumpConnectorInfo.connectionAddress + 1), 'S' });

            this.connectionState = false;
        }
    }


    public void sendHandshakeSignal() throws PlugInBaseException
    {
        LOG.debug("sendHandshakeSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.Handshake);

        for (int i = 0; i < 16; i++)
        {
            sendMessageToDevice(new char[] { '\377', '\277', '\001', '\0', '\0', '\0', '\377', '\377', '\377', '\377',
                                             '\002', (char) i });

            this.animasDevicePacket = new AnimasDevicePacket();

            if (this.isDataAvailable())
            {
                this.readDataFromDevice();
                break;
            }

            AnimasUtils.sleepInMs(500L);
        }
        this.xmitRights = true;
    }


    public void processAllOutboundData() throws PlugInBaseException
    {
        while (this.commandToSend.size() > 0)
        {
            processOutboundData();
        }
    }


    public void processOutboundData() throws PlugInBaseException
    {
        AnimasUtils.sleepInMs(10L);

        if (this.xmitRights)
        {
            if (this.hasCommandToSend(AnimasDeviceCommand.Handshake))
            {
                this.sendHandshakeSignal();
            }
            else if (this.hasCommandToSend(AnimasDeviceCommand.Connect))
            {
                this.sendConnectSignal();
            }
            else if (this.hasCommandToSend(AnimasDeviceCommand.Disconnect))
            {
                this.sendDisconnectSignal();
                this.closeConnection();
            }
            else if (this.hasCommandToSend(AnimasDeviceCommand.ResponseReceived))
            {
                this.sendResponseReceivedSignal();
            }
            else if (this.hasCommandToSend(AnimasDeviceCommand.Instruction))
            {
                this.sendInstructionsSignal(this.animasDevicePacket.commandToSend);
            }
            else if (this.hasCommandToSend(AnimasDeviceCommand.SO))
            {
                this.sendSOSignal();
            }

            if ((this.connectionState) && (this.hasCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired)))
            {
                this.sendResponseReceivedSignal();
            }
        }
    }


    private void sendSOSignal() throws PlugInBaseException
    {
        debugCommunication("sendSOSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.SO, //
            AnimasDeviceCommand.ResponseReceivedRequired);
        this.xmitRights = false;

        char data[] = { '\377', '\003', 'S', '0' };

        sendMessageToDevice(data);
    }


    public void openConnection() throws PlugInBaseException
    {
        this.openConnection(false);
    }


    public void openConnection(boolean reconnect) throws PlugInBaseException
    {
        boolean maxRetriesReached = false;
        int retries = 0;
        int retriesComm = 0;
        boolean pumpConnected = false;

        while (!maxRetriesReached)
        {
            resetConnectionState();
            AnimasUtils.sleepInMs(10L);

            if (initSerialDevice())
            {
                resetConnectionState();
                this.xmitRights = true;
                // this.da.rawSerialNumber = null;

                baseData.resetRawSerialData();

                AnimasUtils.sleepInMs(1000L);
                sendForcedDisconnectSignal();

                int inItAttempt = 0;

                do // 5
                {
                    LOG.debug("Downloader connected, Trying to find pump [retry=" + retriesComm + "]");
                    sendHandshakeSignal();
                    // this.processAllOutboundData();
                    AnimasUtils.sleepInMs(300L);
                    pumpConnected = this.baseData.isDownloaderSerialNumberSet();
                    inItAttempt++;
                    retriesComm++;
                    if (deviceReader.isDownloadCanceled())
                    {
                        throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);
                    }
                } while ((!pumpConnected) && (inItAttempt < MAX_INIT_ATTEMPTS));

                if (pumpConnected)
                {
                    break;
                }

                if (retriesComm > RETRIES_SERIAL_PORT_ON_INIT)
                {
                    maxRetriesReached = true;
                }

                AnimasUtils.sleepInMs(1000);
            }
            else
            {
                LOG.debug("Trying to connect to Downloader [retry=" + retries + "]");

                AnimasUtils.sleepInMs(1000);
                retries++;

                if (retries > RETRIES_SERIAL_PORT_ON_INIT)
                {
                    maxRetriesReached = true;
                }
            } // if - init

        } // while

        if (!pumpConnected)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
        }

        if (!reconnect)
            deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 5);

        getBasePumpData();

    }


    private void getBasePumpData() throws PlugInBaseException
    {
        if (this.baseData.isDownloaderSerialNumberSet())
        {
            AnimasUtils.sleepInMs(1000L);
            sendConnectSignal();

            if (DEBUG)
            {
                LOG.debug("Getting base data for device connected to Downloader with SN "
                        + this.baseData.pumpConnectorInfo.deviceAdapterSerialNumber);
            }

            AnimasUtils.sleepInMs(2500L);

            if (this.isDataAvailable())
            {
                this.readDataFromDevice();
            }

            this.sendRequestAndWait(AnimasDataType.SerialNumber, 0, 1, 1);

            while (!this.getData().isSoftwareCodeSet())
            {
                this.sendRequestAndWait(AnimasDataType.BGUnit, 0, 1, 1);
                this.sendRequestAndWait(AnimasDataType.SoftwareCode, 0, 1, 1);

                if (DEBUG)
                {
                    System.out.println("Software Code=[" + this.getData().pumpInfo.softwareCode + "]");
                }

                if (this.getData().isSoftwareCodeSet())
                {
                    if (this.getData().pumpInfo.softwareCode.charAt(0) != 'A')
                    {
                        this.sendRequestAndWait(AnimasDataType.FoodDbSize, 0, 1, 1);
                    }
                    else
                    {
                        deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);
                    }

                    this.dataConverter.decodePumpModel();

                    if (this.baseData.pumpInfo.deviceType == AnimasDeviceType.Animas_Unknown_Pump)
                    {
                        throw new PlugInBaseException(PlugInExceptionType.DeviceModelCouldNotBeIdentified);
                    }
                }
            }
        }
    }


    private void resetConnectionState()
    {
        this.controlMode = false;
        this.connectionState = false;
        this.baseData.pumpConnectorInfo.connectionAddress = 0;
        this.commandToSend.clear();
        this.xmitRights = false; //
    }


    public void sendRequestToPump(AnimasDataType animasDataType, int startingRecord, int recordCount)
            throws PlugInBaseException
    {
        char[] dataToSend = { 'R', 'I', //
                              (char) (animasDataType.getCode() & 0xFF), //
                              (char) ((animasDataType.getCode() & 0xFF00) >> 8), //
                              (char) (startingRecord & 0xFF), //
                              (char) ((startingRecord & 0xFF00) >> 8), //
                              (char) (recordCount & 0xFF), //
                              (char) ((recordCount & 0xFF00) >> 8) };

        this.animasDevicePacket = new AnimasDevicePacket();
        this.animasDevicePacket.dataTypeObject = animasDataType;
        this.animasDevicePacket.commandToSend = dataToSend;
        this.animasDevicePacket.downloadedQuantity = 0;
        this.animasDevicePacket.startingRecord = startingRecord;
        this.animasDevicePacket.recordCount = recordCount;

        if ((animasDataType.isDateInDataType()) || //
                ((animasDataType.getCode() == 38) && (startingRecord == 0) && (recordCount == 0)))
        {
            this.animasDevicePacket.historyRecordCount = 0;
            this.animasDevicePacket.findHistoryRecordCount = true;
        }

        this.addCommandToSend(AnimasDeviceCommand.Instruction, true);
    }


    public void waitForDownloadedQuantity(int qty) throws PlugInBaseException
    {

        long readTimeout = System.currentTimeMillis() + ENTRY_READ_TIMEOUT;
        int lastQuantity = 0;

        int timeoutCount = 0;
        this.animasDevicePacket.allDataReceived = false;

        while (this.animasDevicePacket.downloadedQuantity < qty)
        {
            if (this.isDataAvailable())
            {
                this.readDataFromDevice();

                if (this.animasDevicePacket.downloadedQuantity != lastQuantity)
                {
                    readTimeout = System.currentTimeMillis() + ENTRY_READ_TIMEOUT;
                    lastQuantity = this.animasDevicePacket.downloadedQuantity;
                }
            }

            if (System.currentTimeMillis() > readTimeout)
            {
                LOG.debug("Timeout waiting for data [timeoutCount=" + timeoutCount + "]");
                readTimeout = System.currentTimeMillis() + ENTRY_READ_TIMEOUT;
                timeoutCount++;
            }

            AnimasUtils.sleepInMs(100L);

            if (deviceReader.isDownloadCanceled())
            {
                throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);
            }

            if (timeoutCount > ENTRY_READ_MAX_TIMEOUT_COUNT)
            {
                throw new PlugInBaseException(PlugInExceptionType.DeviceUnreachableRetry);
            }
        }

        doPostProcessing();

    }


    protected void doPostProcessing()
    {
        if (animasDevicePacket.dataTypeObject.hasPostProcessing())
        {
            getData().postProcessReceivedData(animasDevicePacket);
        }
    }


    private void reconnectToDevice()
    {

        try
        {
            this.disconnectDevice();
        }
        catch (Exception ex)
        {
            LOG.debug("Reconnect mode: Disconnect device failed. Ex.:" + ex);
        }

        try
        {
            this.animasDevicePacket.lastDownloadRunQuantity = this.animasDevicePacket.downloadedQuantity;

            LOG.debug("Last downloaded quantity: " + this.animasDevicePacket.lastDownloadRunQuantity);

            if (this.noReconnectMode)
                return;

            this.openConnection(true);

        }
        catch (PlugInBaseException ex)
        {
            LOG.debug("Reconnect mode: Opening connection failed. Ex.:" + ex);
        }
    }


    public void processMessageFromDevice() throws PlugInBaseException
    {
        AnimasDevicePacket adp = this.animasDevicePacket;

        boolean rrProcessed = false;

        if (!foundSerial)
        {
            if ((adp.dataReceivedLength >= 12) && //
                    adp.isReceivedDataBitSetTo(0, 254) && adp.isReceivedDataBitSetTo(1, 191))
            {
                // debugCommunication("PM-2");
                this.dataConverter.decodeDownloaderSerialNumber(adp.getReplyPacket());
                debugCommunication("ProcessMessage::FoundSerial");
                foundSerial = true;
                postprocessDataReceived();
                return;
            }
        }

        if (isPacketFromDevice())
        {

            if (this.connectionState && //
                    (((adp.getReceivedDataBit(1) & 0x1) == 0) || //
                            ((adp.getReceivedDataBit(1) & 0xF) == 1)))
            {
                // debugCommunication("PM-1");

                int madr = AnimasUtils.getUnsignedShort(adp.getReceivedDataBit(1));

                this.nr = ((((madr & 0xE) >> 1) + 1) & 0x7);

                // debugCommunication("PM::NR: " + nr);

                if (!this.animasDevicePacket.isReceivedDataBitSetTo(2, 62))
                {
                    this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, true);
                    debugCommunication("ProcessMessage::RREQ");
                    rrProcessed = true;
                }
            }

            if ((!this.connectionState) && (adp.dataReceivedLength == 10) && (adp.isReceivedDataBitSetTo(1, 115)))
            {
                // debugCommunication("PM-4");

                if (adp.isReceivedDataBitSetTo(6, 1) && adp.isReceivedDataBitSetTo(7, 0)
                        && adp.isReceivedDataBitSetTo(8, 0) && adp.isReceivedDataBitSetTo(9, 0))
                {
                    // debugCommunication("PM-4.1");
                    this.connectionState = true;
                    this.ns = 0;
                    this.nr = 0;
                    debugCommunication("ProcessMessage::ConnectionState true and NS and NR Reset");
                }
                this.xmitRights = true;
            }

            if (this.connectionState)
            {
                if (((adp.getReceivedDataBit(1) & 0xF) == 1) || ((adp.getReceivedDataBit(1) & 0x1) == 0))
                {

                    // ResponseReceivedRequired
                    int madr = AnimasUtils.getUnsignedShort(adp.getReceivedDataBit(1));

                    this.nr = ((((madr & 0xE) >> 1) + 1) & 0x7);

                    if (!this.animasDevicePacket.isReceivedDataBitSetTo(2, 62))
                    {
                        this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, true); // was
                                                                                                   // false
                        // debugCommunication("ProcessMessage::RREQ");
                        rrProcessed = true;
                    }

                    // AcknowledgeNotRequired
                    if (((adp.getReceivedDataBit(1) & 0xE0) >> 5) == ns)
                    {
                        // debugCommunication("PM-5.1");

                        if (this.acknowledgeRequired != 0)
                        {
                            // debugCommunication("ProcessMessage::AcknowledgeNotRequired");
                            this.acknowledgeRequired = 0;
                        }
                    }
                    this.xmitRights = true;
                }

                if ((adp.getReceivedDataBit(1) & 0x1) == 0)
                {
                    // debugCommunication("PM-7");
                    this.xmitRights = true;

                    if (adp.isReceivedDataBitSetTo(2, 69))
                    {
                        // debugCommunication("ProcessMessage::Error");
                        String dwlError = "E" + adp.getDataReceivedAsString(4);
                        throw new PlugInBaseException(PlugInExceptionType.CommunicationErrorWithCode,
                                new Object[] { dwlError });
                    }
                    else if (adp.isReceivedDataBitSetTo(2, 68))
                    {
                        // debugCommunication("ProcessMessage::Data");

                        int checkIfData = adp.getDataCheck();

                        boolean isRetryRun = adp.lastDownloadRunQuantity > adp.downloadedQuantity;

                        if (checkIfData > 0)
                        {
                            if (!isRetryRun)
                            {
                                this.dataConverter.addRawDataToProcess(this.animasDevicePacket.getReplyPacket());
                                this.deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);
                            }
                        }

                        this.animasDevicePacket.downloadedQuantity += 1;

                        if (DEBUG)
                        {
                            LOG.debug("DataType: " + adp.dataTypeObject.name() + ", quantity=" + adp.downloadedQuantity
                                    + ", dataPresent=" + (checkIfData > 0));
                        }

                        if (!rrProcessed)
                        {
                            this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, false);
                        }

                    }
                    else
                    {
                        // debugCommunication("ProcessMessage::ResponseReceivedRequired");
                        this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, false);
                    }

                }
            }

            // 2 178 68 73 29 0 150 24 23 33 240 60 0 0 240 60 0 0 2 0 103 85
            //
            //
            // 3 = Status (68 data, 69 Err)
            //
            // 5 = Counter
            // 6 = Counter + 256
            // 7 => Data

            postprocessDataReceived();
        }

    }


    private void postprocessDataReceived() throws PlugInBaseException
    {
        if (xmitRights)
        {
            this.processAllOutboundData();
        }
    }


    private void debugCommunication(String msg)
    {
        if (debugCommunication)
            LOG.debug(msg);
    }


    private void receivedMessageDebugNew()
    {
        boolean dbg;

        // if ((this.animasDevicePacket.dataTypeObject != null) &&
        // (this.animasDevicePacket.dataTypeObject.getCode() > 41))
        // {
        // dbg = true;
        // }
        // else
        {
            dbg = debugCommunication;
        }

        AnimasUtils.debugHexData(dbg, this.animasDevicePacket.dataReceived, this.animasDevicePacket.dataReceivedLength,
            LOG);
    }


    private boolean isPacketFromDevice()
    {
        return (this.animasDevicePacket.isReceivedDataBitSetTo(0, this.baseData.pumpConnectorInfo.connectionAddress));
    }


    public void addCommandToSend(AnimasDeviceCommand cmd, boolean processAtOnce) throws PlugInBaseException
    {
        if (!this.commandToSend.containsKey(cmd))
        {
            this.commandToSend.put(cmd, null);
            if (processAtOnce)
            {
                this.processOutboundData();
            }
        }
    }


    public void removeCommandsToSend(AnimasDeviceCommand... commands)
    {
        for (AnimasDeviceCommand cmd : commands)
        {
            if (this.commandToSend.containsKey(cmd))
            {
                this.commandToSend.remove(cmd);
            }
        }
    }


    private boolean hasCommandToSend(AnimasDeviceCommand cmd)
    {
        return this.commandToSend.containsKey(cmd);
    }


    public void readDataFromDevice() throws PlugInBaseException
    {
        byte[] tempData = null;

        if (isDataAvailable())
        {
            tempData = readDataFromDeviceInternal();
        }

        if ((tempData != null) && (tempData.length > 0))
        {
            for (byte value : tempData)
            {

                short bt = AnimasUtils.getUnsignedShort(value);

                switch (bt)
                {
                    case START_MESSAGE_DEVICE:
                        this.animasDevicePacket.clearDataReceived();
                        break;

                    case END_MESSAGE_DEVICE:
                        {
                            this.animasDevicePacket.dataReceivedLength = (this.animasDevicePacket.dataReceived.size()
                                    - 2);

                            // AnimasUtils.debugHexData(debugCommunication,
                            // this.animasDevicePacket.dataReceived,
                            // this.animasDevicePacket.dataReceivedLength + 2,
                            // "Proc msg [%s]", LOG);

                            this.processMessageFromDevice();
                        }
                        break;

                    case CTL_MESSAGE_DEVICE:
                        this.controlMode = true;
                        break;

                    default:
                        if (this.controlMode)
                        {
                            this.controlMode = false;
                            this.animasDevicePacket.addDataReceivedBit((short) (bt ^ 0x20));
                        }
                        else
                        {
                            this.animasDevicePacket.addDataReceivedBit((short) (bt & 0xFF));
                        }

                }
            }
        }
    }


    @Override
    public AnimasImplementationType getImplementationType()
    {
        return AnimasImplementationType.AnimasImplementationV2;
    }

}
