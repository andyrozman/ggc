package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum BlindedModeType
{

    Blinded(1), Unblinded(0);

    private int value;
    private static HashMap<Integer, BlindedModeType> map = new HashMap<Integer, BlindedModeType>();

    static
    {
        for (BlindedModeType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    BlindedModeType(int value)
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

    public static BlindedModeType getEnum(int value)
    {
        return map.get(value);
    }

}
