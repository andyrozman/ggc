package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.util.DataAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.GregorianCalendar;

import com.atech.utils.ATechDate;

public class GGCFileOutputWriter implements OutputWriter
{
	
	BufferedWriter bw;
	OutputUtil out_util; 
	DataAccess m_da;
	
	public GGCFileOutputWriter()
	{
		out_util = new OutputUtil(this);
		m_da = DataAccess.getInstance();
		
		try
		{
			System.out.println("OPEN FILE");
			bw = new BufferedWriter(new FileWriter(new File("DayValueH" + getCurrentDateForFile() + ".txt")));
		}
		catch(Exception ex)
		{
			System.out.println("Error opening file:" + ex);
		}
	}
	
	
    public String getCurrentDateForFile()
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTimeInMillis(System.currentTimeMillis());

    	return gc.get(GregorianCalendar.YEAR) + "_" + 
    	       m_da.getLeadingZero((gc.get(GregorianCalendar.MONTH) +1),2) + "_" + 
    	       m_da.getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2);
	    	
    }
	
	
	
	private void setReadData()
	{
		this.out_util.setLastChangedTime();
	}
	
	
	public void writeRawData(String input, boolean bg_data)
	{
		writeToFile(input);
		
		if (bg_data)
			setReadData();
	}
	
/*	
	public void writeBGData(String date, String bg_value, int bg_type)
	{
		writeBGData(new ATechDate(Long.parseLong(date)), bg_value, bg_type);
	}

	public void writeBGData(ATechDate date, String bg_value, int bg_type)
	{

		/*
		1|200603250730|0|10.0|0.0|0.0|null|null|
		2|200603300730|91|6.0|0.0|0.0|null|null|
		3|200603290730|163|10.0|0.0|0.0|null|null|
		4|200604030730|0|6.0|0.0|0.0|null|null|
		*/
/*		int val = 0;
		
		if (bg_type == OutputUtil.BG_MMOL)
		{
			float fl = Float.parseFloat(bg_value);
			val = (int)this.out_util.getBGValueDifferent(bg_type, fl);
			
		}
		else
		{
			try
			{
				val = Integer.parseInt(bg_value);
			}
			catch(Exception ex)
			{
				val = 0;
			}
		}
		
		//1|200603250730|0|10.0|0.0|0.0|null|null|
		System.out.println(date.getDateTimeString() + " = " + bg_value + " " + this.out_util.getBGTypeName(bg_type));
		
		writeToFile("0|" + date.getATDateTimeAsLong() + "|" + val + "|0.0|0.0|0.0|null|null|");
		setReadData();
		
	}
	*/
	
	public void writeBGData(PumpValuesEntry mve)
	{
		
		
		/*
		1|200603250730|0|10.0|0.0|0.0|null|null|
		2|200603300730|91|6.0|0.0|0.0|null|null|
		3|200603290730|163|10.0|0.0|0.0|null|null|
		4|200604030730|0|6.0|0.0|0.0|null|null|
		*/
		int val = 0;
		
		if (mve.getBgUnit() == OutputUtil.BG_MMOL)
		{
			float fl = Float.parseFloat(mve.getBgValue());
			val = (int)this.out_util.getBGValueDifferent(mve.getBgUnit(), fl);
			
		}
		else
		{
			try
			{
				val = Integer.parseInt(mve.getBgValue());
			}
			catch(Exception ex)
			{
				val = 0;
			}
		}

		String parameters = mve.getParametersAsString();
		
		//1|200603250730|0|10.0|0.0|0.0|null|null|
		
		if (parameters.equals(""))
			System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
		else
			System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()) + " Params: " + parameters );
		
		writeToFile("0|" + mve.getDateTime().getATDateTimeAsLong() + "|" + val + 
				    "|0.0|0.0|0.0|null|null|1|" + parameters);
		setReadData();
		
		//writeToFile(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}
	
	
	
	public void writeHeader()
	{
		// header

		StringBuffer sb = new StringBuffer();
		
		sb.append("; Class: ggc.db.hibernate.DayValueH\n");
		sb.append("; Date of export: " + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, new GregorianCalendar()));
				
				
//				16/1/2008  18:12:3");
		sb.append("; Exported by GGC Pump Tools - GGCHibernateOutputWriter\n");
		sb.append(";\n");
		sb.append("; Columns: id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, comment\n");
		sb.append(";\n");

		writeToFile(sb.toString());
//		setReadData();
		/*
		1|200603250730|0|10.0|0.0|0.0|null|null|
		2|200603300730|91|6.0|0.0|0.0|null|null|
		3|200603290730|163|10.0|0.0|0.0|null|null|
		4|200604030730|0|6.0|0.0|0.0|null|null|
		*/
		
		/*
		String dta = "=======================================================\n";
		dta += "==             Pump Tool Data Dump                  ==\n";
		dta += "=======================================================\n";
		
		writeToFile(dta); */
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
			bw.newLine();
			bw.flush();
		}
		catch(Exception ex)
		{
			System.out.println("Write to file failed: " + ex);
		}
	}
	

	public void endOutput()
	{
		System.out.println("END OUTPUT");
		
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