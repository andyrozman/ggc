package ggc.plugin.manager;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.utils.file.FileReaderContext;

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


public class EmptyMgrDevices implements DeviceInterface //extends DummyDevice
{




    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


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
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * Should be implemented by meter class.
     * 
     * @return global id of meter
     */
    public int getGlobalMeterId()
    {
        return 0;
    }

    
    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return id of company
     */
    public int getCompanyId()
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
    public int getImplementationStatus()
    {
        return 0;
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#canReadConfiguration()
     */
    public boolean canReadConfiguration()
    {
        return false;
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#canReadData()
     */
    public boolean canReadData()
    {
        return false;
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#canReadDeviceInfo()
     */
    public boolean canReadDeviceInfo()
    {
        return false;
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
    public int getConnectionProtocol()
    {
        return 0;
    }

    /**
     * @see ggc.plugin.device.DeviceInterface#getDeviceSpecialComment()
     */
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
     * @see ggc.plugin.device.DeviceInterface#hasSpecialProgressStatus()
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
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
     * @see ggc.plugin.device.DeviceInterface#readDeviceDataPartitial()
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#readInfo()
     */
    public void readInfo() throws PlugInBaseException
    {
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#setDeviceAllowedActions(boolean, boolean, boolean, boolean)
     */
    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#test()
     */
    public void test()
    {
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#compareTo(com.atech.graphics.dialogs.selector.SelectableInterface)
     */
    public int compareTo(SelectableInterface o)
    {
        return 0;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getColumnCount()
     */
    public int getColumnCount()
    {
        return 0;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getColumnName(int)
     */
    public String getColumnName(int num)
    {
        return null;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getColumnValue(int)
     */
    public String getColumnValue(int num)
    {
        return null;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getColumnValueObject(int)
     */
    public Object getColumnValueObject(int num)
    {
        return null;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getColumnWidth(int, int)
     */
    public int getColumnWidth(int num, int width)
    {
        return 0;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getItemId()
     */
    public long getItemId()
    {
        return 0;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getShortDescription()
     */
    public String getShortDescription()
    {
        return null;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#isFound(java.lang.String)
     */
    public boolean isFound(String text)
    {
        return false;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#isFound(int)
     */
    public boolean isFound(int value)
    {
        return false;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#isFound(int, int, int)
     */
    public boolean isFound(int from, int till, int state)
    {
        return false;
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#setColumnSorter(com.atech.graphics.dialogs.selector.ColumnSorter)
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /**
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#setSearchContext()
     */
    public void setSearchContext()
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
     * @see ggc.plugin.device.DeviceInterface#getDeviceCompany()
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return null;
    }


    /**
     * @see ggc.plugin.device.DeviceInterface#setDeviceCompany(ggc.plugin.manager.company.AbstractDeviceCompany)
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
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
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }


    String device_source_name;
    
    /**
     * Get Device Source Name
     * 
     * @return
     */
    public String getDeviceSourceName()
    {
        return device_source_name;
    }


    /** 
     * Get Download SupportType Configuration
     */
    public int getDownloadSupportTypeConfiguration()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NO;
    }


    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    public FileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }


    /**
     * Does this device support file download. Some devices have their native software, which offers export 
     * into some files (usually CSV files or even XML). We sometimes add support to download from such
     * files, and in some cases this is only download supported. 
     *  
     * @return
     */
    public boolean isFileDownloadSupported()
    {
        return false;
    }
    
    
    
    
    
    
    
    
}
