package ggc.pump.manager; 

import ggc.plugin.manager.DeviceManager;
import ggc.pump.manager.company.Animas;
import ggc.pump.manager.company.Deltec;
import ggc.pump.manager.company.Insulet;
import ggc.pump.manager.company.Minimed;
import ggc.pump.manager.company.Roche;
import ggc.pump.manager.company.Sooil;

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
     * Pump Company: Minimed
     */
    public static final int PUMP_COMPANY_MINIMED              = 1;
    

    /**
     * Pump Company: Roche
     */
    public static final int PUMP_COMPANY_ROCHE                 = 2;

    /**
     * Pump Company: Disetronic
     */
    public static final int PUMP_COMPANY_DISETRONIC            = 3;
    

    /**
     * Pump Company: Animas
     */
    public static final int PUMP_COMPANY_ANIMAS                = 4;
    
    
    
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

    
    /**
     * Get PumpManager instance
     * 
     * @return PumpManager instance
     */
    public static PumpManager getInstance()
    {
    	if (PumpManager.s_manager==null)
    		PumpManager.s_manager = new PumpManager();
    	
    	return PumpManager.s_manager;
    }

    
    /**
     * Load devices companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        addDeviceCompany(new Animas());
        addDeviceCompany(new Deltec());
        addDeviceCompany(new Insulet());
        addDeviceCompany(new Minimed());
        addDeviceCompany(new Roche());
        addDeviceCompany(new Sooil());
    }
    
    
    /**
     * Load Supported Devices
     */
    @Override
    public void loadSupportedDevices()
    {
        // TODO add supported devices
        //this.supported_devices.addAll(new AscensiaBayer().getDevices());
    }


}
