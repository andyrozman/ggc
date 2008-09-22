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


package ggc.plugin.manager.company; 

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.manager.EmptyMgrDevices;

import java.util.Hashtable;
import java.util.Vector;

public abstract class AbstractMgrCompany implements MgrCompanyInterface //, SelectableInterface
{


    //protected I18nControl m_ic = I18nControl.getInstance();
    //protected DataAccessPlugInBase m_da = DataAccessCGM.getInstance();
    
    public String id = "";
    public String name = "";
    public int index = 0;
    
    Hashtable<String,DeviceInterface> devices = new Hashtable<String,DeviceInterface>();
    Vector<DeviceInterface> devices_vector = new Vector<DeviceInterface>();
    

/*
    public AbstractMeterCompany(int index, String id, String name)
    {
    	this.index = index;
    	this.id = id;
    	this.name = name;
    }
*/

    
    public AbstractMgrCompany()
    {
        
    }
    

    public AbstractMgrCompany(boolean empty)
    {
        if (empty==true)
        {
            this.devices_vector.add(new EmptyMgrDevices());
        }
    }
    
    
    public Vector<DeviceInterface> getDevices()
    {
        return this.devices_vector;
    }
    
    
    public void addDevice(DeviceInterface md)
    {
        // TODO
//        md.setCGMCompany(this);
    	this.devices.put(""+md.getName(), md);
    	this.devices_vector.add(md);
    }

    
    public DeviceInterface getDevice(String name)
    {
        if (this.devices.containsKey(name))
            return this.devices.get(name);
        else
            return null;
    }
    
    public abstract String getName();
    
    
    public String toString()
    {
        return this.getName();
        
        //"Meter Company [name=" + this.getName() + ",id=" + this.getCompanyId() + "]";
    }
    

    public String getConnectionSample()
    {
        return "";
    }
    
    
    public String getConnectionSamples()
    {
        return "";
    }
    
    
    
}
