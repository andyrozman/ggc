package ggc.pump.manager.company;

import java.util.Hashtable;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.device.AbstractPump;

public class GenericPumpDevice extends AbstractPump
{
    AbstractPumpDeviceCompany apdc;
    
    public GenericPumpDevice(AbstractPumpDeviceCompany apdc)
    {
        this.apdc = apdc;
    }
    
    
    /**
     * getDeviceCompany - get Company for device
     * 
     * @return 
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return apdc;
    }
    
    
    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_NONE;
    }

    
    
    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return "NONE";
    }
    
    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice()
    {
        return false;
    }
    
    /**
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    public String getName()
    {
        return "Generic";
    }


    
    public String[] getProfileNames()
    {
        return this.apdc.getProfileNames();
    }
    
    
    
    public void close() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Hashtable<String, Integer> getBolusMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public DeviceIdentification getDeviceInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Hashtable<String, Integer> getErrorMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public Hashtable<String, Integer> getReportMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }


    public boolean open() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }


    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    
}
