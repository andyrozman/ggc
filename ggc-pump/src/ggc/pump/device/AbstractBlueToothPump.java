package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.BlueToothProtocol;
import ggc.pump.util.DataAccessPump;

import javax.comm.SerialPortEvent;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.file.FileReaderContext;

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
 *  Filename:      AbstractBlueToothPump  
 *  Description:   Abstract implementation of pump, implementing with BlueTooth protocol
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractBlueToothPump extends BlueToothProtocol implements PumpInterface, SelectableInterface
{

    protected int m_status = 0;
    protected I18nControlAbstract ic = null; //DataAccessPump.getInstance().getI18nControlInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected String device_name = "Undefined";
    protected OutputWriter output_writer;
    
    AbstractDeviceCompany pump_company = null;
    boolean communication_established = false;
    

    /**
     * Constructor
     */
    public AbstractBlueToothPump()
    {
        super();
        m_da = DataAccessPump.getInstance();
        ic = m_da.getI18nControlInstance();
    }

    
    /**
     * Constructor
     * 
     * @param i2
     * @param i3
     * @param i4
     * @param i5
     */
    public AbstractBlueToothPump(int i2, int i3, int i4, int i5)
    {
        super();
        //m_da = DataAccessPump.getInstance();
        //ic = m_da.getI18nControlInstance();
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractBlueToothPump(AbstractDeviceCompany cmp)
    {
        super();
        //m_da = DataAccessPump.getInstance();
        //ic = m_da.getI18nControlInstance();

        this.setDeviceCompany(cmp);
        this.setPumpType(cmp.getName(), getName());
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
        
        DeviceIdentification di = new DeviceIdentification();
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
        //this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
        
        this.device_source_name = group + " " + device;
        
    }
    
    
    
//	this.m_device_index = device_index;
    
    
    /**
     * Serial Event
     * 
     * @param event 
     */
    public void serialEvent(SerialPortEvent event)
    {
    }

    /**
     * Dispose
     */
    public void dispose()
    {
        this.close();
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
     * Set Communication Settings
     */
    public void setCommunicationSettings(int baudrate, int databits,
    									 int stopbits, int parity,
    									 int flow_control, int event_type)
    {
    	super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
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
    
    
    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return this.communication_established;
    }
    
    
    /*
    public String getName()
    {
        return this.device_name;
    }*/
    
    
    String serial_port = null;
    
    /**
     * Set Serial Port
     * 
     * @param port
     * @throws Exception 
     */
    public void setSerialPort(String port) throws Exception
    {
        System.out.println("port (ASP): " + port);
        
    	this.serial_port = port;
    	
    	//try
    	{
    		this.setPort(port);
    	}
    	/*catch(PlugInBaseException ex)
    	{
    		System.out.println("No Such Port Ex: " + ex);
    		throw new PlugInBaseException(ex);
    	}*/
    	
    }
    
    
    
    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return this.serial_port;
    }
    
    
    
    
    /**
     * Get Serial Port
     * @return
     */
    public String getSerialPort()
    {
    	return this.serial_port;
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
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    //@Override
    public boolean open() throws PlugInBaseException
    {
        return (communication_established = super.open());
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //@Override
    public void close()
    {
        if (this.serialPort==null)
            return;
        
        this.serialPort.removeEventListener();
        this.serialPort.close();
        this.serialPort = null;
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

    float device_columns_width[] = { 0.25f, 0.25f, 0.3f, 0.1f, 0.1f };
    String device_columns[] = null;
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        if (device_columns==null)
        {
            this.device_columns = new String[5];
            device_columns[0] = ic.getMessage("DEVICE_COMPANY");
            device_columns[1] = ic.getMessage("DEVICE_DEVICE");
            device_columns[2] = ic.getMessage("DEVICE_CONNECTION");
            device_columns[3] = ic.getMessage("DEVICE_DOWNLOAD");
            device_columns[4] = ic.getMessage("DEVICE_SETTINGS");
        }
        
        return device_columns[num-1];
    }


    /**
     * Get Column Value
     */
    public String getColumnValue(int num)
    {
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

    
    String device_source_name;
    
    /**
     * Get Device Source Name
     * 
     * @return
     */
    public String getDeviceSourceName()
    {
        return device_source_name;
    }
    
    
    /** 
     * Get Download SupportType Configuration
     */
    public int getDownloadSupportTypeConfiguration()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }

    
    /**
     * Does this device support file download. Some devices have their native software, which offers export 
     * into some files (usually CSV files or even XML). We sometimes add support to download from such
     * files, and in some cases this is only download supported. 
     *  
     * @return
     */
    public boolean isFileDownloadSupported()
    {
        return false;
    }
    
    
    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    public FileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }
    
    
}
