package ggc.meter.defs.device;

import ggc.meter.data.defs.MeterDeviceDefinition;
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

}
