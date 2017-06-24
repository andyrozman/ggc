package ggc.meter.device.arkray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.arkray.impl.*;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.SerialDeviceReader;

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
 *  Filename:     ArkrayMeterDataReader
 *  Description:  Data reader for Arkray family
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ArkrayMeterDataReader extends SerialDeviceReader
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ArkrayMeterDataReader.class);

    private MeterDeviceDefinition deviceDefinition;

    private ArkrayUSBMeterAbstract arkrayUSBMeter = null;
    private ArkraySerialMeterAbstract arkrayMeter = null;


    public ArkrayMeterDataReader(DeviceDefinition definition, String portName, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        super(portName, outputWriter);

        this.deviceDefinition = (MeterDeviceDefinition) definition;
        configureProgressReporter(ProgressType.Static, 100, this.deviceDefinition.getMaxRecords(), 0);
    }


    @Override
    public void readData() throws PlugInBaseException
    {
        arkrayMeter = getCorrectDevice();

        if (arkrayMeter != null)
        {
            arkrayMeter.readDeviceData();
            return;
        }

        arkrayUSBMeter = getCorrectUSBDevice();

        if (arkrayUSBMeter == null)
        {
            LOGGER.error("Arkray device was not found.");
        }
        else
        {
            arkrayUSBMeter.readDeviceData();
        }
    }


    public ArkraySerialMeterAbstract getCorrectDevice()
    {
        switch (this.deviceDefinition)
        {
            case ArkrayGT_1820:
            case ArkrayGT_1810:
                return new ArkrayGlucoMeter18xx(this);

            case ArkrayGlucoXMeter:
                return new ArkrayGlucoXMeter(this);

            // case ArkrayGlucoCardMemoryPC:
            // return new ArkrayGlucoCardMemoryPC(this);

            case ArkrayGlucoCardMX:
                return new ArkrayGlucocardMX(this);

            // case ArkrayGlucoCard:
            // return new ArkrayGlucoCard(this);

            case ArkrayGlucoCardSM:
            default:
                return null;
        }
    }


    public ArkrayUSBMeterAbstract getCorrectUSBDevice()
    {
        switch (this.deviceDefinition)
        {
            case ArkrayGlucoCardSM:
                return new ArkrayGlucoCardSM(this);

            default:
                return null;
        }
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {

    }


    @Override
    public void closeDevice() throws PlugInBaseException
    {
        if (arkrayMeter != null)
            arkrayMeter.closeDevice();

        if (arkrayUSBMeter != null)
            arkrayUSBMeter.closeDevice();
    }


    @Override
    public void customInitAndChecks() throws PlugInBaseException
    {

    }


    @Override
    public void configureProgressReporter()
    {

    }


    public DataAccessMeter getDataAccess()
    {
        return DataAccessMeter.getInstance();
    }


    public MeterDeviceDefinition getDeviceDefinition()
    {
        return deviceDefinition;
    }

    // Unknown("X"), //
    // GT_1810("GT-1810"), //
    // GT_1820("GT-1820"), //
    // GlucoXMeter("GT-1910"), //
    // GlucoCardMemoryPC("GT-1650"), //
    // GlucoCardMX("GT-1960"), //
    // GlucoCardSM(""), //
    // GlucoCard(""), //

}
