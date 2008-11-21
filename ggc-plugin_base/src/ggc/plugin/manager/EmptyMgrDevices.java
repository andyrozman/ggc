package ggc.plugin.manager;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
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


    public boolean canReadConfiguration()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean canReadData()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean canReadDeviceInfo()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void close() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public int getCGMIndex()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getPort()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean open() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceData() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
        // TODO Auto-generated method stub
        
    }


    public void test()
    {
        // TODO Auto-generated method stub
        
    }


    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public int getColumnCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getColumnName(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getColumnValue(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Object getColumnValueObject(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }


    public int getColumnWidth(int num, int width)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean isFound(String text)
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isFound(int value)
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isFound(int from, int till, int state)
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void setColumnSorter(ColumnSorter cs)
    {
        // TODO Auto-generated method stub
        
    }


    public void setSearchContext()
    {
        // TODO Auto-generated method stub
        
    }


    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public AbstractDeviceCompany getDeviceCompany()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        // TODO Auto-generated method stub
        
    }


    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
    
    


}
