package ggc.plugin.device.impl.minimed.enums;

public enum MinimedDeviceType
{
    Unknown_Device, //

    Minimed_508_508c, //
    Minimed_511, //
    Minimed_512_712, //
    Minimed_515_715, //
    Minimed_522_722, //
    Minimed_523_723, //
    Minimed_553_753_Revel, //
    Minimed_554_754_Veo, //
    Minimed_640G,

    Minimed_512andHigher(Minimed_512_712, Minimed_515_715, Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel,
            Minimed_554_754_Veo, Minimed_640G), //

    Minimed_515andHigher(Minimed_515_715, Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo,
            Minimed_640G), //
    Minimed_522andHigher(Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo, Minimed_640G), //
    Minimed_523andHigher(Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo, Minimed_640G), //

    Minimed_553andHigher(Minimed_553_753_Revel, Minimed_554_754_Veo, Minimed_640G), //
    Minimed_554andHigher(Minimed_554_754_Veo, Minimed_640G), //

    All;

    // static HashMap<String, MinimedDeviceType> deviceTypeToPumpModelMap =
    // null;
    private boolean isFamily;
    private MinimedDeviceType[] familyMembers = null;

    static
    {
        // deviceTypeToPumpModelMap = new HashMap<String, MinimedDeviceType>();
        //
        // for (MinimedDeviceType adt : values())
        // {
        // if ((!adt.isFamily()) &&
        // (!deviceTypeToPumpModelMap.containsKey(adt.pumpModel)))
        // {
        // deviceTypeToPumpModelMap.put(adt.pumpModel, adt);
        // }
        // }
    }


    private MinimedDeviceType()
    {
        this.isFamily = false;
    }


    private MinimedDeviceType(MinimedDeviceType... familyMembers)
    {
        this.familyMembers = familyMembers;
        this.isFamily = true;
    }


    public static boolean isSameDevice(MinimedDeviceType deviceWeCheck, MinimedDeviceType deviceSources)
    {
        if (deviceSources.isFamily)
        {
            for (MinimedDeviceType mdt : deviceSources.familyMembers)
            {
                if (mdt == deviceWeCheck)
                    return true;
            }
        }
        else
        {
            return (deviceWeCheck == deviceSources);
        }

        return false;
    }


    public boolean isFamily()
    {
        return isFamily;
    }


    public MinimedDeviceType[] getFamilyMembers()
    {
        return familyMembers;
    }

}
