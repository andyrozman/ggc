package ggc.plugin.cfg;

import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

public interface DeviceConfigurationDefinition
{
    
    public String getDevicePrefix();
    
    public boolean doesDeviceSupportTimeFix();
    
    public String getDevicesConfigurationFile();
    
    public Object getDummyObject();
    
    public Vector<? extends SelectableInterface> getSupportedDevices(); 

}
