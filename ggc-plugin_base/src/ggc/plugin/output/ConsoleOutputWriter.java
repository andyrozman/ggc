package ggc.plugin.output;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class ConsoleOutputWriter extends AbstractOutputWriter
{

	
	public ConsoleOutputWriter()
	{
	    super();
	}
	
	public void writeData(OutputWriterData data)
	{
	    data.setOutputType(OutputWriterType.CONSOLE);
		System.out.println(data.getDataAsString());
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
	
	/*
	public void writeBGData(MeterValuesEntry mve)
	{
		System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}
	*/
	
	
	
	public void writeHeader()
	{
		// header
		System.out.println("=======================================================");
		System.out.println("==             Meter Tool Data Dump                  ==");
		System.out.println("=======================================================");
	}

	
	public void writeDeviceIdentification()
	{
	    System.out.println(this.getDeviceIdentification().getInformation("* "));
	}
	
	

	
	public void endOutput()
	{
		System.out.println("=======================================================");
	}
	
	
}