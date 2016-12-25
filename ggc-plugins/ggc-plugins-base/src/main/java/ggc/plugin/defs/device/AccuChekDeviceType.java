package ggc.plugin.defs.device;

import java.util.HashMap;

import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;

/**
 * Created by andy on 21.10.15.
 */
public enum AccuChekDeviceType
{
    AccuChekUnknown(), //
    AccuChekCombo(), //
    AccuChekSpirit(), //
    AccuChekDTron(), //
    DisetronicDTron(), //
    AccuChekActive(), //
    AccuChekAdvantage(), //
    AccuChekAviva(), //
    AccuChekAvivaCombo(), //
    AccuChekComfort(), //
    AccuChekCompact(), //
    AccuChekCompactPlus(), //
    AccuChekGo(), //

    ;

    AnimasImplementationType implementationType;
    String name;
    String pumpModel;
    static HashMap<String, AccuChekDeviceType> deviceTypeToPumpModelMap = null;
    private boolean isFamily;
    private AccuChekDeviceType[] familyMembers = null;


    static
    {
        deviceTypeToPumpModelMap = new HashMap<String, AccuChekDeviceType>();

        for (AccuChekDeviceType adt : values())
        {
            if ((!adt.isFamily()) && (!deviceTypeToPumpModelMap.containsKey(adt.pumpModel)))
            {
                deviceTypeToPumpModelMap.put(adt.pumpModel, adt);
            }
        }
    }


    private AccuChekDeviceType()
    {

    }


    private AccuChekDeviceType(AnimasImplementationType implementationType, String name, String pumpModel)
    {
        this.implementationType = implementationType;
        this.name = name;
        this.pumpModel = pumpModel;
    }


    private AccuChekDeviceType(AccuChekDeviceType... familyMembers)
    {
        this.familyMembers = familyMembers;
        this.isFamily = true;
    }


    public AnimasImplementationType getImplementationType()
    {
        return implementationType;
    }


    public static AccuChekDeviceType getAnimasDeviceTypeFromPumpModel(String pumpModel)
    {
        if (deviceTypeToPumpModelMap.containsKey(pumpModel))
        {
            return deviceTypeToPumpModelMap.get(pumpModel);
        }
        else
        {
            return AccuChekDeviceType.AccuChekUnknown;
        }
    }


    public String getDisplayName()
    {
        return name;
    }


    public boolean isFamily()
    {
        return isFamily;
    }


    public AccuChekDeviceType[] getFamilyMembers()
    {
        return familyMembers;
    }

}
