package ggc.plugin.device.impl.animas.enums.advsett;

import com.atech.utils.data.CodeEnum;

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
 *  Filename:     BolusSpeed
 *  Description:  Bolus Speed Enum
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum BolusSpeed implements CodeEnum
{
    Normal, // (0), //
    Slow // (1);
    ;

    int code;


    public int getCode()
    {
        return code;
    }


    public String getDescription()
    {
        // FIXME
        return this.name();
    }

}
