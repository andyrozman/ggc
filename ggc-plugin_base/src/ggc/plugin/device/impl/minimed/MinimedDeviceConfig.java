package ggc.plugin.device.impl.minimed;

import gnu.io.SerialPort;

import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MinimedDeviceConfig
{
    
    private static Log log = LogFactory.getLog(MinimedDeviceConfig.class);

    
    public int comm_delay_io = 4;
    public int comm_baudrate = 57600;
    public int comm_rx_buffer_size = 16384;
    public int comm_tx_buffer_size = 2048;
    public int comm_data_bits = SerialPort.DATABITS_8;
    public int comm_stop_bits = SerialPort.STOPBITS_1;
    public int comm_parity = SerialPort.PARITY_NONE;
    public int comm_flowcontrol = SerialPort.FLOWCONTROL_NONE;
    
    
    public int m_minYear;
    public int m_maxYear;
    
    /*
    public int m_settingBeepVolume;
    public int m_settingAutoOffDurationHrs;
    public double m_settingTempBasalRate;
    public int m_settingTempBasalDurationMin;
    public int m_settingInsulinConcen;
    public double m_settingMaxBasalRate;
    public double m_settingMaxBolus;
    public boolean m_settingVarBolusEnable;
    public int m_settingTimeFormat;
    public boolean m_settingAudioBolusEnable;
    public double m_settingAudioBolusSize;
    public double m_settingTodaysTotal;
    public double m_settingYesterdaysTotal;
    public int m_settingCurrentBasalPattern;
    public int m_settingAlarmMode;
    public boolean m_settingBasalPatternEnable;
    public boolean m_settingRFEnable;
    public boolean m_settingBlockEnable;
    public int m_settingBatteryStatus;
    public int m_settingRemainingInsulin;
    public int m_settingErrorStatus;
    public int m_settingPumpState;
    public String m_settingRemoteID1;
    public String m_settingRemoteID2;
    public String m_settingRemoteID3;
    public int m_tempBasalType;
    public int m_tempBasalPercent;
    public boolean m_paradigmLinkEnable;
    public int m_insulinActionType;
    public int m_lowReservoirWarnType;
    public int m_lowReservoirWarnPoint;
    public int m_keypadLockStatus;
    public int m_settingBolusScrollStepSize;
    public boolean m_settingCaptureEventEnable;
    public boolean m_settingOtherDeviceEnable;
    public boolean m_otherDeviceMarriedState;
    public int m_alarmSnoozeTime;
    public boolean m_calibrationReminderEnable;
    public int m_calibrationReminderTime;
    public boolean m_highGlucoseLimitEnable;
    public double m_highGlucoseLimitValue;
    public int m_highGlucoseSnoozeTime;
    public boolean m_lowGlucoseLimitEnable;
    public double m_lowGlucoseLimitValue;
    public int m_lowGlucoseSnoozeTime;
    public int m_missedDataTime;
    public boolean m_sensorEnable;
    public int m_sensorTransmitterId;
    public int m_sensorBgUnits;
    public double m_calibrationFactor;
    public int m_sequenceNumberInc;
    public int m_totalBytesToRead;
    public int m_bytesReadThusFar;
    //public boolean m_serialPortInitialized;
    //public boolean m_comStationInitialized;
    //public boolean m_pumpInitialized;
    public int m_currentHistoryPageNumber;
    public int m_currentGlucoseHistoryPageNumber;
    //public private Vector m_commandCollection;
*/
    
    
    
    
    
    
    public int strokes_per_basal_unit = 10;
    public int strokes_per_bolus_unit = 10;
    
    public Hashtable<String,String> settings = new Hashtable<String,String>(); 
    
    public String serial_number = null;
    public int[] serial_number_bcd = null;
    public String firmware_version = null;
    

    
    
    public void addSetting(String setting_name, String setting_value)
    {
        if (this.settings.containsKey(setting_name))
        {
            this.settings.remove(setting_name);
        }

        this.settings.put(setting_name, setting_value);
        
    }

    
    public String getSetting(String setting_name)
    {
        if (this.settings.containsKey(setting_name))
            return this.settings.get(setting_name);
        else
            return "Unset";
        
    }
    
    
    
    public void showSettings()
    {
        for(Enumeration<String> en = this.settings.keys(); en.hasMoreElements(); )
        {
            String k = en.nextElement();
            log.debug(k + "=" + this.settings.get(k));
        }
        
        
    }
    
    
}
