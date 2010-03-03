package ggc.pump.data.profile;

import java.util.ArrayList;

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


public class Profile 
{
    
    /**
     * Profile Id
     */
    public String profile_id = "";
    
    // if we are resolving
    /**
     * Date (ATech)
     */
    public long date_at = 0L;
    
    /**
     * Pattern Entries
     */
    public ArrayList<ProfileSubPattern> pattern_entries = new ArrayList<ProfileSubPattern>();
    
    
    /**
     * Other Entries
     */
    public ArrayList<ProfileSubOther> other_entries = new ArrayList<ProfileSubOther>();
    
    
    
    /**
     * Constructor
     */
    public Profile()
    {
    }
    

    /**
     * Add profile sub entry
     * 
     * @param entry
     */
    public void add(ProfileSubEntry entry)
    {
        if (entry.getType()==ProfileSubEntry.PROFILE_SUB_EVENT)
            this.add((ProfileSubOther)entry);
        else
            this.add((ProfileSubPattern)entry);
    }
    
    
    /**
     * Add profile sub other
     * 
     * @param entry
     */
    public void add(ProfileSubOther entry)
    {
        this.other_entries.add(entry);
    }
    

    /**
     * Add profile sub pattern
     * 
     * @param entry
     */
    public void add(ProfileSubPattern entry)
    {
        this.pattern_entries.add(entry);
    }
    
    
}
