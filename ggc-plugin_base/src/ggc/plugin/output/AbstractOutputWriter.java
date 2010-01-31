package ggc.plugin.output;

import ggc.plugin.device.DeviceIdentification;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     AbstractOutputWriter
 *  Description:  Abstract Output Writer as base for all Output Writers.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractOutputWriter implements OutputWriter
{

    OutputUtil out_util; 
    DeviceIdentification device_info = null;
    String sub_status;
    
    
    /**
     * Constructor
     */
    public AbstractOutputWriter()
    {
        out_util = OutputUtil.getInstance(this);
    }
    
    
    /**
     * Write Header
     */
	public abstract void writeHeader();

	
    /**
     * End Output
     */
	public abstract void endOutput();

	
    /**
     * Set BG Output Type
     * 
     * @param bg_type type of BG
     */
    public void setBGOutputType(int bg_type)
    {
        this.out_util.setOutputBGType(bg_type);
    }
	
	
    /**
     * Set Device Identification (we usually don't use this method directly. Prefered way is to use
     * setYYYY method, which sets current name and specified company to Abstract class (which 
     * should contain setYYYY method) and to OutputWriter instance (look at setMeter method in 
     * AbstractSerialMeter in Meter Tool).
     * 
     * @param di DeviceIdentification object
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.device_info = di;
    }

    
    /**
     * Get Device Identification
     * 
     * @return DeviceIdentification object
     */
    public DeviceIdentification getDeviceIdentification()
    {
        return this.device_info;
    }
	
    
    /**
     * Get Output Util
     * 
     * @return OutputUtil instance
     */
    public OutputUtil getOutputUtil()
    {
        return this.out_util;
    }
    
    
    /**
     * Interrupt Communication
     */
    public void interruptCommunication()
    {
        this.out_util.setStopTime();
    }

    
    boolean device_should_be_stopped = false;
    
    /**
     * User can stop readings from his side (if supported)
     */
    public void setReadingStop()
    {
        this.device_should_be_stopped = true;
    }
    
    /**
     * This should be queried by device implementation, to see if it must stop reading
     */
    public boolean isReadingStopped()
    {
        return this.device_should_be_stopped;
    }
    
    int reading_status = 1;

    
    /**
     * Status: Ready 
     */
    public static final int STATUS_READY = 1;
    
    /**
     * Status: Downloading 
     */
    public static final int STATUS_DOWNLOADING = 2;
    
    /**
     * Status: Stopped Device 
     */
    public static final int STATUS_STOPPED_DEVICE = 3;
    
    /**
     * Status: Stopped by User 
     */
    public static final int STATUS_STOPPED_USER = 4;
    
    /**
     * Status: Download Finished 
     */
    public static final int STATUS_DOWNLOAD_FINISHED = 5;
    
    /**
     * Status: Reader Error 
     */
    public static final int STATUS_READER_ERROR = 6;
    
    /**
     * Status: Detecting Device 
     */
    public static final int STATUS_DETECTING_DEVICE = 7;
    
    /**
     * This is status of device and also of GUI that is reading device (if we have one)
     * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
     * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
     * 
     * @param status status of device 
     */
    public void setStatus(int status)
    {
        this.reading_status = status;
    }

    
    /**
     * Get Status
     * 
     * @return status of device
     */
    public int getStatus()
    {
        return this.reading_status;
    }
    
    
    /**
     * Set Sub Status - we use this substatus, when we want to send special display (on progress
     * bar), that user should see.
     *  
     * @param sub_status String with substatus text (should be i18n-ed)
     */
    public void setSubStatus(String sub_status)
    {
        this.sub_status = sub_status;
    }
    
    
    /**
     * Get Sub Status
     * @return Sub status String
     */
    public String getSubStatus()
    {
        return this.sub_status;
    }
    
    
    /**
     * If we have special status progress defined, by device, we need to set progress, by ourselves, this is 
     * done with this method.
     * @param value
     */
    public void setSpecialProgress(int value)
    {
        
    }
    
    
    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    public void writeLog(int entry_type, String message)
    {
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
    }
 
    
    /**
     * Can old data reading be initiated (if module in current running mode supports this, this is
     * intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public void canOldDataReadingBeInitiated(boolean value)
    {
    }
    
  
    /**
     * Set old data reading progress
     * 
     * @param value
     */
    public void setOldDataReadingProgress(int value)
    {
    }
    
    
    String device_source;
    
    /**
     * Set Device Source
     * 
     * @param dev
     */
    public void setDeviceSource(String dev)
    {
        this.device_source = dev;
    }
    
    
    /**
     * Set Device Source
     * 
     * @return 
     */
    public String getDeviceSource()
    {
        return this.device_source;
    }
    
	
    /**
     * setIndeterminateProgress - if we cannot trace progress, we set this and JProgressBar will go
     *    into indeterminate mode
     */
    public void setIndeterminateProgress()
    {
    }
    
    
}