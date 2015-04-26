package ggc.pump.data.writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpWriterValues;
import ggc.pump.data.defs.*;

/**
 * Created by andy on 08.03.15.
 */

// This should be used for all writing of data, not like till now new instances
// of DeviceValuesWriter for each
// reading. If device has special values which can be used instead of this, we
// can using intermediary mapping
// map created.

public class PumpValuesWriter extends DeviceValuesWriter
{

    private static final Log LOG = LogFactory.getLog(PumpValuesWriter.class);
    static PumpValuesWriter staticInstance;


    private PumpValuesWriter()
    {
        super();
        loadDeviceDefinitions();
    }


    public static PumpValuesWriter getInstance(OutputWriter writer)
    {
        if (staticInstance == null)
        {
            staticInstance = new PumpValuesWriter();
        }

        staticInstance.setOutputWriter(writer);

        return staticInstance;
    }


    private void loadDeviceDefinitions()
    {

        // ========= Events =========

        // FIXME missing
        addConfiguration("Event_Basal_Stop", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalStop, false));

        addConfiguration("Event_Basal_Run", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalRun, false));

        addConfiguration("Event_PrimeInfusionSet", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.PrimeInfusionSet, true));

        addConfiguration("Event_FillCannula", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.FillCannula, true));

        addConfiguration("Event_BolusWizard", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BolusWizard, false));

        addConfiguration("Event_BatteryLow", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BatteryLow, false));

        // addConfiguration("Event_BolusWizard", new
        // PumpTempValues(PumpTempValues.OBJECT_BASE, //
        // PumpBaseType.Event, PumpEvents.BolusWizard, false));
        // addConfiguration("Event_BolusWizard", new
        // PumpTempValues(PumpTempValues.OBJECT_BASE, //
        // PumpBaseType.Event, PumpEvents.BolusWizard, false));

        // CartridgeChange(2, "EVENT_CARTRIDGE_CHANGED"), //
        // CartridgeRewind(3, "EVENT_REWIND_INFUSION_SET"), //
        // ReservoirLow(4, "EVENT_RESERVOIR_LOW"), //
        // ReservoirLowDesc(5, "EVENT_RESERVOIR_LOW_DESC"), //
        // SetTemporaryBasalRateType(10, "EVENT_SET_TEMPORARY_BASAL_RATE_TYPE"),
        // // Unit setting (1=%, 0=U)
        // SetBasalPattern(15, "EVENT_SET_BASAL_PATTERN"), //
        // PowerDown(22, "EVENT_POWER_DOWN"), //
        // PowerUp(23, "EVENT_POWER_UP"), //
        // SelfTest(30, "EVENT_SELF_TEST"), //
        // Download(31, "EVENT_DOWNLOAD"), //
        // DateTimeSet(40, "EVENT_DATETIME_SET"), //
        // DateTimeCorrect(41, "EVENT_DATETIME_CORRECT"), //
        // SetMaxBasal(50, "EVENT_SET_MAX_BASAL"), //
        // SetMaxBolus(51, "EVENT_SET_MAX_BOLUS"), //
        // BatteryRemoved(55, "EVENT_BATERRY_REMOVED"), //
        // BatteryReplaced(56, "EVENT_BATERRY_REPLACED"), //
        // BatteryLow(57, "EVENT_BATERRY_LOW"), //
        // BatteryLowDesc(58, "EVENT_BATERRY_LOW_DESC"), //
        // BgFromMeter(70, "EVENT_BG_FROM_METER"), //
        // BolusCancelled(80, "ALARM_BOLUS_CANCELED"), //

        // ========= Alarms =========

        // FIXME rename in classes using it
        addConfiguration("Alarm_Call_Service", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.CallService, false));

        addConfiguration("Alarm_Replace_Battery", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.BatteryReplace, false));

        addConfiguration("Alarm_Empty_Cartridge", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.EmptyCartridge, false));

        addConfiguration("Alarm_Auto_Off", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.AutoOff, false));

        addConfiguration("Alarm_Low_Battery", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.BatteryLow, false));

        addConfiguration("Alarm_Low_Cartridge", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.CartridgeLow, false));

        addConfiguration("Alarm_Unknown", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.UnknownAlarm, false));

        addConfiguration("Alarm_Occlusion_Detected", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Alarm, PumpAlarms.NoDelivery, false));

        for (PumpAlarms alarm : PumpAlarms.getAllValues())
        {
            addConfigurationBase("Alarm_" + alarm.name(), PumpBaseType.Alarm, alarm, false);
        }

        // ========= Report =========

        addConfiguration("Report_All_Daily_Insulin", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Report, PumpReport.InsulinTotalDay, true));

        addConfiguration("Report_Daily_Basal_Insulin", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Report, PumpReport.BasalTotalDay, true));

        // ========= Bolus =========

        addConfiguration("Bolus_Normal", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Normal, true));

        addConfiguration("Bolus_Audio", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Audio, true));

        addConfiguration("Bolus_Extended", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Extended, false));

        addConfiguration("Bolus_Multiwave", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Bolus, PumpBolusType.Multiwave, false));

        // ========= Basal =========

        addConfiguration("Basal_Value_Change", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Basal, PumpBasalType.ValueChange, true));

        // ========= Additional Data =========

        addConfiguration("AdditionalData_BG", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, //
                PumpAdditionalDataType.BloodGlucose, true));

        addConfiguration("AdditionalData_CH", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, //
                PumpAdditionalDataType.Carbohydrates, true));

    }


    public void addConfigurationBase(String key, PumpBaseType baseType, CodeEnumWithTranslation subType,
            boolean isNumericValue)
    {
        if (!this.containsKey(key))
        {
            addConfiguration(key, new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                    baseType, subType, isNumericValue));
        }
        else
        {
            LOG.warn("Key " + key + " already added in PumpValuesWriter. Please check your configuration.");
        }
    }

}
