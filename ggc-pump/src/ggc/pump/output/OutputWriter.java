package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;

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

	
}