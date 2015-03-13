package ggc.pump.data.defs;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

import java.util.ArrayList;
import java.util.List;


public enum PumpDeviceDefinition implements DeviceDefinition
{

    // Animas / One Touch Devices
    Animas_IR1000(40001, "Animas IR 1000", "", "", AnimasDeviceType.Animas_IR1000,
            DeviceImplementationStatus.NotPlanned, DeviceCompanyDefinition.Animas,
            DeviceHandlerType.NoHandler, DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

    Animas_IR1200(40002, "Animas IR 1200", "an_ir1200.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1200,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

    Animas_IR1250(40003, "Animas IR 1250", "an_ir1250.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1250,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

    Animas_2200(40004, "Animas IR 2020", "an_ir2020.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_2200,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

    OneTouchPing(40005, "OneTouch Ping", "an_ping.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Ping,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe.gif", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Vibe,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),

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
    DevicePortParameterType devicePortParameterType;
    DeviceConnectionProtocol deviceConnectionProtocol;
    DeviceProgressStatus deviceProgressStatus;
    String specialComment;



    private PumpDeviceDefinition(int id, String name, String iconName, String instructions,
                                 Object internalDefinition, DeviceImplementationStatus implementationStatus,
                                 DeviceCompanyDefinition companyDefinition, DeviceHandlerType deviceHandlerType,
                                 DevicePortParameterType portParameterType, DeviceConnectionProtocol connectionProtocol,
                                 DeviceProgressStatus progressStatus, String specialComment)
    {
        this.deviceId = id;
        this.deviceName = name;
        this.iconName = iconName;
        this.instructions = instructions;
        this.internalDefintion = internalDefinition;
        this.implementationStatus = implementationStatus;
        this.companyDefinition = companyDefinition;
        this.deviceHandlerType = deviceHandlerType;
        this.devicePortParameterType = portParameterType;
        this.deviceConnectionProtocol = connectionProtocol;
        this.deviceProgressStatus = progressStatus;
        this.specialComment = specialComment;
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


    /**
     * Get Device handler key
     * @return
     */
    public DeviceHandlerType getDeviceHandlerKey()
    {
        return this.deviceHandlerType;
    }


    /**
     * Device Port Parameter Type
     * @return
     */
    public DevicePortParameterType getDevicePortParameterType()
    {
        return this.devicePortParameterType;
    }

    /**
     * Get Connection Protocol
     *
     * @return
     */
    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return this.deviceConnectionProtocol;
    }

    /**
     * Get Device Progress Status. It determines how device progress is determined. In most casess we use Special
     * progress which is then implemented by Handler.
     *
     * @return
     */
    public DeviceProgressStatus getDeviceProgressStatus()
    {
        return this.deviceProgressStatus;
    }


    public String getSpecialComment()
    {
        return this.specialComment;
    }

}
