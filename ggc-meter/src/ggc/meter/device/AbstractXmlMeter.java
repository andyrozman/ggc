package ggc.meter.device;

import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocol;

import java.util.ArrayList;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractXmlMeter extends XmlProtocol implements MeterInterface, SelectableInterface
{

    protected I18nControl ic = I18nControl.getInstance();

    protected String device_name = "Undefined";
    protected OutputWriter output_writer;
    
    AbstractDeviceCompany meter_company = null;
    
    String connection_port = "";

    public AbstractXmlMeter()
    {
        super();
    }


    public AbstractXmlMeter(AbstractDeviceCompany cmp)
    {
        this.setDeviceCompany(cmp);
    }
    
    
    boolean can_read_data = false; 
	boolean can_read_partitial_data = false;
	boolean can_read_device_info = false;
	boolean can_read_device_configuration = false;
    
    
    public void close() throws PlugInBaseException
    {
    }
	
	
    public void setDeviceAllowedActions(boolean can_read_data, 
    									boolean can_read_partitial_data,
    									boolean can_read_device_info,
    									boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data; 
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }
    

    
    String meter_group = null;
    String meter_device = null;
    
    //MeterDevice device_instance = null;
    
    
    public void setMeterType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification(DataAccessMeter.getInstance().getI18nControlInstance());
        di.company = group;
        di.device_selected = device;
        
        this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
    	//this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
    }
    
    /*
    public String getName()
    {
        return this.device_name;
    }*/
    
    
    
    
    
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
     */
    public void setConnectionPort(String con_port)
    {
        this.connection_port = con_port;
    }
    
    
    
    
    
    
    
    /*
    public GenericMeter(int meter_type, String portName)
    {

	super(meter_type,
	      9600, 
	      SerialPort.DATABITS_8, 
	      SerialPort.STOPBITS_1, 
	      SerialPort.PARITY_NONE);

	data = new ArrayList<DailyValuesRow>();

	try
	{
	    this.setPort(portName);

	    if (!this.open())
	    {
		this.m_status = 1;
	    }
	}
	catch(Exception ex)
	{
	    System.out.println("AscensiaMeter -> Error adding listener: " + ex);
	    ex.printStackTrace();
	}
    }
*/



    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return "";
    }
    
    
    
    //************************************************
    //***       Device Implemented methods         ***
    //************************************************
    

    /** 
     * clearDeviceData - Clear data from device 
     */
    public void clearDeviceData()
    {
    }
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
    }
    
    
    /**
     * getDeviceConfiguration - return device configuration
     * @return
     */
    public ArrayList<String> getDeviceConfiguration()
    {
    	return new ArrayList<String>();
    }
    
    











    public void readDeviceData() throws PlugInBaseException
    {
    }





    
    //************************************************
    //***        Available Functionality           ***
    //************************************************


    
    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData()
    {
    	return this.can_read_data;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData()
    {
    	return this.can_read_partitial_data;
    }


    
    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo()
    {
    	return this.can_read_device_info;
    }
    
    
    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration()
    {
    	return this.can_read_device_configuration;
    }
    
    

    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************




    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }



    
    /* 
     * compareTo
     */
    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /* 
     * getColumnCount
     */
    public int getColumnCount()
    {
        return 3;
    }


    String device_columns[] = { ic.getMessage("METER_COMPANY"), ic.getMessage("METER_DEVICE"), ic.getMessage("DEVICE_CONNECTION") }; 
    
    /* 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return device_columns[num-1];
    }


    /* 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        switch(num)
        {
         
            case 2:
                return this.getName();
                
            case 3:
                return this.getDeviceCompany().getConnectionSamples();

            case 1:
            default: 
            {
                //System.out.println("name: " + this.getName());
                return this.getDeviceCompany().getName();
            }
                
                
        }
    }


    /* 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    /* 
     * getColumnWidth
     */
    public int getColumnWidth(int num, int width)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /* 
     * getItemId
     */
    public long getItemId()
    {
        return 0;
    }


    /* 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /* 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /* 
     * setColumnSorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /* 
     * setSearchContext
     */
    public void setSearchContext()
    {
    }

    
    
    
    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.meter_company = company;
    }
    
    
    /**
     * getDeviceCompany - get Company for device
     * 
     * @param company
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.meter_company;
    }



}
