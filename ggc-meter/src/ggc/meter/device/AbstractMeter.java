/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.device;


import ggc.meter.data.MeterValuesEntry;
import ggc.meter.util.I18nControl;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;


public abstract class AbstractMeter implements MeterInterface, SelectableInterface
{

    AbstractDeviceCompany meter_company;

    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected ArrayList<MeterValuesEntry> data = null;
    

    public AbstractMeter()
    {
        super();
    }


    boolean can_read_data = false; 
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;
    
    
    
    
    
    public void setDeviceAllowedActions(boolean can_read_data, 
                                        boolean can_read_partitial_data,
                                        boolean can_clear_data,
                                        boolean can_read_device_info,
                                        boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data; 
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_clear_data = can_clear_data;
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



    /**
     * getDataFull - get all data from Meter
     */
    public ArrayList<MeterValuesEntry> getDataFull()
    {
        //return this.data;
    	return null;
    }


    /**
     * getData - get data for specified time
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to)
    {
    	/*
	ArrayList<DailyValuesRow> out = new ArrayList<DailyValuesRow>();

	for (int i=0; i<this.data.size(); i++)
	{
	    DailyValuesRow dwr = this.data.get(i);

	    if ((dwr.getDateTime() > from) && (dwr.getDateTime() < to))
	    {
		out.add(dwr);
	    }
	}

	return out; */
    	return null;
    }







    public void readDeviceData() throws PlugInBaseException
    {
    }




    //************************************************
    //***          Process Meter Data              ***
    //************************************************


    /**
     * processMeterDataMain - this is main method for processing data. It should be called on all data received, and 
     * from here it should be sent to other process* methods. This methods are meant to be used, but don't have to 
     * be used if we have other ways to get data for methods needed (methods marked as used in Meter GUI)
     */
    public void processMeterData(String data)
    {
    }

    /**
     * processMeterIdentification - this should be used to process identification of meter and versions of firmware.
     */
    public void processMeterIdentification(String data)
    {
    }

    /**
     * processMeterTime - this should be used to process time and date of meter
     */
    public void processMeterTime(String data)
    {
    }

    /**
     * processMeterBGEntry - this should be used to process BG data from meter
     */
    public void processMeterBGEntry(String data)
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
                return this.getDeviceCompany().getConnectionSamples();

            case 0:
            default:    
                return this.getDeviceCompany().getName();
                
                
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
