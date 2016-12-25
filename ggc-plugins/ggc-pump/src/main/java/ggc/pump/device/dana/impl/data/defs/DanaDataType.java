package main.java.ggc.pump.device.dana.impl.data.defs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 10.03.15.
 */
public enum DanaDataType
{

    Connect("Connect", new byte[] { 0x30, 1 }),
    Disconnect("Disconnect", new byte[] { 0x30, 2 }),
    Shipping("", new byte[] { 50, 7 }),
    SettingsBasalPattern("", new byte[] { 50, 2 }),
    SettingsGeneral("", new byte[] { 50, 3 }),
    SettingsMaxValues("", new byte[] { 50, 5 }),
    SettingsBolusHelper("", new byte[] { 50, 4 }),
    SettingsGlucomode("", new byte[] { 50, 9 }),


    Alarms("Alarm", new byte[] { 0x31, 5 }), //5
    Bolus("Bolus", new byte[] { 0x31, 1 }), // 1
    Carbohydrates("Carbohydrates", new byte[] { 0x31, 7 }), // 8
    DailyInsulin("Daily", new byte[] { 0x31, 2 }), // 2
    Errors("Error", new byte[] { 0x31, 6 }), // 4
    Glucose("Glucose", new byte[] { 0x31, 4 }), // 6
    Prime("Prime", new byte[] { 0x31, 3 }), // 3


    ;

    static Map<Integer, DanaDataType> mapByCode = new HashMap<Integer, DanaDataType>();

    static
    {
        mapByCode.put(1, Bolus);
        mapByCode.put(2, DailyInsulin);
        mapByCode.put(3, Prime);
        mapByCode.put(4, Errors);
        mapByCode.put(5, Alarms);
        mapByCode.put(6, Glucose);
        mapByCode.put(8, Carbohydrates);
    }







    private String description;
    private byte[] command;


    DanaDataType(String description, byte[] command)
    {
        this.description = description;
        this.command = command;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public byte[] getCommand()
    {
        return command;
    }

    public void setCommand(byte[] command)
    {
        this.command = command;
    }


    // public static byte[] SYNC_QUERY_BASAL_PROFILE = new byte[] { 50, 6 };
    // public static byte[] SYNC_QUERY_BOLUS = new byte[] { 50, 1 };
    // public static byte[] SYNC_QUERY_PWM = new byte[] { 50, 8 };

    public static DanaDataType getByCode(int code)
    {
        return mapByCode.get(code);

    }

}
