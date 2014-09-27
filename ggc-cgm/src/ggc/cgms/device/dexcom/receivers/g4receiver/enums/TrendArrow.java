package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum TrendArrow
{

    DoubleUp(1), SingleUp(2), FortyFiveUp(3), Flat(4), FortyFiveDown(5), SingleDown(6), DoubleDown(7),

    None(0), NotComputable(8), RateOutOfRange(9), ;

    private int value;
    private static HashMap<Integer, TrendArrow> map = new HashMap<Integer, TrendArrow>();

    static
    {
        for (TrendArrow el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    TrendArrow(int value)
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

    public static TrendArrow getEnum(int value)
    {
        return map.get(value);
    }

}
