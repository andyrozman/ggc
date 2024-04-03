package ggc.meter.device.ascensia;

import ggc.meter.manager.MeterDevicesIds;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import javax.swing.ImageIcon;

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
 *  Filename:     AscensiaDex
 *  Description:  Support for Ascensia/Bayer Dex Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class AscensiaDEX extends AscensiaMeter
{

    /**
     * Constructor
     */
    public AscensiaDEX()
    {
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AscensiaDEX(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public AscensiaDEX(String portName, OutputWriter writer)
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
    public AscensiaDEX(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }

    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "DEX";
    }

    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcon
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ascensia_dex2.png";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ASCENSIA_DEX;
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ASCENSIA_DEX";
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
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.ascensia.AscensiaDEX";
    }

    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 100;
    }

}
