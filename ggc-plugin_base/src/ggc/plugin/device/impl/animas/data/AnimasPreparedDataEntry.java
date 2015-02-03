package ggc.plugin.device.impl.animas.data;

import com.atech.utils.data.ATechDate;

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
 *  Filename:     AnimasPreparedDataEntry
 *  Description:  Prepared data entry class. This will be then written into DeviceDataWriter,
 *       but not at once. Data is written at the end of reading.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasPreparedDataEntry
{
    public String key;
    public ATechDate dateTime;
    public String value;

    public AnimasPreparedDataEntry(String key, ATechDate dateTime)
    {
        this(key, dateTime, null);
    }


    public AnimasPreparedDataEntry(String key, ATechDate dateTime, String value)
    {
        this.key = key;
        this.dateTime = dateTime;
        this.value = value;
    }

}
