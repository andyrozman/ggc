package ggc.plugin.list;

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
 *  Filename:     BaseListEntry
 *  Description:  Base List Entry
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class BaseListEntry
{

    /**
     * Status: None
     */
    public static final int STATUS_NONE = 0; // black

    /**
     * Status: Done
     */
    public static final int STATUS_DONE = 1; // green

    /**
     * Status: Testing
     */
    public static final int STATUS_TESTING = 2; // light green

    /**
     * 
     */
    public static final int STATUS_PART_IMPLEMENTED = 3; // light blue

    /**
     * 
     */
    public static final int STATUS_IMPLEMENTING = 4; // blue
    /**
     * 
     */
    public static final int STATUS_PLANNED = 5; // orange
    /**
     * 
     */
    public static final int STATUS_NOTPLANNED = 6; // red

    /**
     * Name
     */
    public String name;

    /**
     * Page 
     */
    public String page;

    /**
     * Status 
     */
    public int status;

    /**
     * Constructor
     */
    public BaseListEntry()
    {
    }

    /**
     * Constructor
     * 
     * @param name
     * @param page
     * @param status
     */
    public BaseListEntry(String name, String page, int status)
    {
        this.name = name;
        this.page = page;
        this.status = status;
    }

    /**
     * To String 
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }

}
