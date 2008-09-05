package ggc.cgm.output;

import ggc.cgm.device.DeviceIdentification;


public abstract class AbstractOutputWriter implements OutputWriter
{

    OutputUtil out_util; 
    DeviceIdentification device_info = null;
    String sub_status;

    
    public AbstractOutputWriter()
    {
        out_util = new OutputUtil(this);
    }
    
    
    
	public abstract void writeRawData(String input, boolean is_bg_data);
	
//	public abstract void writeBGData(CGMValuesEntry mve);
	
	public abstract void writeHeader();
	
	public abstract void endOutput();

	
    public void setBGOutputType(int bg_type)
    {
        this.out_util.setOutputBGType(bg_type);
        
    }
	
	
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.device_info = di;
    }

    public DeviceIdentification getDeviceIdentification()
    {
        return this.device_info;
    }
	
    
    public OutputUtil getOutputUtil()
    {
        return this.out_util;
    }
    
    
       /* 
     * interruptCommunication
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

    
    public static final int STATUS_READY = 1;
    
    public static final int STATUS_DOWNLOADING = 2;
    public static final int STATUS_STOPPED_DEVICE = 3;
    public static final int STATUS_STOPPED_USER = 4;
    public static final int STATUS_DOWNLOAD_FINISHED = 5;
    public static final int STATUS_READER_ERROR = 6;
    public static final int STATUS_DETECTING_DEVICE = 7;
    
    /**
     * This is status of device and also of GUI that is reading device (if we have one)
     * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
     * 3-Stopped by device, 4-Stoped by user,5-Download finished,...
     */
    public void setStatus(int status)
    {
        this.reading_status = status;
    }
    
    public int getStatus()
    {
        return this.reading_status;
    }
    
    
    public void setSubStatus(String sub_status)
    {
        this.sub_status = sub_status;
    }
    
    
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
    
    
	
}