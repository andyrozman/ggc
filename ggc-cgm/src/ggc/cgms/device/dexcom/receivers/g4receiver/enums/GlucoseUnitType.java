package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum GlucoseUnitType
{

    mgPerDL(1, "GLUCOSE_UNIT_MGDL"), //
    mmolPerL(2, "GLUCOSE_UNIT_MMOLL"), //
    None(0, "NONE"); //

    private int value;
    private String description;
    private static HashMap<Integer, GlucoseUnitType> map = new HashMap<Integer, GlucoseUnitType>();

    static
    {
        for (GlucoseUnitType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    GlucoseUnitType(int value, String description)
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

    public static GlucoseUnitType getEnum(int value)
    {
        return map.get(value);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
