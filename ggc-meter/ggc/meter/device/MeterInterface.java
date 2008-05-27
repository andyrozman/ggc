package ggc.meter.device;

import ggc.meter.data.MeterValuesEntry;

import java.util.ArrayList;

import javax.swing.ImageIcon;


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


public interface MeterInterface
{


    //************************************************
    //***          Working with device             ***
    //************************************************


    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    boolean open() throws MeterException;


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    void close() throws MeterException;


    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    void readDeviceData() throws MeterException;



    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * Should be implemented by meter class.
     * 
     * @return name of meter
     */
    String getName();


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcom
     */
    ImageIcon getIcon();

    
    /**
     * getIconName - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return icon name
     */
    String getIconName();
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return id of meter within company
     */
    int getMeterId();

    
    /**
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return global id of meter
     */
    int getGlobalMeterId();

    
    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return id of company
     */
    int getCompanyId();
    
    
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
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    int getImplementationStatus(); 
    
    
    
    
    
    
    
    
    String getPort();


    //************************************************
    //***           Meter GUI Methods              ***
    //************************************************


    /**
     * getTimeDifference - returns time difference between Meter and Computer. 
     * This data should be read from meter, and is used in Meter GUI
     */
    int getTimeDifference(); 


    /**
     * getInfo - returns Meter information. 
     * This data should be read from meter, and is used in Meter GUI
     */
    String getInfo(); 

    
    /**
     * getStatus - get Status of meter
     * This data should be read from meter, and is used in Meter GUI
     */
    int getStatus();


    /**
     * isStatusOK - has Meter OK status
     */
    boolean isStatusOK();
    
    
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
    									boolean can_clear_data,
    									boolean can_read_device_info,
    									boolean can_read_device_configuration);
    
    
    
    

    //************************************************
    //***       Device Implemented methods         ***
    //************************************************
    

    /** 
     * clearDeviceData - Clear data from device 
     */
    void clearDeviceData() throws MeterException;
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    ArrayList<String> getDeviceInfo() throws MeterException;
    
    
    /**
     * getDeviceConfiguration - return device configuration
     * @return
     */
    ArrayList<String> getDeviceConfiguration() throws MeterException;
    
    
    /**
     * getDataFull - get all data from Meter
     * This data should be read from meter, and is used in Meter GUI
     */
    ArrayList<MeterValuesEntry> getDataFull() throws MeterException;


    /**
     * getData - get data for specified time
     * This data should be read from meter and preprocessed, and is used in Meter GUI
     */
    ArrayList<MeterValuesEntry> getData(int from, int to) throws MeterException;


    //************************************************
    //***          Process Meter Data              ***
    //************************************************


    /**
     * processMeterDataMain - this is main method for processing data. It should be called on all data received, and 
     * from here it should be sent to other process* methods. This methods are meant to be used, but don't have to 
     * be used if we have other ways to get data for methods needed (methods marked as used in Meter GUI)
     */
    //void processMeterData(String data);

    /**
     * processMeterIdentification - this should be used to process identification of meter and versions of firmware.
     */
//    void processMeterIdentification(String data);

    /**
     * processMeterTime - this should be used to process time and date of meter
     */
//    void processMeterTime(String data);

    /**
     * processMeterBGEntry - this should be used to process BG data from meter
     */
//    void processMeterBGEntry(String data);



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
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData();

    /**
     * canClearData - Can Meter class clear data from meter.
     * 
     * @return true if action is allowed
     */
    public boolean canClearData();

    
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


}
