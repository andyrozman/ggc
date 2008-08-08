package ggc.meter.device.accuchek;

import ggc.meter.manager.MeterImplementationStatus;
import ggc.meter.output.OutputWriter;

public class AccuChekAviva extends AccuChekSmartPixMeter
{
    
    public AccuChekAviva()
    {
        super();
    }
    
    public AccuChekAviva(String drive_letter, OutputWriter writer)
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
        return "Aviva";
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ac_aviva.jpg";
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return AccuChekSmartPixMeter.METER_ACCUCHEK_AVIVA;
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
        return "INSTRUCTIONS_ACCUCHEK_AVIVA";
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
     * getDeviceClassName() - get name of this class
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.accuchek.AccuChekAviva";
    }
    
    
    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    public int getNrOfElementsFor1s()
    {
        return 12;
    }

    
    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 500;
    }
    
    
}
