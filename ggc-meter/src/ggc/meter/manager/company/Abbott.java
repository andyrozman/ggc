package ggc.meter.manager.company; 

import ggc.meter.device.abbott.Freestyle;
import ggc.meter.device.abbott.OptiumXceed;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.util.I18nControl;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;

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
 *  Filename:      Abbott  
 *  Description:   Meter Company - Abbott
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class Abbott extends AbstractDeviceCompany
{
    
    /**
     * Constructor
     */
    public Abbott()
    {
        super(I18nControl.getInstance());
        
        addDevice(new Freestyle(this));
        addDevice(new OptiumXceed(this));
    }

    
    /**
     * getName - Get Name of meter company. 
     * 
     * @return name of pump company
     */
    public String getName()
    {
        return "Abbott";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return MeterDevicesIds.COMPANY_ABBOTT;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "ABBOTT_DESC"; 
    }
    
    
    
    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED;
    }
    
    
    
}
