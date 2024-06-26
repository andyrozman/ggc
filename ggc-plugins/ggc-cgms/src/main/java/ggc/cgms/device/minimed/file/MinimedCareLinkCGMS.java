package ggc.cgms.device.minimed.file;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import ggc.cgms.data.CGMSTempValues;
import ggc.cgms.data.defs.*;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: CGMS Tool (support for Pump devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: MinimedCareLinkCGMS
 * Description: Minimed Carelink Export File Handler
 *
 * NOTE: While this class is implemented. it is not yet integrated and tested. Plan is to support file reading in 0.6
 * version.
 *
 *
 * Author: Andy {andy@atech-software.com}
 */

public class MinimedCareLinkCGMS extends MinimedCareLink
{

    DataAccessCGMS m_dap;
    // public String[] profile_names = null;
    // ArrayList<MinimedCareLinkCGMSData> profiles;

    // .x private Hashtable<String,String> defs_cgms = null;
    // public Hashtable<String,String> defs_profile = null;
    private Hashtable<String, String> defs_cgms_config = null;

    private Hashtable<String, MinimedCareLinkCGMSData> config = null;


    /**
     * @param da
     * @param ow
     * @param reading_type
     */
    public MinimedCareLinkCGMS(DataAccessPlugInBase da, OutputWriter ow, int reading_type)
    {
        super(da, ow, reading_type);
        createDeviceValuesWriter();
        m_dap = (DataAccessCGMS) da;
        // setMappingData();
        setDefinitions();
    }


    @Override
    public void setMappingData()
    {
        // FIXME
        // new PumpAlarms();

        // alarms
        this.alarm_mappings.put("101", CGMSAlarms.HighGlucose);
        this.alarm_mappings.put("102", CGMSAlarms.LowGlucose);

        this.alarm_mappings.put("104", CGMSAlarms.CalibrationRequired);
        this.alarm_mappings.put("105", CGMSAlarms.SensorAlarm);
        this.alarm_mappings.put("106", CGMSAlarms.CalibrationError);
        this.alarm_mappings.put("107", CGMSAlarms.SensorEndOfLifeAproaching);
        this.alarm_mappings.put("108", CGMSAlarms.SensorChangeRequired);
        this.alarm_mappings.put("109", CGMSAlarms.SensorError);

        this.alarm_mappings.put("112", CGMSAlarms.WeakSignal);
        this.alarm_mappings.put("113", CGMSAlarms.SensorLost);
        this.alarm_mappings.put("114", CGMSAlarms.HighGlucosePredicted);
        this.alarm_mappings.put("115", CGMSAlarms.LowGlucosePredicted);

        // errors

        this.error_mappings.put("bad", CGMSErrors.SensorBad);
        this.error_mappings.put("end", CGMSErrors.SensorEndOfLife);

        /*
         * Sensor Alert: Meter BG Now (104)
         * Sensor Alarm (105)
         * Sensor Alert: Calibration Error (106)
         * Sensor Alert: Sensor End (107)
         * Sensor Alert: Change Sensor (108)
         * Sensor Alert: Sensor Error (109)
         * Sensor Alert: Lost Sensor (113)
         */

        /*
         * this.alarm_mappings.put("x1",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x2",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x3",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x4",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x5",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x6",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x7",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x8",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         * this.alarm_mappings.put("x9",
         * CGMSAlarms.CGMS_ALARM_HIGH_GLUCOSE_PREDICTED);
         */

    }


    private void setDefinitions()
    {
        // this.defs_pump = new Hashtable<String,String>();
        // this.defs_profile = new Hashtable<String,String>();
        this.setCGMSConfigDefinitions(new Hashtable<String, String>());
        this.config = new Hashtable<String, MinimedCareLinkCGMSData>();

        this.getCGMSConfigDefinitions().put("CurrentTimeDisplayFormat", "");
        this.getCGMSConfigDefinitions().put("CurrentParadigmLinkEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentChildBlockEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentKeypadLockedEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentAlarmClockEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorCalReminderEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorEnable", "");
        this.getCGMSConfigDefinitions().put("CurrentDisplayLanguage", "");
        this.getCGMSConfigDefinitions().put("CurrentParadigmLinkID", "");

        this.getCGMSConfigDefinitions().put("CurrentSensorCalReminderTime", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorAlarmSnoozeTime", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorHighGlucoseSnoozeTime", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorLowGlucoseSnoozeTime", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorMissedDataTime", "");
        this.getCGMSConfigDefinitions().put("ChangeSensorGlucoseLimitPattern", "");
        this.getCGMSConfigDefinitions().put("ChangeSensorGlucoseLimitProfile", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorPredictiveAlertPattern", "");
        this.getCGMSConfigDefinitions().put("CurrentSensorPredictiveAlertProfile", "");
        this.getCGMSConfigDefinitions().put("ChangeSensorSetupConfig2", "");
        this.getCGMSConfigDefinitions().put("x4", "");
        this.getCGMSConfigDefinitions().put("x5", "");
        this.getCGMSConfigDefinitions().put("x6", "");
        this.getCGMSConfigDefinitions().put("x7", "");
        this.getCGMSConfigDefinitions().put("x8", "");
        this.getCGMSConfigDefinitions().put("x9", "");

        /*
         * this.defs_cgms_config.put("x2", "");
         * this.defs_cgms_config.put("x3", "");
         * this.defs_cgms_config.put("x4", "");
         * this.defs_cgms_config.put("x5", "");
         * this.defs_cgms_config.put("x6", "");
         * this.defs_cgms_config.put("x7", "");
         * this.defs_cgms_config.put("x8", "");
         * this.defs_cgms_config.put("x9", "");
         */

        /*
         * this.defs_pump_config.put("CurrentAlarmNotifyMode", "");
         * this.defs_pump_config.put("CurrentBolusReminderEnable", "");
         * this.defs_pump_config.put("CurrentDisplayLanguage", "");
         * this.defs_pump_config.put("CurrentReservoirWarningValueInsulin", "");
         * this.defs_pump_config.put("CurrentBolusWizardCarbUnits", "");
         * this.defs_pump_config.put("CurrentParadigmLinkEnable", "");
         * this.defs_pump_config.put("CurrentAlarmClockEnable", "");
         * this.defs_pump_config.put("CurrentBGReminderEnable", "");
         * this.defs_pump_config.put("CurrentBolusWizardEnable", "");
         * this.defs_pump_config.put("CurrentRFEnable", "");
         * this.defs_pump_config.put("CurrentKeypadLockedEnable", "");
         * this.defs_pump_config.put("CurrentVariableBolusEnable", "");
         * this.defs_pump_config.put("CurrentMaxBasal", "");
         * this.defs_pump_config.put("CurrentInsulinActionCurve", "");
         * this.defs_pump_config.put("CurrentMaxBolus", "");
         * this.defs_pump_config.put("CurrentRemoteControlID", "");
         * this.defs_pump_config.put("CurrentReservoirWarningUnits", "");
         * this.defs_pump_config.put("CurrentBolusWizardBGUnits", "");
         * this.defs_pump_config.put("CurrentTimeDisplayFormat", "");
         * this.defs_pump_config.put("CurrentTempBasalType", "");
         * this.defs_pump_config.put("CurrentPumpModelNumber", "");
         * this.defs_pump_config.put("CurrentAutoOffDuration", "");
         * this.defs_pump_config.put("CurrentVariableBasalProfilePatternEnable",
         * "");
         * this.defs_pump_config.put("CurrentAudioBolusStep", "");
         * this.defs_pump_config.put("CurrentBolusWizardSetupStatus", "");
         * this.defs_pump_config.put("CurrentBGTargetRangePattern", "");
         * this.defs_pump_config.put("CurrentBGTargetRange", "");
         * this.defs_pump_config.put("x7", "");
         * this.defs_pump_config.put("x8", "");
         * this.defs_pump_config.put("x9", "");
         */
        /*
         * this.defs_cgms_config.put("x1", "");
         * this.defs_cgms_config.put("x2", "");
         * this.defs_cgms_config.put("x3", "");
         * this.defs_cgms_config.put("x4", "");
         * this.defs_cgms_config.put("x5", "");
         * this.defs_cgms_config.put("x6", "");
         * this.defs_cgms_config.put("x7", "");
         * this.defs_cgms_config.put("x8", "");
         * this.defs_cgms_config.put("x9", "");
         */

        // this.defs_pump_config.put("x", "");
        // FIXME

    }

    long debug = 0L;
    // MinimedCareLinkCGMSData BGLimit = null;

    /**
     *
     */
    public static final int SPECIAL_DATA_GLUCOSE_LIMIT = 0;

    /**
     *
     */
    public static final int SPECIAL_DATA_PREDICTIVE_ALARM = 1;

    /**
     *
     */
    public static final int SPECIAL_DATA_MAX = 1;

    MinimedCareLinkCGMSData[] special_data = new MinimedCareLinkCGMSData[2];


    @Override
    public void readLineData(String line, int count)
    {

        String[] ld = buildLineData(line);

        MinimedCareLinkCGMSData mcld = new MinimedCareLinkCGMSData(ld, line, this);

        if (mcld.isDeviceData())
        {

            if (mcld.isConfigData() && this.m_reading_type == MinimedCareLink.READ_DEVICE_CONFIG_DATA)
            {
                if (mcld.getKey().startsWith("ChangeSensorGlucoseLimit"))
                {
                    if (mcld.getKey().equals("ChangeSensorGlucoseLimitPattern"))
                    {
                        if (special_data[SPECIAL_DATA_GLUCOSE_LIMIT] == null)
                        {
                            special_data[SPECIAL_DATA_GLUCOSE_LIMIT] = mcld;
                            special_data[SPECIAL_DATA_GLUCOSE_LIMIT].children = new ArrayList<MinimedCareLinkCGMSData>();
                        }
                        else
                        {
                            if (special_data[SPECIAL_DATA_GLUCOSE_LIMIT].dt_long < mcld.dt_long)
                            {
                                special_data[SPECIAL_DATA_GLUCOSE_LIMIT] = mcld;
                                special_data[SPECIAL_DATA_GLUCOSE_LIMIT].children = new ArrayList<MinimedCareLinkCGMSData>();
                            }
                        }
                    }
                    else
                    {
                        if (special_data[SPECIAL_DATA_GLUCOSE_LIMIT].dt_long == mcld.dt_long)
                        {
                            special_data[SPECIAL_DATA_GLUCOSE_LIMIT].children.add(mcld);
                        }
                    }

                    // CurrentBGTargetRangePattern,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954,2232381,93,Paradigm
                    // 522
                    // CurrentBGTargetRange,"PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0",861682955,2232381,94,Paradigm
                    // 522

                }
                else if (mcld.getKey().startsWith("CurrentSensorPredictiveAlert"))
                {
                    if (mcld.getKey().equals("CurrentSensorPredictiveAlertPattern"))
                    {
                        if (special_data[SPECIAL_DATA_PREDICTIVE_ALARM] == null)
                        {
                            special_data[SPECIAL_DATA_PREDICTIVE_ALARM] = mcld;
                            special_data[SPECIAL_DATA_PREDICTIVE_ALARM].children = new ArrayList<MinimedCareLinkCGMSData>();
                        }
                        else
                        {
                            if (special_data[SPECIAL_DATA_PREDICTIVE_ALARM].dt_long < mcld.dt_long)
                            {
                                special_data[SPECIAL_DATA_PREDICTIVE_ALARM] = mcld;
                                special_data[SPECIAL_DATA_PREDICTIVE_ALARM].children = new ArrayList<MinimedCareLinkCGMSData>();
                            }
                        }
                    }
                    else
                    {
                        if (special_data[SPECIAL_DATA_PREDICTIVE_ALARM].dt_long == mcld.dt_long)
                        {
                            special_data[SPECIAL_DATA_PREDICTIVE_ALARM].children.add(mcld);
                        }
                    }
                    // CurrentSensorPredictiveAlertPattern
                }
                else
                {
                    if (config.containsKey(mcld.getKey()))
                    {
                        if (config.get(mcld.getKey()).dt_long < mcld.dt_long)
                        {
                            config.remove(mcld.getKey());
                            config.put(mcld.getKey(), mcld);
                        }
                    }
                    else
                    {
                        config.put(mcld.getKey(), mcld);
                    }
                }

                /*
                 * if (mcld.isTest())
                 * System.out.println(mcld.raw_type + " = " +
                 * mcld.processed_value);
                 */
                // process data on the fly

                // profiles.add(mcld);
            }
            else if (mcld.isDeviceData() && this.m_reading_type == MinimedCareLink.READ_DEVICE_DATA)
            {
                mcld.writeData();
            }
            /*
             * else if (mcld.isDebuged())
             * {
             * //System.out.println(mcld.toString());
             * System.out.println(line);
             * debug++;
             * }
             * else
             * {
             * System.out.println(line);
             * debug++;
             * // System.out.println("Device data not processed.");
             * }
             */

            // System.out.println(line);
            // debug++;

        } // device data

    }


    private void createDeviceValuesWriter()
    {
        this.dvw = new DeviceValuesWriter(false);
        this.dvw.setOutputWriter(this.output_writer);

        // this.dvw.writeObject(_type, _datetime, _value);
        // this.dvw.writeObject(_type, _datetime, code_type, _value)

        // FIXME CGMSTempValue need value flag set and they need to be callled
        // with Enum not Code

        // CGMSTempValues

        this.dvw.put("GlucoseSensorData", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorReading));

        this.dvw.put("AlarmSensor", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Alarm));

        this.dvw.put("BGTherasense", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.SensorCalibration));

        this.dvw.put("SensorWeakSignal", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorWeakSignal));

        this.dvw.put("SensorCal_meter_bg_now", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorCalibrationWithMeterNow));

        this.dvw.put("SensorCal_waiting", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorWaitingForCalibration));

        this.dvw.put("SensorPacket_init", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorInit));

        this.dvw.put("SensorPacket_pre_init", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorPreInit));

        this.dvw.put("SensorPacket_burst", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorBurst));

        this.dvw.put("SensorCal_cal_error", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Error,
                CGMSErrors.SensorCalibrationError));

        this.dvw.put("SensorStatus_off", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorStop));

        this.dvw.put("SensorStatus_on", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorStart));

        this.dvw.put("SensorCalFactor", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorSetCalibrationFactor));

        this.dvw.put("SensorStatus_lost", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorConnectionLost));

        this.dvw.put("GlucoseSensorDataLow", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Event,
                CGMSEvents.SensorDataLowBg));

        this.dvw.put("SensorError", new CGMSTempValues(CGMSObject.SubEntry, CGMSBaseDataType.Error));

        // SensorCal,"CAL_TYPE=meter_bg_now, ISIG=7.03"

        // AlarmSensor

        /*
         * this.dvw.put("Rewind", new PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_CARTRIDGE_REWIND));
         * this.dvw.put("AlarmPump", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE_SET_SUBTYPE,
         * PumpBaseType.PUMP_DATA_ALARM));
         * this.dvw.put("BolusNormal", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BOLUS,
         * PumpBolusType.PUMP_BOLUS_STANDARD));
         * this.dvw.put("BolusSquare", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BOLUS,
         * PumpBolusType.PUMP_BOLUS_SQUARE));
         * this.dvw.put("BolusMultiwave", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BOLUS,
         * PumpBolusType.PUMP_BOLUS_MULTIWAVE));
         * this.dvw.put("BolusWizardBolusEstimate", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_BOLUS_WIZARD));
         * this.dvw.put("BGReceived", new
         * PumpTempValues(PumpTempValues.OBJECT_EXT,
         * PumpAdditionalDataType.PUMP_ADD_DATA_BG));
         * // ChangeTempBasalPercent
         * this.dvw.put("ChangeTempBasalPercent", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BASAL,
         * PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE));
         * this.dvw.put("TBROver", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BASAL,
         * PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_ENDED));
         * this.dvw.put("ChangeActiveBasalProfilePattern", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_BASAL,
         * PumpBasalSubType.PUMP_BASAL_PROFILE));
         * this.dvw.put("ChangeSuspendEnable", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_BASAL_STOP));
         * this.dvw.put("ChangeSuspendEnableNot", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_BASAL_RUN));
         * this.dvw.put("ResultDailyTotal", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_REPORT,
         * PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY));
         * this.dvw.put("SelfTest", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_SELF_TEST));
         * this.dvw.put("ChangeTime", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_EVENT,
         * PumpEvents.PUMP_EVENT_SELF_TEST));
         * this.dvw.put("JournalEntryPumpLowBattery", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_ALARM,
         * PumpAlarms.PUMP_ALARM_BATTERY_LOW));
         * this.dvw.put("JournalEntryPumpLowReservoir", new
         * PumpTempValues(PumpTempValues.OBJECT_BASE,
         * PumpBaseType.PUMP_DATA_ALARM,
         * PumpAlarms.PUMP_ALARM_CARTRIDGE_LOW));
         */

    }


    @Override
    public void postProcessing()
    {

        System.out.println(" ===  Config entries -- Start");

        for (Enumeration<String> en = this.config.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            MinimedCareLinkCGMSData mcld = this.config.get(key);

            if (mcld.hasItems())
            {
                System.out.println(mcld.getKey() + " = " + mcld.getItems());
            }
            else
            {
                System.out.println(mcld.getKey() + " = " + mcld.processed_value);
            }
        }

        System.out.println(" ===  Config entries -- End");

        System.out.println("Debug count left: " + debug);
        // System.out.println("Profile entries: " + this.profiles.size());

        // System.out.println("BGTargetRange: " + BGTargetRange);

    }


    /**
     * @return
     */
    public Hashtable<String, String> getCGMSConfigDefinitions()
    {
        return defs_cgms_config;
    }


    /**
     * @param defs_cgms_config
     */
    public void setCGMSConfigDefinitions(Hashtable<String, String> defs_cgms_config)
    {
        this.defs_cgms_config = defs_cgms_config;
    }

}
