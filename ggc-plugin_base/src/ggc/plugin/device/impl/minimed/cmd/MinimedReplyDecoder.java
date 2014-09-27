package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.device.impl.minimed.MinimedDeviceConfig;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;
import ggc.plugin.device.impl.minimed.MinimedDevicesIds;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.CRCUtils;
import com.atech.utils.data.HexUtils;

public class MinimedReplyDecoder
{
    private static Log log = LogFactory.getLog(MinimedReplyDecoder.class);
    // private String m_firmwareVersion;
    private Object m_timeStamp;

    // util.config.m_minYear, util.config.m_maxYear

    private MinimedDevice device = null;
    private MinimedDeviceUtil util = null;
    private CRCUtils hex_util = null;
    private MinimedDeviceConfig config = null;

    private Hashtable<Integer, String> sett_names = null;

    public MinimedReplyDecoder(MinimedDeviceUtil util_, MinimedDevice md)
    {
        this.util = util_;
        this.device = util_.device;

        this.hex_util = util.getHexUtils();
        this.config = util.config;

        loadSettingsNames();
    }

    public Object decode(MinimedCommand command)
    {
        String tmp_str = null;
        switch (command.command_code)
        {
            case MinimedCommand.READ_FIRMWARE_VERSION:
            case MinimedCommand.READ_PUMP_ID:
                // case 113: // 'q' Serial Number
                // case 116: // firmware
                tmp_str = new String(util.getHexUtils().makeByteArray(command.reply.raw_data));
                tmp_str = tmp_str.trim();
                util.config.addSetting(this.sett_names.get(command.command_code), tmp_str);
                return tmp_str;

            case MinimedCommand.READ_PUMP_STATE:
            case MinimedCommand.READ_PUMP_ERROR_STATUS:
                util.config.addSetting(this.sett_names.get(command.command_code), "" + command.reply.raw_data[0]);
                return command.reply.raw_data[0];

            case MinimedCommand.READ_PROFILES_STD_DATA:
            case MinimedCommand.READ_PROFILES_A_DATA:
            case MinimedCommand.READ_PROFILES_B_DATA:
            case 146:
            case 147:
            case 148:
                return convertProfile(command);

            case MinimedCommand.READ_SENSOR_DEMO_AND_GRAPH_TIMEOUT:
                util.config.addSetting("SENSOR_DEMO_ENABLE", "" + this.parseResultEnable(command.reply.raw_data[0]));
                util.config.addSetting("SENSOR_GRAPH_TIMEOUT", "00:0" + command.reply.raw_data[1]);
                return true;

            case MinimedCommand.READ_REAL_TIME_CLOCK:
                return this.currentTimeStamp(command);

            case MinimedCommand.READ_CARBOHYDRATE_RATIOS:
                return this.carbohydrateRatios(command);

                // case 120:
                // return decodeTempBasal(command, command.reply.raw_data);

            case MinimedCommand.READ_REMAINING_INSULIN:
                return this.remainingInsulin(command);

            case MinimedCommand.READ_BATTERY_STATUS:
                return this.batteryStatus(command);

            case MinimedCommand.READ_SENSOR_ALARM_SILENCE: // = 211;
                return this.sensorAlarmSilence(command);

            case MinimedCommand.READ_SENSOR_PREDICTIVE_ALERTS: // 209;
                return this.sensorPredictiveAlerts(command);

            case MinimedCommand.READ_SENSOR_RATE_OF_CHANGE_ALERTS: // = 212;
                return this.sensorRateOfChangeAlerts(command);

            case MinimedCommand.READ_BG_REMINDER_ENABLE:
            case MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS:
            case MinimedCommand.READ_MISSED_BOLUS_REMINDER_ENABLE:
                util.config.addSetting(this.sett_names.get(command.command_code), ""
                        + getEnableString(command.reply.raw_data[0]));
                return util.config.getSetting(this.sett_names.get(command.command_code));

            case MinimedCommand.READ_SENSOR_SETTINGS: // = 153;
            case 207: // Sensor Settings (523)
                return this.sensorSettings(command);

            case MinimedCommand.READ_MISSED_BOLUS_REMINDERS:
                return this.missedBolusReminder(command);

            case 159:
                return this.bgTargets(command);

            case MinimedCommand.READ_PUMP_MODEL:
                util.config.addSetting("PUMP_MODEL", "" + this.stringWithElementCount(command));
                return util.config.getSetting("PUMP_MODEL");

            case MinimedCommand.READ_CARBOHYDRATE_UNITS:
                util.config.addSetting("CARBOHYDRATE_UNITS", CHUnits(command.reply.raw_data));
                return util.config.getSetting("CARBOHYDRATE_UNITS");

            case MinimedCommand.READ_BG_UNITS:
                util.config.addSetting("BG_UNIT", "" + this.getBGUnitName(command.reply.raw_data[0]));
                return util.config.getSetting("BG_UNIT");

            case MinimedCommand.READ_LANGUAGE:
                util.config.addSetting("LANGUAGE", "" + this.language(command.reply.raw_data[0]));
                return util.config.getSetting("LANGUAGE");

            case MinimedCommand.READ_INSULIN_SENSITIVITES:
                return insulinSensitivities(command);

            default:
                return "Command " + command.command_code + " (" + command.command_description
                        + ") is unknown and could not be decoded.";

        }

    }

