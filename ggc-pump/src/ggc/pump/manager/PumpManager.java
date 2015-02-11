package ggc.pump.manager;

import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.DeviceManager;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.pump.data.defs.PumpDeviceDefinition;
import ggc.pump.device.PumpDeviceInstanceWithHandler;
import ggc.pump.manager.company.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     PumpManager  
 *  Description:  Pump device manager 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpManager extends DeviceManager
{
    private static Log LOG = LogFactory.getLog(PumpManager.class);

    /**
     * Singleton instance
     */
    public static PumpManager s_manager = null;

    /**
     * Constructor
     */
    private PumpManager()
    {
        super();
    }

    @Override
    protected void loadDeviceInstancesV2()
    {
        for(DeviceDefinition dd : PumpDeviceDefinition.getSupportedDevices())
        {
            PumpDeviceDefinition pdd = (PumpDeviceDefinition)dd;

            PumpDeviceInstanceWithHandler di = new PumpDeviceInstanceWithHandler(pdd);

            this.supportedDevicesV2.put(di.getCompany().getName() + "_"+ di.getName(), di);
        }
    }


    /**
     * Get PumpManager instance
     * 
     * @return PumpManager instance
     */
    public static PumpManager getInstance()
    {
        if (PumpManager.s_manager == null)
        {
            PumpManager.s_manager = new PumpManager();
        }

        return PumpManager.s_manager;
    }

    /**
     * Load devices companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        //addDeviceCompany(new Animas());
        addDeviceCompany(new Deltec());
        addDeviceCompany(new Insulet());
        addDeviceCompany(new Minimed());
        addDeviceCompany(new Roche());
        addDeviceCompany(new Sooil());
    }

}
