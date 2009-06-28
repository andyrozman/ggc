package ggc.pump.device.deltec;

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
 *  Filename:     DeltecCosmo1700  
 *  Description:  Deltec Cosmo 1700 implementation (just for settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeltecCosmo1700 extends DeltecPump
{

    /**
     * Constructor 
     */
    public DeltecCosmo1700()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param drive_letter 
     * @param writer 
     */
    public DeltecCosmo1700(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer);
    }
    

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Cosmo 1700";
    }


    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
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
        return DeltecPump.PUMP_DELTEC_COSMO_1700;
    }

    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_DELTEC_COSMO_1700";
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
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.deltec.DeltecCosmo1700";
    }


    /** 
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }
    
}

