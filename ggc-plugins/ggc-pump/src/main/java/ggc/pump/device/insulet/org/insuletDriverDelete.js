module.exports = function (config) {
    var cfg = _.clone(config);
    var buf;
    var bytes;


    // This is a particularly weak checksum algorithm but that's what Insulet uses...
    var weakChecksum = function (offset, count) {
        var total = 0;
        for (var i = 0; i < count; ++i) {
            total += bytes[offset + i];
        }
        return total & 0xFFFF;
    };


    // postprocess is a function that accepts a record, optionally modifies it,
    // and returns null if the record is good, and a message if processing should halt.
    var fixedRecords = {
        log_record: {
            format: 'bNSSbbsbbb.i', fields: [
                'log_id', 'log_index', 'record_size', 'error_code',
                'day', 'month', 'year', 'seconds', 'minutes', 'hours',
                'secs_since_powerup'
            ]
        },
        history_record: {
            format: 'bNSSbbsbbb.ins..', fields: [
                'log_id', 'log_index', 'record_size', 'error_code',
                'day', 'month', 'year', 'seconds', 'minutes', 'hours',
                'secs_since_powerup', 'rectype', 'flags'
            ]
        }
    };

    var logRecords = {

        0x0008: {
            value: 0x0008, name: 'Basal_Rate', format: 'ish', fields: [
                'basal_rate', 'duration', 'percent'
            ], postprocess: function (rec) {
                rec.basal_rate_units_per_hour = toUnits(rec.basal_rate);
                rec.duration_msec = rec.duration * sundial.MIN_TO_MSEC;
                rec.temp_basal_percent = convertTempPercentageToNetBasal(rec.percent);
            }
        },
        0x0040: {
            value: 0x0040, name: 'Suggested_Calc', format: '4in3i6s', fields: [
                'correction_delivered', 'carb_bolus_delivered',
                'correction_programmed', 'carb_bolus_programmed',
                'correction_suggested', 'carb_bolus_suggested',
                'correction_iob', 'meal_iob',
                'correction_factor_used', 'current_bg',
                'target_bg', 'bg_correction_threshold',
                'carb_grams', 'ic_ratio_used'
            ],
            postprocess: function (rec) {
                rec.corr_units_delivered = toUnits(rec.correction_delivered);
                rec.carb_bolus_units_delivered = toUnits(rec.carb_bolus_delivered);
                rec.corr_units_programmed = toUnits(rec.correction_programmed);
                rec.carb_bolus_units_programmed = toUnits(rec.carb_bolus_programmed);
                rec.corr_units_suggested = toUnits(rec.correction_suggested);
                rec.carb_bolus_units_suggested = toUnits(rec.carb_bolus_suggested);
                rec.corr_units_iob = toUnits(rec.correction_iob);
                rec.meal_units_iob = toUnits(rec.meal_iob);
            }
        },
        0x0080: {
            value: 0x0080, name: 'Remote_Hazard_Alarm', format: '2bs3b.4s', fields: [
                'day', 'month', 'year', 'seconds', 'minutes', 'hours',
                'alarm_type', 'file_number', 'line_number', 'error_code'
            ]
        },
        0x0400: {
            value: 0x0400, name: 'Alarm', format: '2bs3b.4s', fields: [
                'day', 'month', 'year', 'seconds', 'minutes', 'hours',
                'alarm_type', 'file_number', 'line_number', 'error_code'
            ]
        },
    };


    var pump_alarm_record = {
        format: '2bs3b.s.b2i6b', fields: [
            'day', 'month', 'year', 'seconds', 'minutes', 'hours',
            'alarm', 'error_code', 'lot_number', 'seq_number',
            'processor_maj', 'processor_min', 'processor_patch',
            'interlock_maj', 'interlock_min', 'interlock_patch'
        ]
    };


    var LOG_FLAGS = {
        CARRY_OVER_FLAG: {value: 0x01, name: 'CARRY_OVER_FLAG'},
        NEW_DAY_FLAG: {value: 0x02, name: 'NEW_DAY_FLAG'},
        IN_PROGRESS_FLAG: {value: 0x04, name: 'IN_PROGRESS_FLAG'},
        END_DAY_FLAG: {value: 0x08, name: 'END_DAY_FLAG'},
        UNCOMFIRMED_FLAG: {value: 0x10, name: 'UNCOMFIRMED_FLAG'},
        REVERSE_CORR_FLAG: {value: 0x0100, name: 'REVERSE_CORR_FLAG'},
        MAX_BOLUS_FLAG: {value: 0x0200, name: 'MAX_BOLUS_FLAG'},
        ERROR: {value: 0x80000000, name: 'ERROR'}
    };


    var LOG_ERRORS = {
        eLogNoErr: {value: 0, name: 'eLogNoErr'},
        eLogGetEEPROMErr: {value: 3, name: 'eLogGetEEPROMErr'},
        eLogCRCErr: {value: 4, name: 'eLogCRCErr'},
        eLogLogIndexErr: {value: 6, name: 'eLogLogIndexErr'},
        eLogRecSizeErr: {value: 8, name: 'eLogRecSizeErr'}
    };


    var getLogRecord = function (offset) {
        var rec = getRecord(offset);
        var logheader = struct.unpack(rec.rawdata, 0,
            fixedRecords.log_record.format,
            fixedRecords.log_record.fields);
        if (logheader.log_id == LOG_TYPES.HISTORY.value) { // history
            logheader = struct.unpack(rec.rawdata, 0,
                fixedRecords.history_record.format,
                fixedRecords.history_record.fields);
            if (logheader.error_code) {
                logheader.error_text = getNameForValue(LOG_ERRORS, logheader.error_code);
            }
            if (logheader.flags !== 0) {
                logheader.flag_text = getFlagNames(LOG_FLAGS, logheader.flags);
            }
        } else {
            // There are other record types but we don't have documentation on them,
            // so we're going to ignore them.
        }
        _.assign(rec, logheader);
        if (rec.rectype & LOG_TYPES.DELETED.mask) {
            // this is a deleted record so we're going to only return
            // a deleted flag and a size
            return {rectype: LOG_TYPES.IGNORE.value, packetlen: rec.packetlen};
        }
        // now process further data, if there is any
        if (rec.log_id == LOG_TYPES.HISTORY.value) {
            if (logRecords[rec.rectype]) {
                if (rec.rectype !== 0) {
                    addTimestamp(rec);
                }
                rec.rectype_name = logRecords[rec.rectype].name;
                var detail = struct.unpack(rec.rawdata,
                    struct.structlen(fixedRecords.history_record.format),
                    logRecords[rec.rectype].format,
                    logRecords[rec.rectype].fields);
                if (logRecords[rec.rectype].postprocess) {
                    logRecords[rec.rectype].postprocess(detail);
                }
                rec.detail = detail;
            } else {
                debug('Unknown history record type %d', rec.rectype);
            }
        } else if (rec.log_id == LOG_TYPES.PUMP_ALARM.value) {
            rec.alarm = struct.unpack(rec.rawdata,
                struct.structlen(fixedRecords.log_record.format),
                pump_alarm_record.format,
                pump_alarm_record.fields);
            addTimestamp(rec.alarm);
            rec.alarm.alarm_text = getNameForValue(ALARM_TYPES, rec.alarm.alarm);
        } else {
            // all other log types are meaningless to us, we're told
            return {rectype: LOG_TYPES.IGNORE.value, packetlen: rec.packetlen};
        }
        return rec;
    };


    // these aren't history records, so we have to find them separately
    var findPumpAlarmRecords = function (recordlist) {
        var result = [];
        for (var i = 0; i < recordlist.length; ++i) {
            // log_index of -1 doesn't have a timestamp, not a valid record
            if (recordlist[i].alarm != null && recordlist[i].log_index >= 0) {
                result.push(i);
            }
        }
        return result.sort(function compare(a, b) {
            return recordlist[a].log_index - recordlist[b].log_index;
        });
    };


    var buildAlarmRecords = function (data, records) {
        var alarmrecs = findSpecificRecords(data.log_records, [
            getValueForName(logRecords, 'Remote_Hazard_Alarm'),
            getValueForName(logRecords, 'Alarm')
        ]);
        var pumpAlarms = findPumpAlarmRecords(data.log_records);
        alarmrecs = alarmrecs.concat(pumpAlarms);
        var postrecords = [];

        function makeSuspended(anAlarm) {
            var suspend = cfg.builder.makeDeviceMetaSuspend()
                .with_time(anAlarm.timestamp || alarm.alarm.timestamp)
                .with_deviceTime(anAlarm.deviceTime || alarm.alarm.deviceTime)
                .with_timezoneOffset(anAlarm.timezoneOffset || alarm.alarm.timezoneOffset)
                .with_reason({suspended: 'automatic'});
            if (anAlarm.detail) {
                suspend.set('index', anAlarm.log_index);
            }
            return suspend.done();
        }

        function makeSuspendBasal(anAlarm) {
            // we don't call .done() on the basal b/c it still needs duration added
            // which happens in the simulator
            var basal = cfg.builder.makeSuspendBasal()
                .with_time(anAlarm.timestamp || alarm.alarm.timestamp)
                .with_deviceTime(anAlarm.deviceTime || alarm.alarm.deviceTime)
                .with_timezoneOffset(anAlarm.timezoneOffset || alarm.alarm.timezoneOffset);
            if (anAlarm.detail) {
                basal.set('index', anAlarm.log_index);
            }
            return basal;
        }

        for (var a = 0; a < alarmrecs.length; ++a) {
            var alarm = data.log_records[alarmrecs[a]];
            var postalarm = null, postsuspend = null, postbasal = null;
            var alarmValue = null;

            postalarm = cfg.builder.makeDeviceMetaAlarm()
                .with_time(alarm.timestamp || alarm.alarm.timestamp)
                .with_deviceTime(alarm.deviceTime || alarm.alarm.deviceTime)
                .with_timezoneOffset(alarm.timezoneOffset || alarm.alarm.timezoneOffset);

            // handle history-style alarms
            if (alarm.detail) {
                alarmValue = alarm.detail.alarm_type;
                postalarm.set('index', alarm.log_index);
            }
            // handle non-history alarms
            else {
                // alarm.alarm.alarm is not a typo!
                if (alarm.alarm.alarm_text != null) {
                    alarmValue = alarm.alarm.alarm;
                }
                else {
                    postalarm = null;
                }
            }
            var alarmText = getNameForValue(ALARM_TYPES, alarmValue);
            switch (alarmValue) {
                // alarmType `other`
                // History - ALARM
                case ALARM_TYPES.AlrmADV_KEY.value:
                case ALARM_TYPES.AlrmEXP_WARNING.value:
                case ALARM_TYPES.AlrmSYSTEM_ERROR10.value:
                case ALARM_TYPES.AlrmSYSTEM_ERROR12.value:
                case ALARM_TYPES.AlrmSYSTEM_ERROR28.value:
                case ALARM_TYPES.AlrmPDM_ERROR0.value:
                case ALARM_TYPES.AlrmPDM_ERROR1.value:
                case ALARM_TYPES.AlrmPDM_ERROR2.value:
                case ALARM_TYPES.AlrmPDM_ERROR3.value:
                case ALARM_TYPES.AlrmPDM_ERROR4.value:
                case ALARM_TYPES.AlrmPDM_ERROR5.value:
                case ALARM_TYPES.AlrmPDM_ERROR6.value:
                case ALARM_TYPES.AlrmPDM_ERROR7.value:
                case ALARM_TYPES.AlrmPDM_ERROR8.value:
                case ALARM_TYPES.AlrmPDM_ERROR9.value:
                // History - REMOTE HAZ
                case ALARM_TYPES.AlrmHAZ_REMOTE.value:
                case ALARM_TYPES.AlrmHAZ_PUMP_ACTIVATE.value:
                case ALARM_TYPES.AlrmADV_PUMP_AUTO_OFF.value:
                case ALARM_TYPES.AlrmADV_PUMP_SUSPEND.value:
                case ALARM_TYPES.AlrmADV_PUMP_EXP1.value:
                case ALARM_TYPES.AlrmADV_PUMP_EXP2.value:
                    postalarm = postalarm.with_alarmType('other')
                        .with_payload({
                            alarmText: alarmText,
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .done();
                    break;
                // Pump advisory and hazard alarm (non-History)
                // alarmType `low_insulin`
                case ALARM_TYPES.AlrmADV_PUMP_VOL.value:
                    postalarm = postalarm.with_alarmType('low_insulin')
                        .with_payload({
                            alarmText: alarmText,
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .done();
                    break;
                // alarmType `no_insulin`
                case ALARM_TYPES.AlrmHAZ_PUMP_VOL.value:
                    postsuspend = makeSuspended(alarm);
                    postbasal = makeSuspendBasal(alarm);
                    postalarm = postalarm.with_alarmType('no_insulin')
                        .with_payload({
                            alarmText: getNameForValue(ALARM_TYPES, alarmValue),
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .with_status(postsuspend)
                        .done();
                    break;
                // alarmType `occlusion`
                case ALARM_TYPES.AlrmHAZ_PUMP_OCCL.value:
                    postsuspend = makeSuspended(alarm);
                    postbasal = makeSuspendBasal(alarm);
                    postalarm = postalarm.with_alarmType('occlusion')
                        .with_payload({
                            alarmText: getNameForValue(ALARM_TYPES, alarmValue),
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .with_status(postsuspend)
                        .done();
                    break;
                // alarmType `no_delivery`
                case ALARM_TYPES.AlrmHAZ_PUMP_EXPIRED.value:
                    postsuspend = makeSuspended(alarm);
                    postbasal = makeSuspendBasal(alarm);
                    postalarm = postalarm.with_alarmType('no_delivery')
                        .with_payload({
                            alarmText: getNameForValue(ALARM_TYPES, alarmValue),
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .with_status(postsuspend)
                        .done();
                    break;
                // alarmType `auto_off`
                case ALARM_TYPES.AlrmHAZ_PDM_AUTO_OFF.value:
                // TODO: clarify with Insulet or get data to figure out whether this (below) is
                // a warning or the actual auto-off; the spec is confused
                case ALARM_TYPES.AlrmHAZ_PUMP_AUTO_OFF.value:
                    postsuspend = makeSuspended(alarm);
                    postbasal = makeSuspendBasal(alarm);
                    postalarm = postalarm.with_alarmType('auto_off')
                        .with_payload({
                            alarmText: getNameForValue(ALARM_TYPES, alarmValue),
                            explanation: ALARM_TYPES[alarmText].explanation,
                            stopsDelivery: ALARM_TYPES[alarmText].stopsDelivery
                        })
                        .with_status(postsuspend)
                        .done();
                    break;
            }
            if (postalarm != null) {
                postrecords.push(postalarm);
            }
            if (postsuspend != null) {
                postrecords.push(postsuspend);
            }
            if (postbasal != null) {
                postrecords.push(postbasal);
            }
        }
        return records.concat(postrecords);
    };


    var buildBasalRecords = function (data, records) {
        var basalrecs = findSpecificRecords(data.log_records, [
            getValueForName(logRecords, 'Basal_Rate')
        ]);
        var postrecords = [];
        var postbasal = null;
        var dupcheck = {};
        for (var b = 0; b < basalrecs.length; ++b) {
            var basal = data.log_records[basalrecs[b]];
            // for Tidepool's purposes, the 'duration' field of a scheduled basal is
            // how long it's supposed to last. In this case, the answer is "until the next rate
            // change" -- but it's NOT the duration field of the basal record; that's only for
            // temp basals
            if (basal.detail.duration === 0) {
                postbasal = cfg.builder.makeScheduledBasal()
                    .with_scheduleName(
                    data.basalPrograms.names[data.basalPrograms.enabled_idx].name)
                    .with_rate(basal.detail.basal_rate_units_per_hour)
                    .with_time(basal.timestamp)
                    .with_deviceTime(basal.deviceTime)
                    .with_timezoneOffset(basal.timezoneOffset)
                    .set('index', basal.log_index);
            }
            else {
                postbasal = cfg.builder.makeTempBasal()
                    .with_rate(basal.detail.basal_rate_units_per_hour)
                    .with_time(basal.timestamp)
                    .with_deviceTime(basal.deviceTime)
                    .with_timezoneOffset(basal.timezoneOffset)
                    .with_duration(basal.detail.duration_msec)
                    .set('index', basal.log_index);
                if (basal.detail.temp_basal_percent != null) {
                    var suppressed = cfg.builder.makeScheduledBasal()
                        .with_rate(common.fixFloatingPoint(basal.detail.basal_rate_units_per_hour / basal.detail.temp_basal_percent, 2))
                        .with_time(basal.timestamp)
                        .with_deviceTime(basal.deviceTime)
                        .with_timezoneOffset(basal.timezoneOffset)
                        .with_duration(basal.detail.duration_msec);
                    postbasal.with_percent(basal.detail.temp_basal_percent)
                        .set('suppressed', suppressed);
                }
            }

            if (dupcheck[postbasal.time]) {
                debug('Duplicate basals at ' + postbasal.deviceTime + '; expect PDM date & time settings change near here.');
                // we can't push the "dups" until we can feed them to jellyfish as a separate stream
                // postrecords.push(postbasal);
            } else {
                dupcheck[postbasal.time] = postbasal;
                postrecords.push(postbasal);
            }
        }
        var lastBasal = postrecords[postrecords.length - 1];
        annotate.annotateEvent(lastBasal, 'insulet/basal/fabricated-from-schedule');
        return records.concat(postrecords);
    };


};
