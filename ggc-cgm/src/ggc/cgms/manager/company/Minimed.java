package ggc.cgms.manager.company; 

import ggc.cgms.device.minimed.GuardianRealTime;
import ggc.cgms.device.minimed.MiniMedCGMSGold;
import ggc.cgms.device.minimed.MiniMedRealTime;
import ggc.cgms.manager.CGMSDevicesIds;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;

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
 *  Filename:      MiniMed  
 *  Description:   CGMS Company - MiniMed
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class Minimed extends AbstractDeviceCompany
{

    
    /**
     * Constructor
     */
    public Minimed()
    {
        super(false,                            // empty devices
            CGMSDevicesIds.COMPANY_MINIMED,     // company_id
            "MiniMed",                          // company name (full)
            "MiniMed",                          // short company name
            "MINIMED_DESC",                     // company description
            DeviceImplementationStatus.IMPLEMENTATION_PLANNED,
            DataAccessCGMS.getInstance());  // implementation status
        
        this.addDevice(new MiniMedCGMSGold(this));
        this.addDevice(new GuardianRealTime(this));
        this.addDevice(new MiniMedRealTime(this));
    }
    
}
