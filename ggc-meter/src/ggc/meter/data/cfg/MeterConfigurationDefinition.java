package ggc.meter.data.cfg;

import ggc.meter.device.DummyMeter;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfigurationDefinition;

import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


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
