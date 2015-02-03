package ggc.pump.device.animas.impl.handler;

import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.pump.device.animas.impl.data.*;
import ggc.plugin.device.impl.animas.handler.AbstractDeviceDataHandler;
import ggc.pump.device.animas.impl.converter.AnimasBaseDataConverter;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasExceptionType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     AnimasBaseDataHandler
 *  Description:  Animas Base Data Handler
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasBaseDataHandler extends AbstractDeviceDataHandler
{

    public static final Log LOG = LogFactory.getLog(AnimasBaseDataHandler.class);

    private static final boolean DEBUG = true;

    AnimasPumpDeviceData data;
    HashMap<String, BigDecimal> bigDecimals = new HashMap<String, BigDecimal>();

    AnimasBaseDataConverter baseDataConverter;

    public AnimasBaseDataHandler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
    }

    @Override
    public void initLocal()
    {
        this.data = new AnimasPumpDeviceData(new ggc.pump.device.animas.impl.data.AnimasPumpDataWriter(this.outputWriter));
        this.setBaseData(data);
        baseDataConverter = new AnimasBaseDataConverter(deviceReader, data);
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
        int maxProgress = 5; // opening is 10

        if (this.data.getTransferType() == AnimasTransferType.DownloadPumpData)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadPumpSettingsBase);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadPumpData);
        }
        else if (this.data.getTransferType() == AnimasTransferType.DownloadPumpSettings)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadPumpSettings);
        }

        this.deviceReader.configureProgressReporter(ProgressType.Static, 100, maxProgress, 0);

    }


    @Override
    public void startAction(AnimasTransferType transferType) throws AnimasException
    {
        data.setTransferType(transferType);

        try
        {
            LOG.debug("Running Animas Base Data Handler (v2)");

            this.checkIfActionAllowed(transferType);

            determineMaxProgressForTransferType(transferType);

            openConnection();

            if (this.data.getTransferType() == AnimasTransferType.DownloadPumpData)
            {

                // temporary
                //downloadSettings();

                downloadData();

                //this.downloadCompleted = true;
            }
            else if (this.data.getTransferType() == AnimasTransferType.DownloadPumpSettings)
            {
                downloadSettings();
            }
            else if ((this.data.getTransferType() == AnimasTransferType.DownloadCGMSData) || //
                    (this.data.getTransferType() == AnimasTransferType.DownloadCGMSSettings))
            {
                //AnimasDexcomDataHandler dexcomProcessor = new AnimasDexcomDataHandler(this);
                //dexcomProcessor.startOperation(this.data.getTransferType());

                LOG.error(String.format("Operation %s not supported/implemented yet.", this.data.getTransferType()
                        .name()));
                throw new AnimasException(AnimasExceptionType.OperationNotSupported);
            }
            else
            {
                //AnimasExtendedDataHandler binaryProcessor = new AnimasExtendedDataHandler(this);
                //binaryProcessor.startOperation(this.data.getTransferType());

                LOG.error(String.format("Operation %s not supported/implemented yet.", this.data.getTransferType()
                        .name()));
                throw new AnimasException(AnimasExceptionType.OperationNotSupported);
            }

            //this.deviceReader.returnFromOperation(this, null);
        }
        catch (AnimasException ex)
        {
            if (AnimasUtils.checkIfUserRelevantExceptionIsThrownNoRethrow(ex))
            {
                LOG.error("Exception: " + ex, ex);
                throw ex;
                //this.deviceReader.returnFromOperation(this, ex);

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


    public void downloadBaseSettings() throws AnimasException
    {
        sendRequestAndWait(AnimasDataType.ClockMode, 0, 1, 100, 100); // 28

        for (int i = 0; i < 4; i++)
        {
            this.data.basalProgramNum = i;
            sendRequestAndWait(AnimasDataType.BasalProfileName, i, 1, 100, 100); // 18
            sendRequestAndWait(AnimasDataType.BasalProfile, i, 1, 100, 100); // 11
        }


    }


    public void downloadSettings() throws AnimasException
    {
        LOG.debug("downloadSettings()");

        deviceReader.addToProgressAndCheckIfCanceled(ProgressType.Static, 0);

        downloadBaseSettings();

        sendRequestAndWait(AnimasDataType.SoundSettings, 0, 1, 100, 100); // 14

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

        // 2020 and higher
        this.data.basalProgramNum = 4;
        sendRequestAndWait(AnimasDataType.InsulinCarbRatio, 0, 1, 100, 10); // 39_4

        this.data.basalProgramNum = 5;
        sendRequestAndWait(AnimasDataType.InsulinBGRatio, 1, 1, 100, 10); // 39_5

        this.data.basalProgramNum = 6;
        sendRequestAndWait(AnimasDataType.BGTargetSetting, 2, 1, 100, 10); // 39_6

        // Ping and Vibe
        sendRequestAndWait(AnimasDataType.FriendlyName, 0, 1, 100, 100);

        if (true)
        {
            this.data.debugAllSettings(LOG);
            // return;
        }

        AnimasUtils.sleepInMs(10L);

    }

    public void downloadData() throws AnimasException
    {
        LOG.debug("downloadData()");

        boolean dontExecute = true;

        int numberOfRecords = 500; // 500

        // numberOfRecords = 65535; // possible error ??

        // FIXME no GGC
        sendRequestAndWait(AnimasDataType.BolusHistory, 0, numberOfRecords, 600); // 21

        numberOfRecords = 65535;

        // FIXME no GGC

        if (!dontExecute)
        {
            sendRequestAndWait(AnimasDataType.TotalDailyDoseHistory, 0, numberOfRecords, 120);

            sendRequestAndWait(AnimasDataType.AlarmHistory, 0, numberOfRecords, 30);

            sendRequestAndWait(AnimasDataType.PrimeHistory, 0, numberOfRecords, 60);

            sendRequestAndWait(AnimasDataType.SuspendHistory, 0, numberOfRecords, 30);

            // FIXME no GGC
            sendRequestAndWait(AnimasDataType.BasalRateHistory, 0, numberOfRecords, 270); // 26

        }



        sendRequestAndWait(AnimasDataType.BolusHistoryExt, 0, 500, 500); // 38, 600

        //sendRequestAndWait(AnimasDataType.BGMHistory, 0, 1000, 1000); // 40



    }


}
