package ggc.cgms.device.abbott.libre.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 28/08/17.
 */
// TODO is this real class
public enum BaseCommand
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
    static Map<Integer, BaseCommand> mapByRequestCode;
    static Map<Integer, BaseCommand> mapByResponseCode;

    static
    {
        mapByRequestCode = new HashMap<Integer, BaseCommand>();
        mapByResponseCode = new HashMap<Integer, BaseCommand>();

        for (BaseCommand command : values())
        {
            mapByRequestCode.put(command.getRequestCode(), command);
            mapByResponseCode.put(command.getResponseCode(), command);
        }
    }


    BaseCommand(int requestCode)
    {
        this.requestCode = requestCode;
    }


    BaseCommand(int requestCode, int responseCode)
    {
        this.requestCode = requestCode;
        this.responseCode = responseCode;
    }


    public int getRequestCode()
    {
        return this.requestCode;
    }


    public static BaseCommand getByRequestCode(int code)
    {
        return mapByRequestCode.get(code);
    }


    public static BaseCommand getByResponseCode(int code)
    {
        return mapByResponseCode.get(code);
    }


    public int getResponseCode()
    {
        return responseCode;
    }

}
