package ggc.pump.data.cfg;

import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.pump.device.DummyPump;
import ggc.pump.manager.PumpManager;

import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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


public class PumpConfigurationDefinition implements DeviceConfigurationDefinition
{
    
    /**
     * Keyword used through configuration and configuration file describing device (for pump plugin, this
     * would be word PUMP).
     *  
     * @return keyword representing device
     */
    public String getDevicePrefix()
    {
        return "PUMP";
    }
    
    
    /**
     * Only certain devices support manual time fix for application (meters do, other's don't).
     * 
     * @return true if time fix is supported, false otherwise
     */
    public boolean doesDeviceSupportTimeFix()
    {
        return false;
    }

    
    /**
     * Get path to Configuration file as string
     * 
     * @return path to configuration file
     */
    public String getDevicesConfigurationFile()
    {
        return "../data/tools/PumpConfiguration.properties";
    }

    
    /**
     * Returns Dummy object (needed for some actions)
     * 
     * @return
     */
    public Object getDummyObject()
    {
        return new DummyPump();
    }

    
    /**
     * Returns list of all supported devices for plugin. Needed for device selection
     * 
     * @return
     */
    public Vector<? extends SelectableInterface> getSupportedDevices()
    {
        return PumpManager.getInstance().getSupportedDevices();
    }
    
    
    /**
     * Returns prefix for help context
     * 
     * @return help context prefix
     */
    public String getHelpPrefix()
    {
        return "PumpTool_";
    }
    
    

}
