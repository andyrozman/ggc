package ggc.meter.defs.device;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandlerAbstract;

/**
 * Created by andy on 22.10.15.
 */
public abstract class MeterDeviceHandler extends DeviceHandlerAbstract
{

    protected DataAccessMeter dataAccessMeter;


    public MeterDeviceHandler()
    {
        dataAccessMeter = DataAccessMeter.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    protected MeterDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (MeterDeviceDefinition) definition;
    }


    /**
     * Check if DataAccess Set, if not set it.
     */
    protected void checkIfDataAccessSet()
    {
        if (dataAccessMeter == null)
            dataAccessMeter = DataAccessMeter.getInstance();
    }

}
