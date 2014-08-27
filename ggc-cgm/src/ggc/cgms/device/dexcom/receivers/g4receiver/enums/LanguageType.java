package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum LanguageType
{
    Czech(0x405), Dutch(0x413), English(0x409), Finnish(0x40b), French(0x40c), French_Canada(0xc0c), German(0x407), Italian(
            0x410), None(0), Polish(0x415), Portugese_Brazil(0x416), Spanish(0x40a), Swedish(0x41d);

    private int value;
    private static HashMap<Integer, LanguageType> map = new HashMap<Integer, LanguageType>();

    static
    {
        for (LanguageType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    LanguageType(int value)
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

    public static LanguageType getEnum(int value)
    {
        return map.get(value);
    }

}
