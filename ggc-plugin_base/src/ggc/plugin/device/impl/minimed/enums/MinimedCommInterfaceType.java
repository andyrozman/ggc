package ggc.plugin.device.impl.minimed.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *  Filename:     MinimedCommInterfaceType
 *  Description:  Enum with definition of all Minimed Communication interfaces.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum MinimedCommInterfaceType
{
    None("None", false), //

    // used only by 508/508c model (not supported)
    ComStation("Minimed ComStation", false), //

    // old adapter Minimed ComLink - MinimedCommunicationComLink
    ComLink("Minimed ComLink", false), //
    ParadigmLinkCom("ParadigmLink Meter (Serial)", false), //
    ParadigmLinkUSB("ParadigmLink Meter (USB)", false), //

    // Carelink USB and its derivates - MinimedCommunicationCareLinkUSB
    CareLinkUSB("Medtronic CareLink USB", true), //

    // Contour Next Link
    ContourUSBNextLink("Contour Next Link", false), //
    ContourUSBNextLink24("Contour Next Link 2.4", false), //
    ;

    static Map<String, MinimedCommInterfaceType> mapByName;
    static Map<String, MinimedCommInterfaceType> mapByDescription;
    static List<MinimedCommInterfaceType> supportedInterfaces;
    String description;
    boolean supported;


    static
    {
        mapByName = new HashMap<String, MinimedCommInterfaceType>();
        mapByDescription = new HashMap<String, MinimedCommInterfaceType>();
        supportedInterfaces = new ArrayList<MinimedCommInterfaceType>();

        for (MinimedCommInterfaceType mcit : values())
        {
            mapByName.put(mcit.name(), mcit);
            mapByDescription.put(mcit.description, mcit);

            if (mcit.supported)
            {
                supportedInterfaces.add(mcit);
            }
        }
    }


    MinimedCommInterfaceType(String description, boolean supported)
    {
        this.description = description;
        this.supported = supported;
    }


    public static MinimedCommInterfaceType getByName(String name)
    {
        if (mapByName.containsKey(name))
            return mapByName.get(name);
        else
            return MinimedCommInterfaceType.None;
    }


    public static String[] getSupportedInterfacesArray()
    {
        String[] interfaceNames = new String[supportedInterfaces.size()];

        int i = 0;
        for (MinimedCommInterfaceType type : supportedInterfaces)
        {
            interfaceNames[i] = type.description;
            i++;
        }

        return interfaceNames;
    }


    public static MinimedCommInterfaceType getByDescription(String description)
    {
        if (mapByDescription.containsKey(description))
            return mapByDescription.get(description);
        else
            return MinimedCommInterfaceType.None;
    }
}
