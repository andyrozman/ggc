/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.gui;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.cfg.MeterConfigEntry;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;

import java.lang.reflect.Constructor;


public class MeterReaderRunner extends Thread implements OutputWriter // extends JDialog implements ActionListener
{

    
    MeterInterface m_mi = null;
    boolean special_status = false;

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

    public void setDeviceComment(String com)
    {
        this.dialog.setDeviceComment(com);
    }
    
    int count = 0;
    

   
    
    /**
     * If we have special status progress defined, by device, we need to set progress, by ourselves, this is 
     * done with this method.
     * @param value
     */
    public void setSpecialProgress(int value)
    {
        //System.out.println("Runner: Special progres: " + value);
        this.dialog.setSpecialProgress(value);
    }
    
    
    
    public void setSubStatus(String sub_status)
    {
        //System.out.println("Runner: Sub Status: " + sub_status);
        this.dialog.setSubStatus(sub_status);
    }
    
    
    public String getSubStatus()
    {
        return this.dialog.getSubStatus();
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

    
    MeterConfigEntry configured_meter;
    MeterDisplayDataDialog dialog;

    
    public MeterReaderRunner(MeterConfigEntry configured_meter, MeterDisplayDataDialog dialog)
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
            
                String className = MeterManager.getInstance().getMeterDeviceClassName(this.configured_meter.meter_company, this.configured_meter.meter_device); 
                
                Class<?> c = Class.forName(className);
                
                Constructor<?> cnst = c.getDeclaredConstructor(String.class, OutputWriter.class);
                this.m_mi = (MeterInterface)cnst.newInstance(this.configured_meter.communication_port, this);
                this.setDeviceComment(this.m_mi.getDeviceSpecialComment());
                this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);
            
                this.special_status = this.m_mi.hasSpecialProgressStatus();
                
                this.m_mi.readDeviceDataFull();
                
                running = false;
                
                this.m_mi.close();
                
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


    public void writeData(OutputWriterData data)
    {
        if (!this.special_status)
        {
            count++;
            
            float f = ((count  * 1.0f)/this.dialog.output_util.getMaxMemoryRecords()) * 100.0f;
            
            //int i = (int)((count/500) * 100);
            //System.out.println("Progress: " + f + "  " + count + " max: " + this.dialog.output_util.getMaxMemoryRecords());
            
            dialog.progress.setValue((int)f);
        }
        
        this.dialog.writeData(data);
    }


    public void writeLog(int entry_type, String message)
    {
        this.dialog.writeLog(entry_type, message);
    }


    public void writeLog(int entry_type, String message, Exception ex)
    {
        this.dialog.writeLog(entry_type, message, ex);
    }
    

}
