package main.java.ggc.pump.device;

import org.apache.commons.lang.builder.ToStringBuilder;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import main.java.ggc.pump.defs.device.PumpDeviceDefinition;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 10.02.15.
 */
public class PumpDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements PumpInterfaceV2
{

    PumpDeviceDefinition pumpDeviceDefinition;


    public PumpDeviceInstanceWithHandler(DeviceDefinition deviceDefinition)
    {
        super(deviceDefinition, DataAccessPump.getInstance());
        this.pumpDeviceDefinition = (PumpDeviceDefinition) deviceDefinition;
    }


    public int getMaxMemoryRecords()
    {
        return 0;
    }


    public void loadPumpSpecificValues()
    {

    }


    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }


    public String getTemporaryBasalTypeDefinition()
    {
        return null;
    }


    public float getBolusStep()
    {
        return 0;
    }


    public float getBasalStep()
    {
        return 0;
    }


    public boolean arePumpSettingsSet()
    {
        return false;
    }


    public int howManyMonthsOfDataStored()
    {
        return -1;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("pumpDeviceDefinition", pumpDeviceDefinition).toString();
    }
}
