package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum NoiseMode
{
    Clean(1),
    Heavy(4),
    Light(2),
    Max(6),
    Medium(3),
    None(0),
    NotComputed(5);
    
    private int value;
    private static HashMap<Integer,NoiseMode> map = new HashMap<Integer,NoiseMode>();
    
    static
    {
        for(NoiseMode el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    NoiseMode(int value)
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

    public static NoiseMode getEnum(int value)
    {
        return map.get(value);
    }

}

