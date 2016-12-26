package ggc.meter.manager;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.MeterDeviceInstanceWithHandler;
import ggc.meter.manager.company.*;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.manager.DeviceManager;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     MeterManager  
 *  Description:  Meter device manager 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterManager extends DeviceManager
{

    /**
     * Singleton instance
     */
    public static MeterManager s_manager = null;


    /**
     * Constructor
     */
    private MeterManager(DataAccessPlugInBase da)
    {
        super(da);
    }


    @Override
    protected void loadDeviceInstancesV2()
    {
        for (DeviceDefinition dd : MeterDeviceDefinition.getAllDevices())
        {
            MeterDeviceDefinition pdd = (MeterDeviceDefinition) dd;

            MeterDeviceInstanceWithHandler di = new MeterDeviceInstanceWithHandler(pdd);

            this.supportedDevicesV2.put(di.getCompany().getName() + "_" + di.getName(), di);
            this.supportedDevicesForSelector.add(di);
        }
    }


    /**
     * Get MeterManager instance
     * 
     * @return
     */
    public static MeterManager getInstance(DataAccessPlugInBase da)
    {
        if (MeterManager.s_manager == null)
        {
            MeterManager.s_manager = new MeterManager(da);
        }

        return MeterManager.s_manager;
    }


    public static MeterManager getInstance()
    {

        return MeterManager.s_manager;
    }


    /**
     * Load devices companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        addDeviceCompany(new AscensiaBayer());
        // addDeviceCompany(new Roche());
        addDeviceCompany(new LifeScan());
        addDeviceCompany(new Abbott());
        addDeviceCompany(new Menarini());
        addDeviceCompany(new DiabeticSupplyOfSunCoast());
        addDeviceCompany(new HipoGuard());
        addDeviceCompany(new HomeDiagnostic());
        addDeviceCompany(new Prodigy());
        addDeviceCompany(new Sanvita());
        addDeviceCompany(new USDiagnostic());
        addDeviceCompany(new Wavesense());
    }

}
