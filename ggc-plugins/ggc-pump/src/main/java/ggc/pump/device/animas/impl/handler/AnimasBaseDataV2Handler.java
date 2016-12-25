package main.java.ggc.pump.device.animas.impl.handler;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.handler.AbstractDeviceDataV2Handler;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;
import main.java.ggc.pump.device.animas.impl.converter.AnimasBaseDataV2Converter;
import main.java.ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;

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
 *  Filename:     AnimasBaseDataV2Handler
 *  Description:  Animas Base Data V2 Handler (for Implementation V2)
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasBaseDataV2Handler extends AbstractDeviceDataV2Handler
{

    private static final Logger LOG = LoggerFactory.getLogger(AnimasBaseDataV2Handler.class);

    AnimasPumpDeviceData data;
    AnimasBaseDataV2Converter baseDataConverter;


    public AnimasBaseDataV2Handler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader,
            OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
    }


    @Override
    public void initLocal()
    {
        this.data = new AnimasPumpDeviceData(new AnimasPumpDataWriter(this.outputWriter));
        this.setBaseData(data);
        baseDataConverter = new AnimasBaseDataV2Converter(deviceReader, data);
        new Thread(baseDataConverter).start();
        this.dataConverter = this.baseDataConverter;
    }


    @Override
    public List<AnimasTransferType> getSupportedActions()
    {
        return Arrays.asList(AnimasTransferType.DownloadPumpData, //
            AnimasTransferType.DownloadPumpSettings);
    }


    protected void determineMaxProgressForTransferType(AnimasTransferType transferType)
    {
        int maxProgress = 5; // opening is 5

        if (transferType == AnimasTransferType.DownloadPumpData)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            // maxProgress +=
            // determineMaxProgressForOperation(AnimasTransferType.DownloadPumpSettingsBase);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadPumpData);
        }
        else if (transferType == AnimasTransferType.DownloadPumpSettings)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadPumpSettings);
        }

        this.deviceReader.configureProgressReporter(ProgressType.Static, 100, maxProgress, 0);

    }


    @Override
    public void startAction(AnimasTransferType transferType) throws PlugInBaseException
    {
        data.setTransferType(transferType);

        try
        {
            LOG.debug("Running Animas Base Data Handler (v2)");

            this.checkIfActionAllowed(transferType);

            determineMaxProgressForTransferType(transferType);

            openConnection();

            this.data.writeIdentification();

            if (this.data.getTransferType() == AnimasTransferType.DownloadPumpData)
            {
                downloadData();
            }
            else if (this.data.getTransferType() == AnimasTransferType.DownloadPumpSettings)
            {
                downloadSettings();
            }

        }
        catch (PlugInBaseException ex)
        {
            if (AnimasUtils.checkIfUserRelevantExceptionIsThrown(ex, true))
            {
                LOG.error("Exception: " + ex, ex);
                throw ex;
            }
        }
        finally
        {
            try
            {
                this.closeConnection();
            }
            catch (Exception ex2)
            {}
        }

    }


    public void downloadBaseSettings() throws PlugInBaseException
    {
        sendRequestAndWait(AnimasDataType.ClockMode, 0, 1, 100, 100); // 28
        this.data.writeSettings(AnimasDataType.ClockMode);

        for (int i = 0; i < 4; i++)
        {
            this.data.basalProgramNum = i;
            sendRequestAndWait(AnimasDataType.BasalProfileName, i, 1, 100, 100); // 18
            sendRequestAndWait(AnimasDataType.BasalProfile, i, 1, 100, 100); // 11
        }

        this.data.writeSettings(AnimasDataType.BasalProfile);

    }


    public void downloadSettings() throws PlugInBaseException
    {
        LOG.debug("downloadSettings()");

        deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 0);

        downloadBaseSettings();

        sendRequestAndWait(AnimasDataType.SoundSettings, 0, 1, 100, 100); // 14
        this.data.writeSettings(AnimasDataType.SoundSettings);

        sendRequestAndWait(AnimasDataType.AdvancedSettings, 0, 1, 100, 100); // 13

        // Before 2020
        sendRequestAndWait(AnimasDataType.DosingSettings, 0, 1, 100, 10);

        this.data.basalProgramNum = 6;
        sendRequestAndWait(AnimasDataType.PumpSettings1_Pre2020, 0, 1, 100, 100);

        for (int i = 0; i < 6; i++)
        {
            this.data.basalProgramNum = i;
            sendRequestAndWait(AnimasDataType.PumpSettings2_Pre2020, i, 1, 100, 100);
        }

        this.data.writeSettings(AnimasDataType.AdvancedSettings);

        // 2020 and higher
        this.data.basalProgramNum = 4;
        sendRequestAndWait(AnimasDataType.InsulinCarbRatio, 0, 1, 100, 10); // 39_4

        this.data.basalProgramNum = 5;
        sendRequestAndWait(AnimasDataType.InsulinBGRatio, 1, 1, 100, 10); // 39_5

        this.data.basalProgramNum = 6;
        sendRequestAndWait(AnimasDataType.BGTargetSetting, 2, 1, 100, 10); // 39_6

        this.data.writeSettings(AnimasDataType.BGTargetSetting);

        // Ping and Vibe
        sendRequestAndWait(AnimasDataType.FriendlyName, 0, 1, 100, 100);

        this.data.writeSettings(AnimasDataType.FriendlyName);

        // this.data.debugAllSettings(LOG);

        AnimasUtils.sleepInMs(10L);
    }


    public void downloadData() throws PlugInBaseException
    {
        LOG.debug("downloadData()");

        int numberOfRecords = 65535; // 500;

        // FIXME
        sendRequestAndWait(AnimasDataType.BolusHistory, 0, numberOfRecords, 500);
        // 21 : This has problem because firmware reports back false data
        // (combined bolus is just defined as extended,
        // so part of data gets lost)

        sendRequestAndWait(AnimasDataType.TotalDailyDoseHistory, 0, numberOfRecords, 120);

        sendRequestAndWait(AnimasDataType.AlarmHistory, 0, numberOfRecords, 30);

        sendRequestAndWait(AnimasDataType.PrimeHistory, 0, numberOfRecords, 60);

        sendRequestAndWait(AnimasDataType.SuspendHistory, 0, numberOfRecords, 30);

        sendRequestAndWait(AnimasDataType.BasalRateHistory, 0, numberOfRecords, 270);

        sendRequestAndWait(AnimasDataType.BolusHistoryExt, 0, 500, 500);

        // sendRequestAndWait(AnimasDataType.BGMHistory, 0, 1000, 1000); // 40

    }

}
