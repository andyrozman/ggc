package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
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

public class ChannelNegotiateRequestMessage extends MedtronicMessage
{

    public ChannelNegotiateRequestMessage(MedtronicCnlSession pumpSession) throws PlugInBaseException
    {
        super(CommandType.SEND_MESSAGE, CommandAction.CHANNEL_NEGOTIATE, pumpSession, buildPayload(pumpSession));
    }


    protected static byte[] buildPayload(MedtronicCnlSession pumpSession)
    {
        ByteBuffer payload = ByteBuffer.allocate(26);
        payload.order(ByteOrder.LITTLE_ENDIAN);
        // The MedtronicMessage sequence number is always sent as 1 for this
        // message,
        // even though the sequence should keep incrementing as normal
        payload.put((byte) 1);
        payload.put(pumpSession.getRadioChannel());
        byte[] unknownBytes = { 0, 0, 0, 0x07, 0x07, 0, 0, 0x02 };
        payload.put(unknownBytes);
        payload.putLong(pumpSession.getLinkMAC());
        payload.putLong(pumpSession.getPumpMAC());

        return payload.array();
    }
}
