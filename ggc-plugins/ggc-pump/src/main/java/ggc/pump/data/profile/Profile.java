package ggc.pump.data.profile;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.util.DataAccessPump;

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

public class Profile implements Comparable<Profile>
{

    private static final Logger LOG = LoggerFactory.getLogger(Profile.class);

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
     * Profile active from
     */
    public long profile_active_from = 0L;

    /**
     * Profile active till
     */
    public long profile_active_till = 0L;

    /**
     * Sort Order: Ascending
     */
    public static final int SORT_ORDER_ASC = 0;

    /**
     * Sort Order: Descending
     */
    public static final int SORT_ORDER_DESC = 1;

    /**
     * Sort Order
     */
    public int sort_order = SORT_ORDER_DESC;


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
        if (entry.getType() == ProfileSubEntry.PROFILE_SUB_EVENT)
        {
            this.add((ProfileSubOther) entry);
        }
        else
        {
            this.add((ProfileSubPattern) entry);
        }
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


    /**
     * Fill End Times
     */
    public void fillEndTimes()
    {

        Collections.sort(this.pattern_entries);
        // ProfileSubPattern first=null, last=null;
        // first = this.pattern_entries.get(0);
        // last = this.pattern_entries.get(0);

        // ArrayList<ProfileSubPattern> pe = new ArrayList<ProfileSubPattern>();

        for (int i = 1; i < this.pattern_entries.size(); i++)
        {
            // if (first.amount!=this.pattern_entries.get(i).amount)
            {
                int time_h = (int) Math.floor(this.pattern_entries.get(i).timeStart / 100);

                int end_time = (time_h - 1) * 100 + 59;

                this.pattern_entries.get(i - 1).timeEnd = end_time;
            }

        }

        this.pattern_entries.get(this.pattern_entries.size() - 1).timeEnd = 2359;

        Collections.sort(this.pattern_entries);

    }


    /**
     * Pack Profiles (if we have start and end time)
     */
    public void packProfiles()
    {
        Collections.sort(this.pattern_entries);
        ProfileSubPattern first = null, last = null;
        first = this.pattern_entries.get(0);
        last = this.pattern_entries.get(0);

        ArrayList<ProfileSubPattern> pe = new ArrayList<ProfileSubPattern>();

        for (int i = 1; i < this.pattern_entries.size(); i++)
        {
            if (first.amount != this.pattern_entries.get(i).amount)
            {
                first.timeEnd = last.timeEnd;
                pe.add(first);

                first = this.pattern_entries.get(i);
            }

            last = this.pattern_entries.get(i);
        }

        first.timeEnd = last.timeEnd;
        pe.add(first);

        this.pattern_entries.clear();
        this.pattern_entries.addAll(pe);
        Collections.sort(this.pattern_entries);

    }


    /**
     * Get Profile Information
     * 
     * @return
     */
    public String getProfileInformation()
    {
        StringBuffer sb = new StringBuffer();

        sb.append(" Profile [name=" + this.profile_id);
        sb.append(",patterns=");

        for (int i = 0; i < this.pattern_entries.size(); i++)
        {
            sb.append("{" + this.pattern_entries.get(i).getPacked() + "},");
        }

        sb.append("]");

        return sb.toString();
    }


    @Override
    public String toString()
    {
        return this.getProfileInformation();
    }


    /**
     * Is Complete Profile
     * 
     * @return
     */
    public boolean isCompleteProfile()
    {
        int[][] time_stamps = new int[24][60];

        for (int i = 0; i < 24; i++)
        {
            for (int j = 0; j < 60; j++)
            {
                time_stamps[i][j] = 0;
            }
        }

        for (int i = 0; i < this.pattern_entries.size(); i++)
        {
            this.pattern_entries.get(i).checkTimePresence(time_stamps);
        }

        boolean error = false;

        StringBuilder sb_not_set = new StringBuilder("Not set: [");
        StringBuilder sb_multiple_set = new StringBuilder("Multiple set: [");

        for (int i = 0; i < 24; i++)
        {
            for (int j = 0; j < 60; j++)
            {
                if (time_stamps[i][j] != 1)
                {
                    if (time_stamps[i][j] == 0)
                    {
                        sb_not_set.append(getPackedTime(i, j, 0));
                        error = true;
                    }
                    else if (time_stamps[i][j] > 1)
                    {
                        sb_multiple_set.append(getPackedTime(i, j, time_stamps[i][j]));
                        error = true;
                    }
                }
            }
        }

        if (!sb_not_set.toString().equals("Not set: ["))
        {
            LOG.debug(sb_not_set.toString() + "]");
        }

        if (!sb_multiple_set.toString().equals("Multiple set: ["))
        {
            LOG.debug(sb_multiple_set.toString() + "]");
        }

        return !error;
    }


    private String getPackedTime(int i, int j, int stamps)
    {

        String time = "";

        if (i == 0)
        {
            time = "00:";
        }
        else if (i < 10)
        {
            time = "0" + i + ":";
        }
        else
        {
            time = i + ":";
        }

        if (j == 0)
        {
            time += "00";
        }
        else if (j < 10)
        {
            time += "0" + j;
        }
        else
        {
            time += j;
        }

        if (stamps != 0)
        {
            time += "," + stamps;
        }

        return "{" + time + "}";
    }


    /**
     * Compare.
     * 
     * @param pse1 the pse1
     * @param pse2 the pse2
     * 
     * @return the int
     */
    public int compare(Profile pse1, Profile pse2)
    {
        if (DataAccessPump.getInstance().getSortSetting("Profile").equals("ASC"))
            return (int) (pse1.date_at - pse2.date_at);
        else
            return (int) (pse2.date_at - pse1.date_at);
    }


    /** 
     * compareTo
     */
    public int compareTo(Profile pse)
    {
        return compare(this, pse);
    }


    /**
     * Equals
     * 
     * @param p_in
     * @return
     */
    public boolean equals(Profile p_in)
    {
        return this.getProfileInformation().equals(p_in.getProfileInformation());
    }


    /**
     * Get Profile Time Info
     * 
     * @return
     */
    public String getProfileTimeInfo()
    {
        return "From: " + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.profile_active_from)
                + ", Till: " + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, this.profile_active_till)
                + ", Profile: " + this.profile_id;
    }


    /**
     * Create Db Object
     * 
     * @return
     */
    public PumpValuesEntryProfile createDbObject()
    {
        PumpValuesEntryProfile p = new PumpValuesEntryProfile();

        p.setName(this.profile_id);
        p.setBasal_base(0);
        p.setBasal_diffs(createBasalDiffs());
        p.setActive_from(this.profile_active_from);
        p.setActive_till(this.profile_active_till);
        p.setPerson_id((int) DataAccessPump.getInstance().getCurrentUserId());
        p.setComment("");
        p.setChanged(System.currentTimeMillis());

        return p;
    }


    /**
     * Create Basal Diffs
     * 
     * @return
     */
    public String createBasalDiffs()
    {
        StringBuilder sb = new StringBuilder();

        Collections.sort(this.pattern_entries);

        for (int i = 0; i < this.pattern_entries.size(); i++)
        {
            sb.append(this.pattern_entries.get(i).getPacked());
            sb.append(";");
        }

        return sb.substring(0, sb.length() - 1);

    }

}
