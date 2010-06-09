package ggc.meter.manager.company; 

import ggc.meter.device.accuchek.AccuChekActive;
import ggc.meter.device.accuchek.AccuChekAdvantage;
import ggc.meter.device.accuchek.AccuChekAviva;
import ggc.meter.device.accuchek.AccuChekComfort;
import ggc.meter.device.accuchek.AccuChekCompact;
import ggc.meter.device.accuchek.AccuChekCompactPlus;
import ggc.meter.device.accuchek.AccuChekGo;
import ggc.meter.device.accuchek.AccuChekIntegra;
import ggc.meter.device.accuchek.AccuChekNano;
import ggc.meter.device.accuchek.AccuChekPerforma;
import ggc.meter.device.accuchek.AccuChekSensor;
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
 *  Filename:      Roche 
 *  Description:   Meter Company - Roche
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class Roche extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public Roche()
    {
        super(false,                            // empty devices
            MeterDevicesIds.COMPANY_ROCHE,      // company_id
            "Accu-Chek/Roche",                  // company name (full)
            "Roche",                            // short company name
            "ROCHE_DESC",                       // company description
            DeviceImplementationStatus.IMPLEMENTATION_DONE,
            DataAccessMeter.getInstance());  // implementation status
        
        //this.addDevice(new AccuChekSmartPix());
        this.addDevice(new AccuChekActive(this));
        this.addDevice(new AccuChekAdvantage(this));
        this.addDevice(new AccuChekAviva(this));
        this.addDevice(new AccuChekComfort(this));
        this.addDevice(new AccuChekCompact(this));
        this.addDevice(new AccuChekCompactPlus(this));
        this.addDevice(new AccuChekGo(this));
        this.addDevice(new AccuChekIntegra(this));
        this.addDevice(new AccuChekPerforma(this));
        this.addDevice(new AccuChekSensor(this));
        this.addDevice(new AccuChekNano(this));
    }
    
    
}
