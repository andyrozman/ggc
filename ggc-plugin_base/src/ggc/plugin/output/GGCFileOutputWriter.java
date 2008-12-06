package ggc.plugin.output;

import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.GregorianCalendar;

import com.atech.utils.ATechDate;

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
 *  Filename:     GGCFileOutputWriter  
 *  Description:  Output Writer for writing to GGC Import/Export File.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class GGCFileOutputWriter extends AbstractOutputWriter
{
	
	BufferedWriter bw;
	long time_created;
	DataAccessPlugInBase m_da;
	
	/**
	 * Constructor 
	 * 
	 * @param da
	 */
	public GGCFileOutputWriter(DataAccessPlugInBase da)
	{
	    super();
		out_util = OutputUtil.getInstance(this);
		m_da = da;
		
		try
		{
			System.out.println("OPEN FILE");
			bw = new BufferedWriter(new FileWriter(new File("DayValueH" + getCurrentDateForFile() + ".txt")));
			this.time_created = System.currentTimeMillis();
		}
		catch(Exception ex)
		{
			System.out.println("Error opening file:" + ex);
		}
	}
	
	
    private String getCurrentDateForFile()
    {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTimeInMillis(System.currentTimeMillis());

    	return gc.get(GregorianCalendar.YEAR) + "_" + 
    	       m_da.getLeadingZero((gc.get(GregorianCalendar.MONTH) +1),2) + "_" + 
    	       m_da.getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2);
	    	
    }
	
	
	private void setReadData()
	{
//b		this.out_util.setLastChangedTime();
	}
	
	
    /**
     * Write Data to OutputWriter
     * 
     * @param data OutputWriterData instance
     */
    public void writeData(OutputWriterData data)
    {
        data.setOutputType(OutputWriterType.GGC_FILE_EXPORT);

        writeToFile(data.getDataAsString());
        
        if (data.isDataBG())
            setReadData();
    }
	
	
	/**
	 * Write Raw Data
	 * 
	 * @param input
	 * @param bg_data
	 */
	public void writeRawData(String input, boolean bg_data)
	{
		writeToFile(input);
		
		if (bg_data)
			setReadData();
	}
	
	
    /**
     * Write Device Identification
     */
	public void writeDeviceIdentification()
    {
        writeToFile(this.getDeviceIdentification().getInformation("; "));
    }

	
    /**
     * Write Header
     */
	public void writeHeader()
	{
		// header

		StringBuffer sb = new StringBuffer();
		
		sb.append("; Class: ggc.db.hibernate.DayValueH\n");
		sb.append("; Date of export: " + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, new GregorianCalendar()));
				
				
//				16/1/2008  18:12:3");
		sb.append("; Exported by GGC Meter Tools - GGCHibernateOutputWriter\n");
		sb.append(";\n");
		sb.append("; Columns: id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, comment, changed\n");
		sb.append(";\n");

		writeToFile(sb.toString());

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
	

    /**
     * End Output
     */
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
		this.interruptCommunication();
		
	}
	
}