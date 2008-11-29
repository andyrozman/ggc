package ggc.cgm.device;

import ggc.cgm.util.I18nControl;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

/**
 * Application: GGC - GNU Gluco Control 
 * Plug-in: CGMS Tool (support for CGMS devices)
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename:     GenericCGM 
 * Description:  This is Generic implementation of CGMS, it should contain all methods (implemented),
 *               is used for Dummy device, maybe later for some more
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class GenericCGM implements CGMInterface
{

    protected I18nControl ic = I18nControl.getInstance();


    /**
     * Constructor
     */
    public GenericCGM()
    {
        super();
    }

    boolean can_read_data = false;
    boolean can_read_partitial_data = false;
    boolean can_clear_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;

    
    /** 
     * setDeviceAllowedActions
     */
    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data, boolean can_read_device_info, boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data;
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;

    }

    /** 
     * open
     */
    public boolean open()
    {
        return false;

    }

    /*
    public GenericMeter(int meter_type, String portName)
    {

    super(meter_type,
          9600, 
          SerialPort.DATABITS_8, 
          SerialPort.STOPBITS_1, 
          SerialPort.PARITY_NONE);

    data = new ArrayList<DailyValuesRow>();

    try
    {
        this.setPort(portName);

        if (!this.open())
        {
    	this.m_status = 1;
        }
    }
    catch(Exception ex)
    {
        System.out.println("AscensiaMeter -> Error adding listener: " + ex);
        ex.printStackTrace();
    }
    }
    */
    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    // @Override
    /*    public boolean open() throws MeterException
        {
            return super.open();
    	//return false;
            //return true;
        }
    */

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    // @Override
    public void close()
    {
        return;
    }


    /** 
     * getIconName
     */
    public String getIconName()
    {
        return null;
    }

    /**
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    public String getName()
    {
        return "Generic device";
    }





    // ************************************************
    // *** Available Functionality ***
    // ************************************************

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

    // ************************************************
    // *** Test ***
    // ************************************************

    /** 
     * test
     */
    public void test()
    {
    }

    AbstractDeviceCompany company;

    /** 
     * setDeviceCompany
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.company = company;
    }

    /** 
     * getDeviceCompany
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.company;

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
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return null;
    }

    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        return;
    }

    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        return;
    }

    /** 
     * This is method for reading configuration
     * 
     */
    public void readConfiguration() throws PlugInBaseException
    {
        return;
    }

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     */
    public void readInfo() throws PlugInBaseException
    {
        return;
    }

    /** 
     * getConnectionProtocol
     */
    public int getConnectionProtocol()
    {
        return 0;
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
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * getShortDescription
     */
    public String getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * isFound
     */
    public boolean isFound(String text)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * isFound
     */
    public boolean isFound(int value)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * setColumnSorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
        // TODO Auto-generated method stub

    }

    /** 
     * setSearchContext
     */
    public void setSearchContext()
    {
        // TODO Auto-generated method stub

    }

    /** 
     * getComment
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * getDeviceClassName
     */
    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getDeviceId
     */
    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * getConnectionPort
     */
    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

}
