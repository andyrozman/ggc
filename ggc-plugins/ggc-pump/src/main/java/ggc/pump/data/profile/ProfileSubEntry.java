package ggc.pump.data.profile;

import com.atech.utils.data.ATechDate;

import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
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

public abstract class ProfileSubEntry implements Comparable<ProfileSubEntry>
{

    /**
     * Profile Sub: Pattern
     */
    public static final int PROFILE_SUB_PATTERN = 1;

    /**
     * Profile Sub: Event
     */
    public static final int PROFILE_SUB_EVENT = 2;

    /**
     * Profile Id
     */
    public String profileId = "";

    /**
     * The time_start.
     */
    public int timeStart;

    /**
     * The time_end.
     */
    public int timeEnd;

    /**
     * The amount.
     */
    public float amount;


    public String getProfileId()
    {
        return profileId;
    }


    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }


    public int getTimeStart()
    {
        return timeStart;
    }


    public void setTimeStart(int timeStart)
    {
        this.timeStart = timeStart;
    }


    public int getTimeEnd()
    {
        return timeEnd;
    }


    public void setTimeEnd(int timeEnd)
    {
        this.timeEnd = timeEnd;
    }


    public float getAmount()
    {
        return amount;
    }


    public void setAmount(float amount)
    {
        this.amount = amount;
    }


    /**
     * Instantiates a new profile sub entry.
     */
    public ProfileSubEntry()
    {
    }


    /**
     * Instantiates a new profile sub entry.
     *
     * @param input the input
     */
    public ProfileSubEntry(String input)
    {
        String[] ss = input.split("[-=]");

        DataAccessPump da = DataAccessPump.getInstance();

        timeStart = da.getIntValueFromString(ss[0]);
        timeEnd = da.getIntValueFromString(ss[1]);
        amount = da.getFloatValueFromString(ss[2]);
    }


    /**
     * Sets the values.
     *
     * @param pse the new values
     */
    public void setValues(ProfileSubEntry pse)
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
    public int compare(ProfileSubEntry pse1, ProfileSubEntry pse2)
    {
        // System.out.println("Same: " + pse1.time_start + ", " +
        // pse2.time_start);
        if (pse1.timeStart == pse2.timeStart)
            // System.out.println("Same: ");
            return pse1.timeEnd - pse2.timeEnd;
        else
            // System.out.println("Subn");
            return pse1.timeStart - pse2.timeStart;
    }


    /**
     * compareTo
     */
    public int compareTo(ProfileSubEntry pse)
    {
        return compare(this, pse);
    }


    /**
     * Get Profile SubEntry Type
     * @return
     */
    public abstract int getType();

}
