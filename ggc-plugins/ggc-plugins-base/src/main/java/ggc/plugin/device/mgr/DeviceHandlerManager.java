package ggc.plugin.device.mgr;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.v2.DeviceHandler;

/**
 * Created by andy on 10.02.15.
 */
public class DeviceHandlerManager
{

    private static DeviceHandlerManager deviceHandlerManager = null;
    private static final Logger LOG = LoggerFactory.getLogger(DeviceHandlerManager.class);

    HashMap<DeviceHandlerType, DeviceHandler> deviceHandlers = new HashMap<DeviceHandlerType, DeviceHandler>();


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
        if ((deviceHandlerType == DeviceHandlerType.NoHandler)
                || (deviceHandlerType == DeviceHandlerType.NoSupportInDevice))
            return null;

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
