package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.atech.utils.ATechDate;

public class FileOutputWriter implements OutputWriter
{
	
	BufferedWriter bw;
	OutputUtil out_util; 
	
	public FileOutputWriter(String fileName)
	{
		out_util = new OutputUtil(this);
		
		try
		{
			bw = new BufferedWriter(new FileWriter(new File(fileName)));
		}
		catch(Exception ex)
		{
			System.out.println("Error opening file:" + ex);
		}
	}

	/* x
	private void setReadData()
	{
		this.out_util.setLastChangedTime();
	} */
	
	public void writeRawData(String input, boolean is_bg_data)
	{
		writeToFile(input);
	}
	
	
	public void writeBGData(String date, String bg_value, int bg_type)
	{
		writeBGData(new ATechDate(Long.parseLong(date)), bg_value, bg_type);
	}

	public void writeBGData(ATechDate date, String bg_value, int bg_type)
	{
		writeToFile(date.getDateTimeString() + " = " + bg_value + " " + this.out_util.getBGTypeName(bg_type));
	}
	
	public void writeBGData(PumpValuesEntry mve)
	{
		writeToFile(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}
	
	
	public void writeHeader()
	{
		// header
		String dta = "=======================================================\n";
		dta += "==             Pump Tool Data Dump                  ==\n";
		dta += "=======================================================\n";
		
		writeToFile(dta);
	}

	public void setBGOutputType(int bg_type)
	{
		this.out_util.setOutputBGType(bg_type);
	}
	
	private void writeToFile(String values)
	{
		try
		{
			bw.write(values);
			bw.flush();
		}
		catch(Exception ex)
		{
			System.out.println("Write to file failed: " + ex);
		}
	}
	

	public void endOutput()
	{
		try
		{
			bw.flush();
			bw.close();
		}
		catch(Exception ex)
		{
			System.out.println("Closing file failed: " + ex);
		}
	}
	
	
	public OutputUtil getOutputUtil()
	{
		return this.out_util;
	}
	
	
	
}