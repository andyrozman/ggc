package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
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

public class MedtronicMessage extends ContourNextLinkBinaryMessage
{

    static int ENVELOPE_SIZE = 2;
    static int CRC_SIZE = 2;


    public MedtronicMessage(CommandType commandType, CommandAction commandAction, MedtronicCnlSession pumpSession,
            byte[] payload)
    {
        super(commandType, pumpSession, buildPayload(commandAction, payload));
    }


    /**
     * MedtronicMessage:
     * +---------------+-------------------+----------------------+--------------------+
     * | CommandAction | byte Payload Size | byte[] Payload bytes | LE short CCITT CRC |
     * +---------------+-------------------+----------------------+--------------------+
     */
    protected static byte[] buildPayload(CommandAction commandAction, byte[] payload)
    {
        byte payloadLength = (byte) (payload == null ? 0 : payload.length);

        ByteBuffer payloadBuffer = ByteBuffer.allocate(ENVELOPE_SIZE + payloadLength + CRC_SIZE);
        payloadBuffer.order(ByteOrder.LITTLE_ENDIAN);

        payloadBuffer.put(commandAction.getCommandCode());
        payloadBuffer.put((byte) (ENVELOPE_SIZE + payloadLength));
        if (payloadLength != 0)
        {
            payloadBuffer.put(payload != null ? payload : new byte[0]);
        }

        payloadBuffer.putShort(
            (short) MessageUtils.CRC16CCITT(payloadBuffer.array(), 0xffff, 0x1021, ENVELOPE_SIZE + payloadLength));

        return payloadBuffer.array();
    }


    public static ContourNextLinkMessage fromBytes(byte[] bytes) throws PlugInBaseException
    {
        ContourNextLinkMessage message = ContourNextLinkBinaryMessage.fromBytes(bytes);

        // TODO - Validate the CCITT
        return message;
    }


    // TODO - maybe move the SecretKeySpec, IvParameterSpec and Cipher
    // construction into the PumpSession?
    protected static byte[] encrypt(byte[] key, byte[] iv, byte[] clear) throws PlugInBaseException
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        byte[] encrypted = new byte[0];

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            encrypted = cipher.doFinal(clear);
        }
        catch (Exception e)
        {
            throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryption);
        }
        return encrypted;
    }


    protected static byte[] decrypt(byte[] key, byte[] iv, byte[] encrypted) throws PlugInBaseException
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        byte[] decrypted;

        try
        {
            Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            decrypted = cipher.doFinal(encrypted);
        }
        catch (Exception e)
        {
            throw new PlugInBaseException(PlugInExceptionType.FailedEncryptionDecryption);
        }
        return decrypted;
    }
}
