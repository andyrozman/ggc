package ggc.cgms.device.animas.impl.handler;

import java.util.Arrays;
import java.util.List;

import ggc.cgms.device.animas.impl.converter.AnimasDexcomDataConverter;
import ggc.cgms.device.animas.impl.data.AnimasCGMSDeviceData;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.handler.AbstractDeviceDataV2Handler;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;

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

public class AnimasDexcomDataHandler extends AbstractDeviceDataV2Handler
{

    AnimasCGMSDeviceData data;
    AnimasDexcomDataConverter dexcomDataConverter;


    public AnimasDexcomDataHandler(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader,
            OutputWriter outputWriter)
    {
        super(portName, deviceType, deviceReader, outputWriter);
    }


    @Override
    public void initLocal()
    {
        this.data = new AnimasCGMSDeviceData(new AnimasCGMSDataWriter(this.outputWriter));
        this.setBaseData(data);
        dexcomDataConverter = new AnimasDexcomDataConverter(deviceReader, data);
        new Thread(dexcomDataConverter).start();
        this.dataConverter = this.dexcomDataConverter;
        this.setDebugMode(true, false);
    }


    @Override
    public List<AnimasTransferType> getSupportedActions()
    {
        return Arrays.asList(AnimasTransferType.DownloadCGMSData, AnimasTransferType.DownloadCGMSSettings);
    }


    protected void determineMaxProgressForTransferType(AnimasTransferType transferType)
    {
        int maxProgress = 5; // opening is 5

        if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSData)
        {
            maxProgress += determineMaxProgressForOperation(AnimasTransferType.All);
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
    public void startAction(AnimasTransferType transferType) throws PlugInBaseException
    {
        data.setTransferType(transferType);

        try
        {
            LOG.debug("Running Animas Dexcom Data Handler");

            this.checkIfActionAllowed(transferType);

            determineMaxProgressForTransferType(transferType);

            openConnection();

            this.data.writeIdentification();

            if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSData)
            {
                downloadData();
            }
            else if (this.data.getTransferType() == AnimasTransferType.DownloadCGMSSettings)
            {
                downloadSettings();
            }

        }
        catch (PlugInBaseException ex)
        {
            if (AnimasUtils.checkIfUserRelevantExceptionIsThrownNoRethrow(ex))
            {
                LOG.error("Exception reading CGMS Data: " + ex, ex);
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

            this.dataConverter.stopConverterThread();
        }

    }


    private void downloadSettings() throws PlugInBaseException
    {
        sendRequestAndWait(AnimasDataType.DexcomSettings, 0, 1, 100, 100); // 42
        this.data.writeSettings(AnimasDataType.DexcomSettings);
    }


    private void downloadData() throws PlugInBaseException
    {

        sendRequestAndWait(AnimasDataType.DexcomWarnings, 0, 33215, 56, 100); // 43
        // 43 was 56, 500

        this.noReconnectMode = true;
        sendRequestAndWait(AnimasDataType.DexcomBgHistory, 0, 48640, 800, 100); // 45

    }


    public void downloadSettingsTest() throws PlugInBaseException
    {
        sendRequestAndWait(AnimasDataType.Dexcom_C3, 0, 1, 100, 100); // 44
        sendRequestAndWait(AnimasDataType.Dexcom_C5, 0, 1, 100, 100); // 48
        sendRequestAndWait(AnimasDataType.Dexcom_C6, 0, 1, 100, 100); // 48
    }


    @Override
    public boolean shouldWeRetryDownloadingData(AnimasDevicePacket devicePacket)
    {
        if (devicePacket.dataTypeObject == AnimasDataType.DexcomBgHistory)
        {
            if (devicePacket.downloadedQuantity > 750)
            {
                this.animasDevicePacket = devicePacket;
                return false;
            }
        }

        return true;
    }

}
