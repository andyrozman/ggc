package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum SensorSessionState
{
    BadTransmitter(8), CountsAberration(4), EnteringManufacturingMode(9), None(0), ResidualAberration(3), SensorRemoved(
            1), SensorShutOffDueToTimeLoss(6), SessionExpired(2), SessionStarted(7), TryingToStartSecondSession(5);

    private int value;
    private static HashMap<Integer, SensorSessionState> map = new HashMap<Integer, SensorSessionState>();

    static
    {
        for (SensorSessionState el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    SensorSessionState(int value)
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

    public static SensorSessionState getEnum(int value)
    {
        return map.get(value);
    }

}
