package ggc.core.data.defs;

import java.util.HashMap;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     Health
 *  Description:  Health Type
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum Health
{
    Null(0), //
    Illness(1), //
    Stress(2), //
    HighSymptoms(3), //
    LowSymptoms(4), //
    Cycle(5), //
    Alcohol(6), //

    ;

    private int value;
    private static HashMap<Integer, Health> map = new HashMap<Integer, Health>();

    static
    {
        for (Health el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    Health(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public static Health getEnum(int value)
    {
        return map.get(value);
    }

}
