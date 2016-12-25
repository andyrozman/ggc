package ggc.cgms.device.minimed.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessAbstract;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverterAbstract;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
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
 *  Filename:     Minimed523CGMSDataConverter
 *  Description:  Data converter class. This will decode Configuration of CGMS device (for 523/723 and later)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed523CGMSDataConverter extends Minimed522CGMSDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed523CGMSDataConverter.class);


    @Override
    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {
            case SensorPredictiveAlerts: // 209
                debugConverterResponse(minimedReply);
                decodeSensorPredictiveAlerts(minimedReply);
                break;

            case SensorRateOfChangeAlerts: // 212
                debugConverterResponse(minimedReply);
                decodeSensorRateOfChangeAlerts(minimedReply);
                break;

            case SensorDemoAndGraphTimeout: // 210
                debugConverterResponse(minimedReply);
                decodeSensorDemoAndGraphTimeout(minimedReply);
                break;

            case SensorAlarmSilence: // 211
                debugConverterResponse(minimedReply);
                decodeSensorAlarmSilence(minimedReply);
                break;

            case SensorSettings: // 207
                debugConverterResponse(minimedReply);
                decodeSensorSettings(minimedReply);
                break;

            case OtherDevicesIds: // 240
                debugConverterResponse(minimedReply);
                getUnknownSettings(minimedReply);
                break;

            case VCntrHistory: // 213
                historyDataConversionNote(LOG, MinimedCommandType.VCntrHistory);
                decodeVCntrHistory(null);
                break;

            default:
                super.convertData(minimedReply);
        }
    }


    private void decodeSensorDemoAndGraphTimeout(MinimedCommandReply minimedReply)
    {
        decodeEnableSetting("CCFG_SENSOR_DEMO_ENABLE", minimedReply, 0, CGMSConfigurationGroup.General);
        writeSetting("CCFG_SENSOR_GRAPH_TIMEOUT", "00:0" + minimedReply.getRawDataAsInt(1),
            CGMSConfigurationGroup.General);
    }


    private void decodeVCntrHistory(MinimedCommandReply minimedReply)
    {
        LOG.error("Decode VCntr History not implemented.");
    }


    private void decodeSensorAlarmSilence(MinimedCommandReply minimedReply)
    {
        // this.util.decoder.debugResult(cmd);

        byte[] rd = minimedReply.getRawData();
        String resStatus = null;
        String resTime = "";

        if (rd[0] == 0)
        {
            resStatus = optionOff;
        }
        else if (rd[0] == 1)
        {
            resStatus = getMessage("CCFG_SENSOR_ALERT_SILENCE_HI_ALERTS");
        }
        else if (rd[0] == 2)
        {
            resStatus = getMessage("CCFG_SENSOR_ALERT_SILENCE_LO_ALERTS");
        }
        else if (rd[0] == 4)
        {
            resStatus = getMessage("CCFG_SENSOR_ALERT_SILENCE_HI_LO_ALER.raw_dataTS");
        }
        else if (rd[0] == 8)
        {
            resStatus = getMessage("CCFG_SENSOR_ALERT_SILENCE_ALL_SENSOR_ALERTS");
        }
        else
        {
            resStatus = getMessage("CFG_BASE_INVALID_VALUE" + rd[0]);
        }

        writeSetting("CCFG_SENSOR_ALERT_SILENCE_TYPE", resStatus, CGMSConfigurationGroup.Warnings);

        if (rd[2] != 0)
        {

            int minutes = rd[2];

            minutes += 6;

            int inter = minutes / 30;

            if (inter % 2 == 0)
            {
                resTime = ATDataAccessAbstract.getLeadingZero(inter / 2, 2) + ":00";
            }
            else
            {
                resTime = ATDataAccessAbstract.getLeadingZero((inter - 1) / 2, 2) + ":30";
            }

            writeSetting("CCFG_SENSOR_ALERT_SILENCE_TIME", resTime, CGMSConfigurationGroup.Warnings);
        }

    }


    public void decodeSensorRateOfChangeAlerts(MinimedCommandReply minimedReply)
    {
        // TODO check if this works with mg/dL ?
        // this.util.decoder.debugResult(cmd);

        // System.out.println("Set Fall Rate Limit: " +
        // DataAccessPump.Decimal3Format.format(rd[2]/1000.0f) + " mmol/L/min");
        // System.out.println("Set Rise Rate Limit: " +
        // DataAccessPump.Decimal3Format.format(rd[1]/1000.0f) + " mmol/L/min");

        writeSetting("CCFG_RATE_OF_CHANGE_ALERTS_RISE_RATE_LIMIT",
            minimedReply.getRawDataAsInt(1) == 0 ? "OFF"
                    : DataAccessPlugInBase.Decimal3Format.format(minimedReply.getRawDataAsInt(1) / 1000.0f)
                            + " mmol/L / min",
            CGMSConfigurationGroup.Warnings);

        writeSetting("CCFG_RATE_OF_CHANGE_ALERTS_FALL_RATE_LIMIT",
            minimedReply.getRawDataAsInt(2) == 0 ? "OFF"
                    : DataAccessPlugInBase.Decimal3Format.format(minimedReply.getRawDataAsInt(2) / 1000.0f)
                            + " mmol/L / min",
            CGMSConfigurationGroup.Warnings);

        // SET FALL RATE LIMIT = 0.250
        // SET RISE RATE LIMIT = 0.275
    }


    public boolean decodeSensorPredictiveAlerts(MinimedCommandReply minimedReply)
    {
        if (minimedReply.getRawDataAsInt(4) == 0)
        {
            writeSetting("CCFG_SENSOR_TIME_SENSITIVITY_LOW_PREDICTIVE_ALERT", optionOff,
                CGMSConfigurationGroup.Warnings);
        }
        else
        {
            writeSetting("CCFG_SENSOR_TIME_SENSITIVITY_LOW_PREDICTIVE_ALERT",
                "00:" + (minimedReply.getRawDataAsInt(4) - 128), CGMSConfigurationGroup.Warnings);
        }

        if (minimedReply.getRawDataAsInt(3) == 0)
        {
            writeSetting("CCFG_SENSOR_TIME_SENSITIVITY_HIGH_PREDICTIVE_ALERT", optionOff,
                CGMSConfigurationGroup.Warnings);
        }
        else
        {
            writeSetting("CCFG_SENSOR_TIME_SENSITIVITY_HIGH_PREDICTIVE_ALERT",
                "00:" + (minimedReply.getRawDataAsInt(3) - 128), CGMSConfigurationGroup.Warnings);
        }

        return true;
    }


    @Override
    public void decodeSensorSettings(MinimedCommandReply minimedReply)
    {
        byte rd[] = minimedReply.getRawData();

        // log.debug("Decoded reply: " +
        // hu.getHexCompact(cmd.reply.rawData));

        // this.util.decoder.debugResult(cmd);

        int tmpInt = 0;

        tmpInt = rd[0];

        if (tmpInt >= 128)
        {
            tmpInt -= 128;
            decodeEnableSetting("CCFG_SENSOR_AUTOCALIBRATE", 1, CGMSConfigurationGroup.General);
        }
        else
        {
            decodeEnableSetting("CCFG_SENSOR_AUTOCALIBRATE", 0, CGMSConfigurationGroup.General);
        }

        decodeEnableSetting("CCFG_SENSOR_ENABLED", tmpInt, CGMSConfigurationGroup.General);

        // old (22), new (59) = diff => 37

        GlucoseUnitType glucoseUnitType = GlucoseUnitType.getByCode(minimedReply.getRawDataAsInt(59));

        writeSetting("CCFG_SENSOR_BG_UNITS", glucoseUnitType.getTranslation(), CGMSConfigurationGroup.General);

        if (rd[2] > 0)
        {
            decodeEnableSetting("SENSOR_BG_LIMITS_ENABLED", 1, CGMSConfigurationGroup.Warnings);
            // writeSetting("SENSOR_BG_LIMITS_COUNT", "" + rd[2]);

            int cnt = rd[2];
            int cnt_curr = 0;
            String key = getMessage("CCFG_SENSOR_BG_LIMIT");
            String template = MinimedDataConverterAbstract.templateTimeAmount + "%6.4f - %6.4f %s";
            String unitName = glucoseUnitType.getTranslation();

            for (int i = 3, j = 1; i < 43; i += 5, j++)
            {

                if (j > cnt)
                {
                    break;
                }

                int time = rd[i];

                String lowLimit = "";
                String highLimit = "";

                lowLimit = rd[i + 4] == 0 ? optionOff : "" + getCorrectBGValueFormatted(glucoseUnitType, rd[i + 4]);
                highLimit = rd[i + 2] == 0 ? optionOff : "" + getCorrectBGValueFormatted(glucoseUnitType, rd[i + 2]);

                writeSetting(key + " #" + j, String.format(template, getTimeFrom30MinInterval(time).getTimeString(),
                    lowLimit, highLimit, unitName), CGMSConfigurationGroup.Warnings);
            }

        }
        else
        {
            decodeEnableSetting("SENSOR_BG_LIMITS_ENABLED", 0, CGMSConfigurationGroup.Warnings);
        }

        int diff = 37;

        writeSetting(String.format("CCFG_X_SNOOZE_TIME", getMessage("PCFG_SOUND_HIGH_ALERT")),
            getTimeFromMinutes(rd[44]), CGMSConfigurationGroup.Warnings);
        writeSetting(String.format("CCFG_X_SNOOZE_TIME", getMessage("PCFG_SOUND_LOW_ALERT")),
            getTimeFromMinutes(rd[46]), CGMSConfigurationGroup.Warnings);

        writeSetting("CCFG_SENSOR_CALIBRATION_REPEAT_TIME", getTimeFromMinutes(rd[48]),
            CGMSConfigurationGroup.Transmiter);

        // log.debug("49 : " + rd[49]);
        // log.debug("50 : " + rd[50]); = 100

        // 51 - this might be not correct
        // log.debug("51 : " + rd[51]);
        // log.debug("52 : " + rd[52]); 39

        writeSetting("CCFG_SENSOR_ALARM_SNOOZE_TIME", "" + minimedReply.getRawDataBytesAsInt(diff + 14, diff + 15),
            CGMSConfigurationGroup.Warnings);

        decodeEnableSetting("CCFG_SENSOR_CALIBRATION_REMINDER_ENABLE", minimedReply, diff + 16,
            CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_CALIBRATION_REMINDER_TIME",
            "" + minimedReply.getRawDataBytesAsInt(diff + 17, diff + 18), CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_TRANSMITER_ID",
            "" + minimedReply.getRawDataBytesAsInt(diff + 19, diff + 20, diff + 21), CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_WEAK_SIGNAL_TIME",
            getTimeFromMinutes(minimedReply.getRawDataBytesAsInt(diff + 23, diff + 24)),
            CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_LOW_BG_SUSPEND",
            getCorrectBGValueFormatted(glucoseUnitType, minimedReply.getRawDataBytesAsInt(62, 63)),
            CGMSConfigurationGroup.Transmiter);

    }

}
