/**
 * 
 */
package ggc.pump.device.animas;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.device.AbstractPump;
import ggc.pump.device.PumpException;
import ggc.pump.protocol.ConnectionProtocols;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * @author innominate
 *
 */
public abstract class EZManagerDb extends AbstractPump
{
    
    //private OutputWriter m_writer;
    
    @SuppressWarnings("unused")
    private String m_fileName;
    
    
    public static final int ANIMAS_COMPANY                = 4;
    
    public static final int PUMP_ANIMAS_X1               = 40001;
    public static final int PUMP_ANIMAS_TEST             = 40099;
    
    
    
    
    public EZManagerDb()
    {
        super();
    }
    
    
    public EZManagerDb(String db_path, OutputWriter writer)
    {        
        super(writer);
        this.m_fileName = db_path;
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close() throws PumpException
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
        return "ggc.pump.device.animas.EZManagerDb";
    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        DeviceIdentification toRet = new DeviceIdentification(ic);  
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
        return DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS;
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
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws PumpException
    {
        return true;
        /*
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
        }*/
    }

    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    public void readConfiguration() throws PumpException
    {
    }

    
    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws PumpException
    {
        /*
        ArrayList<PumpValuesEntry> data = getDataFull();
        
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
        */
        
        // Nate: Writing should be done as soon as it happens, not 
    }

    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PumpException
    {
        // TODO Auto-generated method stub

    }

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws PumpException
    {
        // TODO Auto-generated method stub

    }
    
    
    
    
    /**
     * getDataFull - get all data from Meter
     * This data should be read from meter, and is used in Meter GUI
     */
    
    // FIXME Not used
    public ArrayList<PumpValuesEntryExt> getDataFull() //throws MeterException
    {
        
        return null;
        
        /*
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
        
        return toRet; */
        
        
    }


    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }





    

}
