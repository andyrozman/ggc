package ggc.plugin.device.impl.minimed.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceReader;
import ggc.plugin.device.impl.minimed.comm.MinimedCommunicationInterface;
import ggc.plugin.device.impl.minimed.comm.serial.MinimedCommunicationCareLinkUSB;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverter;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.enums.MinimedTransferType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     MinimedDataHandler
 *  Description:  Main class for reading data.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedDataHandler
{

    private static Logger LOG = LoggerFactory.getLogger(MinimedDataHandler.class);

    protected MinimedCommunicationInterface communicationProtocol = null;
    protected DataAccessPlugInBase dataAccess;

    protected MinimedDeviceType deviceType;
    protected MinimedDataConverter converter = null;

    protected MinimedDeviceReader deviceReader;
    protected OutputWriter outputWriter;


    /**
     * Constructor
     */
    public MinimedDataHandler(DataAccessPlugInBase dataAccess) throws PlugInBaseException
    {
        this.dataAccess = dataAccess;
    }


    /**
     * Constructor
     */
    public MinimedDataHandler(DataAccessPlugInBase dataAccess, MinimedConnectionParametersDTO connectionParametersDTO,
            MinimedDeviceType deviceType, MinimedDeviceReader deviceReader, OutputWriter writer)
            throws PlugInBaseException
    {
        this.dataAccess = dataAccess;
        initDataHandler(connectionParametersDTO, deviceType, deviceReader, writer);
    }


    public void initDataHandler(MinimedConnectionParametersDTO connectionParametersDTO, MinimedDeviceType deviceType,
            MinimedDeviceReader deviceReader, OutputWriter writer) throws PlugInBaseException
    {
        this.deviceReader = deviceReader;
        this.outputWriter = writer;

        this.deviceType = deviceType;

        initCommunicationInterface(connectionParametersDTO);
        initConverter();
    }


    public void initCommunicationInterface(MinimedConnectionParametersDTO connectionParametersDTO)
            throws PlugInBaseException
    {
        if (MinimedUtil.isLowLevelDebug())
            LOG.debug("Protocol ID: " + connectionParametersDTO.interfaceType.name());

        switch (connectionParametersDTO.interfaceType)
        {
            case ComStation:
            case ComLink:
            case ParadigmLinkCom:
            case ParadigmLinkUSB:
                {
                    throw new PlugInBaseException(PlugInExceptionType.UnsupportedAndNotPlannedInterface,
                            new Object[] { connectionParametersDTO.interfaceType.name() });
                }

            case CareLinkUSB:
                {
                    communicationProtocol = new MinimedCommunicationCareLinkUSB(dataAccess, this);
                }
                break;
            case ContourNextLink2:
                {
                    // throw new
                    // PlugInBaseException(PlugInExceptionType.YetUnsupportedInterface,
                    // new Object[] {
                    // connectionParametersDTO.interfaceType.name() });
                    break;
                }

            case None:
            default:
                throw new PlugInBaseException(PlugInExceptionType.NeedToSelectInterface);

        }

        MinimedUtil.setCommunicationInterface(communicationProtocol);
    }


    public void initConverter() throws PlugInBaseException
    {
        this.converter = MinimedUtil.getDataConverter(getDeviceType(), getDeviceTargetType());
        LOG.debug("Converter: " + getConverter());
    }


    public void checkIfDownloadCanceled() throws PlugInBaseException
    {
        if (MinimedUtil.isDownloadCanceled())
        {
            LOG.debug("Communication with Minimed device was closed.");
            throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);
        }
    }


    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progress) throws PlugInBaseException
    {
        deviceReader.addToProgressAndCheckIfCanceled(progressType, progress);
    }


    public void configureProgressReporter(ProgressType progressType, int staticProgressPercentage,
            int staticMaxElements, int dynamicMaxElements)
    {
        deviceReader.configureProgressReporter(progressType, staticProgressPercentage, staticMaxElements,
            dynamicMaxElements);
    }


    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        // FIXME
        return null;
        // /return this.outputWriter.getDeviceIdentification();
    }


    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }


    public void convertDeviceReply(MinimedCommandReply mdc)
    {
        this.converter.convertData(mdc);
    }


    public Object getReplyValue(MinimedCommandReply mdc)
    {
        return this.converter.getReplyValue(mdc);
    }


    public MinimedDeviceType getDeviceType()
    {
        return deviceType;
    }


    public MinimedDataConverter getConverter()
    {
        return converter;
    }


    public void setConverter(MinimedDataConverter converter)
    {
        this.converter = converter;
    }


    public MinimedTargetType getDeviceTargetType()
    {
        return this.deviceReader.getDeviceTargetType();
    }


    public void determineMaxProgress(MinimedTransferType transferType) throws PlugInBaseException
    {
        if ((transferType == MinimedTransferType.DownloadCGMSData)
                || (transferType == MinimedTransferType.DownloadPumpData))
        {
            List<MinimedCommandType> commands = MinimedCommandType.getCommands(this.getDeviceType(),
                this.getDeviceTargetType() == MinimedTargetType.CGMS ? MinimedTargetType.CGMSData
                        : MinimedTargetType.PumpData);

            determineMaxProgress(commands);
        }
        else
        {
            List<MinimedCommandType> commands = MinimedCommandType.getCommands(this.getDeviceType(),
                this.getDeviceTargetType() == MinimedTargetType.CGMS ? MinimedTargetType.CGMSConfiguration
                        : MinimedTargetType.PumpConfiguration);

            determineMaxProgress(commands);
        }
    }


    public void determineMaxProgress(List<MinimedCommandType> commands) throws PlugInBaseException
    {
        int maxProgress = 5; // initializeCommunicationInterface

        for (MinimedCommandType mct : commands)
        {
            maxProgress += (mct.getMaxRecords() * 4);
        }

        maxProgress += 4; // Init

        configureProgressReporter(ProgressType.Static, 100, maxProgress, 0);

    }


    public void startAction(MinimedTransferType transferType) throws PlugInBaseException
    {

        determineMaxProgress(transferType);

        try
        {
            // initialize communication interface
            this.communicationProtocol.initializeCommunicationInterface();

            checkIfDownloadCanceled();

            // initalize device
            this.communicationProtocol.initDevice();

            checkIfDownloadCanceled();

            if ((transferType == MinimedTransferType.DownloadCGMSData)
                    || (transferType == MinimedTransferType.DownloadPumpData))
            {
                this.communicationProtocol.readDataFromInterface();
            }
            else
            {
                this.communicationProtocol.readConfigurationFromInterface();
            }

            // close interface
            this.communicationProtocol.closeCommunicationInterface();

        }
        catch (PlugInBaseException ex)
        {
            LOG.error("Error: " + ex.getMessage(), ex);
            throw ex;
        }
        finally
        {
            this.communicationProtocol.closeDevice();
        }
    }

}
