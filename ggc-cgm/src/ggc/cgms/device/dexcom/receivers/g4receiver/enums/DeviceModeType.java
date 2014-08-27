//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:14
//

package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum DeviceModeType
{
    
    Manufactuiring(1),
    Normal(0);
    
    
    private int value;
    private static HashMap<Integer,DeviceModeType> map = new HashMap<Integer,DeviceModeType>();
    
    static
    {
        for(DeviceModeType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    DeviceModeType(int value)
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

    public static DeviceModeType getEnum(int value)
    {
        return map.get(value);
    }

}

