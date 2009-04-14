package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;

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
 *  Filename:     PumpInterface
 *  Description:  This is interface class, used for pumps. It should be primary implemented by abstract 
 *                protocol class. Each pump family "should" have it's own super class and one class for 
 *                each pump type instance.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface PumpInterface extends DeviceInterface
{


    //************************************************
    //***          Working with device             ***
    //************************************************


    /**
     * Used for opening connection with device.
     * 
     * @return boolean - if connection established
     * @throws PlugInBaseException 
     */
    boolean open() throws PlugInBaseException;


    /**
     * Used for closing connection with device
     * 
     * @throws PlugInBaseException 
     */
    void close() throws PlugInBaseException;


    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************

    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords();

    

    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues(); 
    
    
    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific 
     *     alarm codes
     * @return
     */
    public Hashtable<String,Integer> getAlarmMappings();
    
    
    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getEventMappings();
    
    
    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getErrorMappings();
    

    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getBolusMappings();
    

    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getReportMappings();
    
    
    
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo();
    

    
    public int getDownloadSupportType();
    
    
    public String getTemporaryBasalTypeDefinition();
    

}
