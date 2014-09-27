package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum Exercise
{
    Heavy(3), Light(1), MaxValue(4), Medium(2), Null(0);

    private int value;
    private static HashMap<Integer, Exercise> map = new HashMap<Integer, Exercise>();

    static
    {
        for (Exercise el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    Exercise(int value)
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

    public static Exercise getEnum(int value)
    {
        return map.get(value);
    }

}
