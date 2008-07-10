package ggc.pump.device;


import ggc.pump.manager.PumpDevice;
import ggc.pump.manager.company.AbstractPumpCompany;
import ggc.pump.output.OutputWriter;
import ggc.pump.protocol.SerialProtocol;
import ggc.pump.util.I18nControl;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;


public abstract class AbstractSerialPump extends SerialProtocol implements PumpInterface, SelectableInterface
{

    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected String device_name = "Undefined";
    protected OutputWriter output_writer;
    
    AbstractPumpCompany pump_company = null;
    

    public AbstractSerialPump()
    {
        super();
    }

    
    public AbstractSerialPump(int i2, int i3, int i4, int i5)
    {
        super();
    }
    
    
    
//	this.m_device_index = device_index;
    
    
    public void serialEvent(SerialPortEvent event)
    {
    	
    }

    
    
    boolean can_read_data = false; 
	boolean can_read_partitial_data = false;
	boolean can_read_device_info = false;
	boolean can_read_device_configuration = false;
    
    
	
	
	
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
    

    public void setCommunicationSettings(int baudrate, int databits,
    									 int stopbits, int parity,
    									 int flow_control)
    {
    	super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control);
    }
    
    
    String meter_group = null;
    String meter_device = null;
    
    PumpDevice device_instance = null;
    
    
    public void setMeterType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification();
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
    
    
    String serial_port = null;
    
    public void setSerialPort(String port)
    {
    	this.serial_port = port;
    	
    	try
    	{
    		this.setPort(port);
    	}
    	catch(NoSuchPortException ex)
    	{
    		System.out.println("No Such Port Ex: " + ex);
    		
    	}
    	
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
    public boolean open() throws PumpException
    {
        return super.open();
	//return false;
        //return true;
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
    
    









    public void readDeviceData() throws PumpException
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

    /**
     * getName - Get Name of meter. 
     * Should be implemented by protocol class.
     */
    public String getName()
    {
    	if (this.device_instance==null)
    		return "Generic Serial Device";
    	else
    		return this.device_instance.name;
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by protocol class.
     */
    public ImageIcon getIcon()
    {
    	if (this.device_instance==null)
    		return null;
    	else
    		return m_da.getImageIcon(this.device_instance.picture); 
    	//this.device_instance.picture;
    }
    


    /**
     * getMeterIndex - Get Index of Meter 
     * Should be implemented by protocol class.
     */
    public int getMeterIndex()
    {
        return 0;
    }




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
                return this.getPumpCompany().getConnectionSamples();

            case 1:
            default:    
                return this.getPumpCompany().getName();
                
                
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

    
    
    

    public void setPumpCompany(AbstractPumpCompany company)
    {
        this.pump_company = company;
    }
    
    
    public AbstractPumpCompany getMeterCompany()
    {
        return this.pump_company;
    }




}
