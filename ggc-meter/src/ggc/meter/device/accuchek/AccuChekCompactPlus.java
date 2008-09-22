package ggc.meter.device.accuchek;

import ggc.meter.manager.MeterImplementationStatus;
import ggc.plugin.output.OutputWriter;

public class AccuChekCompactPlus extends AccuChekSmartPixMeter
{
    
    public AccuChekCompactPlus()
    {
        super();
    }
    
    
    public AccuChekCompactPlus(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer);
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
        return "CompactPlus";
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ac_compact_plus.jpg";
    }
    

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return AccuChekSmartPixMeter.METER_ACCUCHEK_COMPACT_PLUS;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AccuChekSmartPixMeter.ROCHE_COMPANY;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ACCUCHEK_COMPACTPLUS";
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
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return MeterImplementationStatus.IMPLEMENTATION_TESTING;
    }
    
    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords()
    {
        return 300;
    }
    
    
    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    public int getNrOfElementsFor1s()
    {
        return 10;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.accuchek.AccuChekCompactPlus";
    }
    
    
}
