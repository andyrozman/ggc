//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:13
//

package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum Health
{
    Null(0), // 
    Illness(1), // 
    Stress(2), // 
    HighSymptoms(3), // 
    LowSymptoms(4), // 
    Cycle(5), //
    Alcohol(6), //

    ;

    private int value;
    private static HashMap<Integer, Health> map = new HashMap<Integer, Health>();

    static
    {
        for (Health el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    Health(int value)
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

    public static Health getEnum(int value)
    {
        return map.get(value);
    }

}
