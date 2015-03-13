package ggc.cgms.data.defs;

import java.util.ArrayList;
import java.util.List;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 06.02.15.
 */
public enum CGMSDeviceDefinition implements DeviceDefinition
{

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe_cgms.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Vibe,
            DeviceImplementationStatus.InProgress, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2CGMSHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, ""),
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

        System.out.println("CGMS Devices V2 (registered: " + allDevices.size() + ", supported: " + supportedDevices.size() + ")");

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



    private CGMSDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
                                 DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
                                 DeviceHandlerType deviceHandlerType,
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


    public static boolean isSupportedDevice(CGMSDeviceDefinition cgmsDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(cgmsDeviceDefinition.getDeviceImplementationStatus());
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
