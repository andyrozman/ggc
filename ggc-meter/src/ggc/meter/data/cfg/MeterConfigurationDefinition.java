package ggc.meter.data.cfg;

import ggc.meter.device.DummyMeter;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfigurationDefinition;

import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

public class MeterConfigurationDefinition implements DeviceConfigurationDefinition
{
    
    public String getDevicePrefix()
    {
        return "METER";
    }
    
    public boolean doesDeviceSupportTimeFix()
    {
        return true;
    }

    public String getDevicesConfigurationFile()
    {
        return "../data/tools/MeterConfiguration.properties";
    }

    public Object getDummyObject()
    {
        return new DummyMeter();
    }

    public Vector<? extends SelectableInterface> getSupportedDevices()
    {
        return MeterManager.getInstance().getSupportedDevices();
    }
    
    
    
    

}
