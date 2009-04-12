package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.pump.util.DataAccessPump;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DummyPump extends AbstractPump //implements PumpInterface //extends GenericPump //implements MeterInterface
{

    /**
     * The m_da.
     */
    DataAccessPump m_da = DataAccessPump.getInstance();
    
    /**
     * The m_ic.
     */
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    
    
    /**
     * Constructor
     */
    public DummyPump()
    {
        super();
    }
    
    /**
     * Constructor
     * 
     * @param ow
     */
    public DummyPump(OutputWriter ow)
    {
        //super(ow);
    }
    

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open()
    {
        return true;
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        return;
    }


    /**
     * Log: Debug
     */
    public static final int LOG_DEBUG = 1;
    
    /**
     * Log: Info
     */
    public static final int LOG_INFO = 2;
    
    /**
     * Log: Warn
     */
    public static final int LOG_WARN = 3;
    
    /**
     * Log: Error
     */
    public static final int LOG_ERROR = 4;
    
    /**
     * Write Log
     * 
     * @param problem
     * @param text
     */
    public void writeLog(int problem, String text)
    {
        System.out.println("Dummy -> " + text);
    }


    /**
     * @return
     */
    public String getInfo() //throws MeterException
    {
        writeLog(LOG_DEBUG, "getVersion() - Start");
        writeLog(LOG_DEBUG, "getVersion() - End");
        return "Dummy Meter\n" +
               "v0.1\n" +
               m_ic.getMessage("DUMMY_INFO_TEXT");
    }

    





    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        return "Dummy Meter";
    }


    








    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /**
     * canReadData - Can Meter Class read data from device
     * @return 
     */
    public boolean canReadData()
    {
        return false;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * @return 
     */
    public boolean canReadPartitialData()
    {
        return false;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     * @return 
     */
    public boolean canClearData()
    {
        return false;
    }



    //************************************************
    //***                    Test                  ***
    //************************************************

    /**
     * 
     */
    public void test()
    {
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
     * getConnectionPort
     */
    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * getConnectionProtocol
     */
    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
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
