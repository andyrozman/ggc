package ggc.cgms.data.defs;

import java.util.ArrayList;
import java.util.List;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;

/**
 * Created by andy on 06.02.15.
 */
public enum CGMSDeviceDefinition implements DeviceDefinition
{

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe_cgms.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Vibe,
            DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    ;


    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;


    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for(CGMSDeviceDefinition cdd : values())
        {
            allDevices.add(cdd);

            if (cdd.isSupportedDevice())
            {
                supportedDevices.add(cdd);
            }
        }
    }




    // we need to extend this to all values currently in DeviceImplementation
    int deviceId;
    String deviceName;
    String iconName;
    String instructions;
    Object internalDefintion;
    DeviceImplementationStatus implementationStatus;
    DeviceCompanyDefinition companyDefinition;
    DeviceHandlerType deviceHandlerType;



    private CGMSDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition, DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition, DeviceHandlerType deviceHandlerType)
    {
        this.deviceId = id;
        this.deviceName = name;
        this.iconName = iconName;
        this.instructions = instructions;
        this.internalDefintion = internalDefinition;
        this.implementationStatus = implementationStatus;
        this.companyDefinition = companyDefinition;
    }

    public int getDeviceId()
    {
        return deviceId;
    }

    public String getDeviceName()
    {
        return this.deviceName;
    }

    public String getIconName()
    {
        return this.iconName;
    }


    public String getInstructionsI18nKey()
    {
        return this.instructions;
    }

    public DeviceImplementationStatus getDeviceImplementationStatus()
    {
        return this.implementationStatus;
    }

    public Object getInternalDefintion()
    {
        return this.internalDefintion;
    }

    public DeviceCompanyDefinition getDeviceCompany()
    {
        return this.companyDefinition;
    }

    public DeviceHandlerType getDeviceHandler()
    {
        return this.deviceHandlerType;
    }

    public static List<DeviceDefinition> getSupportedDevices()
    {
        return supportedDevices;
    }

    public static List<DeviceDefinition> getAllDevices()
    {
        return allDevices;
    }

    public boolean isSupportedDevice()
    {
        return isSupportedDevice(this);
    }


    public static boolean isSupportedDevice(CGMSDeviceDefinition cgmsDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(cgmsDeviceDefinition.getDeviceImplementationStatus());
    }

}