    private String language(int lang_id)
    {
        switch (lang_id)
        {
            case 0:
                return "LANG_ENGLISH";

            case 1:
                return "LANG_FRENCH";

            case 2:
                return "LANG_SPANISH";

            case 3:
                return "LANG_DUTCH";

            case 4:
                return "LANG_SWEDISH";

            case 5:
                return "LANG_GERMAN";

            case 6:
                return "LANG_ITALIAN";

            case 8:
                return "LANG_CZECH";

            case 9:
                return "LANG_DANISH";

            case 10:
                return "LANG_FINNISH";

            case 11:
                return "LANG_HUNGARIAN";

            case 12:
                return "LANG_NORVEGIAN";

            case 13:
                return "LANG_POLISH";

            case 14:
                return "LANG_PORTUGESE";

            case 15:
                return "LANG_SLOVENE";

            case 18:
                return "LANG_TURKISH";

            default:
                return "UNKNOWN_LANGUAGE__" + lang_id + "__";
        }
        // getLanguage

    }

    private String CHUnits(int[] rd)
    {
        if (rd[0] == 0)
            return "UNSET";
        else if (rd[0] == 1)
            return "UNIT_GRAMS";
        else if (rd[0] == 2)
            return "UNIT_EXCHANGE";
        else
            return "UNKNOWN_UNIT" + rd[0];

    }

    private boolean bgTargets(MinimedCommand cmd)
    {
        // 01 00 51 65 17 65 90 2A 87 BD 00
        // 1 0 81 101 23 101 144 42 135 189 0
        int[] rd = cmd.reply.raw_data;

        int unit = rd[0];

        String ins_sens = "Time: %s, BG: %s - %s %s";

        for (int i = 1, j = 1; i < rd.length; i += 3, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            if (unit == 2)
            {
                this.config.addSetting(
                    "BG_TARGETS_" + j,
                    String.format(ins_sens, getTimeFrom30MinInterval(rd[i]).getTimeString(), "" + rd[i + 1] / 10.0d, ""
                            + rd[i + 2] / 10.0d, "mmol/L"));
            }
            else
            {
                this.config.addSetting(
                    "BG_TARGETS_" + j,
                    String.format(ins_sens, getTimeFrom30MinInterval(rd[i]).getTimeString(), "" + rd[i + 1], ""
                            + rd[i + 2], "mg/dL"));
            }

        }

        return true;
    }

    /**
     * Decode Insulin Sensitivities
     * 
     * @param cmd
     * @return
     */
    private boolean insulinSensitivities(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;

        int unit = rd[0];
        // int j = 1;

        String ins_sens = "Time: %s, Amount: %s %s";

        for (int i = 1, j = 1; i < rd.length; i += 2, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            if (unit == 2)
            {
                this.config.addSetting("INSULIN_SENSITIVITY_" + j, String.format(ins_sens,
                    getTimeFrom30MinInterval(rd[i]).getTimeString(), "" + rd[i + 1] / 10.0d, "mmol/L / U"));
            }
            else
            {
                this.config.addSetting("INSULIN_SENSITIVITY_" + j, String.format(ins_sens,
                    getTimeFrom30MinInterval(rd[i]).getTimeString(), "" + rd[i + 1], "mg/dL / U"));
            }
        }

        return true;
    }

