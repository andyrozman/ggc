package ggc.pump.data.defs;

import ggc.pump.util.DataAccessPump;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

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
    BatteryReplace(11, ""), //
    ReviewDatetime(3, "ALARM_REVIEW_DATETIME"), //
    AlarmClock(4, "ALARM_ALARM_CLOCK"), //
    PumpTimer(5, "ALARM_PUMP_TIMER"), //
    TemporaryBasalRateCanceled(6, "ALARM_TEMPORARY_BASAL_RATE_CANCELED"), //
    TemporaryBasalRateOver(7, "ALARM_TEMPORARY_BASAL_RATE_OVER"), //
    BolusCanceled(8, "ALARM_BOLUS_CANCELED"), //
    NoDelivery(10, "ALARM_NO_DELIVERY"), //
    EmptyCartridge(12, ""), //
    AutoOff(13, ""), //
    CallService(14, ""), //
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

    static Map<Integer, PumpAlarms> alarmCodeMapping = new HashMap<Integer, PumpAlarms>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpAlarms pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            alarmCodeMapping.put(pbt.code, pbt);
        }
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

    public static Map<Integer, PumpAlarms> getDescriptions()
    {
        return alarmCodeMapping;
    }

    /*
     * Minimed No Delivery (4) Sensor Alert: High Glucose (101) Sensor Alert:
     * Low Glucose (102) Sensor Alert: Meter BG Now (104) Sensor Alarm (105)
     * Sensor Alert: Calibration Error (106) Sensor Alert: Sensor End (107)
     * Sensor Alert: Change Sensor (108) Sensor Alert: Sensor Error (109) Sensor
     * Alert: Weak Signal (112) Sensor Alert: Lost Sensor (113) Sensor Alert:
     * High Glucose Predicted (114) Sensor Alert: Low Glucose Predicted (115)
     */

}
