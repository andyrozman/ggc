package ggc.plugin.device.impl.minimed.comm.usb.contournext.enums;

/**
 * Created by andy on 04.06.17.
 */

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */


public enum CommandAction
{
    NO_TYPE(0x0), //
    CHANNEL_NEGOTIATE(0x03), //
    PUMP_REQUEST(0x05), //
    PUMP_RESPONSE(0x55);

    private byte value;


    CommandAction(int commandAction)
    {
        value = (byte) commandAction;
    }


    public byte getCommandCode()
    {
        return this.value;
    }
}
