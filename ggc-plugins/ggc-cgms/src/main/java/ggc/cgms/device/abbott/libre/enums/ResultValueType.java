package ggc.cgms.device.abbott.libre.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 28/08/17.
 */
public enum ResultValueType
{

    Glucose(0), //
    Ketone(1), //
    Scan(2), //
    ;

    byte code;
    static Map<Byte, ResultValueType> mapByCode;

    static
    {
        mapByCode = new HashMap<Byte, ResultValueType>();

        for (ResultValueType command : values())
        {
            mapByCode.put(command.getCode(), command);
        }
    }


    ResultValueType(int code)
    {
        this.code = (byte) code;
    }


    byte getCode()
    {
        return this.code;
    }


    public static ResultValueType getByCode(byte code)
    {
        return mapByCode.get(code);
    }
}
