package ggc.pump.device.animas;

import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.pump.output.OutputWriter;

public class AnimasTest extends EZManagerDb
{
    
    public AnimasTest()
    {
        super();
    }
    
    
    public AnimasTest(String db_path, OutputWriter writer)
    {
        super(db_path, writer);
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
        return "Test";
    }


    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "no_meter.jpg";
    }
    

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return EZManagerDb.PUMP_ANIMAS_TEST;
    }

    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ANIMAS_TEST";
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
        return DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS;
    }
    
    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords()
    {
        return 100;
    }
    
    
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.animas.AnimasTest";
    }




    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}
