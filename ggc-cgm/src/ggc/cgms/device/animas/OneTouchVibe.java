package ggc.cgms.device.animas;

import ggc.cgms.device.animas.impl.AnimasCGMSDeviceReader;
import ggc.cgms.manager.CGMSDevicesIds;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     OneTouchVibe
 *  Description:  One Touch Vibe CGMS implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class OneTouchVibe extends AnimasCGMS
{
    String communicationPort;


    /**
     * Constructor
     *
     * @param communicationPort
     * @param writer
     */
    public OneTouchVibe(String communicationPort, OutputWriter writer)
    {
        super(communicationPort, writer);
        this.communicationPort = communicationPort;
    }


    /**
     * Constructor
     *
     * @param cmp
     */
    public OneTouchVibe(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return "Vibe";
    }


    /**
     * {@inheritDoc}
     */
    public String getIconName()
    {
        return "an_vibe.png";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AnimasDeviceType getAnimasDeviceType()
    {
        return AnimasDeviceType.Animas_Vibe;
    }



    /**
     * {@inheritDoc}
     */
    public int getDeviceId()
    {
        return CGMSDevicesIds.CGMS_ANIMAS_VIBE_DEXCOM_INTEGRATION;
    }


    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     *
     * @return instructions for reading data
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ANIMAS_V2";
    }

    /**
     * getComment - Get Comment for device
     *
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }



    /**
     * getImplementationStatus - Get Implementation Status
     *
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_PARTITIAL;
    }


    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     *
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return this.getClass().getName();
    }


    /**
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }



    /**
     * Get Download Support Type
     *
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_CONFIG_FROM_DEVICE | DownloadSupportType.DOWNLOAD_FROM_DEVICE;
    }


    /**
     * How Many Months Of Data Stored
     *
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE;
    }

    public String getConnectionPort()
    {
        return this.communicationPort;
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        try
        {
            AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader(this.communicationPort, this.getAnimasDeviceType(), this.outputWriter);
            reader.downloadCGMSSettings();
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }



    @Override
    public void readDeviceDataFull() throws PlugInBaseException
    {
        try
        {
            AnimasCGMSDeviceReader reader = new AnimasCGMSDeviceReader(this.communicationPort, this.getAnimasDeviceType(), this.outputWriter);
            reader.downloadCGMSData();
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }



}

