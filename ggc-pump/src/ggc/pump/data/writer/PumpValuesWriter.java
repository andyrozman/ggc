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

        for (PumpEvents event : PumpEvents.getAllValues())
        {
            addConfigurationBase("Event_" + event.name(), PumpBaseType.Event, event, false);
        }

        // FIXME missing
        addConfiguration("Event_PrimeInfusionSet", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.PrimeInfusionSet, true));

        addConfiguration("Event_FillCannula", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.FillCannula, true));

        addConfiguration("Event_BolusWizard", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BolusWizard, false));

        addConfiguration("Event_BatteryLow", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BatteryLow, false));

        addConfiguration("Event_BasalStop", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalStop, false));

        addConfiguration("Event_BasalRun", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.BasalRun, false));

        addConfiguration("Event_ReservoirLowDesc", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Event, PumpEvents.ReservoirLowDesc, true));

        // ========= Alarms =========

        for (PumpAlarms alarm : PumpAlarms.getAllValues())
        {
            addConfigurationBase("Alarm_" + alarm.name(), PumpBaseType.Alarm, alarm, false);
        }

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

        // ========= Error =========

        for (PumpErrors error : PumpErrors.getAllValues())
        {
            addConfigurationBase("Error_" + error.name(), PumpBaseType.Error, error, false);
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

        addConfiguration("Basal_ValueChange", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Basal, PumpBasalType.ValueChange, true));

        addConfiguration("Basal_TemporaryBasalRate", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Basal, PumpBasalType.TemporaryBasalRate, false));

        addConfiguration("Basal_TemporaryBasalRateEnded", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Basal, PumpBasalType.TemporaryBasalRateEnded, true));

        addConfiguration("Basal_TemporaryBasalRateCanceled", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                PumpBaseType.Basal, PumpBasalType.TemporaryBasalRateCanceled, false));

        // ========= Additional Data =========

        addConfiguration("AdditionalData_BG", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, //
                PumpAdditionalDataType.BloodGlucose, true));

        addConfiguration("AdditionalData_CH", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, //
                PumpAdditionalDataType.Carbohydrates, true));

    }


    public void addConfigurationBase(String key, PumpBaseType baseType, CodeEnumWithTranslation subType,
            boolean isNumericValue)
    {

        if (this.containsKey(key))
        {
            remove(key);
        }

        addConfiguration(key, new PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
                baseType, subType, isNumericValue));

        // if (!this.containsKey(key))
        // {
        // addConfiguration(key, new
        // PumpWriterValues(PumpWriterValues.OBJECT_BASE, //
        // baseType, subType, isNumericValue));
        // }
        // else
        // {
        // LOG.warn("Key " + key +
        // " already added in PumpValuesWriter. Please check your configuration.");
        // }
    }

}
