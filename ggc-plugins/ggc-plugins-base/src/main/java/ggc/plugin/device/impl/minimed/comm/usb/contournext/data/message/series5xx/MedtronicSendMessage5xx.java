package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.data.NotImplementedException;
import com.atech.utils.data.BitUtils;

import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.test.SimpleReadContourNext;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

/**
 * Based on 600SeriesAndroidUploader implementation. Message for 5xx devices by Andy
 */

public class MedtronicSendMessage5xx extends ContourNextLinkBinaryMessage5xx
{

    private static final Logger LOG = LoggerFactory.getLogger(MedtronicSendMessage5xx.class);

    static int ENVELOPE_SIZE = 4; // 0xA7 S1 S2 S3 CMD 0x00

    static int CRC_SIZE = 1;


    public MedtronicSendMessage5xx(MinimedCommandType commandType, MedtronicCnlSession pumpSession)
    {
        this(commandType, pumpSession, null);
    }


    public MedtronicSendMessage5xx(MinimedCommandType commandType, MedtronicCnlSession pumpSession, byte[] parameters)
    {
        super(CommandType.SEND_MESSAGE, pumpSession, buildPayload(commandType, pumpSession, parameters));
    }


    /**
     * MedtronicSendMessage5xx (just payload for command): NOT CORRECT
     * +-----------------+------------------------------+--------------+-----+
     * | A7 | 3x byte Pump Address (BCD) | command Code | unknown byte | CRC |
     * +-----------------+------------------------------+--------------+-------------------+--------------------------------+
     * <p/>
     * MedtronicSendMessage (decrypted payload):
     * +-------------------------+----------------------+----------------------+--------------------+
     * | byte sendSequenceNumber | BE short sendMessageType | byte[] Payload bytes | BE short CCITT CRC |
     * +-------------------------+----------------------+----------------------+--------------------+
     */

    protected static byte[] buildPayload(MinimedCommandType commandType, MedtronicCnlSession pumpSession,
            byte[] parameters)
    {

        // A7 31 65 51 C0 00 52

        byte commandLength = (byte) (parameters == null ? 2 : 1 + parameters.length);

        ByteBuffer sendPayloadBuffer = ByteBuffer.allocate(ENVELOPE_SIZE + commandLength + CRC_SIZE);
        sendPayloadBuffer.order(ByteOrder.BIG_ENDIAN);

        int[] serialNumberBCD = MinimedUtil.getSerialNumberBCD();

        sendPayloadBuffer.put((byte) 0xA7);
        sendPayloadBuffer.put((byte) serialNumberBCD[0]);
        sendPayloadBuffer.put((byte) serialNumberBCD[1]);
        sendPayloadBuffer.put((byte) serialNumberBCD[2]);

        if (parameters == null)
        {
            sendPayloadBuffer.put((byte) commandType.getCommandCode());
            sendPayloadBuffer.put((byte) 0x00);
        }
        else
        {
            // payload = medtronicHeader.concat(command,parameter);
            // var padding = _.fill(new Array(22-parameter.length),0);
            // payload = payload.concat(padding);
            throw new NotImplementedException(MedtronicSendMessage5xx.class, //
                    "MedtronicSendMessage5xx", //
                    "Command with parameters not implemented yet.");
        }

        byte[] payload = sendPayloadBuffer.array();

        LOG.info(MinimedUtil.getBitUtils().getByteArrayHex(payload));

        int crc = MinimedUtil.getBitUtils().computeCRC8WithPolynomial(payload, 0, payload.length - 1);

        LOG.info("crc: " + crc);

        sendPayloadBuffer.put((byte) crc);

        return sendPayloadBuffer.array();
    }

    // var medtronicHeader = [0xA7,parseInt(serial.substring(0,2),16),
    // parseInt(serial.substring(2,4),16),
    // parseInt(serial.substring(4,6),16)];
    //
    // // first construct payload before we can determine packet length
    // var payload = [];
    // if(command != null) {
    //
    // if(parameter != null) {
    // payload = medtronicHeader.concat(command,parameter);
    // var padding = _.fill(new Array(22-parameter.length),0);
    // payload = payload.concat(padding);
    // } else {
    // payload = medtronicHeader.concat(command,0x00);
    // var payloadChecksum = crcCalculator.crc8_checksum(payload);
    // payload = payload.concat(payloadChecksum);
    // }
    // }


    public static void main(String[] args)
    {
        SimpleReadContourNext.createConnectionDto();

        BitUtils bitUtils = new BitUtils();

        MedtronicCnlSession session = SimpleReadContourNext.createSession();

        MedtronicSendMessage5xx msg = new MedtronicSendMessage5xx(MinimedCommandType.PumpModel, session, null);

        System.out.println(bitUtils.getDebugByteArrayHex(msg.encode()));

    }
}
