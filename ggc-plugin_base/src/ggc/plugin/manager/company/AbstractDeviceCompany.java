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

import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractDeviceCompany implements DeviceCompanyInterface //, SelectableInterface
{


    protected I18nControlAbstract m_ic = null; //I18nControl.getInstance();
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

    
    public AbstractDeviceCompany(I18nControlAbstract ic)
    {
        this.m_ic = ic;
    }
    

    public AbstractDeviceCompany(I18nControlAbstract ic, boolean empty)
    {
        this(ic);
        
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

    
    public DeviceInterface getDevice(String _name)
    {
        if (this.devices.containsKey(_name))
            return this.devices.get(_name);
        else
            return null;
    }
    
    public abstract String getName();
    
    
    public String toString()
    {
        return this.getName();
        
        //"Meter Company [name=" + this.getName() + ",id=" + this.getCompanyId() + "]";
    }
    

    
    /**
     * Get Connection Sample - This will display sample of connection parameter, for example on Serial
     * devices this is COMx. On other OSes there are of course other parameters, but COMx parameter is
     * "known" serial parameter and will be known to all people.
     * 
     * @return
     */
    public String getConnectionSample()
    {
        return "";
    }
    
    
    /**
     * Get Connection Samples - This is I18N keyword, which needs to be added to language file. For Serial 
     * devices this is SERIAL_PORTS.
     * 
     * @return
     */
    public String getConnectionSamples()
    {
        return "";
    }
    
    
    
}
