package ggc.cgms.device;

import ggc.cgms.data.defs.CGMSDeviceDefinition;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandler;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;

/**
 * Created by andy on 10.02.15.
 */
public class CGMSDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements CGMSInterfaceV2
{
    public CGMSDeviceInstanceWithHandler(CGMSDeviceDefinition deviceDefinition)
    {
        super(deviceDefinition, DataAccessCGMS.getInstance());
    }


}
