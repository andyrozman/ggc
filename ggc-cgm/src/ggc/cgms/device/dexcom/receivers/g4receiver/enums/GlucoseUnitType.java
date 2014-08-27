//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:14
//

package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum GlucoseUnitType
{

    mgPerDL(1), mmolPerL(2), None(0);

    private int value;
    private static HashMap<Integer, GlucoseUnitType> map = new HashMap<Integer, GlucoseUnitType>();

    static
    {
        for (GlucoseUnitType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    GlucoseUnitType(int value)
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

    public static GlucoseUnitType getEnum(int value)
    {
        return map.get(value);
    }

}
