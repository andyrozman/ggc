package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message;

import java.nio.ByteBuffer;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class ContourNextLinkMessage
{

    public ByteBuffer mPayload;
    public byte[] decryptedPayload;


    public ContourNextLinkMessage(byte[] bytes)
    {
        if (bytes != null)
        {
            this.mPayload = ByteBuffer.allocate(bytes.length);
            this.mPayload.put(bytes);
        }
    }


    public byte[] encode()
    {
        return mPayload.array();
    }


    public ByteBuffer getPayload()
    {
        return mPayload;
    }


    // FIXME - get rid of this - make a Builder instead
    protected void setPayload(byte[] payload)
    {
        mPayload = ByteBuffer.allocate(payload.length);
        mPayload.put(payload);
    }
}
