package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum ReceiverRecordType
{
    ManufacturingData(0),
    FirmwareParameterData(1),
    PCSoftwareParameter(2),
    SensorData(3),
    EGVData(4),
    CalSet(5),
    Aberration(6),
    
    InsertionTime(7),
    ReceiverLogData(8),
    ReceiverErrorData(9),
    MeterData(10),
    UserEventData(11),
    UserSettingData(12),
    MaxValue(13),
    
    None(-1);
    
    

    private int value;
    private static HashMap<Integer,ReceiverRecordType> map = new HashMap<Integer,ReceiverRecordType>();
    
    static
    {
        for(ReceiverRecordType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    ReceiverRecordType(int value)
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

    public static ReceiverRecordType getEnum(int value)
    {
        return map.get(value);
    }

}

