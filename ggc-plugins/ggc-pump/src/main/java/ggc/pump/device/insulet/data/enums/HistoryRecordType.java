package ggc.pump.device.insulet.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 20.05.15.
 */
public enum HistoryRecordType
{

    EndMarker(0x0000, "", ""), //
    Deactivate(0x0001), //
    TimeChange(0x0002), //
    Bolus(0x0004), //
    BasalRate(0x0008), //
    Suspend(0x0010), //
    DateChange(0x0020), //
    SuggestedCalc(0x0040), //
    RemoteHazardAlarm(0x0080), //
    Alarm(0x0400), //
    BloodGlucose(0x0800), //
    Carb(0x1000), //
    TerminateBolus(0x2000), //
    TerminateBasal(0x4000), //
    Activate(0x8000), //
    Resume(0x10000), //
    Download(0x20000), //
    Occlusion(0x40000), //

    Unknown(-1)

    ;

    // 256 512

    int code;

    static Map<Integer, HistoryRecordType> mapByCode = new HashMap<Integer, HistoryRecordType>();

    static
    {
        for (HistoryRecordType hrt : values())
        {
            mapByCode.put(hrt.code, hrt);
        }

    }


    HistoryRecordType(int code)
    {
        this.code = code;

    }


    HistoryRecordType(int code, String format, String fields)
    {
        this.code = code;
    }


    public static HistoryRecordType getByCode(int code)
    {
        if (mapByCode.containsKey(code))
        {
            return mapByCode.get(code);
        }

        return HistoryRecordType.Unknown;
    }

    // 0x0000: {value: 0x0000, name: 'End_Marker', format: '', fields: []},
    // 0x0001: {value: 0x0001, name: 'Deactivate', format: '', fields: []},
    // 0x0002: {
    // value: 0x0002, name: 'Time_Change', format: '3b.', fields: [
    // 'seconds',
    // 'minutes',
    // 'hours'
    // ]
    // },

    // 0x0004: {
    // value: 0x0004, name: 'Bolus', format: 'isss', fields: [
    // 'volume',
    // 'extended_duration_minutes',
    // 'calculation_record_offset',
    // 'immediate_duration_seconds'
    // ], postprocess: function (rec) {
    // rec.volume_units = toUnits(rec.volume);
    // // this occurs if an extended bolus is programmed but the bolus is
    // interrupted
    // // before any of the extended portion is delivered!
    // if (rec.extended_duration_minutes === 65535) {
    // rec.extended_duration_msec = 0;
    // }
    // else {
    // // a zero *here* means that this isn't an extended bolus
    // // and we need to distinguish between two types of zeros -
    // // dual-wave boluses interrupted before any of the extended was
    // // delivered (i.e., the case above ^) or non-extended boluses (here)
    // // so here we use null instead of 0
    // if (rec.extended_duration_minutes === 0) {
    // rec.extended_duration_msec = null;
    // }
    // else {
    // rec.extended_duration_msec = rec.extended_duration_minutes *
    // sundial.MIN_TO_MSEC;
    // }
    // }
    // rec.immediate_duration_msec = rec.immediate_duration_seconds *
    // sundial.SEC_TO_MSEC;
    // }
    // },
    // 0x0008: {
    // value: 0x0008, name: 'Basal_Rate', format: 'ish', fields: [
    // 'basal_rate', 'duration', 'percent'
    // ], postprocess: function (rec) {
    // rec.basal_rate_units_per_hour = toUnits(rec.basal_rate);
    // rec.duration_msec = rec.duration * sundial.MIN_TO_MSEC;
    // rec.temp_basal_percent = convertTempPercentageToNetBasal(rec.percent);
    // }
    // },
    // 0x0010: {value: 0x0010, name: 'Suspend', format: '', fields: []},
    // 0x0020: {
    // value: 0x0020, name: 'Date_Change', format: 'bbs', fields: [
    // 'day',
    // 'month',
    // 'year'
    // ]
    // },
    // 0x0040: {
    // value: 0x0040, name: 'Suggested_Calc', format: '4in3i6s', fields: [
    // 'correction_delivered', 'carb_bolus_delivered',
    // 'correction_programmed', 'carb_bolus_programmed',
    // 3333 'correction_suggested', 'carb_bolus_suggested',
    // 'correction_iob', 'meal_iob',
    // 'correction_factor_used', 'current_bg',
    // 'target_bg', 'bg_correction_threshold',
    // 'carb_grams', 'ic_ratio_used'
    // ],
    // postprocess: function (rec) {
    // rec.corr_units_delivered = toUnits(rec.correction_delivered);
    // rec.carb_bolus_units_delivered = toUnits(rec.carb_bolus_delivered);
    // rec.corr_units_programmed = toUnits(rec.correction_programmed);
    // rec.carb_bolus_units_programmed = toUnits(rec.carb_bolus_programmed);
    // rec.corr_units_suggested = toUnits(rec.correction_suggested);
    // rec.carb_bolus_units_suggested = toUnits(rec.carb_bolus_suggested);
    // rec.corr_units_iob = toUnits(rec.correction_iob);
    // rec.meal_units_iob = toUnits(rec.meal_iob);
    // }
    // },
    // 0x0080: {
    // value: 0x0080, name: 'Remote_Hazard_Alarm', format: '2bs3b.4s', fields: [
    // 'day', 'month', 'year', 'seconds', 'minutes', 'hours',
    // 'alarm_type', 'file_number', 'line_number', 'error_code'
    // ]
    // },
    // 0x0400: {
    // value: 0x0400, name: 'Alarm', format: '2bs3b.4s', fields: [
    // 'day', 'month', 'year', 'seconds', 'minutes', 'hours',
    // 'alarm_type', 'file_number', 'line_number', 'error_code'
    // ]
    // },
    // 0x0800: {
    // value: 0x0800, name: 'Blood_Glucose', format: 'is24z24zb.', fields: [
    // 'error_code', 'bg_reading',
    // 'user_tag_1', 'user_tag_2',
    // 'flags'
    // ]
    // },
    // 0x1000: {
    // value: 0x1000, name: 'Carb', format: 'sbb', fields: [
    // 'carbs',
    // 'was_preset',
    // 'preset_type'
    // ]
    // },
    // 0x2000: {
    // value: 0x2000, name: 'Terminate_Bolus', format: 'is', fields: [
    // 'insulin_left',
    // 'time_left_minutes'
    // ], postprocess: function (rec) {
    // rec.insulin_units_left = toUnits(rec.insulin_left);
    // rec.time_left_msec = rec.time_left_minutes * sundial.MIN_TO_MSEC;
    // }
    // },
    // 0x4000: {
    // value: 0x4000, name: 'Terminate_Basal', format: 's', fields: [
    // 'time_left_minutes'
    // ], postprocess: function (rec) {
    // rec.time_left_msec = rec.time_left_minutes * sundial.MIN_TO_MSEC;
    // }
    // },
    // 0x8000: {
    // value: 0x8000, name: 'Activate', format: '2S6b', fields: [
    // 'lot_number', 'serial_number',
    // 'pod_maj', 'pod_min', 'pod_patch',
    // 'interlock_maj', 'interlock_min', 'interlock_patch'
    // ]
    // },
    // 0x10000: {value: 0x10000, name: 'Resume', format: '', fields: []},
    // 0x20000: {value: 0x20000, name: 'Download', format: '', fields: []},
    // 0x40000: {value: 0x40000, name: 'Occlusion', format: '', fields: []}

}
