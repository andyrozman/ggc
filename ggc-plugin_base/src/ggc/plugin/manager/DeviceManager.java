package ggc.plugin.manager; 

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import java.util.Hashtable;
import java.util.Vector;


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
 *  Filename:      DeviceManager  
 *  Description:   Super class for all device managers.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class DeviceManager
{

    protected Hashtable<String,AbstractDeviceCompany> companies_ht = new Hashtable<String,AbstractDeviceCompany>(); 
    protected Vector<AbstractDeviceCompany> companies = new Vector<AbstractDeviceCompany>(); 
    protected Vector<DeviceInterface> supported_devices = new Vector<DeviceInterface>();

    /**
     * Constructor 
     */
    public DeviceManager()
    {
    	this.loadDeviceCompanies();
    	this.loadSupportedDevices();
    }

    
    /**
     * Load devices companies
     */
    public abstract void loadDeviceCompanies();
    
    
    /**
     * Load Supported Devices
     */
    public abstract void loadSupportedDevices();
    
    
    /**
     * Get Companies
     * @return
     */
    public Vector<AbstractDeviceCompany> getCompanies()
    {
        return this.companies;
    }
   
    
    /**
     * Get Supported Devices
     * @return
     */
    public Vector<? extends DeviceInterface> getSupportedDevices()
    {
        return this.supported_devices;
    }


    
    /**
     * @param company
     */
    public void addDeviceCompany(AbstractDeviceCompany company)
    {
        this.companies.add(company);
        this.companies_ht.put(company.getName(), company);
    }
    
    
    
    /**
     * Gets the name
     * @param group 
     * @param device 
     * @return Returns a String
     */
    public DeviceInterface getDevice(String group, String device)
    {
        AbstractDeviceCompany cmp = getCompany(group);
        
        if (cmp==null)
        {
            System.out.println("Company not found !");
            System.out.println("companies_nt: " + this.companies_ht);
            return null;
        }
        
        return cmp.getDevice(device);
    }
    
    
    /**
     * Get Device Class name
     * 
     * @param group
     * @param device
     * @return
     */
    public String getDeviceClassName(String group, String device)
    {
        return getDevice(group, device).getDeviceClassName();
    }
    
    
    /**
     * Get Company
     * 
     * @param name
     * @return
     */
    public AbstractDeviceCompany getCompany(String name)
    {
        if (this.companies_ht.containsKey(name))
        {
            return this.companies_ht.get(name);
        }
        else
            return null;
        
    }
    
    

}
