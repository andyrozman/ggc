package ggc.pump.manager.company;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.pump.device.insulet.InsuletOmniPod;
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
 *  Filename:      Insulet  
 *  Description:   Pump Company - Insulet
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Insulet extends AbstractPumpDeviceCompany
{

    /**
     * Constructor
     */
    public Insulet()
    {
        super(PumpDevicesIds.COMPANY_INSULET, // company_id
                "Insulet", // company name (full)
                "Insulet", // short company name
                "INSULET_DESC", // company description
                DeviceImplementationStatus.NotAvailable); // implementation
                                                                          // status

        this.addDevice(new InsuletOmniPod(this));
    }

    /**
     * Init Profile Names (for Profile Editor)
     */
    @Override
    public void initProfileNames()
    {
    }

}
