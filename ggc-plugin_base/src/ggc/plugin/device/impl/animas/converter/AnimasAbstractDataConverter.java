package ggc.plugin.device.impl.animas.converter;

import com.atech.utils.data.ATechDate;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.data.dto.PumpConnectorInfo;
import ggc.plugin.device.impl.animas.handler.AnimasDataConverter;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;

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
 *  Filename:     AnimasAbstractDataConverter
 *  Description:  Abstract Data Converter.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasAbstractDataConverter implements AnimasDataConverter
{
    public static final Log LOG = LogFactory.getLog(AnimasAbstractDataConverter.class);
    AnimasDeviceReader deviceReader;
    private boolean inDataProcessingPacket = false;


    public AnimasAbstractDataConverter(AnimasDeviceReader deviceReader, AnimasDeviceData data)
    {
        this.deviceReader = deviceReader;
    }


    public abstract AnimasDeviceData getData();



    public void processReturnedRawData(AnimasDevicePacket animasDevicePacket)
    {
        while (this.inDataProcessingPacket)
        {
            AnimasUtils.sleepInMs(20L);
            LOG.debug("Locked");
        }

        // FIXME change all processing to list

        AnimasUtils.addDataTypeProcessed(animasDevicePacket.dataTypeObject);

        ATechDate dt = null;

        if (animasDevicePacket.dataTypeObject.isDateInDataType())
        {
            dt = decodeDateTime(animasDevicePacket);

            if (dt == null)
            {
                // LOG.error("Error parsing Date/Time with dataType " +
                // dataTypeAnimas.name());
                this.inDataProcessingPacket = false;
                return;
            }
        }

        switch (animasDevicePacket.dataTypeObject)
        {
            case SerialNumber: // 8
                decodeSerialNumber(animasDevicePacket);
                break;

            case ClockMode: // 28
                decodeClockMode(animasDevicePacket);
                break;

            case BGUnit: // 29
                decodeBGUnit(animasDevicePacket);
                break;

            case SoftwareCode: // 30
                decodeSoftwareCode(animasDevicePacket);
                break;

            case FontTableIndex: // 34
                decodeFontTableIndex(animasDevicePacket);
                break;

            case LanguageIndex: // 35
                decodeLanguageIndex(animasDevicePacket);
                break;

            case FoodDbSize: // 36
                decodeFoodDb(animasDevicePacket);
                break;

            default:
                processCustomReturnedRawData(animasDevicePacket, dt);
        }

        inDataProcessingPacket = false;
    }


    public abstract void processCustomReturnedRawData(AnimasDevicePacket adp, ATechDate dt);



    public void decodePumpModel()
    {
        String pumpModel = "?";
        String softwareCode = getData().pumpInfo.softwareCode;
        short foodDbSize = getData().pumpInfo.foodDbSize;

        if (softwareCode.charAt(0) == 'A')
        {
            pumpModel = "1200";
        }
        else
        {

            if (softwareCode.charAt(0) == 'B')
            {
                pumpModel = "1200+";

                if (foodDbSize == 510)
                {
                    pumpModel = "1250";
                }
                else
                {
                    if (foodDbSize != 255)
                    {
                        logUnknownDbSizeForPumpModel("B", "1200+", "255, 510");
                    }
                }
            }
            else if (softwareCode.charAt(0) == 'C')
            {
                pumpModel = "1250+";

                if (foodDbSize == 765)
                {
                    pumpModel = "1250i";
                }
                else
                {
                    if (foodDbSize != 510)
                    {
                        logUnknownDbSizeForPumpModel("C", "1250+", "510, 765");
                    }
                }
            }
            else if (softwareCode.charAt(0) == 'D')
            {
                pumpModel = "1275";

                if (foodDbSize == 1020)
                {
                    pumpModel = "1275i";
                }
                else
                {
                    if (foodDbSize != 510)
                    {
                        logUnknownDbSizeForPumpModel("D", "1275", "1275, 1275i");
                    }
                }
            }
            else if (softwareCode.charAt(0) == 'E')
            {
                pumpModel = "1285";

                if (foodDbSize == 1020)
                {
                    pumpModel = "1285i";
                }
                else
                {
                    if (foodDbSize != 510)
                    {
                        logUnknownDbSizeForPumpModel("E", "1285", "1285, 1285i");
                    }
                }
            }
            else if (softwareCode.charAt(0) == 'F')
            {
                // if (this.foodDbSize == 2040)
                {
                    pumpModel = "1300";
                }
            }

        }

        getData().pumpInfo.deviceType = AnimasDeviceType.getAnimasDeviceTypeFromPumpModel(pumpModel);
    }


    private void logUnknownDbSizeForPumpModel(String softwareCodePrefix, String defPump, String suppportedSizes)
    {
        LOG.debug(String
                .format(
                        "Software Code Prefix is %s, but Food Db Size (%s) is unknown, we default to Pump %s (supported Db sizes are %s)",
                        softwareCodePrefix, getData().pumpInfo.foodDbSize, defPump, suppportedSizes));
    }



    public void decodeDownloaderSerialNumber(AnimasDevicePacket adp)
    {
        String serialNumber = (AnimasUtils.short2hex(adp.getReceivedDataBit(5)) + AnimasUtils.short2hex(adp.getReceivedDataBit(4)) //
                + AnimasUtils.short2hex(adp.getReceivedDataBit(3)) //
                + AnimasUtils.short2hex(adp.getReceivedDataBit(2)) + " ");

        PumpConnectorInfo pci = getData().pumpConnectorInfo;

        pci.rawSerialNumber = serialNumber;

        pci.deviceAdapter[0] = Integer.parseInt(serialNumber.substring(6, 8), 16);
        pci.deviceAdapter[1] = Integer.parseInt(serialNumber.substring(4, 6), 16);
        pci.deviceAdapter[2] = Integer.parseInt(serialNumber.substring(2, 4), 16);
        pci.deviceAdapter[3] = Integer.parseInt(serialNumber.substring(0, 2), 16);

        String serial = "";
        for (int i = 3; i >= 0; i--)
        {
            serial = serial + AnimasUtils.short2hex((short) pci.deviceAdapter[i]);
        }

        pci.deviceAdapterSerialNumber = serial;

        LOG.debug("Downloader Device SN: " + serialNumber);
    }


    private void decodeSerialNumber(AnimasDevicePacket packet)
    {
        String serialRaw = "";

        for (int j = 6; j <= 16; j++)
        {
            short ch = packet.getReceivedDataBit(j);

            if ((ch >= 32) && (ch <= 126))
            {
                serialRaw += ch;
            }
            else
            {
                serialRaw += "?";
            }
        }

        getData().pumpInfo.serialNumber = serialRaw.substring(8, 10) + "-" + serialRaw.substring(0, 8);
    }


    private ATechDate decodeDateTime(AnimasDevicePacket adp)
    {
        return this.decodeDateTimeFromRawComponents(adp.getReceivedDataBit(7), adp.getReceivedDataBit(6), adp.getReceivedDataBit(8), adp.getReceivedDataBit(9));
    }


    protected String getDateTimeFromCalendar(Calendar c)
    {
        return c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH)+1) + "." + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
    }


    protected ATechDate decodeDateTimeFromRawComponents(short day, short month_year, short hour, short minute)
    {
        short year = 0;
        short month = 0;

        year = (short) ((month_year & 0xF) + 2000);

        // Vibe ??
        if (this.getData().isModelPing())
        {
            year += 7;
        }

        if (this.getData().isModelVibe())
        {
            year += 8;
        }

        month = (short) (((month_year & 0xF0) >> 4) + 1);

        if ((month == 1) && (day == 0))
        {
            return null;
        }

        return new ATechDate(day, month, year, hour, minute, ATechDate.FORMAT_DATE_AND_TIME_S);
    }



    private void decodeClockMode(AnimasDevicePacket packet)
    {
        this.getData().setClockMode(packet.getReceivedDataBit(6));
    }


    private void decodeFoodDb(AnimasDevicePacket packet)
    {
        this.getData().pumpInfo.foodDbSize = (short) AnimasUtils.createIntValueThroughMoreBits(
                AnimasUtils.getUnsignedShort(packet.getReceivedDataBit(7)), //
                AnimasUtils.getUnsignedShort(packet.getReceivedDataBit(6)));
    }


    private void decodeLanguageIndex(AnimasDevicePacket packet)
    {
        this.getData().pumpInfo.languageIndex1 = packet.getReceivedDataBit(6);
        this.getData().pumpInfo.languageIndex2 = packet.getReceivedDataBit(7);
    }


    private void decodeBGUnit(AnimasDevicePacket adp)
    {
        short unit = adp.getReceivedDataBit(6);
        this.getData().setBgUnit(unit == 1);
    }


    private void decodeSoftwareCode(AnimasDevicePacket packet)
    {
        String swCode = "";

        for (int j = 6; j <= (packet.dataReceivedLength); j++)
        {
            short ch = packet.getReceivedDataBit(j);
            if ((ch != 0) && (ch >= 32) && (ch <= 126))
            {
                swCode += (char) ch;
            }
        }

        this.getData().pumpInfo.softwareCode = swCode;
    }


    private void decodeFontTableIndex(AnimasDevicePacket packet)
    {
        this.getData().pumpInfo.fontTableIndex = packet.getReceivedDataBit(13);
    }


    // UTILS

    protected boolean getBooleanValue(short s)
    {
        return (s == 1);
    }


    protected String getStringFromPacket(AnimasDevicePacket adp, int startBit, int length)
    {
        String receivedString = "";

        for (int j = startBit; j <= (startBit + length); j++)
        {
            if (adp.getReceivedDataBit(j) != 0)
            {
                receivedString += (char) adp.getReceivedDataBit(j);
            }
        }

        return receivedString;
    }


    protected ATechDate calculateTimeFromTimeSet(int timeSet)
    {
        int timeMin = timeSet * 30;

        int hour = timeMin / 60;

        long timeFull = hour * 100L + timeMin % 60;

        return new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, timeFull);
    }


}
