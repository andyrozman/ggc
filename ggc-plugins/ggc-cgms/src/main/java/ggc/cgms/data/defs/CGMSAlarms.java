package ggc.cgms.data.defs;

import java.util.Collection;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.cgms.util.DataAccessCGMS;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMAlarms  
 *  Description:  Alarms created by CGMS device
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum CGMSAlarms implements CodeEnumWithTranslation
{
    UnknownAlarm(0, "ALARM_UNKNOWN"), //

    BatteryLow(50, "ALARM_BATTERY_LOW"), //
    ReviewDateTime(51, "ALARM_REVIEW_DATETIME"), //
    AlarmClock(52, "ALARM_ALARM_CLOCK"), //
    CalibrationRequired(104, "ALARM_CALIBRATION_REQUIRED"), //
    WeakSignal(112, "ALARM_WEAK_SIGNAL"), //
    SensorAlarm(105, "ALARM_SENSOR_ALARM"), //
    CalibrationError(106, "ALARM_CALIBRATION_ERROR"), //
    SensorEndOfLifeAproaching(107, "ALARM_SENSOR_END_OF_LIFE"), //
    SensorEndOfLifeAproachingDescription(120, "ALARM_SENSOR_END_OF_LIFE_DESC"), //
    SensorError(109, "ALARM_SENSOR_ERROR"), //
    SensorChangeRequired(108, "ALARM_SENSOR_CHANGE_REQUIRED"), // deprecated
    SensorLost(113, "ALARM_SENSOR_SIGNAL_LOST"), //
    HighGlucosePredicted(114, "ALARM_HIGH_GLUCOSE_PREDICTED"), //
    LowGlucosePredicted(115, "ALARM_LOW_GLUCOSE_PREDICTED"), //
    HighGlucose(101, "ALARM_HIGH_GLUCOSE"), //
    LowGlucose(102, "ALARM_LOW_GLUCOSE"), //

    SessionExpired(120, ""), //
    CGMSFailure(121, ""), //
    SessionStopped(122, ""), //
    GlucoseRiseRateTooHigh(123, ""), //
    GlucoseFallRateTooHigh(124, ""), //

    ;

    static Hashtable<String, CGMSAlarms> translationMapping = new Hashtable<String, CGMSAlarms>();
    static Hashtable<Integer, CGMSAlarms> codeMapping = new Hashtable<Integer, CGMSAlarms>();


    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSAlarms pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;


    private CGMSAlarms(int code, String i18nKey)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }


    public String getName()
    {
        return this.name();
    }


    public static CGMSAlarms getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return CGMSAlarms.UnknownAlarm;
        }
    }


    public static Collection<CGMSAlarms> getAllValues()
    {
        return codeMapping.values();
    }

    // public CodeEnumWithTranslation[] values()
    // {
    // return values();
    // }

}
