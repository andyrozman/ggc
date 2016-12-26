package ggc.pump.device.minimed.file;

import java.util.Hashtable;

import com.atech.utils.ATDataAccessAbstract;

import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.device.impl.minimed.file.MinimedCareLinkData;
import ggc.pump.data.profile.Profile;
import ggc.pump.data.profile.ProfileSubPattern;

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
 *  Filename:     MinimedCareLinkPumpData  
 *  Description:  Minimed CareLink PumpData
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCareLinkPumpProfile extends MinimedCareLinkData // extends
                                                                    // ArrayList<MinimedCareLinkPumpData>
{

    @SuppressWarnings("unused")
    private String date_time_full = null;
    @SuppressWarnings("unused")
    private long dt_full = 0L;

    private String pattern_name = null;
    private int num_profiles = 0;
    private Hashtable<String, ProfileSubPattern> entries_index = new Hashtable<String, ProfileSubPattern>();


    /**
     * Constructor
     * 
     * @param date_time
     * @param mcl
     */
    public MinimedCareLinkPumpProfile(String date_time, MinimedCareLink mcl)
    {
        super(mcl);
        this.date_time_full = date_time;
    }


    // ChangeBasalProfilePattern =
    // "PATTERN_NAME=pattern a, NUM_PROFILES=20, ACTION_REQUESTOR=pump"
    // ChangeBasalProfile =
    // "PATTERN_DATUM=879228396, PROFILE_INDEX=0, RATE=0, 2, START_TIME=0"

    /**
     * Add Entry
     * 
     * @param entry
     */
    public void add(MinimedCareLinkPumpData entry)
    {
        if (entry.raw_type.equals("ChangeBasalProfilePattern"))
        {
            this.pattern_name = this.getDataBetween(entry.raw_values, "PATTERN_NAME=", "NUM_PROFILES=");
            this.num_profiles = Integer.parseInt(getDataNumber(entry.raw_values, "NUM_PROFILES=", "ACTION_REQUESTOR="));

            this.dt_full = entry.getAtechDateLong();

            System.out.println("Pattern name=" + this.pattern_name + ", Num profiles=" + this.num_profiles);
        }
        else if (entry.raw_type.equals("ChangeBasalProfile"))
        {
            // this.num_profiles =
            // Integer.parseInt(getDataNumber(entry.raw_values, "NUM_PROFILES=",
            // "ACTION_REQUESTOR="));

            // ChangeBasalProfile =
            // "PATTERN_DATUM=879228396, PROFILE_INDEX=0, RATE=0, 2,
            // START_TIME=0"
            String index = this.getDataBetween(entry.raw_values, "PROFILE_INDEX=", "RATE=");
            String rate = this.getDataNumber(entry.raw_values, "RATE=", "START_TIME=");
            String start_time = this.getDataDuration(entry.raw_values + ",END", "START_TIME=", "\",END");

            ProfileSubPattern psp = new ProfileSubPattern();
            psp.amount = Float.parseFloat(rate);
            psp.timeStart = Integer.parseInt(ATDataAccessAbstract.replaceExpression(start_time, ":", ""));
            psp.profileId = this.pattern_name;

            this.entries_index.put(index, psp);

            // psp.time_start =
            // psp.amount =

            System.out.print("Index=" + index + ",Rate=" + rate + ",Start time=" + start_time + "\n");

        }
        else
        {
            System.out.println("Error: Unknown type: " + entry.raw_type);
        }

    }


    @Override
    public void processData()
    {
        for (int i = 0; i < this.num_profiles - 1; i++)
        {
            this.entries_index.get("" + i).timeEnd = this.entries_index.get("" + (i + 1)).timeStart;
        }

        this.entries_index.get("" + this.num_profiles).dt_end = 2359;

    }


    /**
     * Get Profile 
     * @return
     */
    public Profile getProfile()
    {
        Profile p = new Profile();
        // p.
        p.profile_id = this.pattern_name;
        p.profile_active_from = 0L;

        p.pattern_entries.addAll(this.entries_index.values());

        return p;
    }

}
