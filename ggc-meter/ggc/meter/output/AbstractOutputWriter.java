package ggc.meter.output;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.DeviceIdentification;

public abstract class AbstractOutputWriter implements OutputWriter
{

    OutputUtil out_util; 
    DeviceIdentification device_info = null;

    
    public AbstractOutputWriter()
    {
        out_util = new OutputUtil(this);
    }
    
    
    
	public abstract void writeRawData(String input, boolean is_bg_data);
	
	public abstract void writeBGData(MeterValuesEntry mve);
	
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

    
    
    
	
}