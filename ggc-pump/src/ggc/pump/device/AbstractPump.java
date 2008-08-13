
package ggc.pump.device;


import ggc.pump.data.PumpValuesEntry;
import ggc.pump.manager.company.AbstractPumpCompany;
import ggc.pump.output.AbstractOutputWriter;
import ggc.pump.output.OutputWriter;
import ggc.pump.util.I18nControl;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;


public abstract class AbstractPump implements PumpInterface, SelectableInterface
{

    AbstractPumpCompany pump_company;

    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected ArrayList<PumpValuesEntry> data = null;
    
    protected OutputWriter m_output_writer = null;
    

    public AbstractPump()
    {
        super();
    }
    
    
    public AbstractPump(OutputWriter ow)
    {
        super();
        this.m_output_writer = ow;
    }


    
    
    boolean can_read_data = false; 
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
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


    public int getMeterIndex()
    {
        return 0;
    }

    public ImageIcon getIcon()
    {
        return null;
    }

    public String getName()
    {
        return "Generic device";
    }

    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    public int getTimeDifference()
    {
	//return this.m_time_difference;
        return 0;
    }


    /**
     * getInfo - returns Meter information
     */
    public String getInfo()
    {
        return "Generic Device, v0.1\nNo real device connected.";
    }


    /**
     * getStatus - get Status of meter
     */
    public int getStatus()
    {
        return m_status;
    }


    /**
     * isStatusOK - has Meter OK status
     */
    public boolean isStatusOK()
    {
        return (m_status == 0);
    }











    public void readDeviceData() throws PumpException
    {
    }











    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
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
     * canClearData - Can Meter class clear data from meter.
     * 
     * @return true if action is allowed
     */
    public boolean canClearData()
    {
        return this.can_clear_data;
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
         
            case 1:
                return this.getName();
                
            case 2:
                return this.getPumpCompany().getConnectionSamples();

            case 0:
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
    
    
    public AbstractPumpCompany getPumpCompany()
    {
        return this.pump_company;
    }

    
}
