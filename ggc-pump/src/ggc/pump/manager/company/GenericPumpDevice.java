package ggc.pump.manager.company;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.device.AbstractPump;

import java.util.Hashtable;

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
 *  Filename:      Generic Pump Device  
 *  Description:   Generic Pump Device used for getting profile names for specified 
 *                 pump company.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class GenericPumpDevice extends AbstractPump
{

    /**
     * The apdc.
     */
    AbstractPumpDeviceCompany apdc;

    /**
     * Instantiates a new generic pump device.
     * 
     * @param apdc the apdc
     */
    public GenericPumpDevice(AbstractPumpDeviceCompany apdc)
    {
        this.apdc = apdc;
    }

    /**
     * getDeviceCompany - get Company for device
     * 
     * @return 
     */
    @Override
    public AbstractDeviceCompany getDeviceCompany()
    {
        return apdc;
    }

    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_NONE;
    }

    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return "NONE";
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
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    @Override
    public String getName()
    {
        return "Generic";
    }

    /** 
     * getProfileNames
     */
    @Override
    public String[] getProfileNames()
    {
        return this.apdc.getProfileNames();
    }

    /** 
     * close
     */
    public void close() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

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
     * open
     */
    public boolean open() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return false;
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
     * getDeviceSpecialComment
     */
    @Override
    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * getIconName
     */
    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
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
     * getInstructions
     */
    public String getInstructions()
    {
        // TODO Auto-generated method stub
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
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_SUPPORT_NA_GENERIC_DEVICE;
    }

    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    @Override
    public int howManyMonthsOfDataStored()
    {
        return -1;
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
