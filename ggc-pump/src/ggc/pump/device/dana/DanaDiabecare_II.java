package ggc.pump.device.dana;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     DanaDiabecare_II  
 *  Description:  Dana Diabecare_II implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */



public class DanaDiabecare_II extends DanaPump
{


    /**
     * Constructor 
     */
    public DanaDiabecare_II()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param params 
     * @param writer 
     */
    public DanaDiabecare_II(String params, OutputWriter writer)
    {
        super();
        //super(params, writer);
    }
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************
    
    
    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Diabcare II";
    }
    
    
    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        //return "da_dc_II.jpg";
        return "no_device.gif";
    }
    
    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return DanaPump.PUMP_DANA_DIABECARE_II;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_DANA_II";
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.dana.DanaDiabcare_IIS";
    }
    
    
    
    
    /** 
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }

}

