package ggc.plugin.device.impl.animas.enums.advsett;

import java.util.HashMap;

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
 *  Filename:     BolusStepSize
 *  Description:  Bolus Step Size Enum
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum BolusStepSize implements CodeEnum
{
    Step_5_Units(3, "5"), //
    Step_1_Units(2, "1"), //
    Step_0_5_Units(1, "0.5"), //
    Step_0_1_Units(0, "0.1");

    // 2 = 1.0, 3 =5.0, 1 = 0.5, 0 =
    // 0.1
    int code = 0;
    private String value;

    static HashMap<Integer, BolusStepSize> mappingWithId = new HashMap<Integer, BolusStepSize>();

    static
    {
        for (BolusStepSize adt : values())
        {
            mappingWithId.put(adt.getCode(), adt);
        }
    }

    private BolusStepSize(int code, String value)
    {
        this.code = code;
        this.value = value;
    }

    public int getCode()
    {
        return code;
    }

    public static BolusStepSize getById(int code)
    {
        if (mappingWithId.containsKey(code))
        {
            return mappingWithId.get(code);
        }
        else
        {
            return BolusStepSize.Step_0_1_Units;
        }
    }

    public String getValue()
    {
        return value;
    }

}
