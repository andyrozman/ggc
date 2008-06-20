package ggc.meter.device.accuchek;

import javax.swing.ImageIcon;

public class AccuChekAviva extends AccuChekSmartPix
{
    
    public AccuChekAviva()
    {
        super();
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
        return "name";
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcom
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return null;
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return 2;
    }

    
    /**
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return global id of meter
     */
    public int getGlobalMeterId()
    {
        return 0;
    }

    
    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 0;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
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
     * getCompanyId - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }
    
}
