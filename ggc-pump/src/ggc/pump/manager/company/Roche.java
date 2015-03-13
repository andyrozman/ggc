package ggc.pump.manager.company;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.pump.device.accuchek.AccuChekCombo;
import ggc.pump.device.accuchek.AccuChekDTron;
import ggc.pump.device.accuchek.AccuChekSpirit;
import ggc.pump.device.accuchek.DisetronicDTron;
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
        super(PumpDevicesIds.COMPANY_ROCHE, // company_id
                "Accu-Chek/Roche", // company name (full)
                "Roche", // short company name
                "ROCHE_DESC", // company description
                DeviceImplementationStatus.Testing); // implementation
                                                                    // status

        this.addDevice(new DisetronicDTron(this));
        this.addDevice(new AccuChekDTron(this));
        this.addDevice(new AccuChekSpirit(this));
        this.addDevice(new AccuChekCombo(this));
    }

    /**
     * Init Profile Names (for Profile Editor)
     */
    @Override
    public void initProfileNames()
    {
        profile_names = new String[5];

        for (int i = 0; i < 5; i++)
        {
            profile_names[i] = "" + (i + 1);
        }
    }

}
