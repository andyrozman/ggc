package ggc.pump.data.cfg;

import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.pump.device.DummyPump;
import ggc.pump.manager.PumpManager;

import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

public class PumpConfigurationDefinition implements DeviceConfigurationDefinition
{
    
    public String getDevicePrefix()
    {
        return "PUMP";
    }
    
    public boolean doesDeviceSupportTimeFix()
    {
        return true;
    }

    public String getDevicesConfigurationFile()
    {
        return "../data/tools/PumpConfiguration.properties";
    }

    public Object getDummyObject()
    {
        return new DummyPump();
    }

    public Vector<? extends SelectableInterface> getSupportedDevices()
    {
        return PumpManager.getInstance().getSupportedDevices();
    }
    
    
    
    

}
