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
 *  Filename:     CGMEvents
 *  Description:  CGMS device events
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// IMPORTANT NOTICE:
// This class is not implemented yet, all existing methods should be rechecked
// (they were copied from similar
// class, with different type of data.

public enum CGMSEvents implements CodeEnumWithTranslation
{

    UnknownEvent(0, "EVENT_UNKNOWN"), //

    ControllerPowerDown(1, "EVENT_CONTROLLER_POWER_DOWN"), //
    ControllerPowerUp(2, "EVENT_CONTROLLER_POWER_UP"), //
    DateTimeSet(3, "EVENT_DATETIME_SET"), //
    DateTimeChanged(4, "EVENT_DATETIME_CORRECT"), //

    // sensor basic functions

    // SensorPowerDown(40), // deprecated
    // SensorPowerUp(41), // deprecated
    SensorConnectionLost(42, "EVENT_SENSOR_CONNECTION_LOST"), //
    SensorStart(43, "EVENT_START_SENSOR"), //
    SensorStop(44, "EVENT_STOP_SENSOR"), //
    SensorCalibrationWithMeter(50, "EVENT_CALIBRATION_WITH_METER"), //
    SensorWaitingForCalibration(51, "EVENT_WAITING_FOR_CALIBRATION"), //
    SensorSetCalibrationFactor(52, "EVENT_SET_CALIBRATION_FACTOR"), //

    // sensor init/operation
    SensorPreInit(60, "EVENT_SENSOR_PRE_INIT"), SensorInit(61, "EVENT_SENSOR_INIT"), SensorBurst(62,
            "EVENT_SENSOR_BURST"), SensorWeakSignal(63, "EVENT_SENSOR_WEAK_SIGNAL"), //

    SensorDataLowBg(64, "EVENT_SENSOR_LOW_BG"), // deprecated
    SensorCalibrationWithMeterNow(70, "EVENT_SENSOR_CALIBRATION_WITH_METER_NOW")

    ;

    static Hashtable<String, CGMSEvents> translationMapping = new Hashtable<String, CGMSEvents>();
    static Hashtable<Integer, CGMSEvents> codeMapping = new Hashtable<Integer, CGMSEvents>();

    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSEvents pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;


    private CGMSEvents(int code, String i18nKey)
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


    /**
     * Get Type from Description
     *
     * @param str
     *            type as string
     * @return type as int
     */
    public int getTypeFromDescription(String str)
    {
        if (translationMapping.containsKey(str))
        {
            return translationMapping.get(str).getCode();
        }
        else
        {
            return 0;
        }
    }


    public static CGMSEvents getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return CGMSEvents.UnknownEvent;
        }
    }


    public static Collection<CGMSEvents> getAllValues()
    {
        return codeMapping.values();
    }

    // // start / end
    // public static final int CGMS_EVENT_CONTROLER_POWER_DOWN = 1;
    // public static final int CGMS_EVENT_CONTROLER_POWER_UP = 2;
    //
    // // date/time
    // public static final int CGMS_EVENT_DATETIME_SET = 3;
    // public static final int CGMS_EVENT_DATETIME_CHANGED = 4;
    //
    // // SENSOR - basic functions
    // public static final int CGMS_EVENT_SENSOR_POWER_DOWN = 40;
    // public static final int CGMS_EVENT_SENSOR_POWER_UP = 41;
    // public static final int CGMS_EVENT_SENSOR_LOST = 42;
    // public static final int CGMS_EVENT_SENSOR_START = 43;
    // public static final int CGMS_EVENT_SENSOR_STOP = 44;
    // public static final int CGMS_EVENT_SENSOR_CALIBRATION_METER_BG_NOW = 50;
    // public static final int CGMS_EVENT_SENSOR_CALIBRATION_WAITING = 51;
    // public static final int CGMS_EVENT_SENSOR_CALIBRATION_FACTOR = 52;
    //
    // // sensor init/operation
    //
    // public static final int CGMS_EVENT_SENSOR_PRE_INIT = 60;
    // public static final int CGMS_EVENT_SENSOR_INIT = 61;
    // public static final int CGMS_EVENT_SENSOR_BURST = 62;
    // public static final int CGMS_EVENT_SENSOR_WEAK_SIGNAL = 63;
    //
    // // Data
    // public static final int CGMS_EVENT_DATA_LOW_BG = 64;

}
