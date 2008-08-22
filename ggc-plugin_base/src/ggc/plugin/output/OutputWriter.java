package ggc.plugin.output;

import ggc.plugin.device.DeviceIdentification;


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