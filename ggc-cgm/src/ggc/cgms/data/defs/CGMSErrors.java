package ggc.cgms.data.defs;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnum;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.cgms.util.DataAccessCGMS;

import java.util.Hashtable;

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
 *  Filename:     CGMErrors  
 *  Description:  Errors produced by CGMS device
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// IMPORTANT NOTICE:
// This class is not implemented yet, all existing methods should be rechecked
// (they were copied from similar
// class, with different type of data.

public enum CGMSErrors implements CodeEnumWithTranslation
{
    UnknownError(0, "ERROR_UNKNOWN_ERROR"), //
    BatteryDepleted(2, "ERROR_BATTERY_DEPLETED"), //
    AutomaticOff(3, "ERROR_AUTOMATIC_OFF"), //
    EndOfOperation(5, "ERROR_END_OF_OPERATION"), //
    MechanicalError(6, "ERROR_MECHANICAL_ERROR"), //
    ElectronicError(7, "ERROR_ELECTRONIC_ERROR"), //
    PowerInterrupt(12, "ERROR_POWER_INTERRUPT"), //
    LanguageError(13, "ERROR_LANGUAGE_ERROR"), //
    SensorCalibrationError(40, "ERROR_SENSOR_CALIB_ERROR"), //
    SensorEndOfLife(41, "ERROR_SENSOR_END_OF_LIFE"), //
    SensorBad(42, "ERROR_SENSOR_BAD"), //
    ;



    static Hashtable<String, CGMSErrors> translationMapping = new Hashtable<String, CGMSErrors>();
    static Hashtable<Integer, CGMSErrors> codeMapping = new Hashtable<Integer, CGMSErrors>();

    static
    {
        I18nControlAbstract ic = DataAccessCGMS.getInstance().getI18nControlInstance();

        for (CGMSErrors pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }
    }

    int code;
    String i18nKey;
    String translation;

    private CGMSErrors(int code, String i18nKey)
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

    public static CGMSErrors getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return CGMSErrors.UnknownError;
        }
    }





//    public static final int CGM_ERROR_UNKNOWN_ERROR = -1; // __________________________151
//    public static final int CGM_ERROR_BATTERY_DEPLETED = 2;// __________________________152
//    public static final int CGM_ERROR_AUTOMATIC_OFF = 3; // _____________________________152
//    public static final int CGM_ERROR_END_OF_OPERATION = 5; // __________________________154
//    public static final int CGM_ERROR_MECHANICAL_ERROR = 6; // _________________________155
//    public static final int CGM_ERROR_ELECTRONIC_ERROR = 7; // _________________________156
//    public static final int CGM_ERROR_POWER_INTERRUPT = 8; // __________________________157
//    public static final int CGM_ERROR_DATA_INTERRUPTED = 12; // _________________________159
//    public static final int CGM_ERROR_LANGUAGE_ERROR = 13; // __________________________160
//
//    // sensor
//
//    public static final int CGMS_ERROR_SENSOR_CALIBRATION_ERROR = 40; // ___________________160
//    public static final int CGMS_ERROR_SENSOR_END_OF_LIFE = 41; // ___________________160
//    public static final int CGMS_ERROR_SENSOR_BAD = 42; // ___________________160



}
