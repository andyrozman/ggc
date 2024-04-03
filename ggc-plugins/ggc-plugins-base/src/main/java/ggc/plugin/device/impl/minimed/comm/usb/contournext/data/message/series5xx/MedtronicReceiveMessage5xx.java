package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.MedtronicMessage;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class MedtronicReceiveMessage5xx extends ContourNextLinkMessage
{

    static int ENVELOPE_SIZE = 33;
    //static int ENCRYPTED_ENVELOPE_SIZE = 3;
    //static int CRC_SIZE = 2;

    byte[] responsePayload;




    public MedtronicReceiveMessage5xx(byte[] payload)
    {
        super(payload);
    }


    public static MedtronicReceiveMessage5xx fromBytes(byte[] bytes) throws PlugInBaseException
    {
        MedtronicReceiveMessage5xx message = new MedtronicReceiveMessage5xx(bytes);


        // Validate checksum
        byte messageChecksum = message.mPayload.get(32);
        byte calculatedChecksum = (byte) (MessageUtils.oneByteSum(message.mPayload.array()) - messageChecksum);

        if (messageChecksum != calculatedChecksum)
        {
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck,
                    new Object[] { (int) calculatedChecksum, (int) messageChecksum });
        }

        // payload is just all content after envelope

        int size = bytes.length-ENVELOPE_SIZE;

        System.out.println("Size: " + size);


        ByteBuffer payloadBuffer = ByteBuffer.allocate(size);
        payloadBuffer.order(ByteOrder.LITTLE_ENDIAN);

        payloadBuffer.put(message.mPayload.array(), 33, size);

        message.responsePayload = payloadBuffer.array();

        return message;
    }


    public byte[] getResponsePayload()
    {
        return this.responsePayload;
    }


    /**
     * MedtronicReceiveMessage5xx:
     * +------------------+-----------------+-----------------+---------------------------------+-------------------+--------------------------------+
     * | LE short unknown | LE long pumpMAC | LE long linkMAC | byte[3] responseSequenceNumber? | byte Payload size | byte[] Encrypted Payload bytes |
     * +------------------+-----------------+-----------------+---------------------------------+-------------------+--------------------------------+
     * <p/>
     * MedtronicReceiveMessage (decrypted payload):
     * +----------------------------+-----------------------------+----------------------+--------------------+
     * | byte receiveSequenceNumber | BE short receiveMessageType | byte[] Payload bytes | BE short CCITT CRC |
     * +----------------------------+-----------------------------+----------------------+--------------------+
     */
    public static ContourNextLinkMessage fromBytes(MedtronicCnlSession pumpSession, byte[] bytes)
            throws PlugInBaseException
    {

//        51 01 33 31 36 35 35 31 00 00 00 00 00 00 00 00 00 00 12 21 05 00 00 00 00 00 00 00 40 00 00 00 1C 00 02 01 01 00 64 00 50 01 00 00 00 00 00 00 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00




        ContourNextLinkMessage message = MedtronicMessage.fromBytes(bytes);

//        // TODO - turn this into a factory
//        ContourNextLinkMessage message = MedtronicMessage.fromBytes(bytes);
//
//        // TODO - Validate the message, inner CCITT, serial numbers, etc
//
//        // If there's not 57 bytes, then we got back a bad message. Not sure how
//        // to process these yet.
//        // Also, READ_INFO and LINK_KEY are not encrypted
//        if (bytes.length >= 57 && (bytes[18] != CommandType.READ_INFO.getRequestCommandType())
//                && (bytes[18] != CommandType.REQUEST_LINK_KEY.getRequestCommandType()))
//        {
//            // Replace the encrypted bytes by their decrypted equivalent (same
//            // block size)
//            byte encryptedPayloadSize = bytes[56];
//
//            if (encryptedPayloadSize == 0)
//            {
//                throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryptionDesc,
//                        new Object[] { "encryptedPayloadSize == 0" });
//            }
//
//            ByteBuffer encryptedPayload = ByteBuffer.allocate(encryptedPayloadSize);
//            encryptedPayload.put(bytes, 57, encryptedPayloadSize);
//            byte[] decryptedPayload = decrypt(pumpSession.getKey(), pumpSession.getIV(), encryptedPayload.array());
//
//            if (decryptedPayload == null)
//            {
//                throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryptionDesc,
//                        new Object[] { "decryptedPayload == null" });
//            }
//
//            // Now that we have the decrypted payload, rewind the mPayload, and
//            // overwrite the bytes
//            // TODO - because this messes up the existing CCITT, do we want to
//            // have a separate buffer for the decrypted payload?
//            // Should be fine provided we check the CCITT first...
//
//            message.getPayload().position(57);
//            message.getPayload().put(decryptedPayload);
//
//            message.decryptedPayload = decryptedPayload;
//
//            // if (BuildConfig.DEBUG)
//            // {
//            // String outputString =
//            // HexDump.dumpHexString(message.getPayload().array());
//            // LOG.debug("DECRYPTED: " + outputString);
//            // }
//
//        }

        return message;

    }
}
