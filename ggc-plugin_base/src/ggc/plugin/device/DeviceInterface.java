package ggc.plugin.device;

import ggc.plugin.manager.company.AbstractDeviceCompany;

import com.atech.graphics.dialogs.selector.SelectableInterface;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename: DeviceInterface
 *  Description:  This is interface class, used for devices. It should be primary 
 *       implemented by protocol class, and protocol class should be used as super 
 *       class for meter definitions. Each meter family "should" have it's own super 
 *       class and one class for each meter.
 *
 *  Example:  for AscensiaContour meter (--> means extends) 
 *      DeviceInterface 
 *         |------>   AbstractSerialMeter (implements SerialProtocol)
 *                        |------>  AscensiaMeter (company meter driver)                
 *                                       |---> AscensiaContour (meter driver)
 * 
 *  Author: Andy {andy@atech-software.com}
 */



public interface DeviceInterface extends SelectableInterface
{


    //************************************************
    //***      Device Identification Methods       ***
    //************************************************


    /**
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    String getName();


    /**
     * getIcon - Get Icon of device
     * Should be implemented by device class.
     * @return 
     */
    String getIconName();


    /**
     * getDeviceId - Get Device Id, this are plugin specific and global (for example only one device 
     * of type meter, can have same id.  
     * Should be implemented by protocol class.
     * 
     * @return id of device
     */
    int getDeviceId();

    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    String getInstructions();
    
    
    /**
     * getComment - Get Comment for device 
     * Should be implemented by meter class.
     * 
     * @return comment or null
     */
    String getComment();
    

    
    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    int getImplementationStatus(); 
    
   

    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    String getDeviceClassName();
    
    
    /**
     * dispose this device interface
     */
    void dispose();
    

    //************************************************
    //***          Device GUI Methods              ***
    //************************************************


    /**
     * setDeviceAllowedActions - sets actions which are allowed by implementation
     *   of DeviceInterface (should be done by device class)
     *   
     * @param can_read_data
     * @param can_read_partitial_data
     * @param can_read_device_info
     * @param can_read_device_configuration
     */
    public void setDeviceAllowedActions(boolean can_read_data, 
    									boolean can_read_partitial_data,
    									boolean can_read_device_info,
    									boolean can_read_device_configuration);
    
    
    
    

    //************************************************
    //***       Device Implemented methods         ***
    //************************************************
    

    /**
     * readDeviceDataFull - This is method for reading data from device. All reading from actual device should 
     * be done from here. Reading can be done directly here, or event can be used to read data. Usage of events 
     * is discouraged because reading takes 3-4x more time.
     * 
     * @throws PlugInBaseException 
     */
    void readDeviceDataFull() throws PlugInBaseException;
    
    
    /**
     * This is method for reading partial data from device. This can be used if your device can be read partialy 
     * (from some date to another)
     * 
     * @throws PlugInBaseException 
     */
    void readDeviceDataPartitial() throws PlugInBaseException;


    /** 
     * This is method for reading configuration, in case that dump doesn't give this information.
     * 
     * @throws PlugInBaseException
     */
    void readConfiguration() throws PlugInBaseException;
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    void readInfo() throws PlugInBaseException;
    

    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     * @return 
     */
    public String getDeviceSpecialComment();
    
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     *    
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus();
    
    
    
    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating();

    

    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData();


    
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

    /**
     * Test
     */
    void test();


    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public int getConnectionProtocol();

    
    
    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort();
    
    
    
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
     * 
     * @return 
     */
    public AbstractDeviceCompany getDeviceCompany();

    
    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * 
     * @return
     */
    public boolean isReadableDevice();

    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType();
    
    
    /**
     * Get Device Source Name
     * 
     * @return
     */
    public String getDeviceSourceName();
    
    
}
