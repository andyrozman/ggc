package ggc.cgms.data.defs;

import java.util.HashMap;

import com.atech.utils.data.CodeEnum;

public enum CGMSTransmiterEvents implements CodeEnum
{
    None(0), //
    SensorNotActive(1), //
    MinimallyEGVAberration(2), //
    NoAntenna(3), //
    Event_4(4), //
    SensorOutOfCal(5), //
    CountsAberration(6), //
    AbsoluteAberration(9), //
    PowerAberration(10), //
    RFBadStatus(12);

    private int value;
    private static HashMap<Integer, CGMSTransmiterEvents> map = new HashMap<Integer, CGMSTransmiterEvents>();

    static
    {
        for (CGMSTransmiterEvents el : values())
        {
            map.put(el.getCode(), el);
        }
    }

    CGMSTransmiterEvents(int value)
    {
        this.value = value;
    }

    public int getCode()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public static CGMSTransmiterEvents getByCode(int value)
    {
        return map.get(value);
    }

}
