package ggc.plugin.device.impl.minimed.comm.usb.contournext.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;

/**
 * Created by lgoedhart on 26/03/2016.
 */


/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */


public class MedtronicCnlSession
{

    private static final String HMAC_PADDING = "A4BD6CED9A42602564F413123";

    private byte[] HMAC;
    private byte[] key;

    private String stickSerial;

    // GGC for CNL-1
    private String pumpSerial;

    private long linkMAC;
    private long pumpMAC;

    private byte radioChannel;
    private int bayerSequenceNumber = 1;
    private int medtronicSequenceNumber = 1;

    private MinimedCommInterfaceType minimedCommunicationInterfaceType;
    private Date pumpTime;
    private long pumpTimeOffset;
    private boolean inEHSMMode;


    /*
     * public byte[] getHMAC() {
     * return HMAC;
     * }
     */

    public byte[] getHMAC() throws NoSuchAlgorithmException
    {
        String shortSerial = this.stickSerial.replaceAll("\\d+-", "");
        byte[] message = (shortSerial + HMAC_PADDING).getBytes();
        byte[] numArray;

        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(message);

        numArray = instance.digest();
        ArrayUtils.reverse(numArray);

        return numArray;
    }


    public byte[] getKey()
    {
        return key;
    }


    public byte[] getIV()
    {
        byte[] iv = new byte[key.length];
        System.arraycopy(key, 0, iv, 0, key.length);
        iv[0] = radioChannel;
        return iv;
    }


    public long getLinkMAC()
    {
        return linkMAC;
    }


    public void setLinkMAC(long linkMAC)
    {
        this.linkMAC = linkMAC;
    }


    public long getPumpMAC()
    {
        return pumpMAC;
    }


    public void setPumpMAC(long pumpMAC)
    {
        this.pumpMAC = pumpMAC;
    }


    public int getBayerSequenceNumber()
    {
        return bayerSequenceNumber;
    }


    public int getMedtronicSequenceNumber()
    {
        return medtronicSequenceNumber;
    }


    public byte getRadioChannel()
    {
        return radioChannel;
    }


    public void incrBayerSequenceNumber()
    {
        bayerSequenceNumber++;
    }


    public void incrMedtronicSequenceNumber()
    {
        medtronicSequenceNumber++;
    }


    public void setRadioChannel(byte radioChannel)
    {
        this.radioChannel = radioChannel;
    }


    public void setHMAC(byte[] hmac)
    {
        this.HMAC = hmac;
    }


    public void setKey(byte[] key)
    {
        this.key = key;
    }


    public byte[] getPackedLinkKey()
    {
        return this.key;
    }


    public void setPackedLinkKey(byte[] packedLinkKey)
    {
        this.key = packedLinkKey;
    }


    public String getStickSerial()
    {
        return stickSerial;
    }


    public void setStickSerial(String stickSerial)
    {
        this.stickSerial = stickSerial;
    }


    public MinimedCommInterfaceType getMinimedCommunicationInterfaceType()
    {
        return minimedCommunicationInterfaceType;
    }


    public void setMinimedCommunicationInterfaceType(MinimedCommInterfaceType minimedCommInterfaceType)
    {
        this.minimedCommunicationInterfaceType = minimedCommInterfaceType;
    }


    public String getPumpSerial()
    {
        return pumpSerial;
    }


    public void setPumpSerial(String pumpSerial)
    {
        this.pumpSerial = pumpSerial;
    }


    public void setPumpTime(Date pumpTime)
    {
        this.pumpTime = pumpTime;
    }


    public Date getPumpTime()
    {
        return pumpTime;
    }


    public void setPumpTimeOffset(long pumpTimeOffset)
    {
        this.pumpTimeOffset = pumpTimeOffset;
    }


    public long getPumpTimeOffset()
    {
        return pumpTimeOffset;
    }


    public void setInEHSMMode(boolean inEHSMMode)
    {
        this.inEHSMMode = inEHSMMode;
    }


    public boolean isInEHSMMode()
    {
        return this.inEHSMMode;
    }

}
