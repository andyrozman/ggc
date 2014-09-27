package ggc.pump.data.profile;

import com.atech.utils.data.ATechDate;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class ProfileSubOther extends ProfileSubEntry
{

    /**
     * Event: Pattern Changed
     */
    public static final int EVENT_PATTERN_CHANGED = 1;

    /**
     * The time_start.
     */
    public long time_event;

    /**
     * Event type
     */
    public int event_type;

    @Override
    public int getType()
    {
        return ProfileSubEntry.PROFILE_SUB_EVENT;
    }

    /**
     * Get Change Info
     * 
     * @return
     */
    public String getChangeInfo()
    {
        return " Pattern " + this.profile_id + " changed at "
                + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.time_event);
    }

    /**
     * Compare.
     * 
     * @param pse1 the pse1
     * @param pse2 the pse2
     * 
     * @return the int
     */
    public int compare(ProfileSubOther pse1, ProfileSubOther pse2)
    {
        return (int) (pse1.time_event - pse2.time_event);
    }

    /** 
     * compareTo
     * 
     * @param psp
     * @return 
     */
    public int compareTo(ProfileSubOther psp)
    {
        return compare(this, psp);
    }

}
