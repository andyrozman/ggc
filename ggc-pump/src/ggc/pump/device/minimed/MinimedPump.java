package ggc.pump.device.minimed;

import java.util.Hashtable;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.pump.device.AbstractPump;

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
 *  Filename:     AccuChekSpirit  
 *  Description:  Accu Chek Spirit Pump Implementation
 * 
 *  Author: Andy {andy@atech-software.com}
 */


// FIXME

public abstract class MinimedPump extends AbstractPump
{

    public static final int PUMP_MINIMED_508      = 10001;
    public static final int PUMP_MINIMED_512_712  = 10002;
    public static final int PUMP_MINIMED_522_722  = 10003;
    public static final int PUMP_MINIMED_x54_VEO  = 10004;
    
    
    /**
     * Constructor 
     */
    public MinimedPump()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param drive_letter 
     * @param writer 
     */
    public MinimedPump(String params, OutputWriter writer)
    {
        super(); //params, writer);
    }
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************



    
    
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
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_NOT_AVAILABLE;
    }
    
    
    
    
    
    

    /**
     * Open
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
    }
    

    /**
     * Close
     */
    public void close() throws PlugInBaseException
    {
    }
   
    

    /** 
     * This is method for reading configuration, in case that dump doesn't give this information.
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }


    /**
     * readDeviceDataFull - This is method for reading data from device. All reading from actual device should 
     * be done from here. Reading can be done directly here, or event can be used to read data. Usage of events 
     * is discouraged because reading takes 3-4x more time.
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
    }


    /**
     * This is method for reading partial data from device. This can be used if your device can be read partialy 
     * (from some date to another)
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
    }

    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }
  

    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific 
     *     alarm codes
     * @return
     */
    public Hashtable<String, Integer> getAlarmMappings()
    {
        return null;
    }


    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getBolusMappings()
    {
        return null;
    }


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getErrorMappings()
    {
        return null;
    }


    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getEventMappings()
    {
        return null;
    }

    
    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getReportMappings()
    {
        return null;
    }


    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }


    /** 
     * Dispose
     */
    public void dispose()
    {
    }

    
    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return null;
    }


    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public int getConnectionProtocol()
    {
        return 0;
    }


    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     * @return 
     */
    public String getDeviceSpecialComment()
    {
        return null;
    }


    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     *    
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }


    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return false;
    }


    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * 
     * @return
     */
    public boolean isReadableDevice()
    {
        return false;
    }

    
}