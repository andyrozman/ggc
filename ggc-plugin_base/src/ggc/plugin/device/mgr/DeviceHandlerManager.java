package ggc.plugin.device.mgr;

import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.v2.DeviceHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

/**
 * Created by andy on 10.02.15.
 */
public class DeviceHandlerManager
{
    private static DeviceHandlerManager deviceHandlerManager = null;
    Log LOG = LogFactory.getLog(DeviceHandlerManager.class);


    HashMap<DeviceHandlerType,DeviceHandler> deviceHandlers = new HashMap<DeviceHandlerType, DeviceHandler>();


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
        this.deviceHandlers.put(handler.getDeviceHandlerKey(), handler);
    }


    public DeviceHandler getDeviceHandler(DeviceHandlerType deviceHandlerType)
    {
        if (this.deviceHandlers.containsKey(deviceHandlerType))
        {
            return this.deviceHandlers.get(deviceHandlerType);
        }
        else
        {
            LOG.error("Handler " + deviceHandlerType.name() + " could not be found.");
            return null;
        }
    }

}
