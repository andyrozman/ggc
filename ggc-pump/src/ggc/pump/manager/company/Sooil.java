package ggc.pump.manager.company; 

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.pump.device.dana.DanaDiabecare_II;
import ggc.pump.device.dana.DanaDiabecare_III_R;
import ggc.pump.device.dana.DanaDiabecare_IIS;
import ggc.pump.manager.PumpDevicesIds;

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
 *  Filename:      Sooil  
 *  Description:   Pump Company - Sooil
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Sooil extends AbstractPumpDeviceCompany
{

    
    /**
     * Constructor
     */
    public Sooil()
    {
        super(PumpDevicesIds.COMPANY_SOOIL,     // company_id
            "Sooil (Dana)",                     // company name (full)
            "Dana",                             // short company name
            "SOOIL_DESC",                       // company description
            DeviceImplementationStatus.IMPLEMENTATION_DONE);  // implementation status
        
        this.addDevice(new DanaDiabecare_II(this));
        this.addDevice(new DanaDiabecare_IIS(this));
        this.addDevice(new DanaDiabecare_III_R(this));
    }

    
    /**
     * Init Profile Names (for Profile Editor)
     */
    public void initProfileNames()
    {
        profile_names = new String[16];
        
        for(int i=0; i<16; i++)
        {
            profile_names[i] = "" + (i+1);
        }
    }
    
    
}
