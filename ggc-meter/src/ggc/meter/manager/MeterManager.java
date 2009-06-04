package ggc.meter.manager; 

import ggc.meter.manager.company.Abbott;
import ggc.meter.manager.company.AscensiaBayer;
import ggc.meter.manager.company.DiabeticSupplyOfSunCoast;
import ggc.meter.manager.company.HipoGuard;
import ggc.meter.manager.company.HomeDiagnostic;
import ggc.meter.manager.company.LifeScan;
import ggc.meter.manager.company.Menarini;
import ggc.meter.manager.company.Prodigy;
import ggc.meter.manager.company.Roche;
import ggc.meter.manager.company.Sanvita;
import ggc.meter.manager.company.USDiagnostic;
import ggc.meter.manager.company.Wavesense;
import ggc.plugin.manager.DeviceManager;

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
     * Meter Company: Ascensia
     */
    public static final int METER_COMPANY_ASCENSIA            = 1;
    
    
    /**
     * Meter Company: Roche
     */
    public static final int METER_COMPANY_ROCHE               = 2;
    
    
    /**
     * 
     */
    public static final int METER_COMPANY_LIFESCAN            = 3;
    
    
    /**
     * 
     */
    public static final int METER_COMPANY_ABBOTT              = 4;
    
    
    /**
     * Singleton instance
     */
    public static MeterManager s_manager = null;
    

    /**
     * Constructor
     */
    private MeterManager()
    {
        super();
    }

    
    /**
     * Get MeterManager instance
     * 
     * @return
     */
    public static MeterManager getInstance()
    {
    	if (MeterManager.s_manager==null)
    		MeterManager.s_manager = new MeterManager();
    	
    	return MeterManager.s_manager;
    }


    /**
     * Load devices companies
     */
    public void loadDeviceCompanies()
    {
        addDeviceCompany(new AscensiaBayer());
        addDeviceCompany(new Roche());
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
    
    
    
    /**
     * Load Supported Devices
     */
    public void loadSupportedDevices()
    {
        this.supported_devices.addAll(new AscensiaBayer().getDevices());
        this.supported_devices.addAll(new Roche().getDevices());
        this.supported_devices.addAll(new LifeScan().getDevices());
        this.supported_devices.addAll(new Abbott().getDevices());
    }
    

}
