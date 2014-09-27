package ggc.cgms.device.minimed.file;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.device.impl.minimed.file.MinimedCareLinkData;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     MinimedCareLinkCGMSData  
 *  Description:  Minimed Carelink Export File CGMS data 
 *
 *  NOTE: While this class is implemented. it is not yet integrated and tested. Plan is to support file reading in 0.6 version.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCareLinkCGMSData extends MinimedCareLinkData
{

    private static Log log = LogFactory.getLog(MinimedCareLinkCGMSData.class);
    private boolean post_process = false;
    private MinimedCareLinkCGMS mmclp;
    private String index = "";
    /**
     * 
     */
    public ArrayList<MinimedCareLinkCGMSData> children = null;
    private float ISIG = 0.0f;
    private String additional_value = "";
    private DataAccessCGMS m_da;
    private boolean isig_set = false;
    Hashtable<String, String> items = null;

    /**
     * @param data
     * @param full_data
     * @param mcl_
     */
    public MinimedCareLinkCGMSData(String[] data, String full_data, MinimedCareLink mcl_)
    {
        super(data, full_data, mcl_);
        m_da = (DataAccessCGMS) mcl_.m_da;
        mmclp = (MinimedCareLinkCGMS) mcl_;
        // processData();

    }

    // FIXME
    /**
     * Get Number Of Parameters
     * 
     * @return
     */
    public int getNumberOfParameters()
    {
        /*
         * if ((this.raw_type.equals("Prime")) ||
         * (this.raw_type.equals("AlarmPump")))
         * return 1;
         */
        if (this.raw_type.equals("Rewind"))
            return 0;
        else
            return 1;

    }

    /**
     * @return
     */
    public boolean isDeviceData()
    {
        // FIXME
        /*
         * this.raw_type.equals("Prime")
         * return this.raw_type.equals("Rewind");
         * return this.raw_type.equals("AlarmPump");
         * return this.raw_type.equals("BolusNormal");
         * return this.raw_type.equals("BolusSquare");
         * this.raw_type.equals("BolusWizardBolusEstimate") || // bolus wiyard
         * estimate
         * DONE
         */

        if (this.raw_type.equals("GlucoseSensorData")
                || // glucose sensor data
                this.raw_type.equals("GlucoseSensorDataLow")
                || // data low
                this.raw_type.equals("SensorWeakSignal")
                || // warning - sensor weak signal
                this.raw_type.equals("AlarmSensor")
                || // alarm

                // this.raw_type.equals("SensorSync") || // sensor sync - ignore
                this.raw_type.equals("SensorCalFactor")
                || // unknown
                this.raw_type.equals("SensorCal")
                || // unknown
                this.raw_type.equals("SensorStatus")
                || // status - unknown
                this.raw_type.equals("SensorPacket")
                || // packets from sensor
                this.raw_type.equals("SensorError")
                || // sensor errors

                this.raw_type.equals("BGTherasense")
                || // meter entry

                // this.raw_type.equals("SensorTimestamp") || // timstamp
                // this.raw_type.equals("CalBGForGH") || // meter - ignore
                // this.raw_type.equals("CalBGForPH") || // meter - ignore
                // this.raw_type.equals("ChangeBatteryEnable") || // battery
                // change - ignore
                // this.raw_type.equals("ChangeBatteryEnableGH") || // battery
                // change - ignore

                // config
                this.raw_type.equals("ChangeSensorGlucoseLimitProfile")
                || // config
                this.raw_type.equals("ChangeSensorAlarmSilenceConfig")
                || this.raw_type.equals("ChangeSensorGlucoseLimitPattern")
                ||
                // this.raw_type.equals("ChangeSensorGlucoseLimitPatternSetup")
                // ||
                // this.raw_type.equals("ChangeSensorSetup2") ||
                this.raw_type.equals("ChangeSensorSetupConfig2")
                ||

                // current ?
                // this.raw_type.equals("CurrentSensorGlucoseLimitProfile") ||
                this.raw_type.equals("CurrentSensorHighGlucoseSnoozeTime")
                || this.raw_type.equals("CurrentSensorPredictiveAlertProfile")
                ||
                // this.raw_type.equals("CurrentSensorGlucoseLimitPattern") ||
                this.raw_type.equals("CurrentSensorPredictiveAlertPattern")
                || this.raw_type.equals("CurrentTimeDisplayFormat")
                || this.raw_type.equals("CurrentBeepVolume")
                || this.raw_type.equals("CurrentChildBlockEnable")
                || this.raw_type.equals("CurrentKeypadLockedEnable")
                || this.raw_type.equals("CurrentAlarmClockEnable")
                || this.raw_type.equals("CurrentAlarmNotifyMode")
                ||
                // this.raw_type.equals("CurrentBatteryStatus") ||
                // this.raw_type.equals("CurrentErrorStatus") ||
                // this.raw_type.equals("CurrentPumpStatus") ||
                this.raw_type.equals("CurrentPumpModelNumber")
                || this.raw_type.equals("CurrentDisplayLanguage")
                || this.raw_type.equals("CurrentParadigmLinkEnable")
                ||
                // this.raw_type.equals("CurrentSavedSettingsTime") ||
                // this.raw_type.equals("CurrentHistoryPageNumber") ||
                this.raw_type.equals("CurrentCarbUnits")
                ||
                // this.raw_type.equals("CurrentGlucoseHistoryPageNumber") ||
                this.raw_type.equals("CurrentSensorCalFactor")
                || this.raw_type.equals("CurrentSensorCalReminderEnable")
                || this.raw_type.equals("CurrentSensorEnable") || this.raw_type.equals("CurrentSensorCalReminderTime")
                || this.raw_type.equals("CurrentSensorAlarmSnoozeTime")
                || this.raw_type.equals("CurrentSensorLowGlucoseSnoozeTime")
                || this.raw_type.equals("CurrentSensorMissedDataTime")
                || this.raw_type.equals("CurrentSensorTransmitterID") || this.raw_type.equals("CurrentSensorBGUnits")
                || this.raw_type.equals("CurrentSensorAlarmSilenceConfig")
                || this.raw_type.equals("CurrentSensorGraphConfig")
                || this.raw_type.equals("CurrentSensorRateOfChangeAlertConfig")
                || this.raw_type.equals("CurrentSensorAreaUnderCurveConfig") ||

                // JournalEntryPumpLowBattery

                // ignore
                this.raw_type.equals("CurrentParadigmLinkID") // ignore

        )
            return true;
        else
            return false;

    }

    /**
     * @return
     */
    public boolean isProfileData()
    {
        return this.raw_type.equals("ChangeBasalProfilePatternPre") || this.raw_type.equals("ChangeBasalProfilePre")
                || this.raw_type.equals("ChangeBasalProfilePattern") || this.raw_type.equals("ChangeBasalProfile");
        // return false;
    }

    /**
     * @return
     */
    public boolean isConfigData()
    {
        return this.mmclp.getCGMSConfigDefinitions().containsKey(this.raw_type);
    }

    // FIXME
    /**
     * @return
     */
    public boolean isDebuged()
    {
        return false;
        // return this.raw_type.startsWith("SensorTimestamp");
        // return this.raw_type.equals("CurrentRFEnable");

        // return this.raw_type.equals("CurrentAlarmNotifyMode");
        // return false;
        /*
         * ||
         * this.raw_type.equals("ChangeBasalProfilePre") ||
         * this.raw_type.equals("ChangeBasalProfilePattern"));// || // basal
         * profile pattern
         */

        // return this.raw_type.equals("ChangeActiveBasalProfilePattern");
        // return this.raw_type.equals("ChangeTempBasalPercent");
        // return this.raw_type.equals("BGReceived");
        // return this.raw_type.equals("BolusSquare");
        // return this.raw_type.equals("BolusNormal");

        // return this.raw_type.equals("Prime");
        // return this.raw_type.equals("Rewind");
        // return this.raw_type.equals("AlarmPump");

        /*
         * // pump
         * if ( this.raw_type.equals("ClearAlarm") || // clear alarm
         * this.raw_type.equals("ChangeSuspendEnable") || // suspend
         * return true;
         * else
         * return false;
         */
    }

    /**
     * 
     */
    public void writeData()
    {
        processData();
    }

    @Override
    public void processData()
    {
        String v = this.getProcessedValue();

        if (v == null)
        {
            this.data_processed = false;

            if (this.isConfigData())
            {
                dt_long = this.mcl.mm_date.getAtechDateLong(date, time);
            }

        }
        else
        {
            if (this.getNumberOfParameters() == 0 || this.getNumberOfParameters() == 1)
            {
                if (this.isig_set)
                {
                    v = String.format("VALUE=%s;ISIG=%s", v, m_da.getDecimalHandler().getDecimalAsString(ISIG, 2));
                }

                this.mcl.dvw.writeObject(this.raw_type, this.mcl.mm_date.getAtechDate(date, time), v);
            }
            /*
             * else if (this.getNumberOfParameters()==2)
             * {
             * System.out.println("BAD 2");
             * }
             */
            else
            {
                System.out.println("BAD More");
            }

            this.data_processed = true;
        }

        postProcess();

    }

    /**
     * 
     */
    public void postProcess()
    {
        if (this.post_process)
        {
            this.data_processed = true;
        }

    }

    /**
     * @return
     */
    public String getKey()
    {
        if (this.raw_type.equals("CurrentParadigmLinkID"))
            return this.raw_type + index;
        else
            return this.raw_type;

        // System.out.println("Debug count left: " + debug);

    }

    /**
     * @return
     */
    public String getProcessedValue()
    {
        String s;

        if (this.raw_type.equals("GlucoseSensorData") || this.raw_type.equals("GlucoseSensorDataLow"))
        {
            // 5,,GlucoseSensorDataLow,"AMOUNT=30, ISIG=3.5",848871970
            this.processed_value = getDataNumber(this.raw_values, "AMOUNT=", "ISIG=");

            s = this.getDataAfter(this.raw_values, "ISIG=");
            s = s.substring(0, s.length() - 1);

            setISIG(s);

            return this.processed_value;
        }
        else if (this.raw_type.equals("AlarmSensor"))
        {
            s = getDataBetween(this.raw_values, "ALARM_TYPE=", "AMOUNT=");
            this.additional_value = getDataBetween(this.raw_values, "AMOUNT=", "ACTION_REQUESTOR=");

            // "ALARM_TYPE=102, AMOUNT=72, ACTION_REQUESTOR=sensor"

            if (this.mcl.alarm_mappings.containsKey(s))
                return String.format("SUB_TYPE=%s;VALUE=%s", this.mcl.alarm_mappings.get(s), this.additional_value);
            else
            {
                String a = "Unsupported alarm mapping: " + s + "\n" + this.full_data_line;

                this.mcl.output_writer.addErrorMessage(a);
                log.error(a);
                return null;
            }

            // System.out.println("ALARM: " + s);
            // RAW_TYPE=4, RAW_MODULE=18,

        }
        else if (this.raw_type.equals("BGTherasense"))
            return getDataNumber(this.raw_values, "AMOUNT=", "TEMPERATURE_STATUS=");
        else if (this.raw_type.equals("SensorWeakSignal"))
            return "";
        else if (this.raw_type.equals("SensorCal"))
        {
            this.processed_value = getDataBetween(this.raw_values, "CAL_TYPE=", "ISIG=");
            this.raw_type += "_" + this.processed_value;

            // this.additional_value = getDataBetween(this.raw_values,
            // "AMOUNT=", "ACTION_REQUESTOR=");
            // SensorCal,"CAL_TYPE=meter_bg_now, ISIG=7.03"

            s = this.getDataAfter(this.raw_values, "ISIG=");
            s = s.substring(0, s.length() - 1);

            setISIG(s);

            return "";
        }
        else if (this.raw_type.equals("SensorPacket"))
        {
            this.processed_value = getDataBetween(this.raw_values, "PACKET_TYPE=", "ISIG=");
            this.raw_type += "_" + this.processed_value;

            // this.additional_value = getDataBetween(this.raw_values,
            // "AMOUNT=", "ACTION_REQUESTOR=");
            // SensorCal,"CAL_TYPE=meter_bg_now, ISIG=7.03"

            s = this.getDataAfter(this.raw_values, "ISIG=");
            s = s.substring(0, s.length() - 1);
            setISIG(s);

            return "";
        }
        else if (this.raw_type.equals("SensorStatus"))
        {
            this.processed_value = this.getDataAfter(this.raw_values, "STATUS_TYPE=");
            this.raw_type += "_" + this.processed_value;

            return "";
        }
        else if (this.raw_type.equals("SensorCalFactor"))
        {
            this.processed_value = this.getDataAfter(this.raw_values, "CAL_FACTOR=");
            // this.raw_type += "_" + this.processed_value;

            return this.processed_value;
        }
        else if (this.raw_type.equals("SensorError"))
        {
            // SensorError,"ERROR_TYPE=end, ISIG=null"
            this.processed_value = getDataBetween(this.raw_values, "ERROR_TYPE=", "ISIG=");
            // this.raw_type += "_" + this.processed_value;

            if (this.mcl.error_mappings.containsKey(this.processed_value))
                return "" + this.mcl.error_mappings.get(this.processed_value); // ,
                                                                               // this.additional_value);
            else
            {
                String a = "Unsupported error mapping: " + this.processed_value + "\n" + this.full_data_line;

                this.mcl.output_writer.addErrorMessage(a);
                log.error(a);
                return null;
            }

        }
        else if (this.raw_type.equals("CurrentTimeDisplayFormat"))
        {
            this.processed_value = getDataAfter(this.raw_values, "FORMAT=");
            return null;
        }
        else if (this.raw_type.equals("CurrentParadigmLinkEnable") || this.raw_type.equals("CurrentChildBlockEnable")
                || this.raw_type.equals("CurrentKeypadLockedEnable") || this.raw_type.equals("CurrentAlarmClockEnable")
                || this.raw_type.equals("CurrentSensorCalReminderEnable")
                || this.raw_type.equals("CurrentSensorEnable"))
        {
            this.processed_value = getDataAfter(this.raw_values, "ENABLE=");
            return null;
        }
        else if (this.raw_type.equals("CurrentDisplayLanguage"))
        {
            this.processed_value = getDataAfter(this.raw_values, "LANGUAGE=");
            return null;
        }
        else if (this.raw_type.equals("CurrentParadigmLinkID"))
        {
            this.index = getDataNumber(this.raw_values, "PROGRAMMER_NUM=", "PARADIGM_LINK_ID=");
            this.processed_value = getDataAfter(this.raw_values, "PARADIGM_LINK_ID=");
            this.processed_value = this.processed_value.substring(0, this.processed_value.length() - 1);

            // System.out.println(this.processed_value);

            return null;
        }
        else if (this.raw_type.equals("CurrentSensorCalReminderTime")
                || this.raw_type.equals("CurrentSensorAlarmSnoozeTime")
                || this.raw_type.equals("CurrentSensorHighGlucoseSnoozeTime")
                || this.raw_type.equals("CurrentSensorLowGlucoseSnoozeTime")
                || this.raw_type.equals("CurrentSensorMissedDataTime"))
        {
            s = getDataAfter(this.raw_values, "TIME=");

            this.processed_value = getHMDuration(s);

            return null;
        }
        else if (this.raw_type.equals("ChangeSensorGlucoseLimitPattern")
                || this.raw_type.equals("CurrentSensorPredictiveAlertPattern"))
        {
            // this.processed_value = getDataBetween(this.raw_values,
            // "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
            // this.processed_value = this.processed_value.substring(0,
            // this.processed_value.length()-1);

            this.processed_value = getDataBetween(this.raw_values, "ENABLE=", "NUM_PROFILES=");

            // System.out.println("Missing" );
            // ,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954

            // System.out.println(this.raw_type + "=" + this.raw_values);

            return null;
        }
        else if (this.raw_type.equals("ChangeSensorGlucoseLimitProfile")
                || this.raw_type.equals("CurrentSensorPredictiveAlertProfile")
                || this.raw_type.equals("ChangeSensorSetupConfig2"))
        {
            // this.processed_value = getDataBetween(this.raw_values,
            // "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
            // this.processed_value = this.processed_value.substring(0,
            // this.processed_value.length()-1);
            // System.out.println("Missing" );
            // System.out.println(this.raw_type + "=" + this.raw_values);

            String[] vals = this.raw_values.split(", ");

            items = new Hashtable<String, String>();

            String k, v = null;
            for (int i = 1; i < vals.length; i++)
            {
                k = vals[i].substring(0, vals[i].indexOf("="));
                v = vals[i].substring(vals[i].indexOf("=") + 1);

                if (k.contains("TIME"))
                {
                    v = v.substring(0, v.length() - 1);
                    v = this.getHMDuration(v);
                }

                items.put(k, v);
            }

            // System.out.println(" It: " + items);

            // System.out.println(this.processed_value);
            // ChangeSensorGlucoseLimitProfile="PATTERN_DATUM=848868440, PROFILE_INDEX=3, LOW_LIMIT_ENABLE=true, LOW_LIMIT=81.072, HIGH_LIMIT_ENABLE=true, HIGH_LIMIT=144.128, START_TIME=59400000"
            return null;
        }
        /*
         * else if (this.raw_type.equals("CurrentSensorPredictiveAlertPattern"))
         * {
         * //this.processed_value = getDataBetween(this.raw_values,
         * "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * System.out.println(this.raw_type + "=" + this.raw_values);
         * System.out.println("Missing" );
         * //,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954
         * return null;
         * }
         */
        /*
         * else if (this.raw_type.equals("CurrentSensorPredictiveAlertProfile"))
         * {
         * // this.processed_value = getDataBetween(this.raw_values,
         * "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * System.out.println("Missing" );
         * System.out.println(this.raw_type + "=" + this.raw_values);
         * //System.out.println(this.processed_value);
         * // CurrentBGTargetRange,
         * "PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0"
         * ,861682955,2232381,94,Paradigm 522
         * return null;
         * }
         */

        // FIXME

        /*
         * 297,CurrentBeepVolume,LEVEL=high,815418777,2212653,1,Guardian
         * REAL-Time
         * 301,CurrentAlarmNotifyMode,MODE=audio,815418781,2212653,5,Guardian
         * REAL-Time
         * 302,CurrentBatteryStatus,"STATUS=normal, VOLTAGE=147",815418782,2212653
         * ,6,Guardian REAL-Time
         * 303,CurrentErrorStatus,RAW_STATUS=0,815418783,2212653,7,Guardian
         * REAL-Time
         * 304,CurrentPumpStatus,STATUS=normal,815418784,2212653,8,Guardian
         * REAL-Time
         * 305,CurrentPumpModelNumber,MODEL_NUMBER=7100,815418785,2212653,9,Guardian
         * REAL-Time
         * 306,CurrentDisplayLanguage,LANGUAGE=hungarian,815418786,2212653,10,
         * Guardian REAL-Time
         * 311,CurrentSavedSettingsTime,SAVE_TIME=1200794775000,815418791,2212653
         * ,15,Guardian REAL-Time
         * 312,CurrentHistoryPageNumber,PAGE_NUMBER=84,815418792,2212653,16,Guardian
         * REAL-Time
         * 313,CurrentCarbUnits,UNITS=grams,815418793,2212653,17,Guardian
         * REAL-Time
         * 314,CurrentGlucoseHistoryPageNumber,PAGE_NUMBER=84,815418794,2212653,18
         * ,Guardian REAL-Time
         * ,,,,,,,,,,,CurrentSensorCalFactor,CAL_FACTOR=10.41,815418795,2212653,18
         * ,Guardian REAL-Time
         * 323,CurrentSensorTransmitterID,TRANSMITTER_ID=2061978,815418803,2212653
         * ,26,Guardian REAL-Time
         * 324,CurrentSensorBGUnits,UNITS=mmol l,815418804,2212653,27,Guardian
         * REAL-Time
         * 325,CurrentSensorAlarmSilenceConfig,"DURATION=0, SCOPE=all",815418805,
         * 2212653,28,Guardian REAL-Time
         * 327,CurrentSensorRateOfChangeAlertConfig,
         * "LIMIT_UNITS=mmol_l_minute, RISING_LIMIT_ENABLE=false, RISING_LIMIT=1.171, FALLING_LIMIT_ENABLE=false, FALLING_LIMIT=1.171"
         * ,815418807,2212653,30,Guardian REAL-Time
         * 328,CurrentSensorAreaUnderCurveConfig,
         * "HIGH_AREA_UNDER_CURVE=144.128, LOW_AREA_UNDER_CURVE=75.667"
         * ,815418808,2212653,31,Guardian REAL-Time
         * 330,CurrentSensorPredictiveAlertProfile,
         * "PATTERN_DATUM=815418809, PROFILE_INDEX=0, LOW_ENABLE=true, LOW_DURATION=1800000, HIGH_ENABLE=true, HIGH_DURATION=1800000, START_TIME=0"
         * ,815418810,2212653,33,Guardian REAL-Time
         * 326,CurrentSensorGraphConfig,"ENABLE=false, DURATION=0",815418806,2212653
         * ,29,Guardian REAL-Time
         * 329,CurrentSensorPredictiveAlertPattern,"ENABLE=true, NUM_PROFILES=1",
         * 815418809,2212653,32,Guardian REAL-Time
         * 331,CurrentSensorGlucoseLimitPattern,"ENABLE=true, NUM_PROFILES=4",
         * 815418811,2212653,34,Guardian REAL-Time
         * 332,CurrentSensorGlucoseLimitProfile,
         * "PATTERN_DATUM=815418811, PROFILE_INDEX=0, LOW_LIMIT_ENABLE=true, LOW_LIMIT=90.08, HIGH_LIMIT_ENABLE=true, HIGH_LIMIT=162.144, START_TIME=0"
         * ,815418812,2212653,35,Guardian REAL-Time
         * 333,CurrentSensorGlucoseLimitProfile,
         * "PATTERN_DATUM=815418811, PROFILE_INDEX=1, LOW_LIMIT_ENABLE=true, LOW_LIMIT=90.08, HIGH_LIMIT_ENABLE=true, HIGH_LIMIT=180.16, START_TIME=28800000"
         * ,815418813,2212653,36,Guardian REAL-Time
         * 334,CurrentSensorGlucoseLimitProfile,
         * "PATTERN_DATUM=815418811, PROFILE_INDEX=2, LOW_LIMIT_ENABLE=true, LOW_LIMIT=90.08, HIGH_LIMIT_ENABLE=true, HIGH_LIMIT=162.144, START_TIME=37800000"
         * ,815418814,2212653,37,Guardian REAL-Time
         * 335,CurrentSensorGlucoseLimitProfile,
         * "PATTERN_DATUM=815418811, PROFILE_INDEX=3, LOW_LIMIT_ENABLE=true, LOW_LIMIT=81.072, HIGH_LIMIT_ENABLE=true, HIGH_LIMIT=144.128, START_TIME=59400000"
         * ,815418815,2212653,38,Guardian REAL-Time
         */

        // SensorCalFactor,CAL_FACTOR=8.182

        // SensorStatus,STATUS_TYPE=on

        // 00,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,SensorPacket,"PACKET_TYPE=init,
        // ISIG=0

        // ensorPacket,"PACKET_TYPE=init, ISIG=0"

        /*
         * else if (this.raw_type.equals("SensorCal"))
         * {
         * s = getDataBetween(this.raw_values, "ALARM_TYPE=", "AMOUNT=");
         * this.additional_value = getDataBetween(this.raw_values, "AMOUNT=",
         * "ACTION_REQUESTOR=");
         * // "ALARM_TYPE=102, AMOUNT=72, ACTION_REQUESTOR=sensor"
         * //System.out.println("ALARM: " + s);
         * //RAW_TYPE=4, RAW_MODULE=18,
         * return null;
         * }
         */

        else
            return null;

        /*
         * if ((this.raw_type.equals("Prime")) ||
         * (this.raw_type.equals("ResultDailyTotal"))
         * )
         * {
         * s = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
         * return s;
         * }
         * else if ((this.raw_type.equals("Rewind")) ||
         * (this.raw_type.equals("SelfTest")) ||
         * (this.raw_type.equals("JournalEntryPumpLowBattery"))
         * )
         * {
         * return "";
         * }
         * else if (this.raw_type.equals("BolusNormal"))
         * {
         * s = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
         * if (this.raw_values.contains("IS_DUAL_COMPONENT=true"))
         * {
         * post_process = true;
         * this.mcl.temp_data = this;
         * return null;
         * }
         * return s;
         * }
         * else if (this.raw_type.equals("BolusSquare"))
         * {
         * MinimedCareLinkData mcd_temp = this.mcl.temp_data;
         * String s1, s2, s3;
         * s1 = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
         * s2 = getDataDuration(this.raw_values, "DURATION=",
         * "IS_DUAL_COMPONENT=");
         * if (mcd_temp!=null)
         * {
         * this.raw_type = "BolusMultiwave";
         * this.date = mcd_temp.date;
         * this.time = mcd_temp.time;
         * s3 = getDataNumber(mcd_temp.raw_values, "AMOUNT=", "CONCENTRATION=");
         * s = "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s";
         * s = String.format(s, s3, s1, s2);
         * this.mcl.temp_data = null;
         * return s;
         * }
         * else
         * {
         * s = "AMOUNT_SQUARE=%s;DURATION=%s";
         * s = String.format(s, s1, s2);
         * return s;
         * }
         * }
         * else if (this.raw_type.equals("BolusWizardBolusEstimate"))
         * {
         * String[] data = new String[11];
         * data[0] = getDataNumber(this.raw_values, "BG_INPUT=", "BG_UNITS=");
         * data[1] = getDataNumber(this.raw_values, "CARB_INPUT=",
         * "CARB_UNITS=");
         * data[2] = getDataNumber(this.raw_values, "CARB_UNITS=",
         * "CARB_RATIO=");
         * data[3] = getDataNumber(this.raw_values, "CARB_RATIO=",
         * "INSULIN_SENSITIVITY=");
         * data[4] = getDataNumber(this.raw_values, "INSULIN_SENSITIVITY=",
         * "BG_TARGET_LOW=");
         * data[5] = getDataNumber(this.raw_values, "BG_TARGET_LOW=",
         * "BG_TARGET_HIGH=");
         * data[6] = getDataNumber(this.raw_values, "BG_TARGET_HIGH=",
         * "BOLUS_ESTIMATE=");
         * data[7] = getDataNumber(this.raw_values, "BOLUS_ESTIMATE=",
         * "CORRECTION_ESTIMATE=");
         * data[8] = getDataNumber(this.raw_values, "CORRECTION_ESTIMATE=",
         * "FOOD_ESTIMATE=");
         * data[9] = getDataNumber(this.raw_values, "FOOD_ESTIMATE=",
         * "UNABSORBED_INSULIN_TOTAL=");
         * data[10] = getDataNumber(this.raw_values,
         * "UNABSORBED_INSULIN_TOTAL=", "UNABSORBED_INSULIN_COUNT=");
         * // FIXME
         * // CH
         * //return
         * String.format(DataAccessPump.getInstance().getPumpEventTypes(
         * ).getValueTemplateForType(PumpEvents.PUMP_EVENT_BOLUS_WIZARD),
         * // data[0], data[1], data[2], data[3], data[4], data[5], data[6],
         * data[7], data[8], data[9], data[10]);
         * return null;
         * //BG_INPUT=109, 898, BG_UNITS=mmol l, CARB_INPUT=74,
         * CARB_UNITS=grams,
         * // CARB_RATIO=15, INSULIN_SENSITIVITY=54, 048, BG_TARGET_LOW=99, 088,
         * // BG_TARGET_HIGH=108, 096, BOLUS_ESTIMATE=4, 9,
         * CORRECTION_ESTIMATE=0,
         * // FOOD_ESTIMATE=4, 9, UNABSORBED_INSULIN_TOTAL=0, 2,
         * UNABSORBED_INSULIN_COUNT=3,
         * }
         * else if ((this.raw_type.equals("BGReceived")) ||
         * (this.raw_type.equals("JournalEntryPumpLowReservoir")))
         * {
         * return getDataNumber(this.raw_values, "AMOUNT=",
         * "ACTION_REQUESTOR=");
         * }
         * else if (this.raw_type.equals("ChangeTempBasalPercent"))
         * {
         * String s1 = getDataNumber(this.raw_values, "PERCENT_OF_RATE=",
         * "DURATION=");
         * s = getDataDuration(this.raw_values, "DURATION=",
         * "ACTION_REQUESTOR=");
         * if (s==null)
         * {
         * this.raw_type = "TBROver";
         * return "";
         * }
         * else
         * return String.format("DURATION=%s;VALUE=%s", s, s1) + "%";
         * // PERCENT_OF_RATE=0, DURATION=0,
         * //PERCENT_OF_RATE=85, DURATION=39600000, ACTION_REQUESTOR=pump
         * }
         * else if (this.raw_type.equals("ChangeActiveBasalProfilePattern"))
         * {
         * //System.out.println(this.raw_values);
         * s = this.getDataBetween(this.raw_values, "PATTERN_NAME=",
         * "OLD_PATTERN_NAME=");
         * if (s.toLowerCase().equals("standard"))
         * return this.mmclp.profile_names[0];
         * else if (s.toLowerCase().equals("pattern a"))
         * return this.mmclp.profile_names[1];
         * else if (s.toLowerCase().equals("pattern b"))
         * return this.mmclp.profile_names[2];
         * else
         * {
         * String a = "Unsupported profile pattern change: " + s + "\n" +
         * this.full_data_line;
         * this.mcl.output_writer.addErrorMessage(a);
         * log.error(a);
         * return null;
         * }
         * // PATTERN_NAME=pattern b
         * // PATTERN_NAME=standard
         * // ChangeBasalProfilePattern,"PATTERN_NAME=pattern a, NUM_PROFILES=20
         * }
         * else if (this.raw_type.equals("ChangeSuspendEnable"))
         * {
         * s = this.getDataBetween(this.raw_values, "ENABLE=",
         * "ACTION_REQUESTOR=");
         * if (s.equals("false"))
         * {
         * this.raw_type = "ChangeSuspendEnableNot";
         * }
         * return "";
         * }
         * else if (this.raw_type.equals("ChangeTime"))
         * {
         * /*
         * s = getDataNumber(this.raw_values, "NEW_TIME=", "ACTION_REQUESTOR=");
         * long x = Long.parseLong(s);
         * Date gc = new Date();
         * gc.setTime(x);
         * /*
         * GregorianCalendar gc = new GregorianCalendar(x );
         * gc.setTimeInMillis(x);
         * gc.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
         */

        // problem with timezones
        // System.out.println( gc.get(Calendar.DAY_OF_MONTH) + "/" +
        // (gc.get(Calendar.MONTH)+1) + "/" + gc.get(Calendar.YEAR) + "  " +
        // gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE) + ":" +
        // gc.get(Calendar.SECOND));
        // System.out.println( /*gc.get(Calendar.DAY_OF_MONTH) + "/" +
        // (gc.get(Calendar.MONTH)+1) + "/" + gc.get(Calendar.YEAR) + "  " +*/
        // gc.getHours() + ":" + gc.getMinutes() + ":" + gc.getSeconds());

        // 08.6.14 11:14:00
        // 1213442040 000
        /*
         * return getDataNumber(this.raw_values, "NEW_TIME=",
         * "ACTION_REQUESTOR=");
         * }
         * else if (this.raw_type.equals("AlarmPump"))
         * {
         * s = getDataBetween(this.raw_values, "RAW_TYPE=", "RAW_MODULE=");
         * if (this.mcl.alarm_mappings.containsKey(s))
         * return "" + this.mcl.alarm_mappings.get(s);
         * else
         * {
         * String a = "Unsupported alarm mapping: " + s + "\n" +
         * this.full_data_line;
         * this.mcl.output_writer.addErrorMessage(a);
         * log.error(a);
         * return null;
         * }
         * //System.out.println("ALARM: " + s);
         * //RAW_TYPE=4, RAW_MODULE=18,
         * }
         * else if (this.raw_type.equals("CurrentAlarmNotifyMode"))
         * {
         * this.processed_value = getDataAfter(this.raw_values, "MODE=");
         * return null;
         * }
         * else if ((this.raw_type.equals("CurrentBolusReminderEnable")) ||
         * (this.raw_type.equals("CurrentParadigmLinkEnable")) ||
         * (this.raw_type.equals("CurrentBGReminderEnable")) ||
         * (this.raw_type.equals("CurrentBolusWizardEnable")) ||
         * (this.raw_type.equals("CurrentRFEnable")) ||
         * (this.raw_type.equals("CurrentKeypadLockedEnable")) ||
         * (this.raw_type.equals("CurrentVariableBolusEnable")) ||
         * (this.raw_type.equals("CurrentVariableBasalProfilePatternEnable")) ||
         * (this.raw_type.equals("CurrentBolusWizardEnable")) ||
         * (this.raw_type.equals("CurrentBolusWizardEnable")) ||
         * (this.raw_type.equals("CurrentAlarmClockEnable"))
         * )
         * {
         * this.processed_value = getDataAfter(this.raw_values, "ENABLE=");
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentDisplayLanguage"))
         * {
         * this.processed_value = getDataAfter(this.raw_values, "LANGUAGE=");
         * return null;
         * }
         * else if
         * ((this.raw_type.equals("CurrentReservoirWarningValueInsulin")) ||
         * (this.raw_type.equals("CurrentInsulinActionCurve")) ||
         * (this.raw_type.equals("CurrentMaxBolus")) ||
         * (this.raw_type.equals("CurrentInsulinActionCurve")) ||
         * (this.raw_type.equals("CurrentInsulinActionCurve")) ||
         * (this.raw_type.equals("CurrentInsulinActionCurve")) ||
         * (this.raw_type.equals("CurrentInsulinActionCurve"))
         * )
         * {
         * this.processed_value = getDataAfter(this.raw_values, "AMOUNT=");
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentMaxBasal"))
         * {
         * this.processed_value = getDataAfter(this.raw_values, "RATE=");
         * //System.out.println(this.processed_value);
         * return null;
         * }
         * else if ((this.raw_type.equals("CurrentBolusWizardCarbUnits")) ||
         * (this.raw_type.equals("CurrentBolusWizardBGUnits")) ||
         * (this.raw_type.equals("CurrentReservoirWarningUnits"))
         * )
         * {
         * this.processed_value = getDataAfter(this.raw_values, "UNITS=");
         * this.processed_value = this.processed_value.replace(' ', '/');
         * return null;
         * }
         * else if ((this.raw_type.equals("CurrentTimeDisplayFormat")) ||
         * (this.raw_type.equals("CurrentTimeDisplayFormat")) ||
         * (this.raw_type.equals("CurrentTimeDisplayFormat"))
         * )
         * {
         * this.processed_value = getDataAfter(this.raw_values, "FORMAT=");
         * //this.processed_value = this.processed_value.replace(' ', '/');
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentTempBasalType"))
         * {
         * this.processed_value = getDataAfter(this.raw_values,
         * "TEMP_BASAL_TYPE=");
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentPumpModelNumber"))
         * {
         * this.processed_value = getDataAfter(this.raw_values,
         * "MODEL_NUMBER=");
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentAutoOffDuration"))
         * {
         * this.processed_value = getDataAfter(this.raw_values, "DURATION=");
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentAudioBolusStep"))
         * {
         * //this.index = getDataNumber(this.raw_values, "PROGRAMMER_NUM=",
         * "REMOTE_CONTROL_ID=");
         * this.processed_value = this.raw_values.substring(1,
         * this.raw_values.length()-2);
         * this.processed_value = this.processed_value.replace(", ", ";");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * //System.out.println(this.processed_value);
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentBolusWizardSetupStatus"))
         * {
         * this.processed_value = getDataBetween(this.raw_values,
         * "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * //System.out.println(this.processed_value);
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentBGTargetRangePattern"))
         * {
         * //this.processed_value = getDataBetween(this.raw_values,
         * "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * System.out.println("Missing" );
         * //,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954
         * return null;
         * }
         * else if (this.raw_type.equals("CurrentBGTargetRange"))
         * {
         * // this.processed_value = getDataBetween(this.raw_values,
         * "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
         * //this.processed_value = this.processed_value.substring(0,
         * this.processed_value.length()-1);
         * System.out.println("Missing" );
         * //System.out.println(this.processed_value);
         * // CurrentBGTargetRange,
         * "PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0"
         * ,861682955,2232381,94,Paradigm 522
         * return null;
         * }
         * //
         * CurrentBGTargetRangePattern,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954
         * ,2232381,93,Paradigm 522
         * // CurrentBGTargetRange,
         * "PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0"
         * ,861682955,2232381,94,Paradigm 522
         * //CurrentBolusWizardSetupStatus,
         * "IS_SETUP_COMPLETE=true, IS_BOLUS_WIZARD_ENABLED=true, IS_CARB_UNITS_COMPLETE=true, IS_CARB_RATIOS_COMPLETE=true, IS_BG_UNITS_COMPLETE=true, IS_INSULIN_SENSITIVITIES_COMPLETE=true, IS_BG_TARGETS_COMPLETE=true"
         * //CurrentAudioBolusStep,"AMOUNT=1, ENABLE=true"
         * //CurrentAutoOffDuration,DURATION=0
         * // FIXME
         * else
         * return null;
         */

    }

    /*
     * PumpValuesEntryInterface pve_object;
     * public PumpValuesEntryInterface getPumpValuesEntryObject()
     * {
     * return pve_object;
     * }
     */

    private void setISIG(String isig_)
    {
        try
        {
            this.ISIG = Float.parseFloat(isig_.replace(',', '.'));
            this.isig_set = true;
        }
        catch (Exception ex)
        {
            this.ISIG = 0.0f;
            this.isig_set = false;
        }
    }

    /**
     * @return
     */
    public Hashtable<String, String> getItems()
    {
        return this.items;
    }

    /**
     * @return
     */
    public boolean hasItems()
    {
        return this.items != null;
    }

}
