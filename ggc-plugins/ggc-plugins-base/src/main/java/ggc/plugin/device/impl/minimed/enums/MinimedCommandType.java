package ggc.plugin.device.impl.minimed.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ggc.plugin.device.impl.minimed.data.MinimedCommandTypeInterface;

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
 *  Filename:     MinimedCommandType
 *  Description:  Minimed Command Type.
 *
 *  Author: Andy {andy@atech-software.com}
 */

// Lot of stuff in here is legacy stuff for CareLink(1). We should remove it in
// future.
public enum MinimedCommandType implements Serializable, MinimedCommandTypeInterface
{
    // All (9)
    CommandAck(6, "Acknowledge", MinimedTargetType.ActionCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    PushAck(91, "Push ACK", MinimedTargetType.ActionCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(2)), //

    PushEsc(91, "Push Esc", MinimedTargetType.ActionCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(1)), //

    RFPowerOn(93, "RF Power On", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(1, 10)), //

    // RFPowerOn(93, "RF Power On", MinimedTargetType.InitCommand,
    // MinimedDeviceType.All,
    // MinimedCommandParameterType.NoParameters),

    RFPowerOff(93, "RF Power Off", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(0, 0)), //

    SetSuspend(77, "Set Suspend", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(1)), //

    CancelSuspend(77, "Cancel Suspend", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.FixedParameters, getByteArray(0)), //

    PumpState(131, "Pump State", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    ReadPumpErrorStatus(117, "Pump Error Status", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    // 511 (InitCommand = 2, Config 7, Data = 1(+3)
    DetectBolus(75, "Detect Bolus", MinimedTargetType.InitCommand, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.FixedParameters, getByteArray(0, 0, 0)), //

    ReadTemporaryBasal_511(120, "Read Temporary Basal", MinimedTargetType.InitCommand, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.NoParameters), //

    RemoteControlIds(118, "Remote Control Ids", MinimedTargetType.PumpConfiguration_NA, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    FirmwareVersion(116, "Firmware Version", MinimedTargetType.InitCommand, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    PumpId(113, "Pump Id", MinimedTargetType.PumpConfiguration, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), // init

    RealTimeClock(112, "Real Time Clock", MinimedTargetType.PumpConfiguration, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), // 0x70

    BatteryStatus(127, "Read Battery Status", MinimedTargetType.PumpConfiguration, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters), //

    RemainingInsulin(115, "Read Remaining Insulin", MinimedTargetType.PumpConfiguration, MinimedDeviceType.All,
            MinimedCommandParameterType.NoParameters),

    Settings_511(127, "Settings", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.NoParameters), //

    HistoryData_511(128, "History data", MinimedTargetType.PumpData, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.SubCommands, 1024, 32, 0), // 0x80

    Profile_STD_511(122, "Profile Standard", MinimedTargetType.PumpDataAndConfiguration, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.NoParameters, 128, 1, 8), // FIXME_

    Profile_A_511(123, "Profile A", MinimedTargetType.PumpDataAndConfiguration, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.NoParameters, 128, 1, 9), // FIXME_

    Profile_B_511(124, "Profile B", MinimedTargetType.PumpDataAndConfiguration, MinimedDeviceType.Minimed_511,
            MinimedCommandParameterType.NoParameters, 128, 1, 10), // FIXME_

    // 512
    ReadTemporaryBasal(152, "Read Temporary Basal", MinimedTargetType.InitCommand,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters),

    SetTemporaryBasal(76, "Set Temp Basal Rate (bolus detection only)", MinimedTargetType.InitCommand,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters, getByteArray(0, 0, 0)),
    // util.getCommand(MinimedCommand.SET_TEMPORARY_BASAL).allowedRetries
    // = 0;

    // 512 Config
    PumpModel(141, "Pump Model", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters), // 0x8D

    BGTargets_512(140, "BG Targets", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512_712,
            MinimedCommandParameterType.NoParameters), //

    BGUnits(137, "BG Units", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters), //

    Language(134, "Language", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters), //

    Settings_512(145, "Settings", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512_712,
            MinimedCommandParameterType.NoParameters), //

    BGAlarmClocks(142, "BG Alarm Clocks", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters), //

    BGAlarmEnable(151, "BG Alarm Enable", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters), //

    BGReminderEnable(144, "BG Reminder Enable", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters), //

    InsulinSensitivities(139, "Insulin Sensitivities", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters), //

    // 512 Data
    HistoryData(128, "History data", MinimedTargetType.PumpData, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.SubCommands, 1024, 36, 0), // 0x80
    // new MinimedCommandHistoryData(36)

    Profile_STD(146, "Profile Standard", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters, 192, 1, 8), // FIXME

    Profile_A(147, "Profile A", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters, 192, 1, 9), // FIXME

    Profile_B(148, "Profile B", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_512andHigher,
            MinimedCommandParameterType.NoParameters, 192, 1, 10), // FIXME

    BolusWizardSetupStatus(135, "Bolus Wizard Setup Status", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters), //

    CarbohydrateRatios(138, "Carbohydrate Ratios", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters), //

    CarbohydrateUnits(136, "Carbohydrate Units", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_512andHigher, MinimedCommandParameterType.NoParameters), //

    // 515
    PumpStatus(206, "Pump Status", MinimedTargetType.InitCommand, MinimedDeviceType.Minimed_515andHigher,
            MinimedCommandParameterType.NoParameters), // PumpConfiguration

    Settings(192, "Settings", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_515andHigher,
            MinimedCommandParameterType.NoParameters), //

    BGTargets(159, "BG Targets", MinimedTargetType.PumpConfiguration, MinimedDeviceType.Minimed_515andHigher,
            MinimedCommandParameterType.NoParameters), //

    MissedBolusReminderEnable(197, "Missed Bolus Reminder Enable", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_515andHigher, MinimedCommandParameterType.NoParameters), //

    MissedBolusReminders(198, "Missed Bolus Reminder", MinimedTargetType.PumpConfiguration,
            MinimedDeviceType.Minimed_515andHigher, MinimedCommandParameterType.NoParameters), //

    // 522
    CalibrationFactor(156, "Calibration Factor", MinimedTargetType.CGMSConfiguration,
            MinimedDeviceType.Minimed_522andHigher, MinimedCommandParameterType.NoParameters), //

    SensorSettings_522(153, "Sensor Settings", MinimedTargetType.CGMSConfiguration, MinimedDeviceType.Minimed_522_722,
            MinimedCommandParameterType.NoParameters), //

    GlucoseHistory(154, "Glucose History", MinimedTargetType.CGMSData, MinimedDeviceType.Minimed_522andHigher,
            MinimedCommandParameterType.SubCommands, 1024, 32, 0), //

    ISIGHistory(155, "Isig History", MinimedTargetType.CGMSData_NA, MinimedDeviceType.Minimed_522andHigher,
            MinimedCommandParameterType.SubCommands, 2048, 32, 0), //

    // 523
    SensorPredictiveAlerts(209, "Sensor Predictive Alerts", MinimedTargetType.CGMSConfiguration,
            MinimedDeviceType.Minimed_523andHigher, MinimedCommandParameterType.NoParameters), //

    SensorRateOfChangeAlerts(212, "Sensor Rate Of Change Alerts", MinimedTargetType.CGMSConfiguration,
            MinimedDeviceType.Minimed_523andHigher, MinimedCommandParameterType.NoParameters), //

    SensorDemoAndGraphTimeout(210, "Sensor Demo and Graph Timeout", MinimedTargetType.CGMSConfiguration,
            MinimedDeviceType.Minimed_523andHigher, MinimedCommandParameterType.NoParameters), //

    SensorAlarmSilence(211, "Sensor Alarm Silence", MinimedTargetType.CGMSConfiguration,
            MinimedDeviceType.Minimed_523andHigher, MinimedCommandParameterType.NoParameters), //

    SensorSettings(207, "Sensor Settings", MinimedTargetType.CGMSConfiguration, MinimedDeviceType.Minimed_523andHigher,
            MinimedCommandParameterType.NoParameters), //

    OtherDevicesIds(240, "Other Devices ID", MinimedTargetType.CGMSConfiguration_NA,
            MinimedDeviceType.Minimed_523andHigher, MinimedCommandParameterType.NoParameters), //

    VCntrHistory(213, "Vcntr History", MinimedTargetType.CGMSData_NA, MinimedDeviceType.Minimed_523andHigher,
            MinimedCommandParameterType.SubCommands, 1024, 32, 0), //

    // 553
    // 554

    // var MESSAGES = {
    // READ_TIME : 0x70,
    // READ_BATTERY_STATUS: 0x72,
    // READ_HISTORY : 0x80,
    // READ_CARB_RATIOS : 0x8A,
    // READ_INSULIN_SENSITIVITIES: 0x8B,
    // READ_MODEL : 0x8D,
    // READ_PROFILE_STD : 0x92,
    // READ_PROFILE_A : 0x93,
    // READ_PROFILE_B : 0x94,
    // READ_CBG_HISTORY: 0x9A,
    // READ_ISIG_HISTORY: 0x9B,
    // READ_CURRENT_PAGE : 0x9D,
    // READ_BG_TARGETS : 0x9F,
    // READ_SETTINGS : 0xC0, 192
    // READ_CURRENT_CBG_PAGE : 0xCD
    // };

    ;

    public int commandCode = 0;
    private int recordLength = 64;

    MinimedTargetType targetType;
    MinimedDeviceType devices;

    public String commandDescription = "";

    public byte[] commandParameters = null;
    public int commandParametersCount = 0;

    /**
     * Command: Max Records
     */
    public int maxRecords = 1;

    /**
     * Command: Command Type
     */
    public int command_type = 0;

    /**
     * Command: Allowed Retries
     */
    public int allowedRetries = 2;

    /**
     * Command: Max Timeout (Allowed Time)
     */
    public int maxAllowedTime = 2000;

    public MinimedCommandParameterType parameterType;

    public int minimalBufferSizeToStartReading = 14;

    public byte weirdByte;

    // public boolean hasSubCommands = false;

    static
    {
        MinimedCommandType.RFPowerOn.maxAllowedTime = 17000;
        MinimedCommandType.RFPowerOn.allowedRetries = 0;
        MinimedCommandType.RFPowerOn.recordLength = 0;
        MinimedCommandType.RFPowerOn.minimalBufferSizeToStartReading = 1;
        // MinimedCommandType.RFPowerOn.recordLength = 0; // THIS SHOULDN'T BE
        // NECESSARY

        prepareDataForNextLink();

    }


    private static void prepareDataForNextLink()
    {
        MinimedCommandType.ReadTemporaryBasal.weirdByte = (byte) 0xc6;
        MinimedCommandType.PumpModel.weirdByte = (byte) 0x30;
    }


    MinimedCommandType(int code, String description, MinimedTargetType targetType, MinimedDeviceType devices,
            MinimedCommandParameterType parameterType)
    {
        // this(code, description, targetType, devices, parameterType, 64, 1,
        // 0);
        this(code, description, targetType, devices, parameterType, 64, 1, 0, 0, 0);
    }


    MinimedCommandType(int code, String description, MinimedTargetType targetType, MinimedDeviceType devices,
            MinimedCommandParameterType parameterType, int recordLength, int maxRecords, int commandType)
    {
        this(code, description, targetType, devices, parameterType, recordLength, maxRecords, 0, 0, commandType);

        // m_dataOffset = 0;
        // m_cmdLength = 2;
        // setUseMultiXmitMode(false);
    }


    MinimedCommandType(int code, String description, MinimedTargetType targetType, MinimedDeviceType devices,
            MinimedCommandParameterType parameterType, byte[] cmd_params)
    {
        // Command(int code, String desc, int bytes_per_rec, int max_recs, int
        // cmd_type)
        // this(code, desc, targetType, devices, parameterType, 0, 1, 11);

        this(code, description, targetType, devices, parameterType, 0, 1, 0, 0, 11);

        this.commandParameters = cmd_params;
        this.commandParametersCount = cmd_params.length;
    }


    MinimedCommandType(int code, String description, MinimedTargetType targetType, MinimedDeviceType devices,
            MinimedCommandParameterType parameterType, int recordLength, int max_recs, int addy, int addy_len,
            int cmd_type)
    {
        this.commandCode = code;
        this.commandDescription = description;
        this.targetType = targetType;
        this.devices = devices;
        this.recordLength = recordLength;
        this.maxRecords = max_recs;

        this.command_type = cmd_type;
        this.commandParametersCount = 0;
        this.allowedRetries = 2;
        this.parameterType = parameterType;

        if (this.parameterType == MinimedCommandParameterType.SubCommands)
        {
            this.minimalBufferSizeToStartReading = 200;
        }

    }


    private static HashMap<MinimedDeviceType, String> getDeviceTypesArray(MinimedDeviceType... types)
    {
        HashMap<MinimedDeviceType, String> hashMap = new HashMap<MinimedDeviceType, String>();

        for (MinimedDeviceType type : types)
        {
            hashMap.put(type, null);
        }

        return hashMap;
    }


    private static byte[] getByteArray(int... data)
    {
        byte[] array = new byte[data.length];
        int counter = 0;

        for (int type : data)
        {
            array[counter] = (byte) type;
            counter++;
        }

        return array;
    }


    private static int[] getIntArray(int... data)
    {
        int[] array = new int[data.length];
        int counter = 0;

        for (int type : data)
        {
            array[counter] = type;
            counter++;
        }

        return array;
    }


    public static MinimedCommandType getReadTemporaryBasal(MinimedDeviceType device)
    {
        if (device == MinimedDeviceType.Minimed_508_508c)
        {
            throw new NotImplementedException("508/508c device not supported");
        }
        else if (device == MinimedDeviceType.Minimed_511)
        {
            return MinimedCommandType.ReadTemporaryBasal_511;
        }
        else
        {
            return MinimedCommandType.ReadTemporaryBasal;
        }
    }


    public static MinimedCommandType getDetectBolus(MinimedDeviceType device)
    {
        if (device == MinimedDeviceType.Minimed_508_508c)
        {
            throw new NotImplementedException("508/508c device not supported");
        }
        else if (device == MinimedDeviceType.Minimed_511)
        {
            return MinimedCommandType.DetectBolus;
        }
        else
        {
            return MinimedCommandType.SetTemporaryBasal;
        }
    }


    public static List<MinimedCommandType> getCommands(MinimedDeviceType device, MinimedTargetType targetType)
    {
        List<MinimedCommandType> commands = new ArrayList<MinimedCommandType>();

        for (MinimedCommandType mct : values())
        {

            if ((mct.targetType == targetType)
                    && ((MinimedDeviceType.isSameDevice(device, mct.devices)) || mct.devices == MinimedDeviceType.All))
            {
                commands.add(mct);
            }
        }

        return commands;
    }


    /**
     * Get Full Command Description
     *
     * @return command description
     */
    public String getFullCommandDescription()
    {
        return "Command [name=" + this.name() + ", id=" + this.commandCode + ",description=" + this.commandDescription
                + "] ";
    }


    /**
     * Is Multi Xmit Mode
     *
     * @return MultiXmit mode (only for RFPowerOn)
     */
    public boolean isMultiXmitMode()
    {
        return (this == MinimedCommandType.RFPowerOn);
    }


    public boolean canReturnData()
    {
        System.out.println("CanReturnData: ]id=" + this.name() + "max=" + this.maxRecords + "recLen=" + recordLength);
        return (this.maxRecords * this.recordLength) > 0;
    }


    public int getRecordLength()
    {
        return recordLength;
    }


    public int getMaxRecords()
    {
        return maxRecords;
    }


    public int getCommandCode()
    {
        return commandCode;
    }


    public int getCommandParametersCount()
    {
        if (this.commandParameters == null)
        {
            return 0;
        }
        else
        {
            return this.commandParameters.length;
        }
    }


    public byte[] getCommandParameters()
    {
        return commandParameters;
    }


    public boolean hasCommandParameters()
    {
        return (getCommandParametersCount() > 0);
    }

}
