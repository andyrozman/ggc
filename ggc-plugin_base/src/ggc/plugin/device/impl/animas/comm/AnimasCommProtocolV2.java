package ggc.plugin.device.impl.animas.comm;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.handler.AnimasDataConverter;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceCommand;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

import java.util.HashMap;
import java.util.List;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.LogEntryType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

//  - retry
public class AnimasCommProtocolV2 extends AnimasCommProtocolAbstract
{
    public static final Log LOG = LogFactory.getLog(AnimasCommProtocolV2.class);

    // command
    private AnimasDevicePacket animasDevicePacket;
    public HashMap<AnimasDeviceCommand, Object> commandToSend = new HashMap<AnimasDeviceCommand, Object>();

    // retry
    // private boolean allDataReceived = false;
    private boolean retryData;
    private boolean reconnectToDevice;
    private boolean pumpConnected; //


    public boolean controlMode;
    public int nr;
    public int ns;
    public int acknowledgeRequired;
    public boolean xmitRights;
    public boolean connectionState;
    public boolean disconnectSignalSent;
    protected AnimasDataConverter dataConverter;


    public AnimasCommProtocolV2(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
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



    protected void sendRequestAndWait(AnimasDataType dataType, int startRecord, int numberOfRecords, int commandWaitTime)
            throws PlugInBaseException
    {
        if  ((dataType.getBaseTransferType()!= AnimasTransferType.All) && (!this.baseData.isCommandAllowedForDeviceType(dataType)))
        {
            return;
        }

        retryData = false;
        reconnectToDevice = false;
        int retryCount = 0;
        String retryString = "";

        do
        {
            if (retryCount > 0)
            {
                retryString = " [retry " + retryCount + "]";
            }

            String logMsg;

            if (startRecord == 0)
            {
                logMsg = "Downloading - " + dataType.getDebugDescription() + retryString;
                LOG.debug(logMsg);
                outputWriter.writeLog(LogEntryType.INFO, logMsg);
            }
            else
            {
                logMsg ="Downloading - " + dataType.getDebugDescription() + " #" + startRecord + retryString;
                LOG.debug(logMsg);
                outputWriter.writeLog(LogEntryType.INFO, logMsg);
            }

            if (reconnectToDevice)
            {
                LOG.error("Reconnect to device, not implemented yet.");
            }

            sendRequestToPump(dataType, startRecord, numberOfRecords);
            waitForDownloadedQuantity(Math.min(commandWaitTime, numberOfRecords));

            AnimasUtils.sleepInMs(10L);

            retryCount++;

        } while (retryData);
    }


    protected void sendRequestAndWait(AnimasDataType dataType, int startRecord, int numberOfRecords, int commandWaitTime,
            int systemWaitTimeBefore) throws PlugInBaseException
    {
        AnimasUtils.sleepInMs(systemWaitTimeBefore);
        sendRequestAndWait(dataType, startRecord, numberOfRecords, commandWaitTime);
    }


    public void closeConnection() throws PlugInBaseException
    {
        if (!this.pumpConnected)
        {
            return;
        }

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
        this.pumpConnected = false;
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
        if (debugCommunication)
            LOG.debug("sendForcedDisconnectSignal");

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
        LOG.debug("sendSOSignal");

        this.removeCommandsToSend(AnimasDeviceCommand.SO, //
                                  AnimasDeviceCommand.ResponseReceivedRequired);
        this.xmitRights = false;

        char data[] = { '\377', '\003', 'S', '0' };

        sendMessageToDevice(data);
    }


    public void openConnection() throws PlugInBaseException
    {
        boolean maxRetriesReached = false;
        int retries = 0;
        int retriesComm = 0;

        while (!maxRetriesReached)
        {
            resetConnectionState();
            AnimasUtils.sleepInMs(10L);

            if (initSerialDevice())
            {
                resetConnectionState();
                this.xmitRights = true;

                AnimasUtils.sleepInMs(1000L);

                sendForcedDisconnectSignal();
                this.pumpConnected = false;
                int intAttempt = 1;

                while ((!this.pumpConnected) && (intAttempt < 10)) // 5
                {
                    LOG.debug("Downloader connected, Trying to find pump [retry=" + retriesComm + "]");
                    sendHandshakeSignal();
                    AnimasUtils.sleepInMs(300L);
                    this.pumpConnected = this.baseData.isDownloaderSerialNumberSet();
                    intAttempt++;
                    retriesComm++;
                    if (deviceReader.isDownloadCanceled())
                    {
                        throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);
                    }
                }

                if (this.pumpConnected)
                {
                    break;
                }

                if (retriesComm > 40)
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

                if (retries > 120)
                {
                    maxRetriesReached = true;
                }
            } // if - init

        } // while

        if (!this.pumpConnected)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
        }

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

        if ((animasDataType.isDateInDataType()) || //
                ((animasDataType.getCode() == 38) && (startingRecord == 0) && (recordCount == 0)))
        {
            this.animasDevicePacket.historyRecordCount = 0;
            this.animasDevicePacket.findHistoryRecordCount = true;
        }

