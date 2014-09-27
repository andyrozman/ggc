package ggc.meter.device;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.manager.company.AbstractDeviceCompany;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     AbstractOtherMeter
 *  Description:  Abstract class for Meters using other protocols
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractOtherMeter extends AbstractMeter /**extends XmlProtocol*/
// implements MeterInterface, SelectableInterface
{

    protected String device_name = "Undefined";
    String connection_port = "";

    /**
     * Constructor
     */
    public AbstractOtherMeter()
    {
        super();
        m_da = DataAccessMeter.getInstance();
        ic = m_da.getI18nControlInstance();
    }

    /**
     * Constructor
     * @param cmp
     */
    public AbstractOtherMeter(AbstractDeviceCompany cmp)
    {
        super();
        m_da = DataAccessMeter.getInstance();
        ic = m_da.getI18nControlInstance();
        this.setDeviceCompany(cmp);
        this.setMeterType(cmp.getName(), getName());
    }

    String meter_group = null;
    String meter_device = null;

    /**
     * Set Meter type
     * 
     * @param group
     * @param device
     */
    @Override
    public void setMeterType(String group, String device)
    {
        this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(m_da.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.output_writer != null)
        {
            this.output_writer.setDeviceIdentification(di);
            // this.output_writer.
            // this.device_instance =
            // MeterManager.getInstance().getMeterDevice(group, device);
        }

        this.device_source_name = group + " " + device;
    }

    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return this.connection_port;
    }

    /**
     * setConnectionPort - connection port data
     * 
     * @param con_port 
     */
    public void setConnectionPort(String con_port)
    {
        this.connection_port = con_port;
    }

    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    @Override
    public boolean isReadableDevice()
    {
        return false;
    }

    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return true;
    }

}
