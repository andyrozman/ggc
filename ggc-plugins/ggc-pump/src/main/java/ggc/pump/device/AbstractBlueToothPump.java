package ggc.pump.device;

import java.util.Hashtable;

import javax.comm.SerialPortEvent;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.BlueToothProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.*;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:      AbstractBlueToothPump  
 *  Description:   Abstract implementation of pump, implementing with BlueTooth protocol
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AbstractBlueToothPump extends BlueToothProtocol implements PumpInterface // ,
                                                                                               // SelectableInterface
{

    // protected int m_status = 0;
    // protected I18nControlAbstract i18nControlAbstract = null;
    // //DataAccessPump.getInstance().getI18nControlInstance();

    // protected String m_info = "";
    // protected int m_time_difference = 0;
    protected String device_name = "Undefined";
    // protected OutputWriter outputWriter;

    // AbstractDeviceCompany pump_company = null;
    boolean communication_established = false;


    /**
     * Constructor
     */
    public AbstractBlueToothPump()
    {
        super(DataAccessPump.getInstance());
    }


    /**
     * Constructor
     * 
     * @param comm_parameters
     * @param writer
     * @param da 
     */
    public AbstractBlueToothPump(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }


    /**
     * Constructor
     * 
     * @param cmp
     */
    public AbstractBlueToothPump(AbstractDeviceCompany cmp, DataAccessPlugInBase da)
    {
        super(cmp, da);
        // super(cmp);

        // System.out.println("m_Da: " + dataAccess);
        // System.out.println("DAP: " + DataAccessPump.getInstance());

        // this.setDeviceCompany(cmp);
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
        this.device_name = device;

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
     * 
     * @param event 
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }


    /**
     * Dispose
     */
    public void dispose()
    {
        this.close();
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
     * @throws Exception 
     */
    public void setSerialPort(String port) throws Exception
    {
        System.out.println("port (ASP): " + port);

        this.serial_port = port;

        // try
        {
            this.setPort(port);
        }
        /*
         * catch(PlugInBaseException ex)
         * {
         * System.out.println("No Such Port Ex: " + ex);
         * throw new PlugInBaseException(ex);
         * }
         */

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
    // @Override
    @Override
    public void close()
    {
        if (this.serialPort == null)
            return;

        this.serialPort.removeEventListener();
        this.serialPort.close();
        this.serialPort = null;
    }


    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific
     *     alarm codes
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return null;
    }


    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpBolusType> getBolusMappings()
    {
        return null;
    }


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return null;
    }


    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpEventType> getEventMappings()
    {
        return null;
    }


    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpReport> getReportMappings()
    {
        return null;
    }


    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }

}
