package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;


/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class ContourNextLinkBinaryMessage extends ContourNextLinkMessage
{

    private static final Logger LOG = LoggerFactory.getLogger(ContourNextLinkBinaryMessage.class);
    static int ENVELOPE_SIZE = 33;


    public ContourNextLinkBinaryMessage(CommandType commandType, MedtronicCnlSession pumpSession)
    {
        this(commandType, pumpSession, null);
    }


    public ContourNextLinkBinaryMessage(CommandType commandType, MedtronicCnlSession pumpSession, byte[] payload)
    {
        super(buildPayload(commandType, pumpSession, payload));
    }


    protected static byte[] buildPayload(CommandType commandType, MedtronicCnlSession pumpSession, byte[] payload)
    {
         int payloadLength = payload == null ? 0 : payload.length;

         ByteBuffer payloadBuffer = ByteBuffer.allocate(ENVELOPE_SIZE +
         payloadLength);
         payloadBuffer.order(ByteOrder.LITTLE_ENDIAN);

         payloadBuffer.put((byte) 0x51);
         payloadBuffer.put((byte) 0x3);
         payloadBuffer.put("000000".getBytes()); // Text of PumpInfo serial, but 000000 for 640g
         byte[] unknownBytes = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
         payloadBuffer.put(unknownBytes);
         payloadBuffer.put(commandType.getRequestCommandType());
         payloadBuffer.putInt(pumpSession.getBayerSequenceNumber());
         byte[] unknownBytes2 = { 0, 0, 0, 0, 0 };
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
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck, new Object[] {(int) calculatedChecksum, (int) messageChecksum});
        }

        return message;
    }
}
