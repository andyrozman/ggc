package ggc.pump.device.minimed.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
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
 *  Filename:     Minimed515PumpDataConverter
 *  Description:  Data converter class. This will decode Configuration of Pump device (for 515/715)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed515PumpDataConverter extends Minimed512PumpDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed515PumpDataConverter.class);


    public Minimed515PumpDataConverter(DataAccessPump dataAccess)
    {
        super(dataAccess);
    }


    @Override
    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {
            case PumpStatus: // 206
                debugConverterResponse(minimedReply);
                decodePumpStatus("PCFG_PUMP_STATUS", minimedReply);
                break;

            case Settings: // 192
                debugConverterResponse(minimedReply);
                decodeCurrentSettings(minimedReply);
                break;

            case BGTargets: // 159
                debugConverterResponse(minimedReply);
                decodeBGTargets(minimedReply);
                break;

            case CarbohydrateRatios: // 138
                debugConverterResponse(minimedReply);
                decodeCarbohydrateRatios(minimedReply);
                break;

            case MissedBolusReminderEnable: // 197
                debugConverterResponse(minimedReply);
                decodeEnableSetting("PCFG_MISSED_BOLUS_REMINDER_ENABLED", minimedReply, PumpConfigurationGroup.Bolus);
                break;

            case MissedBolusReminders: // 198
                debugConverterResponse(minimedReply);
                decodeMissedBolusReminder(minimedReply);
                break;

            default:
                super.convertData(minimedReply);
        }
    }


    public void decodeCurrentSettings(MinimedCommandReply minimedReply)
    {
        super.decodeCurrentSettings(minimedReply);

        LOG.debug("convertCurrentSettings");

        writeSetting("PCFG_MM_RESERVOIR_WARNING_TYPE_TIME",
            minimedReply.getRawDataAsInt(18) != 0 ? "PCFG_MM_RESERVOIR_WARNING_TYPE_TIME"
                    : "PCFG_MM_RESERVOIR_WARNING_TYPE_UNITS", PumpConfigurationGroup.Other);

        writeSetting("PCFG_MM_SRESERVOIR_WARNING_POINT", "" + minimedReply.getRawDataAsInt(19),
            PumpConfigurationGroup.Other);

        decodeEnableSetting("CFG_MM_KEYPAD_LOCKED", minimedReply, 20, PumpConfigurationGroup.Other);

    }


    @Override
    protected void decodeRemainingInsulin(MinimedCommandReply minimedReply)
    {
        double value = decodeBasalInsulin(minimedReply.getRawDataBytesAsUnsignedShort(2, 3));
        this.writeSetting("PCFG_REMAINING_INSULIN", basalInsulinFormat(value), value, PumpConfigurationGroup.Insulin);
    }


    private void decodeMissedBolusReminder(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        // 00 1E 08 1E FF FF FF FF FF FF FF FF FF FF FF FF 01 00 0A 00 FF FF FF
        // FF FF FF FF FF FF FF FF FF 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        // 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
        // 0 30 8 30 255 255 255 255 255 255 255 255 255 255 255 255 1 0 10 0
        // 255 255 255 255 255 255 255 255 255 255 255 255 0 0 0 0 0 0 0 0 0 0 0
        // 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0

        // 0 start *by 2
        // 16

        String start, end;
        String template = "%s - %s";
        String key = getMessage("PCFG_MISSED_BOLUS_REMINDER");

        for (int i = 0, j = 1; i < 16; i += 2, j++)
        {
            if (rd[i] == 255)
            {
                break;
            }

            start = ATDataAccessAbstract.getLeadingZero(rd[i], 2) + ":"
                    + ATDataAccessAbstract.getLeadingZero(rd[i + 1], 2);
            end = ATDataAccessAbstract.getLeadingZero(rd[i + 16], 2) + ":"
                    + ATDataAccessAbstract.getLeadingZero(rd[i + 17], 2);

            writeSetting(key + " #" + j, String.format(template, start, end), PumpConfigurationGroup.Bolus);

        }
    }


    @Override
    public void decodeInsulinActionSetting(byte ai[])
    {
        int i = ai[17];
        String s = "";

        if ((i == 0) || (i == 1))
        {
            s = (ai[17] != 0 ? getMessage("PCFG_INSULIN_ACTION_TYPE_REGULAR")
                    : getMessage("PCFG_INSULIN_ACTION_TYPE_FAST"));
        }
        else
        {
            if (i == 15)
                s = getMessage("PCFG_INSULIN_ACTION_TYPE_UNSET");
            else
                s = String.format(getMessage("PCFG_INSULIN_ACTION_CURVE"), i);
        }

        writeSetting("PCFG_INSULIN_ACTION_TYPE", s, PumpConfigurationGroup.Insulin);
    }


    @Override
    protected void decodeBGTargets(MinimedCommandReply minimedReply)
    {

        byte[] rd = minimedReply.getRawData();

        int unit = rd[0];

        String unitName = unit == 1 ? //
        GlucoseUnitType.mg_dL.getTranslation()
                : //
                GlucoseUnitType.mmol_L.getTranslation();

        for (int i = 1, j = 1; i < rd.length; i += 3, j++)
        {
            if (rd[i] == 0 && rd[i + 1] == 0)
            {
                break;
            }

            RatioDTO ratioDTO = new RatioDTO(RatioType.BGTargetRange, j, getTimeFrom30MinInterval(rd[i]), //
                    unit == 1 ? rd[i + 1] : rd[i + 1] / 10.0f, //
                    unit == 1 ? rd[i + 2] : rd[i + 2] / 10.0f, unitName);

            writeSetting(ratioDTO.getType().getTranslation() + " #" + ratioDTO.getIndex(), ratioDTO.getSettingValue(),
                ratioDTO, PumpConfigurationGroup.Bolus);
        }
    }


    private void decodeCarbohydrateRatios(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        // 01 03 00 00 64 1B 00 96 25 00 82 00
        // 1 3 0 0 100 27 0 150 37 0 130 0

        // [01 04 00 00 64 1B 00 96 25 00 82 27 07 08 00 00
        // 1 4 0 0 100 27 0 150 37 0 130 39 7 8 0 0 0 0

        int unit = rd[0];

        String unitName = unit == 1 ? //
        getMessage("CFG_BASE_CARBOHYDRATE_UNIT_GRAMS_SHORT") + "/" + //
                getMessage("CFG_BASE_UNIT_UNIT_SHORT")
                : //
                getMessage("CFG_BASE_CARBOHYDRATE_UNIT_EXCH_SHORT") + "/" + //
                        getMessage("CFG_BASE_UNIT_UNIT_SHORT");

        for (int i = 2, j = 1; j <= rd[1]; i += 3, j++)
        {
            RatioDTO ratioDTO = new RatioDTO(RatioType.InsulinCHRatio, j, //
                    getTimeFrom30MinInterval(rd[i]), //
                    this.bitUtils.toInt(rd[i + 1], rd[i + 2]), unitName);

            writeSetting(ratioDTO.getType().getTranslation() + " #" + ratioDTO.getIndex(), ratioDTO.getSettingValue(),
                ratioDTO, PumpConfigurationGroup.Bolus);
        }
    }


    protected void decodeProfile(MinimedCommandReply minimedReply)
    {
        byte rep[] = minimedReply.getRawData();

        String profile = getProfileName(minimedReply);

        String key = getMessage("PCFG_BASAL_PROFILE") + " " + profile;

        writeSetting(key + " " + i18nControl.getMessage("CFG_BASE_NAME"), profile, profile,
            PumpConfigurationGroup.Basal);

        if ((rep.length >= 3) && (rep[2] == 0x3F))
        {
            String i18value = i18nControl.getMessage("NOT_SET");
            writeSetting(key, i18value, i18value, PumpConfigurationGroup.Basal);
            return;
        }

        int time_x;
        double vald;

        for (int i = 0; i < rep.length; i += 3)
        {
            // int val = this.bitUtils.makeUnsignedShort(rep[i + 1], rep[i]);
            vald = decodeBasalInsulin(minimedReply.getRawDataBytesAsUnsignedShort(rep[i + 1], rep[i]));

            time_x = rep[i + 2]; // Integer.parseInt("" + rep[i+2], 16);

            ATechDate atd = getTimeFrom30MinInterval(time_x);

            if (i != 0)
            {
                if (time_x == 0)
                {
                    break;
                }
                // else
                // {
                // // sb_coded.append(";");
                // // sb_display.append("\n");
                // }
            }

            String value = i18nControl.getMessage("CFG_BASE_FROM") + "=" + atd.getTimeString() + ", "
                    + i18nControl.getMessage("CFG_BASE_AMOUNT") + "=" + vald;

            writeSetting(key, value, value, PumpConfigurationGroup.Basal);

        }

    }

}
