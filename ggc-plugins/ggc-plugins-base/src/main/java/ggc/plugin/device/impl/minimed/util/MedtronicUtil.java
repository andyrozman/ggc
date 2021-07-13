package ggc.plugin.device.impl.minimed.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDeviceReader;
import ggc.plugin.device.impl.minimed.comm.MinimedCommunicationInterface;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.data.MinimedCommandTypeInterface;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverter;
import ggc.plugin.device.impl.minimed.data.decoder.HistoryDecoderProcessor;
import ggc.plugin.device.impl.minimed.data.decoder.MinimedHistoryDecoder;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedConverterType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     MedtronicUtil
 *  Description:  Minimed Util
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MedtronicUtil
{

    private static final Logger LOG = LoggerFactory.getLogger(MedtronicUtil.class);

    private static OutputWriter outputWriter;
    private static BitUtils bitUtils = new BitUtils();
    private static MinimedDeviceType deviceType;
    private static MinimedConnectionParametersDTO connectionParameters;
    private static ClockModeType clockMode;
    private static GlucoseUnitType glucoseUnitType;
    private static MinimedCommunicationInterface communicationInterface;
    private static MinimedDeviceReader minimedDeviceReader;
    private static boolean paradigmLink = false;
    private static String serialNumber;
    private static int[] serialNumberBCD;
    private static String firmwareVersion;
    private static HistoryDecoderProcessor historyDecoderProcessor;
    private static boolean lowLevelDebug = false;
    private static boolean lowLevelDebugData = true;
    private static int ioDelay = 100;
    private static Map<MinimedConverterType, MinimedDataConverter> pumpConverterMap = new HashMap<MinimedConverterType, MinimedDataConverter>();
    private static Map<MinimedConverterType, MinimedDataConverter> cgmsConverterMap = new HashMap<MinimedConverterType, MinimedDataConverter>();
    private static Map<MinimedTargetType, MinimedHistoryDecoder> recordsDecoderMap = new HashMap<MinimedTargetType, MinimedHistoryDecoder>();
    private static MedtronicCnlSession medtronicCnlSession;


    public static OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    public static HistoryDecoderProcessor getHistoryDecoderProcessor()
    {
        if (historyDecoderProcessor == null)
        {
            historyDecoderProcessor = new HistoryDecoderProcessor();
        }

        return historyDecoderProcessor;
    }


    public static void setOutputWriter(OutputWriter outputWriter)
    {
        MedtronicUtil.outputWriter = outputWriter;
    }


    public static int[] getSerialNumberBCD()
    {
        return serialNumberBCD;
    }


    public static BitUtils getBitUtils()
    {
        return bitUtils;
    }


    public static MinimedDeviceType getDeviceType()
    {
        return deviceType;
    }


    public static void setDeviceType(MinimedDeviceType deviceType)
    {
        MedtronicUtil.deviceType = deviceType;
    }


    public static void setConnectionParameters(MinimedConnectionParametersDTO connectionParameters)
    {
        MedtronicUtil.connectionParameters = connectionParameters;

        if ((connectionParameters.interfaceType == MinimedCommInterfaceType.ParadigmLinkCom)
                || (connectionParameters.interfaceType == MinimedCommInterfaceType.ParadigmLinkUSB))
        {
            paradigmLink = true;
        }
        else if ((connectionParameters.interfaceType == MinimedCommInterfaceType.ContourNextLink2)
                || (connectionParameters.interfaceType == MinimedCommInterfaceType.ContourNextLink24))
        {
            bitUtils.computeCRC8WithPolynomialInit(0x9b, 0, null);
        }

        serialNumber = connectionParameters.serialNumber;
        serialNumberBCD = connectionParameters.serialNumberBCD;
    }


    public static MinimedConnectionParametersDTO getConnectionParameters()
    {
        return MedtronicUtil.connectionParameters;
    }


    public static void setClockMode(ClockModeType clockMode)
    {
        MedtronicUtil.clockMode = clockMode;
    }


    public static ClockModeType getClockMode()
    {
        return clockMode;
    }


    public static void setGlucoseUnitType(GlucoseUnitType glucoseUnitType)
    {
        MedtronicUtil.glucoseUnitType = glucoseUnitType;
    }


    public static GlucoseUnitType getGlucoseUnitType()
    {
        return glucoseUnitType;
    }


    public static MinimedCommunicationInterface getCommunicationInterface()
    {
        return communicationInterface;
    }


    public static void setCommunicationInterface(MinimedCommunicationInterface communicationInterface)
    {
        MedtronicUtil.communicationInterface = communicationInterface;
    }


    public static MinimedDeviceReader getMinimedDeviceReader()
    {
        return minimedDeviceReader;
    }


    public static void setMinimedDeviceReader(MinimedDeviceReader minimedDeviceReader)
    {
        MedtronicUtil.minimedDeviceReader = minimedDeviceReader;
    }


    public static boolean isDownloadCanceled()
    {
        return MedtronicUtil.minimedDeviceReader.isDownloadCanceled();
    }


    public static void sleepPhysicalCommunication()
    {
        if (lowLevelDebug)
            LOG.debug("Sleeping for 100 ms");
        sleepMs(ioDelay);
    }


    public static void sleepInterface()
    {
        sleepMs(paradigmLink ? 250 : 100);
    }


    public static void sleepMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            //
        }
    }


    public static String getSerialNumber()
    {
        return serialNumber;
    }


    public static String getFirmwareVersion()
    {
        return firmwareVersion;
    }


    public static void setFirmwareVersion(String firmwareVersion)
    {
        MedtronicUtil.firmwareVersion = firmwareVersion;
    }


    public static boolean isFirmwareVersion_16_or_17()
    {
        return (firmwareVersion != null
                && (firmwareVersion.startsWith("VER 1.6") || firmwareVersion.startsWith("VER 1.7")));
    }


    public static boolean isLowLevelDebug()
    {
        return lowLevelDebug;
    }


    public static void setLowLevelDebug(boolean lowLevelDebug)
    {
        MedtronicUtil.lowLevelDebug = lowLevelDebug;
    }


    public static void setIoDelay(int ioDelay)
    {
        MedtronicUtil.ioDelay = ioDelay;
    }


    public static MinimedDataConverter getDataConverter(MinimedDeviceType deviceType, MinimedTargetType targetType)
            throws PlugInBaseException
    {
        switch (targetType)
        {
            case CGMS:
                {
                    if (deviceType.getCGMSConverterType() == null)
                        throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                                new Object[] { "No CGMS Converter available for this device" });
                    else
                    {
                        if (!cgmsConverterMap.containsKey(deviceType.getCGMSConverterType()))
                        {
                            throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                                    new Object[] { "CGMS Converter " + deviceType.getPumpConverterType()
                                            + " is not registered." });
                        }

                        return cgmsConverterMap.get(deviceType.getCGMSConverterType());
                    }
                }

            default:
                {
                    if (deviceType.getPumpConverterType() == null)
                        throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                                new Object[] { "No Pump Converter available for this device" });
                    else
                    {
                        if (!pumpConverterMap.containsKey(deviceType.getPumpConverterType()))
                        {
                            throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                                    new Object[] { "Pump Converter " + deviceType.getPumpConverterType()
                                            + " is not registered." });
                        }

                        return pumpConverterMap.get(deviceType.getPumpConverterType());
                    }

                }
        }

    }


    public static void registerConverter(MinimedTargetType targetType, MinimedConverterType converterType,
            MinimedDataConverter dataConverter)
    {
        if (targetType == MinimedTargetType.CGMS)
        {
            cgmsConverterMap.put(converterType, dataConverter);
        }
        else
        {
            pumpConverterMap.put(converterType, dataConverter);
        }
    }


    public static boolean isTargetRegistered(MinimedTargetType targetType)
    {
        if (targetType == MinimedTargetType.CGMS)
        {
            return (cgmsConverterMap.size() != 0);
        }
        else
        {
            return (pumpConverterMap.size() != 0);
        }
    }


    public static void refreshConverters(MinimedTargetType targetType)
    {
        Map<MinimedConverterType, MinimedDataConverter> map = null;

        if (targetType == MinimedTargetType.CGMS)
        {
            map = cgmsConverterMap;
        }
        else
        {
            map = pumpConverterMap;
        }

        for (MinimedDataConverter mdc : map.values())
        {
            mdc.refreshOutputWriter();
        }
    }


    public static void registerDecoder(MinimedTargetType targetType, MinimedHistoryDecoder historyDecoder)
    {
        recordsDecoderMap.put(targetType, historyDecoder);
    }


    public static MinimedHistoryDecoder getDecoder(MinimedTargetType targetType,
            MinimedCommandTypeInterface commandType) throws PlugInBaseException
    {
        if (targetType == MinimedTargetType.Pump)
        {
            if (!recordsDecoderMap.containsKey(MinimedTargetType.Pump))
            {
                throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                        new Object[] { "Pump Decoder is not registered." });
            }

            return recordsDecoderMap.get(MinimedTargetType.Pump);
        }
        else
        {

            if (!recordsDecoderMap.containsKey(MinimedTargetType.CGMS))
            {
                throw new PlugInBaseException(PlugInExceptionType.InvalidInternalConfiguration,
                        new Object[] { "CGMS Decoder is not registered." });
            }

            // TODO we would probably need more than one decoder here, if we
            // support all history records
            return recordsDecoderMap.get(MinimedTargetType.CGMS);
        }

    }


    public static boolean isLowLevelDebugData()
    {
        return lowLevelDebugData;
    }


    public static void setLowLevelDebugData(boolean lowLevelDebugData)
    {
        MedtronicUtil.lowLevelDebugData = lowLevelDebugData;
    }


    public static void setMedtronicCnlSession(MedtronicCnlSession medtronicCnlSession)
    {
        MedtronicUtil.medtronicCnlSession = medtronicCnlSession;
    }


    public static MedtronicCnlSession getMedtronicCnlSession()
    {
        return medtronicCnlSession;
    }
}
