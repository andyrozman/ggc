package ggc.cgms.manager; 

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
     * CGMS Company: Minimed
     */
    public static final int CGMS_COMPANY_MINIMED              = 1;
    

    /**
     * CGMS Company: Roche
     */
    public static final int CGMS_COMPANY_ABBOTT               = 2;

    
    /**
     * CGMS Company: Disetronic
     */
    public static final int CGMS_COMPANY_DEXCOM               = 3;
    

    
    
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
        // TODO: Added supported devices
    }
    
    
    /** 
     * Load Device Companies
     */
    @Override
    public void loadDeviceCompanies()
    {
        // TODO: Load devices companies
    }
    

}
