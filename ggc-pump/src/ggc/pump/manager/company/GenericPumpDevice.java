package ggc.pump.manager.company;

import java.util.Hashtable;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.device.AbstractPump;

// TODO: Auto-generated Javadoc
/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:      Generic Pump Device  
 *  Description:   Generic Pump Device used for getting profile names for specified 
 *                 pump company.
 * 
 *  Author: Andy {andy@atech-software.com}
 */



public class GenericPumpDevice extends AbstractPump
{
    
    /**
     * The apdc.
     */
    AbstractPumpDeviceCompany apdc;
    
    /**
     * Instantiates a new generic pump device.
     * 
     * @param apdc the apdc
     */
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


    
    /** 
     * getProfileNames
     */
    public String[] getProfileNames()
    {
        return this.apdc.getProfileNames();
    }
    
    
    
    /** 
     * close
     */
    public void close() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * getAlarmMappings
     */
    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getBolusMappings
     */
    public Hashtable<String, Integer> getBolusMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getDeviceInfo
     */
    public DeviceIdentification getDeviceInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getErrorMappings
     */
    public Hashtable<String, Integer> getErrorMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getEventMappings
     */
    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getMaxMemoryRecords
     */
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * getReportMappings
     */
    public Hashtable<String, Integer> getReportMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * loadPumpSpecificValues
     */
    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * open
     */
    public boolean open() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return false;
    }


    /** 
     * dispose
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * getComment
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getDeviceClassName
     */
    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getDeviceId
     */
    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * getDeviceSpecialComment
     */
    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getIconName
     */
    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * getInstructions
     */
    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * hasSpecialProgressStatus
     */
    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    /** 
     * isDeviceCommunicating
     */
    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }


    /** 
     * readConfiguration
     */
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * readDeviceDataPartitial
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    /** 
     * readInfo
     */
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    
}
