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
 *  Filename:      OutputWriter 
 *  Description:   Output Writer interface.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface OutputWriter
{
	
    /**
     * Write Data to OutputWriter
     * 
     * @param data OutputWriterData instance
     */
    public abstract void writeData(OutputWriterData data);
    
	/**
	 * Write Header
	 */
	public abstract void writeHeader();
	
	
	/**
	 * Set BG Output Type
	 * 
	 * @param bg_type type of BG
	 */
	public abstract void setBGOutputType(int bg_type);
	
	/**
	 * End Output
	 */
	public abstract void endOutput();
	
	
	/**
	 * Get Output Util
	 * 
	 * @return OutputUtil instance
	 */
	public abstract OutputUtil getOutputUtil();

    /**
     * Write Device Identification
     */
    public abstract void writeDeviceIdentification();
	
	/**
	 * Interrupt Communication
	 */
	public abstract void interruptCommunication();

	/**
	 * User can stop readings from his side (if supported)
	 */
	public abstract void setReadingStop();
	
	/**
	 * This should be queried by device implementation, to see if it must stop reading
	 * @return if reading has stopped
	 */
	public abstract boolean isReadingStopped();
	
	/**
	 * This is status of device and also of GUI that is reading device (if we have one)
	 * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
	 * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
	 * 
	 * @param status status of device 
	 */
	public abstract void setStatus(int status);
	
	/**
	 * Get Status
	 * 
	 * @return status of device
	 */
	public abstract int getStatus();
	
	/**
	 * Set Device Identification (we usually don't use this method directly. Prefered way is to use
	 * setYYYY method, which sets current name and specified company to Abstract class (which 
	 * should contain setYYYY method) and to OutputWriter instance (look at setMeter method in 
	 * AbstractSerialMeter in Meter Tool).
	 * 
	 * @param di DeviceIdentification object
	 */
    public abstract void setDeviceIdentification(DeviceIdentification di);

    
    /**
     * Get Device Identification
     * 
     * @return DeviceIdentification object
     */
    public abstract DeviceIdentification getDeviceIdentification();
    
    /**
     * Set Sub Status - we use this substatus, when we want to send special display (on progress
     * bar), that user should see.
     *  
     * @param sub_status String with substatus text (should be i18n-ed)
     */
    public abstract void setSubStatus(String sub_status);
    
    /**
     * Get Sub Status
     * @return Sub status String
     */
    public abstract String getSubStatus();
    
    /**
     * If we have special status progress defined, by device, we need to set progress, by ourselves, this is 
     * done with this method.
     * @param value
     */
    public abstract void setSpecialProgress(int value);
    
    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    public abstract void writeLog(int entry_type, String message);
    
    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     * @param ex
     */
    public abstract void writeLog(int entry_type, String message, Exception ex);
    
}