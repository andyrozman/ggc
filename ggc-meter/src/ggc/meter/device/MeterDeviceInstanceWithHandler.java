package ggc.meter.device;

import ggc.meter.data.defs.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;

/**
 * Created by andy on 10.02.15.
 */
public class MeterDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements MeterInterfaceV2
{

    MeterDeviceDefinition meterDeviceDefinition;


    public MeterDeviceInstanceWithHandler(DeviceDefinition deviceDefinition)
    {
        super(deviceDefinition, DataAccessMeter.getInstance());
        meterDeviceDefinition = (MeterDeviceDefinition) deviceDefinition;
    }


    public int getMaxMemoryRecords()
    {
        return 0;
    }


    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }


    public MeterDisplayInterfaceType getInterfaceTypeForMeter()
    {
        return meterDeviceDefinition.getMeterDisplayInterfaceType();
    }


    public int howManyMonthsOfDataStored()
    {
        return -1;
    }
}
