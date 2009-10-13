package ggc.meter.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;

import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterInterface
 *  Description:  This is interface class, used for meters. It should be primary implemented by abstract 
 *                protocol class. Each meter family "should" have it's own super class and one class for 
 *                each meter type instance.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface MeterInterface extends SelectableInterface, DeviceInterface
{

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


    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords();

    
    


    //************************************************
    //***        Available Functionality for Meter          ***
    //************************************************

    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo();
    
    
    /**
     * Meter Interface - Simple (for normal meter devices, which store only BG data)
     */
    public static final int METER_INTERFACE_SIMPLE =1;

    /**
     * Meter Interface - Extended (for meter devices, which store more then just BG data)
     */
    public static final int METER_INTERFACE_EXTENDED =2;
    
    
    /**
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but 
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display. 
     * @return
     */
    public int getInterfaceTypeForMeter();
    
    
    
}
