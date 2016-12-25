package ggc.core.util;

import java.util.HashMap;

public enum GGCSoftwareMode
{
    PEN_INJECTION_MODE(0), //
    PUMP_MODE(1);

    private int mode;

    private static HashMap<Integer, GGCSoftwareMode> map = new HashMap<Integer, GGCSoftwareMode>();

    static
    {
        for (GGCSoftwareMode el : values())
        {
            map.put(el.getMode(), el);
        }
    }

    GGCSoftwareMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return mode;
    }

    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public static GGCSoftwareMode getEnum(int value)
    {
        return map.get(value);
    }

}
