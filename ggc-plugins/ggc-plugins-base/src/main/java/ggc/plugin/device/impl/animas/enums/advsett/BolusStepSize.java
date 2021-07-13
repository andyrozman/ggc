package ggc.plugin.device.impl.animas.enums.advsett;

import java.util.HashMap;
import java.util.Map;

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
    Step_5_Units(3, "5", "5.0 U"), //
    Step_1_Units(2, "1", "1.0 U"), //
    Step_0_5_Units(1, "0.5", "0.5 U"), //
    Step_0_1_Units(0, "0.1", "0.1 U");

    // 2 = 1.0, 3 =5.0, 1 = 0.5, 0 =
    // 0.1
    int code = 0;
    private String value;
    private String description;

    static Map<Integer, BolusStepSize> mappingWithId = new HashMap<Integer, BolusStepSize>();
    static Map<String, BolusStepSize> mapByDescription = new HashMap<String, BolusStepSize>();

    static
    {
        for (BolusStepSize adt : values())
        {
            mappingWithId.put(adt.getCode(), adt);
            mapByDescription.put(adt.getDescription(), adt);
        }
    }


    BolusStepSize(int code, String value, String description)
    {
        this.code = code;
        this.value = value;
        this.description = description;
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


    public static BolusStepSize getByDescription(String description)
    {
        if (mapByDescription.containsKey(description))
        {
            return mapByDescription.get(description);
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


    public String getDescription()
    {
        return description;
    }
}
