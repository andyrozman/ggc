package ggc.meter.device;

import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

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
 *  Filename:     AbstractSerialMeter
 *  Description:  This abstract class for all meters using serial interface
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AbstractSerialMeter extends SerialProtocol implements MeterInterface, SelectableInterface
{

    protected I18nControl ic = I18nControl.getInstance();
    protected OutputWriter output_writer;
    AbstractDeviceCompany device_company = null;
    protected int m_status = 0;
    protected boolean communication_established = false;
    
    
    /**
     * Constructor
     */
    public AbstractSerialMeter()
    {
        super();
    }

    
    /**
     * Constructor
     * @param cmp
     */
    public AbstractSerialMeter(AbstractDeviceCompany cmp)
    {
        this.setDeviceCompany(cmp);
    }
    
    

    /**
     * Constructor
     * @param da
     */
    public AbstractSerialMeter(DataAccessMeter da)
    {
        super(da);
    }


    /** 
     * Serial Event - for handling serial events, this method is called internally
     */
    public void serialEvent(SerialPortEvent event)
    {

    }

    boolean can_read_data = false;
    boolean can_read_partitial_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;

    /** 
     * Set Device Allowed Actions
     */
    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data, boolean can_read_device_info, boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data;
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }

    /** 
     * Set Communication Settings
     */
    public void setCommunicationSettings(int baudrate, int databits, int stopbits, int parity, int flow_control, int event_type)
    {
        super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
    }

    String meter_group = null;
    String meter_device = null;

    /**
     * Dispose this instance
     */
    public void dispose()
    {
        this.close();
    }

    /**
     * Set meter type
     * 
     * @param group
     * @param device
     */
    public void setMeterType(String group, String device)
    {
        //this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(DataAccessMeter.getInstance().getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        this.output_writer.setDeviceIdentification(di);
        // this.output_writer.
        // this.device_instance =
        // MeterManager.getInstance().getMeterDevice(group, device);
    }


    String serial_port = null;

    /**
     * Set Serial Port used
     * 
     * @param port
     * @throws PlugInBaseException
     */
    public void setSerialPort(String port) throws PlugInBaseException
    {
        this.serial_port = port;
        this.setPort(port);
    }

    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return this.serial_port;
    }

    /**
     * Get Serial port
     * 
     * @return
     */
    public String getSerialPort()
    {
        return this.serial_port;
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
     * Used for opening connection with device.
     * 
     * @return boolean - if connection established
     */
    public boolean open() throws PlugInBaseException
    {
        return (communication_established = super.open());
    }

    
    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return this.communication_established;
    }
    
    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        if (this.serialPort == null)
            return;

        this.serialPort.removeEventListener();
        this.serialPort.close();
    }


    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return "";
    }

    // ************************************************
    // *** Device Implemented methods ***
    // ************************************************

    /** 
     * clearDeviceData - Clear data from device 
     */
    public void clearDeviceData()
    {

    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
    }



    
    /**
     * Wait for x ms
     * @param time
     */
    public void waitTime(long time)
    {
        try
        {
            Thread.sleep(time);

        }
        catch (Exception ex)
        {
        }
    }

    
    protected void deviceDisconnected()
    {
        this.output_writer.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
        this.output_writer.endOutput();
    }
    
    

    // ************************************************
    // *** Process Meter Data ***
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
        return device_columns[num - 1];
    }

    /** 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        switch (num)
        {

        case 2:
            return this.getName();

        case 3:
            return this.getDeviceCompany().getConnectionSamples();

        case 1:
        default:
            {
     //           System.out.println("name: " + this.getName());
                return this.getDeviceCompany().getName();
            }

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
        this.device_company = company;
    }

    /**
     * getDeviceCompany - get Company for device
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.device_company;
    }

    
    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice()
    {
        return true;
    }

    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_YES;
    }
    
    
}
