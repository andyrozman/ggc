package ggc.pump.device.minimed.data.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.atech.utils.data.CodeEnum;

import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     PumpHistoryEntryType
 *  Description:  Pump History Entry Type.
 *
 *  Data is from several sources, so in comments there are "versions".
 *  Version:
 *  v1 - default doc from decoding-carelink
 *  v2 - nightscout code
 *  v3 - testing
 *  v4 - Andy testing (?)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum PumpHistoryEntryType implements CodeEnum
{

    None(0, "None", 1, 0, 0), //
    // Bolus(0x01, "Bolus", 4, 5, 4), // 4,5,0 -> 4,5,4
    // Bolus(0x01, "Bolus", 2, 5, 4),

    // 523+[H=8]
    Bolus(0x01, "Bolus", 4, DateFormat.LongDate, 0),

    Prime(0x03, "Prime", 5, 5, 0), //
    NoDeliveryAlarm(0x06, "NoDelivery", 4, 5, 0), //

    // V1: 5/5/41 V2: 5,2,3 V3, 5,2,0
    EndResultTotals(0x07, "ResultTotals", 5, 2, 0),

    // V1: 2,5,42 V2:2,5,145; V4:
    ChangeBasalProfile_OldProfile(0x08, "ChangeBasalProfile", 17, 5, 0), //

    // unknown
    ChangeBasalProfile_NewProfile(0x09, "ChangeBasalProfile", 2, 5, 145), //

    Andy10(0x10, "Unknown Andy", 29, 5, 0),

    CalBGForPH(0x0a, "CalBGForPH"), //

    // was Ian08
    SensorAlert(0x0b, "SensorAlert - CGMS", 3, 5, 0), // Ian08
    ClearAlarm(0x0c, "ClearAlarm"), //

    Andy0d(0x0d, "Unknown", 2, 5, 0),

    SelectBasalProfile(0x14, "SelectBasalProfile"), //
    TempBasalDuration(0x16, "TempBasalDuration"), //
    ChangeTime(0x17, "ChangeTime"), //
    NewTimeSet(0x18, "NewTimeSet"), //
    LowBattery(0x19, "LowBattery"), //
    BatteryActivity(0x1a, "Battery Activity"), //
    // case setAutoOff = 0x1b
    PumpSuspend(0x1e, "PumpSuspend"), //
    PumpResume(0x1f, "PumpResume"), //
    // case selftest = 0x20
    Rewind(0x21, "Rewind"), //
    // case clearSettings = 0x22
    //changeChildBlockEnable((byte) 0x23, ChangeChildBlockEnablePumpEvent.class),  //
    changeMaxBolus(0x24, "Unknown", 2, 5, 0), //
    ToggleRemote(0x26, "EnableDisableRemote", 2, 5, 14), //
    ChangeRemoteId(0x27, "ChangeRemoteID"), // ??
    //                case changeMaxBasal = 0x2c
    // V3 ?
    BolusWizardEnabled(0x2d, "BolusWizardEnabled"),
    //            case changeBGReminderOffset = 0x31
    //            case changeAlarmClockTime = 0x32
    TempBasalRate(0x33, "TempBasal", 2, 5, 1), //
    LowReservoir(0x34, "LowReservoir"), //

    //            case changeMeterId = 0x36
//            case questionable3b = 0x3b
    // V3 ?
    ChangeParadigmLinkID(0x3c, "ChangeParadigmLinkID"), //

    BGReceived(0x3f, "BGReceived/Ian3F", 2, 5, 3), // Ian3F
    //             case journalEntryMealMarker = 0x40
    //JournalEntryExerciseMarker((byte) 0x41, JournalEntryExerciseMarkerPumpEvent.class),  //
    //JournalEntryInsulinMarker((byte) 0x42, Unknown7ByteEvent1.class),  //
    //journalEntryOtherMarker((byte) 0x43, InsulinMarkerEvent.class),  //
    ChangeBolusWizardSetup(0x4f, "", 0, 0, 0), //

//    changeSensorSetup2((byte) 0x50, ChangeSensorSetup2PumpEvent.class),  //
    //                case restoreMystery51 = 0x51
//            case restoreMystery52 = 0x52
//            case changeSensorAlarmSilenceConfig = 0x53
//            case restoreMystery54 = 0x54
//            case restoreMystery55 = 0x55
//    ChangeSensorRateOfChangeAlertSetup((byte) 0x56, ChangeSensorRateOfChangeAlertSetupPumpEvent.class),  //
//    ChangeBolusScrollStepSize((byte) 0x57, ChangeBolusScrollStepSizePumpEvent.class),  //


    // V4
    Andy58(0x58, "Unknown", 13, 5, 0), // TODO is this one really there

    // V2: 522+[B=143]
    BolusWizardChange(0x5a, "BolusWizard", 2, 5, 117), //

    // V2: 523+[B=15]
    BolusWizardBolusEstimate(0x5b, "BolusWizard", 2, 5, 13), // 15

    // head[1] -> body length
    UnabsorbedInsulin(0x5c, "UnabsorbedInsulinBolus", 5, 0, 0),

    //             case saveSettings = 0x5d
    //changeVariableBolus((byte) 0x5e, ChangeVariableBolusPumpEvent.class),  //

    // V3 ?
    changeAudioBolus(0x5f, "EasyBolusEnabled"), //

    // questionable60(0x60), //
//    ChangeBGReminderEnable((byte) 0x60, ChangeBGReminderEnablePumpEvent.class), //
//    ChangeAlarmClockEnable((byte) 0x61, ChangeAlarmClockEnablePumpEvent.class),  //
//    ChangeTempBasalType((byte) 0x62, ChangeTempBasalTypePumpEvent.class),  //

    ChangeAlarmNotifyMode(0x63, "ChangeUtility"), // ChangeUtility

    ChangeTimeDisplay(0x64, "ChangeTimeDisplay"), //

//    ChangeTempBasalType((byte) 0x62, ChangeTempBasalTypePumpEvent.class),  //
//    ChangeAlarmNotifyMode((byte) 0x63, ChangeAlarmNotifyModePumpEvent.class),  //
//    ChangeTimeFormat((byte) 0x64, ChangeTimeFormatPumpEvent.class),  //
//    ChangeReservoirWarningTime((byte) 0x65, ChangeReservoirWarningTimePumpEvent.class),  //
//    ChangeBolusReminderEnable((byte) 0x66, ChangeBolusReminderEnablePumpEvent.class),  //
//    ChangeBolusReminderTime((byte) 0x67, ChangeBolusReminderTimePumpEvent.class),  //
//    DeleteBolusReminderTime((byte) 0x68, DeleteBolusReminderTimePumpEvent.class),  //
//    // case bolusReminder = 0x69
//    DeleteAlarmClockTime((byte) 0x6a, DeleteAlarmClockTimePumpEvent.class),  //
//    // case dailyTotal515 = 0x6c

    DailyTotals512(0x6c, "Daily Totals 512", 0, 0, 36), //

    // hack1(0x6d, "hack1", 46, 5, 0), //
    // V2: 1,2,41 V3:
    DailyTotals522(0x6d, "Daily Totals 522", 1, 2, 41), //
    DailyTotals523(0x6e, "Daily Totals 523", 1, 2, 49), // 1102014-03-17T00:00:00
    // ChangeCarbUnits((byte) 0x6f, ChangeCarbUnitsPumpEvent.class),  //

    // -
    // 722
    BasalProfileStart(0x7b, "BasalProfileStart", 2, 5, 3), //
//    ChangeWatchdogEnable((byte) 0x7c, ChangeWatchdogEnablePumpEvent.class), //
//    ChangeOtherDeviceID((byte) 0x7d, ChangeOtherDeviceIDPumpEvent.class),  //
//    ChangeWatchdogMarriageProfile((byte) 0x81, ChangeWatchdogMarriageProfilePumpEvent.class),  //
//    DeleteOtherDeviceID((byte) 0x82, DeleteOtherDeviceIDPumpEvent.class),  //
//    ChangeCaptureEventEnable((byte) 0x83, ChangeCaptureEventEnablePumpEvent.class),

    Ian69(0x69, "xx", 1, 5, 2), //

    Ian50(0x50, "xx", 1, 5, 34), //

    Ian54(0x54, "xx", 10, 5, 57), //

    IanA8(0xA8, "xx", 10, 5, 0), //

    Andy90(0x90, "Unknown", 7, 5, 0),

    AndyB4(0xb4, "Unknown", 7, 5, 0),
    // Andy4A(0x4a, "Unknown", 5, 5, 0),

    // head[1],
    // body[49] op[0x6e]

    UnknownBasePacket(0xFF, "Unknown Base Packet");

    // private PumpHistoryEntryType(String description, List<Integer> opCode,
    // byte length)
    // {
    //
    // }

    private int opCode;
    private String description;
    private int headLength = 0;
    private int dateLength;
    private int bodyLength;
    private int totalLength;
    // private MinimedDeviceType deviceType;

    // special rules need to be put in list from highest to lowest (e.g.:
    // 523andHigher=12, 515andHigher=10 and default (set in cnstr) would be 8)
    private List<SpecialRule> specialRulesHead;
    private List<SpecialRule> specialRulesBody;
    private boolean hasSpecialRules = false;

    private static Map<Integer, PumpHistoryEntryType> opCodeMap = new HashMap<Integer, PumpHistoryEntryType>();

    static
    {
        for (PumpHistoryEntryType type : values())
        {
            opCodeMap.put(type.opCode, type);
        }

        setSpecialRulesForEntryTypes();
    }




    static void setSpecialRulesForEntryTypes()
    {
        Bolus.addSpecialRuleHead(new SpecialRule(MinimedDeviceType.Minimed_523andHigher, 8));
        BolusWizardChange.addSpecialRuleBody(new SpecialRule(MinimedDeviceType.Minimed_522andHigher, 143));
        BolusWizardBolusEstimate.addSpecialRuleBody(new SpecialRule(MinimedDeviceType.Minimed_523andHigher, 15));
    }


    PumpHistoryEntryType(int opCode, String name)
    {
        this(opCode, name, 2, 5, 0);
    }


    PumpHistoryEntryType(int opCode, String name, int head, DateFormat dateFormat, int body) {
        this(opCode, name, head, dateFormat.getLength(), body);
    }

    PumpHistoryEntryType(int opCode, String name, int head, int date, int body)
    {
        this.opCode = (byte) opCode;
        this.description = name;
        this.headLength = head;
        this.dateLength = date;
        this.bodyLength = body;
        this.totalLength = (head + date + body);
    }


    public int getCode()
    {
        return this.opCode;
    }


    //
    // private PumpHistoryEntryType(int opCode, String name, int head, int date,
    // int body)
    // {
    // this.opCode = (byte) opCode;
    // this.description = name;
    // this.headLength = head;
    // this.dateLength = date;
    // this.bodyLength = body;
    // this.totalLength = (head + date + body);
    // }
    //

    public int getTotalLength()
    {
        if (hasSpecialRules())
        {
            return getHeadLength() + getBodyLength() + getDateLength();
        }
        else
        {
            return totalLength;
        }
    }


    private boolean hasSpecialRules()
    {
        return hasSpecialRules;
    }


    void addSpecialRuleHead(SpecialRule rule)
    {
        if (CollectionUtils.isEmpty(specialRulesHead))
        {
            specialRulesHead = new ArrayList<SpecialRule>();
        }

        specialRulesHead.add(rule);
        hasSpecialRules = true;
    }


    void addSpecialRuleBody(SpecialRule rule)
    {
        if (CollectionUtils.isEmpty(specialRulesBody))
        {
            specialRulesBody = new ArrayList<SpecialRule>();
        }

        specialRulesBody.add(rule);
        hasSpecialRules = true;
    }


    public static PumpHistoryEntryType getByCode(int opCode)
    {
        if (opCodeMap.containsKey(opCode))
        {
            return opCodeMap.get(opCode);
        }
        else
        {
            return PumpHistoryEntryType.UnknownBasePacket;
        }
    }


    public int getOpCode()
    {
        return opCode;
    }


    public String getDescription()
    {
        return description;
    }


    public int getHeadLength()
    {
        if (hasSpecialRules)
        {
            if (CollectionUtils.isNotEmpty(specialRulesHead))
            {
                return determineSizeByRule(headLength, specialRulesHead);
            }
            else
            {
                return headLength;
            }
        }
        else
        {
            return headLength;
        }
    }


    public int getDateLength()
    {
        return dateLength;
    }


    public int getBodyLength()
    {
        if (hasSpecialRules)
        {
            if (CollectionUtils.isNotEmpty(specialRulesBody))
            {
                return determineSizeByRule(bodyLength, specialRulesBody);
            }
            else
            {
                return bodyLength;
            }
        }
        else
        {
            return bodyLength;
        }
    }


    private int determineSizeByRule(int defaultValue, List<SpecialRule> rules)
    {
        int size = defaultValue;

        for (SpecialRule rule : rules)
        {
            if (MinimedDeviceType.isSameDevice(MedtronicUtil.getDeviceType(), rule.deviceType))
            {
                size = rule.size;
                break;
            }
        }

        return size;
    }

    // byte[] dh = { 2, 3 };

    public static class SpecialRule
    {

        MinimedDeviceType deviceType;
        int size;


        public SpecialRule(MinimedDeviceType deviceType, int size)
        {
            this.deviceType = deviceType;
            this.size = size;
        }
    }


    enum DateFormat
    {
        None(0), //
        LongDate(5), //
        ShortDate(2);

        private int length;

        DateFormat(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }

}
