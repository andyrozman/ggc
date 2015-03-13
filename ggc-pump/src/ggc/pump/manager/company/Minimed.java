package ggc.pump.manager.company;

import ggc.plugin.manager.DeviceImplementationStatus;
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
        super(PumpDevicesIds.COMPANY_MINIMED, // company_id
                "Minimed", // company name (full)
                "Minimed", // short company name
                "MINIMED_DESC", // company description
                DeviceImplementationStatus.NotAvailable); // implementation
                                                                          // status
        /*
         * this.addDevice(new Minimed508(this));
         * this.addDevice(new Minimed512(this));
         * // this.addDevice(new Minimed522(this));
         * this.addDevice(new Minimed554_Veo(this));
         */
    }

    /**
     * Init Profile Names (for Profile Editor)
     */
    @Override
    public void initProfileNames()
    {
        profile_names = new String[3];
        profile_names[0] = "Standard";
        profile_names[1] = "Pattern A";
        profile_names[2] = "Pattern B";
    }

}
