package ggc.plugin.manager.company;

import ggc.plugin.manager.DeviceImplementationStatus;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceCompanyInterface
 *  Description:  Device Company Interface
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public interface DeviceCompanyInterface
{

    // ********************************************************
    // *** Device Company Identification Methods ***
    // ********************************************************

    /**
     * getName - Get Name of device. 
     * Should be implemented by company class.
     * 
     * @return name of device
     */
    String getName();

    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by company class.
     * 
     * @return id of company
     */
    int getCompanyId();

    /**
     * getInstructions - get instructions for device
     * Should be implemented by company class.
     * 
     * @return instructions for reading data 
     */
    String getDescription();

    /**
     * getImplementationStatus - Get Implementation status 
     * Should be implemented by company class.
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    DeviceImplementationStatus getImplementationStatus();

}
