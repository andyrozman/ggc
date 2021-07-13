package ggc.meter.data.cfg;

import ggc.meter.device.DummyMeter;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfigurationDefinitionAbstract;

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
 *  Filename:      MeterConfigurationDefinition  
 *  Description:   Definition for Meter configurator
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterConfigurationDefinition extends DeviceConfigurationDefinitionAbstract
{

    public MeterConfigurationDefinition()
    {
        super(true, // timeFix
                MeterManager.getInstance(), // deviceManager
                "METER", // DevicePrefix
                "MeterConfiguration.properties", // ConfigurationFilename
                "MeterTool_" // Help Prefix
        );
    }

    /**
     * Returns Dummy object (needed for some actions)
     *
     * @return
     */
    public Object getDummyObject()
    {
        return new DummyMeter();
    }

    @Override
    public void initDeviceManager()
    {
        this.deviceManager = MeterManager.getInstance();
    }


}
