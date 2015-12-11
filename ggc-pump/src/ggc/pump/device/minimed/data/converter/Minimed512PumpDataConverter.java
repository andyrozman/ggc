package ggc.pump.device.minimed.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.RatioType;
import ggc.pump.data.dto.RatioDTO;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:     Minimed512PumpDataConverter
 *  Description:  Data converter class. This will decode Configuration of Pump device (for 512/712)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed512PumpDataConverter extends Minimed511PumpDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed512PumpDataConverter.class);


    public Minimed512PumpDataConverter(DataAccessPump dataAccess)
    {
        super(dataAccess);
    }


    @Override
    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {
            case Profile_STD: // 146
            case Profile_A: // 147
            case Profile_B: // 148
                debugConverterResponse(minimedReply);
                decodeProfile(minimedReply);
                break;

            case PumpModel: // 141
                debugConverterResponse(minimedReply);
                decodePumpModel(minimedReply);
                break;

            case Language: // 134
                debugConverterResponse(minimedReply);
                decodeLanguage(minimedReply);
                break;

            case Settings_512: // 145
                debugConverterResponse(minimedReply);
                decodeCurrentSettings(minimedReply);
                break;

            case HistoryData: // 128
                debugConverterResponse(minimedReply);
                historyDataConversionNote(LOG, MinimedCommandType.HistoryData);
                break;

            case RemainingInsulin: // 115
                debugConverterResponse(minimedReply);
                decodeRemainingInsulin(minimedReply);
                break;

            case SetTemporaryBasal:
                debugConverterResponse(minimedReply);
                this.decodeTempBasal(minimedReply);
                break;

            case BGUnits: // 137
                debugConverterResponse(minimedReply);
                decodeBGUnit(minimedReply);
                break;

            case BGReminderEnable: // 144
                debugConverterResponse(minimedReply);
                decodeEnableSetting("PCFG_BG_REMINDER_ENABLED", minimedReply, PumpConfigurationGroup.General);
                break;

            case BolusWizardSetupStatus: // 135
                debugConverterResponse(minimedReply);
                decodeEnableSetting("PCFG_BOLUS_WIZARD_ENABLED", minimedReply, PumpConfigurationGroup.Bolus);
                break;

            case CarbohydrateRatios: // 138
                debugConverterResponse(minimedReply);
                decodeCarbohydrateRatios(minimedReply);
                break;

            case InsulinSensitivities: // 139
                debugConverterResponse(minimedReply);
                decodeInsulinSensitivities(minimedReply);
                break;

            case BGTargets_512: // 140
                debugConverterResponse(minimedReply);
                decodeBGTargets(minimedReply);
                break;

            case BGAlarmClocks: // 142
                debugConverterResponse(minimedReply);
                decodeBGAlarmClocks(minimedReply); // FIXME
                break;

            case BGAlarmEnable: // 151
                debugConverterResponse(minimedReply);
                decodeEnableSetting("PCFG_BG_ALARM_ENABLED", minimedReply, PumpConfigurationGroup.General);
                break;

            case CarbohydrateUnits: // 136
                debugConverterResponse(minimedReply);
                decodeCarbohydrateUnits(minimedReply);
                break;

            default:
                super.convertData(minimedReply);
        }
    }


    private void decodeBGAlarmClocks(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        if (rd[0] == -1)
        {
            return;
        }

        for (int i = 0, index = 1; i < 16; i += 2, index++)
        {
            String time = DataAccessPlugInBase.getLeadingZero(rd[i], 2) + ":"
                    + DataAccessPlugInBase.getLeadingZero(rd[i + 1], 2);

            writeSetting(i18nControl.getMessage("PCFG_ALARM_CLOCK") + " #" + index, time, PumpConfigurationGroup.Other);
        }
    }


    private void decodeCarbohydrateUnits(MinimedCommandReply minimedReply)
    {
        String carbUnit = "CFG_BASE_CARBOHYDRATE_UNIT_GRAMS";

        int carbUnitInt = minimedReply.getRawDataAsInt(0);

        if (carbUnitInt == 2)
        {
            carbUnit = "CFG_BASE_CARBOHYDRATE_UNIT_EXCH";
        }
        else
        {
            if (carbUnitInt != 1)
            {
                LOG.warn("Unknown Carbohydrate Unit [" + carbUnitInt + "], defaulting to grams");
            }
        }

        writeSetting("CFG_BASE_CARBOHYDRATE_UNIT", getMessage(carbUnit), PumpConfigurationGroup.General);
    }


    /**
     * Decode Insulin Sensitivities
     */
    private void decodeInsulinSensitivities(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        int unit = rd[0];

        String unitName = unit == 1 ? //
                GlucoseUnitType.mg_dL.getTranslation()
                : //
                GlucoseUnitType.mmol_L.getTranslation();

        unitName += " / " + getMessage("CFG_BASE_UNIT_UNIT_SHORT");

        for (int i = 1, j = 1; i < rd.length; i += 2, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            RatioDTO ratioDTO = new RatioDTO(RatioType.InsulinBGRatio, j, //
                    getTimeFrom30MinInterval(rd[i]), //
                    unit == 1 ? rd[i + 1] : rd[i + 1] / 10.0f, //
                    unitName);

            writeSetting(ratioDTO.getType().getTranslation() + " #" + ratioDTO.getIndex(), ratioDTO.getSettingValue(),
                ratioDTO, PumpConfigurationGroup.Bolus);
        }
    }


    protected void decodeBGTargets(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        int unit = rd[0];

        String unitName = unit == 1 ? //
                GlucoseUnitType.mg_dL.getTranslation()
                : //
                GlucoseUnitType.mmol_L.getTranslation();

        for (int i = 1, j = 1; i < rd.length; i += 2, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            RatioDTO ratioDTO = new RatioDTO(RatioType.BGTarget, j, //
                    getTimeFrom30MinInterval(rd[i]), //
                    unit == 1 ? rd[i + 1] : rd[i + 1] / 10.0f, unitName);

            writeSetting(ratioDTO.getType().getTranslation() + " #" + ratioDTO.getIndex(), ratioDTO.getSettingValue(),
                ratioDTO, PumpConfigurationGroup.Bolus);
        }
    }


    private void decodeCarbohydrateRatios(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        int unit = rd[0];

        String unitName = unit == 1 ? //
                getMessage("CFG_BASE_CARBOHYDRATE_UNIT_GRAMS_SHORT") + "/" + //
                        getMessage("CFG_BASE_UNIT_UNIT_SHORT")
                : //
                getMessage("CFG_BASE_CARBOHYDRATE_UNIT_EXCH_SHORT") + "/" + //
                        getMessage("CFG_BASE_UNIT_UNIT_SHORT");

        for (int i = 1, j = 1; j <= rd.length; i += 2, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            RatioDTO ratioDTO = new RatioDTO(RatioType.InsulinCHRatio, j, //
                    getTimeFrom30MinInterval(rd[i]), //
                    rd[i + 1], unitName);

            writeSetting(ratioDTO.getType().getTranslation() + " #" + ratioDTO.getIndex(), ratioDTO.getSettingValue(),
                ratioDTO, PumpConfigurationGroup.Bolus);
        }
    }


    private void decodeBGUnit(MinimedCommandReply minimedReply)
    {
        GlucoseUnitType glucoseUnitType = GlucoseUnitType.mmol_L;

        int num = minimedReply.getRawDataAsInt(0);

        if (num == 1)
        {
            glucoseUnitType = GlucoseUnitType.mg_dL;
        }
        else
        {
            if (num != 2)
            {
                LOG.warn("Unknown BgUnit [" + num + "], defaulting to mmol/L");
            }
        }

        MinimedUtil.setGlucoseUnitType(glucoseUnitType);

        writeSetting("CFG_BASE_GLUCOSE_UNIT", glucoseUnitType.getTranslation(), PumpConfigurationGroup.General);
    }


    private void decodeLanguage(MinimedCommandReply minimedReply)
    {
        int langId = minimedReply.getRawData()[0];

        String languageKey = null;

        switch (langId)
        {
            case 0:
                languageKey = "LANGUAGE_ENGLISH";
                break;

            case 1:
                languageKey = "LANGUAGE_FRENCH";
                break;

            case 2:
                languageKey = "LANGUAGE_SPANISH";
                break;

            case 3:
                languageKey = "LANGUAGE_DUTCH";
                break;

            case 4:
                languageKey = "LANGUAGE_SWEDISH";
                break;

            case 5:
                languageKey = "LANGUAGE_GERMAN";
                break;

            case 6:
                languageKey = "LANGUAGE_ITALIAN";
                break;

            case 8:
                languageKey = "LANGUAGE_CZECH";
                break;

            case 9:
                languageKey = "LANGUAGE_DANISH";
                break;

            case 10:
                languageKey = "LANGUAGE_FINNISH";
                break;

            case 11:
                languageKey = "LANGUAGE_HUNGARIAN";
                break;

            case 12:
                languageKey = "LANGUAGE_NORVEGIAN";
                break;

            case 13:
                languageKey = "LANGUAGE_POLISH";
                break;

            case 14:
                languageKey = "LANGUAGE_PORTUGESE";
                break;

            case 15:
                languageKey = "LANGUAGE_SLOVENE";
                break;

            case 18:
                languageKey = "LANGUAGE_TURKISH";
                break;

            default:
                break;
        }

        String lang;

        if (languageKey == null)
        {
            lang = String.format(getMessage("LANGUAGE_UNKNOWN"), langId);
        }
        else
        {
            lang = getMessage(languageKey);
        }

        writeSetting("CFG_BASE_LANGUAGE", lang, PumpConfigurationGroup.General);

    }


    private void decodePumpModel(MinimedCommandReply minimedReply)
    {
        this.writeSetting("PCFG_PUMP_MODEL", this.stringWithElementCount(minimedReply), PumpConfigurationGroup.Device);
    }


    @Override
    public float getStrokesPerUnit(PumpBaseType baseType)
    {
        return baseType == PumpBaseType.Basal ? 40.0f : 10.0f;
    }


    @Override
    public void decodeCurrentSettings(MinimedCommandReply minimedReply)
    {
        super.decodeCurrentSettings(minimedReply);

        byte rd[] = minimedReply.getRawData();

        writeSetting("PCFG_TEMP_BASAL_TYPE",
            rd[14] != 0 ? getMessage("PCFG_TEMP_BASAL_TYPE_PERCENT") : getMessage("PCFG_TEMP_BASAL_TYPE_UNITS"),
            PumpConfigurationGroup.Basal);

        if (rd[14] == 1)
        {
            writeSetting("PCFG_TEMP_BASAL_PERCENT", "" + rd[15], PumpConfigurationGroup.Basal);
        }

        writeSetting("CFG_PARADIGM_LINK_ENABLE", parseResultEnable(rd[16]), PumpConfigurationGroup.General);

        decodeInsulinActionSetting(rd);
    }


    public void decodeInsulinActionSetting(byte ai[])
    {
        writeSetting("PCFG_INSULIN_ACTION_TYPE", (ai[17] != 0 ? getMessage("PCFG_INSULIN_ACTION_TYPE_REGULAR")
                : getMessage("PCFG_INSULIN_ACTION_TYPE_FAST")),
            PumpConfigurationGroup.Insulin);
    }


    public int decodeTempBasal(MinimedCommandReply minimedReply)
    {
        writeSetting("PCFG_TEMP_BASAL_RATE", "" + decodeBasalInsulin(minimedReply.getRawDataBytesAsUnsignedShort(2, 3)),
            PumpConfigurationGroup.Basal);
        int duration = minimedReply.getRawDataBytesAsUnsignedShort(4, 5);
        writeSetting("PCFG_TEMP_BASAL_DURATION", "" + duration, PumpConfigurationGroup.Basal);

        return duration;
    }

}
