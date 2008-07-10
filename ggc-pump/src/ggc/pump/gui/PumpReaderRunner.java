
package ggc.pump.gui;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.cfg.PumpConfigEntry;
import ggc.pump.device.DeviceIdentification;
import ggc.pump.device.PumpInterface;
import ggc.pump.manager.PumpManager;
import ggc.pump.output.AbstractOutputWriter;
import ggc.pump.output.OutputUtil;
import ggc.pump.output.OutputWriter;

import java.lang.reflect.Constructor;


public class PumpReaderRunner extends Thread implements OutputWriter // extends JDialog implements ActionListener
{


    /* 
     * endOutput
     */
    public void endOutput()
    {
        // TODO Auto-generated method stub
        
    }


    /* 
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        return this.dialog.device_ident;
    }


    /* 
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return this.dialog.output_util;
        //return null;
    }


    /* 
     * interruptCommunication
     */
    public void interruptCommunication()
    {
    }


    /* 
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        this.dialog.output_util.setOutputBGType(bg_type);
    }


    /* 
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.dialog.setDeviceIdentification(di);
        // TODO Auto-generated method stub
        
    }

    int count = 0;
    
    /* 
     * writeBGData
     */
    public void writeBGData(PumpValuesEntry mve)
    {
        count++;
        // TODO Auto-generated method stub
        
        float f = ((count  * 1.0f)/this.dialog.output_util.getMaxMemoryRecords()) * 100.0f;
        
        //int i = (int)((count/500) * 100);
        
        //System.out.println("Progress: " + f + "  " + count + " max: " + this.dialog.output_util.getMaxMemoryRecords());
        
        dialog.progress.setValue((int)f);
        
        this.dialog.writeBGData(mve);
        
        
    }

    
    
    
    /**
     * User can stop readings from his side (if supported)
     */
    public void setReadingStop()
    {
        this.dialog.setReadingStop();
    }
    
    /**
     * This should be queried by device implementation, to see if it must stop reading
     */
    public boolean isReadingStopped()
    {
        return this.dialog.isReadingStopped();
    }
    
    
    
    /**
     * This is status of device and also of GUI that is reading device (if we have one)
     * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
     * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
     */
    public void setStatus(int status)
    {
        this.dialog.setStatus(status);
    }
    
    public int getStatus()
    {
        return this.dialog.getStatus();
    }
    
    
    
    
    
    
    

    /* 
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        this.dialog.writeDeviceIdentification();
    }


    /* 
     * writeHeader
     */
    public void writeHeader()
    {
    }


    /* 
     * writeRawData
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
    }


    /**
     * 
     */
    private static final long serialVersionUID = 7159799607489791137L;

    
    PumpConfigEntry configured_meter;
    PumpDisplayDataDialog dialog;

    
    public PumpReaderRunner(PumpConfigEntry configured_meter, PumpDisplayDataDialog dialog)
    {
//        this.writer = writer;
        this.configured_meter = configured_meter;
        this.dialog = dialog;
    }
    
/*    
    private void loadConfiguration()
    {
        // TODO: this should be read from config
        
        this.configured_meter = new MeterConfigEntry();
        this.configured_meter.id =1;
        this.configured_meter.communication_port = "COM9";
        this.configured_meter.name = "My Countour";
        this.configured_meter.meter_company = "Ascensia/Bayer";
        this.configured_meter.meter_device = "Contour";
        this.configured_meter.ds_area= "Europe/Prague";
        this.configured_meter.ds_winter_change = 0;
        this.configured_meter.ds_summer_change = 1;
        this.configured_meter.ds_fix = true;
        
        /*
        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(+1);
        */
        
        //MeterInterface mi = MeterManager.getInstance().getMeterDevice(this.configured_meter.meter_company, this.configured_meter.meter_device);
        
        //this.meter_interface = mi;
  /*      
    }
*/
    
    boolean running = true;
    
    public void run()
    {

        while(running)
        {

            try
            {
            
                String className = PumpManager.getInstance().getPumpDeviceClassName(this.configured_meter.meter_company, this.configured_meter.meter_device); 
                
                Class<?> c = Class.forName(className);
                
                Constructor<?> cnst = c.getDeclaredConstructor(String.class, OutputWriter.class);
                PumpInterface mi = (PumpInterface)cnst.newInstance(this.configured_meter.communication_port, this);
                this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);
                
                mi.readDeviceDataFull();
                
                running = false;
                
                mi.close();
                
            }
            catch(Exception ex)
            {
                this.setStatus(AbstractOutputWriter.STATUS_READER_ERROR);
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
                
                running = false;
            }
            
            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            
        }  // while

    }
    

}
