package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandAction;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class MedtronicReceiveMessage extends MedtronicMessage
{

    static int ENVELOPE_SIZE = 22;
    static int ENCRYPTED_ENVELOPE_SIZE = 3;
    static int CRC_SIZE = 2;


    protected MedtronicReceiveMessage(CommandType commandType, CommandAction commandAction,
            MedtronicCnlSession pumpSession, byte[] payload)
    {
        super(commandType, commandAction, pumpSession, payload);
    }

    public enum ReceiveMessageType
    {
        NO_TYPE(0x0), TIME_RESPONSE(0x407);

        private short value;


        ReceiveMessageType(int messageType)
        {
            value = (short) messageType;
        }
    }


    /**
     * MedtronicReceiveMessage:
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
        // TODO - turn this into a factory
        ContourNextLinkMessage message = MedtronicMessage.fromBytes(bytes);

        // TODO - Validate the message, inner CCITT, serial numbers, etc

        // If there's not 57 bytes, then we got back a bad message. Not sure how
        // to process these yet.
        // Also, READ_INFO and LINK_KEY are not encrypted
        if (bytes.length >= 57 && (bytes[18] != CommandType.READ_INFO.getRequestCommandType())
                && (bytes[18] != CommandType.REQUEST_LINK_KEY.getRequestCommandType()))
        {
            // Replace the encrypted bytes by their decrypted equivalent (same
            // block size)
            byte encryptedPayloadSize = bytes[56];

            if (encryptedPayloadSize == 0)
            {
                throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryptionDesc,
                        new Object[] { "encryptedPayloadSize == 0" });
            }

            ByteBuffer encryptedPayload = ByteBuffer.allocate(encryptedPayloadSize);
            encryptedPayload.put(bytes, 57, encryptedPayloadSize);
            byte[] decryptedPayload = decrypt(pumpSession.getKey(), pumpSession.getIV(), encryptedPayload.array());

            if (decryptedPayload == null)
            {
                throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryptionDesc,
                        new Object[] { "decryptedPayload == null" });
            }

            // Now that we have the decrypted payload, rewind the mPayload, and
            // overwrite the bytes
            // TODO - because this messes up the existing CCITT, do we want to
            // have a separate buffer for the decrypted payload?
            // Should be fine provided we check the CCITT first...

            message.getPayload().position(57);
            message.getPayload().put(decryptedPayload);

            message.decryptedPayload = decryptedPayload;

            // if (BuildConfig.DEBUG)
            // {
            // String outputString =
            // HexDump.dumpHexString(message.getPayload().array());
            // LOG.debug("DECRYPTED: " + outputString);
            // }

        }

        return message;

    }
}
