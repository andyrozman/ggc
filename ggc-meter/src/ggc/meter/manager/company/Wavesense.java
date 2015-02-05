package ggc.meter.manager.company;

import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;

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
 *  Filename:      Wavesense  
 *  Description:   Meter Company - Wavesense
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class Wavesense extends AbstractDeviceCompany
{

    /**
     * Constructor
     */
    public Wavesense()
    {
        super(true, // empty devices
                MeterDevicesIds.COMPANY_WAVESENSE, // company_id
                "Wave Sense", // company name (full)
                "Wave Sense", // short company name
                "WAVESENSE_DESC", // company description
                DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED, DataAccessMeter.getInstance()); // implementation
                                                                                                       // status
    }

    // ********************************************************
    // *** Meter Company Identification Methods ***
    // ********************************************************

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    @Override
    public String getName()
    {
        return "Wave Sense";
    }

    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    @Override
    public int getCompanyId()
    {
        return 12;
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    @Override
    public String getDescription()
    {
        return "WAVESENSE_DESC";
    }

    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    @Override
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_PLANNED;
    }

}
