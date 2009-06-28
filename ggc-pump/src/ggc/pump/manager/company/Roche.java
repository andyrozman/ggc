package ggc.pump.manager.company; 

import ggc.pump.device.accuchek.AccuChekSpirit;
import ggc.pump.manager.PumpManager;
import ggc.pump.util.I18nControl;

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
 *  Filename:      Roche  
 *  Description:   Pump Company - Roche
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class Roche extends AbstractPumpDeviceCompany
{

    /**
     * Constructor
     */
    public Roche()
    {
        super(I18nControl.getInstance(), false);
        profile_names = new String[5];
        
        for(int i=0; i<5; i++)
        {
            profile_names[i] = "" + (i+1);
        }
        
        this.addDevice(new GenericPumpDevice(this));
        this.addDevice(new AccuChekSpirit(this));
    }



    
    
    
    //********************************************************
    //***      Meter Company Identification Methods        ***
    //********************************************************


    /**
     * getName - Get Name of pump company. 
     * 
     * @return name of pump company
     */
    public String getName()
    {
        return "Accu-Chek (Roche)";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return PumpManager.PUMP_COMPANY_ROCHE;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "ROCHE_DESC"; 
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
