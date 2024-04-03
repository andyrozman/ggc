package ggc.cgms.data.defs.extended;

import java.util.HashMap;
import java.util.Map;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

// TODO refactor
public enum CGMSExtendedDataType
{

    None(0, "NONE"), //
    Carbs(1, "CGMS_EXT_CARBS"), //
    Insulin(2, "CGMS_EXT_INSULIN"), //
    Health(3, "CGMS_EXT_HEALTH"), //
    Exercise(4, "CGMS_EXT_EXERCISE"), //
    ;

    private int value;
    private String description;
    private static Map<Integer, CGMSExtendedDataType> map = new HashMap<Integer, CGMSExtendedDataType>();

    static
    {
        for (CGMSExtendedDataType el : values())
        {
            map.put(el.getValue(), el);
        }
    }


    CGMSExtendedDataType(int value, String description)
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


    public static CGMSExtendedDataType getEnum(int value)
    {
        if (map.containsKey(value))
            return map.get(value);
        else
            return CGMSExtendedDataType.None;
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
