package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocol;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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


public abstract class AbstractXmlPump extends XmlProtocol implements PumpInterface, SelectableInterface
{

    protected I18nControl ic = I18nControl.getInstance();

    protected String device_name = "Undefined";
    protected OutputWriter output_writer;
    
    AbstractDeviceCompany pump_company = null;
    
    String connection_port = "";

    /**
     * Constructor
     */
    public AbstractXmlPump()
    {
        super();
    }

    
    boolean can_read_data = false; 
	boolean can_read_partitial_data = false;
	boolean can_read_device_info = false;
	boolean can_read_device_configuration = false;
    
    
	
	
	
    /** 
     * Set Device Allowed Actions
     */
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

    
    /**
     * Set Pump Type
     * 
     * @param group
     * @param device
     */
    public void setPumpType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification(ic);
        di.company = group;
        di.device_selected = device;
        
        this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
        //this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
    }
    
    

    /*
    String meter_group = null;
    String meter_device = null;
    
    PumpInterface device_instance = null;
    
    
    public void setMeterType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification(ic);
        di.company = group;
        di.device_selected = device;
        
        this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
    	//this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
    }*/
    
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
     * @param con_port 
     * 
     */
    public void setConnectionPort(String con_port)
    {
        this.connection_port = con_port;
    }
    
    
    /**
     * Dispose
     */
    public void dispose()
    {
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
     * Is Device Communicating
     */
    public boolean isDeviceCommunicating()
    {
        return true;
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
    //***                    Test                  ***
    //************************************************

    /** 
     * test
     */
    public void test()
    {
    }



    
    /** 
     * Compare To
     */
    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * Get Column Count
     */
    public int getColumnCount()
    {
        return 5;
    }


    
    String device_columns[] = { ic.getMessage("DEVICE_COMPANY"), ic.getMessage("DEVICE_DEVICE"), ic.getMessage("DEVICE_CONNECTION"), ic.getMessage("DEVICE_DOWNLOAD"), ic.getMessage("DEVICE_SETTINGS") }; 
    float device_columns_width[] = { 0.3f, 0.2f, 0.3f, 0.1f, 0.1f };
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return device_columns[num-1];
    }


    /** 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        //System.out.println("Num: " + num);
        switch(num)
        {
            case 1:
                return this.getDeviceCompany().getName();

            case 2:
                return this.getName();
                
            case 3:
                return this.getDeviceCompany().getConnectionSamples();

            case 4:
                if (this.getDownloadSupportType()==DownloadSupportType.DOWNLOAD_YES)
                    return DataAccessPump.getInstance().getYesNoOption(true);
                else
                    return DataAccessPump.getInstance().getYesNoOption(false);
                
            case 5:
                return DataAccessPump.getInstance().getYesNoOption(false);
                
                
            default:                 
                return "N/A: " + num;
        }
    }


    /** 
     * Get Column Value Object
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }

    /** 
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {
        return (int)(this.device_columns_width[num-1] * width);
    }



    /** 
     * Get Item Id
     */
    public long getItemId()
    {
        return 0;
    }


    /** 
     * Get Short Description
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /** 
     * Is Found
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /** 
     * Is Found
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /** 
     * Is Found
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /** 
     * Set Column Sorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /** 
     * Set Search Context
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
        this.pump_company = company;
    }
    
    
    /**
     * getDeviceCompany - get Company for device
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.pump_company;
    }


    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice()
    {
        return true;
    }


    /** 
     * Get Download Support Type
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }
    
    
    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        return "TYPE=Unit;STEP=0.1";
    }
    
    
}
