package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.device.DeviceIdentification;

public interface OutputWriter
{
	
	public abstract void writeRawData(String input, boolean is_bg_data);
	
	//public abstract void writeBGData(String date, String bg_value, int bg_type);
	
	//public abstract void writeBGData(ATechDate date, String bg_value, int bg_type);
	
	public abstract void writeBGData(PumpValuesEntry mve);
	
	

	//public abstract void writeBGData(String date, String bg_value);

	//public abstract void writeHeader(int bg_type);
	
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

    
    
    
    
    
}