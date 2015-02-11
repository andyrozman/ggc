package ggc.meter.data.defs;

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
public enum MeterDeviceDefinition implements DeviceDefinition
{

    AscensiaNextUsb(10009, "Ascensia Next USB", "pict", "instr", null,
            DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS, DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler),





    ;

    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;


    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for(MeterDeviceDefinition mdd : values())
        {
            allDevices.add(mdd);

            if (mdd.isSupportedDevice())
            {
                supportedDevices.add(mdd);
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



    private MeterDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition, DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition, DeviceHandlerType deviceHandlerType)
    {
        this.deviceId = id;
        this.deviceName = name;
        this.iconName = iconName;
        this.instructions = instructions;
        this.internalDefintion = internalDefinition;
        this.implementationStatus = implementationStatus;
        this.companyDefinition = companyDefinition;
        this.deviceHandlerType = deviceHandlerType;
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

    public List<DeviceDefinition> getSupportedDevices()
    {
        return supportedDevices;
    }

    public List<DeviceDefinition> getAllDevices()
    {
        return allDevices;
    }

    public boolean isSupportedDevice()
    {
        return isSupportedDevice(this);
    }


    public static boolean isSupportedDevice(MeterDeviceDefinition meterDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(meterDeviceDefinition.getDeviceImplementationStatus());
    }


}
