package ggc.plugin.gui;

import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.plugin.util.LogEntryType;

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterReaderRunner
 *  Description:  This is separate thread class to get current data from database in otder to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//Try to assess possibility of super-classing


public class DeviceReaderRunner extends Thread implements OutputWriter // extends JDialog implements ActionListener
{

    private static Log log = LogFactory.getLog(DeviceReaderRunner.class);
    
    DeviceInterface m_mi = null;
    boolean special_status = false;


    DeviceConfigEntry configured_meter;
    DeviceDisplayDataDialog dialog;

    boolean running = true;
    DataAccessPlugInBase m_da;
    
    /**
     * Constructor
     * 
     * @param da 
     * @param configured_meter
     * @param dialog
     */
    public DeviceReaderRunner(DataAccessPlugInBase da, DeviceConfigEntry configured_meter, DeviceDisplayDataDialog dialog)
    {
        this.m_da = da;
        this.configured_meter = configured_meter;
        this.dialog = dialog;
    }
    
    
    
    /** 
     * Thread running method
     */
    public void run()
    {

        while(running)
        {
            String lg = "";
            try
            {
                lg = "Creating instance [name=" + this.configured_meter.name + ",company=" + this.configured_meter.device_company + ",device=" + this.configured_meter.device_device + ",comm_port=" + this.configured_meter.communication_port + "]";
                log.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
                
                String className = m_da.getManager().getDeviceClassName(this.configured_meter.device_company, this.configured_meter.device_device); 
                
                Class<?> c = Class.forName(className);
                
                Constructor<?> cnst = c.getDeclaredConstructor(String.class, OutputWriter.class);
                this.m_mi = (DeviceInterface)cnst.newInstance(this.configured_meter.communication_port, this);
                this.setDeviceComment(this.m_mi.getDeviceSpecialComment());
                this.setStatus(AbstractOutputWriter.STATUS_DOWNLOADING);
                
                lg = "Meter device instance created and initied";
                log.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
            
                this.special_status = this.m_mi.hasSpecialProgressStatus();
                
                lg = "Start reading of data";
                log.debug(lg);
                writeLog(LogEntryType.DEBUG, lg);
                
                this.m_mi.readDeviceDataFull();

                running = false;
                
                this.m_mi.dispose();
                
            }
            catch(Exception ex)
            {
                this.setStatus(AbstractOutputWriter.STATUS_READER_ERROR);
                //System.out.println("Exception: " + ex);
                //ex.printStackTrace();
                //log.error("MeterReaderRunner:Exception:" + ex, ex);
                lg = "MeterReaderRunner:Exception:" + ex;
                log.error(lg, ex);
                writeLog(LogEntryType.ERROR, lg, ex);
                running = false;
            }
            
            this.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);

            lg = "Reading finished";
            log.debug(lg);
            writeLog(LogEntryType.DEBUG, lg);
            
            
        }  // while

    }

    
    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
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


    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    public void writeLog(int entry_type, String message)
    {
        this.dialog.writeLog(entry_type, message);
    }


    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     * @param ex
     */
    public void writeLog(int entry_type, String message, Exception ex)
    {
        this.dialog.writeLog(entry_type, message, ex);
    }
    
    
    /** 
     * endOutput
     */
    public void endOutput()
    {
    }


    /** 
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        return this.dialog.device_ident;
    }


    /** 
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return this.dialog.output_util;
    }


    /** 
     * interruptCommunication
     */
    public void interruptCommunication()
    {
    }


    /** 
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        this.dialog.output_util.setOutputBGType(bg_type);
    }


    /** 
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.dialog.setDeviceIdentification(di);
        // TODO Auto-generated method stub
        
    }

    /**
     * Set Device Comment
     * 
     * @param com
     */
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
    
    
    /** 
     * Set Sub Status
     */
    public void setSubStatus(String sub_status)
    {
        //System.out.println("Runner: Sub Status: " + sub_status);
        this.dialog.setSubStatus(sub_status);
    }
    
    
    /** 
     * Get Sub Status
     */
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
    
    
    /** 
     * Get Status
     */
    public int getStatus()
    {
        return this.dialog.getStatus();
    }
    

    /** 
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        this.dialog.writeDeviceIdentification();
    }


    /** 
     * writeHeader
     */
    public void writeHeader()
    {
    }


    /** 
     * writeRawData
     * 
     * @param input 
     * @param is_bg_data 
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
    }
    
    
}
