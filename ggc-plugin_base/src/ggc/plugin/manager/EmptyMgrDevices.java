package ggc.plugin.manager;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import javax.swing.ImageIcon;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class EmptyMgrDevices extends DeviceAbstract // extends DummyDevice
{

    /**
     * Constructor
     * 
     * @param da
     */
    public EmptyMgrDevices(DataAccessPlugInBase da)
    {
        super(da);
    }

    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of meter. 
     * Should be implemented by meter class.
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "NO_DEVICES_AVAILABLE";
    }

    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcom
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    /**
     * getIconName - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "none";
    }

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return id of meter within company
     */
    public int getDeviceId()
    {
        return 0;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
    }

    /**
     * getComment - Get Comment for device 
     * Should be implemented by meter class.
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }

    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }


    /**
     * @throws PlugInBaseException
     */
    public void close() throws PlugInBaseException
    {
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#getConnectionProtocol()
     */
    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return DeviceConnectionProtocol.None;
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#getDeviceSpecialComment()
     */
    @Override
    public String getDeviceSpecialComment()
    {
        return null;
    }

    /**
     * @return
     */
    public String getPort()
    {
        return null;
    }

    /**
     * @return
     * @throws PlugInBaseException
     */
    public boolean open() throws PlugInBaseException
    {
        return false;
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#readConfiguration()
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#readDeviceDataFull()
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#getDeviceClassName()
     */
    public String getDeviceClassName()
    {
        return null;
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#getConnectionPort()
     */
    public String getConnectionPort()
    {
        return null;
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#dispose()
     */
    public void dispose()
    {
    }

    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return true;
    }

    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    @Override
    public boolean isReadableDevice()
    {
        return false;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NoDownloadSupport;
    }

}
