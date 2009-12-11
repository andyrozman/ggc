package ggc.pump.manager.company; 

import ggc.pump.device.minimed.Minimed508;
import ggc.pump.device.minimed.Minimed512;
import ggc.pump.device.minimed.Minimed522;
import ggc.pump.device.minimed.Minimed554_Veo;
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
 *  Filename:      Minimed  
 *  Description:   Pump Company - Minimed
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class Minimed extends AbstractPumpDeviceCompany
{

    /**
     * Constructor
     */
    public Minimed()
    {
        super(false);
        
        profile_names = new String[3];
        profile_names[0] = "Standard";
        profile_names[1] = "Pattern A";
        profile_names[2] = "Pattern B";
        
        //this.addDevice(new GenericPumpDevice(this));
        this.addDevice(new Minimed508(this));
        this.addDevice(new Minimed512(this));
        this.addDevice(new Minimed522(this));
        this.addDevice(new Minimed554_Veo(this));

    }



    /**
     * getName - Get Name of pump company. 
     * 
     * @return name of pump company
     */
    public String getName()
    {
        return "Minimed";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return PumpDevicesIds.COMPANY_MINIMED;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "MINIMED_DESC"; 
    }
    
    
    
    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return 0;
    }
    
    
    
}
