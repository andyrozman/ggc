package ggc.pump.data.profile;

import com.atech.utils.data.ATechDate;

import ggc.plugin.util.DataAccessPlugInBase;
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

// this is now used instead ProfileSubEntry, but we need to keep all variables,
// methods and constructors
public class ProfileSubPattern extends ProfileSubEntry // implements
// Comparable<ProfileSubPattern>
{

    /**
     * The time_start.
     */
    // public int time_start;

    /**
     * The time_end.
     */
    // public int time_end;

    /**
     * The time_start.
     */
    public long dt_start;

    /**
     * The time_end.
     */
    public long dt_end;


    /**
     * The amount.
     */
    // public float amount;

    /**
     * Instantiates a new profile sub entry.
     */
    public ProfileSubPattern()
    {
    }


    /**
     * Instantiates a new profile sub entry.
     *
     * @param input the input
     */
    public ProfileSubPattern(String input)
    {
        String[] ss = input.split("[-=]");

        DataAccessPump da = DataAccessPump.getInstance();

        timeStart = da.getIntValueFromString(ss[0]);
        timeEnd = da.getIntValueFromString(ss[1]);

        if (timeEnd == 0)
        {
            timeEnd = 2359;
        }

        amount = da.getFloatValueFromString(ss[2]);
    }


    /**
     * Sets the values.
     *
     * @param pse the new values
     */
    public void setValues(ProfileSubPattern pse)
    {
        this.timeStart = pse.timeStart;
        this.timeEnd = pse.timeEnd;
        this.amount = pse.amount;
    }


    /**
     * Gets the packed.
     *
     * @return the packed
     */
    @Override
    public String getPacked()
    {
        return timeStart + "-" + timeEnd + "=" + DataAccessPlugInBase.Decimal2Format.format(amount);
    }


    /**
     * toString
     */
    @Override
    public String toString()
    {
        return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, timeStart) + " - "
                + ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, timeEnd) + " = "
                + DataAccessPlugInBase.Decimal2Format.format(amount);
    }


    /**
     * Compare.
     *
     * @param pse1 the pse1
     * @param pse2 the pse2
     *
     * @return the int
     */
    /*
     * public int compare(ProfileSubPattern pse1, ProfileSubPattern pse2)
     * {
     * if (pse1.time_start == pse2.time_start)
     * {
     * System.out.println("Same Patt: ");
     * return (pse1.time_end - pse2.time_end);
     * }
     * else
     * {
     * System.out.println("Diff: " + (pse1.time_start - pse2.time_start));
     * return (pse1.time_start - pse2.time_start);
     * }
     * }
     */
    /**
     * compareTo
     *
     * @param psp
     * @return
     */
    /*
     * public int compareTo(ProfileSubPattern psp)
     * {
     * return compare(this, psp);
     * }
     */

    public boolean isForHour(int hour)
    {
        int fullHour = (hour * 100) + 1;

        return ((this.timeStart < fullHour) && (fullHour < this.timeEnd));
    }


    @Override
    public int getType()
    {
        return ProfileSubEntry.PROFILE_SUB_PATTERN;
    }


    /**
     * Check Time Presence
     *
     * @param time_table
     */
    public void checkTimePresence(int[][] time_table)
    {
        /*
         * int hour, min;
         * //boolean ret = true;
         * System.out.println("Start time: " + this.time_start + ", end_time: "
         * + this.time_end);
         * for(int i=this.time_start; i<this.time_end; i++)
         * {
         * hour = (i/60);
         * min = i-(hour*100);
         * if ((min>59) || (min<0))
         * {
         * System.out.println("Packed: " + i + ", Hour: " + hour + ", Minute: "
         * + min);
         * i = ((hour-1)*100) + 99;
         * System.out.println(" I=" + i);
         * continue;
         * }
         * System.out.println("Packed: " + i + ", Hour: " + hour + ", Minute: "
         * + min);
         * time_table[hour][min]++;
         * }
         * System.exit(0);
         */

        int start_h = (int) Math.floor(this.timeStart / 100);
        int start_m = this.timeStart - start_h * 100;

        int end_h = (int) Math.floor(this.timeEnd / 100);
        int end_m = this.timeEnd - end_h * 100;

        // int x = (int)Math.floor((this.time_end/100));
        // System.out.println("Time end: " + time_end + ", x: " + x);

        // boolean set = false;

        // System.out.println("Strat: " + start_h + ", " + start_m);
        // System.out.println("End: " + end_h + ", " + end_m);

        boolean begin = false, end = false;
        for (int i = start_h; i < end_h + 1; i++)
        {
            for (int j = 0; j < 60; j++)
            {

                if (start_h == i && j >= start_m || i > start_h)
                {
                    begin = true;
                    // time_table[i][j]++;
                    // set = true;
                }
                /*
                 * else if ((start_h>i))
                 * {
                 * set = true;
                 * }
                 */

                if (i < end_h || end_h == i && j <= end_m)
                {
                    end = true;
                    // set = false;
                }

                if (begin && end)
                {
                    time_table[i][j]++;
                    begin = false;
                    end = false;
                }

            }

        }

    }

}
