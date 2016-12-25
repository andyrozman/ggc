package ggc.pump.device.insulet.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 27.05.15.
 */
public enum AlarmType
{
    None(-1, null, null, null, null, null), //
    AlrmPDM_ERROR0(0, "AlrmPDM_ERROR0", "PDM error", null, "Error_PDMError", "PDM Error 0"), //
    AlrmPDM_ERROR1(1, "AlrmPDM_ERROR1", "PDM error", null, "Error_PDMError", "PDM Error 1"), //
    AlrmPDM_ERROR2(2, "AlrmPDM_ERROR2", "PDM error", null, "Error_PDMError", "PDM Error 2"), //
    AlrmPDM_ERROR3(3, "AlrmPDM_ERROR3", "PDM error", null, "Error_PDMError", "PDM Error 3"), //
    AlrmPDM_ERROR4(4, "AlrmPDM_ERROR4", "PDM error", null, "Error_PDMError", "PDM Error 4"), //
    AlrmPDM_ERROR5(5, "AlrmPDM_ERROR5", "PDM error", null, "Error_PDMError", "PDM Error 5"), //
    AlrmPDM_ERROR6(6, "AlrmPDM_ERROR6", "PDM error", null, "Error_PDMError", "PDM Error 6"), //
    AlrmPDM_ERROR7(7, "AlrmPDM_ERROR7", "PDM error", null, "Error_PDMError", "PDM Error 7"), //
    AlrmPDM_ERROR8(8, "AlrmPDM_ERROR8", "PDM error", null, "Error_PDMError", "PDM Error 8"), //
    AlrmPDM_ERROR9(9, "AlrmPDM_ERROR9", "PDM error", null, "Error_PDMError", "PDM Error 9"), //
    AlrmSYSTEM_ERROR10(10, "AlrmSYSTEM_ERROR10", "system error", false, "Error_PDMError", "PDM Error 10 - System error"), //
    AlrmSYSTEM_ERROR11(11, "AlrmSYSTEM_ERROR12", "system error", null, "Error_PDMError", "PDM Error 11 - System error"), //
    AlrmSYSTEM_ERROR12(12, "AlrmSYSTEM_ERROR12", "system error", null, "Error_PDMError", "PDM Error 12 - System error"), //
    AlrmHAZ_REMOTE(13, "AlrmHAZ_REMOTE", "clock reset alarm", false, "Alarm_ClockReset", null), //
    AlrmHAZ_PUMP_VOL(14, "AlrmHAZ_PUMP_VOL", "empty reservoir", false, "Alarm_EmptyCartridge", null), //
    AlrmHAZ_PUMP_AUTO_OFF(15, "AlrmHAZ_PUMP_AUTO_OFF", "auto-off", true, "Alarm_AutoOff", null), //
    AlrmHAZ_PUMP_EXPIRED(16, "AlrmHAZ_PUMP_EXPIRED", "pod expired", true, "Error_PodError", "Expired"), //
    AlrmHAZ_PUMP_OCCL(17, "AlrmHAZ_PUMP_OCCL", "pump site occluded", true, "Error_NoDeliveryOcclusion", null), //
    AlrmHAZ_PUMP_ACTIVATE(18, "AlrmHAZ_PUMP_ACTIVATE", "pod is a lump of coal", false, null, null), // ignored
    AlrmADV_KEY(21, "AlrmADV_KEY", "PDM stuck key detected", false, "Error_PDMError", "Stuck key detected"), //
    AlrmADV_PUMP_VOL(23, "AlrmADV_PUMP_VOL", "low reservoir", false, "Alarm_CartridgeLow", null), //
    AlrmADV_PUMP_AUTO_OFF(24, "AlrmADV_PUMP_AUTO_OFF", "15 minutes to auto-off warning", false, "Alarm_AutoOffSoon",
            "15 minutes"), //
    AlrmADV_PUMP_SUSPEND(25, "AlrmADV_PUMP_SUSPEND", "suspend done", false), // ignored
    AlrmADV_PUMP_EXP1(26, "AlrmADV_PUMP_EXP1", "pod expiration advisory", false, "Alarm_CartridgeLow", null), //
    AlrmADV_PUMP_EXP2(27, "AlrmADV_PUMP_EXP2", "pod expiration alert", false, "Alarm_EmptyCartridge", null), //
    AlrmSYSTEM_ERROR28(28, "AlrmSYSTEM_ERROR28", "system error", null, "Error_PDMError", "PDM Error 28 - System error"), //
    AlrmEXP_WARNING(37, "AlrmEXP_WARNING", "pod expiration advisory", false, "Alarm_CartridgeLow", null), //
    AlrmHAZ_PDM_AUTO_OFF(39, "AlrmHAZ_PDM_AUTO_OFF", "auto-off", true, "Alarm_AutoOff", null);

    private int code;
    private String name;
    private String description;
    private Boolean pumpStopped;
    private String ggcEventKey;
    private String ggcEventData;

    private static Map<Integer, AlarmType> mapByCode;

    static
    {
        mapByCode = new HashMap<Integer, AlarmType>();

        for (AlarmType atype : values())
        {
            mapByCode.put(atype.code, atype);
        }
    }


    AlarmType(int code, String name, String description, Boolean pumpStopped)
    {
        this(code, name, description, pumpStopped, null, null);
    }


    AlarmType(int code, String name, String description, Boolean pumpStopped, String eventAlarmKey, String eventValue)
    {
        this.code = code;
        this.name = name;
        this.description = description;
        this.pumpStopped = pumpStopped;
        this.ggcEventKey = eventAlarmKey;
        this.ggcEventData = eventValue;
    }


    public int getCode()
    {
        return code;
    }


    public String getName()
    {
        return name;
    }


    public String getDescription()
    {
        return description;
    }


    public Boolean getPumpStopped()
    {
        return pumpStopped;
    }


    public String getGGCEventKey()
    {
        return ggcEventKey;
    }


    public String getGGCEventData()
    {
        return ggcEventData;
    }


    public static AlarmType getByCode(int alarm_type)
    {
        if (mapByCode.containsKey(alarm_type))
            return mapByCode.get(alarm_type);
        else
            return None;
    }
}
