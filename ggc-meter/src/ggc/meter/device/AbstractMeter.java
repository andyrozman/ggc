package ggc.meter.device;


import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
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
 *  Filename:     AbstractMeter
 *  Description:  Abstract Meter for creating MeterDevices
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractMeter extends DeviceAbstract //implements MeterInterface, SelectableInterface
{

    //AbstractDeviceCompany meter_company;

    //protected int m_status = 0;
    //protected I18nControlAbstract ic = DataAccessMeter.getInstance().getI18nControlInstance();
    //protected OutputWriter output_writer;
    //protected ArrayList<MeterValuesEntry> data = null;
    //protected DataAccessMeter m_da = null;

    /**
     * Constructor
     */
    public AbstractMeter()
    {
        super(DataAccessMeter.getInstance());
        //m_da = DataAccessMeter.getInstance();
    }


    /**
     * Constructor
     * @param cmp
     */
    public AbstractMeter(AbstractDeviceCompany cmp)
    {
        super(DataAccessMeter.getInstance());
        this.setDeviceCompany(cmp);
        this.setMeterType(cmp.getName(), getName());
    }
    
    
    
    
    //boolean can_read_data = false; 
    //boolean can_read_partitial_data = false;
    //boolean can_clear_data = false;
    //boolean can_read_device_info = false;
    //boolean can_read_device_configuration = false;
    
    
    /**
     * Dispose instance
     * 
     * @see ggc.plugin.device.DeviceInterface#dispose()
     */
    public void dispose()
    {
    }
    
    
    
    
    
    /**
     * Set Meter type
     * 
     * @param group
     * @param device
     */
    public void setMeterType(String group, String device)
    {
        //this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification(m_da.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
        //this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
        
        this.device_source_name = group + " " + device;
    }



    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /** 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }


    
    /**
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but 
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display. 
     * @return
     */
    public int getInterfaceTypeForMeter()
    {
        return MeterInterface.METER_INTERFACE_SIMPLE;
    }
    
    
    
}
