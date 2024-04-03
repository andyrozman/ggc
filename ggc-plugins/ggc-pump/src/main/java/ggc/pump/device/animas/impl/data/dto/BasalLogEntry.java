package ggc.pump.device.animas.impl.data.dto;

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
 *  Filename:     BasalLogEntry
 *  Description:  Basal Log Entry
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class BasalLogEntry
{

    private ATechDate dateTime;
    private float rate;
    private int flag;


    public BasalLogEntry(ATechDate dateTime, float rate, int flag)
    {
        this.dateTime = dateTime;
        this.rate = rate;
        this.flag = flag;
    }


    public ATechDate getDateTime()
    {
        return dateTime;
    }


    public void setDateTime(ATechDate dateTime)
    {
        this.dateTime = dateTime;
    }


    public float getRate()
    {
        return rate;
    }


    public void setRate(float rate)
    {
        this.rate = rate;
    }


    public int getFlag()
    {
        return flag;
    }


    public void setFlag(int flag)
    {
        this.flag = flag;
    }

}
