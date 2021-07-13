package ggc.plugin.device;

import ggc.core.plugins.GGCPluginType;

/**
 * Created by andy on 31.12.16.
 */
public interface DeviceHandlerStartup
{

    GGCPluginType getPluginType();


    void initDeviceHandler();

}
