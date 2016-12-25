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
 *  Filename:     ExerciseStrength
 *  Description:  Exercise Strength
 *
 *  Author:    Andy {andy@atech-software.com}
 */

public enum ExerciseStrength
{
    Heavy(3), //
    Light(1), //
    MaxValue(4), //
    Medium(2), //
    Null(0);

    private int value;
    private static HashMap<Integer, ExerciseStrength> map = new HashMap<Integer, ExerciseStrength>();

    static
    {
        for (ExerciseStrength el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    ExerciseStrength(int value)
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

    public static ExerciseStrength getEnum(int value)
    {
        return map.get(value);
    }

}
