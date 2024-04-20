package ggc.plugin.manager;

import java.util.*;

import com.atech.graphics.dialogs.selector.SelectableInterface;

import com.atech.graphics.dialogs.selector.SelectableInterfaceV2;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.util.DataAccessPlugInBase;

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

    protected DataAccessPlugInBase dataAccessPlugInBase;
    protected Hashtable<String, AbstractDeviceCompany> companies_ht = new Hashtable<String, AbstractDeviceCompany>();
    protected Vector<AbstractDeviceCompany> companies = new Vector<AbstractDeviceCompany>();
    protected List<SelectableInterfaceV2> supportedDevicesForSelector = new ArrayList<>();

    protected HashMap<String, DeviceInterface> supportedDevicesV1 = new HashMap<String, DeviceInterface>();
    protected HashMap<String, DeviceInstanceWithHandler> supportedDevicesV2 = new HashMap<String, DeviceInstanceWithHandler>();


    /**
     * Constructor 
     */
    public DeviceManager(DataAccessPlugInBase dataAccessPlugInBase)
    {
        this.dataAccessPlugInBase = dataAccessPlugInBase;
        this.loadDeviceCompanies();
        this.loadSupportedDevices();
        this.loadDeviceInstancesV2();
    }


    protected abstract void loadDeviceInstancesV2();


    /**
     * Load devices companies
     */
    public abstract void loadDeviceCompanies();


    public void loadSupportedDevices()
    {
        for (AbstractDeviceCompany pdc : this.companies)
        {
            for (DeviceInterface di : pdc.getDevices())
            {
                if (DeviceImplementationStatus.isSupportedDevice(pdc.getImplementationStatus()))
                {
                    this.supportedDevicesV1.put(pdc.getName() + "_" + di.getName(), di);
                    this.supportedDevicesForSelector.add(di);
                }
            }
        }
    }


    public int getLoadedDevicesV1Count()
    {
        int count = 0;

        for (AbstractDeviceCompany pdc : this.companies)
        {
            count += pdc.getDevices().size();
        }

        return count;
    }


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
    public List<SelectableInterfaceV2> getSupportedDevicesForSelector()
    {
        return this.supportedDevicesForSelector;
    }


    /**
     * @param company
     */
    public void addDeviceCompany(AbstractDeviceCompany company)
    {
        this.companies.add(company);
        this.companies_ht.put(company.getShortName(), company);
    }


    /**
     * Gets the name
     * @param company
     * @param device 
     * @return Returns a String
     */
    public DeviceInterface getDeviceV1(String company, String device)
    {
        // System.out.println("Company: " + company + ", device=" + device);

        String key = company + "_" + device;

        if (this.supportedDevicesV1.containsKey(key))
        {
            return this.supportedDevicesV1.get(key);
        }
        else
        {
            return null;
        }

        // AbstractDeviceCompany cmp = getCompany(group);
        //
        // if (cmp == null)
        // // System.out.println("Company not found !");h
        // // System.out.println("companies_nt: " + this.companies_ht);
        // return null;
        //
        // return cmp.getDevice(device);
    }


    public DeviceInstanceWithHandler getDeviceV2(String company, String device)
    {
        // System.out.println("Company: " + company + ", device=" + device);

        String key = company + "_" + device;

        // System.out.print(this.supportedDevicesV2);

        if (this.supportedDevicesV2.containsKey(key))
        {
            return this.supportedDevicesV2.get(key);
        }
        else
        {
            return null;
        }
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
        return getDeviceV1(group, device).getDeviceClassName();
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
            return this.companies_ht.get(name);
        else
            return null;

    }

}
