package ggc.plugin.output;

import java.util.ArrayList;

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
    void writeData(OutputWriterData data);


    /**
     * Write Configuration Data to OutputWriter
     *
     * @param configData OutputWriterData instance
     */
    void writeConfigurationData(OutputWriterConfigData configData);


    /**
     * Set Plugin Name from where OutputWritter is called (for debuging output mostly)
     *
     * @param pluginName
     */
    void setPluginName(String pluginName);


    /**
     * Get Plugin Name
     *
     * @return
     */
    String getPluginName();


    /**
     * Write Header
     */
    void writeHeader();


    /**
     * Set BG Output Type
     * 
     * @param bg_type type of BG
     */
    void setBGOutputType(int bg_type);


    /**
     * End Output
     */
    void endOutput();


    /**
     * Get Output Util
     * 
     * @return OutputUtil instance
     */
    OutputUtil getOutputUtil();


    /**
     * Write Device Identification
     */
    void writeDeviceIdentification();


    /**
     * Interrupt Communication
     */
    void interruptCommunication();


    /**
     * User can stop readings from his side (if supported)
     */
    void setReadingStop();


    /**
     * This should be queried by device implementation, to see if it must stop reading
     * @return if reading has stopped
     */
    boolean isReadingStopped();


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
    DeviceIdentification getDeviceIdentification();


    /**
     * Set Sub Status - we use this substatus, when we want to send special display (on progress
     * bar), that user should see.
     *  
     * @param sub_status String with substatus text (should be i18n-ed)
     */
    void setSubStatus(String sub_status);


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
    void setSpecialProgress(int value);


    /**
     * setIndeterminateProgress - if we cannot trace progress, we set this and JProgressBar will go
     *    into indeterminate mode
     */
    void setIndeterminateProgress();


    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    void writeLog(int entry_type, String message);


    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     * @param ex
     */
    void writeLog(int entry_type, String message, Exception ex);


    /**
     * Can old data reading be initiated (if module in current running mode supports this, this is
     * intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public abstract void canOldDataReadingBeInitiated(boolean value);


    /**
     * Set old data reading progress
     * 
     * @param value
     */
    void setOldDataReadingProgress(int value);


    /**
     * Set Device Source
     * 
     * @param dev
     */
    void setDeviceSource(String dev);


    /**
     * Set Device Source
     * 
     * @return 
     */
    String getDeviceSource();


    /**
     * Add Error Message
     * 
     * @param msg
     */
    void addErrorMessage(String msg);


    /**
     * Get Error Message Count
     * 
     * @return
     */
    int getErrorMessageCount();


    /**
     * Get Error Messages
     * 
     * @return
     */
    ArrayList<String> getErrorMessages();

}