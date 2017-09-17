package ggc.plugin.device.impl.minimed.comm.usb.contournext.enums;

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


public enum CommandType
{
    NO_TYPE(0x0), //

    // 5xx and 6xx
    OPEN_CONNECTION(0x10, 0x01, 0x1E), //
    CLOSE_CONNECTION(0x11, 0x01, 0x1E), //
    SEND_MESSAGE(0x12, 0x81, 0x21, 0x05), //

    READ_INFO(0x14, null, "Read Info", ResponseExpected.OneFirstConverted), //
    REQUEST_LINK_KEY(0x16, 0x86, "Request Link Key", ResponseExpected.OneFirstConverted), //
    SEND_LINK_KEY(0x17), //
    RECEIVE_MESSAGE(0x80, 0x81), //
    // SEND_MESSAGE_RESPONSE(0x81), //
    // REQUEST_LINK_KEY_RESPONSE(0x86) //
    ;

    private byte requestCommandType;
    private byte responseCommandType;
    private byte retries = 0x00;
    private byte timeout = 0x00;
    String description;
    ResponseExpected responseExpected;


    CommandType(int commandType)
    {
        this.requestCommandType = (byte) commandType;
    }


    CommandType(int requestCommandType, int responseCommandType)
    {
        this.requestCommandType = (byte) requestCommandType;
        this.responseCommandType = (byte) responseCommandType;
    }


    CommandType(int requestCommandType, int retries, int timeout)
    {
        this.requestCommandType = (byte) requestCommandType;
        this.retries = (byte) retries;
        this.timeout = (byte) timeout;
    }


    CommandType(int requestCommandType, int responseCommandType, int retries, int timeout)
    {
        this.requestCommandType = (byte) requestCommandType;
        this.responseCommandType = (byte) responseCommandType;

        this.retries = (byte) retries;
        this.timeout = (byte) timeout;
    }


    CommandType(int requestCommandType, Integer responseCommandType, String description,
            ResponseExpected responseExpected)
    {
        this(requestCommandType, responseCommandType, null, null, description, responseExpected);
    }


    CommandType(int requestCommandType, Integer responseCommandType, Integer retries, Integer timeout,
            String description, ResponseExpected responseExpected)
    {
        this.requestCommandType = (byte) requestCommandType;

        if (responseCommandType != null)
            this.responseCommandType = responseCommandType.byteValue();

        if (retries != null)
            this.retries = retries.byteValue();

        if (timeout != null)
            this.timeout = timeout.byteValue();

        this.description = description;
        this.responseExpected = responseExpected;
    }


    public byte getRequestCommandType()
    {
        return requestCommandType;
    }


    public byte getResponseCommandType()
    {
        return responseCommandType;
    }


    public byte getRetries()
    {
        return retries;
    }


    public byte getTimeout()
    {
        return timeout;
    }


    public ResponseExpected getResponseExpected()
    {
        return responseExpected;
    }


    public String getDescription()
    {
        return description;
    }
}
