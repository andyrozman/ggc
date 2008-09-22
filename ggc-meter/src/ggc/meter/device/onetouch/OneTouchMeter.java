package ggc.meter.device.onetouch;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.device.MeterException;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPort;

import java.util.ArrayList;

import com.atech.utils.TimeZoneUtil;


public abstract class OneTouchMeter extends AbstractSerialMeter
{
    
    
    protected ArrayList<MeterValuesEntry> data = null;
    protected OutputWriter m_output_writer;
    public TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    public int meter_type = 20000;

    public OneTouchMeter(int meter_type, String portName, OutputWriter writer)
    {
        //super(portName, 9600, SerialConfig.LN_8BITS, SerialConfig.ST_1BITS, SerialConfig.PY_NONE);

//      int s= SerialConfig.LN_8BITS; 
      
        super(DataAccessMeter.getInstance());
        
        this.meter_type = meter_type;
        
        /*
        super( 
              9600,
              //19200,
              SerialPort.DATABITS_8, 
              SerialPort.STOPBITS_1, 
              SerialPort.PARITY_NONE,
              SerialProtocol.SERIAL_EVENT_NONE);
    */
        this.setCommunicationSettings( 
                  9600,
                  SerialPort.DATABITS_8, 
                  SerialPort.STOPBITS_1, 
                  SerialPort.PARITY_NONE,
                  SerialPort.FLOWCONTROL_NONE, 
                  SerialProtocol.SERIAL_EVENT_NONE);
                
        
        
        //String portName, int baudrate, int databits, int stopbits, int parity
        
        //int buffer = this.serialPort.getInputBufferSize();
        //System.out.println("Buffer: " + buffer);
        
        data = new ArrayList<MeterValuesEntry>();
        
        this.m_output_writer = writer; 
            //new ConsoleOutputWriter();
    
        try
        {
            this.setSerialPort(portName);
//            this.setPort(portName);
    
            if (!this.open())
            {
                this.m_status = 1;
            }
        }
        catch(Exception ex)
        {
            System.out.println("OneTouchMeter -> Error adding listener: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws PlugInBaseException
    {
        //return true;
        return super.open();
    //return false;
    }

    
    
    /* 
     * getComment
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getCompanyId
     */
    public int getCompanyId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return 0;
    }

    /* 
     * getIconName
     */
    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * getInstructions
     */
    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getMeterId
     */
    public int getMeterId()
    {
        // TODO Auto-generated method stub
        return 0;
    }
   

    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchMeter";
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
    
    
    
    
    
    
}