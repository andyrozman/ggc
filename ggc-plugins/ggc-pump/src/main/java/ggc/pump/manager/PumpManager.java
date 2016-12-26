package ggc.pump.manager;

import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.manager.DeviceManager;
import ggc.pump.defs.device.PumpDeviceDefinition;
import ggc.pump.device.PumpDeviceInstanceWithHandler;
import ggc.pump.util.DataAccessPump;

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

    /**
     * Singleton instance
     */
    public static PumpManager s_manager = null;


    /**
     * Constructor
     */
    private PumpManager(DataAccessPump da)
    {
        super(da);
    }


    @Override
    protected void loadDeviceInstancesV2()
    {
        for (DeviceDefinition dd : PumpDeviceDefinition.getAllDevices())
        {
            PumpDeviceDefinition pdd = (PumpDeviceDefinition) dd;

            PumpDeviceInstanceWithHandler di = new PumpDeviceInstanceWithHandler(pdd);

            this.supportedDevicesV2.put(di.getCompany().getName() + "_" + di.getName(), di);
            this.supportedDevicesForSelector.add(di);
        }
    }


    /**
     * Get PumpManager instance
     * 
     * @return PumpManager instance
     */
    public static PumpManager getInstance(DataAccessPump da)
    {
        if (PumpManager.s_manager == null)
        {
            PumpManager.s_manager = new PumpManager(da);
        }

        return PumpManager.s_manager;
    }


    /**
     * Get PumpManager instance
     *
     * @return PumpManager instance
     */
    public static PumpManager getInstance()
    {
        return PumpManager.s_manager;
    }


    /**
     * Load devices companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        // addDeviceCompany(new Animas()); v2
        // addDeviceCompany(new Deltec(dataAccessPlugInBase));
        // addDeviceCompany(new Insulet()); v2
        // addDeviceCompany(new Minimed());
        // addDeviceCompany(new Roche(dataAccessPlugInBase));
        // addDeviceCompany(new Sooil(dataAccessPlugInBase));
    }

}
