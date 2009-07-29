package ggc.meter.device.onetouch;

import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control<br>
 *  Plug-in:       Meter Tool (support for Meter devices)<br><br>
 *
 *  See AUTHORS for copyright information.<br><br>
 *  
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.<br>
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.<br>
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA<br><br>
 *  
 *  Filename:     OneTouchUltraEasy<br>
 *  Description:  Support for LifeScan OneTouch Ultra Easy Meter<br><br>
 * 
 *  Author: Andy {andy@atech-software.com}
 */



// in works
public class OneTouchUltraEasy extends OneTouchMeter2
{

    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchUltraEasy(String portName, OutputWriter writer)
    {
        super(portName, writer);
    }


    /**
     * Constructor
     */
    public OneTouchUltraEasy()
    {
        super();
    }
    
    
    
    /**
     * Constructor
     * 
     * @param n
     */
    public OneTouchUltraEasy(boolean n)
    {
    	super(n);
    }
    
    
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchUltraEasy(AbstractDeviceCompany cmp)
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
        return "One Touch Ultra Easy";
    }

    
    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchUltraEasy";
    }

    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRA_EASY;
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
        return 500;
    }

    /**
     * getShortName - Get short name of meter. 
     * 
     * @return short name of meter
     */
    public String getShortName()
    {
        return "Ultra Easy";
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
                return "02" + "09" + "00" + "05" + "0D" + "02" + "03" + "DA" + "71";

            case OneTouchMeter2.COMMAND_READ_SERIAL_NUMBER:    
                return "02" + "12" + "00" + "05" + // STX Len Link
                "0B" + "02" + "00" + "00" + "00" + "00" + "84" + "6A" + "E8" + "73" + "00" +  // CM1-CM12
                "03" + "9B" + "EA"; // ETX CRC-L CRC-H
                
            default:
                return "";
        }
        
    }
    
    
    
    
}