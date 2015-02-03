package ggc.cgms.device.animas.impl.handler;

import ggc.cgms.device.animas.impl.converter.AnimasDexcomDataConverter;
import ggc.cgms.device.animas.impl.data.AnimasCGMSDataWriter;
import ggc.cgms.device.animas.impl.data.AnimasCGMSDeviceData;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.handler.AbstractDeviceDataHandler;
import ggc.plugin.device.impl.animas.util.AnimasException;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;

import java.util.Arrays;
import java.util.List;

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
 *  Filename:     AnimasDexcomDataHandler
 *  Description:  Animas Dexcom Data Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasDexcomDataHandler extends AbstractDeviceDataHandler
{
    AnimasCGMSDeviceData data;
    AnimasDexcomDataConverter dexcomDataConverter;


    public AnimasDexcomDataHandler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader, OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
    }


    @Override
    public void initLocal()
    {
        this.data = new AnimasCGMSDeviceData(new AnimasCGMSDataWriter(this.outputWriter));
        this.setBaseData(data);
        dexcomDataConverter = new AnimasDexcomDataConverter(deviceReader, data);
        this.dataConverter = this.dexcomDataConverter;
    }

    @Override
    public List<AnimasTransferType> getSupportedActions()
    {
        return Arrays.asList(AnimasTransferType.DownloadCGMSData,
                AnimasTransferType.DownloadCGMSSettings);
    }


    protected void determineMaxProgressForTransferType(AnimasTransferType transferType)
    {
        int maxProgress = 5; // opening is 10

        if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSData)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadCGMSSettingsBase);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadCGMSData);
        }
        else if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSSettings)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.DownloadCGMSSettings);
        }

        this.deviceReader.configureProgressReporter(ProgressType.Static, 100, maxProgress, 0);

    }


    @Override
    public void startAction(AnimasTransferType transferType) throws AnimasException
    {
        data.setTransferType(transferType);

        try
        {
            LOG.debug("Running Animas Dexcom Data Handler (v2)");

            this.checkIfActionAllowed(transferType);

            determineMaxProgressForTransferType(transferType);

            openConnection();

            if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSData)
            {
                downloadAll();

                //this.downloadCompleted = true;
            }
            else if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSSettings)
            {
                downloadSettings();
            }

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

    private void downloadSettings()
    {

    }

    private void downloadData() throws AnimasException
    {
        // done no GGC
        //sendRequestAndWait(AnimasDataType.DexcomWarnings, 0, 33215, 56, 100); // 43


    }


    public void downloadAll() throws AnimasException
    {
        // settings ?
        sendRequestAndWait(AnimasDataType.DexcomSettings, 0, 1, 100, 100); // 42
        sendRequestAndWait(AnimasDataType.Dexcom_C3, 0, 1, 100, 100); // 44
        sendRequestAndWait(AnimasDataType.Dexcom_C5, 0, 1, 100, 100); // 48

//        sendRequestAndWait(AnimasDataType.DexcomBgHistory, 0, 48640, 48640, 100); // 45



        //sendRequestAndWait(AnimasDataType.Dexcom_C7, 0, 256, 100, 100); // 46

        //testCommand(AnimasDataType.DexcomSettings, 0, 1, 1);

        // data ?


        // invalid
//        sendRequestAndWait(AnimasDataType.Dexcom_C6, 0, 1, 100, 100); // 46




    }

}
