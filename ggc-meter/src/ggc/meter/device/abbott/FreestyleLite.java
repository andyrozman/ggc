package ggc.meter.device.freestyle;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     FreestyleLite
 *  Description:  Support for Abbott Freestyle Lite Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class FreestyleLite extends FreestyleMeter
{

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(FreestyleLite.class);
    
    
    /**
     * Constructor
     */
    public FreestyleLite()
    {
        super();
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public FreestyleLite(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    
    
    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public FreestyleLite(String portName, OutputWriter writer)
    {
        super(portName, writer);
    }
    
    

    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }

    /** 
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.freestyle.FreestyleLite";
    }

    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return FreestyleMeter.METER_FREESTYLE_LITE;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ab_freestyle_lite.jpg";
    }

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Abbott Freestyle Lite";
    }

    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ABBOTT_FREESTYLE_LITE";
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
     * getImplementationStatus - Get implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }
    
    
}

