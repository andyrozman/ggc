/**
 * 
 */
package ggc.meter.device.pseudo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.atech.utils.ATechDate;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractMeter;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterException;
import ggc.meter.output.OutputWriter;
import ggc.meter.protocol.ConnectionProtocols;


/**
 * @author innominate
 *
 */
public class EZManagerDBMeter extends AbstractMeter
{
    
    private OutputWriter m_writer;
    
    private String m_fileName;
    
    public EZManagerDBMeter()
    {
        super();
    }
    
    
    public EZManagerDBMeter(String portName, OutputWriter writer)
    {        
        super();
        m_writer = writer;
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close() throws MeterException
    {
        //dont need to do anything here
        return;
    }

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return "This is a pseudo meter that reads BG values from an Animas Ez" +
        "Manager Database.";
    }

    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        // TODO: im not to sure what this is supposed to be -Nate
        return 0;
    }

    /**
     *
     */
    public String getConnectionPort()
    {
        return "No port";
    }

    /**
     * 
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_NONE;
    }

    /**
     * 
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.pseudo.EZManagerDBMeter";
    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        DeviceIdentification toRet = new DeviceIdentification();  
        toRet.company = "Animas";
        toRet.device_family = "n/a";
        toRet.device_hardware_version = "n/a";
        toRet.device_identified = "? Identified ?";
        toRet.device_selected = "? Selected ?";
        toRet.device_serial_number = "n/a";
        toRet.device_software_version = "v 5.2.1 (Build 32)";        
        return toRet;
    }

    /**
     * 
     */
    public String getIconName()
    {
        return "no_meter.gif";
    }

    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus()
    {
        // TODO: ???? -Nate
        return 0;
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "Point the file browser \n to your EZManager Database file\n" +
        "It should be named \"ezmd.mdb\"";
    }

    /**
     * 
     */
    public int getMaxMemoryRecords()
    {
        return Integer.MAX_VALUE;
    }

    /**
     * 
     */
    public int getMeterId()
    {
        // TODO: ????? -Nate
        return 0;
    }

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws MeterException
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "MDB Files", "mdb");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            m_fileName = chooser.getSelectedFile().getAbsolutePath();
           return true;
        }
        else
        {
            return false;        
        }
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
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws MeterException
    {
        ArrayList<MeterValuesEntry> data = getDataFull();
        
        if (data == null)
        {
            
        }
        else
        {
        
            for (MeterValuesEntry entry : data)
            {
                m_writer.writeBGData(entry);
            }
        }
    }

    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws MeterException
    {
        // TODO Auto-generated method stub

    }

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws MeterException
    {
        // TODO Auto-generated method stub

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
    
    
    /**
     * getDataFull - get all data from Meter
     * This data should be read from meter, and is used in Meter GUI
     */
    public ArrayList<MeterValuesEntry> getDataFull() //throws MeterException
    {
        if (m_fileName == null)
        {
            try 
            {
                open();
            }
            catch(MeterException e)
            {
                return null;
            }
            
        }
        
        
        
        ArrayList<MeterValuesEntry> toRet = new ArrayList<MeterValuesEntry>();
        
        try {
            Class.forName("mdbtools.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection("jdbc:mdbtools:" + m_fileName);            
            
            PreparedStatement bgValsStatement = conn.prepareStatement("Select * From bgLog");
            ResultSet bgVals = bgValsStatement.executeQuery();
            while (bgVals.next())
            {         
              String bg = bgVals.getString("bg");
              
              int day = Integer.parseInt( bgVals.getString("day") );
              int month = Integer.parseInt( bgVals.getString("month") );
              int year = Integer.parseInt( bgVals.getString("year") );
              int hours = Integer.parseInt( bgVals.getString("hours") );
              int mins = Integer.parseInt( bgVals.getString("minutes") );
             
              
              long dateTime = 0;
              
              dateTime += year*100000000L; 
              dateTime += month*1000000L;
              dateTime += day*10000L;
              dateTime += hours*100L;
              dateTime += mins;
              
              ATechDate date = new ATechDate(dateTime);              
              
              MeterValuesEntry newEntry = new MeterValuesEntry();
              newEntry.setBgValue(bg);
              newEntry.setDateTime(date);
              
              toRet.add(newEntry);
                      
            }
            bgVals.close();
            conn.close();
        }
            catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
        
        return toRet;
    }


    /**
     * getData - get data for specified time
     * This data should be read from meter and preprocessed, and is used in Meter GUI
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to) //throws MeterException
    {
        return null;
    }

    

}