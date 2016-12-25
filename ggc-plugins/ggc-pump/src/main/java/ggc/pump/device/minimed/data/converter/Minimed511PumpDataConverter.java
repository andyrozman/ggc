package main.java.ggc.pump.device.minimed.data.converter;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.defs.ClockModeType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverterAbstract;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import main.java.ggc.pump.data.defs.PumpBaseType;
import main.java.ggc.pump.data.defs.PumpConfigurationGroup;
import main.java.ggc.pump.util.DataAccessPump;

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
 *  Filename:     Minimed511PumpDataConverter
 *  Description:  Data converter class. This will decode Configuration of Pump device (for 511)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed511PumpDataConverter extends MinimedDataConverterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed511PumpDataConverter.class);


    public Minimed511PumpDataConverter(DataAccessPump dataAccess)
    {
        super(dataAccess);
        this.bitUtils = MinimedUtil.getBitUtils();
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {

            case FirmwareVersion: // 116
                debugConverterResponse(minimedReply);
                decodeStringValue("CFG_BASE_FIRMWARE_VERSION", PumpConfigurationGroup.Device, minimedReply);
                break;

            case PumpId: // 113
                debugConverterResponse(minimedReply);
                decodeStringValue("CFG_BASE_SERIAL_NUMBER", PumpConfigurationGroup.Device, minimedReply);
                break;

            case RealTimeClock: // 112
                debugConverterResponse(minimedReply);
                decodeCurrentTimeStamp(minimedReply);
                break;

            case RemoteControlIds: // 118
                debugConverterResponse(minimedReply);
                getUnknownSettings(minimedReply);
                break;

            case BatteryStatus: // 127
                debugConverterResponse(minimedReply);
                decodeBatteryStatus(minimedReply);
                break;

            case RemainingInsulin: // 115
                debugConverterResponse(minimedReply);
                decodeRemainingInsulin(minimedReply);
                break;

            case Settings_511: // 127
                debugConverterResponse(minimedReply);
                decodeCurrentSettings(minimedReply);
                break;

            case HistoryData_511: // 128
                historyDataConversionNote(LOG, MinimedCommandType.HistoryData_511);
                break;

            case Profile_STD_511: // 122
            case Profile_A_511: // 123
            case Profile_B_511: // 124
                debugConverterResponse(minimedReply);
                decodeProfile(minimedReply);
                break;

            case DetectBolus:
            case SetTemporaryBasal:
                debugConverterResponse(minimedReply);
                decodeTempBasal(minimedReply);
                break;

            case PumpState:
            case ReadPumpErrorStatus:
                debugConverterResponse(minimedReply);
                decodePumpStatus("PUMP_STATUS", minimedReply);
                break;

            default:
                LOG.warn("Unknown command type [" + minimedReply.getCommandType().name() + "] for decoding");

        }
    }


    public Object getReplyValue(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {
            case SetSuspend:
            case CancelSuspend:
            case CommandAck:
            case PushAck:
            case PushEsc:
            case PumpState:
            case ReadPumpErrorStatus:
            case DetectBolus:
            case ReadTemporaryBasal:
            case SetTemporaryBasal:
            case PumpStatus:
                return decodePumpStatus(null, minimedReply);

            default:
                break;
        }

        return null;
    }


    public void refreshOutputWriter()
    {
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    // FIXME check on 512
    public void decodeCurrentSettings(MinimedCommandReply commandReply)
    {
        // System.out.println("ERROR: Decode Current Settings (Minimed511)");
        // log.debug("convertCurrentSettings");

        byte rd[] = commandReply.getRawData();

        writeSetting("PCFG_AUTOOFF_TIMEOUT", "" + rd[0], PumpConfigurationGroup.General);

        // int i = rd[1];

        if (rd[0] == 4)
        {
            writeSetting("PCFG_ALARM_MODE", getMessage("PCFG_ALARM_MODE_SILENT"), PumpConfigurationGroup.Sound);
        }
        else
        {
            writeSetting("PCFG_ALARM_MODE", getMessage("PCFG_ALARM_MODE_NORMAL"), PumpConfigurationGroup.Sound);
            writeSetting("PCFG_ALARM_BEEP_VOLUME", "" + rd[0], PumpConfigurationGroup.Sound);
        }

        writeSetting("PCFG_AUDIO_BOLUS_ENABLED", parseResultEnable(rd[2]), PumpConfigurationGroup.Bolus);

        if (rd[2] == 1)
        {
            writeSetting("PCFG_AUDIO_BOLUS_STEP_SIZE", "" + decodeBolusInsulin(rd[3]), PumpConfigurationGroup.Bolus);
        }

        writeSetting("PCFG_VARIABLE_BOLUS_ENABLED", parseResultEnable(rd[4]), PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_MAX_BOLUS", "" + decodeMaxBolus(rd), PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_MAX_BASAL",
            "" + decodeBasalInsulin(
                this.bitUtils.makeUnsignedShort(rd[getSettingIndexMaxBasal()], rd[getSettingIndexMaxBasal() + 1])),
            PumpConfigurationGroup.Basal);

        ClockModeType clockModeType = ClockModeType.getByCode(rd[getSettingIndexTimeDisplayFormat()], true);

        MinimedUtil.setClockMode(clockModeType);

        writeSetting("CFG_BASE_CLOCK_MODE", clockModeType.getTranslation(), PumpConfigurationGroup.General);
        writeSetting("PCFG_INSULIN_CONCENTRATION", "" + (rd[9] != 0 ? 50 : 100), PumpConfigurationGroup.Insulin);
        writeSetting("PCFG_BASAL_PROFILES_ENABLED", parseResultEnable(rd[10]), PumpConfigurationGroup.Basal);

        if (rd[10] == 1)
        {
            String patt;
            switch (rd[11])
            {
                case 0:
                    patt = "STD";
                    break;

                case 1:
                    patt = "A";
                    break;

                case 2:
                    patt = "B";
                    break;

                default:
                    patt = getMessage("UNKNOWN");
                    break;
            }

            writeSetting("PCFG_ACTIVE_BASAL_PROFILE", patt, PumpConfigurationGroup.Basal);

        }

        writeSetting("CFG_MM_RF_ENABLED", parseResultEnable(rd[12]), PumpConfigurationGroup.General);
        writeSetting("CFG_MM_BLOCK_ENABLED", parseResultEnable(rd[13]), PumpConfigurationGroup.General);

    }


    int getSettingIndexMaxBasal()
    {
        return 6;
    }


    int getSettingIndexTimeDisplayFormat()
    {
        return 8;
    }


    public double decodeMaxBolus(byte ai[])
    {
        return decodeBolusInsulin(ai[5]);
    }


    public int decodeTempBasal(MinimedCommandReply minimedReply)
    {
        writeSetting("PCFG_TEMP_BASAL_RATE", "" + decodeBasalInsulin(minimedReply.getRawDataBytesAsUnsignedShort(0, 1)),
            PumpConfigurationGroup.Basal);
        int duration = minimedReply.getRawDataBytesAsUnsignedShort(2, 3);
        writeSetting("PCFG_TEMP_BASAL_DURATION", "" + duration, PumpConfigurationGroup.Basal);

        // log.info("decodeTempBasal: Temp Basal Rate = " +
        // this.util.config.settings.get("MM_TEMP_BASAL_RATE"));
        // log.info("decodeTempBasal: Temp Basal Remain Dur = " +
        // this.util.config.settings.get("MM_TEMP_BASAL_DURATION"));
        // return i;
        return duration;
    }


    protected int decodePumpStatus(String key, MinimedCommandReply minimedReply)
    {
        if (key != null)
        {
            writeSetting(key, "" + minimedReply.getRawData()[0], PumpConfigurationGroup.General);
        }

        return minimedReply.getRawData()[0];
    }


    protected void decodeProfile(MinimedCommandReply minimedReply)
    {
        byte rep[] = minimedReply.getRawData();

        String profile = getProfileName(minimedReply);

        String key = getMessage("PCFG_BASAL_PROFILE") + " " + profile;

        writeSetting(key + " " + i18nControl.getMessage("CFG_BASE_NAME"), profile, profile,
            PumpConfigurationGroup.Basal);

        // 0x12 0x00 0x00 0x16 0x00 0x11 0x00

        if ((rep.length >= 3) && (rep[2] == 0x3F))
        {
            String i18value = i18nControl.getMessage("NOT_SET");
            writeSetting(key, i18value, i18value, PumpConfigurationGroup.Basal);
            return;
        }

        int time_x;
        double vald;

        for (int i = 0; i < rep.length; i += 3)
        {
            vald = decodeBasalInsulin(this.bitUtils.makeUnsignedShort(rep[i + 1], rep[i]));

            time_x = rep[i + 2];

            ATechDate atd = getTimeFrom30MinInterval(time_x);

            if ((i != 0) && (time_x == 0))
            {
                break;
            }

            String value = i18nControl.getMessage("CFG_BASE_FROM") + "=" + atd.getTimeString() + ", "
                    + i18nControl.getMessage("CFG_BASE_AMOUNT") + "=" + vald;

            writeSetting(key, value, value, PumpConfigurationGroup.Basal);
        }
    }


    protected String getProfileName(MinimedCommandReply minimedReply)
    {
        MinimedCommandType commandType = minimedReply.getCommandType();

        String profile;

        if (commandType == MinimedCommandType.Profile_A_511 || commandType == MinimedCommandType.Profile_A)
        {
            profile = "A";
        }
        else if (commandType == MinimedCommandType.Profile_B_511 || commandType == MinimedCommandType.Profile_B)
        {
            profile = "B";
        }
        else
        {
            profile = "STD";
        }

        return profile;
    }


    private String decodeBatteryStatus(MinimedCommandReply minimedReply)
    {
        int status = minimedReply.getRawDataAsInt(0);

        String status_s;

        if (status == 0)
        {
            status_s = getMessage("CFG_BASE_BATTERY_STATUS_NORMAL");
        }
        else if (status == 1)
        {
            status_s = getMessage("CFG_BASE_BATTERY_STATUS_LOW");
        }
        else if (status == 2)
        {
            status_s = getMessage("CFG_BASE_BATTERY_STATUS_OFF");
        }
        else
        {
            status_s = String.format(getMessage("CFG_BASE_BATTERY_STATUS_UNKNOWN"), status);
        }

        if (minimedReply.getRawData().length > 2)
        {
            // if response in 3 bytes then we add additional information
            double d = minimedReply.getRawDataBytesAsInt(1, 2) / 100.0d;

            status_s += ", " + getMessage("CFG_BASE_VOLTAGE") + ": " + d + " V ("
                    + getMessage("CFG_MM_VOLTAGE_NORMAL_1_5_V") + ")";

            // LOG.warn("Unknown status: " + status + " Resolved to: " +
            // status_s);
            // LOG.warn("Full result: " + minimedReply.getRawDataForDebug());
        }

        this.writeSetting("CFG_BASE_BATTERY_STATUS", status_s, status_s, PumpConfigurationGroup.Device);

        return status_s;
    }


    protected void decodeRemainingInsulin(MinimedCommandReply minimedReply)
    {
        double value = minimedReply.getRawDataBytesAsUnsignedShort(0, 1) / 10.0f;

        this.writeSetting("PCFG_REMAINING_INSULIN", DataAccessPump.DecimalFormaters[1].format(value), value,
            PumpConfigurationGroup.Insulin);
    }


    private String decodeCurrentTimeStamp(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        GregorianCalendar gc;

        if (MinimedUtil.getDeviceType() == MinimedDeviceType.Minimed_511)
        {
            int year = rd[2];

            if (year > 90 && year < 100)
            {
                year += 1900;
            }

            if (year < 1900)
            {
                year += 2000;
            }

            gc = new GregorianCalendar(year, rd[1] - 1, rd[0], this.bitUtils.makeUnsignedShort(rd[3], rd[4]), rd[5],
                    rd[6]);
        }
        else
        {
            gc = new GregorianCalendar(this.bitUtils.makeUnsignedShort(rd[3], rd[4]), rd[5] - 1, rd[6], rd[0], rd[1],
                    0);
        }

        String dtString = ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin, gc);

        writeSetting("CFG_BASE_DISPLAY_TIME", dtString, dtString, PumpConfigurationGroup.Device);

        return ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin, gc);
    }


    // UTIL METHODS

    protected String basalInsulinFormat(double value)
    {
        return DataAccessPump.DecimalFormaters[3].format(value);
    }


    protected String getMessage(String key)
    {
        return i18nControl.getMessage(key);
    }


    public double decodeBasalInsulin(int i)
    {
        return (double) i / (double) getStrokesPerUnit(PumpBaseType.Basal);
    }


    public double decodeBolusInsulin(int i)
    {
        return (double) i / (double) getStrokesPerUnit(PumpBaseType.Bolus);
    }


    public float getStrokesPerUnit(PumpBaseType baseType)
    {
        return 10.0f;
        // return baseType == PumpBaseType.Basal ? 10 : 10;
    }


    protected void decodeStringValue(String key, PumpConfigurationGroup pcg, MinimedCommandReply minimedReply)
    {
        String value = createString(minimedReply);
        writeSetting(key, value, value, pcg);
    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, PumpConfigurationGroup pcg)
    {
        decodeEnableSetting(key, minimedReply, 0, pcg);
    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, int bit,
            PumpConfigurationGroup pcg)
    {
        writeSetting(key, parseResultEnable(minimedReply.getRawData()[bit]), pcg);
    }


    protected void writeSetting(String key, String value, Object rawValue, PumpConfigurationGroup group)
    {
        if (rawValue != null)
        {
            writeSetting(key, value, group);
        }
        else
        {
            if (dataDebug)
            {
                LOG.info("Raw Value is null, this value won't be written. [key={}, value={}, group={}]", key, value,
                    group.name());
            }
        }
    }


    protected void writeSetting(String key, String value, PumpConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));

        if (dataDebug)
        {
            LOG.info("CONFIGURATION: [key={}, description={}, value={}, group={}]", key, i18nControl.getMessage(key),
                value, group.name());
        }
    }

}
