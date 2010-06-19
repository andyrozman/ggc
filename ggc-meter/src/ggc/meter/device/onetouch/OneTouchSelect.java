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
 *  Filename:     OneTouchSelect
 *  Description:  Support for LifeScan OneTouch Select Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */


// in works
public class OneTouchSelect extends OneTouchMeter2
{

    // Not implemented
    // Pictures
    
    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchSelect(String portName, OutputWriter writer)
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
    public OneTouchSelect(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }
    
    
    /**
     * Constructor
     */
    public OneTouchSelect()
    {
        super();
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchSelect(AbstractDeviceCompany cmp)
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
        return "One Touch Select";
    }

    
    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchSelect";
    }

    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_LIFESCAN_ONE_TOUCH_SELECT;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        // TODO fill in
        return null;
    }

    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_LIFESCAN_OFF";
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
        return "Select";
    }


    /**
     * Get Command
     * 
     * @param command
     * @return
     */
    @Override
    public String getCommand(int command)
    {
        switch(command)
        {
            case OneTouchMeter2.COMMAND_READ_SW_VERSION_AND_CREATE:
                return "02" + "09" + "00" + "05" + "0D" + "02" + "03" + "E8" + "42";

            case OneTouchMeter2.COMMAND_READ_SERIAL_NUMBER:    
                return "02" + "12" + "00" + "05" + // STX Len Link
                "0B" + "02" + "00" + "00" + "00" + "00" + "00" + "00" + "00" + "00" + "00" +  // CM1-CM12
                "03" + "19" + "E7"; // ETX CRC-L CRC-H
                
                
            default:
                return "";
        }
        
    }

}