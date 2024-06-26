package ggc.meter.device.abbott;

import ggc.meter.manager.MeterDevicesIds;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.DeviceImplementationStatus;
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
 *  Filename:     PrecisionXtra
 *  Description:  Support for Abbott Precision Xtra Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PrecisionXtra extends OptiumXceed
{

    /**
     * Constructor
     */
    public PrecisionXtra()
    {
        super();
    }


    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public PrecisionXtra(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public PrecisionXtra(String portName, OutputWriter writer)
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
    public PrecisionXtra(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }


    /** 
     * Get Device ClassName
     */
    @Override
    public String getDeviceClassName()
    {
        return "ggc.meter.device.abbott.PrecisionXtra";
    }


    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    @Override
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ABBOTT_PRECISION_XTRA;
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    @Override
    public String getIconName()
    {
        return "ab_precision_xtra.jpg";
    }


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    @Override
    public String getName()
    {
        return "Abbott Precision Xtra";
    }


    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    @Override
    public String getInstructions()
    {
        return "INSTRUCTIONS_ABBOTT_OPTIUMXCEED";
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
        return DeviceImplementationStatus.Done;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.DownloadData;
    }

}
