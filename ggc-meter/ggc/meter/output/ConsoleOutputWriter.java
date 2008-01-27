package ggc.meter.output;

import ggc.meter.data.MeterValuesEntry;

public class ConsoleOutputWriter implements OutputWriter
{

	OutputUtil out_util; 
	
	public ConsoleOutputWriter()
	{
		out_util = new OutputUtil(this);
		
		
	}
	
	public void writeRawData(String input)
	{
		System.out.println(input);
		
	}
	

	/*
	public void writeBGData(String date, String bg_value, int bg_type)
	{
		writeBGData(new ATechDate(Long.parseLong(date)), bg_value, bg_type);
	}

	public void writeBGData(ATechDate date, String bg_value, int bg_type)
	{
		System.out.println(date.getDateTimeString() + " = " + bg_value + " " + this.out_util.getBGTypeName(bg_type));
	}*/
	
	public void writeBGData(MeterValuesEntry mve)
	{
		System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}
	
	
	
	
	public void writeHeader()
	{
		// header
		System.out.println("=======================================================");
		System.out.println("==             Meter Tool Data Dump                  ==");
		System.out.println("=======================================================");
	}
	
	
	public void setBGOutputType(int bg_type)
	{
		this.out_util.setOutputBGType(bg_type);
		
	}

	
	public void endOutput()
	{
		System.out.println("=======================================================");
	}
	
	public OutputUtil getOutputUtil()
	{
		return this.out_util;
	}
	
	
	
}