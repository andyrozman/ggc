package ggc.pump.data.defs;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpErrors  
 *  Description:  Pump Errors 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public enum PumpErrors implements CodeEnumWithTranslation
{

    UnknownError(0, "ERROR_UNKNOWN_ERROR"), //
    CartridgeEmpty(1, "ERROR_CARTRIDGE_EMPTY"), //
    BatteryDepleted(2, "ERROR_BATTERY_DEPLETED"), //

    AutomaticOff(3, "ERROR_AUTOMATIC_OFF"), //
    NoDeliveryOcclusion(4, "ERROR_NO_DELIVERY"), //

    EndOfOperation(5, "ERROR_END_OF_OPERATION"), //
    MechanicalError(6, "ERROR_MECHANICAL_ERROR"), //
    ElectronicError(7, "ERROR_ELECTRONIC_ERROR"), //
    PowerInterrupt(8, "ERROR_POWER_INTERRUPT"), //
    CartridgeError(10, "ERROR_CARTRIDGE_ERROR"), //
    SetNotPrimed(11, "ERROR_SET_NOT_PRIMED"), //
    DataInterrupted(12, "ERROR_DATA_INTERRUPTED"), //
    LanguageError(13, "ERROR_LANGUAGE_ERROR"), //
    InsulinChanged(14, "ERROR_INSULIN_CHANGED"), //

    ;

    /**
     * Errors Description
     */
    private static String[] errors_desc = null;

    static Map<String, CodeEnumWithTranslation> translationMapping = new HashMap<String, CodeEnumWithTranslation>();
    static Map<Integer, PumpErrors> codeMapping = new HashMap<Integer, PumpErrors>();

    static
    {
        I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

        for (PumpErrors pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            translationMapping.put(pbt.getTranslation(), pbt);
            codeMapping.put(pbt.code, pbt);
        }

        String[] errors_desc_lcl = { ic.getMessage("SELECT_SUBTYPE"), //
                                    ic.getMessage("ERROR_CARTRIDGE_EMPTY"), //
                                    ic.getMessage("ERROR_BATTERY_DEPLETED"), //
                                    ic.getMessage("ERROR_AUTOMATIC_OFF"), //
                                    ic.getMessage("ERROR_NO_DELIVERY"), //
                                    ic.getMessage("ERROR_END_OF_OPERATION"), //
                                    ic.getMessage("ERROR_MECHANICAL_ERROR"), //
                                    ic.getMessage("ERROR_ELECTRONIC_ERROR"), //
                                    ic.getMessage("ERROR_POWER_INTERRUPT"), //
                                    ic.getMessage("ERROR_CARTRIDGE_ERROR"), //
                                    ic.getMessage("ERROR_SET_NOT_PRIMED"), //
                                    ic.getMessage("ERROR_DATA_INTERRUPTED"), //
                                    ic.getMessage("ERROR_LANGUAGE_ERROR"), //
                                    ic.getMessage("ERROR_INSULIN_CHANGED"), };

        errors_desc = errors_desc_lcl;
    }

    int code;
    String i18nKey;
    String translation;


    private PumpErrors(int code, String i18nKey)
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
    public static int getTypeFromDescription(String str)
    {
        return ATDataAccessAbstract.getTypeFromDescription(str, translationMapping);
    }


    public static PumpErrors getByCode(int code)
    {
        if (codeMapping.containsKey(code))
        {
            return codeMapping.get(code);
        }
        else
        {
            return PumpErrors.UnknownError;
        }
    }


    /**
     * Get Descriptions (array)
     *
     * @return array of strings with description
     */
    public static String[] getDescriptions()
    {
        return errors_desc;
    }

    // public static final int PUMP_ERROR_UNKNOWN_ERROR = 0; //
    // __________________________151
    // public static final int PUMP_ERROR_CARTRIDGE_EMPTY = 1; //
    // __________________________151
    // public static final int PUMP_ERROR_BATTERY_DEPLETED = 2;//
    // __________________________152
    // public static final int PUMP_ERROR_AUTOMATIC_OFF = 3; //
    // _____________________________152
    // public static final int PUMP_ERROR_NO_DELIVERY = 4; // minimed 'No
    // Delivery'=4, roche 'Occlusion'=4
    // public static final int PUMP_ERROR_END_OF_OPERATION = 5; //
    // __________________________154
    // public static final int PUMP_ERROR_MECHANICAL_ERROR = 6; //
    // _________________________155
    // public static final int PUMP_ERROR_ELECTRONIC_ERROR = 7; //
    // _________________________156
    // public static final int PUMP_ERROR_POWER_INTERRUPT = 8; //
    // __________________________157
    // public static final int PUMP_ERROR_CARTRIDGE_ERROR = 10; //
    // _________________________158
    // public static final int PUMP_ERROR_SET_NOT_PRIMED = 11; //
    // ___________________________158
    // public static final int PUMP_ERROR_DATA_INTERRUPTED = 12; //
    // _________________________159
    // public static final int PUMP_ERROR_LANGUAGE_ERROR = 13; //
    // __________________________160
    // public static final int PUMP_ERROR_INSULIN_CHANGED = 14; //
    // __________________________

}
