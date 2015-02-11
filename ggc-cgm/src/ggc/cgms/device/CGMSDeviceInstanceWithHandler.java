package ggc.cgms.device;

import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandler;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;

/**
 * Created by andy on 10.02.15.
 */
public class CGMSDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements CGMSInterface
{
    public CGMSDeviceInstanceWithHandler(DeviceDefinition deviceDefinition)
    {
        super(deviceDefinition);
    }


}
