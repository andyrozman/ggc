package ggc.pump.device.minimed.file;

import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.device.impl.minimed.file.MinimedCareLinkData;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     MinimedCareLinkPumpData  
 *  Description:  Minimed CareLink PumpData
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedCareLinkPumpData extends MinimedCareLinkData
{

    private static Log log = LogFactory.getLog(MinimedCareLinkPumpData.class);
    private boolean post_process = false;
    private MinimedCareLinkPump mmclp;
    private String index = "";
    
    /**
     * Children
     */
    public ArrayList<MinimedCareLinkPumpData> children = null;
    
    /**
     * Constructor 
     * 
     * @param data
     * @param full_data
     * @param mcl_
     */
    public MinimedCareLinkPumpData(String[] data, String full_data, MinimedCareLink mcl_ )
    {
        super(data, full_data, mcl_);
        mmclp = (MinimedCareLinkPump)mcl_;
        //processData();
        
    }

    // FIXME
    /**
     * Get Number Of Parameters
     * 
     * @return
     */
    public int getNumberOfParameters()
    {
        /*if ((this.raw_type.equals("Prime")) || 
            (this.raw_type.equals("AlarmPump")))
            return 1; */
        if (this.raw_type.equals("Rewind"))
            return 0;
        else
            return 1;

    }
    
    
    
    
    /**
     * Is Device Data
     * 
     * @return
     */
    public boolean isDeviceData()
    {
        // FIXME
        /*
        
        this.raw_type.equals("Prime")
        return this.raw_type.equals("Rewind");
        return this.raw_type.equals("AlarmPump");
        return this.raw_type.equals("BolusNormal");
        return this.raw_type.equals("BolusSquare");
                this.raw_type.equals("BolusWizardBolusEstimate") ||  // bolus wiyard estimate
        
        
        DONE
        
        */
        
        
        if (

            // bolus
            this.raw_type.equals("BolusNormal") ||         // bolus - normal
            this.raw_type.equals("BolusSquare") ||         // bolus - square / multiwave
            this.raw_type.equals("BolusWizardBolusEstimate") ||  // bolus wizard estimate
            // bg
            this.raw_type.equals("BGReceived") ||           // bg data from meter
            // basal
            this.raw_type.equals("ChangeTempBasalPercent") ||    // TBR
            this.raw_type.equals("ChangeSuspendEnable") ||       // pump - suspend
            this.raw_type.equals("ChangeActiveBasalProfilePattern") ||  // active basal profile pattern 
            this.raw_type.equals("ChangeBasalProfile") ||        // change basal
            // basal pattern - processed separately
            //this.raw_type.equals("ChangeBasalProfilePatternPre") || 
            //this.raw_type.equals("ChangeBasalProfilePre") || 
            this.raw_type.equals("ChangeBasalProfilePattern") || // basal profile pattern - change
            // pump
            this.raw_type.equals("Prime") ||               // prime
            this.raw_type.equals("Rewind") ||              // rewind
            this.raw_type.equals("AlarmPump") ||           // alarm
            this.raw_type.equals("ResultDailyTotal") ||    // report - result daily total
            this.raw_type.equals("SelfTest") ||            // self test
            this.raw_type.equals("ChangeTime") ||          // time changed
            // journal
            this.raw_type.equals("JournalEntryPumpLowBattery") ||     // warn - low baterry
            this.raw_type.equals("JournalEntryPumpLowReservoir") ||   // warn - low reservoir
            
            
            
            // not yet - pump
            
            
            
            // ignored 
            //this.raw_type.equals("ChangeParadigmLinkID") ||           
            //this.raw_type.equals("ClearAlarm") ||           // clear alarm
            //this.raw_type.equals("UnabsorbedInsulin") ||    // unabsorbed insulin
            //this.raw_type.equals("ChangeTimeGH") ||         // time changed ?

            

            
            // config change pump
      /*      this.raw_type.equals("ChangeBolusWizardSetupConfig") ||
            this.raw_type.equals("ChangeBolusWizardSetup") ||
            this.raw_type.equals("ChangeBatteryEnable") || 
            this.raw_type.equals("ChangeBatteryEnableGH") || 
            this.raw_type.equals("ChangeTimeDisplayFormat") || 
            this.raw_type.equals("ChangeMaxBasal") || 
            this.raw_type.equals("ChangeCarbRatio") ||      // change carb ratio
            this.raw_type.equals("ChangeCarbRatioPattern") ||
            this.raw_type.equals("ChangeBGTargetRange") ||  // target range
            this.raw_type.equals("ChangeBGTargetRangePattern") ||
            this.raw_type.equals("ChangeInsulinSensitivity") ||  // change insulin sensitivity
            this.raw_type.equals("ChangeInsulinSensitivityPattern") || //
        */    
            // current 
            this.raw_type.equals("CurrentAlarmNotifyMode") ||
            this.raw_type.equals("CurrentRFEnable") ||
            this.raw_type.equals("CurrentBolusReminderEnable") ||
            
            
            
            this.raw_type.equals("CurrentRFEnable") ||
            //this.raw_type.equals("CurrentTotalYesterday") ||
            //this.raw_type.equals("CurrentBatteryStatus") ||
            //this.raw_type.equals("CurrentTotalRemaining") ||
            //this.raw_type.equals("CurrentErrorStatus") ||
            this.raw_type.equals("CurrentDisplayLanguage") ||
            //this.raw_type.equals("CurrentGlucoseHistoryPageNumber") ||
            this.raw_type.equals("CurrentTimeDisplayFormat") ||
            this.raw_type.equals("CurrentAudioBolusStep") ||
            this.raw_type.equals("CurrentVariableBolusEnable") ||
            this.raw_type.equals("CurrentAutoOffDuration") ||
            //this.raw_type.equals("CurrentTotalToday") ||
            //this.raw_type.equals("CurrentTempBasal") ||
            this.raw_type.equals("CurrentMaxBasal") ||
            this.raw_type.equals("CurrentMaxBolus") ||
            //this.raw_type.equals("CurrentActiveBasalProfilePattern") ||
            this.raw_type.equals("CurrentVariableBasalProfilePatternEnable") ||
            this.raw_type.equals("CurrentBolusWizardSetupStatus") ||
            this.raw_type.equals("CurrentBGTargetRangePattern") ||
            this.raw_type.equals("CurrentBGTargetRange") ||
            this.raw_type.equals("CurrentReservoirWarningUnits") ||
            this.raw_type.equals("CurrentReservoirWarningValueInsulin") ||
            this.raw_type.equals("CurrentKeypadLockedEnable") ||
            //this.raw_type.equals("CurrentSavedSettingsTime") ||
            //this.raw_type.equals("CurrentHistoryPageNumber") ||
            this.raw_type.equals("CurrentRemoteControlID") ||
            //this.raw_type.equals("CurrentPumpStatus") ||
            this.raw_type.equals("CurrentTempBasalType") ||
            this.raw_type.equals("CurrentParadigmLinkEnable") ||
            this.raw_type.equals("CurrentInsulinActionCurve") ||
            this.raw_type.equals("CurrentPumpModelNumber") ||
            this.raw_type.equals("CurrentAlarmClockEnable") ||
            this.raw_type.equals("CurrentBGReminderEnable") ||
            this.raw_type.equals("CurrentBolusWizardEnable") ||
            this.raw_type.equals("CurrentBolusWizardBGUnits") ||
            this.raw_type.equals("CurrentBolusWizardCarbUnits") //||
            //this.raw_type.equals("CurrentCarbRatioPattern") ||
            //this.raw_type.equals("CurrentCarbRatio") ||
            //this.raw_type.equals("CurrentInsulinSensitivityPattern") ||
            //this.raw_type.equals("CurrentInsulinSensitivity") //||
            //this.raw_type.equals("CurrentBasalProfile") ||
            //this.raw_type.equals("CurrentBasalProfilePattern") 
                
          )
            return true;
        else
            return false;
            
            
            
            
            /* CGMS
            if (this.raw_type.equals("GlucoseSensorData") ||           // data
                this.raw_type.equals("GlucoseSensorDataLow") ||        // data low
                this.raw_type.equals("SensorWeakSignal") ||     // warning - sensor weak signal
                this.raw_type.equals("AlarmSensor") ||          // alarm
                 
                 this.raw_type.equals("CalBGForGH") ||          // unknown
                 this.raw_type.equals("CalBGForPH") ||          // unknown
                 this.raw_type.equals("ChangeBatteryEnable") || // unknown
                 this.raw_type.equals("ChangeBatteryEnableGH") ||  // unknown

                 this.raw_type.equals("SensorSync") ||             // unknown
                 this.raw_type.equals("SensorCalFactor") ||     // unknown
                 this.raw_type.equals("SensorCal") ||           // unknown
                 this.raw_type.equals("SensorStatus") ||        // status - unknown
                 this.raw_type.equals("SensorTimestamp") ||     // unknown
                 this.raw_type.equals("SensorPacket") ||        // unknown 
                 this.raw_type.equals("SensorError") ||         // unknown
                 
                 // config
                 this.raw_type.equals("ChangeSensorGlucoseLimitProfile") ||  // config
                 this.raw_type.equals("ChangeSensorAlarmSilenceConfig") ||
                 this.raw_type.equals("ChangeSensorGlucoseLimitPattern") ||
                 this.raw_type.equals("ChangeSensorGlucoseLimitPatternSetup") ||
                 this.raw_type.equals("ChangeSensorSetup2") ||
                 this.raw_type.equals("ChangeSensorSetupConfig2") ||
                 
                 // current ?
                 this.raw_type.equals("CurrentSensorGlucoseLimitProfile") || 
                 this.raw_type.equals("CurrentSensorHighGlucoseSnoozeTime") ||
                 this.raw_type.equals("CurrentSensorPredictiveAlertProfile") ||
                 this.raw_type.equals("CurrentSensorGlucoseLimitPattern") ||
                 this.raw_type.equals("CurrentSensorPredictiveAlertPattern") ||
                 this.raw_type.equals("CurrentTimeDisplayFormat") ||
                 this.raw_type.equals("CurrentBeepVolume") ||
                 this.raw_type.equals("CurrentChildBlockEnable") ||
                 this.raw_type.equals("CurrentKeypadLockedEnable") ||
                 this.raw_type.equals("CurrentAlarmClockEnable") ||
                 this.raw_type.equals("CurrentAlarmNotifyMode") ||
                 this.raw_type.equals("CurrentBatteryStatus") ||
                 this.raw_type.equals("CurrentErrorStatus") ||
                 this.raw_type.equals("CurrentPumpStatus") ||
                 this.raw_type.equals("CurrentPumpModelNumber") ||
                 this.raw_type.equals("CurrentDisplayLanguage") ||
                 this.raw_type.equals("CurrentParadigmLinkEnable") ||
                 this.raw_type.equals("CurrentSavedSettingsTime") ||
                 this.raw_type.equals("CurrentHistoryPageNumber") ||
                 this.raw_type.equals("CurrentCarbUnits") ||
                 this.raw_type.equals("CurrentGlucoseHistoryPageNumber") ||
                 this.raw_type.equals("CurrentSensorCalFactor") ||
                 this.raw_type.equals("CurrentSensorCalReminderEnable") ||
                 this.raw_type.equals("CurrentSensorEnable") ||
                 this.raw_type.equals("CurrentSensorCalReminderTime") ||
                 this.raw_type.equals("CurrentSensorAlarmSnoozeTime") ||
                 this.raw_type.equals("CurrentSensorLowGlucoseSnoozeTime") ||
                 this.raw_type.equals("CurrentSensorMissedDataTime") ||
                 this.raw_type.equals("CurrentSensorTransmitterID") ||
                 this.raw_type.equals("CurrentSensorBGUnits") ||
                 this.raw_type.equals("CurrentSensorAlarmSilenceConfig") ||
                 this.raw_type.equals("CurrentSensorGraphConfig") ||
                 this.raw_type.equals("CurrentSensorRateOfChangeAlertConfig") ||
                 this.raw_type.equals("CurrentSensorAreaUnderCurveConfig") ||

                 //JournalEntryPumpLowBattery             
                 
                 
                 // ignore
                 this.raw_type.equals("CurrentParadigmLinkID") ||  // ignore
                 
                 this.raw_type.equals("BGTherasense")           // meter entry
                )
                return true;
                else
                    return false;
            */
    }
        
        
    
    
    
    /**
     * Is Profile Data
     * 
     * @return
     */
    public boolean isProfileData()
    {
        return (this.raw_type.equals("ChangeBasalProfilePatternPre") ||
                this.raw_type.equals("ChangeBasalProfilePre") ||
                this.raw_type.equals("ChangeBasalProfilePattern") ||
                this.raw_type.equals("ChangeBasalProfile")   // change basal
        );
        //return false;
    }
    
    
    /**
     * Is Config Data
     * 
     * @return
     */
    public boolean isConfigData()
    {
        return this.mmclp.defs_pump_config.containsKey(this.raw_type);
    }
    
   
    
    
    // FIXME
    /**
     * Is Debugged
     * 
     * @return
     */
    public boolean isDebuged()
    {
        return this.raw_type.startsWith("Current");
        //return this.raw_type.equals("CurrentRFEnable");
        
        //return this.raw_type.equals("CurrentAlarmNotifyMode");
        //return false;
                /*
                || 
        this.raw_type.equals("ChangeBasalProfilePre") || 
        this.raw_type.equals("ChangeBasalProfilePattern"));//  || // basal profile pattern
*/
        
        //return this.raw_type.equals("ChangeActiveBasalProfilePattern");
        //return this.raw_type.equals("ChangeTempBasalPercent");
        //return this.raw_type.equals("BGReceived");
        //return this.raw_type.equals("BolusSquare");
        //return this.raw_type.equals("BolusNormal");
        
        //return this.raw_type.equals("Prime");
        //return this.raw_type.equals("Rewind");
        //return this.raw_type.equals("AlarmPump");
        
        /*
        // pump
        if (    this.raw_type.equals("ClearAlarm") ||  // clear alarm
                this.raw_type.equals("ChangeSuspendEnable") ||  // suspend
                
                
            return true;
        else
            return false;
            */
    }
    

    public long getAtechDateLong()
    {
        return this.mcl.mm_date.getAtechDateLong(date, time);
    }
    
      
    public void processData()
    {
        String v = this.getProcessedValue();
        
        if (v==null)
        {
            this.data_processed = false;
            
            if (this.isConfigData())
            {
                dt_long = this.mcl.mm_date.getAtechDateLong(date, time);
            }
            
        }
        else
        {
            if ((this.getNumberOfParameters()==0) || (this.getNumberOfParameters()==1))
            {
                this.mcl.dvw.writeObject(this.raw_type, 
                    this.mcl.mm_date.getAtechDate(date, time), 
                    v);
            }
            else if (this.getNumberOfParameters()==2)
            {
                System.out.println("BAD 2");
            }
            else 
            {
                System.out.println("BAD More");
            }
                
            this.data_processed = true;
        }
        
        postProcess();
        
    }
    
    /**
     * Post Process
     */
    public void postProcess()
    {
        if (this.post_process)
            this.data_processed = true;
        
    }
    
    
    /**
     * Get Key
     * 
     * @return
     */
    public String getKey()
    {
        if (this.raw_type.equals("CurrentRemoteControlID"))
        {
            return this.raw_type + index;
        }
        else
            return this.raw_type;
        
//        System.out.println("Debug count left: " + debug);

        
    }
    
    
    
    /**
     * Get Processed Value
     * 
     * @return
     */
    public String getProcessedValue()
    {
        String s;
        
        if ((this.raw_type.equals("Prime")) || 
            (this.raw_type.equals("ResultDailyTotal"))
            ) 
        {
            s = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
            
            return s;
        }
        else if ((this.raw_type.equals("Rewind")) ||
                 (this.raw_type.equals("SelfTest")) ||
                 (this.raw_type.equals("JournalEntryPumpLowBattery")) 
                 
                 
                 )
        {
            return "";
        }
        else if (this.raw_type.equals("BolusNormal")) 
        {
            s = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
            
            if (this.raw_values.contains("IS_DUAL_COMPONENT=true"))
            {
                post_process = true;
                this.mcl.temp_data = this;
                
                return null;
            }
            
            return s;
        }
        else if (this.raw_type.equals("BolusSquare"))
        {
            MinimedCareLinkData mcd_temp = this.mcl.temp_data;

            String s1, s2, s3; 
            s1 = getDataNumber(this.raw_values, "AMOUNT=", "CONCENTRATION=");
            s2 = getDataDuration(this.raw_values, "DURATION=", "IS_DUAL_COMPONENT=");
            
            
            if (mcd_temp!=null)
            {
                this.raw_type = "BolusMultiwave";
                this.date = mcd_temp.date;
                this.time = mcd_temp.time;
                
                s3 = getDataNumber(mcd_temp.raw_values, "AMOUNT=", "CONCENTRATION=");

                s = "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s";
                s = String.format(s, s3, s1, s2);
                this.mcl.temp_data = null;
                return s;
            }
            else
            {
                s = "AMOUNT_SQUARE=%s;DURATION=%s";
                s = String.format(s, s1, s2);
                return s;
            }
            
        }
        else if (this.raw_type.equals("BolusWizardBolusEstimate"))
        {
            String[] data = new String[11];
            
            data[0] = getDataNumber(this.raw_values, "BG_INPUT=", "BG_UNITS=");
            data[1] = getDataNumber(this.raw_values, "CARB_INPUT=", "CARB_UNITS=");
            data[2] = getDataNumber(this.raw_values, "CARB_UNITS=", "CARB_RATIO=");
            data[3] = getDataNumber(this.raw_values, "CARB_RATIO=", "INSULIN_SENSITIVITY=");
            data[4] = getDataNumber(this.raw_values, "INSULIN_SENSITIVITY=", "BG_TARGET_LOW=");
            
            data[5] = getDataNumber(this.raw_values, "BG_TARGET_LOW=", "BG_TARGET_HIGH=");
            data[6] = getDataNumber(this.raw_values, "BG_TARGET_HIGH=", "BOLUS_ESTIMATE=");
            data[7] = getDataNumber(this.raw_values, "BOLUS_ESTIMATE=", "CORRECTION_ESTIMATE=");
            data[8] = getDataNumber(this.raw_values, "CORRECTION_ESTIMATE=", "FOOD_ESTIMATE=");
            data[9] = getDataNumber(this.raw_values, "FOOD_ESTIMATE=", "UNABSORBED_INSULIN_TOTAL=");
            data[10] = getDataNumber(this.raw_values, "UNABSORBED_INSULIN_TOTAL=", "UNABSORBED_INSULIN_COUNT=");
            
            
            // FIXME
            // CH
            
            
            return String.format(DataAccessPump.getInstance().getPumpEventTypes().getValueTemplateForType(PumpEvents.PUMP_EVENT_BOLUS_WIZARD), 
                    data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]);
            
            //BG_INPUT=109, 898, BG_UNITS=mmol l, CARB_INPUT=74, CARB_UNITS=grams, 
            // CARB_RATIO=15, INSULIN_SENSITIVITY=54, 048, BG_TARGET_LOW=99, 088, 
            // BG_TARGET_HIGH=108, 096, BOLUS_ESTIMATE=4, 9, CORRECTION_ESTIMATE=0, 
            // FOOD_ESTIMATE=4, 9, UNABSORBED_INSULIN_TOTAL=0, 2, UNABSORBED_INSULIN_COUNT=3, 
            
        }
        else if ((this.raw_type.equals("BGReceived")) ||
                (this.raw_type.equals("JournalEntryPumpLowReservoir")))   
        {
            return getDataNumber(this.raw_values, "AMOUNT=", "ACTION_REQUESTOR=");
        }
        else if (this.raw_type.equals("ChangeTempBasalPercent"))
        {
            String s1 = getDataNumber(this.raw_values, "PERCENT_OF_RATE=", "DURATION=");
            s = getDataDuration(this.raw_values, "DURATION=", "ACTION_REQUESTOR=");

            if (s==null)
            {
                this.raw_type = "TBROver";
                return "";
            }
            else
                return String.format("DURATION=%s;VALUE=%s", s, s1) + "%";
            
           // PERCENT_OF_RATE=0, DURATION=0, 
            //PERCENT_OF_RATE=85, DURATION=39600000, ACTION_REQUESTOR=pump
        }
        else if (this.raw_type.equals("ChangeActiveBasalProfilePattern")) 
        {
            //System.out.println(this.raw_values);
            s = this.getDataBetween(this.raw_values, "PATTERN_NAME=", "OLD_PATTERN_NAME=");
            
            if (s.toLowerCase().equals("standard"))
                return this.mmclp.profile_names[0];
            else if (s.toLowerCase().equals("pattern a"))
                return this.mmclp.profile_names[1];
            else if (s.toLowerCase().equals("pattern b"))
                return this.mmclp.profile_names[2];
            else
            {
                String a = "Unsupported profile pattern change: " + s + "\n" + this.full_data_line;
                
                this.mcl.output_writer.addErrorMessage(a);
                log.error(a);
                return null;
            }
            
            // PATTERN_NAME=pattern b
            // PATTERN_NAME=standard
//            ChangeBasalProfilePattern,"PATTERN_NAME=pattern a, NUM_PROFILES=20
        }
        else if (this.raw_type.equals("ChangeSuspendEnable"))
        {
            s = this.getDataBetween(this.raw_values, "ENABLE=", "ACTION_REQUESTOR=");
            
            if (s.equals("false"))
            {
                this.raw_type = "ChangeSuspendEnableNot";
            }
            return "";
        }
        else if (this.raw_type.equals("ChangeTime"))
        {
            /*
            s = getDataNumber(this.raw_values, "NEW_TIME=", "ACTION_REQUESTOR=");
            
            long x = Long.parseLong(s);
            
            Date gc = new Date();
            gc.setTime(x);
            
            /*
            GregorianCalendar gc = new GregorianCalendar(x );
            gc.setTimeInMillis(x);
            gc.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());
            */
            
            // problem with timezones
            //System.out.println( gc.get(Calendar.DAY_OF_MONTH) + "/" + (gc.get(Calendar.MONTH)+1) + "/" + gc.get(Calendar.YEAR) + "  " + gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE) + ":" + gc.get(Calendar.SECOND));
           //System.out.println( /*gc.get(Calendar.DAY_OF_MONTH) + "/" + (gc.get(Calendar.MONTH)+1) + "/" + gc.get(Calendar.YEAR) + "  " +*/ gc.getHours() + ":" + gc.getMinutes() + ":" + gc.getSeconds());
            
            //08.6.14  11:14:00
            //1213442040 000
            return getDataNumber(this.raw_values, "NEW_TIME=", "ACTION_REQUESTOR=");
        }
        else if (this.raw_type.equals("AlarmPump"))
        {
            s = getDataBetween(this.raw_values, "RAW_TYPE=", "RAW_MODULE=");
            
            if (this.mcl.alarm_mappings.containsKey(s))
                return "" + this.mcl.alarm_mappings.get(s);
            else
            {
                String a = "Unsupported alarm mapping: " + s + "\n" + this.full_data_line;
                    
                this.mcl.output_writer.addErrorMessage(a);
                log.error(a);
                return null;
            }
            
            //System.out.println("ALARM: " + s);
            //RAW_TYPE=4, RAW_MODULE=18, 
            
        }
        else if (this.raw_type.equals("CurrentAlarmNotifyMode"))
        {
            this.processed_value = getDataAfter(this.raw_values, "MODE=");
            return null;
        }
        else if ((this.raw_type.equals("CurrentBolusReminderEnable")) ||
                 (this.raw_type.equals("CurrentParadigmLinkEnable")) ||
                 (this.raw_type.equals("CurrentBGReminderEnable")) ||
                 (this.raw_type.equals("CurrentBolusWizardEnable")) ||
                 (this.raw_type.equals("CurrentRFEnable")) ||
                 (this.raw_type.equals("CurrentKeypadLockedEnable")) ||
                 (this.raw_type.equals("CurrentVariableBolusEnable")) ||
                 (this.raw_type.equals("CurrentVariableBasalProfilePatternEnable")) ||
                 (this.raw_type.equals("CurrentBolusWizardEnable")) ||
                 (this.raw_type.equals("CurrentBolusWizardEnable")) ||
                 (this.raw_type.equals("CurrentAlarmClockEnable")) 
                 )
        {
            this.processed_value = getDataAfter(this.raw_values, "ENABLE=");
            return null;
        }
        else if (this.raw_type.equals("CurrentDisplayLanguage"))
        {
            this.processed_value = getDataAfter(this.raw_values, "LANGUAGE=");
            return null;
        }
        else if ((this.raw_type.equals("CurrentReservoirWarningValueInsulin")) ||
                (this.raw_type.equals("CurrentInsulinActionCurve")) ||
                (this.raw_type.equals("CurrentMaxBolus")) ||
                (this.raw_type.equals("CurrentInsulinActionCurve")) ||
                (this.raw_type.equals("CurrentInsulinActionCurve")) ||
                (this.raw_type.equals("CurrentInsulinActionCurve")) ||
                (this.raw_type.equals("CurrentInsulinActionCurve")) 
                )
        {
            
            this.processed_value = getDataAfter(this.raw_values, "AMOUNT=");
            return null;
        }
        else if (this.raw_type.equals("CurrentMaxBasal"))
        {
            this.processed_value = getDataAfter(this.raw_values, "RATE=");
            
            //System.out.println(this.processed_value);
            
            return null;
        }
        else if (this.raw_type.equals("CurrentRemoteControlID"))
        {
            this.index = getDataNumber(this.raw_values, "PROGRAMMER_NUM=", "REMOTE_CONTROL_ID=");
            this.processed_value = getDataAfter(this.raw_values, "REMOTE_CONTROL_ID=");
            this.processed_value = this.processed_value.substring(0, this.processed_value.length()-1);
            
            //System.out.println(this.processed_value);
            
            return null;
        }
        
        else if ((this.raw_type.equals("CurrentBolusWizardCarbUnits")) ||
                 (this.raw_type.equals("CurrentBolusWizardBGUnits")) ||
                 (this.raw_type.equals("CurrentReservoirWarningUnits"))
                )
        {
            
            
            this.processed_value = getDataAfter(this.raw_values, "UNITS=");
            this.processed_value = this.processed_value.replace(' ', '/');
            return null;
        }
        else if ((this.raw_type.equals("CurrentTimeDisplayFormat")) ||
                (this.raw_type.equals("CurrentTimeDisplayFormat")) ||
                (this.raw_type.equals("CurrentTimeDisplayFormat"))
               )
        {
           this.processed_value = getDataAfter(this.raw_values, "FORMAT=");
           //this.processed_value = this.processed_value.replace(' ', '/');
           return null;
        }
        else if (this.raw_type.equals("CurrentTempBasalType"))
        {
            this.processed_value = getDataAfter(this.raw_values, "TEMP_BASAL_TYPE=");
            return null;
        }
        else if (this.raw_type.equals("CurrentPumpModelNumber"))
        {
            this.processed_value = getDataAfter(this.raw_values, "MODEL_NUMBER=");
            return null;
        }
        else if (this.raw_type.equals("CurrentAutoOffDuration"))
        {
            this.processed_value = getDataAfter(this.raw_values, "DURATION=");
            return null;
        }
        else if (this.raw_type.equals("CurrentAudioBolusStep"))
        {
            
            //this.index = getDataNumber(this.raw_values, "PROGRAMMER_NUM=", "REMOTE_CONTROL_ID=");
            this.processed_value = this.raw_values.substring(1, this.raw_values.length()-2);
            this.processed_value = this.processed_value.replace(", ", ";");
            //this.processed_value = this.processed_value.substring(0, this.processed_value.length()-1);
            
            //System.out.println(this.processed_value);
            
            return null;
        }
        else if (this.raw_type.equals("CurrentBolusWizardSetupStatus"))
        {
            this.processed_value = getDataBetween(this.raw_values, "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
            //this.processed_value = this.processed_value.substring(0, this.processed_value.length()-1);
            
            //System.out.println(this.processed_value);
            
            return null;
        }
        else if (this.raw_type.equals("CurrentBGTargetRangePattern"))
        {
            //this.processed_value = getDataBetween(this.raw_values, "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
            //this.processed_value = this.processed_value.substring(0, this.processed_value.length()-1);
            
//xa            System.out.println("Missing" );
            //,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954
            return null;
        }
        else if (this.raw_type.equals("CurrentBGTargetRange"))
        {
//            this.processed_value = getDataBetween(this.raw_values, "IS_SETUP_COMPLETE=", "IS_BOLUS_WIZARD_ENABLED=");
            //this.processed_value = this.processed_value.substring(0, this.processed_value.length()-1);
//xa            System.out.println("Missing" );
            
            //System.out.println(this.processed_value);
//          CurrentBGTargetRange,"PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0",861682955,2232381,94,Paradigm 522                    
            
            return null;
        }
        
        
        
//      CurrentBGTargetRangePattern,"ORIGINAL_UNITS=mmol l, SIZE=1",861682954,2232381,93,Paradigm 522
//      CurrentBGTargetRange,"PATTERN_DATUM=861682954, INDEX=0, AMOUNT_LOW=99,088, AMOUNT_HIGH=108,096, START_TIME=0",861682955,2232381,94,Paradigm 522                    
        
        
        //CurrentBolusWizardSetupStatus,"IS_SETUP_COMPLETE=true, IS_BOLUS_WIZARD_ENABLED=true, IS_CARB_UNITS_COMPLETE=true, IS_CARB_RATIOS_COMPLETE=true, IS_BG_UNITS_COMPLETE=true, IS_INSULIN_SENSITIVITIES_COMPLETE=true, IS_BG_TARGETS_COMPLETE=true"
        //CurrentAudioBolusStep,"AMOUNT=1, ENABLE=true"
        //CurrentAutoOffDuration,DURATION=0
        // FIXME
        else
            return null;
    }
    
    
    
    
/*    
    PumpValuesEntryInterface pve_object;
    
    
    public PumpValuesEntryInterface getPumpValuesEntryObject()
    {
        return pve_object;
    }
  */  
    
    
    
    
}