    private boolean carbohydrateRatios(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;

        // 01 03 00 00 64 1B 00 96 25 00 82 00
        // 1 3 0 0 100 27 0 150 37 0 130 0

        // [01 04 00 00 64 1B 00 96 25 00 82 27 07 08 00 00
        // 1 4 0 0 100 27 0 150 37 0 130 39 7 8 0 0 0 0

        int unit = rd[0];
        String ins_sens = "Time: %s, Amount: %s %s";

        for (int i = 2, j = 1; j <= rd[1]; i += 3, j++)
        {
            if (unit == 2)
            {
                this.config.addSetting(
                    "CARB_RATIO_" + j,
                    String.format(ins_sens, getTimeFrom30MinInterval(rd[i]).getTimeString(),
                        "" + this.hex_util.makeInt(rd[i + 1], rd[i + 2]) / 10.0d, "exch/U"));
            }
            else
            {
                this.config.addSetting(
                    "CARB_RATIO_" + j,
                    String.format(ins_sens, getTimeFrom30MinInterval(rd[i]).getTimeString(),
                        "" + this.hex_util.makeInt(rd[i + 1], rd[i + 2]) / 10.0d, "g/U"));
            }
        }

        return true;
    }

    private String currentTimeStamp(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;

        // log.debug("Result TIME: " + this.hex_util.getHexCompact(rd));
        // log.debug("Result TIME: " + this.hex_util.getIntArrayShow(rd));

        GregorianCalendar gc = null;

        if (this.device.getMinimedDeviceId() == MinimedDevicesIds.PUMP_MINIMED_511)
        {
            // FIXME test on 511
            int year = rd[2];

            if (year > 90 && year < 100)
            {
                year += 1900;
            }

            if (year < 1900)
            {
                year += 2000;
            }

            gc = new GregorianCalendar(year, rd[1] - 1, rd[0], this.hex_util.makeUnsignedShort(rd[3], rd[4]), rd[5],
                    rd[6]);

        }
        else
        {
            gc = new GregorianCalendar(this.hex_util.makeUnsignedShort(rd[3], rd[4]), rd[5] - 1, rd[6], rd[0], rd[1], 0);
        }

        this.config.addSetting("PUMP_TIME", ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, gc));

