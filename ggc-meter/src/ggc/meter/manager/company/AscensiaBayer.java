package ggc.meter.manager.company; 

import ggc.meter.device.ascensia.AscensiaBreeze;
import ggc.meter.device.ascensia.AscensiaBreeze2;
import ggc.meter.device.ascensia.AscensiaContour;
import ggc.meter.device.ascensia.AscensiaContourLink;
import ggc.meter.device.ascensia.AscensiaContourTest;
import ggc.meter.device.ascensia.AscensiaDEX;
import ggc.meter.device.ascensia.AscensiaEliteXL;
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
        super();
        
        this.addDevice(new AscensiaEliteXL(this));
        this.addDevice(new AscensiaDEX(this));
        this.addDevice(new AscensiaBreeze(this));
        this.addDevice(new AscensiaBreeze2(this));
        this.addDevice(new AscensiaContour(this));
        this.addDevice(new AscensiaContourLink(this));
        this.addDevice(new AscensiaContourTest(this));
        
        //System.out.println(this.devices_vector);
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
        return "Ascensia/Bayer";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 1;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "ASCENSIA_DESC"; 
    }
    
    
    
    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_DONE;
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
