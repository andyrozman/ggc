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
    NotAvailable, // 0

    /**
     * Implementation: Not Planned
     */
    NotPlanned, // 1

    /**
     * Implementation: Planned
     */
    Planned, // 2;

    /**
     * Implementation: Partitial
     */
    Partitial, // 3;

    /**
     * Implementation: Full
     */
    Full, // = 4;

    /**
     * Implementation: In Progress
     */
    InProgress, // = 5;

    /**
     * Implementation: Testing
     */
    Testing, // = 6;

    /**
     * Implementation: Done
     */
    Done, // = 7;

    NotDoneButShouldBeDisplayed, //

    DoesntSupportDownload,

    ;

    public static boolean isSupportedDevice(DeviceImplementationStatus status)
    {
        return ((status == Partitial) || //
                (status == Full) || //
                (status == InProgress) || //
                (status == Testing) || //
                (status == Done) || //
                (status == NotDoneButShouldBeDisplayed) || //
        (status == DoesntSupportDownload) //
        );
    }
}
