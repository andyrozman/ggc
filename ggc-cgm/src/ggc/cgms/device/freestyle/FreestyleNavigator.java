package ggc.cgms.device.freestyle;

import ggc.cgms.manager.CGMSDevicesIds;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     InsuletOmniPod  
 *  Description:  Insulet OmniPod implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FreestyleNavigator extends FreestyleCGMS
{

    /**
     * Constructor 
     */
    public FreestyleNavigator()
    {
        super();
    }

    /**
     * Constructor 
     * 
     * @param drive_letter 
     * @param writer 
     */
    public FreestyleNavigator(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer);
    }

    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da
     */
    public FreestyleNavigator(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public FreestyleNavigator(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    // ************************************************
    // *** Device Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of device 
     * 
     * @return name of device
     */
    @Override
    public String getName()
    {
        return "Freestyle Navigator";
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ab_navigator.jpg";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return CGMSDevicesIds.CGMS_FREESTYLE_NAVIGATOR;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_NO_INFO";
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
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    @Override
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotPlanned;
    }

    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.cgms.device.freestyle.FreestyleNavigator";
    }

    /** 
     * Get Max Memory Records
     * 
     * @return 
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NoDownloadSupport;
    }

    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    @Override
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }

}
