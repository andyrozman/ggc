package ggc.pump.data.defs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.atech.utils.data.CodeEnum;

import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;

/**
 * Application:   GGC - GNU Gluco Control
 * Plug-in:       GGC PlugIn Base (base class for all plugins)
 * <p/>
 * See AUTHORS for copyright information.
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Filename:     AnimasDataType
 * Description:  Animas Data Type enum, with all important settings for type.
 * <p/>
 * Author: Andy Rozman {andy@atech-software.com}
 */

public enum PumpSettingsType implements CodeEnum
{
    UnknownDataType(0), //
    SerialNumber(8, "Serial Number", false, 1, AnimasTransferType.All), //
    BasalProfile(11, "Basal Profile", false, 4, AnimasTransferType.DownloadPumpSettings), //
    ActiveBasal(12, "Active Basal", false, 1, AnimasTransferType.DownloadPumpSettings), //
    AdvancedSettings(13, "Advanced Settings", false, 1, AnimasTransferType.DownloadPumpSettings), //
    SoundSettings(14, "Sound settings", false, 1, AnimasTransferType.DownloadPumpSettings), //
    BasalProfileName(18, "Basal Profile Name", false, 4, AnimasTransferType.DownloadPumpSettings), //
    PumpSettings1_Pre2020(19, "Pump Name (pre 2020)", false, 1, AnimasTransferType.DownloadPumpSettings, //
            Arrays.asList(AnimasDeviceType.Animas_Family_Pre2200)), //
    PumpSettings2_Pre2020(20, "Pump Profile Name (pre 2020)", false, 4, AnimasTransferType.DownloadPumpSettings, //
            Arrays.asList(AnimasDeviceType.Animas_Family_Pre2200)),

    // 21 - 26
    BolusHistory(21, "Bolus History", true, 500, AnimasTransferType.DownloadPumpData), // 500?
    TotalDailyDoseHistory(22, "Total Daily Dose History", true, 120, AnimasTransferType.DownloadPumpData), //
    AlarmHistory(23, "Alarm History", true, 30, AnimasTransferType.DownloadPumpData), //
    PrimeHistory(24, "Prime History", true, 60, AnimasTransferType.DownloadPumpData), //
    SuspendHistory(25, "Suspend/Resume History", true, 30, AnimasTransferType.DownloadPumpData), //
    BasalRateHistory(26, "Basal Rate History", true, 270, AnimasTransferType.DownloadPumpData), //

    ClockMode(28, "Clock Mode", false, 1, AnimasTransferType.All), //
    BGUnit(29, "BG Unit", false, 1, AnimasTransferType.All), //
    SoftwareCode(30, "Software Code", false, 1, AnimasTransferType.All), //

    FontTableIndex(34, "Font Table Index", false, 1, AnimasTransferType.UploadFoodDb), //
    LanguageIndex(35, "Language Font Index", false, 1, AnimasTransferType.UploadFoodDb), //
    FoodDbSize(36, "Food Db Size", false, 1, AnimasTransferType.All), //

    DosingSettings(37, "Dosing Settings (pre 2020)", false, 1, AnimasTransferType.DownloadPumpSettings, //
            Arrays.asList(AnimasDeviceType.Animas_Family_Pre2200)), // pre 2002

    BolusHistoryExt(38, "BolusHistoryExt", false, 500, AnimasTransferType.DownloadPumpData, Arrays.asList(//
            AnimasDeviceType.Animas_Family_2200, //
            AnimasDeviceType.Animas_Family_Ping, //
            AnimasDeviceType.Animas_Family_Vibe)), //

    // SETTINGS_ALL(39), //
    InsulinCarbRatio, //
    InsulinBGRatio, //
    BGTargets, //
    BasalPatterns, //
    FriendlyName, DeviceSerialAndVersion(), GlucoseUnit(), ActiveBasalPattern();

    // 33215

    static HashMap<Integer, PumpSettingsType> mappingWithId = new HashMap<Integer, PumpSettingsType>();

    static
    {
        for (PumpSettingsType adt : values())
        {
            mappingWithId.put(adt.getCode(), adt);
        }
    }

    int code;
    private boolean isDateInDataType = false;
    AnimasTransferType baseTransferType;
    boolean postProcessing = false;
    private int entriesCount;
    private String debugDescription;

    /**
     * Allowed Device Types. Commands are mostly ties to specific AnimasDeviceType. If this list is null, all devices (1000 is here excluded) can
     * issue the command, if this is set only specific devices will be allowed to issue commands.
     */
    private HashMap<AnimasDeviceType, Object> allowedDeviceTypes = null;


    private PumpSettingsType(int code)
    {
        this.code = code;
    }


    PumpSettingsType()
    {
    }


    private PumpSettingsType(int code, String debugDescription, boolean isDateInDataType, int entriesCount, AnimasTransferType baseTransferType)
    {
        this(code, debugDescription, isDateInDataType, entriesCount, baseTransferType, null);
    }


    private PumpSettingsType(int code, String debugDescription, boolean isDateInDataType, int entriesCount, AnimasTransferType baseTransferType, List<AnimasDeviceType> allowedDevices)
    {
        this(code, debugDescription, isDateInDataType, entriesCount, baseTransferType, allowedDevices, false);
    }


    private PumpSettingsType(int code, String debugDescription, boolean isDateInDataType, int entriesCount, AnimasTransferType baseTransferType, List<AnimasDeviceType> allowedDevices, boolean postProcessing)
    {
        this.code = code;
        this.isDateInDataType = isDateInDataType;
        this.debugDescription = debugDescription;
        this.baseTransferType = baseTransferType;
        this.entriesCount = entriesCount;
        this.postProcessing = postProcessing;

        processDevices(allowedDevices);
    }


    private void processDevices(List<AnimasDeviceType> allowedDevices)
    {
        if (allowedDevices == null)
            return;

        allowedDeviceTypes = new HashMap<AnimasDeviceType, Object>();

        for (AnimasDeviceType adt : allowedDevices)
        {
            if (adt.isFamily())
            {
                for (AnimasDeviceType adt2 : adt.getFamilyMembers())
                {
                    allowedDeviceTypes.put(adt2, null);
                }
            }
            else
            {
                allowedDeviceTypes.put(adt, null);
            }
        }
    }


    public int getEntriesCount()
    {
        return entriesCount;
    }


    public AnimasTransferType getBaseTransferType()
    {
        return baseTransferType;
    }


    public static PumpSettingsType getAnimasDataTypeById(int code)
    {
        if (mappingWithId.containsKey(code))
        {
            return mappingWithId.get(code);
        }
        else
        {
            return PumpSettingsType.UnknownDataType;
        }
    }


    public boolean isDateInDataType()
    {
        return isDateInDataType;
    }


    public int getCode()
    {
        return code;
    }


    public String getDebugDescription()
    {
        return debugDescription;
    }


    public void setDebugDescription(String debugDescription)
    {
        this.debugDescription = debugDescription;
    }


    public boolean isCommandAllowedForDeviceType(AnimasDeviceType deviceType)
    {
        if (this.allowedDeviceTypes == null)
        {
            return true;
        }
        else
        {
            return this.allowedDeviceTypes.containsKey(deviceType);
        }
    }


    public boolean hasPostProcessing()
    {
        return this.postProcessing;
    }

    // (dataType >= 21) && (dataType <= 26)

}
