package ggc.pump.device.minimed.file;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpTempValues;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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
 *  Filename:     MinimedCareLinkPump  
 *  Description:  Minimed CareLink Pump
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCareLinkPump extends MinimedCareLink
{
    DataAccessPump m_dap;
    /**
     * Profile names
     */
    public String[] profile_names = null;
    ArrayList<MinimedCareLinkPumpData> profiles;

    @SuppressWarnings("unused")
    private Hashtable<String, String> defs_pump = null;
    private Hashtable<String, String> defs_profile = null;

    /**
     * Defs Pump Config
     */
    public Hashtable<String, String> defs_pump_config = null;

    private Hashtable<String, MinimedCareLinkPumpData> config = null;
    boolean m_read_config = false;

    /**
     * Constructor
     * 
     * @param da
     * @param ow
     * @param reading_type 
     */
    public MinimedCareLinkPump(DataAccessPlugInBase da, OutputWriter ow, int reading_type)
    {
        super(da, ow, reading_type);
        // this.m_read_config = read_config;
        createDeviceValuesWriter();
        m_dap = (DataAccessPump) da;
        setMappingData();
        setDefinitions();
    }

    @Override
    public void setMappingData()
    {
        // new PumpAlarms();
        this.alarm_mappings.put("4", PumpAlarms.NO_DELIVERY.getAlarmCode());

        profile_names = new String[3];
        profile_names[0] = "Standard";
        profile_names[1] = "Pattern A";
        profile_names[2] = "Pattern B";

        profiles = new ArrayList<MinimedCareLinkPumpData>();
    }

    private void setDefinitions()
    {
        this.defs_pump = new Hashtable<String, String>();
        this.defs_profile = new Hashtable<String, String>();
        this.defs_pump_config = new Hashtable<String, String>();
        this.config = new Hashtable<String, MinimedCareLinkPumpData>();

        this.defs_profile.put("ChangeBasalProfile", ""); // change basal
        this.defs_profile.put("ChangeBasalProfilePattern", ""); //
        this.defs_profile.put("ChangeBasalProfilePre", "");
        this.defs_profile.put("ChangeBasalProfilePatternPre", "");

        this.defs_pump_config.put("CurrentAlarmNotifyMode", "");
        this.defs_pump_config.put("CurrentBolusReminderEnable", "");
        this.defs_pump_config.put("CurrentDisplayLanguage", "");
        this.defs_pump_config.put("CurrentReservoirWarningValueInsulin", "");
        this.defs_pump_config.put("CurrentBolusWizardCarbUnits", "");
        this.defs_pump_config.put("CurrentParadigmLinkEnable", "");
        this.defs_pump_config.put("CurrentAlarmClockEnable", "");
        this.defs_pump_config.put("CurrentBGReminderEnable", "");
        this.defs_pump_config.put("CurrentBolusWizardEnable", "");
        this.defs_pump_config.put("CurrentRFEnable", "");
        this.defs_pump_config.put("CurrentKeypadLockedEnable", "");
        this.defs_pump_config.put("CurrentVariableBolusEnable", "");

        this.defs_pump_config.put("CurrentMaxBasal", "");
        this.defs_pump_config.put("CurrentInsulinActionCurve", "");
        this.defs_pump_config.put("CurrentMaxBolus", "");
        this.defs_pump_config.put("CurrentRemoteControlID", "");
        this.defs_pump_config.put("CurrentReservoirWarningUnits", "");
        this.defs_pump_config.put("CurrentBolusWizardBGUnits", "");
        this.defs_pump_config.put("CurrentTimeDisplayFormat", "");
        this.defs_pump_config.put("CurrentTempBasalType", "");
        this.defs_pump_config.put("CurrentPumpModelNumber", "");

        this.defs_pump_config.put("CurrentAutoOffDuration", "");
        this.defs_pump_config.put("CurrentVariableBasalProfilePatternEnable", "");
        this.defs_pump_config.put("CurrentAudioBolusStep", "");
        this.defs_pump_config.put("CurrentBolusWizardSetupStatus", "");
        this.defs_pump_config.put("CurrentBGTargetRangePattern", "");
        this.defs_pump_config.put("CurrentBGTargetRange", "");
        this.defs_pump_config.put("x7", "");
        this.defs_pump_config.put("x8", "");
        this.defs_pump_config.put("x9", "");

        /*
         * this.defs_pump_config.put("x1", "");
         * this.defs_pump_config.put("x2", "");
         * this.defs_pump_config.put("x3", "");
         * this.defs_pump_config.put("x4", "");
         * this.defs_pump_config.put("x5", "");
         * this.defs_pump_config.put("x6", "");
         * this.defs_pump_config.put("x7", "");
         * this.defs_pump_config.put("x8", "");
         * this.defs_pump_config.put("x9", "");
         */

        // this.defs_pump_config.put("x", "");
        // FIXME

    }

    long debug = 0L;
    MinimedCareLinkPumpData BGTargetRange = null;

    @Override
    public void readLineData(String line, int count)
    {

        String[] ld = buildLineData(line);

        MinimedCareLinkPumpData mcld = new MinimedCareLinkPumpData(ld, line, this);

        if (mcld.isDeviceData())
        {
            if (m_reading_type == READ_DEVICE_DATA)
            {
                // pump data

                if (mcld.isProfileData())
                {
                    mcld.processData();
                    profiles.add(mcld);
                }
                else
                {
                    mcld.processData();
                    // System.out.println(mcld.getUnprocessedData());
                }
            }
            else
            {
                // config

                if (mcld.isConfigData())
                {

                    if (mcld.getKey().startsWith("CurrentBGTargetRange"))
                    {
                        if (mcld.getKey().equals("CurrentBGTargetRangePattern"))
                        {
                            if (BGTargetRange == null)
                            {
                                BGTargetRange = mcld;
                                BGTargetRange.children = new ArrayList<MinimedCareLinkPumpData>();
                            }
                            else
                            {
                                if (BGTargetRange.dt_long < mcld.dt_long)
                                {
                                    BGTargetRange = mcld;
                                    BGTargetRange.children = new ArrayList<MinimedCareLinkPumpData>();
                                }
                            }
                        }
                        else
                        {
                            if (BGTargetRange.dt_long == mcld.dt_long)
                            {
                                BGTargetRange.children.add(mcld);
                            }
                        }

                        // CurrentBGTargetRangePattern,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954,2232381,93,Paradigm
                        // 522
                        // CurrentBGTargetRange,"PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0",861682955,2232381,94,Paradigm
                        // 522

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
                } // config data

            } // else
        } // device_data

        /*
         * if (mcld.isProfileData())
         * {
         * profiles.add(mcld);
         * }
         * else if ((mcld.isConfigData()) && (m_read_config))
         * {
         * if (mcld.getKey().startsWith("CurrentBGTargetRange"))
         * {
         * if (mcld.getKey().equals("CurrentBGTargetRangePattern"))
         * {
         * if (BGTargetRange == null)
         * {
         * BGTargetRange = mcld;
         * BGTargetRange.children = new ArrayList<MinimedCareLinkPumpData>();
         * }
         * else
         * {
         * if (BGTargetRange.dt_long < mcld.dt_long)
         * {
         * BGTargetRange = mcld;
         * BGTargetRange.children = new ArrayList<MinimedCareLinkPumpData>();
         * }
         * }
         * }
         * else
         * {
         * if (BGTargetRange.dt_long==mcld.dt_long)
         * BGTargetRange.children.add(mcld);
         * }
         * //
         * CurrentBGTargetRangePattern,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954
         * ,2232381,93,Paradigm 522
         * // CurrentBGTargetRange,
         * "PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0"
         * ,861682955,2232381,94,Paradigm 522
         * }
         * else
         * {
         * if (config.containsKey(mcld.getKey()))
         * {
         * if (config.get(mcld.getKey()).dt_long < mcld.dt_long)
         * {
         * config.remove(mcld.getKey());
         * config.put(mcld.getKey(), mcld);
         * }
         * }
         * else
         * {
         * config.put(mcld.getKey(), mcld);
         * }
         * }
         * // process data on the fly
         * //profiles.add(mcld);
         * }
         * else if (mcld.isProcessed())
         * {
         * //mcld.
         * //System.out.println("Proc: " + line);
         * }
         * else if (mcld.isDebuged())
         * {
         * //System.out.println(mcld.toString());
         * System.out.println("Debug: " + line);
         * debug++;
         * }
         */

        // else
        // System.out.println("NoDeviceData: " + line);

        // if (count==5105)
        // showCollection(ld);

        // System.out.println(count + ": [size=" + ld.length + ",id=" +
        // ld[0] + ",el33=" + ld[33] + "]");

    }

    private void createDeviceValuesWriter()
    {
        // <<this.dvw = new DeviceValuesWriter(false);

        this.dvw = new DeviceValuesWriter(true);
        this.dvw.setOutputWriter(this.output_writer);

        // this.dvw.writeObject(_type, _datetime, _value);
        // this.dvw.writeObject(_type, _datetime, code_type, _value)

        // FIXME

        this.dvw.put("Prime", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_EVENT,
                PumpEvents.PUMP_EVENT_PRIME_INFUSION_SET));

        this.dvw.put("Rewind", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_EVENT,
                PumpEvents.PUMP_EVENT_CARTRIDGE_REWIND));

        this.dvw.put("AlarmPump", new PumpTempValues(PumpTempValues.OBJECT_BASE_SET_SUBTYPE,
                PumpBaseType.PUMP_DATA_ALARM));

        this.dvw.put("BolusNormal", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BOLUS,
                PumpBolusType.PUMP_BOLUS_STANDARD));

        this.dvw.put("BolusSquare", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BOLUS,
                PumpBolusType.PUMP_BOLUS_SQUARE));

        this.dvw.put("BolusMultiwave", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BOLUS,
                PumpBolusType.PUMP_BOLUS_MULTIWAVE));

        this.dvw.put("BolusWizardBolusEstimate", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_EVENT, PumpEvents.PUMP_EVENT_BOLUS_WIZARD));

        this.dvw.put("BGReceived", new PumpTempValues(PumpTempValues.OBJECT_EXT,
                PumpAdditionalDataType.PUMP_ADD_DATA_BG));

        // ChangeTempBasalPercent
        this.dvw.put("ChangeTempBasalPercent", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_BASAL, PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE));

        this.dvw.put("TBROver", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BASAL,
                PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_ENDED));

        this.dvw.put("ChangeActiveBasalProfilePattern", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_BASAL, PumpBasalSubType.PUMP_BASAL_PROFILE));

        this.dvw.put("ChangeSuspendEnable", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_EVENT, PumpEvents.PUMP_EVENT_BASAL_STOP));

        this.dvw.put("ChangeSuspendEnableNot", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_EVENT, PumpEvents.PUMP_EVENT_BASAL_RUN));

        this.dvw.put("ResultDailyTotal", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_REPORT,
                PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY));

        this.dvw.put("SelfTest", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_EVENT,
                PumpEvents.PUMP_EVENT_SELF_TEST));

        this.dvw.put("ChangeTime", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_EVENT,
                PumpEvents.PUMP_EVENT_SELF_TEST));

        this.dvw.put("JournalEntryPumpLowBattery", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_ALARM, PumpAlarms.BATTERY_LOW.getAlarmCode()));

        this.dvw.put("JournalEntryPumpLowReservoir", new PumpTempValues(PumpTempValues.OBJECT_BASE,
                PumpBaseType.PUMP_DATA_ALARM, PumpAlarms.CARTRIDGE_LOW.getAlarmCode()));

    }

    @Override
    public void postProcessing()
    {

        if (m_reading_type == READ_DEVICE_DATA)
        {
            // ArrayList<MinimedCareLinkPumpData> profiles

            Hashtable<String, MinimedCareLinkPumpProfile> profiles_proc = new Hashtable<String, MinimedCareLinkPumpProfile>();

            for (int i = 0; i < profiles.size(); i++)
            {
                // System.out.println(profiles.get(i));
                // //.getUnprocessedData());
                MinimedCareLinkPumpData d = profiles.get(i);
                String dt = d.date + "_" + d.time;

                if (profiles_proc.containsKey(dt))
                {
                    profiles_proc.get(dt).add(d);
                }
                else
                {
                    MinimedCareLinkPumpProfile pp = new MinimedCareLinkPumpProfile(dt, this);
                    pp.add(d);

                    profiles_proc.put(dt, pp);
                }

                // System.out.println(profiles.get(i).raw_type + " = " +
                // profiles.get(i).raw_values); //.getUnprocessedData());
            }

            for (MinimedCareLinkPumpProfile mcp : profiles_proc.values())
            {
                // System.out.println(mcp);
            }

        }
        else
        {

            System.out.println(" ===  Config entries -- Start");

            for (Enumeration<String> en = this.config.keys(); en.hasMoreElements();)
            {
                String key = en.nextElement();
                MinimedCareLinkPumpData mcld = this.config.get(key);

                System.out.println(mcld.raw_type + " = " + mcld.processed_value);
            }

            System.out.println(" ===  Config entries -- End");

            System.out.println("Debug count left: " + debug);
            System.out.println("Profile entries: " + this.profiles.size());

            System.out.println("BGTargetRange: " + BGTargetRange);
        }

    }

}
