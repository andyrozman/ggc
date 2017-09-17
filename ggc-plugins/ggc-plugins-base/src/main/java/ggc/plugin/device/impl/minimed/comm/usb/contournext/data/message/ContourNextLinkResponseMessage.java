package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message;

import ggc.plugin.data.enums.ASCII;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public abstract class ContourNextLinkResponseMessage extends ContourNextLinkMessage
{

    public ContourNextLinkResponseMessage(byte[] payload) throws PlugInBaseException
    {
        super(payload);
    }


    public void checkControlMessage(ASCII controlCharacter) throws PlugInBaseException
    {
        checkControlMessage(mPayload.array(), controlCharacter);
    }


    public static void checkControlMessage(byte[] msg, ASCII controlCharacter) throws PlugInBaseException
    {
        if (msg.length != 1 || controlCharacter.getValue() != msg[0])
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseCommand,
                    new Object[] { (int) msg[0], (int) controlCharacter.getValue() });
        }
    }
}
