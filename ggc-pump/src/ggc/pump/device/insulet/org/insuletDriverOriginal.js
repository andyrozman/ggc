/*
 * == BSD2 LICENSE ==
 * Copyright (c) 2014, Tidepool Project
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the associated License, which is identical to the BSD 2-Clause
 * License as published by the Open Source Initiative at opensource.org.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the License for more details.
 *
 * You should have received a copy of the License along with this program; if
 * not, you can obtain one from Tidepool Project at tidepool.org.
 * == BSD2 LICENSE ==
 */

var _ = require('lodash');
var struct = require('./../struct.js')();
var sundial = require('sundial');

var annotate = require('../eventAnnotations');
var common = require('../insulet/common');
var logic = require('../insulet/objectBuildingLogic');
var insuletSimulatorMaker = require('../insulet/insuletSimulator');

var isBrowser = typeof window !== 'undefined';
var debug = isBrowser ? require('../bows')('InsuletDriver') : console.log;

module.exports = function (config) {
  var cfg = _.clone(config);
  var buf;
  var bytes;

  var BG_UNITS = 'mg/dL';

  // all insulin unit readings are in .01 unit increments, so we divide by 100.0 to get units
  // (multiplying by 0.01 tends to cause floating point issues)
  var toUnits = function (x) {
    return x / 100.0;
  };

  // Insulet reports percentage temp basals as +/- integers [-100, 100+]
  // i.e., -50 for 50% of the scheduled basal rate
  var convertTempPercentageToNetBasal = function(x) {
    // 0 means there *wasn't* a temp basal in this record
    if (x === 0) {
      return null;
    }
    return 1.0 + x / 100.0;
  };

  // This is a particularly weak checksum algorithm but that's what Insulet uses...
  var weakChecksum = function (offset, count) {
    var total = 0;
    for (var i = 0; i < count; ++i) {
      total += bytes[offset + i];
    }
    return total & 0xFFFF;
  };

  var getRecord = function (offset) {
    var recsize = struct.extractBEShort(bytes, offset);
    var retval = {
      recsize: recsize,
      packetlen: recsize + 2,
      rawdata: new Uint8Array(buf, offset + 2, recsize - 2),
      chksum: struct.extractBEShort(bytes, offset + recsize),
      calcsum: weakChecksum(offset + 2, recsize - 2)
    };
    retval.valid = (retval.calcsum == retval.chksum);
    return retval;
  };

  var addTimestamp = function (o) {
    var dt = sundial.buildTimestamp(o);
    if (dt) {
      o.timestamp = sundial.applyTimezone(dt, cfg.timezone).toISOString();
      o.deviceTime = sundial.formatDeviceTime(dt);
      o.timezoneOffset = sundial.getOffsetFromZone(o.timestamp, cfg.timezone);
    }
  };

  // postprocess is a function that accepts a record, optionally modifies it,
  // and returns null if the record is good, and a message if processing should halt.
  var fixedRecords = {
    ibf_version: { format: '6S8z8z', fields: [
      'ibf_maj', 'ibf_min', 'ibf_patch',
      'eng_maj', 'eng_min', 'eng_patch',
      'vendorid', 'productid'
    ], postprocess: function (rec) {
      if ((rec.ibf_maj === 0) && (rec.ibf_min >= 1) &&
          (rec.eng_maj === 0) && (rec.ibf_min >= 1) &&
          (rec.vendorid === 'Insulet') && (rec.productid === 'OmniPod')) {
        return null;
      } else {
        return 'ibf_version record is incompatible with this driver.';
      }
    }
    },
    pdm_version: { format: '3S', fields: [
      'pdm_maj', 'pdm_min', 'pdm_patch'
    ], postprocess: function (rec) {
      if ((rec.pdm_maj === 2) && (rec.pdm_min >= 3)) {
        return null;
      } else {
        return 'pdm_version record is incompatible with this driver.';
      }
    } },
    // this format gets rewritten before being used
    mfg_data: { format: '??z', fields: ['data']},
    basal_programs_hdr: { format: '3S', fields: [
      'num_progs', 'enabled_idx', 'max_name_size'
    ] },
    // this format gets rewritten before being used
    basal_programs_name: { format: 'S??z', fields: ['index', 'name'] },
    eeprom_settings: {
      format: '13.4i2b4.b5.b.bb8.i19.7b3sb19.bi',
      fields: [
        'BOLUS_INCR',
        'BOLUS_MAX',
        'BASAL_MAX',
        'LOW_VOL',
        'AUTO_OFF',
        'LANGUAGE',
        'EXPIRE_ALERT',
        'BG_REMINDER',
        'CONF_ALERT',
        'REMDR_ALERT',
        'REMOTE_ID',
        'TEMP_BAS_TYPE',
        'EXT_BOL_TYPE',
        'BOL_REMINDER',
        'BOL_CALCS',
        'BOL_CALCS_REVERSE',
        'BG_DISPLAY',
        'BG_SOUND',
        'BG_MIN',
        'BG_GOAL_LOW',
        'BG_GOAL_UP',
        'INSULIN_DURATION',
        'ALARM_REPAIR_COUNT',
        'PDM_CONFIG'
      ],
      postprocess: function (rec) {
        rec.bolus_incr_units = toUnits(rec.BOLUS_INCR);
        rec.bolus_max_units = toUnits(rec.BOLUS_MAX);
        rec.basal_max_units = toUnits(rec.BASAL_MAX);
        rec.low_vol_units = toUnits(rec.LOW_VOL);
        rec.serial_number = rec.REMOTE_ID.toString();
        if (rec.BG_DISPLAY === 0) {
          rec.units = 'mg/dL';
        } else {
          rec.units = 'mmol/L';
        }
        rec.insulin_duration_msec = rec.INSULIN_DURATION * sundial.MIN30_TO_MSEC;
        return null;
      }
    },
    profile_hdr: { format: 'b6.Si', fields: [
      'profile_idx', 'error_code', 'operation_time'
    ] },
    log_hdr: { format: '7bS3b.S', fields: [
      'logs_info_revision',
      'insulin_history_revision',
      'alarm_history_revision',
      'blood_glucose_revision',
      'insulet_stats_revision',
      'day',
      'month',
      'year',
      'seconds',
      'minutes',
      'hours',
      'num_log_descriptions'
    ]},
    log_description: { format: '5S2N', fields: [
      'log_index', 'backup', 'location', 'has_variable', 'record_size',
      'first_index', 'last_index'
    ]},
    log_record: { format: 'bNSSbbsbbb.i', fields: [
      'log_id', 'log_index', 'record_size', 'error_code',
      'day', 'month', 'year', 'seconds', 'minutes', 'hours',
      'secs_since_powerup'
    ]},
    history_record: { format: 'bNSSbbsbbb.ins..', fields: [
      'log_id', 'log_index', 'record_size', 'error_code',
      'day', 'month', 'year', 'seconds', 'minutes', 'hours',
      'secs_since_powerup', 'rectype', 'flags'
    ]}
  };

  var logRecords = {
    0x0000: { value: 0x0000, name: 'End_Marker', format: '', fields: [] },
    0x0001: { value: 0x0001, name: 'Deactivate', format: '', fields: [] },
    0x0002: { value: 0x0002, name: 'Time_Change', format: '3b.', fields: [
      'seconds',
      'minutes',
      'hours'
    ]
    },
    0x0004: { value: 0x0004, name: 'Bolus', format: 'isss', fields: [
      'volume',
      'extended_duration_minutes',
      'calculation_record_offset',
      'immediate_duration_seconds'
    ], postprocess: function (rec) {
      rec.volume_units = toUnits(rec.volume);
      // this occurs if an extended bolus is programmed but the bolus is interrupted
      // before any of the extended portion is delivered!
      if (rec.extended_duration_minutes === 65535) {
        rec.extended_duration_msec = 0;
      }
      else {
        // a zero *here* means that this isn't an extended bolus
        // and we need to distinguish between two types of zeros -
        // dual-wave boluses interrupted before any of the extended was
        // delivered (i.e., the case above ^) or non-extended boluses (here)
        // so here we use null instead of 0
        if (rec.extended_duration_minutes === 0) {
          rec.extended_duration_msec = null;
        }
        else {
          rec.extended_duration_msec = rec.extended_duration_minutes * sundial.MIN_TO_MSEC;
        }
      }
      rec.immediate_duration_msec = rec.immediate_duration_seconds * sundial.SEC_TO_MSEC;
    }
    },
    0x0008: { value: 0x0008, name: 'Basal_Rate', format: 'ish', fields: [
      'basal_rate', 'duration', 'percent'
    ], postprocess: function (rec) {
      rec.basal_rate_units_per_hour = toUnits(rec.basal_rate);
      rec.duration_msec = rec.duration * sundial.MIN_TO_MSEC;
      rec.temp_basal_percent = convertTempPercentageToNetBasal(rec.percent);
    }
    },
    0x0010: { value: 0x0010, name: 'Suspend', format: '', fields: [] },
    0x0020: { value: 0x0020, name: 'Date_Change', format: 'bbs', fields: [
      'day',
      'month',
      'year'
    ]
    },
    0x0040: { value: 0x0040, name: 'Suggested_Calc', format: '4in3i6s', fields: [
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
    0x0080: { value: 0x0080, name: 'Remote_Hazard_Alarm', format: '2bs3b.4s', fields: [
      'day', 'month', 'year', 'seconds', 'minutes', 'hours',
      'alarm_type', 'file_number', 'line_number', 'error_code'
    ]
    },
    0x0400: { value: 0x0400, name: 'Alarm', format: '2bs3b.4s', fields: [
      'day', 'month', 'year', 'seconds', 'minutes', 'hours',
      'alarm_type', 'file_number', 'line_number', 'error_code'
    ]
    },
    0x0800: { value: 0x0800, name: 'Blood_Glucose', format: 'is24z24zb.', fields: [
      'error_code', 'bg_reading',
      'user_tag_1', 'user_tag_2',
      'flags'
    ]
    },
    0x1000: { value: 0x1000, name: 'Carb', format: 'sbb', fields: [
      'carbs',
      'was_preset',
      'preset_type'
    ]
    },
    0x2000: { value: 0x2000, name: 'Terminate_Bolus', format: 'is', fields: [
      'insulin_left',
      'time_left_minutes'
    ], postprocess: function (rec) {
      rec.insulin_units_left = toUnits(rec.insulin_left);
      rec.time_left_msec = rec.time_left_minutes * sundial.MIN_TO_MSEC;
    }
    },
    0x4000: { value: 0x4000, name: 'Terminate_Basal', format: 's', fields: [
      'time_left_minutes'
    ], postprocess: function (rec) {
      rec.time_left_msec = rec.time_left_minutes * sundial.MIN_TO_MSEC;
    }
    },
    0x8000: { value: 0x8000, name: 'Activate', format: '2S6b', fields: [
      'lot_number', 'serial_number',
      'pod_maj', 'pod_min', 'pod_patch',
      'interlock_maj', 'interlock_min', 'interlock_patch'
    ]
    },
    0x10000: { value: 0x10000, name: 'Resume', format: '', fields: [] },
    0x20000: { value: 0x20000, name: 'Download', format: '', fields: [] },
    0x40000: { value: 0x40000, name: 'Occlusion', format: '', fields: [] }
  };

  var PROFILES = {
    carbRatio: {
      value: 11, name: 'carbRatio', mfrname: 'IC Ratio', isBasal: false,
      keyname: 'amount', valuename: 'value'
    },
    insulinSensitivity: {
      value: 12, name: 'insulinSensitivity', mfrname: 'Correction', isBasal: false,
      keyname: 'amount', valuename: 'value'
    },
    bgTarget: {
      value: 13, name: 'bgTarget', mfrname: 'Target BG', isBasal: false,
      keyname: 'low', valuename: 'value'
    },
    bgThreshold: {
      value: 14, name: 'bgThreshold', mfrname: 'BG Threshold', isBasal: false,
      keyname: 'amount', valuename: 'value'
    },
    basalprofile0: {
      value: 15, name: 'basalprofile0', mfrname: 'Basal Profile 0', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile1: {
      value: 16, name: 'basalprofile1', mfrname: 'Basal Profile 1', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile2: {
      value: 17, name: 'basalprofile2', mfrname: 'Basal Profile 2', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile3: {
      value: 18, name: 'basalprofile3', mfrname: 'Basal Profile 3', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile4: {
      value: 19, name: 'basalprofile4', mfrname: 'Basal Profile 4', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile5: {
      value: 20, name: 'basalprofile5', mfrname: 'Basal Profile 5', isBasal: true,
      keyname: 'rate', valuename: 'units'
    },
    basalprofile6: {
      value: 21, name: 'basalprofile6', mfrname: 'Basal Profile 6', isBasal: true,
      keyname: 'rate', valuename: 'units'
    }
  };

  var pump_alarm_record = {
    format: '2bs3b.s.b2i6b', fields: [
      'day', 'month', 'year', 'seconds', 'minutes', 'hours',
      'alarm', 'error_code', 'lot_number', 'seq_number',
      'processor_maj', 'processor_min', 'processor_patch',
      'interlock_maj', 'interlock_min', 'interlock_patch'
    ]
  };

  var BG_FLAGS = {
    MANUAL_FLAG: { value: 0x01, name: 'MANUAL_FLAG' },
    TEMPERATURE_FLAG: { value: 0x02, name: 'TEMPERATURE_FLAG' },
    BELOW_TARGET_FLAG: { value: 0x04, name: 'BELOW_TARGET_FLAG' },
    ABOVE_TARGET_FLAG: { value: 0x08, name: 'ABOVE_TARGET_FLAG' },
    RANGE_ERROR_LOW_FLAG: { value: 0x10, name: 'RANGE_ERROR_LOW_FLAG' },
    RANGE_ERROR_HIGH_FLAG: { value: 0x20, name: 'RANGE_ERROR_HIGH_FLAG' },
    OTHER_ERROR_FLAG: { value: 0x40, name: 'OTHER_ERROR_FLAG' }
  };

  var PDM_CONFIG_FLAGS = {
    SUGGESTED_BOLUS_STYLE: { value: 0x01, name: 'SUGGESTED_BOLUS_STYLE' },
    PRODUCT_ID: { mask: 0x1E, shift: 1, name: 'PRODUCT_ID' },
    LOT_TID_SUPPORT: { value: 0x20, name: 'LOT_TID_SUPPORT' },
    BG_BOARD_TYPE: { mask: 0x3C0, shift: 6, name: 'BG_BOARD_TYPE' }
  };

  var BG_BOARD_TYPES = {
    0: { name: 'Abbott FreeStyle', highest: 500 },
    2: { name: 'LifeScan Verio', highest: 600 },
    3: { name: 'None', highest: 600 }
  };

  var LOG_FLAGS = {
    CARRY_OVER_FLAG: { value: 0x01, name: 'CARRY_OVER_FLAG' },
    NEW_DAY_FLAG: { value: 0x02, name: 'NEW_DAY_FLAG' },
    IN_PROGRESS_FLAG: { value: 0x04, name: 'IN_PROGRESS_FLAG' },
    END_DAY_FLAG: { value: 0x08, name: 'END_DAY_FLAG' },
    UNCOMFIRMED_FLAG: { value: 0x10, name: 'UNCOMFIRMED_FLAG' },
    REVERSE_CORR_FLAG: { value: 0x0100, name: 'REVERSE_CORR_FLAG' },
    MAX_BOLUS_FLAG: { value: 0x0200, name: 'MAX_BOLUS_FLAG' },
    ERROR: { value: 0x80000000, name: 'ERROR' }
  };

  var LOG_TYPES = {
    HISTORY: { value: 0x03, name: 'HISTORY' },
    PUMP_ALARM: { value: 0x05, name: 'PUMP_ALARM' },
    DELETED: { mask: 0x80000000, name: 'DELETED' },
    // this is an impossible value for a 1-byte LOG_TYPE
    IGNORE: { value: 0x100, name: 'IGNORED by driver' }
  };

  var ALARM_TYPES = {
    AlrmPDM_ERROR0: { value: 0, name: 'AlrmPDM_ERROR0', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR1: { value: 1, name: 'AlrmPDM_ERROR1', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR2: { value: 2, name: 'AlrmPDM_ERROR2', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR3: { value: 3, name: 'AlrmPDM_ERROR3', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR4: { value: 4, name: 'AlrmPDM_ERROR4', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR5: { value: 5, name: 'AlrmPDM_ERROR5', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR6: { value: 6, name: 'AlrmPDM_ERROR6', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR7: { value: 7, name: 'AlrmPDM_ERROR7', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR8: { value: 8, name: 'AlrmPDM_ERROR8', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmPDM_ERROR9: { value: 9, name: 'AlrmPDM_ERROR9', explanation: 'PDM error', stopsDelivery: 'unknown' },
    AlrmSYSTEM_ERROR10: { value: 10, name: 'AlrmSYSTEM_ERROR10', explanation: 'system error', stopsDelivery: false },
    AlrmSYSTEM_ERROR12: { value: 12, name: 'AlrmSYSTEM_ERROR12', explanation: 'system error', stopsDelivery: 'unknown' },
    AlrmHAZ_REMOTE: { value: 13, name: 'AlrmHAZ_REMOTE', explanation: 'clock reset alarm', stopsDelivery: false },
    AlrmHAZ_PUMP_VOL: { value: 14, name: 'AlrmHAZ_PUMP_VOL', explanation: 'empty reservoir', stopsDelivery: true },
    AlrmHAZ_PUMP_AUTO_OFF: { value: 15, name: 'AlrmHAZ_PUMP_AUTO_OFF', explanation: 'auto-off', stopsDelivery: true },
    AlrmHAZ_PUMP_EXPIRED: { value: 16, name: 'AlrmHAZ_PUMP_EXPIRED', explanation: 'pod expired', stopsDelivery: true },
    AlrmHAZ_PUMP_OCCL: { value: 17, name: 'AlrmHAZ_PUMP_OCCL', explanation: 'pump site occluded', stopsDelivery: true },
    AlrmHAZ_PUMP_ACTIVATE: { value: 18, name: 'AlrmHAZ_PUMP_ACTIVATE', explanation: 'pod is a lump of coal', stopsDelivery: false },
    AlrmADV_KEY: { value: 21, name: 'AlrmADV_KEY', explanation: 'PDM stuck key detected', stopsDelivery: false },
    AlrmADV_PUMP_VOL: { value: 23, name: 'AlrmADV_PUMP_VOL', explanation: 'low reservoir', stopsDelivery: false },
    AlrmADV_PUMP_AUTO_OFF: { value: 24, name: 'AlrmADV_PUMP_AUTO_OFF', explanation: '15 minutes to auto-off warning', stopsDelivery: false },
    AlrmADV_PUMP_SUSPEND: { value: 25, name: 'AlrmADV_PUMP_SUSPEND', explanation: 'suspend done', stopsDelivery: false },
    AlrmADV_PUMP_EXP1: { value: 26, name: 'AlrmADV_PUMP_EXP1', explanation: 'pod expiration advisory', stopsDelivery: false },
    AlrmADV_PUMP_EXP2: { value: 27, name: 'AlrmADV_PUMP_EXP2', explanation: 'pod expiration alert', stopsDelivery: false },
    AlrmSYSTEM_ERROR28: { value: 28, name: 'AlrmSYSTEM_ERROR28', explanation: 'system error', stopsDelivery: 'unknown' },
    AlrmEXP_WARNING: { value: 37, name: 'AlrmEXP_WARNING', explanation: 'pod expiration advisory', stopsDelivery: false },
    AlrmHAZ_PDM_AUTO_OFF: { value: 39, name: 'AlrmHAZ_PDM_AUTO_OFF', explanation: 'auto-off', stopsDelivery: true }
  };

  var LOG_ERRORS = {
    eLogNoErr: { value: 0, name: 'eLogNoErr' },
    eLogGetEEPROMErr: { value: 3, name: 'eLogGetEEPROMErr' },
    eLogCRCErr: { value: 4, name: 'eLogCRCErr' },
    eLogLogIndexErr: { value: 6, name: 'eLogLogIndexErr' },
    eLogRecSizeErr: { value: 8, name: 'eLogRecSizeErr' }
  };

  var getItemWithValue = function (list, itemname, valuename, value) {
    for (var i in list) {
      if (list[i][valuename] == value) {
        return list[i][itemname];
      }
    }
    return null;
  };

  var getNameForValue = function (list, v) {
    return getItemWithValue(list, 'name', 'value', v);
  };

  var getValueForName = function (list, n) {
    return getItemWithValue(list, 'value', 'name', n);
  };


  var getFlagNames = function (list, v) {
    var flags = [];
    for (var i in list) {
      if (list[i].value & v) {
        flags.push(list[i].name);
      }
    }
    return flags.join('|');
  };

  var hasFlag = function (flag, v) {
    if (flag.value & v) {
      return true;
    }
    return false;
  };


  var getFixedRecord = function (recname, offset) {
    var rec = getRecord(offset);
    var decoded = struct.unpack(rec.rawdata, 0,
                                fixedRecords[recname].format, fixedRecords[recname].fields);
    _.assign(rec, decoded);
    return rec;
  };

  var getManufacturingData = function (offset) {
    var rec = getRecord(offset);
    var fmt = rec.recsize + 'z';
    var decoded = struct.unpack(rec.rawdata, 0, fmt, fixedRecords.mfg_data.fields);
    _.assign(rec, decoded);
    return rec;
  };

  var getBasalProgramNames = function (offset) {
    var basalProgramsHeader = getFixedRecord('basal_programs_hdr', offset);
    var basalProgramNames = [];
    for (var i = 0; i < 7; ++i) {
      var prgOffset = 6 + i * (2 + basalProgramsHeader.max_name_size);
      // the format for the name data is dependent on the name size
      fixedRecords.basal_programs_name.format = 'S' +
                                                basalProgramsHeader.max_name_size + 'z';
      var prog = struct.unpack(basalProgramsHeader.rawdata, prgOffset,
                               fixedRecords.basal_programs_name.format,
                               fixedRecords.basal_programs_name.fields);
      prog.name = prog.name.replace(/\./g, '-');
      prog.name = prog.name.replace(/\$/g, '');
      basalProgramNames.push(prog);
    }
    basalProgramsHeader.names = basalProgramNames;
    return basalProgramsHeader;
  };

  var getProfiles = function (offset, basalProgramNames) {
    var profiles = [];
    var totalLen = 0;
    // there are 11 profiles in a row
    for (var i = 0; i < 11; ++i) {
      // profiles consist of a header plus 48 integers
      var profile = getFixedRecord('profile_hdr', offset);
      profile.standard_name = getItemWithValue(PROFILES, 'mfrname', 'value', profile.profile_idx);
      if (getItemWithValue(PROFILES, 'isBasal', 'value', profile.profile_idx)) {
        // user-assigned name is at an index in the program names
        // relative to the profile index, which starts at 15 in
        // insulet data
        profile.name = basalProgramNames[profile.profile_idx - 15].name;
      }
      profile.steps = [];
      var step_offset = struct.structlen(fixedRecords.profile_hdr.format);
      for (var j = 0; j < 48; ++j) {
        profile.steps.push({
                             // profile steps are every 30 minutes starting at midnight
                             // convert them to milliseconds
                             starttime: j * sundial.MIN30_TO_MSEC,
                             value: struct.extractInt(profile.rawdata, step_offset + j * 4)
                           });
        // if it's a basal profile, add the units conversion
        if (getItemWithValue(PROFILES, 'isBasal', 'value', profile.profile_idx)) {
          profile.steps[j].units = toUnits(profile.steps[j].value);
        }
      }
      profiles.push(profile);
      offset += profile.packetlen;
      totalLen += profile.packetlen;
    }
    profiles.packetlen = totalLen;
    return profiles;
  };

  var getLogDescriptions = function (offset) {
    var logDescriptions = getFixedRecord('log_hdr', offset);
    logDescriptions.descs = [];
    addTimestamp(logDescriptions);
    for (var i = 0; i < logDescriptions.num_log_descriptions; ++i) {
      var descOffset = 15 + i * 18;
      var desc = struct.unpack(logDescriptions.rawdata, descOffset,
                               fixedRecords.log_description.format,
                               fixedRecords.log_description.fields);
      logDescriptions.descs.push(desc);
    }
    return logDescriptions;
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
      return { rectype: LOG_TYPES.IGNORE.value, packetlen: rec.packetlen };
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
      return { rectype: LOG_TYPES.IGNORE.value, packetlen: rec.packetlen };
    }
    return rec;
  };

  var getLogRecords = function (recordset) {
    // this is where we get the position-independent information
    var offset = recordset.independent_offset;
    var done = false;
    var log_records = [];
    var index = 0;
    while (!done) {
      var rec = getLogRecord(offset);
      offset += rec.packetlen;
      if (offset >= bytes.length) {
        done = true;
      }
      if (rec.rectype == LOG_TYPES.IGNORE.value) {
        continue;
      }
      if (rec.error_code) {
        debug('Nonzero logRecord error_code!');
      }
      rec.index = index++;
      log_records.push(rec);
    }

    return log_records;
  };

  // returns indices of the matching records
  var findSpecificRecords = function (recordlist, rectypelist) {
    var result = [];
    for (var i = 0; i < recordlist.length; ++i) {
      for (var j = 0; j < rectypelist.length; ++j) {
        if (recordlist[i].rectype === rectypelist[j]) {
          result.push(i);
        }
      }
    }
    return result.sort(function compare(a, b) {
      return recordlist[a].log_index - recordlist[b].log_index;
    });
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

  var linkWizardRecords = function (data, bolusrecs) {
    // we need to see if two (or more!) boluses come from the same calculation (wizard) record
    // if so, they're actually a dual bolus, and need to be consolidated
    // so we create a table of backlinks from the calc records to the bolus records that
    // refer to them
    var wizRecords = {};
    for (var b = 0; b < bolusrecs.length; ++b) {
      var bolus = data.log_records[bolusrecs[b]];
      var wiz_idx;
      // these are boluses not linked with wizard records (i.e., quick boluses)
      // but they could be dual-wave boluses with a normal and square component
      // so we use the timestamp to index them
      if (bolus.detail.calculation_record_offset === 0) {
        wiz_idx = bolus.timestamp;
      }
      else {
        wiz_idx = bolusrecs[b] + bolus.detail.calculation_record_offset;
      }
      var r = wizRecords[wiz_idx] || {};
      if (bolus.detail.extended_duration_msec !== null) {
        // the extended portion of a dual-wave bolus is split into two records
        // if it crosses local (deviceTime) midnight
        if (r.extended != null) {
          r.extended2 = bolusrecs[b];
        } else {
          r.extended = bolusrecs[b];
        }
      } else {
        r.immediate = bolusrecs[b];
      }
      if (r.immediate && r.extended) {
        r.isDual = true;
      }
      wizRecords[wiz_idx] = r;
    }
    return wizRecords;
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

  var buildBolusRecords = function (data, records) {
    var bolusrecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Bolus')
    ]);
    var wizRecords = linkWizardRecords(data, bolusrecs);
    var postrecords = [];

    for (var b = 0; b < bolusrecs.length; ++b) {
      var bolus = data.log_records[bolusrecs[b]];
      var wiz_idx;
      // quick boluses are indexed by timestamp
      if (bolus.detail.calculation_record_offset === 0) {
        wiz_idx = bolus.timestamp;
      }
      else {
        wiz_idx = bolusrecs[b] + bolus.detail.calculation_record_offset;
      }

      // if we already did this dual bolus, skip it on the second round
      if (wizRecords[wiz_idx] && wizRecords[wiz_idx].handled) {
        continue;
      }

      var postbolus = null;
      if (wizRecords[wiz_idx]) {
        if (wizRecords[wiz_idx].isDual) {
          var ext = wizRecords[wiz_idx].extended;
          var ext2 = wizRecords[wiz_idx].extended2 || null;
          var imm = wizRecords[wiz_idx].immediate;
          postbolus = cfg.builder.makeDualBolus()
            .with_normal(data.log_records[imm].detail.volume_units)
            .with_time(data.log_records[imm].timestamp)
            .with_deviceTime(data.log_records[imm].deviceTime)
            .with_timezoneOffset(data.log_records[imm].timezoneOffset)
            .set('index', data.log_records[imm].log_index);
          if (ext2 != null) {
            postbolus = postbolus.with_extended(common.fixFloatingPoint(
                data.log_records[ext].detail.volume_units + data.log_records[ext2].detail.volume_units,
                2)
              )
              .with_duration(
                data.log_records[ext].detail.extended_duration_msec + data.log_records[ext2].detail.extended_duration_msec
              )
              // TODO: delete after conclusion of Jaeb study
              .set('jaebPayload', {logIndices: [
                data.log_records[imm].log_index,
                data.log_records[ext].log_index,
                data.log_records[ext2].log_index
              ]})
              // TODO: end deletion
              .done();
          } else {
            postbolus = postbolus.with_extended(data.log_records[ext].detail.volume_units)
              .with_duration(data.log_records[ext].detail.extended_duration_msec)
              // TODO: delete after conclusion of Jaeb study
              .set('jaebPayload', {logIndices: [data.log_records[imm].log_index, data.log_records[ext].log_index]})
              // TODO: end deletion
              .done();
          }
          wizRecords[wiz_idx].handled = true;
        } else if (bolus.detail.extended_duration_msec !== null) {
          var square2 = wizRecords[wiz_idx].extended2 || null;
          postbolus = cfg.builder.makeSquareBolus()
            .with_time(bolus.timestamp)
            .with_deviceTime(bolus.deviceTime)
            .with_timezoneOffset(bolus.timezoneOffset)
            .set('index', bolus.log_index);
          if (square2 != null) {
            postbolus = postbolus.with_extended(common.fixFloatingPoint(
                bolus.detail.volume_units + data.log_records[square2].detail.volume_units,
                2)
              )
              .with_duration(
                bolus.detail.extended_duration_msec + data.log_records[square2].detail.extended_duration_msec
              ).done();
          } else {
            postbolus.with_extended(bolus.detail.volume_units)
              .with_duration(bolus.detail.extended_duration_msec);
            var millisInDay = sundial.getMsFromMidnight(postbolus.time, postbolus.timezoneOffset);
            // extended boluses with timestamps at "precisely" (within 5 sec.) of midnight
            // might actually be the second half of a previous dual- or square-wave bolus
            // since Insulet always splits these records when they cross midnight
            // when there isn't a wizard record to tie split records together, we don't
            // know if this is a component of a split, so we annotate
            if (millisInDay <= 5000 && !data.log_records[wiz_idx]) {
              annotate.annotateEvent(postbolus, 'insulet/bolus/split-extended');
            }
            postbolus = postbolus.done();
          }
        } else if (bolus.detail.immediate_duration_msec !== 0) {
          postbolus = cfg.builder.makeNormalBolus()
            .with_normal(bolus.detail.volume_units)
            .with_time(bolus.timestamp)
            .with_deviceTime(bolus.deviceTime)
            .with_timezoneOffset(bolus.timezoneOffset)
            .set('index', bolus.log_index)
            .done();
        } else {
          if (bolus.detail.volume_units !== 0) {
            debug('Unexpected bolus of nonzero volume %d but zero duration!', bolus.detail.volume_units);
          }
          else {
            // we thought we could ignore zero-volume boluses, but it turns out they could
            // be the result of an interrupted non-zero-volume bolus (when followed by a 
            // bolus termination)
            postbolus = cfg.builder.makeNormalBolus()
              .with_normal(bolus.detail.volume_units)
              .with_time(bolus.timestamp)
              .with_deviceTime(bolus.deviceTime)
              .with_timezoneOffset(bolus.timezoneOffset)
              .set('index', bolus.log_index)
              .done();
          }
        }
      }

      if (postbolus) {
        postrecords.push(postbolus);
        if (wizRecords[wiz_idx]) {
          var wiz = data.log_records[wiz_idx] || {};
          // wiz will be empty if the bolus was a quick bolus
          // and wiz.detail will be empty in various circumstances that share
          // the common feature that the bolus attempting to link to the
          // wizard record is the first data point (chronologically) in the
          // log records
          // presumably these wizard records are missing because this is
          // the PDM's memory cut-off
          if (!_.isEmpty(wiz) && !_.isEmpty(wiz.detail)) {
            var payload = _.assign({}, wiz.detail);
            var bg = wiz.detail.current_bg;
            if (bg === 65535) {
              // if bg was not given to the wizard, don't report it.
              bg = null;
            }
            var carb = wiz.detail.carb_grams;
            if (carb === 65535) {
              // if carb count was not given to the wizard, don't report it.
              carb = null;
            }
            var postwiz = cfg.builder.makeWizard()
              .with_recommended({
                carb: wiz.detail.carb_bolus_units_suggested,
                correction: wiz.detail.corr_units_suggested,
                net: logic.calculateNetRecommendation(wiz.detail)
              })
              .with_bgInput(bg)
              .with_carbInput(carb)
              .with_insulinOnBoard(common.fixFloatingPoint(wiz.detail.corr_units_iob + wiz.detail.meal_units_iob, 2))
              .with_insulinCarbRatio(wiz.detail.ic_ratio_used)
              .with_insulinSensitivity(wiz.detail.correction_factor_used)
              .with_bgTarget({target: wiz.detail.target_bg,
                               high: wiz.detail.bg_correction_threshold })
              .with_bolus(postbolus)
              .with_payload(payload)
              .with_time(wiz.timestamp)
              .with_deviceTime(wiz.deviceTime)
              .with_timezoneOffset(wiz.timezoneOffset)
              .with_units(BG_UNITS)
              .set('index', wiz.log_index)
              .done();
            postrecords.push(postwiz);
          }
        }
      }
    }
    return records.concat(postrecords);
  };

  var buildBolusTerminations = function(data, records) {
    var termrecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Terminate_Bolus')
    ]);
    var postrecords = [];
    var postterm = null;
    for (var t = 0; t < termrecs.length; ++t) {
      var term = data.log_records[termrecs[t]];
      // these get fed to the simulator but not uploaded (just used to modify other events)
      // hence not using the object builder
      postterm = {
        type: 'termination',
        subType: 'bolus',
        time: term.timestamp,
        missedInsulin: term.detail.insulin_units_left,
        durationLeft: term.detail.time_left_msec,
        index: term.log_index
      };
      postrecords.push(postterm);
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
            .with_rate(common.fixFloatingPoint(basal.detail.basal_rate_units_per_hour/basal.detail.temp_basal_percent, 2))
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

  // it turns out these records may not actually be implemented
  // occlusions are only represented through pump advisory and hazard alarms
  // TODO: maybe we should just delete this code?
  var buildOcclusionRecords = function (data, records) {
    var occlusionrecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Occlusion')
    ]);
    var postrecords = [];
    var postocc = null;
    for (var o = 0; o < occlusionrecs.length; ++o) {
      var occlusion = data.log_records[occlusionrecs[o]];
      postocc = cfg.builder.makeDeviceMetaAlarm()
        .with_time(occlusion.timestamp)
        .with_deviceTime(occlusion.deviceTime)
        .with_timezoneOffset(occlusion.timezoneOffset)
        .with_alarmType('occlusion')
        .set('index', occlusion.log_index)
        .done();
      postrecords.push(postocc);
    }
    return records.concat(postrecords);
  };

  var buildSuspendRecords = function (data, records) {
    var suspendrecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Suspend'),
      getValueForName(logRecords, 'Deactivate')
    ]);
    var postrecords = [];
    var postsuspend = null, postbasal = null, postreschange = null;
    for (var s = 0; s < suspendrecs.length; ++s) {
      var suspend = data.log_records[suspendrecs[s]];
      postsuspend = cfg.builder.makeDeviceMetaSuspend()
        .with_time(suspend.timestamp)
        .with_deviceTime(suspend.deviceTime)
        .with_timezoneOffset(suspend.timezoneOffset)
        // the spec doesn't really specify circumstances under which suspends happen
        // i.e., 'manual' reason code is an assumption here
        // 'Deactivate' is probably most commonly *not* manual (b/c it's pod expiration)
        // but it *can* be and the event itself doesn't identify
        // in the future we could consider keeping track of the pump warnings that precede
        // (e.g., in the simulator) and attempt to infer the proper reason code that way
        // TODO: consider an annotation here re: unknown reason code
        .with_reason({suspended: 'manual'})
        .set('index', suspend.log_index)
        .done();
      if (suspend.rectype_name === 'Deactivate') {
        postreschange = cfg.builder.makeDeviceMetaReservoirChange()
          .with_time(suspend.timestamp)
          .with_deviceTime(suspend.deviceTime)
          .with_timezoneOffset(suspend.timezoneOffset)
          .with_payload({event: 'pod_deactivation'})
          .with_status(postsuspend)
          .set('index', suspend.log_index)
          .done();
        postrecords.push(postreschange);
      }
      postrecords.push(postsuspend);
      // we don't call .done() on the basal b/c it still needs duration added
      // which happens in the simulator
      postbasal = cfg.builder.makeSuspendBasal()
        .with_time(suspend.timestamp)
        .with_deviceTime(suspend.deviceTime)
        .with_timezoneOffset(suspend.timezoneOffset)
        .set('index', suspend.log_index);
      postrecords.push(postbasal);
    }
    return records.concat(postrecords);
  };

  var buildResumeRecords = function (data, records) {
    var resumerecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Resume')
    ]);
    var postrecords = [];
    var postresume = null;
    for (var r = 0; r < resumerecs.length; ++r) {
      var resume = data.log_records[resumerecs[r]];
      // we don't call .done() on a resume b/c we still need to pair it with
      // its previous suspend in the simulator
      postresume = cfg.builder.makeDeviceMetaResume()
        .with_time(resume.timestamp)
        .with_deviceTime(resume.deviceTime)
        .with_timezoneOffset(resume.timezoneOffset)
        // the spec doesn't really specify circumstances under which resumes happen
        // i.e., 'manual' reason code is an assumption here
        .with_reason({resumed: 'manual'})
        .set('index', resume.log_index);
      postrecords.push(postresume);
    }
    return records.concat(postrecords);
  };

  var buildActivationRecords = function (data, records) {
    var activerecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Activate')
    ]);
    var postrecords = [];
    var postactivate = null;
    for (var a = 0; a < activerecs.length; ++a) {
      var activate = data.log_records[activerecs[a]];
      postactivate = cfg.builder.makeDeviceMetaResume()
        .with_time(activate.timestamp)
        .with_deviceTime(activate.deviceTime)
        .with_timezoneOffset(activate.timezoneOffset)
        .with_reason('new_pod')
        .set('index', activate.log_index);
      postrecords.push(postactivate);
    }
    return records.concat(postrecords);
  };

  var buildTimeChangeRecords = function (data, records) {
    var seenChanges = {};
    function findNearbyChanges(index) {
      var donechange = false, changes = [data.log_records[index]];
      while (!donechange) {
        var currentrec = data.log_records[--index];
        if (currentrec.rectype_name === 'Date_Change' || currentrec.rectype_name === 'Time_Change') {
          seenChanges[index] = true;
          changes.push(currentrec);
        }
        else {
          donechange = true;
        }
      }
      return changes;
    }

    var changerecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Date_Change'),
      getValueForName(logRecords, 'Time_Change')
    ]);
    var postrecords = [];
    var postchange = null;
    debug('Date and time change(s):');
    for (var c = 0; c < changerecs.length; ++c) {
      if (!seenChanges[changerecs[c]]) {
        var changegroup = findNearbyChanges(changerecs[c]);
        changegroup.forEach(function(c) { debug(c.log_index, c.rectype_name, c.detail); });
        var grouped = _.groupBy(changegroup, 'rectype_name');
        var first = changegroup[0];
        var ts = sundial.buildTimestamp(_.assign(
          {},
          grouped.Date_Change ? grouped.Date_Change[grouped.Date_Change.length - 1].detail :
            {year: first.year, month: first.month, day: first.day},
          grouped.Time_Change ? grouped.Time_Change[grouped.Time_Change.length - 1].detail :
            {hours: first.hours, minutes: first.minutes, seconds: first.seconds}
        ));
        postchange = cfg.builder.makeDeviceMetaTimeChange()
          .with_time(first.timestamp)
          .with_deviceTime(first.deviceTime)
          .with_timezoneOffset(first.timezoneOffset)
          .with_change({
            from: first.deviceTime,
            to: sundial.formatDeviceTime(ts),
            agent: 'manual'
          })
          .done();
        debug(postchange);
        postrecords.push(postchange);
      }
    }
    return records.concat(postrecords);
  };

  var buildBGRecords = function (data, records) {
    var bgrecs = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Blood_Glucose')
    ]);
    var postrecords = [];
    for (var i = 0; i < bgrecs.length; ++i) {
      var bg = data.log_records[bgrecs[i]];
      // skip errored records: meter error code, temperature error flag, other error flag
      if (bg.detail.error_code !== 0 ||
        hasFlag(BG_FLAGS.TEMPERATURE_FLAG, bg.detail.flags) ||
        hasFlag(BG_FLAGS.OTHER_ERROR_FLAG, bg.detail.flags)) {
        continue;
      }
      var bgMeter = BG_BOARD_TYPES[PDM_CONFIG_FLAGS.BG_BOARD_TYPE.mask & data.eeprom_settings.PDM_CONFIG_FLAGS];
      var postbg = cfg.builder.makeSMBG()
        .with_time(bg.timestamp)
        .with_deviceTime(bg.deviceTime)
        .with_timezoneOffset(bg.timezoneOffset)
        .with_units(BG_UNITS)
        .with_subType(hasFlag(BG_FLAGS.MANUAL_FLAG, bg.detail.flags) ? 'manual' : 'linked')
        .set('index', bg.log_index);
      var value = bg.detail.bg_reading;
      if (hasFlag(BG_FLAGS.RANGE_ERROR_LOW_FLAG, bg.detail.flags)) {
        value = 19;
        annotate.annotateEvent(postbg, {code: 'bg/out-of-range', value: 'low', threshold: 20});
      }
      if (hasFlag(BG_FLAGS.RANGE_ERROR_HIGH_FLAG, bg.detail.flags)) {
        value = bgMeter.highest + 1;
        annotate.annotateEvent(postbg, {code: 'bg/out-of-range', value: 'high', threshold: bgMeter.highest});
      }
      postbg = postbg.with_value(value)
        .done();
      postrecords.push(postbg);
      if (bg.user_tag_1 || bg.user_tag_2) {
        var s = bg.user_tag_1 + ' ' + bg.user_tag_2;
        var note = cfg.builder.makeNote()
          .with_value(s)
          .with_time(bg.timestamp)
          .with_deviceTime(bg.deviceTime)
          .with_timezoneOffset(bg.timezoneOffset)
          .done();
        debug('Not storing note:', s);
      }
    }
    return records.concat(postrecords);
  };

  var buildSettingsRecord = function (data, records) {
    var downloads = findSpecificRecords(data.log_records, [
      getValueForName(logRecords, 'Download')
    ]);
    // there can in very rare circumstances be more than one download
    // so we always take the last (most current) one
    var download = data.log_records[downloads[downloads.length - 1]];

    var bgunits = ['mg/dL', 'mmol/L'][data.eeprom_settings.BG_DISPLAY];

    var schedules = {};
    var generateSchedule = function (prof_idx) {
      var keyname = getItemWithValue(PROFILES, 'keyname', 'value', data.profiles[prof_idx].profile_idx);
      var valname = getItemWithValue(PROFILES, 'valuename', 'value', data.profiles[prof_idx].profile_idx);
      var sked = [];
      var addStep = function (idx) {
        var o = { start: data.profiles[prof_idx].steps[idx].starttime };
        o[keyname] = data.profiles[prof_idx].steps[idx][valname];
        sked.push(o);
      };
      addStep(0);
      for (var j = 1; j < data.profiles[prof_idx].steps.length; ++j) {
        if (data.profiles[prof_idx].steps[j][valname] != sked[sked.length - 1][keyname]) {
          addStep(j);
        }
      }
      return sked;
    };
    var settings = {};
    for (var i = 0; i < data.profiles.length; ++i) {
      if (getItemWithValue(PROFILES, 'isBasal', 'value', data.profiles[i].profile_idx)) {
        if (data.profiles[i].name) {
          schedules[data.profiles[i].name] = generateSchedule(i);
        }
      } else {
        settings[getNameForValue(PROFILES, data.profiles[i].profile_idx)] = generateSchedule(i);
      }
    }
    var bgSettingsByStart = {};
    for (var j = 0; j < settings.bgTarget.length; ++j) {
      var target = settings.bgTarget[j];
      bgSettingsByStart[target.start] = {target: target};
    }
    for (var k = 0; k < settings.bgThreshold.length; ++k) {
      var threshold = settings.bgThreshold[k];
      if (bgSettingsByStart[threshold.start]) {
        bgSettingsByStart[threshold.start].threshold = threshold;
      }
      else {
        bgSettingsByStart[threshold.start] = {threshold: threshold};
      }
    }

    var bgTargetSettings = [];
    var starts = _.sortBy(Object.keys(bgSettingsByStart), function(start) { return start; });

    for (var l = 0; l < starts.length; ++l) {
      var currentStart = starts[l];
      var start;
      // find the current, or most recent previous target or threshold
      // if there isn't a new one for this start time
      for (var m = l; m >= 0; --m) {
        if (bgSettingsByStart[starts[m]].target) {
          start = starts[m];
          break;
        }
      }
      var thisTarget = bgSettingsByStart[start].target;

      for (var n = l; n >= 0; --n) {
        if (bgSettingsByStart[starts[n]].threshold) {
          start = starts[n];
          break;
        }
      }
      var thisThreshold = bgSettingsByStart[start].threshold;

      bgTargetSettings.push({
        start: parseInt(currentStart, 10),
        target: thisTarget.low,
        high: thisThreshold.amount
      });
    }

    var postsettings = cfg.builder.makeSettings()
      .with_activeSchedule(data.basalPrograms.names[data.basalPrograms.enabled_idx].name)
      .with_units({ carb: 'grams', bg: bgunits })
      .with_basalSchedules(schedules)
      .with_carbRatio(settings.carbRatio)
      .with_insulinSensitivity(settings.insulinSensitivity)
      .with_bgTarget(bgTargetSettings)
      .with_time(download.timestamp)
      .with_deviceTime(download.deviceTime)
      .with_timezoneOffset(download.timezoneOffset)
      .done();

    records.push(postsettings);
    return records;
  };

  return {
    setup: function (deviceInfo, progress, cb) {
      debug('Insulet Setup!');
      progress(0);
      var data = {stage: 'setup'};
      if (cfg.filename && cfg.filedata) {
        buf = cfg.filedata;
        bytes = new Uint8Array(buf);
        progress(100);
        return cb(null, data);
      } else {
        progress(100);
        return cb('No filedata available!', null);
      }
    },

    connect: function (progress, data, cb) {
      debug('Insulet Connect!');
      // let's do a validation pass
      data.stage = 'connect';
      progress(0);
      var done = false;
      var offset = 0;
      data.npackets = 0;
      while (!done) {
        var rec = getRecord(offset);
        if (!rec.valid) {
          return cb('Checksum error', rec);
        }
        ++data.npackets;
        offset += rec.packetlen;
        if (offset >= bytes.length) {
          done = true;
        }
      }

      // we made it through
      progress(100);
      return cb(null, data);
    },

    getConfigInfo: function (progress, data, cb) {
      debug('Insulet GetConfigInfo!');
      data.stage = 'getConfigInfo';
      progress(0);
      var offset = 0;
      data.ibf_version = getFixedRecord('ibf_version', offset);
      offset += data.ibf_version.packetlen;

      progress(10);
      data.pdm_version = getFixedRecord('pdm_version', offset);
      offset += data.pdm_version.packetlen;

      progress(20);
      data.mfg_data = getManufacturingData(offset);
      offset += data.mfg_data.packetlen;

      progress(30);
      data.basalPrograms = getBasalProgramNames(offset);
      offset += data.basalPrograms.packetlen;

      progress(50);
      data.eeprom_settings = getFixedRecord('eeprom_settings', offset);
      offset += data.eeprom_settings.packetlen;

      progress(70);
      data.profiles = getProfiles(offset, data.basalPrograms.names);
      offset += data.profiles.packetlen;

      progress(80);
      data.logDescriptions = getLogDescriptions(offset);
      offset += data.logDescriptions.packetlen;
      data.independent_offset = offset;

      progress(100);
      return cb(null, data);
    },

    fetchData: function (progress, data, cb) {
      debug('Insulet FetchData!');
      data.stage = 'fetchData';
      progress(0);
      var log_records = getLogRecords(data);
      data.log_records = log_records;
      progress(100);
      return cb(null, data);
    },

    processData: function (progress, data, cb) {
      debug('Insulet ProcessData!');
      data.stage = 'processData';
      // basal and profiles are processed while being loaded
      var recnames = ['ibf_version', 'pdm_version', 'mfg_data', 'eeprom_settings'];
      for (var i = 0; i < recnames.length; ++i) {
        if (fixedRecords[recnames[i]].postprocess) {
          var err = fixedRecords[recnames[i]].postprocess(data[recnames[i]]);
          if (err) {
            return cb(err, null);
          }
        }
      }
      progress(100);
      return cb(null, data);
    },

    uploadData: function (progress, data, cb) {
      debug('Insulet UploadData!');
      data.stage = 'uploadData';
      var insuletDeviceId = data.ibf_version.vendorid.slice(0,3) + data.ibf_version.productid.slice(0,3) + '-' + data.eeprom_settings.REMOTE_ID.toString();

      cfg.builder.setDefaults({ deviceId: insuletDeviceId });

      var postrecords = [], settings = null;
      postrecords = buildSettingsRecord(data, postrecords);
      if (!_.isEmpty(postrecords)) {
        settings = postrecords[0];
      }
      // order of these matters (we use it to ensure the secondary sort order)
      postrecords = buildTimeChangeRecords(data, postrecords);
      postrecords = buildActivationRecords(data, postrecords);
      postrecords = buildAlarmRecords(data, postrecords);
      postrecords = buildOcclusionRecords(data, postrecords);
      postrecords = buildSuspendRecords(data, postrecords);
      postrecords = buildResumeRecords(data, postrecords);
      postrecords = buildBasalRecords(data, postrecords);
      postrecords = buildBolusRecords(data, postrecords);
      postrecords = buildBolusTerminations(data, postrecords);
      postrecords = buildBGRecords(data, postrecords);
      // first sort by log index
      postrecords = _.sortBy(postrecords, function(d) {
        return d.index;
      });
      // then filter out records when the time is out of sequence compared to the index
      // this happens when the PDM is reset and undergoes an automatic time change
      // back to 1/1/2007, for which the appropriate reaction is D:
      var filteredrecords = [], first_time, current_time;
      for (var i = 0; i < postrecords.length; ++i) {
        var rec = postrecords[i];
        if (i === 0) {
          first_time = rec.time;
          current_time = rec.time;
          filteredrecords.push(rec);
        }
        else {
          if (rec.index != null) {
            if (rec.time >= current_time) {
              filteredrecords.push(rec);
              current_time = rec.time;
            }
            else if (rec.time < current_time && rec.time >= first_time) {
              annotate.annotateEvent(rec, 'time-change');
              filteredrecords.push(rec);
            }
            // the `else` here is when rec.time < current_time
            // AND rec.time < first_time, which means it's something like
            // a PDM reset back to 1/1/2007 === something we just want to toss
            else {
              debug('Removing event at', rec.time);
            }
          }
          else {
            if (rec.time >= first_time) {
              filteredrecords.push(rec);
              current_time = rec.time;
            }
          }
        }
      }
      // finally sort by time, including indexed (history) and non-indexed records
      filteredrecords = _.sortBy(filteredrecords, function(d) { return d.time; });
      var simulator = insuletSimulatorMaker.make({settings: settings});
      for (var j = 0; j < filteredrecords.length; ++j) {
        var datum = filteredrecords[j];
        switch (datum.type) {
          case 'basal':
            simulator.basal(datum);
            break;
          case 'bolus':
            simulator.bolus(datum);
            break;
          case 'termination':
            if (datum.subType === 'bolus') {
              simulator.bolusTermination(datum);
            }
            break;
          case 'deviceMeta':
            if (datum.subType === 'status') {
              if (datum.status === 'suspended') {
                simulator.suspend(datum);
              }
              else if (datum.status === 'resumed') {
                if (datum.reason === 'new_pod') {
                  simulator.podActivation(datum);
                }
                else {
                  simulator.resume(datum);
                }
              }
              else {
                debug('Unknown deviceMeta status!', datum.status);
              }
            }
            else if (datum.subType === 'alarm') {
              simulator.alarm(datum);
            }
            else if (datum.subType === 'reservoirChange') {
              simulator.changeReservoir(datum);
            }
            else if (datum.subType === 'timeChange') {
              simulator.changeDeviceTime(datum);
            }
            else {
              debug('deviceMeta of subType %s not passed to simulator!', datum.subType);
            }
            break;
          case 'settings':
            simulator.settings(datum);
            break;
          case 'smbg':
            simulator.smbg(datum);
            break;
          case 'wizard':
            simulator.wizard(datum);
            break;
          default:
            debug('[Hand-off to simulator] Unhandled type!', datum.type);
        }
      }
      simulator.finalBasal();
      data.post_records = [];

      var sessionInfo = {
        deviceTags: ['insulin-pump', 'bgm'],
        deviceManufacturers: ['Insulet', 'Abbott'],
        deviceModel: data.ibf_version.productid,
        deviceSerialNumber: String(data.eeprom_settings.REMOTE_ID),
        deviceId: insuletDeviceId,
        start: sundial.utcDateString(),
        tzName: cfg.timezone,
        version: cfg.version
      };

      cfg.api.upload.toPlatform(
        simulator.getEvents(),
        sessionInfo,
        progress,
        cfg.groupId,
        function (err, result) {
          if (err) {
            debug(err);
            progress(100);
            return cb(err, data);
          } else {
            progress(100);
            data.post_records = data.post_records.concat(postrecords);
            return cb(null, data);
          }
      });

    },

    disconnect: function (progress, data, cb) {
      debug('Insulet Disconnect!');
      data.stage = 'disconnect';
      progress(100);
      return cb(null, data);
    },

    cleanup: function (progress, data, cb) {
      debug('Insulet Cleanup!');
      data.stage = 'cleanup';
      progress(100);
      delete data.stage;
      return cb(null, data);
    }
  };
};
