package ggc.meter.device;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocol;

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
 *  Filename:     AbstractXmlMeter
 *  Description:  This abstract class for all meters using Xml interface
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractXmlMeter extends XmlProtocol implements MeterInterface //, SelectableInterface
{


    protected String device_name = "Undefined";
    String connection_port = "";
    protected boolean communication_established = false;

    /**
     * Constructor
     * @param ow 
     */
    public AbstractXmlMeter(OutputWriter ow)
    {
        super(DataAccessMeter.getInstance(), ow);
        m_da = DataAccessMeter.getInstance();
        ic = m_da.getI18nControlInstance();
    }


    /**
     * Constructor
     * @param cmp
     */
    public AbstractXmlMeter(AbstractDeviceCompany cmp)
    {
        super(DataAccessMeter.getInstance());
        this.setDeviceCompany(cmp);
        this.setMeterType(cmp.getName(), getName());
    }
    
    
    /** 
     * close
     */
    public void close() throws PlugInBaseException
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
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification();
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
    	//this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
        
        this.device_source_name = group + " " + device;
    }
    
    
    
    public void dispose()
    {
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
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return this.communication_established;
    }

    

    

    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_FROM_DEVICE;
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
