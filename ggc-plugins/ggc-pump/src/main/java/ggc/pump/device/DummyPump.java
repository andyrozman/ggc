package main.java.ggc.pump.device;

import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import main.java.ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DummyPump extends AbstractPump // implements PumpInterface
                                            // //extends GenericPump
                                            // //implements MeterInterface
{

    /**
     * The dataAccess.
     */
    DataAccessPump m_da = DataAccessPump.getInstance();

    /**
     * The m_ic.
     */
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    /**
     * Constructor
     */
    public DummyPump()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param ow
     */
    public DummyPump(OutputWriter ow)
    {
        super(ow);
        // super(ow);
    }

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open()
    {
        return true;
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        return;
    }

    /**
     * Log: Debug
     */
    public static final int LOG_DEBUG = 1;

    /**
     * Log: Info
     */
    public static final int LOG_INFO = 2;

    /**
     * Log: Warn
     */
    public static final int LOG_WARN = 3;

    /**
     * Log: Error
     */
    public static final int LOG_ERROR = 4;

    /**
     * Write Log
     * 
     * @param problem
     * @param text
     */
    public void writeLog(int problem, String text)
    {
        System.out.println("Dummy -> " + text);
    }

    /**
     * @return
     */
    public String getInfo() // throws MeterException
    {
        writeLog(LOG_DEBUG, "getVersion() - Start");
        writeLog(LOG_DEBUG, "getVersion() - End");
        return "Dummy Meter\n" + "v0.1\n" + m_ic.getMessage("DUMMY_INFO_TEXT");
    }

    /**
     * Get Name
     * 
     * @return
     */
    @Override
    public String getName()
    {
        return "Dummy Meter";
    }

    // ************************************************
    // *** Available Functionality ***
    // ************************************************

    /**
     * canReadData - Can Meter Class read data from device
     * @return 
     */
    @Override
    public boolean canReadData()
    {
        return false;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * @return 
     */
    @Override
    public boolean canReadPartitialData()
    {
        return false;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     * @return 
     */
    public boolean canClearData()
    {
        return false;
    }

    // ************************************************
    // *** Test ***
    // ************************************************

    /**
     * 
     */
    @Override
    public void test()
    {
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
     * getDeviceInfo
     */
    @Override
    public DeviceIdentification getDeviceInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * getMaxMemoryRecords
     */
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    /** 
     * dispose
     */
    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    /** 
     * getComment
     */
    public String getComment()
    {
        return null;
    }

    /** 
     * getConnectionPort
     */
    public String getConnectionPort()
    {
        return null;
    }

    /** 
     * getConnectionProtocol
     */
    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return DeviceConnectionProtocol.None;
    }


    /** 
     * getDeviceClassName
     */
    public String getDeviceClassName()
    {
        return null;
    }

    /** 
     * getDeviceId
     */
    public int getDeviceId()
    {
        return 0;
    }

    /** 
     * getDeviceSpecialComment
     */
    @Override
    public String getDeviceSpecialComment()
    {
        return "DEVICE_DUMMY_SPECIAL_COMMENT";
    }

    /** 
     * getIconName
     */
    public String getIconName()
    {
        return null;
    }

    /** 
     * getImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }

    /** 
     * getInstructions
     */
    public String getInstructions()
    {
        return null;
    }

    /** 
     * hasSpecialProgressStatus
     */
    @Override
    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * isDeviceCommunicating
     */
    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * readConfiguration
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    /** 
     * readDeviceDataPartitial
     */
    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    /** 
     * readInfo
     */
    @Override
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    @Override
    public String getTemporaryBasalTypeDefinition()
    {
        // return "TYPE=Unit;STEP=0.1";
        return null;
    }

    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    public float getBolusStep()
    {
        return 0.1f;
    }

    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep()
    {
        return 0.1f;
    }

    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    @Override
    public boolean arePumpSettingsSet()
    {
        return false;
    }

}
