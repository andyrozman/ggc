package ggc.plugin.device.impl.minimed.enums;

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
 *  Filename:     MinimedDeviceType
 *  Description:  Internal enum for Minimed Devices.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum MinimedDeviceType
{
    Unknown_Device, //

    // Pump
    Minimed_508_508c(null, null), //
    Minimed_511(MinimedConverterType.Pump511Converter, null), //
    Minimed_512_712(MinimedConverterType.Pump512Converter, null), //
    Minimed_515_715(MinimedConverterType.Pump515Converter, null), //
    Minimed_522_722(MinimedConverterType.Pump515Converter, MinimedConverterType.CGMS522Converter), //
    Minimed_523_723(MinimedConverterType.Pump523Converter, MinimedConverterType.CGMS523Converter), //
    Minimed_553_753_Revel(MinimedConverterType.Pump523Converter, MinimedConverterType.CGMS523Converter), //
    Minimed_554_754_Veo(MinimedConverterType.Pump523Converter, MinimedConverterType.CGMS523Converter), //
    Minimed_640G(MinimedConverterType.Pump523Converter, MinimedConverterType.CGMS523Converter),

    Minimed_512andHigher(Minimed_512_712, Minimed_515_715, Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel,
            Minimed_554_754_Veo, Minimed_640G), //

    Minimed_515andHigher(Minimed_515_715, Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo,
            Minimed_640G), //
            Minimed_522andHigher(Minimed_522_722, Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo,
                    Minimed_640G), //
                    Minimed_523andHigher(Minimed_523_723, Minimed_553_753_Revel, Minimed_554_754_Veo, Minimed_640G), //

    Minimed_553andHigher(Minimed_553_753_Revel, Minimed_554_754_Veo, Minimed_640G), //
    Minimed_554andHigher(Minimed_554_754_Veo, Minimed_640G), //

    // CGMS
    MinimedCGMSGold(null, MinimedConverterType.CGMS522Converter), //

    MinimedGuradianRealTime(null, MinimedConverterType.CGMS522Converter), //

    //
    All;

    private boolean isFamily;
    private MinimedDeviceType[] familyMembers = null;


    static
    {
        // deviceTypeToPumpModelMap = new HashMap<String, MinimedDeviceType>();
        //
        // for (MinimedDeviceType adt : values())
        // {
        // if ((!adt.isFamily()) &&
        // (!deviceTypeToPumpModelMap.containsKey(adt.pumpModel)))
        // {
        // deviceTypeToPumpModelMap.put(adt.pumpModel, adt);
        // }
        // }
    }

    MinimedConverterType pumpConverter;
    MinimedConverterType cgmsConverter;


    MinimedDeviceType(MinimedConverterType pumpConverter, MinimedConverterType cgmsConverter)
    {
        this.isFamily = false;
        this.pumpConverter = pumpConverter;
        this.cgmsConverter = cgmsConverter;
    }


    MinimedDeviceType(MinimedDeviceType... familyMembers)
    {
        this.familyMembers = familyMembers;
        this.isFamily = true;
    }


    public static boolean isSameDevice(MinimedDeviceType deviceWeCheck, MinimedDeviceType deviceSources)
    {
        if (deviceSources.isFamily)
        {
            for (MinimedDeviceType mdt : deviceSources.familyMembers)
            {
                if (mdt == deviceWeCheck)
                    return true;
            }
        }
        else
        {
            return (deviceWeCheck == deviceSources);
        }

        return false;
    }


    public boolean isFamily()
    {
        return isFamily;
    }


    public MinimedDeviceType[] getFamilyMembers()
    {
        return familyMembers;
    }


    public MinimedConverterType getCGMSConverterType()
    {
        return cgmsConverter;
    }


    public MinimedConverterType getPumpConverterType()
    {
        return pumpConverter;
    }
}
