package ggc.cgms.data.cfg;

import ggc.cgms.device.DummyCGMS;
import ggc.cgms.manager.CGMSManager;
import ggc.plugin.cfg.DeviceConfigurationDefinition;

import java.util.List;
import java.util.Vector;

import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSConfigurationDefinition
 *  Description:  Definition for CGMS configurator
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSConfigurationDefinition implements DeviceConfigurationDefinition
{

    /**
     * Keyword used through configuration and configuration file describing device (for meter plugin, this
     * would be word METER).
     *  
     * @return keyword representing device
     */
    public String getDevicePrefix()
    {
        return "CGMS";
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
        return "../data/tools/CGMSConfiguration.properties";
    }

    /**
     * Returns Dummy object (needed for some actions)
     * 
     * @return
     */
    public Object getDummyObject()
    {
        return new DummyCGMS();
    }

    /**
     * Returns list of all supported devices for plugin. Needed for device selection
     * 
     * @return
     */
    public List<SelectableInterface> getSupportedDevices()
    {
        return CGMSManager.getInstance().getSupportedDevicesForSelector();
    }

    /**
     * Returns prefix for help context
     * 
     * @return help context prefix
     */
    public String getHelpPrefix()
    {
        return "CGMSTool_";
    }

    public Object getSpecificDeviceInstance(String company, String deviceName)
    {
        Object device = CGMSManager.getInstance().getDeviceV2(company, deviceName);

        if (device==null)
        {
            device = CGMSManager.getInstance().getDeviceV1(company, deviceName);
        }

        return device;
    }
}