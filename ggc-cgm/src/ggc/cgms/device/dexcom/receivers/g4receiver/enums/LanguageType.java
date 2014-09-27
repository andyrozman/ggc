package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum LanguageType
{
    Czech(0x405, "LANGUAGE_CZECH"), //
    Dutch(0x413, "LANGUAGE_DUTCH"), //
    English(0x409, "LANGUAGE_ENGLISH"), //
    Finnish(0x40b, "LANGUAGE_FINNISH"), //
    French(0x40c, "LANGUAGE_FRENCH"), //
    French_Canada(0xc0c, "LANGUAGE_FRENCH_CANADA"), //
    German(0x407, "LANGUAGE_GERMAN"), //
    Italian(0x410, "LANGUAGE_ITALIAN"), //
    None(0, "NONE"), //
    Polish(0x415, "LANGUAGE_POLISH"), //
    Portugese_Brazil(0x416, "LANGUAGE_PORTUGESE_BRAZIL"), //
    Spanish(0x40a, "LANGUAGE_SPANISH"), //
    Swedish(0x41d, "LANGUAGE_SWEDISH"); //

    private int value;
    private String description;
    private static HashMap<Integer, LanguageType> map = new HashMap<Integer, LanguageType>();

    static
    {
        for (LanguageType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    LanguageType(int value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public static LanguageType getEnum(int value)
    {
        return map.get(value);
    }
}
