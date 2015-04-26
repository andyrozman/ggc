package ggc.cgms.device.animas.impl.converter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

import ggc.cgms.device.animas.impl.data.AnimasCGMSDeviceData;
import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomHistoryEntry;
import ggc.cgms.device.animas.impl.data.dto.AnimasDexcomWarning;
import ggc.cgms.device.animas.impl.data.dto.CGMSSettings;
import ggc.cgms.device.animas.impl.data.enums.AnimasCGMSWarningType;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.converter.AnimasAbstractDataConverter;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDeviceReplyPacket;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.SoundValueType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;

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
 *  Filename:     AnimasDexcomDataConverter
 *  Description:  Converter for Dexcom Animas Data
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasDexcomDataConverter extends AnimasAbstractDataConverter
{
    public static final Log LOG = LogFactory.getLog(AnimasDexcomDataConverter.class);

    AnimasCGMSDeviceData data;
    HashMap<String, BigDecimal> bigDecimals = new HashMap<String, BigDecimal>();
    SimpleTimeZone timezoneUTC;


    public AnimasDexcomDataConverter(AnimasDeviceReader deviceReader, AnimasDeviceData data)
    {
        super(deviceReader);
        // super(portName, deviceType, deviceReader);

        this.data = (AnimasCGMSDeviceData) data;
        this.createNeededBigDecimals();
        timezoneUTC = new SimpleTimeZone(0, "UTC");
    }


    @Override
    public AnimasDeviceData getData()
    {
        return this.data;
    }


    private void createNeededBigDecimals()
    {
        bigDecimals.put("BIG_DECIMAL_1000", new BigDecimal(1000));
    }

    // DATA METHODS


    @Override
    public void processCustomReturnedRawData(AnimasDeviceReplyPacket animasDevicePacket, ATechDate dt)
    {
        switch (animasDevicePacket.dataTypeObject)
        {
            case DexcomSettings:
                decodeDexcomSettings(animasDevicePacket);
                break;

            case DexcomWarnings: // 43
                decodeDexcomWarnings(animasDevicePacket, dt);
                break;

            case DexcomBgHistory: // 45
                decodeDexcomBgHistory(animasDevicePacket);
                break;

            case Dexcom_C3:
            case Dexcom_C5:
            case Dexcom_C6:
            case Dexcom_C7:
                decodeDexcomDataSpecific(animasDevicePacket, animasDevicePacket.dataTypeObject.name());
                break;

            default:
                LOG.warn(String.format("This type (code=%s) is not available/supported.",
                    animasDevicePacket.dataTypeObject.getCode()));
        }
    }


    private void decodeDexcomWarnings(AnimasDeviceReplyPacket packet, ATechDate dt)
    {
        for (int i = 0; i < 8; i++)
        {

            try
            {
                int start = 6 + (i * 14);

                AnimasDexcomWarning warn = new AnimasDexcomWarning();

                warn.dateTime = this.decodeDateTimeFromRawComponents(packet.getReceivedDataBit(start + 1),
                    packet.getReceivedDataBit(start), packet.getReceivedDataBit(start + 2),
                    packet.getReceivedDataBit(start + 3));

                warn.warningCode = packet.getReceivedDataBit(start + 4);

                warn.warningType = AnimasCGMSWarningType.getWarningType(warn.warningCode);

                short[] dataBits = { packet.getReceivedDataBit(start + 5), packet.getReceivedDataBit(start + 6),
                                    packet.getReceivedDataBit(start + 7), packet.getReceivedDataBit(start + 8),
                                    packet.getReceivedDataBit(start + 9) };

                warn.dataBits = dataBits;

                data.addDexcomWarning(warn);

            }
            catch (Exception ex)
            {
                LOG.error("Exception parsing warning: " + ex);
            }
        }
    }


    public void decodeDexcomBgHistory(AnimasDeviceReplyPacket packet)
    {
        for (int i = 6; i < packet.dataReceived.size() - 2; i += 6)
        {
            AnimasDexcomHistoryEntry entry = new AnimasDexcomHistoryEntry();

            entry.glucoseValueWithFlags = (short) AnimasUtils.createIntValueThroughMoreBits(
                packet.getReceivedDataBit(i), packet.getReceivedDataBit(i + 1));

            entry.dateTime = convertDexcomDateTimeToATechDate(packet.getReceivedDataBit(i + 2),
                packet.getReceivedDataBit(i + 3), packet.getReceivedDataBit(i + 4), packet.getReceivedDataBit(i + 5));

            if (entry.dateTime.getGregorianCalendar().get(Calendar.YEAR) > 2030)
            {
                LOG.warn("Invalid BG Entry: [datetime=" + entry.dateTime.getDateTimeString() + ", Val="
                        + entry.getGlucoseValueWithoutFlags());
                continue;
            }

            if (entry.hasGlucoseValue())
            {
                data.addDexcomHistory(entry);
            }
            else if (entry.hasSpecialValue())
            {
                data.addDexcomTransmiterEvent(entry);
            }
            else
            {
                LOG.warn("Unidentified Dexcom Bg History record: " + entry);
            }
        }
    }


    private void decodeDexcomSettings(AnimasDeviceReplyPacket packet)
    {
        CGMSSettings settings = this.data.cgmsSettings;

        settings.soundVolumes.put(AnimasSoundType.CGMS_HighAlert,
            SoundValueType.getByCode(packet.getReceivedDataBit(6)));
        settings.soundVolumes
                .put(AnimasSoundType.CGMS_LowAlert, SoundValueType.getByCode(packet.getReceivedDataBit(7)));
        settings.soundVolumes
                .put(AnimasSoundType.CGMS_RiseRate, SoundValueType.getByCode(packet.getReceivedDataBit(8)));
        settings.soundVolumes
                .put(AnimasSoundType.CGMS_FallRate, SoundValueType.getByCode(packet.getReceivedDataBit(9)));
        settings.soundVolumes.put(AnimasSoundType.CGMS_Range, SoundValueType.getByCode(packet.getReceivedDataBit(10)));
        settings.soundVolumes.put(AnimasSoundType.CGMS_Others, SoundValueType.getByCode(packet.getReceivedDataBit(11)));

        settings.highAlertWarnAbove = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(12),
            packet.getReceivedDataBit(13));

        settings.lowAlertWarnBelow = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(14),
            packet.getReceivedDataBit(15));

        settings.riseRateWarnAbove = packet.getReceivedDataBit(16);

        settings.fallRateWarnAbove = packet.getReceivedDataBit(17);

        settings.highAlertSnoozeTime = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(18),
            packet.getReceivedDataBit(19));
        settings.lowAlertSnoozeTime = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(20),
            packet.getReceivedDataBit(21));
        settings.transmiterOutOfRangeSnoozeTime = packet.getReceivedDataBit(22);

        // bit 23, could be part of transmiterOutOfRangeSnoozeTime, but since
        // that is limited to 201, it might not be

        settings.highAlertWarningEnabled = getBooleanValue(packet.getReceivedDataBit(24));
        settings.lowAlertWarningEnabled = getBooleanValue(packet.getReceivedDataBit(25));
        settings.riseRateWarningEnabled = getBooleanValue(packet.getReceivedDataBit(26));
        settings.fallRateWarningEnabled = getBooleanValue(packet.getReceivedDataBit(27));
        settings.transmiterOutOfRangeWarningEnabled = getBooleanValue(packet.getReceivedDataBit(28));

        settings.transmiterSerialNumber = "" + (char) packet.getReceivedDataBit(29) //
                + (char) packet.getReceivedDataBit(30) //
                + (char) packet.getReceivedDataBit(31) //
                + (char) packet.getReceivedDataBit(32);

        // 33= 77 34= 239 35=118
        // 77 239 118
    }


    private void decodeDexcomDataSpecific(AnimasDeviceReplyPacket packet, String name)
    {
        AnimasUtils.debugHexData(true, packet.dataReceived, packet.dataReceived.size(), name + ": %s [%s]", LOG);
    }


    // UTILS


    /**
     * This method now works for DaylightSaving and for GMT, but we still need to determine if this will work
     * in other timezones. But for now this is implemented
     *
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
    private ATechDate convertDexcomDateTimeToATechDate(short b1, short b2, short b3, short b4)
    {
        BigDecimal dec = AnimasUtils.createBigDecimalValueThroughMoreBits(b1, b2, b3, b4). //
                multiply(bigDecimals.get("BIG_DECIMAL_1000"));

        Calendar c = Calendar.getInstance(this.timezoneUTC);
        c.setTimeInMillis(dec.longValue());

        c.add(Calendar.YEAR, 38);

        if (!TimeZone.getDefault().inDaylightTime(c.getTime()))
        {
            c.add(Calendar.HOUR_OF_DAY, -1);
        }

        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, c);
    }

}
