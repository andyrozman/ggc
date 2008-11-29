package ggc.meter.device;


import ggc.meter.data.MeterValuesEntry;
import ggc.meter.util.I18nControl;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import java.util.ArrayList;

import com.atech.graphics.dialogs.selector.ColumnSorter;
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
 *  Filename:     AbstractMeter
 *  Description:  Abstract Meter for creating MeterDevices
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractMeter implements MeterInterface, SelectableInterface
{

    AbstractDeviceCompany meter_company;

    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected ArrayList<MeterValuesEntry> data = null;
    

    /**
     * Constructor
     */
    public AbstractMeter()
    {
        super();
    }


    boolean can_read_data = false; 
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;
    
    
    public void dispose()
    {
    }
    
    
    
    /**
     * Set device allowed actions
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
                                        boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data; 
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_clear_data = can_clear_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }
    
    



    //************************************************
    //***                    Test                  ***
    //************************************************

    /** 
     * test
     */
    public void test()
    {
    }



    //************************************************
    //***        Available Functionality           ***
    //************************************************


    
    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData()
    {
        return this.can_read_data;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData()
    {
        return this.can_read_partitial_data;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     * 
     * @return true if action is allowed
     */
    public boolean canClearData()
    {
        return this.can_clear_data;
    }

    
    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo()
    {
        return this.can_read_device_info;
    }
    
    
    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration()
    {
        return this.can_read_device_configuration;
    }


    /** 
     * compareTo
     */
    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * getColumnCount
     */
    public int getColumnCount()
    {
        return 3;
    }


    String device_columns[] = { ic.getMessage("METER_COMPANY"), ic.getMessage("METER_DEVICE"), ic.getMessage("DEVICE_CONNECTION") }; 
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return device_columns[num-1];
    }


    /** 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        switch(num)
        {
         
            case 1:
                return this.getName();
                
            case 2:
                return this.getDeviceCompany().getConnectionSamples();

            case 0:
            default:    
                return this.getDeviceCompany().getName();
                
                
        }
    }


    /** 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    /** 
     * getColumnWidth
     */
    public int getColumnWidth(int num, int width)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /** 
     * getItemId
     */
    public long getItemId()
    {
        return 0;
    }


    /** 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /** 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /** 
     * isFound
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /** 
     * isFound
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /** 
     * setColumnSorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /** 
     * setSearchContext
     */
    public void setSearchContext()
    {
    }


    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.meter_company = company;
    }
    
    
    /**
     * getDeviceCompany - get Company for device
     * 
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.meter_company;
    }

    
}
