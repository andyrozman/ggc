package ggc.meter.device;


import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.utils.file.FileReaderContext;


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
    //protected I18nControlAbstract ic = DataAccessMeter.getInstance().getI18nControlInstance();
    protected OutputWriter output_writer;
    //protected ArrayList<MeterValuesEntry> data = null;
    protected DataAccessMeter m_da = null;

    /**
     * Constructor
     */
    public AbstractMeter()
    {
        super();
        m_da = DataAccessMeter.getInstance();
    }


    /**
     * Constructor
     * @param cmp
     */
    public AbstractMeter(AbstractDeviceCompany cmp)
    {
        super();
        m_da = DataAccessMeter.getInstance();
        this.setDeviceCompany(cmp);
        this.setMeterType(cmp.getName(), getName());
    }
    
    
    
    
    boolean can_read_data = false; 
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;
    
    
    /**
     * Dispose instance
     * 
     * @see ggc.plugin.device.DeviceInterface#dispose()
     */
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
    
    
    /**
     * Set Meter type
     * 
     * @param group
     * @param device
     */
    public void setMeterType(String group, String device)
    {
        //this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification();
        di.company = group;
        di.device_selected = device;
        
        if (this.output_writer!=null)
            this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
        //this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
        
        this.device_source_name = group + " " + device;
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
     * Get Column Count
     */
    public int getColumnCount()
    {
        return m_da.getPluginDeviceUtil().getColumnCount();
    }
    
    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnName(num);
    }    
    
    
    /** 
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {
        return m_da.getPluginDeviceUtil().getColumnWidth(num, width);
    }
    
    
    /** 
     * getColumnValue - get Value of column, for configuration
     */
    public String getColumnValue(int num)
    {
        return m_da.getPluginDeviceUtil().getColumnValue(num, this);
    }
    

//    String device_columns[] = { "DEVICE_COMPANY", "DEVICE_DEVICE", "DEVICE_CONNECTION" }; 
    


    /** 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
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
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but 
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display. 
     * @return
     */
    public int getInterfaceTypeForMeter()
    {
        return MeterInterface.METER_INTERFACE_SIMPLE;
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
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }
    
}
