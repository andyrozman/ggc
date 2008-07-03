/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.device;



/**
 * @author andyrozman
 *
 * 
 */


import ggc.meter.data.MeterValuesEntry;
import ggc.meter.protocol.ConnectionProtocols;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.util.ArrayList;

import javax.swing.ImageIcon;


public class DummyMeter extends AbstractMeter //implements MeterInterface
{

    DataAccessMeter m_da = DataAccessMeter.getInstance();
    I18nControl m_ic = m_da.getI18nInstance();

    public int m_meter_index = 0;
    

    
    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws MeterException
    {
    }
    
    

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open()
    {
        return true;
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        return;
    }


    public static final int LOG_DEBUG = 1;
    public static final int LOG_INFO = 2;
    public static final int LOG_WARN = 3;
    public static final int LOG_ERROR = 4;
    
    public void writeLog(int problem, String text)
    {
        System.out.println("Dummy -> " + text);
    }


    public String getInfo() //throws MeterException
    {
        writeLog(LOG_DEBUG, "getVersion() - Start");
        writeLog(LOG_DEBUG, "getVersion() - End");
        return "Dummy Meter\n" +
               "v0.1\n" +
               m_ic.getMessage("DUMMY_INFO_TEXT");
    }

    

    public int getTimeDifference() // throws MeterException
    {
        writeLog(LOG_DEBUG, "getTimeDifference() - Start");
        writeLog(LOG_DEBUG, "getTimeDifference() - End");
        return 0;
    }


    public int getStatus()
    {
        return 0;
    }
    
    public boolean isStatusOK()
    {
        return true;
    }



    // Internal methods

/*
    public String getName()
    {
        return "Dummy Meter";
    }
*/
    public int getMeterIndex()
    {
        return m_meter_index;
    }

/*    
    public ImageIcon getIcon()
    {
        return m_da.getMeterManager().getMeterImage(1); //m_meter_index);
    }
*/
    /*
     * 
     * Dupkicate
    public ArrayList<MeterValuesEntry> getDataFull()
    {
        writeLog(LOG_DEBUG, "getDataFull() - Start");
        ArrayList<MeterValuesEntry> al = new ArrayList<MeterValuesEntry>();
        writeLog(LOG_DEBUG, "getDataFull() - End");
        return al;
    }

    public ArrayList<MeterValuesEntry> getData(int from, int to)
    {
        writeLog(LOG_DEBUG, "getData() - Start");
        ArrayList<MeterValuesEntry> al = new ArrayList<MeterValuesEntry>();
        writeLog(LOG_DEBUG, "getData() - End");
        return al;
    }
*/

    public void loadInitialData()
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceData()
    {
    }





    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /**
     * canReadData - Can Meter Class read data from device
     */
    public boolean canReadData()
    {
        return false;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     */
    public boolean canReadPartitialData()
    {
        return false;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     */
    public boolean canClearData()
    {
        return false;
    }



    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }



    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "name";
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcon
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return null;
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return 0;
    }

    
    /**
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * 
     * @return global id of meter
     */
    public int getGlobalMeterId()
    {
        return 0;
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 0;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }


    public String getDeviceClassName()
    {
        return "ggc.meter.device.DummyMeter";
    }

    
    
    
    //************************************************
    //***       Device Implemented methods         ***
    //************************************************
    

    /** 
     * clearDeviceData - Clear data from device 
     */
    public void clearDeviceData() throws MeterException
    {
    }
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo() //throws MeterException
    {
        return null;
    }
    
    
    /**
     * getDeviceConfiguration - return device configuration
     * @return
     */
    public ArrayList<String> getDeviceConfiguration() throws MeterException
    {
        return null;
    }
    
    
    /**
     * getDataFull - get all data from Meter
     * This data should be read from meter, and is used in Meter GUI
     */
    public ArrayList<MeterValuesEntry> getDataFull() //throws MeterException
    {
        return null;
    }


    /**
     * getData - get data for specified time
     * This data should be read from meter and preprocessed, and is used in Meter GUI
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to) //throws MeterException
    {
        return null;
    }
    
    
    
    public String getConnectionPort()
    {
        return "No port";
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_NONE;
    }
    
    
    public int getMaxMemoryRecords()
    {
        return 1;
    }
    
 
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws MeterException
    {
        
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    public void readConfiguration() throws MeterException
    {
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws MeterException
    {
    }
    
    
    /**
     * setDeviceAllowedActions - sets actions which are allowed by implementation
     *   of MeterInterface (actually of GenericMeterXXXXX classes)
     *   
     * @param can_read_data
     * @param can_read_partitial_data
     * @param can_read_device_info
     * @param can_read_device_configuration
     */
    public void setDeviceAllowedActions(boolean can_read_data, 
                                        boolean can_read_partitial_data,
                                        boolean can_read_device_info,
                                        boolean can_read_device_configuration)
    {
    }
    
    
}