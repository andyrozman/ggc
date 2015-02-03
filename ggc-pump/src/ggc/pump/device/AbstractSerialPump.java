package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.util.DataAccessPump;
import gnu.io.SerialPortEvent;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:      AbstractSerialPump
 *  Description:   Abstract class for Pump using Serial Interface (extends SerialProtocol
 *       and implements PumpInterface
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractSerialPump extends SerialProtocol implements PumpInterface
{

    boolean communication_established = false;

    /**
     * Constructor
     */
    public AbstractSerialPump()
    {
        super(DataAccessPump.getInstance());
    }

    /**
     * Constructor
     * 
     * @param i2
     * @param i3
     * @param i4
     * @param i5
     */
    public AbstractSerialPump(int i2, int i3, int i4, int i5)
    {
        super(DataAccessPump.getInstance());
        // dataAccess = DataAccessPump.getInstance();
        // i18nControlAbstract = dataAccess.getI18nControlInstance();
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractSerialPump(AbstractDeviceCompany cmp)
    {
        super(DataAccessPump.getInstance());
        // dataAccess = DataAccessPump.getInstance();
        // i18nControlAbstract = dataAccess.getI18nControlInstance();

        this.setDeviceCompany(cmp);
        this.setPumpType(cmp.getName(), getName());
    }

    /**
     * Set Pump Type
     * 
     * @param group
     * @param device
     */
    public void setPumpType(String group, String device)
    {
        // this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(dataAccess.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.outputWriter != null)
        {
            this.outputWriter.setDeviceIdentification(di);
            // this.outputWriter.
            // this.device_instance =
            // MeterManager.getInstance().getMeterDevice(group, device);
        }

        this.deviceSourceName = group + " " + device;

    }

    // this.m_device_index = device_index;

    /**
     * Serial Event
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {

    }

    /**
     * Dispose
     */
    @Override
    public void dispose()
    {
        this.close();
    }

    /**
     * Set Communication Settings
     */
    @Override
    public void setCommunicationSettings(int baudrate, int databits, int stopbits, int parity, int flow_control,
            int event_type)
    {
        super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
    }

    /*
     * String meter_group = null;
     * String meter_device = null;
     * PumpInterface device_instance = null;
     * public void setMeterType(String group, String device)
     * {
     * this.device_name = device;
     * DeviceIdentification di = new DeviceIdentification(i18nControlAbstract);
     * di.company = group;
     * di.device_selected = device;
     * this.outputWriter.setDeviceIdentification(di);
     * //this.outputWriter.
     * //this.device_instance = MeterManager.getInstance().getMeterDevice(group,
     * device);
     * }
     */

    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return this.communication_established;
    }

    /*
     * public String getName()
     * {
     * return this.device_name;
     * }
     */

    String serial_port = null;

    /**
     * Set Serial Port
     * 
     * @param port
     */
    public void setSerialPort(String port)
    {
        System.out.println("port (ASP): " + port);

        this.serial_port = port;

        try
        {
            this.setPort(port);
        }
        catch (PlugInBaseException ex)
        {
            System.out.println("No Such Port Ex: " + ex);
            // throw new PlugInBaseException(ex);

        }

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
     * Get Serial Port
     * @return
     */
    public String getSerialPort()
    {
        return this.serial_port;
    }

    /*
     * public GenericMeter(int meter_type, String portName)
     * {
     * super(meter_type,
     * 9600,
     * SerialPort.DATABITS_8,
     * SerialPort.STOPBITS_1,
     * SerialPort.PARITY_NONE);
     * data = new ArrayList<DailyValuesRow>();
     * try
     * {
     * this.setPort(portName);
     * if (!this.open())
     * {
     * this.m_status = 1;
     * }
     * }
     * catch(Exception ex)
     * {
     * System.out.println("AscensiaMeter -> Error adding listener: " + ex);
     * ex.printStackTrace();
     * }
     * }
     */
    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    // @Override
    @Override
    public boolean open() throws PlugInBaseException
    {
        return communication_established = super.open();
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    @Override
    public void close()
    {
        if (this.serialPort == null)
            return;

        this.serialPort.removeEventListener();
        this.serialPort.close();
    }

}
