package ggc.plugin.manager;

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
 *  Filename:      DeviceImplementationStatus  
 *  Description:   Implementation Status of Device. 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum DeviceImplementationStatus
{

    /**
     * Implementation: Not Available
     */
    IMPLEMENTATION_NOT_AVAILABLE, // 0

    /**
     * Implementation: Not Planned
     */
    IMPLEMENTATION_NOT_PLANNED, // 1

    /**
     * Implementation: Planned
     */
    IMPLEMENTATION_PLANNED, // 2;

    /**
     * Implementation: Partitial
     */
    IMPLEMENTATION_PARTITIAL, //3;

    /**
     * Implementation: Full
     */
    IMPLEMENTATION_FULL, // = 4;

    /**
     * Implementation: In Progress
     */
    IMPLEMENTATION_IN_PROGRESS, // = 5;

    /**
     * Implementation: Testing
     */
    IMPLEMENTATION_TESTING, // = 6;

    /**
     * Implementation: Done
     */
    IMPLEMENTATION_DONE, // = 7;

    /**
     * Functionality: Read Data Full
     */
    FUNCTIONALITY_READ_DATA_FULL, // = 1;

    /**
     * Functionality: Read Data Partitial
     */
    FUNCTIONALITY_READ_DATA_PARTITIAL, // = 2;

    /**
     * Functionality: Read Info
     */
    FUNCTIONALITY_READ_INFO, // = 4;

    /**
     * Functionality: Read Config
     */
    FUNCTIONALITY_READ_CONFIG, // = 8;

    ;

}
