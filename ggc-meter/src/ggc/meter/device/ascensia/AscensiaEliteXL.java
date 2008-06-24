package ggc.meter.device.ascensia;


import ggc.meter.output.AbstractOutputWriter;



public class AscensiaEliteXL extends AscensiaMeter
{
    
    public AscensiaEliteXL()
    {
    }
    
    
    public AscensiaEliteXL(String portName, AbstractOutputWriter writer)
    {
    	super(portName, writer);
    }

    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "EliteXL";
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ascensia_elite_xl.png";
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return AscensiaMeter.METER_ASCENSIA_ELITE_XL;
    }


    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AscensiaMeter.ASCENSIA_COMPANY;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ASCENSIA_ELITE_XL";
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }
    
    
    
    public String getDeviceClassName()
    {
        return "ggc.meter.device.ascensia.AscensiaEliteXL";
    }

    
    public int getMaxMemoryRecords()
    {
        return 120;
    }

    
    

}
