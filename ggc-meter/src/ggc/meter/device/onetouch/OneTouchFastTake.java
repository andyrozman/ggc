package ggc.meter.device.onetouch;

import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     OneTouchFastTake
 *  Description:  Support for LifeScan OneTouch FastTake Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */


// in works
public class OneTouchFastTake extends OneTouchMeter
{

    // Not implemented
    
    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchFastTake(String portName, OutputWriter writer)
    {
        super(portName, writer);
    }


    /**
     * Constructor
     */
    public OneTouchFastTake()
    {
        super();
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchFastTake(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    
    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "One Touch FastTake";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return OneTouchMeter.LIFESCAN_COMPANY;
    }

    
    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchFastTake";
    }

    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_FASTTAKE;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ls_ot_fasttake.jpg";
    }


    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        // TODO: 
        return 150;
    }

    /**
     * getShortName - Get short name of meter. 
     * 
     * @return short name of meter
     */
    public String getShortName()
    {
        return "FastTake";
    }

}