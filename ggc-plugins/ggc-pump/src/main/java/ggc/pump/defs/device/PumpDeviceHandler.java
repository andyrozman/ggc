package ggc.pump.defs.device;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandlerAbstract;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 22.10.15.
 */
public abstract class PumpDeviceHandler extends DeviceHandlerAbstract
{

    protected DataAccessPump dataAccessPump;


    public PumpDeviceHandler()
    {
        dataAccessPump = DataAccessPump.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    protected PumpDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (PumpDeviceDefinition) definition;
    }


    /**
     * Check if DataAccess Set, if not set it.
     */
    protected void checkIfDataAccessSet()
    {
        if (dataAccessPump == null)
            dataAccessPump = DataAccessPump.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    public GGCPluginType getGGCPluginType()
    {
        return GGCPluginType.PumpToolPlugin;
    }

}
