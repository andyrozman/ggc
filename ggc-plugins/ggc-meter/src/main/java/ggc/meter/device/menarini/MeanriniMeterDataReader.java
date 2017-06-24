package ggc.meter.device.menarini;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.menarini.impl.*;
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
 *  Filename:     AscensiaBreeze2
 *  Description:  Data/device reader for Arkray GlucoCard
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MeanriniMeterDataReader extends SerialDeviceReader
{

    private static final Logger LOGGER = LoggerFactory.getLogger(MeanriniMeterDataReader.class);

    private MeterDeviceDefinition deviceDefinition;
    private MenariniSerialMeterAbstract menariniMeter;


    public MeanriniMeterDataReader(DeviceDefinition definition, String portName, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        super(portName, outputWriter);

        this.deviceDefinition = (MeterDeviceDefinition) definition;
        configureProgressReporter(ProgressType.Static, 100, this.deviceDefinition.getMaxRecords(), 0);

        createDeviceInstance();
    }


    private void createDeviceInstance()
    {
        menariniMeter = getCorrectDevice();

        if (menariniMeter == null)
        {
            LOGGER.error("Menarini device instance could not be created.");
        }
    }


    private MenariniSerialMeterAbstract getCorrectDevice()
    {
        switch (this.deviceDefinition)
        {
            case MenariniGlucoFixId:
            case MenariniGlucoMenGM:
                return new MenariniGlucoMenGM(this);

            case MenariniGlucoFixMio:
                return new MenariniGlucoMenLXMio(this);

            case MenariniGlucoMenAreo2k:
            case MenariniGlucoMenLx2:
            case MenariniGlucoMenLxPlus:
            case MenariniGlucoFixPremium:
            case MenariniGlucoFixMioPlus:
            case MenariniGlucoMenAreo: // no ket (?)
            case MenariniGlucoFixTech: // NOT SURE, which type
                return new MenariniGlucoMenLXMioPlus(this);

            case MenariniGlucoMenReady:
            case MenariniGlucoMenMendor:
                return new MenariniGlucoMenReady(this);

            case MenariniGlucoMenPC:
                return new MenariniGlucoMenPC(this);

            case MenariniGlucoMenVisio:
                return new MenariniGlucoMenVisio(this);

            default:
                return null;
        }
    }


    @Override
    public void readData() throws PlugInBaseException
    {
        if (menariniMeter != null)
        {
            menariniMeter.readDeviceData();
        }
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {

    }


    @Override
    public void closeDevice() throws PlugInBaseException
    {
        if (menariniMeter != null)
        {
            this.menariniMeter.closeDevice();
        }
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

}
