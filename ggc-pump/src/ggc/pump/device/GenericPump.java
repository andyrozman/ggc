/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.pump.device;


import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.output.OutputWriter;
import ggc.pump.util.I18nControl;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;


public class GenericPump extends AbstractPump //implements PumpInterface 
{

    protected I18nControl ic = I18nControl.getInstance();

    protected ArrayList<PumpValuesEntry> data = null;


    public GenericPump(OutputWriter ow)
    {
        super(ow);
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
    
    
    
    
    
    public boolean open()
    {
    	return false;
    	
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
/*    public boolean open() throws MeterException
    {
        return super.open();
	//return false;
        //return true;
    }
*/

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //@Override
    public void close()
    {
        return;
    }


    public int getPumpIndex()
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

    public String getPort()
    {
    	return "None";
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
     * getDeviceConfiguration - return device configuration
     * @return
     */
    public ArrayList<String> getDeviceConfiguration()
    {
    	return new ArrayList<String>();
    }
    
    
    




    public void readDeviceData() throws PumpException
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





    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getCompanyId()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public DeviceIdentification getDeviceInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public AbstractDeviceCompany getDeviceCompany()
    {
        // TODO Auto-generated method stub
        return null;
    }




    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public void readConfiguration() throws PumpException
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceDataFull() throws PumpException
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceDataPartitial() throws PumpException
    {
        // TODO Auto-generated method stub
        
    }





    public void readInfo() throws PumpException
    {
        // TODO Auto-generated method stub
        
    }





    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
        // TODO Auto-generated method stub
        
    }





    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        // TODO Auto-generated method stub
        
    }



    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }
    
    
    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String,Integer> getAlarmMappings()
    {
        return null;
    }
    
    
    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String,Integer> getEventMappings()
    {
        return null;
    }










    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    
    




}
