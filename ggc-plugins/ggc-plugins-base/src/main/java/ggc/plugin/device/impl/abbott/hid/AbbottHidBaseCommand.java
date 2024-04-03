package ggc.plugin.device.impl.abbott.hid;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 28/08/17.
 */
public enum AbbottHidBaseCommand
{

    INIT_REQUEST_1(0x04, 0x34), // 04
    INIT_REQUEST_2(0x05, 0x06), // 05
    INIT_REQUEST_3(0x15, 0x35), // 15 / D
    INIT_REQUEST_4(0x01, 0x71), // 01
    BINARY_REQUEST(0x0a), //
    BINARY_RESPONSE(0x0b), //
    ACK_FROM_DEVICE(0x0c), //
    ACK_FROM_HOST(0x0d), //
    // TEXT_REQUEST(0x21), //
    TEXT_COMMAND(0x60, 0x60)

    ; // 60

    int requestCode;
    int responseCode;
    static Map<Integer, AbbottHidBaseCommand> mapByRequestCode;
    static Map<Integer, AbbottHidBaseCommand> mapByResponseCode;

    static
    {
        mapByRequestCode = new HashMap<Integer, AbbottHidBaseCommand>();
        mapByResponseCode = new HashMap<Integer, AbbottHidBaseCommand>();

        for (AbbottHidBaseCommand command : values())
        {
            mapByRequestCode.put(command.getRequestCode(), command);
            mapByResponseCode.put(command.getResponseCode(), command);
        }
    }


    AbbottHidBaseCommand(int requestCode)
    {
        this.requestCode = requestCode;
    }


    AbbottHidBaseCommand(int requestCode, int responseCode)
    {
        this.requestCode = requestCode;
        this.responseCode = responseCode;
    }


    public int getRequestCode()
    {
        return this.requestCode;
    }


    public static AbbottHidBaseCommand getByRequestCode(int code)
    {
        return mapByRequestCode.get(code);
    }


    public static AbbottHidBaseCommand getByResponseCode(int code)
    {
        return mapByResponseCode.get(code);
    }


    public int getResponseCode()
    {
        return responseCode;
    }

}
