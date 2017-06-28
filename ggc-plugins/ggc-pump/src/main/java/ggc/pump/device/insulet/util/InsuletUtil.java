package ggc.pump.device.insulet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.BitUtils;

import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.device.insulet.data.dto.config.BasalProgramNames;
import ggc.pump.device.insulet.data.enums.ProfileType;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 09.05.15.
 */
public class InsuletUtil
{

    private static final Logger LOG = LoggerFactory.getLogger(InsuletUtil.class);

    private static OutputWriter outputWriter;
    private static BitUtils bitUtils = new BitUtils();
    private static ClockModeType clockMode;
    private static GlucoseUnitType glucoseUnitType;
    private static DataAccessPump dataAccess;
    private static I18nControlAbstract i18nControl;
    private static String yesValue;
    private static String noValue;
    private static boolean translatedTypes = false;
    private static BasalProgramNames basalProgramNames;


    public static void setDataAccess(DataAccessPump dataAccess_)
    {
        dataAccess = dataAccess_;
        i18nControl = dataAccess.getI18nControlInstance();
    }


    public static OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    public static void setOutputWriter(OutputWriter outputWriter)
    {
        InsuletUtil.outputWriter = outputWriter;
    }


    public static BitUtils getBitUtils()
    {
        return bitUtils;
    }


    public static void setClockMode(ClockModeType clockMode)
    {
        InsuletUtil.clockMode = clockMode;
    }


    public static ClockModeType getClockMode()
    {
        return clockMode;
    }


    public static void setGlucoseUnitType(GlucoseUnitType glucoseUnitType)
    {
        InsuletUtil.glucoseUnitType = glucoseUnitType;
    }


    public static GlucoseUnitType getGlucoseUnitType()
    {
        return glucoseUnitType;
    }


    public static void sleepMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {}
    }


    public static void writeConfiguration(String key, String value, PumpConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(new DeviceValueConfigEntry( //
                dataAccess.getI18nControlInstance().getMessage(key), value, group));
    }


    public static String getBooleanValue(Boolean val)
    {
        checkIfValuesSet();

        return val ? yesValue : noValue;
    }


    private static void checkIfValuesSet()
    {
        if (yesValue == null)
        {
            yesValue = i18nControl.getMessage("YES");
            noValue = i18nControl.getMessage("NO");
        }
    }


    public static String getBooleanValue(int value)
    {
        checkIfValuesSet();

        return value == 0 ? noValue : yesValue;
    }


    public static I18nControlAbstract getI18nControl()
    {
        return i18nControl;
    }


    public static void setI18nControl(I18nControlAbstract i18nControl)
    {
        InsuletUtil.i18nControl = i18nControl;
    }


    public static void translateTypes()
    {
        if (translatedTypes)
            return;

        ProfileType.translateKeywords(i18nControl);
        translatedTypes = true;

    }


    public static void setBasalProgramNames(BasalProgramNames basalProgramNames)
    {
        InsuletUtil.basalProgramNames = basalProgramNames;
    }


    public static BasalProgramNames getBasalProgramNames()
    {
        return basalProgramNames;
    }

    // public static boolean checkHeaderAndCRC(MinimedCommandPacket
    // commandPacket, int arrayData[])
    // throws PlugInBaseException
    // {
    // // FIXME refacor
    // int crcFromRecord = arrayData[arrayData.length - 1];
    // int crcCalculated = bitUtils.computeCRC8(arrayData, 0, (arrayData.length
    // - 1)) & 0xff;
    //
    // if (crcFromRecord != crcCalculated)
    // {
    // LOG.debug("checkHeaderAndCRC: " +
    // commandPacket.getFullCommandDescription()
    // + " resulted in bad CRC value of " + crcFromRecord + " (expected " +
    // crcCalculated + ") "
    // + "(byte data = " + "<" + bitUtils.getHex(arrayData) + ">)");
    // throw new PlugInBaseException("checkHeaderAndCRC: " +
    // commandPacket.getFullCommandDescription()
    // + " resulted in bad CRC value of " + crcFromRecord + " (expected " +
    // crcCalculated + ") "
    // + "(byte data = " + "<" + bitUtils.getHex(arrayData) + ">)");
    // }
    //
    // if (arrayData[0] != 167)
    // throw new PlugInBaseException("checkHeaderAndCRC: " +
    // commandPacket.getFullCommandDescription()
    // + " resulted in bad Type Code value of " +
    // bitUtils.getHex(arrayData[0]));
    //
    // // int ai1[] = packSerialNumber();
    // if (serialNumberBCD[0] != arrayData[1] || serialNumberBCD[1] !=
    // arrayData[2]
    // || serialNumberBCD[2] != arrayData[3])
    // throw new PlugInBaseException("checkHeaderAndCRC: " +
    // commandPacket.getFullCommandDescription()
    // + " resulted in bad serial number value of <" +
    // bitUtils.getHex(arrayData[1]) + " "
    // + bitUtils.getHex(arrayData[2]) + " " + bitUtils.getHex(arrayData[3]) +
    // ">");
    // else
    // return true;
    // }

}
