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
 *  Filename:     OneTouchInDuo
 *  Description:  Support for LifeScan InDuo Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// in works
public class OneTouchInDuo extends OneTouchMeter
{

    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchInDuo(String portName, OutputWriter writer)
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
    public OneTouchInDuo(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }

    /**
     * Constructor
     */
    public OneTouchInDuo()
    {
        super();
    }

    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchInDuo(AbstractDeviceCompany cmp)
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
        return "InDuo";
    }

    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchInDuo";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_LIFESCAN_INDUO;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ls_induo.jpg";
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    @Override
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
    @Override
    public String getShortName()
    {
        return "InDuo";
    }

}
