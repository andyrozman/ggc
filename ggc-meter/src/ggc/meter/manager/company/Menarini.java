package ggc.meter.manager.company;

import ggc.meter.device.menarini.GlucofixMio;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.util.DataAccessMeter;
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
 *  Filename:      Menarini  
 *  Description:   Meter Company - Menarini
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Menarini extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public Menarini()
    {
        super(false, // empty devices
                MeterDevicesIds.COMPANY_MENARINI, // company_id
                "Menarini", // company name (full)
                "Menarini", // short company name
                "MENARINI_DESC", // company description
                DeviceImplementationStatus.Testing, DataAccessMeter.getInstance()); // implementation
                                                                                                   // status

        this.addDevice(new GlucofixMio(this));
    }

}
