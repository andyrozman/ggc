package ggc.plugin.output;


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