package ggc.pump.device.minimed;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedCareLinkData
{
    //DataAccessCGM m_da = DataAccessCGM.getInstance();
  
    private String index;           // 0
    private String date;            // 1
    private String time;            // 2 
    private String raw_type;        // 33
    private String raw_values;      // 34
    @SuppressWarnings("unused")
    private String raw_id;          // 35
    @SuppressWarnings("unused")
    private String raw_upload_id;   // 36
    @SuppressWarnings("unused")
    private String raw_seq_num;     // 37
    private String raw_device_type; // 38
    
    
//  Index,Date,Time,Timestamp,New Device Time,BG Reading (mmol/L),Linked BG Meter ID,Temp Basal Amount (U/h),
//  Temp Basal Type,Temp Basal Duration (hh:mm:ss),Bolus Type,Bolus Volume Selected (U),Bolus Volume Delivered (U),
//  Programmed Bolus Duration (hh:mm:ss),Prime Type,Prime Volume Delivered (U),Suspend,Rewind,BWZ Estimate (U),
//  BWZ Target High BG (mmol/L),BWZ Target Low BG (mmol/L),BWZ Carb Ratio (grams),BWZ Insulin Sensitivity (mmol/L),
//  BWZ Carb Input (grams),BWZ BG Input (mmol/L),BWZ Correction Estimate (U),BWZ Food Estimate (U),
//  BWZ Active Insulin (U),Alarm,Sensor Calibration BG (mmol/L),Sensor Glucose (mmol/L),ISIG Value,
//  Daily Insulin Total (U),Raw-Type,Raw-Values,Raw-ID,Raw-Upload ID,Raw-Seq Num,Raw-Device Type

    public MinimedCareLinkData(String[] data)
    {
        this.index = data[0];
        this.date = data[1];
        this.time = data[2];

        this.raw_type = data[33];        // 33
        this.raw_values = data[34];      // 34
        this.raw_id = data[35];          // 35
        this.raw_upload_id = data[36];   // 36
        this.raw_seq_num = data[37];     // 37
        this.raw_device_type = data[38]; // 38
    }
    
    
    public boolean isIdentified()
    {
        
        if (
            // BG data    
            this.raw_type.equals("BGReceived") ||           // bg data from meter
            this.raw_type.equals("ChangeParadigmLinkID") ||           // bg data from meter
                
            // pump
            this.raw_type.equals("ClearAlarm") ||  // clear alarm
            this.raw_type.equals("ChangeSuspendEnable") ||  // suspend
            this.raw_type.equals("Prime") ||
            this.raw_type.equals("Rewind") ||
            this.raw_type.equals("AlarmPump") ||           // alarm
                
            // bolus
            this.raw_type.equals("BolusNormal") ||      // bolus
            this.raw_type.equals("BolusSquare") ||

            this.raw_type.equals("BolusWizardBolusEstimate") ||  // bolus wiyard estimate
            this.raw_type.equals("ChangeBolusWizardSetupConfig") ||
            this.raw_type.equals("ChangeBolusWizardSetup") ||
            
            // basal
            this.raw_type.equals("ChangeTempBasalPercent") || 
            this.raw_type.equals("ChangeActiveBasalProfilePattern") || 
            this.raw_type.equals("ChangeBasalProfilePatternPre") || 
            this.raw_type.equals("ChangeBasalProfilePre") || 
            this.raw_type.equals("ChangeBasalProfilePattern") ||
            
            
            
            
            
            
            // config pump
            this.raw_type.equals("ChangeBatteryEnable") || 
            this.raw_type.equals("ChangeBatteryEnableGH") || 
            this.raw_type.equals("ChangeTimeDisplayFormat") || 
            this.raw_type.equals("ChangeMaxBasal") || 
            
            
            
            
            
            this.raw_type.equals("ChangeTimeGH") ||          // unknown
            this.raw_type.equals("ChangeTime") ||          // unknown
            this.raw_type.equals("CalBGForGH") ||          // unknown
            this.raw_type.equals("CalBGForPH") ||          // unknown
            this.raw_type.equals("SelfTest") ||

            
            // sensor
            this.raw_type.equals("SensorSync") ||             // unknown
            this.raw_type.equals("SensorCalFactor") ||     // unknown
            this.raw_type.equals("SensorCal") ||           // unknown
            this.raw_type.equals("SensorStatus") ||        // status - unknown
            this.raw_type.equals("SensorTimestamp") ||     // unknown
            this.raw_type.equals("SensorPacket") ||        // unknown 
            this.raw_type.equals("SensorError") ||         // unknown
            this.raw_type.equals("AlarmSensor") ||          // alarm for sensor
            this.raw_type.equals("GlucoseSensorData") ||           // data
            this.raw_type.equals("GlucoseSensorDataLow") ||        // data low
            this.raw_type.equals("SensorWeakSignal") ||     // warning - sensor weak signal

            
            // config sensor
            this.raw_type.equals("ChangeSensorGlucoseLimitProfile") ||  // config
            this.raw_type.equals("ChangeSensorAlarmSilenceConfig") ||
            this.raw_type.equals("ChangeSensorGlucoseLimitPattern") ||
            this.raw_type.equals("ChangeSensorGlucoseLimitPatternSetup") ||
            this.raw_type.equals("ChangeSensorSetup") ||
            this.raw_type.equals("ChangeSensorSetupConfig") ||
            
            
            // journal
            this.raw_type.equals("JournalEntryPumpLowBattery") ||
            this.raw_type.equals("JournalEntryPumpLowReservoir") ||
            
            // report
            this.raw_type.equals("ResultDailyTotal") ||    // report - result daily total
            
            this.raw_type.equals("UnabsorbedInsulin") ||    // unabsorbed insulin
            
            this.raw_type.equals("ChangeCarbRatio") ||      // change carb ratio
            this.raw_type.equals("ChangeCarbRatioPattern") ||
            
            
            this.raw_type.equals("ChangeBasalProfile") ||   // change basal
            
            this.raw_type.equals("ChangeBGTargetRange") ||  // target range
            this.raw_type.equals("ChangeBGTargetRangePattern") ||
            
            this.raw_type.equals("ChangeInsulinSensitivity") ||  // change insulin sensitivity
            this.raw_type.equals("ChangeInsulinSensitivityPattern") || //
            
            // current 
            
            this.raw_type.equals("CurrentAlarmNotifyMode") ||
            this.raw_type.equals("CurrentRFEnable") ||
            this.raw_type.equals("CurrentTotalYesterday") ||
            this.raw_type.equals("CurrentBatteryStatus") ||
            this.raw_type.equals("CurrentTotalRemaining") ||
            this.raw_type.equals("CurrentErrorStatus") ||
            this.raw_type.equals("CurrentDisplayLanguage") ||
            this.raw_type.equals("CurrentGlucoseHistoryPageNumber") ||
            this.raw_type.equals("CurrentTimeDisplayFormat") ||
            this.raw_type.equals("CurrentAudioBolusStep") ||
            this.raw_type.equals("CurrentVariableBolusEnable") ||
            this.raw_type.equals("CurrentAutoOffDuration") ||
            this.raw_type.equals("CurrentTotalToday") ||
            this.raw_type.equals("CurrentTempBasal") ||
            this.raw_type.equals("CurrentMaxBasal") ||
            this.raw_type.equals("CurrentMaxBolus") ||
            this.raw_type.equals("CurrentActiveBasalProfilePattern") ||
            this.raw_type.equals("CurrentVariableBasalProfilePatternEnable") ||
            this.raw_type.equals("CurrentChildBlockEnable") ||
            
            this.raw_type.equals("CurrentSensorCalFactor") ||
            this.raw_type.equals("CurrentSensorCalReminderEnable") ||
            this.raw_type.equals("CurrentSensorHighGlucoseAlarmEnable") ||
            this.raw_type.equals("CurrentSensorLowGlucoseAlarmEnable") ||
            this.raw_type.equals("CurrentSensorEnable") ||
            this.raw_type.equals("CurrentSensorHighGlucoseLimit") ||
            this.raw_type.equals("CurrentSensorLowGlucoseLimit") ||
            this.raw_type.equals("CurrentSensorCalReminderTime") ||
            this.raw_type.equals("CurrentSensorAlarmSnoozeTime") ||
            this.raw_type.equals("CurrentSensorHighGlucoseSnoozeTime") ||
            this.raw_type.equals("CurrentSensorLowGlucoseSnoozeTime") ||
            this.raw_type.equals("CurrentSensorMissedDataTime") ||
            this.raw_type.equals("CurrentSensorTransmitterID") ||
            this.raw_type.equals("CurrentSensorBGUnits") ||
            
            this.raw_type.equals("CurrentParadigmLinkID") ||
            this.raw_type.equals("CurrentBolusWizardSetupStatus") ||
            this.raw_type.equals("CurrentBGTargetRangePattern") ||
            this.raw_type.equals("CurrentBGTargetRange") ||
            this.raw_type.equals("CurrentReservoirWarningUnits") ||
            this.raw_type.equals("CurrentReservoirWarningValueInsulin") ||
            this.raw_type.equals("CurrentKeypadLockedEnable") ||
            this.raw_type.equals("CurrentBolusReminderEnable") ||
            this.raw_type.equals("CurrentSavedSettingsTime") ||
            this.raw_type.equals("CurrentHistoryPageNumber") ||
            
            this.raw_type.equals("CurrentRemoteControlID") ||
            this.raw_type.equals("CurrentPumpStatus") ||
            this.raw_type.equals("CurrentTempBasalType") ||
            this.raw_type.equals("CurrentParadigmLinkEnable") ||
            this.raw_type.equals("CurrentInsulinActionCurve") ||
            this.raw_type.equals("CurrentPumpModelNumber") ||
            this.raw_type.equals("CurrentAlarmClockEnable") ||
            this.raw_type.equals("CurrentBGReminderEnable") ||
            this.raw_type.equals("CurrentBolusWizardEnable") ||
            this.raw_type.equals("CurrentBolusWizardBGUnits") ||
            this.raw_type.equals("CurrentBolusWizardCarbUnits") ||
            
            this.raw_type.equals("CurrentCarbRatioPattern") ||
            this.raw_type.equals("CurrentCarbRatio") ||
            
            this.raw_type.equals("CurrentInsulinSensitivityPattern") ||
            this.raw_type.equals("CurrentInsulinSensitivity") ||
            
            this.raw_type.equals("CurrentBasalProfile") ||
            this.raw_type.equals("CurrentBasalProfilePattern") 
                
          )
            return true;
        else
            return false;
        
        
        
        
        /*
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
    
    public String getRawType()
    {
        return this.raw_type;
    }
    
//    System.out.println(count + ": [size=" + ld.length + ",id=" + ld[0] + ",el33=" + ld[33] + "]");

    public String toString()
    {
        return "MinimedCareLinkData [index=" + this.index + "date=" + this.date + ",time=" + this.time + ",raw_type=" + this.raw_type +
               ",raw_values=" + this.raw_values + ",raw_device_type=" + this.raw_device_type + "]"; 
//               "\nraw_id=" + this.raw_id + ",raw_upload_id=" + this.raw_upload_id + ",raw_seq_num=" + this.raw_seq_num;
    }
    

}