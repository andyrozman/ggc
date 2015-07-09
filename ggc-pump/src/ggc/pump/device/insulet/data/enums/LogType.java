package ggc.pump.device.insulet.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 20.05.15.
 */
public enum LogType
{

    History(0x03, 31), //
    PumpAlarm(0x05, 0), //
    Unknown(-1, 0),
    // Deleted
    ;

    //
    // HISTORY: {value: 0x03, name: 'HISTORY'},
    // PUMP_ALARM: {value: 0x05, name: 'PUMP_ALARM'},
    // DELETED: {mask: 0x80000000, name: 'DELETED'},
    // // this is an impossible value for a 1-byte LOG_TYPE
    // IGNORE: {value: 0x100, name: 'IGNORED by driver'}

    private final int offset;
    int code;

    static Map<Integer, LogType> mapByCode = new HashMap<Integer, LogType>();

    static
    {
        for (LogType hrt : values())
        {
            mapByCode.put(hrt.code, hrt);
        }
    }


    LogType(int code, int offset)
    {
        this.code = code;
        this.offset = offset;
    }


    public static LogType getByCode(int code)
    {
        if (mapByCode.containsKey(code))
        {
            return mapByCode.get(code);
        }

        return LogType.Unknown;
    }


    public int getOffset()
    {
        return offset;
    }
}
