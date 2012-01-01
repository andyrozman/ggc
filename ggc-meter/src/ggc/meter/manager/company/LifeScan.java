package ggc.meter.manager.company; 

import ggc.meter.device.onetouch.OneTouchProfile;
import ggc.meter.device.onetouch.OneTouchSelect;
import ggc.meter.device.onetouch.OneTouchUltra;
import ggc.meter.device.onetouch.OneTouchUltra2;
import ggc.meter.device.onetouch.OneTouchUltraEasy;
import ggc.meter.device.onetouch.OneTouchUltraMini;
import ggc.meter.device.onetouch.OneTouchUltraSmart;
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
 *  Filename:      LifeScan  
 *  Description:   Meter Company - LifeScan
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class LifeScan extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public LifeScan()
    {
        super(false,                            // empty devices
            MeterDevicesIds.COMPANY_LIFESCAN,   // company_id
            "LifeScan/One Touch/J&J",           // company name (full)
            "LifeScan",                         // short company name
            "LIFESCAN_DESC",                    // company description
            DeviceImplementationStatus.IMPLEMENTATION_PARTITIAL,
            DataAccessMeter.getInstance());  // implementation status
        
        this.addDevice(new OneTouchUltra(this));
        this.addDevice(new OneTouchProfile(this));
        this.addDevice(new OneTouchUltraEasy(this));
        this.addDevice(new OneTouchUltraMini(this));
        this.addDevice(new OneTouchUltraSmart(this));
        this.addDevice(new OneTouchSelect(this));
        
        
        this.addDevice(new OneTouchUltra2(this));
    }
    
    
}
