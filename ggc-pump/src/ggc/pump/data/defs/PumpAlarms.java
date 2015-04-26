package ggc.pump.data.defs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccess;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.pump.util.DataAccessPump;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the aFree Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename:     PumpAlarms
 * Description:  Enum for Pump Alarms
 *
 * Author: Andy {andy@atech-software.com}
 */

public enum PumpAlarms implements CodeEnumWithTranslation
{

    UnknownAlarm(0, "ALARM_UNKNOWN"), //
    CartridgeLow(1, "ALARM_CARTRIDGE_LOW"), //
    BatteryLow(2, "ALARM_BATTERY_LOW"), //
    BatteryReplace(11, "ALARM_REPLACE_BATTERY"), //
    ReviewDatetime(3, "ALARM_REVIEW_DATETIME"), //
    AlarmClock(4, "ALARM_ALARM_CLOCK"), //
    PumpTimer(5, "ALARM_PUMP_TIMER"), //
    TemporaryBasalRateCanceled(6, "ALARM_TEMPORARY_BASAL_RATE_CANCELED"), //
    TemporaryBasalRateOver(7, "ALARM_TEMPORARY_BASAL_RATE_OVER"), //
    BolusCanceled(8, "ALARM_BOLUS_CANCELED"), //
    NoDelivery(10, "ALARM_NO_DELIVERY"), //
    EmptyCartridge(12, "ALARM_EMPTY_CARTRIDGE"), //
    AutoOff(13, "ALARM_AUTO_OFF"), //
    CallService(14, "ALARM_CALL_SERVICE"), //
    ;

    int code;
    String i18nKey;
    String translation;


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public String getName()
    {
        return this.name();
    }

    static Map<Integer, PumpAlarms> alarmCodeMapping = new HashMap<Integer, PumpAlarms>();
    static Map<String, CodeEnumWithTranslation> translationMap = new HashMap<String, CodeEnumWithTranslation>();
    static String[] alarm_desc;

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpAlarms pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMap.put(pbt.getTranslation(), pbt);
            alarmCodeMapping.put(pbt.code, pbt);
        }

        String[] alarm_desc_lcl = { ic.getMessage("SELECT_SUBTYPE"), //
                                   ic.getMessage("ALARM_CARTRIDGE_LOW"), //
                                   ic.getMessage("ALARM_BATTERY_LOW"), //
                                   ic.getMessage("ALARM_REPLACE_BATTERY"), //
                                   ic.getMessage("ALARM_REVIEW_DATETIME"), //
                                   ic.getMessage("ALARM_ALARM_CLOCK"),//
                                   ic.getMessage("ALARM_PUMP_TIMER"),//
                                   ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"),//
                                   ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"),//
                                   ic.getMessage("ALARM_BOLUS_CANCELED"),//
                                   ic.getMessage("ALARM_NO_DELIVERY"),//
                                   ic.getMessage("ALARM_EMPTY_CARTRIDGE"),//
                                   ic.getMessage("ALARM_AUTO_OFF"),//
                                   ic.getMessage("ALARM_CALL_SERVICE"),//
        };

        alarm_desc = alarm_desc_lcl;

    }


    private PumpAlarms(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public static PumpAlarms getByCode(int code)
    {
        if (alarmCodeMapping.containsKey(code))
        {
            return alarmCodeMapping.get(code);
        }
        else
        {
            return PumpAlarms.UnknownAlarm;
        }
    }


    public static String[] getDescriptions()
    {
        return alarm_desc;
    }


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public static int getTypeFromDescription(String str)
    {
        return ATDataAccess.getTypeFromDescription(str, translationMap);
    }


    public static Collection<PumpAlarms> getAllValues()
    {
        return alarmCodeMapping.values();
    }

}
