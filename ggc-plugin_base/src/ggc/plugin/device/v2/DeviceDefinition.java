package ggc.plugin.device;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.manager.DeviceImplementationStatus;

import java.util.List;

/**
 * Created by andy on 06.02.15.
 */
public interface DeviceDefinition
{

    public int getDeviceId();

    public String getDeviceName();

    public String getIconName();

    public String getInstructionsI18nKey();

    public DeviceImplementationStatus getDeviceImplementationStatus();

    public Object getInternalDefintion();

    public DeviceCompanyDefinition getDeviceCompany();

    public DeviceHandlerType getDeviceHandler();


}
