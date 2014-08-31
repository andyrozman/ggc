package ggc.core.data.defs;

import java.util.HashMap;


public enum ExerciseStrength
{
    Heavy(3),
    Light(1),
    MaxValue(4),
    Medium(2),
    Null(0);
    
    private int value;
    private static HashMap<Integer,ExerciseStrength> map = new HashMap<Integer,ExerciseStrength>();
    
    static
    {
        for(ExerciseStrength el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    ExerciseStrength(int value)
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

    public static ExerciseStrength getEnum(int value)
    {
        return map.get(value);
    }
    
}

