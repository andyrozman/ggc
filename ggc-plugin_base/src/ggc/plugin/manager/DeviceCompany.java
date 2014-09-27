/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */

package ggc.plugin.manager;

import ggc.plugin.device.DeviceInterface;

import java.util.Hashtable;

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
 *  Filename:      DeviceCompany  
 *  Description:   This is class for companies that are added to Managers.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DeviceCompany
{

    protected String id = "";
    protected String name = "";
    protected int index = 0;

    Hashtable<String, DeviceInterface> devices = new Hashtable<String, DeviceInterface>();

    /**
     * Constructor 
     * 
     * @param index index of device 
     * @param id id of device
     * @param name name of device
     */
    public DeviceCompany(int index, String id, String name)
    {
        this.index = index;
        this.id = id;
        this.name = name;
    }

    /**
     * Add Device
     * 
     * @param md device interface instance
     */
    public void addDevice(DeviceInterface md)
    {
        this.devices.put("" + md.getDeviceId(), md);
    }

    /**
     * getCompanyTypeDescription - this tells us for which plugin this company is valid.  
     * @return
     */
    public abstract String getCompanyTypeDescription();

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getCompanyTypeDescription() + " Company [index=" + index + ",id=" + id + ",name=" + name + "]";
    }

}
