package ggc.pump.device.animas.impl.data.dto;

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
 *  Filename:     BasalProfile
 *  Description:  Basal Profile
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class BasalProfile extends ArrayList<BasalProfileEntry>
{

    public int number;
    public String name;


    public BasalProfile(int number)
    {
        super();
        this.number = number;
    }


    public boolean checkIfProfileIsSame(BasalLogDay basalDay)
    {

        return false;
    }

}
