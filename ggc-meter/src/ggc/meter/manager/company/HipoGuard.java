package ggc.meter.manager.company;

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
 *  Filename:      HipoGuard
 *  Description:   Meter Company - HipoGuard
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class HipoGuard extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public HipoGuard()
    {
        super(true, // empty devices
                MeterDevicesIds.COMPANY_HIPOGUARD, // company_id
                "HipoGuard", // company name (full)
                "HipoGuard", // short company name
                "HIPOGUARD_DESC", // company description
                DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED, DataAccessMeter.getInstance()); // implementation
                                                                                                       // status
    }

}
