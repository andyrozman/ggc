/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.gui;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.cfg.MeterConfigEntry;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterReaderRunner extends Thread implements OutputWriter // extends JDialog implements ActionListener
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
        // TODO Auto-generated method stub
        
    }


    /* 
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        // TODO Auto-generated method stub
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
    public void writeBGData(MeterValuesEntry mve)
    {
        count++;
        // TODO Auto-generated method stub
        
        float f = (count/500.0f) * 100f;
        
        //int i = (int)((count/500) * 100);
        
        System.out.println("Progress: " + f + "  " + count);
        
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
        // TODO Auto-generated method stub
        
    }


    /* 
     * writeHeader
     */
    public void writeHeader()
    {
        // TODO Auto-generated method stub
        
    }


    /* 
     * writeRawData
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * 
     */
    private static final long serialVersionUID = 7159799607489791137L;

    
    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccessMeter m_da = DataAccessMeter.getInstance();
    
    JButton button_start;
    



    int x,y;

    JFrame parentMy;

    
    
    //MeterInterface meter_interface;

    
    MeterConfigEntry configured_meter;
    //OutputWriter writer = null;
    MeterDisplayDataDialog dialog;

    public MeterReaderRunner(MeterConfigEntry configured_meter, MeterDisplayDataDialog dialog)
    {
//        this.writer = writer;
        this.configured_meter = configured_meter;
        this.dialog = dialog;
    }
    
    
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
        
    }

    
    boolean running = true;
    
    public void run()
    {
        System.out.println("run");

        while(running)
        {
            System.out.println("run.running");

            try
            {
            
                System.out.println(this.configured_meter);
                
                String className = MeterManager.getInstance().getMeterDeviceClassName(this.configured_meter.meter_company, this.configured_meter.meter_device); 
                
                System.out.println(className);
                
                
                Class<?> c = Class.forName(className);
                
                // String portName, AbstractOutputWriter writer
                
                Constructor<?> cnst = c.getDeclaredConstructor(String.class, OutputWriter.class);
                MeterInterface mi = (MeterInterface)cnst.newInstance(this.configured_meter.communication_port, this);
                
                mi.readDeviceData();
                
                
                System.out.println(cnst);
                
                running = false;
                
            }
            catch(Exception ex)
            {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
                
                running = false;
            }
            
            
            
        }  // while

        System.out.println("Exited runner");
    }
    


    



    




}
