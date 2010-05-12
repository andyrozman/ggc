package ggc.cgms.manager; 

import ggc.cgms.manager.company.Abbott;
import ggc.cgms.manager.company.Dexcom;
import ggc.cgms.manager.company.Minimed;
import ggc.plugin.manager.DeviceManager;


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
 *  Filename:     CGMManager  
 *  Description:  CGMS device manager 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class CGMSManager extends DeviceManager
{
    
    
    /**
     * Singelton instance
     */
    public static CGMSManager s_manager = null;
    

    /**
     * Constructor
     */
    private CGMSManager()
    {
        super();
    }

    
    /**
     * Return singelton instance
     * @return
     */
    public static CGMSManager getInstance()
    {
    	if (CGMSManager.s_manager==null)
    	    CGMSManager.s_manager = new CGMSManager();
    	
    	return CGMSManager.s_manager;
    }


    /** 
     * Load Supported Devices
     */
    public void loadSupportedDevices()
    {
        addDeviceCompany(new Abbott());
        addDeviceCompany(new Dexcom());
        addDeviceCompany(new Minimed());
        
        //System.out.println("!!! CGMS Companies: " + this.companies.size());
    }
    
    
    /** 
     * Load Device Companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        this.supported_devices.addAll(new Abbott().getDevices());
        this.supported_devices.addAll(new Dexcom().getDevices());
        this.supported_devices.addAll(new Minimed().getDevices());

        //System.out.println("!!! CGMS Devices: " + this.supported_devices.size());
    }
    

}
