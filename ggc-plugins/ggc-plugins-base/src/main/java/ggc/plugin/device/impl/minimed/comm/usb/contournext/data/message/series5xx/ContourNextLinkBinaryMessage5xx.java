package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

/**
 * Based on 600SeriesAndroidUploader implementation. Message for 5xx devices by Andy
 */

public class ContourNextLinkBinaryMessage5xx extends ContourNextLinkMessage
{

    private static final Logger LOG = LoggerFactory.getLogger(ContourNextLinkBinaryMessage5xx.class);
    static int ENVELOPE_SIZE = 33;


    public ContourNextLinkBinaryMessage5xx(CommandType commandType, MedtronicCnlSession pumpSession, byte[] payload)
    {
        super(buildPayload(commandType, pumpSession, payload));
    }


    protected static byte[] buildPayload(CommandType commandType, MedtronicCnlSession pumpSession, byte[] payload)
    {

        LOG.info("Command Type: " + commandType.name());

        int payloadLength = payload == null ? 0 : payload.length;

        LOG.info("Payload Length: " + payloadLength);

        ByteBuffer payloadBuffer = ByteBuffer.allocate(ENVELOPE_SIZE + payloadLength);
        payloadBuffer.order(ByteOrder.LITTLE_ENDIAN);

        payloadBuffer.put((byte) 0x51);

        payloadBuffer.put((byte) 0x01);
        payloadBuffer.put(pumpSession.getPumpSerial().getBytes());

        byte[] unknownBytes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        payloadBuffer.put(unknownBytes);
        payloadBuffer.put(commandType.getRequestCommandType());

        payloadBuffer.put(commandType.getRetries());
        payloadBuffer.put(commandType.getTimeout());
        byte[] unknownBytes2 = { 0, 0, 0, 0, 0, 0, 0 };
        payloadBuffer.put(unknownBytes2);

        payloadBuffer.putInt(payloadLength);
        payloadBuffer.put((byte) 0); // Placeholder for the CRC

        if (payloadLength != 0)
        {
            payloadBuffer.put(payload);
        }

        // Now that we have the payload, calculate the message CRC
        payloadBuffer.position(32);
        payloadBuffer.put(MessageUtils.oneByteSum(payloadBuffer.array()));

        System.out.println("Packet: " + MinimedUtil.getBitUtils().getDebugArrayHexValue(payloadBuffer.array()));

        return payloadBuffer.array();

    }


    public static ContourNextLinkMessage fromBytes(byte[] bytes) throws PlugInBaseException
    {
        ContourNextLinkMessage message = new ContourNextLinkMessage(bytes);

        // Validate checksum
        byte messageChecksum = message.mPayload.get(32);
        byte calculatedChecksum = (byte) (MessageUtils.oneByteSum(message.mPayload.array()) - messageChecksum);

        if (messageChecksum != calculatedChecksum)
        {
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck,
                    new Object[] { (int) calculatedChecksum, (int) messageChecksum });
        }

        return message;
    }

}
