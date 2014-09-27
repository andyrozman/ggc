package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum SpecialGlucoseValues
{
    None(0), SensorNotActive(1), MinimallyEGVAberration(2), NoAntenna(3), SensorOutOfCal(5), CountsAberration(6), AbsoluteAberration(
            9), PowerAberration(10), RFBadStatus(12);

    private int value;
    private static HashMap<Integer, SpecialGlucoseValues> map = new HashMap<Integer, SpecialGlucoseValues>();

    static
    {
        for (SpecialGlucoseValues el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    SpecialGlucoseValues(int value)
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

    public static SpecialGlucoseValues getEnum(int value)
    {
        return map.get(value);
    }

}
