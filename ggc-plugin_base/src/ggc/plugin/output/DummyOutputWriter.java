package ggc.plugin.output;



public class DummyOutputWriter extends AbstractOutputWriter
{
	
	
	public DummyOutputWriter()
	{
	    super();
	}
	
	
	
    public void writeData(OutputWriterData data)
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
	
	/*
	public void writeBGData(MeterValuesEntry mve)
	{
		//System.out.println(mve.getDateTime().getDateTimeString() + " = " + mve.getBgValue() + " " + this.out_util.getBGTypeName(mve.getBgUnit()));
	}*/
	
	
	public void writeHeader(int bg_type)
	{
	}
	
	public void writeHeader()
	{
	}

	

    public void writeDeviceIdentification()
    {
//        System.out.println(this.getDeviceIdentification().getInformation("* "));
    }
	
	
	public void endOutput()
	{
	}
	
	
}