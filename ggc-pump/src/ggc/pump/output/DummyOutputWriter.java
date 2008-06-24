package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;


public class DummyOutputWriter implements OutputWriter
{
	
	OutputUtil out_util; 
	
	public DummyOutputWriter()
	{
		out_util = new OutputUtil(this);
	}
	
	
	
	public void writeRawData(String input, boolean is_bg_data)
	{
	}
	
	/*
	public void writeBGData(String date, String bg_value)
	{
	}

	public void writeBGData(String date, String bg_value, int bg_type)
	{
	}
	
	
	public void writeBGData(ATechDate date, String bg_value, int bg_type)
	{
		
	} */
	
	
	public void writeBGData(PumpValuesEntry mve)
	{
		//System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}
	
	
	public void writeHeader(int bg_type)
	{
	}
	
	public void writeHeader()
	{
	}

	public void setBGOutputType(int bg_type)
	{
		this.out_util.setOutputBGType(bg_type);
	}
	
	
	public void endOutput()
	{
	}
	
	public OutputUtil getOutputUtil()
	{
		return this.out_util;
	}
	
	
}