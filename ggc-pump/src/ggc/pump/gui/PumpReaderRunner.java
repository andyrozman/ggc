
package ggc.pump.gui;

import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.device.PumpInterface;
import ggc.pump.manager.PumpManager;

import java.lang.reflect.Constructor;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpReaderRunner extends Thread implements OutputWriter // extends JDialog implements ActionListener
{

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
    }

    int count = 0;
    
    /* 
     * writeBGData
     */
    public void writeBGData(PumpValuesEntry mve)
    {
        
        if (!this.special_status)
        {
            count++;
            
            float f = ((count  * 1.0f)/this.dialog.output_util.getMaxMemoryRecords()) * 100.0f;
            
            //int i = (int)((count/500) * 100);
            //System.out.println("Progress: " + f + "  " + count + " max: " + this.dialog.output_util.getMaxMemoryRecords());
            
            dialog.progress.setValue((int)f);
        }
        
        this.dialog.writeBGData(mve);
        
    }

   
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

    
    public void setDeviceComment(String com)
    {
        // TODO
        //this.dialog.setDeviceComment(com);
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

    
    DeviceConfigEntry configured_device;
    PumpDisplayDataDialog dialog;

    
    public PumpReaderRunner(DeviceConfigEntry configured_device_in, PumpDisplayDataDialog dialog)
    {
//        this.writer = writer;
        this.configured_device = configured_device_in;
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
            
                String className = PumpManager.getInstance().getPumpDeviceClassName(this.configured_device.device_company, this.configured_device.device_device); 
                
                Class<?> c = Class.forName(className);
                
                Constructor<?> cnst = c.getDeclaredConstructor(String.class, OutputWriter.class);
                PumpInterface mi = (PumpInterface)cnst.newInstance(this.configured_device.communication_port, this);
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


    public void writeData(OutputWriterData data)
    {
        // TODO Auto-generated method stub
        
    }


    public void writeLog(int entry_type, String message)
    {
        // TODO Auto-generated method stub
        
    }


    public void writeLog(int entry_type, String message, Exception ex)
    {
        // TODO Auto-generated method stub
        
    }
    

}
