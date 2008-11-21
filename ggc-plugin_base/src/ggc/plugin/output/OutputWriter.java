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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface OutputWriter
{
	
    public abstract void writeData(OutputWriterData data);
    
	public abstract void writeHeader();
	
	public abstract void setBGOutputType(int bg_type);
	
	public abstract void endOutput();
	
	public abstract OutputUtil getOutputUtil();

    public abstract void writeDeviceIdentification();
	
	public abstract void interruptCommunication();

	/**
	 * User can stop readings from his side (if supported)
	 */
	public abstract void setReadingStop();
	
	/**
	 * This should be queried by device implementation, to see if it must stop reading
	 */
	public abstract boolean isReadingStopped();
	
	/**
	 * This is status of device and also of GUI that is reading device (if we have one)
	 * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
	 * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
	 */
	public abstract void setStatus(int status);
	
	public abstract int getStatus();
	
    public abstract void setDeviceIdentification(DeviceIdentification di);

    public abstract DeviceIdentification getDeviceIdentification();
    
    public abstract void setSubStatus(String sub_status);
    
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