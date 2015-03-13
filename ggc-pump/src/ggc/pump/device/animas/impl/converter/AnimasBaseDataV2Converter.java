package ggc.pump.device.animas.impl.converter;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.converter.AnimasAbstractDataConverter;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.data.AnimasDevicePacket;
import ggc.plugin.device.impl.animas.enums.AnimasSoundType;
import ggc.plugin.device.impl.animas.enums.advsett.*;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.pump.device.animas.impl.data.AnimasPumpDeviceData;
import ggc.pump.device.animas.impl.data.dto.*;
import ggc.pump.device.animas.impl.data.enums.AnimasBolusSettingSubType;

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
 *  Filename:     AnimasBaseDataV2Converter
 *  Description:  Converter for basic Animas Data for Implementation V2
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasBaseDataV2Converter extends AnimasAbstractDataConverter
{

    public static final Log LOG = LogFactory.getLog(AnimasBaseDataV2Converter.class);
    AnimasPumpDeviceData data;
    boolean inDataProcessing = false;
    HashMap<String, BigDecimal> bigDecimals = new HashMap<String, BigDecimal>();
    int bolusExtendedIndex = 1;
    private boolean inDataProcessingPacket = false;


    public AnimasBaseDataV2Converter(AnimasDeviceReader deviceReader, AnimasDeviceData data)
    {
        super(deviceReader);
        // super(portName, deviceType, deviceReader);

        AnimasUtils.setAnimasData(data);

        this.data = (AnimasPumpDeviceData) data;
        this.createNeededBigDecimals();
    }


    @Override
    public AnimasDeviceData getData()
    {
        return this.data;
    }


    private void createNeededBigDecimals()
    {
        bigDecimals.put("BIG_DECIMAL_100f", new BigDecimal(100.0f));
        bigDecimals.put("BIG_DECIMAL_0", new BigDecimal(0));
        bigDecimals.put("BIG_DECIMAL_6", new BigDecimal(6));
        bigDecimals.put("BIG_DECIMAL_10f", new BigDecimal(10.0f));
        bigDecimals.put("BIG_DECIMAL_18f", new BigDecimal(18.0f));
        bigDecimals.put("BIG_DECIMAL_100", new BigDecimal(100));
        bigDecimals.put("BIG_DECIMAL_1000", new BigDecimal(1000));
        bigDecimals.put("BIG_DECIMAL_10000f", new BigDecimal(10000.0f));
        bigDecimals.put("BIG_DECIMAL_1000f", new BigDecimal(1000.0f));

    }


    public void processCustomReturnedRawData(AnimasDevicePacket animasDevicePacket, ATechDate dt)
    {
        switch (animasDevicePacket.dataTypeObject)
        {
            case BasalProfile: // 11
                decodeBasalProfile(animasDevicePacket);
                break;

            case ActiveBasal: // 12
                decodeActiveBasal(animasDevicePacket);
                break;

            case AdvancedSettings: // 13
                decodeAdvancedSettings(animasDevicePacket);
                break;

            case SoundSettings: // 14
                decodeSoundVolumeSettings(animasDevicePacket);
                break;

            case BasalProfileName: // 18
                decodeBasalProfileName(animasDevicePacket);
                break;

            case PumpSettings1_Pre2020: // 19
            case PumpSettings2_Pre2020: // 20
                decode19or20(animasDevicePacket);
                break;

            case BolusHistory: // 21
                decodeBolusLog(animasDevicePacket, dt);
                break;

            case TotalDailyDoseHistory: // 22
                decodeTotalDailyDoseHistory(animasDevicePacket, dt);
                break;

            case AlarmHistory: // 23
                decodeAlarmLog(animasDevicePacket, dt);
                break;

            case PrimeHistory: // 24
                decodePrimeLog(animasDevicePacket, dt);
                break;

            case SuspendHistory: // 25
                decodeSuspendHistory(animasDevicePacket, dt);
                break;

            case BasalRateHistory: // 26
                decodeBasalLog(animasDevicePacket, dt);
                break;

            case DosingSettings: // 37
                decodeDosingSettings(animasDevicePacket);
                break;

            case BolusHistoryExt: // 38
                decodeExtendedBolus(animasDevicePacket);
                break;

            case InsulinCarbRatio: // 39_4
                decodeInsulinCHRatio(animasDevicePacket);
                break;

            case InsulinBGRatio: // 39_5
                decodeInsulinBGRatio(animasDevicePacket);
                break;

            case BGTargetSetting: // 39_6
                decodeBGTarget(animasDevicePacket);
                break;

            case FriendlyName: // 41
                decodeFriendlyName(animasDevicePacket);
                break;

            // case BGMHistory:
            // decodeBGLog(rawData);
            // break;

            default:
                LOG.warn(String.format("This type (code=%s) is not available/supported.",
                        animasDevicePacket.dataTypeObject.getCode()));
        }

    }


    // UTILS

    // DATA METHODS

    private void decodeAlarmLog(AnimasDevicePacket packet, ATechDate dateTime)
    {
        int code = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(11));
        int subCode = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(12),
            packet.getReceivedDataBit(13));
        String codes = "code=" + code + ",subCode=" + subCode;

        if (code <= 127)
        {
            packet.addPreparedData("Alarm_Call_Service", dateTime);
            LOG.warn("Alarm Call Service, with Code: " + code);
        }
        else
        {
            if ((code >= 145) && (code <= 149))
            {
                packet.addPreparedData("Alarm_Occlusion_Detected", dateTime, codes);
            }
            else
            {
                switch (code)
                {
                    case 128:
                        packet.addPreparedData("Alarm_Replace_Battery", dateTime, codes);
                        break;

                    case 144:
                        packet.addPreparedData("Alarm_Empty_Cartridge", dateTime, codes);
                        break;

                    case 150:
                        packet.addPreparedData("Alarm_Auto_Off", dateTime, codes);
                        break;

                    case 177:
                        packet.addPreparedData("Alarm_Low_Battery", dateTime, codes);
                        break;

                    case 178:
                        packet.addPreparedData("Alarm_Low_Cartridge", dateTime, codes);
                        break;

                    default:
                        {
                            packet.addPreparedData("Alarm_Unknown", dateTime, codes);
                            LOG.warn("Unknown Alarm Code: " + codes);
                        }
                        break;
                }
            }
        }
    }


    private void decodeBolusLog(AnimasDevicePacket packet, ATechDate dateTime)
    {
        BolusEntry bolusEntry = new BolusEntry();

        bolusEntry.dateTime = dateTime;
        bolusEntry.bolusType = (short) ((packet.getReceivedDataBit(18) >> 6) & 0x3);
        bolusEntry.bolusSubType = packet.getReceivedDataBit(18) & 0x3;
        bolusEntry.bolusRecordType = (packet.getReceivedDataBit(18) >> 2) & 0x3;

        bolusEntry.amount = AnimasUtils.createBigDecimalValueThroughMoreBits( //
            packet.getReceivedDataBit(10), packet.getReceivedDataBit(11), //
            packet.getReceivedDataBit(12), packet.getReceivedDataBit(13)).divide(bigDecimals.get("BIG_DECIMAL_10000f"));
        bolusEntry.requestedAmount = AnimasUtils.createBigDecimalValueThroughMoreBits( //
            packet.getReceivedDataBit(14), //
            packet.getReceivedDataBit(15)).divide(bigDecimals.get("BIG_DECIMAL_1000f"));
        bolusEntry.duration = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(16), //
            packet.getReceivedDataBit(17)).multiply(bigDecimals.get("BIG_DECIMAL_6"));

        if (this.data.isModel2020OrHigher())
        {
            bolusEntry.syncCounter = packet.getReceivedDataBit(19);
        }

        bolusEntry.prepareEntry();
        bolusEntry.createPreparedData(packet, this);

        this.data.addBolusLogEntry(bolusEntry);
    }


    public void writeDataInternal(AnimasDevicePacket packet, String key, ATechDate dateTime, String value)
    {
        this.data.writeData(key, dateTime, value);
    }


    private void decodeExtendedBolus(AnimasDevicePacket packet)
    {
        BolusExtEntry ebs = new BolusExtEntry();
        ebs.syncRecordId = packet.getReceivedDataBit(6);

        int BGmult = 10;
        if ((packet.getReceivedDataBit(17) & 0x2) == 2)
        {
            BGmult = 18;
        }

        ebs.i2C = packet.getReceivedDataBit(7);
        ebs.carbs = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(8),
            packet.getReceivedDataBit(9));
        ebs.bg = ((int) Math.round(AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(12),
            packet.getReceivedDataBit(13))
                * 0.1D * BGmult));
        ebs.isf = AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(11));
        ebs.bgTarget = ((int) Math.round(AnimasUtils.createIntValueThroughMoreBits(packet.getReceivedDataBit(14),
            packet.getReceivedDataBit(15)) * 0.1D * BGmult));
        ebs.bgDelta = ((int) Math.round(packet.getReceivedDataBit(16) * 0.1D * BGmult));

        short t = (short) ((packet.getReceivedDataBit(17) & 0x4) >> 2);

        ebs.iobEnabled = getBooleanValue(t);
        ebs.iobValue = AnimasUtils
                .createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(18), packet.getReceivedDataBit(19))
                .divide(bigDecimals.get("BIG_DECIMAL_100")).intValue();

        t = (short) ((packet.getReceivedDataBit(17) & 0x8) >> 3);

        ebs.bgCorrection = getBooleanValue(t);
        ebs.iobDuration = (((packet.getReceivedDataBit(17) & 0xF0) >> 4) * 5);
        ebs.iobDuration /= 100;

        BolusEntry be = data.getBolusLogByIndex(bolusExtendedIndex);

        if (be.syncCounter != ebs.syncRecordId)
        {
            LOG.error(String.format(
                "Bolus and BolusExt don't have same syncCounter. Bolus syncCounter %s - BolusExt syncCounter %s",
                be.syncCounter, ebs.syncRecordId));
        }

        ebs.bolusEntry = data.getBolusLogByIndex(bolusExtendedIndex);

        ebs.createPreparedData(packet, this);

        bolusExtendedIndex++;

    }


    private void decodeBasalLog(AnimasDevicePacket packet, ATechDate dateTime)
    {
        BigDecimal rate = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(11)). //
                divide(bigDecimals.get("BIG_DECIMAL_1000"), 3, BigDecimal.ROUND_CEILING);
        int flag = (short) (packet.getReceivedDataBit(12) & 0x1);

        writeDataInternal(packet, "Basal_Value_Change", dateTime, String.format("%6.3f", rate.floatValue()).trim());
    }


    private void decodeSuspendHistory(AnimasDevicePacket packet, ATechDate dateTime)
    {
        packet.addPreparedData("Event_Basal_Stop", dateTime);

        ATechDate dt = decodeDateTimeFromRawComponents(packet.getReceivedDataBit(11), packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(12), packet.getReceivedDataBit(13));

        if (dt != null)
        {
            writeDataInternal(packet, "Event_Basal_Run", dt, null);
        }
    }


    private void decodeTotalDailyDoseHistory(AnimasDevicePacket packet, ATechDate dateTime)
    {
        dateTime.hourOfDay = 0;
        dateTime.minute = 0;
        dateTime.second = 0;

        BigDecimal tdd = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(11), packet.getReceivedDataBit(12), packet.getReceivedDataBit(13)).divide(
            bigDecimals.get("BIG_DECIMAL_10000f"), 3, BigDecimal.ROUND_CEILING);

        packet.addPreparedData("Report_All_Daily_Insulin", dateTime, String.format("%4.3f", tdd.floatValue()));

        BigDecimal basal = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(14),
            packet.getReceivedDataBit(15), packet.getReceivedDataBit(16), packet.getReceivedDataBit(17)).divide(
            bigDecimals.get("BIG_DECIMAL_10000f"), 3, BigDecimal.ROUND_CEILING);

        writeDataInternal(packet, "Report_Daily_Basal_Insulin", dateTime, String.format("%4.3f", basal.floatValue()));
    }


    private void decodePrimeLog(AnimasDevicePacket packet, ATechDate dateTime)
    {
        BigDecimal primeAmount = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(10),
            packet.getReceivedDataBit(11)).divide(bigDecimals.get("BIG_DECIMAL_100"), 2, BigDecimal.ROUND_CEILING);
        short flag = packet.getReceivedDataBit(12);

        // flag 2: prime
        // flag 3: fill canula

        if (flag == 2)
        {
            writeDataInternal(packet, "Event_PrimeInfusionSet", dateTime, //
                String.format("%4.3f", primeAmount.floatValue()));
        }
        else if (flag == 3)
        {
            writeDataInternal(packet, "Event_FillCannula", dateTime, //
                String.format("%4.3f", primeAmount.floatValue()));
        }
        else
        {
            LOG.warn(String.format("decodePrimeLog, unknown flag (%s) with value (%4.2f)", flag,
                primeAmount.floatValue()));
        }
    }


    private void decodeBGLog(AnimasDevicePacket packet)
    {
        // THIS METHOD IS COPIED FROM ORIGINAL CODE, BUT IT DOESN'T WORK, AT
        // LEAST NOT AS IT
        // SHOULD. IT IS UNUSED FOR NOW.

        // bg Log: 02 b2 44 49 00 00 b6 17 0f 05 7d 00 [2 178 68 73 0 0 182 23
        // 15 5 125 0 ]

        // AnimasUtils.debugHexData(true, readBuffer, readBuffer.length-2,
        // "bg Log: %s [%s]", LOG);

        int startx = 10;
        int year = 2000;

        if (this.data.isModelVibe())
        {
            startx = 6;
            year = 2013;
        }

        int BGmult = 10;
        if ((packet.getReceivedDataBit(startx + 7) & 0x2) == 2)
        {
            BGmult = 18;
        }
        int timeDelta = packet.getReceivedDataBit(startx) + (packet.getReceivedDataBit(startx + 1) * 256)
                + (packet.getReceivedDataBit(startx + 2) * 256 * 256);

        System.out.println("Time delta: " + timeDelta);

        Calendar bgTime = Calendar.getInstance();
        if (timeDelta > 0)
        {
            bgTime.set(year, 0, 1, 0, 0, 0);
            bgTime.add(12, timeDelta);

            if (TimeZone.getDefault().inDaylightTime(bgTime.getTime()))
            {
                bgTime.add(10, -1);
            }

            int bgValue = packet.getReceivedDataBit(startx + 3)
                    + ((short) (packet.getReceivedDataBit(startx + 4) & 0x3) * 256);

            // int bgValue = ((int) Math.round((readBuffer[startx+3] +
            // (readBuffer[startx+4] * 256)) * 0.1D * BGmult));

            if (bgValue < 20)
            {
                bgValue = 18;
            }
            if (bgValue > 600)
            {
                bgValue = 601;
            }
            int bgTestFlag = (short) ((packet.getReceivedDataBit(startx + 4) & 0x4) >> 2);

            // System.out.println("BgTestFlag: " + bgTestFlag);

            if (bgTestFlag == 0)
            {
                System.out.println(String.format("Time: %s, bg=%s", getDateTimeFromCalendar(bgTime), bgValue));
            }

        }
    }


    // SETTINGS

    private void decodeSoundVolumeSettings(AnimasDevicePacket packet)
    {
        this.data.setSoundVolume(AnimasSoundType.Normal, SoundValueType.getByCode(packet.getReceivedDataBit(6)));
        this.data.setSoundVolume(AnimasSoundType.AudioBolus, SoundValueType.getByCode(packet.getReceivedDataBit(7)));
        this.data.setSoundVolume(AnimasSoundType.TempBasal, SoundValueType.getByCode(packet.getReceivedDataBit(8)));
        this.data.setSoundVolume(AnimasSoundType.Alerts, SoundValueType.getByCode(packet.getReceivedDataBit(9)));
        this.data.setSoundVolume(AnimasSoundType.Reminders, SoundValueType.getByCode(packet.getReceivedDataBit(10)));
        this.data.setSoundVolume(AnimasSoundType.Warnings, SoundValueType.getByCode(packet.getReceivedDataBit(11)));
        this.data.setSoundVolume(AnimasSoundType.Alarms, SoundValueType.getByCode(packet.getReceivedDataBit(12)));

        if (this.data.isModelPingOrHigher())
        {
            this.data.setSoundVolume(AnimasSoundType.RemoteBolus,
                SoundValueType.getByCode(packet.getReceivedDataBit(13)));
        }

        // 2 = low, 3 - Medium, 4 = High, 1 = Vibrate, 0=Not present
    }


    private void decodeBasalProfileName(AnimasDevicePacket packet)
    {
        String basalName = "";
        int cp = 0;
        while ((packet.getReceivedDataBit((6 + cp)) > 0) && (cp < 8))
        {
            basalName += (char) packet.getReceivedDataBit((6 + cp));
            cp++;
        }

        if (StringUtils.isBlank(basalName))
        {
            basalName = "Pump_Default_Basal" + String.valueOf(this.data.basalProgramNum + 1) + "_Name";
        }

        data.setBasalName(this.data.basalProgramNum + 1, basalName);
    }


    private void decodeAdvancedSettings(AnimasDevicePacket packet)
    {
        PumpSettings pumpSettings = this.data.pumpSettings;

        pumpSettings.audioBolusEnabled = getBooleanValue(packet.getReceivedDataBit(6));
        pumpSettings.audioBolusStepSize = BolusStepSize.getById(packet.getReceivedDataBit(7));
        pumpSettings.advancedBolusEnabled = getBooleanValue(packet.getReceivedDataBit(8));
        pumpSettings.bolusReminderEnabled = getBooleanValue(packet.getReceivedDataBit(9));
        pumpSettings.bolusSpeed = packet.getReceivedDataBit(10) == 0 ? BolusSpeed.Normal : BolusSpeed.Slow;
        pumpSettings.numberOfBasalProfiles = packet.getReceivedDataBit(11);
        pumpSettings.maxBasalAmountProHour = AnimasUtils.createBigDecimalValueThroughMoreBits(
            packet.getReceivedDataBit(12), packet.getReceivedDataBit(13)).divide(
            this.bigDecimals.get("BIG_DECIMAL_100f"), 2, BigDecimal.ROUND_CEILING);
        pumpSettings.maxBolusProHour = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(14),
            packet.getReceivedDataBit(15))
                .divide(this.bigDecimals.get("BIG_DECIMAL_100f"), 2, BigDecimal.ROUND_CEILING);
        pumpSettings.totalDailyDose = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(16),
            packet.getReceivedDataBit(17))
                .divide(this.bigDecimals.get("BIG_DECIMAL_100f"), 2, BigDecimal.ROUND_CEILING);

        int startBit = 18;

        if ((this.data.isModelPingOrHigher()))
        {
            pumpSettings.maxDoseIn2h = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(18),
                packet.getReceivedDataBit(19)).divide(this.bigDecimals.get("BIG_DECIMAL_100f"), 2,
                BigDecimal.ROUND_CEILING);
            startBit = 20;
        }
        else
        {
            pumpSettings.maxDoseIn2h = this.bigDecimals.get("BIG_DECIMAL_0");
        }

        pumpSettings.maxLock = packet.getReceivedDataBit(startBit);
        pumpSettings.language = Language.getLanguageById(packet.getReceivedDataBit(startBit + 1));
        pumpSettings.displayTimeout = packet.getReceivedDataBit(startBit + 2);
        pumpSettings.autoOffEnabled = getBooleanValue(packet.getReceivedDataBit(startBit + 3));
        pumpSettings.autoOffTimeoutHr = packet.getReceivedDataBit(startBit + 4);
        pumpSettings.lowCartridgeWarning = packet.getReceivedDataBit(startBit + 5);
        pumpSettings.occlusionSensitivity = getBooleanValue(packet.getReceivedDataBit(startBit + 6)) ? OcclusionSensitivity.High
                : OcclusionSensitivity.Low;
        pumpSettings.iOBEnabled = getBooleanValue(packet.getReceivedDataBit(startBit + 7));
        pumpSettings.iOBDecay = AnimasUtils.createBigDecimalValueThroughMoreBits(packet.getReceivedDataBit(28)).divide(
            this.bigDecimals.get("BIG_DECIMAL_10f"), 1, BigDecimal.ROUND_CEILING);

        if ((this.data.isModel1200()) || (this.data.isModel1200p()))
        {
            int i2c_s0 = packet.getReceivedDataBit(27);
            int i2c_s1 = packet.getReceivedDataBit(28);
            int i2c_s2 = packet.getReceivedDataBit(29);
            int i2c_s3 = packet.getReceivedDataBit(30);

            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio, 1,
                    this.calculateTimeFromTimeSet(0), i2c_s3));
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio, 2,
                    this.calculateTimeFromTimeSet(8), i2c_s0));
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio, 3,
                    this.calculateTimeFromTimeSet(20), i2c_s1));
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio, 4,
                    this.calculateTimeFromTimeSet(32), i2c_s2));
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio, 5,
                    this.calculateTimeFromTimeSet(44), i2c_s3));
        }
        else
        {
            BigDecimal sickBGOverLimit = AnimasUtils.createBigDecimalValueThroughMoreBits(
                packet.getReceivedDataBit(startBit + 10), packet.getReceivedDataBit(startBit + 11));

            if (this.data.isBGinMgDL())
            {
                pumpSettings.sickDaysCheckOverLimit = sickBGOverLimit;
            }
            else
            {
                pumpSettings.sickDaysCheckOverLimit = sickBGOverLimit.divide(bigDecimals.get("BIG_DECIMAL_18f"), 1,
                    BigDecimal.ROUND_CEILING);
            }

            pumpSettings.sickDaysCheckKetones = packet.getReceivedDataBit(startBit + 12);
            pumpSettings.sickDaysCheckBG = packet.getReceivedDataBit(startBit + 13);
        }

    }


    private void decode19or20(AnimasDevicePacket adp)
    {
        data.pumpSettings.userInfo.put(this.data.basalProgramNum, getStringFromPacket(adp, 6, 12));
    }


    private void decodeActiveBasal(AnimasDevicePacket adp)
    {
        data.setActiveBasalProfile(adp.getReceivedDataBit(6));
    }


    private void decodeBasalProfile(AnimasDevicePacket adp)
    {
        int profileEntries = adp.getReceivedDataBit(7);
        int profileNumber = this.data.basalProgramNum + 1;

        for (int p = 0; p < profileEntries; p++)
        {
            ATechDate time = calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + p)));

            BigDecimal amount = AnimasUtils.createBigDecimalValueThroughMoreBits(
                adp.getReceivedDataBit((20 + (p * 2))), adp.getReceivedDataBit((20 + (p * 2) + 1))).divide(
                bigDecimals.get("BIG_DECIMAL_1000f"), 3, BigDecimal.ROUND_CEILING);

            data.addBasalProfileEntry(profileNumber, new BasalProfileEntry(time, amount.floatValue()));
        }
    }


    private void decodeDosingSettings(AnimasDevicePacket adp)
    {
        for (int i = 0; i < 6; i++)
        {
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio,
                    (i + 1), this.calculateTimeFromTimeSet(i * 8), adp.getReceivedDataBit((6 + i))));

            int isf = AnimasUtils.createIntValueThroughMoreBits(adp.getReceivedDataBit((12 + (2 * i))),
                adp.getReceivedDataBit((12 + (2 * i) + 1)));

            this.data
                    .addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinBGRatio,
                            (i + 1), this.calculateTimeFromTimeSet(i * 8), this.data.isBGinMgDL() ? isf
                            : (float) (isf / 18.0f)));
        }

        for (int i = 0; i < 12; i++)
        {
            short bgTarget = adp.getReceivedDataBit((24 + i));
            short bgDelta = adp.getReceivedDataBit((36 + i));

            this.data
                    .addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.BGTarget, (i + 1),
                            this.calculateTimeFromTimeSet(i * 8), this.data.isBGinMgDL() ? bgTarget
                            : (float) (bgTarget / 18.0f), this.data.isBGinMgDL() ? bgDelta
                            : (float) (bgDelta / 18.0f)));
        }
    }


    private void decodeInsulinCHRatio(AnimasDevicePacket adp)
    {
        int maxEntry = adp.getReceivedDataBit(7);
        for (int i = 0; i < maxEntry; i++)
        {
            this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinCHRatio,
                    (i + 1), this.calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + i))), //
                    AnimasUtils.createIntValueThroughMoreBits(adp.getReceivedDataBit((20 + (i * 2))),
                        adp.getReceivedDataBit((20 + (i * 2) + 1)))));
        }
    }


    private void decodeInsulinBGRatio(AnimasDevicePacket adp) // 39/5
    {
        int maxEntry = adp.getReceivedDataBit(7);
        for (int i = 0; i < maxEntry; i++)
        {
            short isf = (short) AnimasUtils.createIntValueThroughMoreBits(adp.getReceivedDataBit((20 + (i * 2))),
                adp.getReceivedDataBit((20 + (i * 2) + 1)));

            if (this.data.isBGinMgDL())
            {
                this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinBGRatio,
                        (i + 1), this.calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + i))), isf));
            }
            else
            {
                this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.InsulinBGRatio,
                        (i + 1), this.calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + i))), (isf / 18.0f)));
            }
        }
    }


    private void decodeBGTarget(AnimasDevicePacket adp) // 39/6
    {
        int maxEntry = adp.getReceivedDataBit(7);
        for (int i = 0; i < maxEntry; i++)
        {
            short bgTarget = (short) AnimasUtils.createIntValueThroughMoreBits(adp.getReceivedDataBit((20 + (i * 2))),
                adp.getReceivedDataBit((20 + (i * 2) + 1)));
            short bgDelta = adp.getReceivedDataBit((44 + i));

            if (this.data.isBGinMgDL())
            {
                this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.BGTarget,
                        (i + 1), this.calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + i))), bgTarget, bgDelta));
            }
            else
            {
                this.data.addSettingTimeValueEntry(new SettingTimeValueEntry(AnimasBolusSettingSubType.BGTarget,
                        (i + 1), this.calculateTimeFromTimeSet(adp.getReceivedDataBit((8 + i))), (bgTarget / 18.0f),
                        (bgDelta / 18.0f)));
            }
        }
    }


    private void decodeFriendlyName(AnimasDevicePacket adp)
    {
        this.data.pumpSettings.friendlyName = getStringFromPacket(adp, 6, 12).trim();
    }

    // UTILS

    // public void decodeRegAddrData(String regAddr, short[] rawData, int
    // rawDataLength, String rawDataString)
    // {
    // // String tstr;
    //
    // this.data.pumpInfo.registryUsed = true;
    // String hex = "";
    // String rg = "";
    //
    // if (regAddr.equals("40000"))
    // {
    // for (int i = 6; i < (rawDataLength - 1); i++)
    // {
    // rg += (char) rawData[i];
    // hex += AnimasUtils.short2hex(rawData[i]);
    // }
    // this.data.pumpInfo.serialNumber = rg;
    // this.data.pumpInfo.serialNumberHex = hex;
    //
    // LOG.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // LOG.debug("ezU_SerNumString: " + rg);
    // LOG.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // }
    // else if (regAddr.equals("401E0"))
    // {
    // for (int i = 6; i < 18; i++)
    // {
    // rg += (char) rawData[i];
    // hex += AnimasUtils.short2hex(rawData[i]);
    // }
    //
    // this.data.pumpInfo.oSRevision = rg;
    // this.data.pumpInfo.oSRevisionHex = hex;
    // this.data.pumpInfo.oSRevisionReadable = rg.substring(0, 8);
    //
    // LOG.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // LOG.debug("ezU_OSRevRcvString: " + rg);
    // LOG.debug("ezU_OSRevVisSubStr: " +
    // this.data.pumpInfo.oSRevisionReadable);
    // LOG.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    // }
    // else
    // {
    // processReturnedRawData(Integer.parseInt(regAddr, 16), rawData,
    // this.data.getPumpCommunicationInterface(),
    // rawDataString);
    // }
    // }

}