        this.addCommandToSend(AnimasDeviceCommand.Instruction, true);
    }


    private int ENTRY_READ_TIMEOUT = 2000;
    private int ENTRY_READ_MAX_TIMEOUT_COUNT = 5;


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
//                if (timeoutCount > 10)
//                {
//                    break;
//                }
            }

            AnimasUtils.sleepInMs(100L);



            if (timeoutCount > ENTRY_READ_MAX_TIMEOUT_COUNT)
            {
                LOG.warn("Communication with device was stopped by device (device not " +
                        "reachable anymore). Exiting.");
//                LOG.debug("Timeout elpased. Trying to reconnect device and redownload data: "
//                        + this.animasDevicePacket.dataTypeObject.name());
//
//                reconnectToDevice = true;
//
//                // FIXME
//                LOG.error("Reconnect to device NOT IMPLEMENTED !!!!");
//
//                if (!this.animasDevicePacket.allDataReceived)
//                {
//                    retryData = true;
//                    //throw new Ani
//                }
                throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByDevice);
            }
        }

//        Data is written directly - not at end
//
//        if (this.animasDevicePacket.allDataReceived)
//        {
//            this.baseData.addAllDataReceived(this.animasDevicePacket.dataTypeObject);
//            this.baseData.processReceivedData(this.animasDevicePacket);
//
//            if (animasDevicePacket.dataTypeObject.hasPostProcessing())
//            {
//                getData().postProcessReceivedData(animasDevicePacket);
//            }
//        }


        // FIXME remove
        // long readTimeout = System.currentTimeMillis() + 7000;
        // int retries = 0;
        // int lastQuantity = 0;
        //
        // while (this.animasDevicePacket.downloadedQuantity < qty)
        // {
        // if (this.isDataAvailable())
        // {
        // this.readDataFromDevice();
        //
        // if (this.animasDevicePacket.downloadedQuantity != lastQuantity)
        // {
        // readTimeout = System.currentTimeMillis() + 7000;
        // lastQuantity = this.animasDevicePacket.downloadedQuantity;
        // }
        // }
        //
        // if (System.currentTimeMillis() > readTimeout)
        // {
        // System.out.println("Timeout");
        // readTimeout = System.currentTimeMillis() + 7000;
        // retries++;
        //
        // if (retries > 5)
        // {
        // throw new
        // AnimasException(AnimasExceptionType.PumpCommunicationTimeout);
        // }
        // }
        //
        // this.sleepInMs(100L);
        // }
    }


    public void processMessageFromDevice() throws PlugInBaseException
    {
        AnimasDevicePacket adp = this.animasDevicePacket;

        //AnimasUtils.debugHexData(debugCommunication, adp.getDataReceivedAsArray(), adp.dataReceivedLength, "%s", LOG);

        if (isConnectedAndPacketIsFromDevice() && //
                (((adp.getReceivedDataBit(1) & 0x1) == 0) || //
                ((adp.getReceivedDataBit(1) & 0xF) == 1)))
        {
            debugCommunication("PM-1");

            int madr = AnimasUtils.getUnsignedShort(adp.getReceivedDataBit(1));

            this.nr = ((((madr & 0xE) >> 1) + 1) & 0x7);

            debugCommunication("PM::NR: " + nr);

            if (!this.animasDevicePacket.isReceivedDataBitSetTo(2, 62))
            {
                this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, false);
                debugCommunication("PM::NR: RREQ");
            }
        }

        receivedMessageDebugNew();

        if ((adp.dataReceivedLength >= 12) && //
                adp.isReceivedDataBitSetTo(0, 254) && adp.isReceivedDataBitSetTo(1, 191))
        {
            debugCommunication("PM-2");
            this.dataConverter.decodeDownloaderSerialNumber(adp);
            debugCommunication("PM::FoundSerial");
        }

        if ((adp.dataReceivedLength == 2) && (adp.isReceivedDataBitSetTo(1, 115)))
        {
            debugCommunication("PM-3");
            this.xmitRights = true;
            debugCommunication("PM::XmitRIghts");
        }

        if ((!this.connectionState) && (adp.dataReceivedLength == 10)
                && (adp.isReceivedDataBitSetTo(0, this.baseData.pumpConnectorInfo.connectionAddress)) && (adp.isReceivedDataBitSetTo(1, 115)))
        {
            debugCommunication("PM-4");

            if (adp.isReceivedDataBitSetTo(6, 1) && adp.isReceivedDataBitSetTo(7, 0) && adp.isReceivedDataBitSetTo(8, 0) && adp.isReceivedDataBitSetTo(9, 0))
            {
                debugCommunication("PM-4.1");
                this.connectionState = true;
                this.ns = 0;
                this.nr = 0;
                debugCommunication("PM::ConnectionState true and NS and NR Reset");
            }
            this.xmitRights = true;
        }

        if ((isConnectedAndPacketIsFromDevice()) && (((adp.getReceivedDataBit(1) & 0xF) == 1) || ((adp.getReceivedDataBit(1) & 0x1) == 0)))
        {
            debugCommunication("PM-5");

            if (((adp.getReceivedDataBit(1) & 0xE0) >> 5) == ns)
            {
                debugCommunication("PM-5.1");

                if (this.acknowledgeRequired != 0)
                {
                    debugCommunication("PM-5.1.1");
                    this.acknowledgeRequired = 0;
                }
            }
            this.xmitRights = true;
        }

        if (adp.isReceivedDataBitSetTo(0, 255) && adp.isReceivedDataBitSetTo(1, 3) && adp.dataReceivedLength == 4 && //
                adp.isReceivedDataBitSetTo(2, 68) && adp.isReceivedDataBitSetTo(3, 76))
        {
            debugCommunication("PM-6");
            // this.downloadReceived = true;
            debugCommunication("PM::Download received.");
        }

        if (isConnectedAndPacketIsFromDevice() && ((adp.getReceivedDataBit(1) & 0x1) == 0))
        {
            debugCommunication("PM-7");
            this.xmitRights = true;

            if (adp.isReceivedDataBitSetTo(2, 69))
            {
                debugCommunication("PM-7.1");
                String dwlError = "E" + adp.getDataReceivedAsString(4);
                throw new PlugInBaseException(PlugInExceptionType.CommunicationErrorWithCode, new Object[] { dwlError });
            }
            else if (adp.isReceivedDataBitSetTo(2, 68))
            {
                debugCommunication("PM-7.2");
                debugCommunication("PM::Processor");
                int checkIfData = adp.getDataCheck();

                boolean processReturnedData = false;

                if (adp.findHistoryRecordCount)
                {
                    this.animasDevicePacket.historyRecordCount = AnimasUtils.createIntValueThroughMoreBits(
                        adp.getReceivedDataBit(4), adp.getReceivedDataBit(5));

                    debugCommunication("PM::NumberHistoryRecords: " + adp.historyRecordCount);
                    adp.findHistoryRecordCount = false;

                    if ((adp.historyRecordCount == 0) && (checkIfData > 0))
                    {
                        processReturnedData = true;
                    }
                }
                else
                {
                    if (checkIfData > 0)
                    {
                        processReturnedData = true;
                    }
                    else
                    {
                        // FIXME remove
                        // this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired,
                        // false);
                        this.animasDevicePacket.allDataReceived = true;
                        // if (this.dldQuantity > this.numHistoryRecords)
                        // {
                        // dataTypeComplete = true;
                        // this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired,
                        // true);
                        // // this.processAllOutboundData();
                        // return;
                        // }
                    }

                }

                if (processReturnedData)
                {
                    this.dataConverter.processReturnedRawData(this.animasDevicePacket);
                }

                this.animasDevicePacket.downloadedQuantity += 1;

                if (DEBUG)
                {
                    LOG.debug("DataType: " + adp.dataTypeObject.name() + ", quantity=" + adp.downloadedQuantity
                            + ", dataPresent=" + (checkIfData > 0));
                }

                this.deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 1);

            }
            else
            {
                debugCommunication("PM-7.3");

                //LOG.debug("PM::RR_Required");
                this.addCommandToSend(AnimasDeviceCommand.ResponseReceivedRequired, false);
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

        if (xmitRights)
        {
            debugCommunication("PM-8");
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

        if ((this.animasDevicePacket.dataTypeObject != null) && (this.animasDevicePacket.dataTypeObject.getCode() > 41))
        {
            dbg = true;
        }
        else
        {
            dbg = debugCommunication;
        }

        AnimasUtils.debugHexData(dbg, this.animasDevicePacket.dataReceived, this.animasDevicePacket.dataReceivedLength,
            LOG);
    }


    private boolean isConnectedAndPacketIsFromDevice()
    {
        return ((this.connectionState) && //
        (this.animasDevicePacket.isReceivedDataBitSetTo(0, this.baseData.pumpConnectorInfo.connectionAddress)));
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


    public void removeCommandsToSend(AnimasDeviceCommand...commands)
    {
        for(AnimasDeviceCommand cmd : commands)
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
        List<Short> tempData = null;
        if (isDataAvailable())
        {
            tempData = readDataFromDeviceInternal();
        }

        if ((tempData != null) && (tempData.size() > 0))
        {
            for (int j = 0; j < tempData.size(); j++)
            {
                short bt = tempData.get(j);

                switch (bt)
                {
                    case START_MESSAGE_DEVICE:
                        this.animasDevicePacket.clearDataReceived();
                        break;

                    case END_MESSAGE_DEVICE:
                        {
                            this.animasDevicePacket.dataReceivedLength = (this.animasDevicePacket.dataReceived.size() - 2);

                            AnimasUtils.debugHexData(debugCommunication, this.animasDevicePacket.dataReceived,
                                this.animasDevicePacket.dataReceivedLength + 2, "Proc msg [%s]", LOG);

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


