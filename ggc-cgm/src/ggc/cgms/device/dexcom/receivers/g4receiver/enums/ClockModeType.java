package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum ClockModeType
{
    ClockMode24Hour(0), // 
    ClockMode12Hour(1), //
    ;

    private int value;
    private static HashMap<Integer, ClockModeType> map = new HashMap<Integer, ClockModeType>();

    static
    {
        for (ClockModeType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    ClockModeType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public static ClockModeType getEnum(int value)
    {
        return map.get(value);
    }

}
