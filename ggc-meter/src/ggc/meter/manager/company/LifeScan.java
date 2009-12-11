package ggc.meter.manager.company; 

import ggc.meter.device.onetouch.OneTouchProfile;
import ggc.meter.device.onetouch.OneTouchSelect;
import ggc.meter.device.onetouch.OneTouchUltra;
import ggc.meter.device.onetouch.OneTouchUltraEasy;
import ggc.meter.device.onetouch.OneTouchUltraMini;
import ggc.meter.device.onetouch.OneTouchUltraSmart;
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
        super();
        
        this.addDevice(new OneTouchUltra(this));
        this.addDevice(new OneTouchProfile(this));
        this.addDevice(new OneTouchUltraEasy(this));
        this.addDevice(new OneTouchUltraMini(this));
        this.addDevice(new OneTouchUltraSmart(this));
        this.addDevice(new OneTouchSelect(this));
        
        //this.addDevice(new OneTouchUltra2(this));
    }



    
    
    
    //********************************************************
    //***      Meter Company Identification Methods        ***
    //********************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "LifeScan";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 3;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "LIFESCAN_DESC"; 
    }
    
    
    
    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_PLANNED;
    }
    

    /**
     * Get Connection Sample - This will display sample of connection parameter, for example on Serial
     * devices this is COMx. On other OSes there are of course other parameters, but COMx parameter is
     * "known" serial parameter and will be known to all people.
     * 
     * @return
     */
    public String getConnectionSample()
    {
        return "COM9";
    }
    
    
    /**
     * Get Connection Samples - This is I18N keyword, which needs to be added to language file. For Serial 
     * devices this is SERIAL_PORTS.
     * 
     * @return
     */
    public String getConnectionSamples()
    {
        return "SERIAL_PORTS";  //"Serial Ports: COM2,...";
    }
    
    
    
}
