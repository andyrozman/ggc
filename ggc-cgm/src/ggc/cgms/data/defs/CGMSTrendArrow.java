package ggc.cgms.data.defs;

import java.util.HashMap;


public enum CGMSTrendArrow
{
    
    DoubleUp(1),
    SingleUp(2),
    FortyFiveUp(3),
    Flat(4),
    FortyFiveDown(5),
    SingleDown(6),
    DoubleDown(7),
    
    None(0),
    NotComputable(8),
    RateOutOfRange(9),
    ;
    
    private int value;
    private static HashMap<Integer,CGMSTrendArrow> map = new HashMap<Integer,CGMSTrendArrow>();
    
    static
    {
        for(CGMSTrendArrow el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    CGMSTrendArrow(int value)
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
    
    public static CGMSTrendArrow getEnum(int value)
    {
        return map.get(value);
    }

}

