package ggc.plugin.manager.company;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.manager.EmptyMgrDevices;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     AbstractDeviceCompany
 *  Description:  Abstract Device Company...
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractDeviceCompany implements DeviceCompanyInterface // ,
                                                                              // SelectableInterface
{

    // protected I18nControlAbstract m_ic = null; //I18nControl.getInstance();

    protected String id = "";
    protected String name = "";
    protected int index = 0;

    Hashtable<String, DeviceInterface> devices = new Hashtable<String, DeviceInterface>();
    Vector<DeviceInterface> devices_vector = new Vector<DeviceInterface>();

    protected int company_id = 0;
    protected String company_name = "";
    protected String company_description = "";
    protected int company_implementation_status = 0;
    protected String company_short_name = "";

    /**
     * Constructor
     * 
     * @param ic
     */
    public AbstractDeviceCompany(/* I18nControlAbstract ic */)
    {
        // this.m_ic = ic;
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param empty
     * @param da 
     */
    public AbstractDeviceCompany(boolean empty, DataAccessPlugInBase da)
    {
        this();

        if (empty == true)
        {
            this.devices_vector.add(new EmptyMgrDevices(da));
        }
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param empty
     * @param company_id_ 
     * @param company_name 
     * @param short_company_name 
     * @param company_desc 
     * @param implementation_status 
     * @param da 
     */
    public AbstractDeviceCompany(boolean empty, int company_id_, String company_name, String short_company_name,
            String company_desc, int implementation_status, DataAccessPlugInBase da)
    {
        this(empty, da);

        this.company_id = company_id_;
        this.company_name = company_name;
        this.company_short_name = short_company_name;
        this.company_implementation_status = implementation_status;
        this.company_description = company_desc;
    }

    /**
     * Get Devices
     * 
     * @return vector of devices (Vector<DeviceInterface>)
     */
    public Vector<DeviceInterface> getDevices()
    {
        return this.devices_vector;
    }

    /**
     * Add Device
     * 
     * @param md DeviceInterface instance
     */
    public void addDevice(DeviceInterface md)
    {
        this.devices.put("" + md.getName(), md);
        this.devices_vector.add(md);
    }

    /**
     * Get Device
     * 
     * @param _name name of device
     * @return DeviceInterface instance
     */
    public DeviceInterface getDevice(String _name)
    {
        if (this.devices.containsKey(_name))
            return this.devices.get(_name);
        else
            return null;
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Device Company [name=" + this.getName() + ",id=" + this.getCompanyId() + "]";
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

    /**
     * getName - Get Name of company. 
     * 
     * @see ggc.plugin.manager.company.DeviceCompanyInterface#getName()
     * @return name of device
     */
    public String getName()
    {
        return this.company_name;
    }

    /**
     * getName - Get Name of company. 
     * 
     * @see ggc.plugin.manager.company.DeviceCompanyInterface#getName()
     * @return name of device
     */
    public String getShortName()
    {
        if (this.company_short_name.length() == 0)
            return this.getName();
        else
            return this.company_short_name;
    }

    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return this.company_id;
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
        return this.company_description;
    }

    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return this.company_implementation_status;
    }

}
