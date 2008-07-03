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


package ggc.pump.manager.company; 

import ggc.pump.device.PumpInterface;
import ggc.pump.manager.EmptyPumpDevices;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.Hashtable;
import java.util.Vector;

public abstract class AbstractPumpCompany implements PumpCompanyInterface //, SelectableInterface
{


    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessPump m_da = DataAccessPump.getInstance();
    
    public String id = "";
    public String name = "";
    public int index = 0;
    
    Hashtable<String,PumpInterface> devices = new Hashtable<String,PumpInterface>();
    Vector<PumpInterface> devices_vector = new Vector<PumpInterface>();
    

/*
    public AbstractMeterCompany(int index, String id, String name)
    {
    	this.index = index;
    	this.id = id;
    	this.name = name;
    }
*/

    
    public AbstractPumpCompany()
    {
        
    }
    

    public AbstractPumpCompany(boolean empty)
    {
        if (empty==true)
        {
            this.devices_vector.add(new EmptyPumpDevices());
        }
    }
    
    
    public Vector<PumpInterface> getDevices()
    {
        return this.devices_vector;
    }
    
    
    public void addDevice(PumpInterface md)
    {
        md.setPumpCompany(this);
    	this.devices.put(""+md.getName(), md);
    	this.devices_vector.add(md);
    }

    
    public PumpInterface getDevice(String name)
    {
        if (this.devices.containsKey(name))
            return this.devices.get(name);
        else
            return null;
    }
    
    
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