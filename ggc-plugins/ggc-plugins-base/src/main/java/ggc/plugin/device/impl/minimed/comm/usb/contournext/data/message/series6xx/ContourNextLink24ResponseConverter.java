package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.tmp.PumpStatusEvent;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

/**
 * Created by andy on 01/06/17.
 */
public class ContourNextLink24ResponseConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(ContourNextLink24ResponseConverter.class);

    MedtronicCnlSession pumpSession;
    long pumpTimeOffset = 0L;


    public ContourNextLink24ResponseConverter(MedtronicCnlSession pumpSession)
    {
        // super(dataAccess);
        this.pumpSession = pumpSession;
        MinimedUtil.setMedtronicCnlSession(this.pumpSession);
    }


    public MinimedCommandReply convert(CommandType commandType, SendMessageType sendMessageType, byte[] rawData)
            throws PlugInBaseException
    {
        ContourNextLinkMessage response = fromBytes(commandType, null, rawData);

        switch (commandType)
        {
            case NO_TYPE:
                break;

            case SEND_MESSAGE: // this one is only one that returns correct
                               // data, all other commands are used internally
                return getMinimedCommandReply(sendMessageType, rawData);

            case READ_INFO:
                readInfo(response);
                break;

            case REQUEST_LINK_KEY:
                readRequestLinkKey(response);
                // readLinkKey(response);
                break;

            case SEND_LINK_KEY:
                break;

            case RECEIVE_MESSAGE:
                break;
            // case SEND_MESSAGE_RESPONSE:
            // break;

            case OPEN_CONNECTION:
            case CLOSE_CONNECTION:
            default:
                break;

        }

        return null;
    }


    private MinimedCommandReply getMinimedCommandReply(SendMessageType commandType, byte[] rawData)
            throws PlugInBaseException
    {

        ContourNextLinkMessage message = MedtronicReceiveMessage.fromBytes(pumpSession, rawData);

        MinimedCommandReply reply = new MinimedCommandReply(commandType);

        reply.setRawData(message.encode());
        reply.setDecryptedPayload(message.decryptedPayload);

        return reply;
    }


    private void readRequestLinkKey(ContourNextLinkMessage response) throws PlugInBaseException
    {

        if (response.encode().length < (0x57 - 4))
        {
            // Invalid message. Don't try and parse it
            // TODO - deal with this more elegantly
            LOG.error("Invalid message received for requestLinkKey, Contour Next Link is not paired with pump.");

            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseDescription,
                    new Object[] { "Invalid response for requestLinkKey. Contour Next Link not paired with pump." });
        }

        ByteBuffer infoBuffer = ByteBuffer.allocate(55);
        infoBuffer.order(ByteOrder.BIG_ENDIAN);
        infoBuffer.put(response.encode(), 0x21, 55);

        setPackedLinkKey(infoBuffer.array());

        LOG.info(String.format("requestLinkKey. linkKey = '%s'", pumpSession.getPackedLinkKey()));
    }


    private void setPackedLinkKey(byte[] packedLinkKey)
    {
        byte[] key = new byte[16];

        int pos = pumpSession.getStickSerial().charAt(pumpSession.getStickSerial().length() - 1) & 7;

        for (int i = 0; i < key.length; i++)
        {
            if ((packedLinkKey[pos + 1] & 1) == 1)
            {
                key[i] = (byte) ~packedLinkKey[pos];
            }
            else
            {
                key[i] = packedLinkKey[pos];
            }

            if (((packedLinkKey[pos + 1] >> 1) & 1) == 0)
            {
                pos += 3;
            }
            else
            {
                pos += 2;
            }
        }

        pumpSession.setPackedLinkKey(key);
    }


    private void readLinkKey(ContourNextLinkMessage response)
    {
        ByteBuffer infoBuffer = ByteBuffer.allocate(55);
        infoBuffer.order(ByteOrder.BIG_ENDIAN);
        infoBuffer.put(response.encode(), 0x21, 55);

        byte[] packedLinkKey = infoBuffer.array();

        this.pumpSession.setPackedLinkKey(packedLinkKey);

        LOG.debug(String.format("RequestLinkKey. linkKey = '%s'", packedLinkKey));
    }


    private void readInfo(ContourNextLinkMessage response)
    {
        ByteBuffer infoBuffer = ByteBuffer.allocate(16);
        infoBuffer.order(ByteOrder.BIG_ENDIAN);
        infoBuffer.put(response.encode(), 0x21, 16);
        long linkMAC = infoBuffer.getLong(0);
        long pumpMAC = infoBuffer.getLong(8);

        this.pumpSession.setLinkMAC(linkMAC);
        this.pumpSession.setPumpMAC(pumpMAC);

        LOG.debug(String.format("requestReadInfo [linkMAC = '%d', pumpMAC = '%d]", linkMAC, pumpMAC));
    }


    public ContourNextLinkMessage fromBytes(CommandType commandType, SendMessageType sendMessageType, byte[] bytes)
            throws PlugInBaseException
    {
        // FIXME include sendMessageType

        // TODO return MinimedReply
        // TODO - turn this into a factory
        ContourNextLinkMessage message = null;
        message = MedtronicReceiveMessage.fromBytes(pumpSession, bytes);

        // TODO - Validate the MessageType

        return message;
    }

    // public static ContourNextLinkMessage fromBytes(MedtronicCnlSession
    // pumpSession, byte[] bytes)
    // throws ChecksumException, EncryptionException
    // {
    // // TODO - turn this into a factory
    // ContourNextLinkMessage message =
    // MedtronicReceiveMessage.fromBytes(pumpSession, bytes);
    //
    // // TODO - Validate the MessageType
    //
    // return message;
    // }

    // public void convert(byte[] rawData) throws PlugInBaseException
    // {
    //
    //
    // ContourNextLinkMessage response = fromBytes(rawData);
    //
    //
    //
    //
    //
    // }


    private static PumpStatusEvent.CGM_TREND getTrend(byte messageByte)
    {
        switch (messageByte)
        {
            case (byte) 0x60:
                return PumpStatusEvent.CGM_TREND.FLAT;
            case (byte) 0xc0:
                return PumpStatusEvent.CGM_TREND.DOUBLE_UP;
            case (byte) 0xa0:
                return PumpStatusEvent.CGM_TREND.SINGLE_UP;
            case (byte) 0x80:
                return PumpStatusEvent.CGM_TREND.FOURTY_FIVE_UP;
            case (byte) 0x40:
                return PumpStatusEvent.CGM_TREND.FOURTY_FIVE_DOWN;
            case (byte) 0x20:
                return PumpStatusEvent.CGM_TREND.SINGLE_DOWN;
            case (byte) 0x00:
                return PumpStatusEvent.CGM_TREND.DOUBLE_DOWN;
            default:
                return PumpStatusEvent.CGM_TREND.NOT_COMPUTABLE;
        }
    }


    public void convertData(MinimedCommandReply minimedReply)
    {

    }


    public Object getReplyValue(MinimedCommandReply minimedReply)
    {
        return null;
    }


    public void refreshOutputWriter()
    {

    }
}
