package ggc.plugin.device.mgr;

import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.v2.DeviceHandler;

import java.util.HashMap;

/**
 * Created by andy on 10.02.15.
 */
public class DeviceHandlerManager
{
    private static DeviceHandlerManager deviceHandlerManager = null;


    HashMap<String,DeviceHandler> deviceHandlers = new HashMap<String, DeviceHandler>();


    public static DeviceHandlerManager getInstance()
    {
        if (deviceHandlerManager == null)
        {
            deviceHandlerManager = new DeviceHandlerManager();
        }

        return deviceHandlerManager;
    }



    private DeviceHandlerManager()
    {
    }


    public void addDeviceHandler(DeviceHandler handler)
    {
    }


    public DeviceHandler getDeviceHandler(DeviceHandlerType deviceHandlerType)
    {
        return null;
    }

}
