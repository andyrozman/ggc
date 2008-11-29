package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import java.util.Hashtable;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterInterface
 *  Purpose:  This is interface class, used for meters. It should be primary implemented by protocol class, and 
 *       protocol class should be used as super class for meter definitions. Each meter family "should" 
 *       have it's own super class and one class for each meter.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 */


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


public interface PumpInterface extends DeviceInterface
{


    //************************************************
    //***          Working with device             ***
    //************************************************


    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     * @throws PlugInBaseException 
     */
    boolean open() throws PlugInBaseException;


    /**
     * Will be called, when the import is ended and freeing resources.
     * @throws PlugInBaseException 
     */
    void close() throws PlugInBaseException;



    //************************************************
    //***             Meter Methods                ***
    //************************************************
    
    
    

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
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String,Integer> getAlarmMappings();
    
    
    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String,Integer> getEventMappings();
    

    //************************************************
    //***        Should bne implemnetyed by Abstract Meter ter          ***
    //************************************************


    //************************************************
    //***        Available Functionality for Meter          ***
    //************************************************

    
    
    /**
     * setDeviceAllowedActions - sets actions which are allowed by implementation
     *   of MeterInterface (actually of GenericMeterXXXXX classes)
     *   
     * @param can_read_data
     * @param can_read_partitial_data
     * @param can_read_device_info
     * @param can_read_device_configuration
     */
    public void setDeviceAllowedActions(boolean can_read_data, 
                                        boolean can_read_partitial_data,
//                                      boolean can_clear_data,
                                        boolean can_read_device_info,
                                        boolean can_read_device_configuration);
    
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo();
    
    
    

    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData();

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData();


    
    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo();
    
    
    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration();


    //************************************************
    //***                    Test                  ***
    //************************************************

    void test();


    //************************************************
    //***          Company Specific Settings                ***
    //************************************************

    
    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company);
    
    
    /**
     * getDeviceCompany - get Company for device
     */
    public AbstractDeviceCompany getDeviceCompany();
    

}
