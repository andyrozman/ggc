package ggc.meter.manager.company;

import ggc.meter.device.ascensia.AscensiaBreeze;
import ggc.meter.device.ascensia.AscensiaBreeze2;
import ggc.meter.device.ascensia.AscensiaContour;
import ggc.meter.device.ascensia.AscensiaContourLink;
import ggc.meter.device.ascensia.AscensiaContourTest;
import ggc.meter.device.ascensia.AscensiaDEX;
import ggc.meter.device.ascensia.AscensiaEliteXL;
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
 *  Filename:      AscensiaBayer  
 *  Description:   Meter Company - Ascensia/Bayer
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class AscensiaBayer extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public AscensiaBayer()
    {
        super(false, // empty devices
                MeterDevicesIds.COMPANY_ASCENSIA, // company_id
                "Ascensia/Bayer", // company name (full)
                "Ascensia", // short company name
                "ASCENSIA_DESC", // company description
                DeviceImplementationStatus.IMPLEMENTATION_DONE, DataAccessMeter.getInstance()); // implementation
                                                                                                // status

        this.addDevice(new AscensiaEliteXL(this));
        this.addDevice(new AscensiaDEX(this));
        this.addDevice(new AscensiaBreeze(this));
        this.addDevice(new AscensiaBreeze2(this));
        this.addDevice(new AscensiaContour(this));
        this.addDevice(new AscensiaContourLink(this));
        this.addDevice(new AscensiaContourTest(this));
    }

}
