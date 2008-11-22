/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MeterManager extends DeviceManager
{
    
    /**
     * Single instance
     */
    public static MeterManager s_manager = null;
    

    /**
     * Constructor
     */
    private MeterManager()
    {
        super();
    	//this.loadMeterCompanies();
    	//this.loadSupportedDevices();
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
    }
    
    

/*    
    public DeviceInterface getMeterDevice(String group, String device)
    {
        AbstractDeviceCompany cmp = getCompany(group);
        
        if (cmp==null)
        {
            System.out.println("Company not found !");
            System.out.println("companies_nt: " + this.companies_ht);
            return null;
        }
        
        return cmp.getDevice(device);
    }
    
    
    public String getMeterDeviceClassName(String group, String device)
    {
        DeviceInterface mi = getMeterDevice(group, device);
        return mi.getDeviceClassName();
    }
  */  
    

}
