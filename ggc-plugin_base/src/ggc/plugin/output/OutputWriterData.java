package ggc.plugin.output;


public abstract class OutputWriterData 
{
    
	
    public abstract String getDataAsString();

    public abstract boolean isDataBG();
    
    public abstract void setOutputType(int type);
    
}