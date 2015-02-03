package ggc.plugin.data.enums;

import java.util.HashMap;

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
 *  Filename:     GlucoseUnitType
 *  Description:  Glucose Unit Type Enum
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum GlucoseUnitType
{

    mg_dL(1, "GLUCOSE_UNIT_MGDL"), //
    mmol_L(2, "GLUCOSE_UNIT_MMOLL"), //
    None(0, "NONE"); //

    private int value;
    private String description;
    private static HashMap<Integer, GlucoseUnitType> map = new HashMap<Integer, GlucoseUnitType>();

    static
    {
        for (GlucoseUnitType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    GlucoseUnitType(int value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public static GlucoseUnitType getEnum(int value)
    {
        return map.get(value);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}
