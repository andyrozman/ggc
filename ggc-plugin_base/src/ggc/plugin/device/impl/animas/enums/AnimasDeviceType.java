package ggc.plugin.device.impl.animas.enums;

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
 *  Filename:     AnimasDeviceType
 *  Description:  Animas Device Type
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum AnimasDeviceType
{
    Animas_Unknown_Pump(AnimasImplementationType.AnimasImplementationV2, "Unknown Animas Pump", ""), //

    Animas_IR1000(AnimasImplementationType.AnimasImplementationV1, "Animas IR 1000", "1000"), //
    Animas_IR1200(AnimasImplementationType.AnimasImplementationV2, "Animas IR 1200", "1200"), //
    Animas_IR1200p(AnimasImplementationType.AnimasImplementationV2, "Animas IR 1200+", "1200+"), //

    Animas_IR1250(AnimasImplementationType.AnimasImplementationV2, "Animas IR 1250", "1250"), //

    Animas_IR1275(AnimasImplementationType.AnimasImplementationV2, "Animas 2020", "1275"), //
    Animas_IR1275i(AnimasImplementationType.AnimasImplementationV2, "Animas 2020i", "1275i"), //
    Animas_2200(AnimasImplementationType.AnimasImplementationV2, "Animas 2020", "1275"), //
    Animas_2200i(AnimasImplementationType.AnimasImplementationV2, "Animas 2020i", "1275i"), //

    Animas_IR1285(AnimasImplementationType.AnimasImplementationV2, "OneTouch Ping", "1285"), //
    Animas_IR1285i(AnimasImplementationType.AnimasImplementationV2, "OneTouch Ping INTL", "1285i"), //
    Animas_Ping(AnimasImplementationType.AnimasImplementationV2, "OneTouch Ping", "1285"), //

    Animas_Vibe(AnimasImplementationType.AnimasImplementationV2, "OneTouch Vibe", "1300"),

    Animas_Family_Pre2200(Animas_IR1000, Animas_IR1200, Animas_IR1200p, Animas_IR1250), //
    Animas_Family_2200(Animas_IR1275, Animas_IR1275i, Animas_2200, Animas_2200i), //
    Animas_Family_Ping(Animas_IR1285, Animas_IR1285i, Animas_Ping), //
    Animas_Family_Vibe(Animas_Vibe);

    AnimasImplementationType implementationType;
    String name;
    String pumpModel;
    static HashMap<String, AnimasDeviceType> deviceTypeToPumpModelMap = null;
    private boolean isFamily;
    private AnimasDeviceType[] familyMembers = null;

    static
    {
        deviceTypeToPumpModelMap = new HashMap<String, AnimasDeviceType>();

        for (AnimasDeviceType adt : values())
        {
            if ((!adt.isFamily()) && (!deviceTypeToPumpModelMap.containsKey(adt.pumpModel)))
            {
                deviceTypeToPumpModelMap.put(adt.pumpModel, adt);
            }
        }
    }

    private AnimasDeviceType(AnimasImplementationType implementationType, String name, String pumpModel)
    {
        this.implementationType = implementationType;
        this.name = name;
        this.pumpModel = pumpModel;
    }

    private AnimasDeviceType(AnimasDeviceType... familyMembers)
    {
        this.familyMembers = familyMembers;
        this.isFamily = true;
    }

    public AnimasImplementationType getImplementationType()
    {
        return implementationType;
    }

    public static boolean isAnimasPingFamily(AnimasDeviceType type)
    {
        return ((type == Animas_IR1285) || (type == Animas_IR1285i) || (type == Animas_Ping));
    }

    public static boolean isAnimas2020Family(AnimasDeviceType type)
    {
        return ((type == Animas_IR1275) || (type == Animas_IR1275i) || (type == Animas_2200) || (type == Animas_2200i));
    }

    public static AnimasDeviceType getAnimasDeviceTypeFromPumpModel(String pumpModel)
    {
        if (deviceTypeToPumpModelMap.containsKey(pumpModel))
        {
            return deviceTypeToPumpModelMap.get(pumpModel);
        }
        else
        {
            return AnimasDeviceType.Animas_Unknown_Pump;
        }
    }

    public String getDisplayName()
    {
        return name;
    }

    public boolean isFamily()
    {
        return isFamily;
    }

    public AnimasDeviceType[] getFamilyMembers()
    {
        return familyMembers;
    }

}