        return ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, gc);
    }

    private String stringWithElementCount(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;

        // int i = ai[0];
        int arr[] = new int[rd[0]];
        System.arraycopy(rd, 1, arr, 0, arr.length);

        String val = this.hex_util.makeString(arr);
        // m_modelNumber = MedicalDevice.Util.makeString(ai1);

        // int i = ai[0];
        // int ai1[] = new int[i];
        // System.arraycopy(ai, 1, ai1, 0, ai1.length);
        // m_modelNumber = MedicalDevice.Util.makeString(ai1);

        return val;

    }

    private int remainingInsulin(MinimedCommand cmd)
    {
        debugResult(cmd);
        int d = 0;

        if (this.device.getMinimedDeviceId() == MinimedDevicesIds.PUMP_MINIMED_511)
        {
            d = hex_util.makeUnsignedShort(cmd.reply.raw_data[0], cmd.reply.raw_data[1]);
        }
        else
        {
            d = hex_util.makeUnsignedShort(cmd.reply.raw_data[2], cmd.reply.raw_data[3]);
        }

        config.addSetting("PUMP_REMAINING_INSULIN", "" + toBasalInsulin(d));

        return d;
    }

    private String sensorAlarmSilence(MinimedCommand cmd)
    {
        // this.util.decoder.debugResult(cmd);

        int[] rd = cmd.reply.raw_data;
        String res_status = null;
        String res_time = "";

        if (rd[0] == 0)
        {
            res_status = "OFF";
        }
        else if (rd[0] == 1)
        {
            res_status = "HI_ALERTS";
        }
        else if (rd[0] == 2)
        {
            res_status = "LO_ALERTS";
        }
        else if (rd[0] == 4)
        {
            res_status = "HI_LO_ALERTS";
        }
        else if (rd[0] == 8)
        {
            res_status = "ALL_SENSOR_ALERTS";
        }
        else
        {
            res_status = "UNKNOWN_STATUS__" + rd[0] + "__";
        }

        this.config.addSetting("SENSOR_ALERT_SILENCE_TYPE", res_status);

        if (rd[2] != 0)
        {

            int minutes = rd[2];

            minutes += 6;

            int inter = minutes / 30;

            if (inter % 2 == 0)
            {
                res_time = ATDataAccessAbstract.getLeadingZero(inter / 2, 2) + ":00";
            }
            else
            {
                res_time = ATDataAccessAbstract.getLeadingZero((inter - 1) / 2, 2) + ":30";
            }

            this.config.addSetting("SENSOR_ALERT_SILENCE_TIME", res_time);

        }

        return "SENSOR_ALERT_SILENCE" + ": " + res_status + ", " + "TIME: " + res_time;
    }

    public boolean sensorPredictiveAlerts(MinimedCommand cmd)
    {
        if (cmd.reply.raw_data[4] == 0)
        {
            util.config.addSetting("SENSOR_TIME_SENSITIVITY_LOW_PREDICTIVE_ALERT", "OFF");
        }
        else
        {
            util.config.addSetting("SENSOR_TIME_SENSITIVITY_LOW_PREDICTIVE_ALERT", "00:"
                    + (cmd.reply.raw_data[4] - Integer.parseInt("80", 16)));
        }

        if (cmd.reply.raw_data[3] == 0)
        {
            util.config.addSetting("SENSOR_TIME_SENSITIVITY_HIGH_PREDICTIVE_ALERT", "OFF");
        }
        else
        {
            util.config.addSetting("SENSOR_TIME_SENSITIVITY_HIGH_PREDICTIVE_ALERT", "00:"
                    + (cmd.reply.raw_data[4] - Integer.parseInt("80", 16)));
        }

        return true;
    }

    public boolean sensorRateOfChangeAlerts(MinimedCommand cmd)
    {
        // this.util.decoder.debugResult(cmd);

        int[] rd = cmd.reply.raw_data;

        // System.out.println("Set Fall Rate Limit: " +
        // DataAccessPump.Decimal3Format.format(rd[2]/1000.0f) + " mmol/L/min");
        // System.out.println("Set Rise Rate Limit: " +
        // DataAccessPump.Decimal3Format.format(rd[1]/1000.0f) + " mmol/L/min");

        this.config.addSetting("RATE_OF_CHANGE_ALERTS_FALL_RATE_LIMIT", rd[2] == 0 ? "OFF"
                : DataAccessPlugInBase.Decimal3Format.format(rd[2] / 1000.0f) + " mmol/L/min");
        this.config.addSetting("RATE_OF_CHANGE_ALERTS_RISE_RATE_LIMIT", rd[1] == 0 ? "OFF"
                : DataAccessPlugInBase.Decimal3Format.format(rd[1] / 1000.0f) + " mmol/L/min");

        // SET FALL RATE LIMIT = 0.250
        // SET RISE RATE LIMIT = 0.275

        return true;
    }

    private boolean missedBolusReminder(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;

        // 00 1E 08 1E FF FF FF FF FF FF FF FF FF FF FF FF 01 00 0A 00 FF FF FF
        // FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        // 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        // 0 30 8 30 255 255 255 255 255 255 255 255 255 255 255 255 1 0 10 0
        // 255 255 255 255 255 255 255 255 255 255 255 255 0 0 0 0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0

        // 0 start *by 2
        // 16

        String start, end;

        String template = "%s - %s";

        for (int i = 0, j = 1; i < 16; i += 2, j++)
        {
            if (rd[i] == 255)
            {
                break;
            }

            start = ATDataAccessAbstract.getLeadingZero(rd[i], 2) + ":"
                    + ATDataAccessAbstract.getLeadingZero(rd[i + 1], 2);
            end = ATDataAccessAbstract.getLeadingZero(rd[i + 16], 2) + ":"
                    + ATDataAccessAbstract.getLeadingZero(rd[i + 17], 2);

            this.config.addSetting("MISSED_BOLUS_REMINDER_" + j, String.format(template, start, end));

        }

        // return ATDataAccessAbstract.getLeadingZero(h, 2) + ":" +
        // ATDataAccessAbstract.getLeadingZero(m, 2);
        return true;

    }

    private String batteryStatus(MinimedCommand cmd)
    {
        int[] rd = cmd.reply.raw_data;
        int status = rd[0];

        String status_s = "";

        if (status == 0)
        {
            status_s = "NORMAL";
        }
        else if (status == 1)
        {
            status_s = "LOW";
        }
        else
        {
            status_s = "OFF";
        }

        double d = hex_util.makeInt(rd[1], rd[2]) / 100.0d;

        status_s += ", " + "VOLTAGE" + ": " + d + " V (" + "NORMAL_1_5_V" + ")";

        // FIXME check status with 522 and 511
        log.warn("Unknown status: " + rd[0] + " Resolved to: " + status_s);
        log.warn("Full result: " + this.hex_util.getIntArrayShow(rd));

        this.config.addSetting("PUMP_BATTERY_STATUS", status_s);

        return status_s;
    }

    public void loadSettingsNames()
    {
        this.sett_names = new Hashtable<Integer, String>();

        this.sett_names.put(MinimedCommand.READ_PUMP_ID, "MM_SERIAL_NUMBER");
        this.sett_names.put(MinimedCommand.READ_FIRMWARE_VERSION, "MM_FIRMWARE_VERSION");
        this.sett_names.put(MinimedCommand.READ_PUMP_STATE, "MM_PUMP_STATE");
        this.sett_names.put(MinimedCommand.READ_PUMP_ERROR_STATUS, "MM_PUMP_ERROR_STATUS");
        this.sett_names.put(MinimedCommand.READ_TEMPORARY_BASAL, "MM_TEMP_BASAL_");
        this.sett_names.put(MinimedCommand.READ_BG_REMINDER_ENABLE, "MM_BG_REMINDER_ENABLED");
        this.sett_names.put(MinimedCommand.READ_BOLUS_WIZARD_SETUP_STATUS, "MM_BOLUS_WIZARD_ENABLED");
        this.sett_names.put(MinimedCommand.READ_MISSED_BOLUS_REMINDER_ENABLE, "MM_MISSED_BOLUS_REMINDER_ENABLED");

    }

    public void debugResult(MinimedCommand cmd)
    {
        log.debug(cmd.command_code + " [" + cmd.command_description + "]");
        log.debug("[" + util.getHexUtils().getHexCompact(cmd.reply.raw_data));

        log.warn("Int: " + this.hex_util.getIntArrayShow(cmd.reply.raw_data));

    }

    /*
     * private void verifyDeviceValue(int i, int j, int k, String s) throws
     * BadDeviceValueException
     * {
     * if (i < j || i > k)
     * throw new BadDeviceValueException((new
     * StringBuilder()).append("The value of ").append(i).append(" for '")
     * .append(s).append("' is out of range; ").append("must be ").append(j).append
     * (" to ").append(k)
     * .toString());
     * else
     * return;
     * }
     * private void verifyDeviceValue(double d, double d1, double d2, String s)
     * throws BadDeviceValueException
     * {
     * if (d < d1 || d > d2)
     * throw new BadDeviceValueException((new
     * StringBuilder()).append("The value of ").append(d).append(" for '")
     * .append(s).append("' is out of range; ").append("must be ").append(d1).append
     * (" to ").append(d2)
     * .toString());
     * else
     * return;
     * }
     * static void verifyDeviceValue(boolean flag, String s) throws
     * BadDeviceValueException
     * {
     * if (!flag)
     * throw new BadDeviceValueException((new
     * StringBuilder()).append("Condition failed: '").append(s).append("'")
     * .toString());
     * else
     * return;
     * }
     */

    public static boolean parseEnable(int i, String s)
    {
        // verifyDeviceValue(i, 0, 1, s);
        return i == 1;
    }

    public static String getEnableString(int i)
    {
        if (i == 1)
            return "TRUE";
        else
            return "FALSE";
    }

    public long toBasalStrokes(double d)
    {
        return (long) (d * this.util.config.strokes_per_basal_unit);
    }

    public long toBolusStrokes(double d)
    {
        return (long) (d * this.util.config.strokes_per_bolus_unit);
    }

    public double toBasalInsulin(int i)
    {
        return (double) i / (double) this.util.config.strokes_per_basal_unit;
    }

    public double toBolusInsulin(int i)
    {
        // log.debug("to Bolus Insulin: strokes: " +
        // this.util.config.strokes_per_bolus_unit + ", i=" + i + ", tog= " +
        // (double)i / (double)this.util.config.strokes_per_bolus_unit );
        return (double) i / (double) this.util.config.strokes_per_bolus_unit;
    }

    public boolean sensorSettings(MinimedCommand cmd)
    {
        HexUtils hu = this.util.getHexUtils();
        int rd[] = cmd.reply.raw_data;

        if (this.device.getMinimedDeviceId() == MinimedDevicesIds.PUMP_MINIMED_522)
        {
            log.warn("VERIFY 522 DEVICE !!!!!!!!!!!!!!!!!!!!!!!!!");

            // HexUtils hu = this.util.getHexUtils();

            // log.debug("Decoded reply: " +
            // hu.getHexCompact(cmd.reply.raw_data));

            util.config.addSetting("SENSOR_ENABLED", util.decoder.parseResultEnable(cmd.reply.raw_data[0]));

            int bg_units = cmd.reply.raw_data[22];

            util.config.addSetting("SENSOR_BG_UNITS", util.decoder.getBGUnitName(bg_units));
            util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_ENABLE",
                util.decoder.parseResultEnable(cmd.reply.raw_data[1]));

            int i = hu.makeInt(cmd.reply.raw_data[2], cmd.reply.raw_data[3]);

            if (bg_units == 2)
            {
                util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_VALUE", "" + util.decoder.toWholeUnits(i));
            }
            else
            {
                util.config.addSetting("SENSOR_HIGH_GLUCOSE_LIMIT_VALUE", "" + i);
            }

            util.config.addSetting("SENSOR_HIGH_BG_SNOOZE_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[4], cmd.reply.raw_data[5]));
            util.config.addSetting("SENSOR_LOW_BG_LIMIT_ENABLE", util.decoder.parseResultEnable(cmd.reply.raw_data[6]));

            i = hu.makeInt(cmd.reply.raw_data[7], cmd.reply.raw_data[8]);

            if (bg_units == 2)
            {
                util.config.addSetting("SENSOR_LOW_BG_LIMIT_VALUE", "" + util.decoder.toWholeUnits(i));
            }
            else
            {
                util.config.addSetting("SENSOR_LOW_BG_LIMIT_VALUE", "" + i);
            }

            util.config.addSetting("SENSOR_LOW_BG_SNOOZE_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[9], cmd.reply.raw_data[10]));
            util.config.addSetting("SENSOR_ALARM_SNOOZE_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[14], cmd.reply.raw_data[15]));
            util.config.addSetting("SENSOR_CALIBRATION_REMINDER_ENABLE",
                util.decoder.parseResultEnable(cmd.reply.raw_data[16]));
            util.config.addSetting("SENSOR_CALIBRATION_REMINDER_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[17], cmd.reply.raw_data[18]));
            util.config.addSetting("SENSOR_TRANSMITER_ID",
                "" + hu.makeInt(cmd.reply.raw_data[19], cmd.reply.raw_data[20], cmd.reply.raw_data[21]));
            util.config.addSetting("SENSOR_MISSED_DATA_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[23], cmd.reply.raw_data[24]));

            // FIX

            return false;
        }
        else
        {

            // log.debug("Decoded reply: " +
            // hu.getHexCompact(cmd.reply.raw_data));

            this.util.decoder.debugResult(cmd);

            int tmp_int = 0;

            tmp_int = rd[0];

            if (tmp_int >= 128)
            {
                tmp_int -= 128;
                util.config.addSetting("SENSOR_AUTOCALIBRATE", "ON");
            }
            else
            {
                util.config.addSetting("SENSOR_AUTOCALIBRATE", "OFF");
            }

            if (tmp_int == 0)
            {
                util.config.addSetting("SENSOR_ENABLED", "FALSE"); // util.decoder.parseResultEnable(cmd.reply.raw_data[0]));
                return true;
            }
            else if (tmp_int == 1)
            {
                util.config.addSetting("SENSOR_ENABLED", "TRUE"); // util.decoder.parseResultEnable(cmd.reply.raw_data[0]));
            }
            else
            {
                util.config.addSetting("SENSOR_ENABLED", "UNKNOWN" + " " + rd[0]);
            }

            int bg_units = rd[59];

            // old (22), new (59) = diff => 37
            util.config.addSetting("SENSOR_BG_UNITS", util.decoder.getBGUnitName(bg_units));

            if (rd[2] > 0)
            {
                util.config.addSetting("SENSOR_BG_LIMITS_ENABLED", "TRUE");
                util.config.addSetting("SENSOR_BG_LIMITS_COUNT", "" + rd[2]);

                int cnt = rd[2];
                int cnt_curr = 0;

                for (int i = 3; i < 43; i += 5)
                {
                    cnt_curr++;

                    if (cnt_curr > cnt)
                    {
                        break;
                    }

                    int time = rd[i];

                    String low_ll = "";
                    String high_ll = "";

                    low_ll = rd[i + 4] == 0 ? "OFF" : "" + getCorrectBG(bg_units, rd[i + 4]);
                    high_ll = rd[i + 2] == 0 ? "OFF" : "" + getCorrectBG(bg_units, rd[i + 2]);

                    util.config.addSetting("SENSOR_BG_LIMITS_" + cnt_curr, "START_TIME" + ": "
                            + MinimedReplyDecoder.getTimeFrom30MinInterval(time).getTimeString() + " " + low_ll + " - "
                            + high_ll);

                }

            }
            else
            {
                util.config.addSetting("SENSOR_BG_LIMITS_ENABLED", "FALSE");
            }

            int diff = 37;

            util.config.addSetting("SENSOR_HIGH_BG_SNOOZE_TIME", getTimeFromMinutes(rd[44]));
            util.config.addSetting("SENSOR_LOW_BG_SNOOZE_TIME", getTimeFromMinutes(rd[46]));
            util.config.addSetting("SENSOR_CALIBRATION_REPEAT_TIME", getTimeFromMinutes(rd[48]));

            // log.debug("49 : " + rd[49]);
            // log.debug("50 : " + rd[50]); = 100

            // 51 - this might be not correct
            util.config.addSetting("SENSOR_ALARM_SNOOZE_TIME",
                "" + hu.makeInt(cmd.reply.raw_data[diff + 14], cmd.reply.raw_data[diff + 15]));
            // log.debug("51 : " + rd[51]);
            // log.debug("52 : " + rd[52]); 39

            util.config.addSetting("SENSOR_CALIBRATION_REMINDER_ENABLE", util.decoder.parseResultEnable(rd[diff + 16]));
            util.config.addSetting("SENSOR_CALIBRATION_REMINDER_TIME", "" + hu.makeInt(rd[diff + 17], rd[diff + 18]));
            util.config
                    .addSetting("SENSOR_TRANSMITER_ID", "" + hu.makeInt(rd[diff + 19], rd[diff + 20], rd[diff + 21]));
            util.config.addSetting("SENSOR_WEAK_SIGNAL_TIME",
                getTimeFromMinutes(hu.makeInt(cmd.reply.raw_data[diff + 23], cmd.reply.raw_data[diff + 24])));
            util.config.addSetting("SENSOR_LOW_BG_SUSPEND", getCorrectBG(bg_units, hu.makeInt(rd[62], rd[63])));

            return true;

        }

    }

    private String getTimeFromMinutes(int minutes)
    {
        int h = minutes / 60;
        int m = minutes - h * 60;

        return ATDataAccessAbstract.getLeadingZero(h, 2) + ":" + ATDataAccessAbstract.getLeadingZero(m, 2);
    }

    private String getCorrectBG(int bg_unit, int bg_value)
    {
        if (bg_unit == 1)
            return "" + bg_value;
        else
            return "" + bg_value / 10.0d;

    }

    public double toWholeUnits(int i)
    {
        return i / 10D;
    }

    public String convertProfile(MinimedCommand mc)
    {
        if (this.device.getMinimedDeviceId() == MinimedDevicesIds.PUMP_MINIMED_511)
        {
            log.warn("This Profile interpretation might not work for 511. Will try anyway.");
        }

        int rep[] = mc.reply.raw_data;

        StringBuffer sb_display = new StringBuffer();
        StringBuffer sb_coded = new StringBuffer();
        String profile = "";

        sb_display.append("Profile ");

        if (mc.command_code == 147 || mc.command_code == 123)
        {
            profile = "A";
        }
        else if (mc.command_code == 148 || mc.command_code == 124)
        {
            profile = "B";
        }
        else
        {
            profile = "STD";
        }

        sb_display.append(profile);
        sb_display.append("\n");

        int val, time_x;
        double vald;

        for (int i = 0; i < rep.length; i += 3)
        {
            val = this.util.getHexUtils().makeUnsignedShort(rep[i + 1], rep[i]);
            vald = toBasalInsulin(val);

            time_x = rep[i + 2]; // Integer.parseInt("" + rep[i+2], 16);
            // log.debug("Returned time: 0x" + rep[i+2]);
            // log.debug("Returned time(interval): " + time_x);

            // time_x *= 30;

            // log.debug("Returned time(minutes): " + time_x);
            ATechDate atd = getTimeFrom30MinInterval(time_x);

            /*
             * null; //new ATechDate(0, 0, 0, ((int)time_x/60) ,
             * ((int)time_x%60), ATechDate.FORMAT_TIME_ONLY_MIN);
             * if (time_x%2==0)
             * {
             * atd = new ATechDate(0, 0, 0, ((int)time_x/2) , 00,
             * ATechDate.FORMAT_TIME_ONLY_MIN);
             * }
             * else
             * {
             * atd = new ATechDate(0, 0, 0, ((int)(time_x-1)/2) , 30,
             * ATechDate.FORMAT_TIME_ONLY_MIN);
             * }
             */

            if (i != 0)
            {
                if (time_x == 0)
                {
                    break;
                }
                else
                {
                    sb_coded.append(";");
                    sb_display.append("\n");
                }
            }

            // log.debug("ATD (string): " + atd.getTimeString());
            // log.debug("ATD (long): " + atd.getATDateTimeAsLongString());

            sb_coded.append(atd.getATDateTimeAsLongString());
            sb_display.append(atd.getTimeString());

            sb_coded.append("=");
            sb_display.append(" = ");

            sb_coded.append(vald);
            sb_display.append(vald);

        }

        util.config.addSetting("BASAL_PATTERN_PROFILE_" + profile, sb_coded.toString());
        util.config.addSetting("BASAL_PATTERN_PROFILE_" + profile + "_DISPLAY", sb_display.toString());

        log.info("Display profiles: " + sb_display.toString());

        return sb_coded.toString();

    }

    public static ATechDate getTimeFrom30MinInterval(int interval)
    {
        ATechDate atd = null; // new ATechDate(0, 0, 0, ((int)time_x/60) ,
                              // ((int)time_x%60),
                              // ATechDate.FORMAT_TIME_ONLY_MIN);

        if (interval % 2 == 0)
        {
            atd = new ATechDate(0, 0, 0, interval / 2, 00, ATechDate.FORMAT_TIME_ONLY_MIN);
        }
        else
        {
            atd = new ATechDate(0, 0, 0, (interval - 1) / 2, 30, ATechDate.FORMAT_TIME_ONLY_MIN);
        }

        return atd;
    }

    public String getBGUnitName(int unit)
    {
        if (unit == 0)
            return "UNSET";
        else if (unit == 1)
            return "mg/dL";
        else if (unit == 2)
            return "mmol/L";
        else
            return "INVALID_DATA__" + unit + "__";
    }

    // FIXME

    public String parseResultEnable(int i)
    {
        switch (i)
        {
            case 0:
                return "FALSE";
            case 1:
                return "TRUE";
            default:
                return "INVALID_VALUE__" + i + "__";
        }
    }

    /**
     * TBR: Rate
     */
    public static final int TBR_RATE = 0;

    /**
     * TBR: Duration
     */
    public static final int TBR_DURATION = 1;

    private MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();

    /**
     * Get Unsigned Short
     * 
     * @param mc
     * @param parameter
     * @return
     */
    public int getUnsignedShort(MinimedCommand mc)
    {
        return this.getUnsignedShort(mc, 0);
    }

    /**
     * Get Unsigned Short
     * 
     * @param mc
     * @param parameter
     * @return
     */
    public int getUnsignedShort(MinimedCommand mc, int parameter)
    {
        int[] dt = getDataPart(mc, parameter);
        return utils.getHexUtils().convertIntsToUnsignedShort(dt[0], dt[1]);
    }

    /**
     * Get String
     * 
     * @param mc
     * @return
     */
    public String getString(MinimedCommand mc)
    {
        return getString(mc, 0);
    }

    /**
     * Get String
     * 
     * @param mc
     * @param parameter 
     * @return
     */
    public String getString(MinimedCommand mc, int parameter)
    {
        return new String(utils.getHexUtils().convertIntArrayToByteArray(getDataPart(mc, parameter)));
    }

    // public static final int TBR_DURATION = 1;

    private int[] getDataPart(MinimedCommand mc, int parameter)
    {

        switch (mc.command_code)
        {
            case MinimedCommand.READ_FIRMWARE_VERSION:
                return mc.reply.raw_data;

            case 120: // TBR
                if (parameter == MinimedReplyDecoder.TBR_DURATION)
                    return utils.getParamatersArray(2, mc.reply.raw_data[2], mc.reply.raw_data[3]);
                else
                {
                    // int i = MedicalDevice.Util.makeUnsignedShort(ai[0],
                    // ai[1]);
                    // MedicalDevice.Util.verifyDeviceValue(i, 0.0D,
                    // toBasalStrokes(35D), "Temporary Basal Rate");
                    // m_settingTempBasalRate = toBasalInsulin(i);
                    System.out.println("This parameter resolve not supported [command=" + mc.command_code + ",desc="
                            + mc.command_description + ",paremeter=" + parameter);
                }

            case 152:
                if (parameter == MinimedReplyDecoder.TBR_DURATION)
                    return utils.getParamatersArray(2, mc.reply.raw_data[4], mc.reply.raw_data[5]);
                else
                {
                    // int i = MedicalDevice.Util.makeUnsignedShort(ai[2],
                    // ai[3]);
                    // MedicalDevice.Util.verifyDeviceValue(i, 0.0D,
                    // toBasalStrokes(35D), "Temporary Basal Rate");
                    // m_settingTempBasalRate = toBasalInsulin(i);
                    System.out.println("This parameter resolve not supported [command=" + mc.command_code + ",desc="
                            + mc.command_description + ",paremeter=" + parameter);
                }

            default:
                return null;

        }

    }

}
