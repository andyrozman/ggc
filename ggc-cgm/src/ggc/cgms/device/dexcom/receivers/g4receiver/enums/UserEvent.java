package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum UserEvent
{
    NullType(0), //
    Carbs(1), //
    Insulin(2), //
    Health(3), //
    Exercise(4), //
    MaxValue(5), //
    ;

    private int value;
    private static HashMap<Integer, UserEvent> map = new HashMap<Integer, UserEvent>();

    static
    {
        for (UserEvent el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    UserEvent(int value)
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

    public static UserEvent getEnum(int value)
    {
        return map.get(value);
    }

}
