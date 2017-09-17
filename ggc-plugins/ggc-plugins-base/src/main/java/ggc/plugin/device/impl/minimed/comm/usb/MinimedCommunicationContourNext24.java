package ggc.plugin.device.impl.minimed.comm.usb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.ASCII;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkCommandMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.ChannelNegotiateRequestMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.ContourNextLink24ResponseConverter;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.ContourNextLinkBinaryMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.MedtronicSendMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.ResponseExpected;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.tmp.PumpStatusEvent;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverter;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Created by andy on 13.06.17.
 */

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class MinimedCommunicationContourNext24 extends MinimedCommunicationContourNextAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCommunicationContourNext24.class);

    private static final byte[] RADIO_CHANNELS = { 0x14, 0x11, 0x0e, 0x17, 0x1a };

    ContourNextLink24ResponseConverter responseConverter;
    MinimedDataConverter dataConverter;


    /**
     * Constructor
     *
     * @param dataAccess
     * @param minimedDataHandler data Handler
     */
    public MinimedCommunicationContourNext24(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
            throws PlugInBaseException
    {
        super(dataAccess, minimedDataHandler);

        responseConverter = new ContourNextLink24ResponseConverter(session);
        dataConverter = MinimedUtil.getDataConverter(MinimedDeviceType.Minimed_640G, MinimedTargetType.Pump);
    }


    @Override
    public MinimedCommInterfaceType getInterfaceType()
    {
        return MinimedCommInterfaceType.ContourNextLink24;
    }


    @Override
    protected void executeHistoryCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {

    }


    public int initDevice() throws PlugInBaseException
    {

        try
        {
            enterControlMode();
            enterPassthroughMode();
            openConnection();

            requestDeviceInfo();

            requestReadInfo();
            requestLinkKey();

            long pumpMAC = session.getPumpMAC();
            LOG.info("PumpInfo MAC: " + (pumpMAC & 0xffffff));

            byte radioChannel = negotiateChannel((byte) 0x00); // activePump.getLastRadioChannel());

            if (radioChannel == 0)
            {
                LOG.info("Could not communicate with the 640g. Are you near the pump ? ");
                throw new PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
            }
            else
            {
                // activePump.setLastRadioChannel(radioChannel);

                LOG.debug(String.format("Connected to Contour Next Link on channel %d.", (int) radioChannel));

                beginEHSMSession();
            }
        }
        catch (Exception ex)
        {
            LOG.error("Communication Error on initDevice. Unexpected Message", ex.getMessage(), ex);
        }

        return 0;
    }


    public void requestDeviceInfo() throws PlugInBaseException
    {
        sendMessage(new ContourNextLinkCommandMessage("X"));

        boolean doRetry = false;

        // TODO - parse this into an ASTM record for the device info.
        try
        {
            // The stick will return either the ASTM message, or the ENQ first.
            // The order can change,
            // so we need to handle both cases
            byte[] response1 = readMessage();
            byte[] response2 = readMessage();

            if (response1[0] == ASCII.EOT.getValue())
            {
                // response 1 is the ASTM message
                checkControlMessage(response2, ASCII.ENQ);
                extractStickSerial(new String(response1));
            }
            else
            {
                // response 2 is the ASTM message
                checkControlMessage(response1, ASCII.ENQ);
                extractStickSerial(new String(response2));
            }
        }
        catch (PlugInBaseException e)
        {
            // Terminate comms with the pump, then try again
            sendMessage(new ContourNextLinkCommandMessage(ASCII.EOT.getValue()));
            doRetry = true;
        }
        finally
        {
            if (doRetry)
            {
                requestDeviceInfo();
            }
        }
    }


    public void requestReadInfo() throws PlugInBaseException
    {
        sendCommandToDevice(CommandType.READ_INFO);
    }


    private void extractStickSerial(String astmMessage)
    {
        Pattern pattern = Pattern.compile(".*?\\^(\\d{4}-\\d{7})\\^.*");
        Matcher matcher = pattern.matcher(astmMessage);
        if (matcher.find())
        {
            session.setStickSerial(matcher.group(1));
        }
    }


    private void readSomeData() throws Exception
    {

        PumpStatusEvent pumpRecord = new PumpStatusEvent();
        // realm.createObject(PumpStatusEvent.class);

        String deviceName = String.format("medtronic-640g://%s", session.getStickSerial());
        // activePump.setDeviceName(deviceName);

        // TODO - this should not be necessary. We should reverse
        // lookup the device name from PumpInfo
        pumpRecord.setDeviceName(deviceName);

        long pumpTime = getPumpTime().getTime();
        long pumpOffset = pumpTime - System.currentTimeMillis();
        LOG.debug("Time offset between pump and device: " + pumpOffset + " millis.");

        // TODO - send ACTION to MainActivity to show offset between
        // pump and
        // uploader.pumpRecord.setPumpTimeOffset(pumpOffset);
        pumpRecord.setPumpDate(new Date(pumpTime - pumpOffset));
        // getPumpStatus(pumpRecord, pumpOffset);
        // activePump.getPumpHistory().add(pumpRecord);

        boolean cancelTransaction = true;
        if (pumpRecord.getSgv() != 0)
        {
            // Check that the record doesn't already exist before
            // committing
            // RealmResults<PumpStatusEvent> checkExistingRecords =
            // activePump.getPumpHistory().where()
            // .equalTo("eventDate",
            // pumpRecord.getEventDate()).equalTo("sgv",
            // pumpRecord.getSgv())
            // .findAll();

            // There should be the 1 record we've already added in
            // this transaction.
            // if (checkExistingRecords.size() <= 1) {
            // realm.commitTransaction();
            // cancelTransaction = false;
            // }

            // Tell the Main Activity we have new data
            // sendMessage(Constants.ACTION_REFRESH_DATA);
        }

        // if (cancelTransaction) {
        // realm.cancelTransaction();
        // }

    }


    public int closeDevice() throws PlugInBaseException
    {
        try
        {
            endEHSMSession();

            closeConnection();
            endPassthroughMode();
            endControlMode();
        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    public MinimedCommandReply executeCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {
        return null;
    }


    public void openConnection() throws IOException, TimeoutException, NoSuchAlgorithmException, PlugInBaseException
    {
        LOG.debug("Begin openConnection");
        sendMessage(new ContourNextLinkBinaryMessage(CommandType.OPEN_CONNECTION, session, session.getHMAC()));
        // FIXME - We need to care what the response message is - wrong MAC and
        // all that
        readMessage();
        LOG.debug("Finished openConnection");
    }


    public void beginEHSMSession() throws PlugInBaseException
    {
        sendCommandToDevice(SendMessageType.BEGIN_EHSM_SESSION);
        session.setInEHSMMode(true);
    }


    public void endEHSMSession() throws PlugInBaseException
    {
        sendCommandToDevice(SendMessageType.END_EHSM_SESSION);
        session.setInEHSMMode(false);
    }


    public void requestLinkKey() throws PlugInBaseException
    {
        sendCommandToDevice(CommandType.REQUEST_LINK_KEY);
    }


    public byte negotiateChannel(byte lastRadioChannel) throws PlugInBaseException
    {
        ArrayList<Byte> radioChannels = new ArrayList(Arrays.asList(ArrayUtils.toObject(RADIO_CHANNELS)));

        if (lastRadioChannel != 0x00)
        {
            // If we know the last channel that was used, shuffle the
            // negotiation order
            Byte lastChannel = radioChannels.remove(radioChannels.indexOf(lastRadioChannel));

            if (lastChannel != null)
            {
                radioChannels.add(0, lastChannel);
            }
        }

        LOG.debug("Begin negotiateChannel");
        for (byte channel : radioChannels)
        {
            LOG.debug(String.format("negotiateChannel: trying channel '%d'...", channel));
            session.setRadioChannel(channel);
            sendMessage(new ChannelNegotiateRequestMessage(session));

            // Don't care what the 0x81 response message is at this stage
            LOG.debug("negotiateChannel: Reading 0x81 message");
            readMessage();
            // The 0x80 message
            LOG.debug("negotiateChannel: Reading 0x80 message");
            ContourNextLinkMessage response = ContourNextLinkBinaryMessage.fromBytes(readMessage());
            byte[] responseBytes = response.encode();

            LOG.debug("negotiateChannel: Check response length");
            if (responseBytes.length > 46)
            {
                // Looks promising, let's check the last byte of the payload to
                // make sure
                if (responseBytes[76] == session.getRadioChannel())
                {
                    break;
                }
                else
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceCommandInvalidResponse,
                            new Object[] { "ChannelNegotiateRequestMessage", session.getRadioChannel(),
                                           responseBytes[76] });
                    // throw new IOException(
                    // String.format(Locale.getDefault(), "Expected to get a
                    // message for channel %d. Got %d",
                    // session.getRadioChannel(), responseBytes[76]));
                }
            }
            else
            {
                session.setRadioChannel((byte) 0);
            }
        }

        LOG.debug(String.format("Finished negotiateChannel with channel '%d'", session.getRadioChannel()));
        return session.getRadioChannel();
    }


    private void checkIfInEHSMMode() throws PlugInBaseException
    {
        if (session.isInEHSMMode())
            throw new PlugInBaseException(PlugInExceptionType.DeviceIsInWrongState, new Object[] { "No-EHMS", "EHMS" });
    }


    public Date getPumpTime() throws PlugInBaseException
    {
        checkIfInEHSMMode();

        sendCommandToDevice(SendMessageType.TIME);

        return session.getPumpTime();
    }


    public void getPumpStatus() throws PlugInBaseException
    {
        sendCommandToDevice(SendMessageType.READ_PUMP_STATUS);
    }


    public void sendCommandToDevice(SendMessageType sendMessageType) throws PlugInBaseException
    {
        LOG.debug(sendMessageType.getDescription() + " - START");
        sendMessage(new MedtronicSendMessage(sendMessageType, session, sendMessageType.getCommandPayload()));

        byte[] response1 = readMessage();
        byte[] response2 = null;
        ResponseExpected responseExpected = sendMessageType.getResponseExpected();

        if (responseExpected == ResponseExpected.Two || responseExpected == ResponseExpected.TwoSecondConverted)
        {
            response2 = readMessage();
        }

        if (responseExpected == ResponseExpected.TwoSecondConverted)
        {
            MinimedCommandReply minimedCommandReply = responseConverter.convert(CommandType.SEND_MESSAGE,
                sendMessageType, response2);

            dataConverter.convertData(minimedCommandReply);
        }

        LOG.debug(sendMessageType.getDescription() + " - END");

    }


    public void sendCommandToDevice(CommandType commandType) throws PlugInBaseException
    {
        LOG.debug(commandType.getDescription() + " - START");

        sendMessage(new ContourNextLinkBinaryMessage(commandType, session, null));

        byte[] response1 = readMessage();
        ResponseExpected responseExpected = commandType.getResponseExpected();

        if (responseExpected == ResponseExpected.OneFirstConverted)
        {
            responseConverter.convert(commandType, null, response1);
        }

        LOG.debug(commandType.getDescription() + " - END");
    }

}
