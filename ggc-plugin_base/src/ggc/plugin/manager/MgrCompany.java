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

public abstract class MgrCompany
{

    //protected I18nControl m_ic = I18nControl.getInstance();
    //protected DataAccessCGM m_da = DataAccessCGM.getInstance();
    
    public String id = "";
    public String name = "";
    public int index = 0;
    
    Hashtable<String,DeviceInterface> devices = new Hashtable<String,DeviceInterface>();


    public MgrCompany(int index, String id, String name)
    {
    	this.index = index;
    	this.id = id;
    	this.name = name;
    }


    public void addDevice(DeviceInterface md)
    {
    	this.devices.put(""+md.getDeviceId(), md);
    }

    /**
     * getCompanyTypeDescription - this tells us for which plugin this company is valid.  
     * @return
     */
    public abstract String getCompanyTypeDescription();
    
    
    public String toString()
    {
    	return getCompanyTypeDescription() + " Company [index=" + index + ",id=" + id + ",name=" + name + "]";
    }
    
    
}
