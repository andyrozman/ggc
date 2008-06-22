package ggc.meter.device.onetouch;

import java.util.ArrayList;

import com.atech.utils.TimeZoneUtil;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.device.MeterException;
import ggc.meter.output.OutputWriter;
import gnu.io.SerialPort;


public class OneTouchMeter extends AbstractSerialMeter
{
    
    
    protected ArrayList<MeterValuesEntry> data = null;
    protected OutputWriter m_output_writer;
    public TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    

    public OneTouchMeter(int meter_type, String portName, OutputWriter writer)
    {
        //super(portName, 9600, SerialConfig.LN_8BITS, SerialConfig.ST_1BITS, SerialConfig.PY_NONE);

//      int s= SerialConfig.LN_8BITS; 
        
        
        super(meter_type, /*portName, */ 
              9600,
              //19200,
              SerialPort.DATABITS_8, 
              SerialPort.STOPBITS_1, 
              SerialPort.PARITY_NONE);
    
        this.setCommunicationSettings( 
                  9600,
                  SerialPort.DATABITS_8, 
                  SerialPort.STOPBITS_1, 
                  SerialPort.PARITY_NONE,
                  SerialPort.FLOWCONTROL_NONE);
                
        this.setSerialPort(portName);
        
        
        //String portName, int baudrate, int databits, int stopbits, int parity
        
        //int buffer = this.serialPort.getInputBufferSize();
        //System.out.println("Buffer: " + buffer);
        
        data = new ArrayList<MeterValuesEntry>();
        
        this.m_output_writer = writer; 
            //new ConsoleOutputWriter();
    
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

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws MeterException
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

    /* 
     * getGlobalMeterId
     */
    public int getGlobalMeterId()
    {
        // TODO Auto-generated method stub
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


    
    
    
    
}