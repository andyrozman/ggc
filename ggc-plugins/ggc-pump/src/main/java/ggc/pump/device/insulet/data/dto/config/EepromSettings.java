package ggc.pump.device.insulet.data.dto.config;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;
import ggc.pump.device.insulet.util.InsuletUtil;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 19.05.15.
 */

public class EepromSettings extends ConfigRecord
{

    Double bolusIncrement;
    Double bolusMax;
    Double basalMax;
    Double lowVolume;
    Byte autoOff;
    Byte language;
    Byte expireAlert;
    Byte bgReminder;
    Byte configurationAlert;
    Byte reminderAlert;
    Integer serialNumber;
    Byte temporaryBasalType;
    Byte externalBolusType;
    Byte bolusReminder;
    Byte bolusCalculations;
    Byte bolusCalculationsReverse;
    Byte bgDisplay;
    Byte bgSound;
    Short bgMin;
    Short bgGoalLow;
    Short bgGoalUp;
    String insulinDuration;
    Byte alarmRepairCount;
    Integer pdmConfig;
    private GlucoseUnitType glucoseUnitType;


    public EepromSettings()
    {
        super(true);
    }


    @Override
    public void customProcess(int[] data)
    {
        // 13. 4i 2b 4. b 5.b.bb8.i19.7b3sb19.bi

        // 13. 4i
        bolusIncrement = toDecimal(getIntInverted(15));
        bolusMax = toDecimal(getIntInverted(19));
        basalMax = toDecimal(getIntInverted(23));
        lowVolume = toDecimal(getIntInverted(27));

        // 2b
        autoOff = getByte(31);
        language = getByte(32);
        // 4. b
        expireAlert = getByte(37);
        // 5. b
        bgReminder = getByte(43);
        // . bb
        configurationAlert = getByte(45);
        reminderAlert = getByte(46);

        // 8. i
        serialNumber = getIntInverted(55);

        // 19. 7b // 55 65 74
        temporaryBasalType = getByte(78);
        externalBolusType = getByte(79);
        bolusReminder = getByte(80);
        bolusCalculations = getByte(81);
        bolusCalculationsReverse = getByte(82);
        bgDisplay = getByte(83);
        glucoseUnitType = GlucoseUnitType.getByCode(bgDisplay == 0 ? 1 : 2);
        bgSound = getByte(84);

        // 3s b
        bgMin = getShortInverted(85);
        bgGoalLow = getShortInverted(87);
        bgGoalUp = getShortInverted(89);
        insulinDuration = DataAccessPump.getTimeFromMinutes((getByte(91) - 1) * 30);

        // 19. b i // 88 98 107
        alarmRepairCount = getByte(111);
        pdmConfig = getIntInverted(112);
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.EEpromSettings;
    }


    @Override
    public String toString()
    {
        return "EepromSettings [" + "bolusIncrement=" + bolusIncrement + ", bolusMax=" + bolusMax + ", basalMax="
                + basalMax + ", lowVolume=" + lowVolume + ", autoOff=" + autoOff + ", language=" + language
                + ", expireAlert=" + expireAlert + ", bgReminder=" + bgReminder + ", configurationAlert="
                + configurationAlert + ", reminderAlert=" + reminderAlert + ", serialNumber='" + serialNumber + '\''
                + ", \ntemporaryBasalType=" + temporaryBasalType + ", externalBolusType=" + externalBolusType
                + ", bolusReminder=" + bolusReminder + ", bolusCalculations=" + bolusCalculations
                + ", bolusCalculationsReverse=" + bolusCalculationsReverse + ", bgSound=" + bgSound + ", bgMin=" + bgMin
                + ", bgGoalLow=" + bgGoalLow + ", bgGoalUp=" + bgGoalUp + ", insulinDuration='" + insulinDuration + '\''
                + ", \nalarmRepairCount=" + alarmRepairCount + ", pdmConfig=" + pdmConfig + ", glucoseUnitType="
                + glucoseUnitType + ']';
    }


    @Override
    public void writeConfigData()
    {
        writeSetting("PCFG_BOLUS_INCREMENT", getDecimalString(bolusIncrement, 2), bolusIncrement,
            PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_MAX_BOLUS", getDecimalString(bolusMax, 2), bolusMax, PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_MAX_BASAL", getDecimalString(basalMax, 2), basalMax, PumpConfigurationGroup.Basal);

        writeSetting("PCFG_OMNI_MIN_LIMIT_INS_RESERVE", getDecimalString(lowVolume, 2), lowVolume,
            PumpConfigurationGroup.Bolus);

        writeSetting("PCFG_AUTOOFF_ENABLED", InsuletUtil.getBooleanValue(autoOff), autoOff,
            PumpConfigurationGroup.General);

        writeSetting("PCFG_OMNI_ALERT_POD_EXPIRE", InsuletUtil.getBooleanValue(expireAlert), expireAlert,
            PumpConfigurationGroup.Bolus);

        writeSetting("PCFG_BG_REMINDER_ENABLED", InsuletUtil.getBooleanValue(bgReminder), bgReminder,
            PumpConfigurationGroup.Bolus);

        writeSetting("PCFG_OMNI_CONFIGURATION_ALERT", InsuletUtil.getBooleanValue(configurationAlert),
            configurationAlert, PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_OMNI_REMINDER_ALERT", InsuletUtil.getBooleanValue(reminderAlert), reminderAlert,
            PumpConfigurationGroup.Bolus);

        writeSetting("CFG_BASE_SERIAL_NUMBER", "" + serialNumber, serialNumber, PumpConfigurationGroup.Device);
        writeSetting("PCFG_BOLUS_REMINDER", InsuletUtil.getBooleanValue(bolusReminder), bolusReminder,
            PumpConfigurationGroup.Bolus);
        writeSetting("PCFG_BOLUS_WIZARD_ENABLED", InsuletUtil.getBooleanValue(bolusCalculations), bolusCalculations,
            PumpConfigurationGroup.Bolus);

        writeSetting("CFG_BASE_GLUCOSE_UNIT", glucoseUnitType.getTranslation(), bgDisplay,
            PumpConfigurationGroup.General);
        writeSetting("PCFG_BG_ALARM_ENABLED", InsuletUtil.getBooleanValue(bgSound), bgSound,
            PumpConfigurationGroup.General);

        writeSetting("PCFG_BG_MINIMAL", "" + bgMin, bgMin, PumpConfigurationGroup.General);
        writeSetting("PCFG_BG_LOW_TARGET", "" + bgGoalLow, bgGoalLow, PumpConfigurationGroup.General);
        writeSetting("PCFG_BG_HIGH_TARGET", "" + bgGoalUp, bgGoalUp, PumpConfigurationGroup.General);

        writeSetting("PCFG_INSULIN_DURATION", insulinDuration, insulinDuration, PumpConfigurationGroup.Insulin);
    }

}
