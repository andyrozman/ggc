package ggc.meter.device.abbott;

import ggc.meter.manager.MeterDevicesIds;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     FreestyleFreedomLite
 *  Description:  Support for Abbott Freestyle Freedom Lite Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FreestyleFreedomLite extends FreestyleMeter
{

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(FreestyleFreedomLite.class);

    /**
     * Constructor
     */
    public FreestyleFreedomLite()
    {
        super();
    }

    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public FreestyleFreedomLite(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public FreestyleFreedomLite(String portName, OutputWriter writer)
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
    public FreestyleFreedomLite(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
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
        return "ggc.meter.device.freestyle.FreestyleFreedomLite";
    }

    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ABBOTT_FREESTYLE_FREEDOM_LITE;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ab_freestyle_freedom_lite.jpg";
    }

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Abbott Freestyle Freedom Lite";
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    @Override
    public String getInstructions()
    {
        return "INSTRUCTIONS_ABBOTT_FREESTYLE_FREEDOM_LITE";
    }

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    @Override
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
    @Override
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }

}
