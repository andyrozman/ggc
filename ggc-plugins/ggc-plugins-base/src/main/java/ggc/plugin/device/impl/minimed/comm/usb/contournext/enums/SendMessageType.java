package ggc.plugin.device.impl.minimed.comm.usb.contournext.enums;

import ggc.plugin.device.impl.minimed.data.MinimedCommandTypeInterface;

/**
 * Created by andy on 01/06/17.
 */

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public enum SendMessageType implements MinimedCommandTypeInterface
{
    NO_TYPE(0x00, 0x00), //

    // COMMANDS
    BEGIN_EHSM_SESSION(0x412, null, new byte[] { 0x00 }, "beginEHMSSession", ResponseExpected.One), //
    END_EHSM_SESSION(0x412, null, new byte[] { 0x01 }, "endEHMSSession", ResponseExpected.One), //

    // REQUEST-RESPONSE
    TIME(0x0403, 0x0407, null, "PumpTime", ResponseExpected.TwoSecondConverted), //
    READ_PUMP_STATUS(0x0112, 0x013C, null, "PumpStatus"), //

    // NEW
    READ_BASAL_PATTERN_REQUEST(0x0116, 0x0123), // old was 0x0112, probably
                                                // mistake, RS, RQ difference
                                                // (should be +1)

    READ_BOLUS_WIZARD_CARB_RATIOS(0x012B, 0x012C), //
    READ_BOLUS_WIZARD_SENSITIVITY_FACTORS(0x012E, 0x012F), //
    READ_BOLUS_WIZARD_BG_TARGETS(0x0131, 0x0132), //
    DEVICE_STRING(0x013A, 0x013B), //
    DEVICE_CHARACTERISTICS(0x0200, 0x0201), //
    READ_HISTORY(0x0304, 0x0305), //
    READ_HISTORY_INFO(0x030C, 0x030D), //

    // SPECIAL
    END_HISTORY_TRANSMISSION(0x030A), //
    UNMERGED_HISTORY_RESPONSE(0x030E), //

    // COMMANDS ?
    INITIATE_MULTIPACKET_TRANSFER(0xFF00), //
    MULTIPACKET_SEGMENT_TRANSMISSION(0xFF01), //
    MULTIPACKET_RESEND_PACKETS(0xFF02), //
    ACK_MULTIPACKET_COMMAND(0x00FE), //

    ;

    private String description;
    private short requestCode;
    private short responseCode;
    private byte[] commandPayload;
    private ResponseExpected responseExpected;
    private int recordLength = 0;


    SendMessageType(int requestCode)
    {
        this(requestCode, null, null, "Unknown");
    }


    SendMessageType(int requestCode, byte[] commandPayload)
    {
        this(requestCode, null, commandPayload, "Unknown");
    }


    SendMessageType(int requestCode, byte[] commandPayload, String description)
    {
        this(requestCode, null, commandPayload, description);
    }


    SendMessageType(int requestCode, int responseCode)
    {
        this(requestCode, responseCode, null, "Unknown");
    }


    SendMessageType(int requestCode, Integer responseCode, byte[] commandPayload, String description)
    {
        this(requestCode, responseCode, commandPayload, description, null);
    }


    SendMessageType(int requestCode, Integer responseCode, byte[] commandPayload, String description,
            ResponseExpected responseExpected)
    {
        this.requestCode = (short) requestCode;

        if (responseCode != null)
            this.responseCode = responseCode.shortValue();

        this.commandPayload = commandPayload;
        this.description = description;
        this.responseExpected = responseExpected;
    }


    public short getRequestCode()
    {
        return this.requestCode;
    }


    public short getResponseCode()
    {
        return this.responseCode;
    }


    public byte[] getCommandPayload()
    {
        return this.commandPayload;
    }


    public int getCommandCode()
    {
        return this.requestCode;
    }


    public String getDescription()
    {
        return description;
    }


    public ResponseExpected getResponseExpected()
    {
        return responseExpected;
    }


    public int getRecordLength()
    {
        return recordLength;
    }
}
