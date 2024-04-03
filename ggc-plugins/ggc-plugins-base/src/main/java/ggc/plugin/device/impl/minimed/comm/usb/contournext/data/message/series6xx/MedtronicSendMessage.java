package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandAction;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class MedtronicSendMessage extends MedtronicMessage
{

    private static final Logger LOG = LoggerFactory.getLogger(MedtronicSendMessage.class);

    static int ENVELOPE_SIZE = 11;
    static int ENCRYPTED_ENVELOPE_SIZE = 3;
    static int CRC_SIZE = 2;


    public MedtronicSendMessage(SendMessageType sendMessageType, MedtronicCnlSession pumpSession, byte[] payload)
            throws PlugInBaseException
    {
        super(CommandType.SEND_MESSAGE, CommandAction.PUMP_REQUEST, pumpSession,
                buildPayload(sendMessageType, pumpSession, payload));
    }


    public MedtronicSendMessage(SendMessageType sendMessageType, MedtronicCnlSession pumpSession)
            throws PlugInBaseException
    {
        this(sendMessageType, pumpSession, null);
    }


    /**
     * MedtronicSendMessage:
     * +-----------------+------------------------------+--------------+-------------------+--------------------------------+
     * | LE long pumpMAC | byte medtronicSequenceNumber | byte unknown | byte Payload size | byte[] Encrypted Payload bytes |
     * +-----------------+------------------------------+--------------+-------------------+--------------------------------+
     * <p/>
     * MedtronicSendMessage (decrypted payload):
     * +-------------------------+----------------------+----------------------+--------------------+
     * | byte sendSequenceNumber | BE short sendMessageType | byte[] Payload bytes | BE short CCITT CRC |
     * +-------------------------+----------------------+----------------------+--------------------+
     */
    protected static byte[] buildPayload(SendMessageType sendMessageType, MedtronicCnlSession pumpSession,
            byte[] payload) throws PlugInBaseException
    {
        byte payloadLength = (byte) (payload == null ? 0 : payload.length);

        ByteBuffer sendPayloadBuffer = ByteBuffer.allocate(ENCRYPTED_ENVELOPE_SIZE + payloadLength + CRC_SIZE);
        sendPayloadBuffer.order(ByteOrder.BIG_ENDIAN); // I know, this is the
                                                       // default - just being
                                                       // explicit

        sendPayloadBuffer.put(sendSequenceNumber(sendMessageType));
        sendPayloadBuffer.putShort(sendMessageType.getRequestCode());
        if (payloadLength != 0)
        {
            sendPayloadBuffer.put(payload);
        }

        sendPayloadBuffer.putShort((short) MessageUtils.CRC16CCITT(sendPayloadBuffer.array(), 0xffff, 0x1021,
            ENCRYPTED_ENVELOPE_SIZE + payloadLength));

        ByteBuffer payloadBuffer = ByteBuffer.allocate(ENVELOPE_SIZE + sendPayloadBuffer.capacity());
        payloadBuffer.order(ByteOrder.LITTLE_ENDIAN);

        payloadBuffer.putLong(pumpSession.getPumpMAC());
        payloadBuffer.put((byte) pumpSession.getMedtronicSequenceNumber());
        payloadBuffer.put((byte) 0x10);
        payloadBuffer.put((byte) sendPayloadBuffer.capacity());

        LOG.debug("Pump Session: " + pumpSession.getMinimedCommunicationInterfaceType());

        if (pumpSession.getMinimedCommunicationInterfaceType() == MinimedCommInterfaceType.ContourNextLink24)
            payloadBuffer.put(encrypt(pumpSession.getKey(), pumpSession.getIV(), sendPayloadBuffer.array()));
        else
            payloadBuffer.put(sendPayloadBuffer.array());

        return payloadBuffer.array();
    }


    // FIXME Andy probably need to extend this, perhaps move it to enum?
    protected static byte sendSequenceNumber(SendMessageType sendMessageType)
    {
        switch (sendMessageType)
        {
            case BEGIN_EHSM_SESSION:
                return (byte) 0x80;
            case TIME:
                return (byte) 0x02;
            case READ_PUMP_STATUS:
                return (byte) 0x03;
            default:
                return 0x00;
        }
    }
}
