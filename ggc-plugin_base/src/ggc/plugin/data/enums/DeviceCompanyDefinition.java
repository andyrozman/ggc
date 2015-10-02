package ggc.plugin.data.enums;

import ggc.plugin.manager.DeviceImplementationStatus;

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
 *  Filename:     DeviceCompanyDefinition
 *  Description:  Device Company Definition. List of all companies used for devices (V2).
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum DeviceCompanyDefinition
{

    // 1xx = Meter
    Ascensia(101, "Ascensia/Bayer", "", DeviceImplementationStatus.Done),

    // 2xx = Pump
    Roche(2, "Roche", "", DeviceImplementationStatus.Partitial), // Disetronic(2),
    Deltec(5), Insulet(206, "Insulet", "", DeviceImplementationStatus.Done), Sooil(7),

    // 3xx = CGMS
    Dexcom(301, "Dexcom", "", DeviceImplementationStatus.Partitial), //

    // 4xx = Pump + CGMS
    Animas(401, "Animas", "", DeviceImplementationStatus.Testing), //
    Minimed(402, "Minimed", "", DeviceImplementationStatus.Planned), //

    ;

    int id;
    String companyName;
    String companyDescription;
    DeviceImplementationStatus companyImplementationStatus;


    private DeviceCompanyDefinition(int code)
    {

    }


    private DeviceCompanyDefinition(int id, String name, String description,
            DeviceImplementationStatus implementationStatus)
    {
        this.id = id;
        this.companyName = name;
        this.companyDescription = description;
        this.companyImplementationStatus = implementationStatus;
    }


    public String getName()
    {
        return this.companyName;
    }


    public int getCompanyId()
    {
        return this.id;
    }


    public String getDescription()
    {
        return this.companyDescription;
    }


    public DeviceImplementationStatus getImplementationStatus()
    {
        return this.companyImplementationStatus;
    }

}
