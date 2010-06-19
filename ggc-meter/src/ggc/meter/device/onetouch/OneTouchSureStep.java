package ggc.meter.device.onetouch;

import ggc.meter.manager.MeterDevicesIds;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     OneTouchSureStep
 *  Description:  Support for LifeScan OneTouch SureStep Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */



// in works
public class OneTouchSureStep extends OneTouchMeter
{

    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchSureStep(String portName, OutputWriter writer)
    {
        super(portName, writer);
    }


    /**
     * Constructor
     * 
     * @param comm_parameters
     * @param writer
     * @param da 
     */
    public OneTouchSureStep(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }
    
    
    /**
     * Constructor
     */
    public OneTouchSureStep()
    {
        super();
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchSureStep(AbstractDeviceCompany cmp)
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
        return "One Touch SureStep";
    }

    
    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchSureStep";
    }

    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_LIFESCAN_ONE_TOUCH_SURESTEP;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ls_ot_surestep.jpg";
    }

    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_LIFESCAN_ON";
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
        return "SureStep";
    }

}