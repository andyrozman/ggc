package ggc.pump.device.minimed;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.device.AbstractPump;
import ggc.pump.device.minimed.file.FRC_MinimedCarelink;

import java.util.Hashtable;

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
     * @param params 
     * @param writer 
     */
    public MinimedPump(String params, OutputWriter writer)
    {
        super(); //params, writer);
    }
    
    
    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da 
     */
    public MinimedPump(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public MinimedPump(AbstractDeviceCompany cmp)
    {
        super(cmp);
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

    
    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        //return DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE;
        return DownloadSupportType.DOWNLOAD_SUPPORT_NA_DEVICE; //.DOWNLOAD_FROM_DEVICE_FILE;
    }
    
    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }
    
    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    public boolean hasIndeterminateProgressStatus()
    {
        return true;
    }    
    
    
    public boolean hasDefaultParameter()
    {
        return false;
    }    
    
    
    /**
     * Load File Contexts - Load file contexts that device supports
     */
    public void loadFileContexts()
    {
//        System.out.println("loadFileContexts");
        this.file_contexts = new GGCPlugInFileReaderContext[1];
        this.file_contexts[0] = new FRC_MinimedCarelink(m_da, this.output_writer);
    }
    
    
    
    
}