package ggc.pump.data.defs;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 06.02.15.
 */
public enum PumpDeviceDefinition implements DeviceDefinition
{

    Animas_IR1000(40001, "Animas IR 1000", "", "", AnimasDeviceType.Animas_IR1000,
            DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED, DeviceCompanyDefinition.Animas, DeviceHandlerType.NoHandler),

    Animas_IR1200(40002, "Animas IR 1200", "an_ir1200.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1200,
            DeviceImplementationStatus.IMPLEMENTATION_DONE, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    Animas_IR1250(40003, "Animas IR 1250", "an_ir1250.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1250,
            DeviceImplementationStatus.IMPLEMENTATION_DONE, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    Animas_2200(40004, "Animas IR 2020", "an_ir2020.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_2200,
            DeviceImplementationStatus.IMPLEMENTATION_DONE, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    OneTouchPing(40005, "OneTouch Ping", "an_ping.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Ping,
            DeviceImplementationStatus.IMPLEMENTATION_DONE, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe.png", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Vibe,
            DeviceImplementationStatus.IMPLEMENTATION_DONE, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler),

    ;


    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;


    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for(PumpDeviceDefinition pdd : values())
        {
            allDevices.add(pdd);

            if (pdd.isSupportedDevice())
            {
                supportedDevices.add(pdd);
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



    private PumpDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition, DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition, DeviceHandlerType deviceHandlerType)
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


    public static boolean isSupportedDevice(PumpDeviceDefinition pumpDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(pumpDeviceDefinition.getDeviceImplementationStatus());
    }


}
