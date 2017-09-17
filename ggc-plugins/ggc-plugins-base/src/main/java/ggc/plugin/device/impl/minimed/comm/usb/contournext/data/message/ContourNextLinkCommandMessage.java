package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message;

import ggc.plugin.data.enums.ASCII;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class ContourNextLinkCommandMessage extends ContourNextLinkMessage
{

    public ContourNextLinkCommandMessage(byte command)
    {
        super(new byte[] { command });
    }


    public ContourNextLinkCommandMessage(ASCII asciiCommand)
    {
        super(new byte[] { asciiCommand.getValue() });
    }


    public ContourNextLinkCommandMessage(String command)
    {
        super(command.getBytes());
    }
}
