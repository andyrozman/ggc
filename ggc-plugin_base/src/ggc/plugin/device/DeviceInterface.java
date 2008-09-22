package ggc.plugin.device;

import com.atech.graphics.dialogs.selector.SelectableInterface;


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


public interface DeviceInterface extends SelectableInterface
{


    //************************************************
    //***          Working with device             ***
    //************************************************


    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    //boolean open() throws PlugInBaseException;


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //void close() throws PlugInBaseException;


    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    void readDeviceData() throws PlugInBaseException;



    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************

    //void loadInitialData();
    //void readCommData();

    /**
     * getName - Get Name of device. 
     * Should be implemented by protocol class.
     */
    String getName();


    /**
     * getIcon - Get Icon of device
     * Should be implemented by protocol class.
     */
    String getIconName();


    /**
     * getDeviceId - Get Device Id, this are plugin specific and global (for example only one device 
     * of type meter, can have same id.  
     * Should be implemented by protocol class.
     */
    int getDeviceId();

    
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
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    int getImplementationStatus(); 
    
   

    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    String getDeviceClassName();
    
    //String getPort();
    

    //************************************************
    //***           Meter GUI Methods              ***
    //************************************************



    
    /**
     * setDeviceAllowedActions - sets actions which are allowed by implementation
     *   of MeterInterface (actually of GenericMeterXXXXX classes)
     *   
     * @param can_read_data
     * @param can_read_partitial_data
     * @param can_clear_data
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
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    void readDeviceDataFull() throws PlugInBaseException;
    
    
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    void readDeviceDataPartitial() throws PlugInBaseException;


    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    void readConfiguration() throws PlugInBaseException;
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    void readInfo() throws PlugInBaseException;
    

    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment();
    
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus();
    
    

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

    void test();

    /*
    public void setCGMCompany(AbstractCGMCompany company);
    
    
    public AbstractCGMCompany getCGMCompany();
    */
    
    public int getConnectionProtocol();

    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    String getInstructions();
    
    

}
